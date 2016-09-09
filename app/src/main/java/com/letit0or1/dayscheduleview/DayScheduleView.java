package com.letit0or1.dayscheduleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.letit0or1.dayscheduleview.events.DrawableEvent;
import com.letit0or1.dayscheduleview.events.EventsController;

import java.util.ArrayList;


public class DayScheduleView extends View {

    //private boolean isTwentyForHourStyle = true;
    private static String TAG = "DayScheduleView";

    private EventsController eventsController;
    private Paint paintSeparator, paintText, paintRectangle, paintTextRect;
    //private Color commonRectColor;
    private float hourHeight, maxHourHeight, minHourHeight, separatorHeight, scroll, scale, hourMarginLeft, separatorMarginLeft, separatorPaddingRight;

    //Zoom gesture detector
    private ScaleGestureDetector mGestureScaleDetector;
    private ScaleGestureDetector.OnScaleGestureListener mScaleListener = new ScaleGestureDetector.OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            zoom(scaleGestureDetector.getScaleFactor());
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

        }
    };

    //Gesture detector
    private GestureDetectorCompat mGestureDetector;
    private final GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            float x = e.getX(), y = e.getY();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //MOVE OUR LIST
            if (getDrawViewHeight() > getHeight()) {
                if (distanceY < 0) {
                    if (scroll < 0)
                        scroll -= distanceY;

                    //Prevent over scroll
                    if (scroll >= 0)
                        scroll = 0;
                } else {
                    if (getDrawViewEnd() > getHeight())
                        scroll -= distanceY;

                    //Prevent over scroll
                    if (getDrawViewEnd() <= getHeight())
                        scroll = getHeight() - getDrawViewHeight();
                }
                invalidate();
                return true;
            }
            return false;
        }
    };

    //Constructors
    public DayScheduleView(Context context) {
        super(context);

        //Initialize common variable
        separatorHeight = 50f;
        maxHourHeight = 200;
        minHourHeight = 20;
        hourHeight = (maxHourHeight + minHourHeight) / 2;

        //Paint for separator
        paintSeparator = new Paint();
        paintSeparator.setColor(Color.GRAY);

        //Paint for hour text
        paintText = new Paint();
        paintText.setTextSize(minHourHeight);
        paintText.setColor(Color.BLACK);

        //Paint for rect text
        paintTextRect = new Paint();
        paintTextRect.setColor(Color.WHITE);
    }

    public DayScheduleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DayScheduleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        //ATTRIBUTES
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.DayScheduleView);

        //SEPARATOR
        separatorHeight = attributes.getDimension(R.styleable.DayScheduleView_separatorHeight, 2);
        paintSeparator = new Paint();
        paintSeparator.setColor(attributes.getColor(R.styleable.DayScheduleView_separatorColor, Color.GRAY));
        paintSeparator.setStrokeWidth(attributes.getDimension(R.styleable.DayScheduleView_separatorHeight, 2));
        separatorPaddingRight = attributes.getDimension(R.styleable.DayScheduleView_separatorPaddingRight, 40);
        separatorMarginLeft = attributes.getDimension(R.styleable.DayScheduleView_separatorPaddingLeft, 40);


        //DIMENSION
        minHourHeight = 20;
        minHourHeight = attributes.getDimension(R.styleable.DayScheduleView_hourFieldsMinHeight, minHourHeight);
        maxHourHeight = 200;
        maxHourHeight = attributes.getDimension(R.styleable.DayScheduleView_hourFieldsMaxHeight, maxHourHeight);
        hourHeight = attributes.getDimension(R.styleable.DayScheduleView_hourFieldsHeight, (minHourHeight + maxHourHeight) / 2);

        //TEXT
        paintText = new Paint();
        paintText.setColor(attributes.getColor(R.styleable.DayScheduleView_android_textColor, Color.WHITE));
        paintText.setTextSize(attributes.getDimension(R.styleable.DayScheduleView_textSize, hourHeight / 3));
        hourMarginLeft = attributes.getDimension(R.styleable.DayScheduleView_hourMarginLeft, 40);

        //Guest detector
        mGestureDetector = new GestureDetectorCompat(getContext(), mGestureListener);
        mGestureScaleDetector = new ScaleGestureDetector(getContext(), mScaleListener);

        //EVENTS
        eventsController = new EventsController();
        paintRectangle = new Paint();
        paintRectangle.setARGB(64, 0, 148, 228);
        paintRectangle.setStyle(Paint.Style.FILL);
        paintRectangle.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.SOLID));

        //Paint for rect text
        paintTextRect = new Paint();
        paintTextRect.setColor(Color.WHITE);
        paintTextRect.setTextSize(24);

        setBackgroundColor(Color.rgb(22, 46, 61));
        attributes.recycle();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent mEvent) {

        mGestureDetector.onTouchEvent(mEvent);
        mGestureScaleDetector.onTouchEvent(mEvent);

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSepAndHours(canvas);
        drawEvents(canvas);
    }

    public float getDrawViewHeight() {
        return 24 * (separatorHeight + hourHeight);
    }

    public void zoom(float scale) {
        setScroll(scroll * scale);
        setHourHeight(scale * hourHeight);
        if (getDrawViewHeight() <= getHeight()) {
            scroll = 0;
            setHourHeight(getHeight() / 24 - separatorHeight);
        } else if (getDrawViewEnd() < getHeight()) {
            scroll -= getDrawViewEnd() - getHeight();
        }
        invalidate();
    }

    public void drawEvents(Canvas canvas) {

        float rectMargin = hourMarginLeft + paintText.getTextSize() * 2 + 20;

        calculateDrawType(eventsController.getEvents());

        for (DrawableEvent i : eventsController.getEvents()) {
            float
                    rectWidth = (getWidth() - separatorPaddingRight - rectMargin) / (i.getOverlapIndex() + 1),
                    spaceTop = paintText.getTextSize() + separatorHeight * 2;

            i.setReady(true);
            i.setRect(
                    new RectF(
                            rectMargin * (i.getOverlapIndex() + 1),
                            getPxByTime(i.getEvent().getTimeFrom()),
                            getWidth() - separatorPaddingRight,
                            getPxByTime(i.getEvent().getTimeTo())));


            //DRAWING RECTANGLE TET
            canvas.drawRect(i.getRect(), i.getRectPaint() == null ? paintRectangle : i.getRectPaint());
            canvas.drawText(
                    i.getEvent().getTimeFrom() + "-" + i.getEvent().getTimeTo(),
                    rectMargin * (i.getOverlapIndex() + 1),
                    getPxByTime(i.getEvent().getTimeFrom()) + paintTextRect.getTextSize(),
                    i.getTextPaint() == null ? paintTextRect : i.getTextPaint());

        }
    }

    void calculateDrawType(ArrayList<DrawableEvent> events) {
        for (int i = 0; i < events.size(); i++) {

            int j = i + 1;

            if (j < events.size()) {

                boolean isLabelsAreOverlapping = isOverlapTimeLabel(events.get(i), events.get(j));

                if (isLabelsAreOverlapping) {

                    int iOverlapIndex = events.get(i).getOverlapIndex();
                    events.get(j).setOverlapIndex(iOverlapIndex + 1);

                }
            }
        }
    }

    //Is label or rectangle overlapping another label
    boolean isOverlapTimeLabel(DrawableEvent event0, DrawableEvent event1) {

        float
                pxEvent0 = getPxByTime(new Time(event0.getEvent().getTimeFrom().getHour(), event0.getEvent().getTimeFrom().getMinute())),
                pxEvent1 = getPxByTime(new Time(event1.getEvent().getTimeFrom().getHour(), event1.getEvent().getTimeFrom().getMinute())),
                textSize = paintTextRect.getTextSize();

        boolean isOver = pxEvent1 - pxEvent0 <= textSize;

        //Log.i("OVER PX ", (pxEvent1 - pxEvent0) + " || " + textSize + " : " + isOver);
        return isOver;
    }

    //takes a own time class and returns height in pixels
    private float getPxByTime(Time time) {
        float minutesInPx = (getStep() / 60) * time.getMinute();
        float hoursInPx = getStep() * time.getHour();

        return hoursInPx + minutesInPx + scroll;
    }

    //takes a pixels and returns height in
    private float getTimeByPx(int px) {
        float pxMin = getStep() / 60;
        return px / pxMin;
    }


    //GET:SET
    public float getDrawViewEnd() {
        return getDrawViewHeight() + scroll;
    }

    public float getDrawViewStart() {
        return scroll + getStep();
    }

    private float getStep() {
        return hourHeight + separatorHeight;
    }

    private void drawSepAndHours(Canvas canvas) {

        for (int i = 0; i < 24; i++) {
            canvas.drawLine(
                    separatorMarginLeft,
                    i * getStep() + scroll,
                    canvas.getWidth() - separatorPaddingRight,
                    i * getStep() + scroll,
                    paintSeparator);

            canvas.drawText(i + ":00",
                    hourMarginLeft,
                    i * getStep() + scroll + paintText.getTextSize(),
                    paintText);
        }

    }

    public void setDrawHeight(float height) {

        float cut = height / 24 - (24 * separatorHeight);
        setHourHeight(hourHeight - cut);

        cut = height / 24 - (24 * hourHeight);
        setSeparatorHeight(separatorHeight - cut);
    }

    public void setScroll(float scroll) {
        this.scroll = scroll;
    }

    //Hours
    public void setHourHeight(float height) {
        hourHeight = Math.max(minHourHeight, Math.min(height, maxHourHeight));
    }

    public void setHourMarginLeft(float hourMarginLeft) {
        this.hourMarginLeft = hourMarginLeft;
    }

    //Separator
    public void setSeparatorHeight(float height) {
        separatorHeight = height;
    }

    public void setSeparatorPaddingRight(float separatorPaddingRight) {
        this.separatorPaddingRight = separatorPaddingRight;
    }

    public void setSeparatorMarginLeft(float separatorMarginLeft) {
        this.separatorMarginLeft = separatorMarginLeft;
    }

    public void setEvents(ArrayList<DrawableEvent> events) {
        eventsController.setEvents(events);
    }

    public void setRectangleTextSize(float size) {
        paintTextRect.setTextSize(size);
    }

    public float getRectangleTextSize() {
        return paintText.getTextSize();
    }
}
