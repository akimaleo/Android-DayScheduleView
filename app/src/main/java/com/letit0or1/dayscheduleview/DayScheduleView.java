package com.letit0or1.dayscheduleview;

import android.content.Context;
import android.content.res.TypedArray;
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

import java.util.ArrayList;


public class DayScheduleView extends View {

    //private boolean isTwentyForHourStyle = true;
    private static String TAG = "DayScheduleView";

    private EventsController eventsController;
    private Paint paintSeparator, paintText, paintRectangle;
    private float hourHeight, maxHourHeight, minHourHeight, separatorHeight, scroll, scale, hourMarginLeft, separatorMarginLeft, separatorMarginRight;

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

        //Paint for text
        paintText = new Paint();
        paintText.setTextSize(minHourHeight);
        paintText.setColor(Color.BLACK);

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
        separatorMarginRight = attributes.getDimension(R.styleable.DayScheduleView_separatorMarginRight, 40);
        separatorMarginLeft = attributes.getDimension(R.styleable.DayScheduleView_separatorMarginRight, 40);


        //DIMENSION
        minHourHeight = 20;
        minHourHeight = attributes.getDimension(R.styleable.DayScheduleView_hourFieldsMinHeight, minHourHeight);
        maxHourHeight = 200;
        maxHourHeight = attributes.getDimension(R.styleable.DayScheduleView_hourFieldsMaxHeight, maxHourHeight);
        hourHeight = attributes.getDimension(R.styleable.DayScheduleView_hourFieldsHeight, (minHourHeight + maxHourHeight) / 2);

        //TEXT
        paintText = new Paint();
        paintText.setColor(attributes.getColor(R.styleable.DayScheduleView_android_textColor, Color.BLACK));
        paintText.setTextSize(attributes.getDimension(R.styleable.DayScheduleView_textSize, hourHeight / 3));
        hourMarginLeft = attributes.getDimension(R.styleable.DayScheduleView_hourMarginLeft, 40);

        //Guest detector
        mGestureDetector = new GestureDetectorCompat(getContext(), mGestureListener);
        mGestureScaleDetector = new ScaleGestureDetector(getContext(), mScaleListener);

        //EVENTS
        eventsController = new EventsController();
        paintRectangle = new Paint();
        paintRectangle.setColor(Color.CYAN);
        attributes.recycle();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        mGestureDetector.onTouchEvent(event);
        mGestureScaleDetector.onTouchEvent(event);

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
                    canvas.getWidth() - separatorMarginRight,
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

    public void setSeparatorMarginRight(float separatorMarginRight) {
        this.separatorMarginRight = separatorMarginRight;
    }

    public void setSeparatorMarginLeft(float separatorMarginLeft) {
        this.separatorMarginLeft = separatorMarginLeft;
    }


    public void setEvents(ArrayList<Event> events) {
        eventsController.setEvents(events);
    }

    public void drawEvents(Canvas canvas) {
        ArrayList<Event> testE = new ArrayList<Event>();
        testE.add(new Event(new Time(3, 20), new Time(4, 20)));
        testE.add(new Event(new Time(4, 00), new Time(4, 40)));
        eventsController.setEvents(testE);

        for (Event i : eventsController.getEvents()) {
            ArrayList<Event> events = ArethmLine.getOverlapingEventsTo(i, eventsController.getEvents());

            if (events.size() > 0)
                for (Event j : events) {
                    drawEvent(j, canvas);
                }
        }
    }

    private void drawEvent(Event event, Canvas canvas) {
        event.setConfirmed(true);
        event.setRect(
                new RectF(
                        hourMarginLeft + paintText.getTextSize() * 5 + 20,
                        getPxByTime(event.getTimeFrom()),
                        separatorMarginRight,
                        getPxByTime(event.getTimeTo())));

        canvas.drawRect(event.getRect(), paintRectangle);

    }

    private float getPxByTime(Time time) {
        float oneMinStep = time.getTime() / 1000 / 60 / getStep();
        return time.getTime() / oneMinStep;
    }

}
