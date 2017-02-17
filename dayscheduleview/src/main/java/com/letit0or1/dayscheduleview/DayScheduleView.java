package com.letit0or1.dayscheduleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.letit0or1.dayscheduleview.entity.DrawableEvent;
import com.letit0or1.dayscheduleview.entity.Event;
import com.letit0or1.dayscheduleview.entity.EventUtil;
import com.letit0or1.dayscheduleview.entity.EventsController;
import com.letit0or1.dayscheduleview.handler.EditEvent;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DayScheduleView extends View {

    private static String TAG = "DayScheduleView";

    private EventsController eventsController;

    //STYLE VARIABLE
    private Paint paintSeparator, paintTimeText, paintRectangle, paintTextRect;
    private float hourSpacingHeight, maxHourHeight, minHourHeight, separatorHeight, scroll, hourMarginLeft, separatorMarginLeft, separatorPaddingRight, eventMarginLeft, eventMarginRight;
    private int timeHourFrom, timeHourTo;


    //Zoom gesture detector
    private ScaleGestureDetector mGestureScaleDetector;
    private ScaleGestureDetector.OnScaleGestureListener mScaleListener = new ScaleGestureDetector.OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float scale = scaleGestureDetector.getScaleFactor();

            if (scale > 1 && getStep() >= maxHourHeight)
                return false;

            float hScalePx = (scale * hourSpacingHeight) - hourSpacingHeight;

            float yFocus = scaleGestureDetector.getFocusY();
            Time focusTime = getTimeByPx((int) yFocus);

            Time difTime = new Time(focusTime.getTime() - new Time(timeHourFrom, 0).getTime());
            scroll -= difTime.getHour() * hScalePx;
            setHourSpacingHeight(scale * hourSpacingHeight);

            //VALIDATE
            if (scroll > 0)
                scroll = 0;
            if (getDrawViewHeight() <= getHeight()) {
                scroll = 0;
                setHourSpacingHeight(getHeight() / (timeHourTo - timeHourFrom) - separatorHeight);
            } else if (getDrawViewEnd() < getHeight()) {
                scroll -= getDrawViewEnd() - getHeight();
            }
            invalidate();
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
    private com.letit0or1.dayscheduleview.handler.OnClickListener onClickListener;

    //Gesture detector
    private GestureDetector mGestureDetector;
    private EditEvent mEditEvent;
    private final GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            float x = e.getX(), y = e.getY();
            onClickListener.onSingleTap((int) x, (int) y, getTimeByPx((int) y), getDrawableEvent(x, y));
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!canScroll)
                return false;
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

        public void onLongPress(MotionEvent e) {
            int x = (int) e.getX(), y = (int) e.getY();
            onClickListener.onLongTap((int) x, (int) y, getTimeByPx((int) y), getDrawableEvent(x, y));
        }

    };

    public void setEditable(DrawableEvent event) {
        if (eventsController.getmDventsList().contains(event)) {
            event.setEditable(true);
        }
    }

    private volatile boolean drawTimeLine;
    public boolean canScroll = true;

    //Constructors
    public DayScheduleView(Context context) {
        super(context);

        //Initialize common variable
        separatorHeight = 50f;
        maxHourHeight = 200;
        minHourHeight = 20;
        hourSpacingHeight = (maxHourHeight + minHourHeight) / 2;

        //Paint for separator
        paintSeparator = new Paint();
        paintSeparator.setColor(Color.GRAY);

        //Paint for hour text
        paintTimeText = new Paint();
        paintTimeText.setTextSize(minHourHeight);
        paintTimeText.setColor(Color.BLACK);

        //Paint for rect text
        paintTextRect = new Paint();
        paintTextRect.setColor(Color.WHITE);
    }

    public DayScheduleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DayScheduleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        //ATTRIBUTES
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.DayScheduleView);
        mEditEvent = new EditEvent(this);
        onClickListener = new com.letit0or1.dayscheduleview.handler.OnClickListener() {
            @Override
            public void onSingleTap(int x, int y, Time touchTime, ArrayList<DrawableEvent> e) {

            }

            @Override
            public void onLongTap(int x, int y, Time touchTime, ArrayList<DrawableEvent> e) {

            }
        };

        drawTimeLine = attributes.getBoolean(R.styleable.DayScheduleView_timeLine, false);
        if (drawTimeLine) {
            Thread moveTimeLine = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (drawTimeLine) {
                        synchronized (this) {
                            try {
                                wait(20000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            DayScheduleView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    invalidate();
                                }
                            });
                        }
                    }
                }
            });
            moveTimeLine.start();
        }

        //TIME HOUR FROM TO
        timeHourFrom = attributes.getInt(R.styleable.DayScheduleView_timeHourFrom, 0);
        timeHourTo = attributes.getInt(R.styleable.DayScheduleView_timeHourTo, 24);

        //SEPARATOR
        separatorHeight = attributes.getDimension(R.styleable.DayScheduleView_separatorHeight, 2);
        paintSeparator = new Paint();
        paintSeparator.setColor(attributes.getColor(R.styleable.DayScheduleView_separatorColor, Color.GRAY));
        paintSeparator.setStrokeWidth(attributes.getDimension(R.styleable.DayScheduleView_separatorHeight, 2));
        separatorPaddingRight = attributes.getDimension(R.styleable.DayScheduleView_separatorPaddingRight, 10);
        separatorMarginLeft = attributes.getDimension(R.styleable.DayScheduleView_separatorPaddingLeft, 10);

        //DIMENSION
        minHourHeight = attributes.getDimension(R.styleable.DayScheduleView_hourFieldsMinHeight, 20);
        maxHourHeight = attributes.getDimension(R.styleable.DayScheduleView_hourFieldsMaxHeight, 500);
        hourSpacingHeight = attributes.getDimension(R.styleable.DayScheduleView_hourFieldsHeight, (minHourHeight + maxHourHeight) / 2);

        //TEXT
        paintTimeText = new Paint();
        paintTimeText.setColor(attributes.getColor(R.styleable.DayScheduleView_timeTextColor, Color.GRAY));
        paintTimeText.setTextSize(attributes.getDimension(R.styleable.DayScheduleView_timeTextSize, 50));
        hourMarginLeft = attributes.getDimension(R.styleable.DayScheduleView_hourMarginLeft, 40);

        //Guest detector
        mGestureDetector = new GestureDetector(getContext(), mGestureListener);
        mGestureScaleDetector = new ScaleGestureDetector(getContext(), mScaleListener);

        //EVENTS
        eventsController = new EventsController(null);
        paintRectangle = new Paint();
        paintRectangle.setColor(attributes.getColor(R.styleable.DayScheduleView_eventBackgroundColor, Color.argb(200, 150, 150, 150)));
        paintRectangle.setStyle(Paint.Style.FILL);
        eventMarginLeft = attributes.getDimension(R.styleable.DayScheduleView_evenMarginLeft, 150);
        eventMarginRight = attributes.getDimension(R.styleable.DayScheduleView_evenMarginRight, 10);
        //paintRectangle.setShadowLayer(10.0f, 0.0f, 2.0f, Color.LTGRAY);
        //paintRectangle.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.SOLID));

        //Paint for rect text
        paintTextRect = new Paint();
        paintTextRect.setColor(attributes.getColor(R.styleable.DayScheduleView_eventTextColor, Color.argb(128, 240, 240, 240)));
        paintTextRect.setTextSize(attributes.getDimension(R.styleable.DayScheduleView_eventTextSize, 30));

        //setBackgroundColor(attributes.getColor(R.styleable.Da, Color.rgb(244, 244, 244)));
        attributes.recycle();
    }

    //TOUCH EVENT HANDLER
    @Override
    public boolean dispatchTouchEvent(MotionEvent mEvent) {

        mGestureDetector.onTouchEvent(mEvent);
        mGestureScaleDetector.onTouchEvent(mEvent);
        mEditEvent.dispatchEdit(mEvent);

        invalidate();

        return true;
    }

    //DRAW
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSepAndHours(canvas);
        drawEvents(canvas);

        if (drawTimeLine)
            drawTimeLine(canvas);
    }

    private void drawTimeLine(Canvas c) {

        Calendar cdr = Calendar.getInstance();
        Time curTime = new Time(cdr.get(Calendar.HOUR_OF_DAY), cdr.get(Calendar.MINUTE));
        float y = getPxByTime(curTime);

        Paint timeLine = new Paint();
        timeLine.setColor(Color.RED);
        c.drawLine(0, y - separatorHeight / 2, c.getWidth(), y + separatorHeight / 2, timeLine);
    }

    private void drawSepAndHours(Canvas canvas) {

        for (int i = timeHourFrom; i < timeHourTo; i++) {

            int j = i - timeHourFrom;
            float yPos = j * getStep() + scroll;
            if (yPos > getDrawViewHeight())
                break;

            canvas.drawLine(
                    separatorMarginLeft,
                    yPos,
                    canvas.getWidth() - separatorPaddingRight,
                    j * getStep() + scroll,
                    paintSeparator);

            Rect bounds = new Rect();
            paintTimeText.getTextBounds("00:00", 0, 1, bounds);

            canvas.drawText(i + ":00",
                    hourMarginLeft,
                    yPos + paintTimeText.getTextSize() / 2 + hourSpacingHeight / 2,
                    paintTimeText);

        }

    }

    public void drawEvents(Canvas canvas) {

        if (eventsController.getmDventsList() != null) {
            ArrayList<DrawableEvent> filtered = filterEventByTime((ArrayList<DrawableEvent>) eventsController.getmDventsList());
            calculateDrawType(filtered);

            float
                    textLen = rectTimeTextWidth(),
                    marginLeftRect = getRectMarginLeft();
            for (DrawableEvent i : filtered) {

                i.setRect(
                        new RectF(
                                marginLeftRect + textLen * (i.getOverlapIndex() - 1),
                                getPxByTime(i.getEvent().getTimeFrom()),
                                getWidth() - separatorPaddingRight - eventMarginRight,
                                getPxByTime(i.getEvent().getTimeTo())));


                //DRAWING RECTANGLE TEXT
                canvas.drawRect(i.getRect(), i.getRectPaint() == null ? paintRectangle : i.getRectPaint());
                canvas.drawText(
                        i.getEvent().getTimeFrom() + "-" + i.getEvent().getTimeTo(),
                        marginLeftRect + textLen * (i.getOverlapIndex() - 1),
                        getPxByTime(i.getEvent().getTimeFrom()) + paintTextRect.getTextSize(),
                        i.getTextPaint() == null ? paintTextRect : i.getTextPaint());

                //DRAW RECT RESIZABLE BUTTON
                if (i.isResizable()) {

                }
            }
        }
    }

    public DrawableEvent createEvent(Event e, boolean isEditable) {
        DrawableEvent event = new DrawableEvent(e);
        event.setEditable(isEditable);
        eventsController.getmDventsList().add(event);
        invalidate();
        return event;
    }

    //ADDITIONAL PART
    private ArrayList<DrawableEvent> filterEventByTime(ArrayList<DrawableEvent> toFilter) {

        ArrayList<DrawableEvent> filter = new ArrayList<>();

        for (int i = 0; i < toFilter.size(); i++) {
//            long q = toFilter.get(i).getEvent().getTimeFrom().getTime(), w = new Time(timeHourTo, 0).getTime();
//            if (toFilter.get(i).getEvent().getTimeFrom().getTime() > new Time(timeHourTo, 0).getTime())
//                continue;
//            if (toFilter.get(i).getEvent().getTimeTo().getTime() < new Time(timeHourFrom, 0).getTime())
//                continue;

            if (
                    !(toFilter.get(i).getEvent().getTimeFrom().getTime() > new Time(timeHourTo, 0).getTime()) &&
                            !(toFilter.get(i).getEvent().getTimeTo().getTime() < new Time(timeHourFrom, 0).getTime()))
                filter.add(toFilter.get(i));
        }
        return filter;
    }

    public float getDrawViewHeight() {
        return (timeHourTo - timeHourFrom) * (separatorHeight + hourSpacingHeight);
    }

    private float rectTimeTextWidth() {
        Rect bounds = new Rect();
        paintTimeText.getTextBounds("00:00", 0, 5, bounds);
//
//        return bounds.width();
        return paintTimeText.measureText("00:00-00:00") - ((int) paintTimeText.getTextSize() >> 1);
    }

    private float getRectMarginLeft() {
        return paintTimeText.measureText("00:00") + hourMarginLeft + eventMarginLeft;
    }

    private void calculateDrawType(List<DrawableEvent> events) {
        for (int i = 0; i < events.size(); i++)
            events.get(i).setOverlapIndex(1);

        for (int i = 0; i < events.size(); i++) {

            int j = i + 1;

            if (j < events.size()) {

                boolean isLabelsAreOverlapping = isOverlapTimeLabel(events.get(i), events.get(j));

                if (isLabelsAreOverlapping) {

                    int iOverlapIndex = events.get(i).getOverlapIndex();

                    events.get(j).setOverlapIndex(++iOverlapIndex);

                    for (int k = 1; k < iOverlapIndex; k++) {
                        events.get(j - k).setOverlapCount(iOverlapIndex);
                    }
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

        return isOver;
    }


    //takes a own time class and returns height in pixels
    private float getPxByTime(Time time) {
        float minutesInPx = (getStep() / 60) * time.getMinute();
        float hoursInPx = getStep() * (time.getHour() - timeHourFrom);
        return hoursInPx + minutesInPx + scroll;
    }

    //takes a pixels and returns height in
    public Time getTimeByPx(int px) {
        float pxInMin = getStep() / 60;
        float minInPx = 60 / getStep();
        Time a = new Time((long) ((minInPx * (px + Math.abs(scroll))) * 60 * 1000));
        return a;
    }


    //GET:SET
    public float getDrawViewEnd() {
        return getDrawViewHeight() + scroll;
    }

    public float getDrawViewStart() {
        return scroll + getStep();
    }

    private float getStep() {
        return hourSpacingHeight + separatorHeight;
    }


    public void setDrawHeight(float height) {

        float cut = height / (timeHourTo - timeHourFrom) - ((timeHourTo - timeHourFrom) * separatorHeight);
        setHourSpacingHeight(hourSpacingHeight - cut);

        cut = height / (timeHourTo - timeHourFrom) - (((timeHourTo - timeHourFrom) * hourSpacingHeight));
        setSeparatorHeight(separatorHeight - cut);
    }

    public void setScroll(float scroll) {
        this.scroll = scroll;
    }

    //Hours
    public void setHourSpacingHeight(float height) {
        hourSpacingHeight = Math.max(minHourHeight, Math.min(height, maxHourHeight));
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

    public void setEvents(List<DrawableEvent> events) {
        eventsController.setmDventsList(events);
    }

    public void setRectangleTextSize(float size) {
        paintTextRect.setTextSize(size);
    }

    public float getRectangleTextSize() {
        return paintTimeText.getTextSize();
    }

    public int touchIndex(float x, float y) {
        int touchTypeIndex = 0;
        float
                textLen = rectTimeTextWidth(),
                marginLeftRect = getRectMarginLeft();

        for (int i = 1; i < 6; i++)
            if (x < marginLeftRect + textLen * i) {
                touchTypeIndex = i - 1;
                break;
            }
        return touchTypeIndex;
    }

    public ArrayList<DrawableEvent> getDrawableEvent(float x, float y) {
        List<DrawableEvent> events = eventsController.getmDventsList();

        //filter for overlapping events by Y
        List<DrawableEvent> hoverEvents = new ArrayList<>();
        for (DrawableEvent i : events) {
            if (hoverClickByY(i, x, y)) {
                hoverEvents.add(i);
            }

        }
        return (ArrayList<DrawableEvent>) hoverEvents;
//        int touchTypeIndex = touchIndex(x, y);
//
//        if (hoverEvents.size() > 0)
//            return hoverEvents.get(hoverEvents.size() - 1);
//        else return null;
    }

    boolean hoverClickByY(DrawableEvent event, float x, float y) {
        Time
                start0 = event.getEvent().getTimeFrom(),
                end0 = event.getEvent().getTimeTo();

        return EventUtil.inRange(y, getPxByTime(start0), getPxByTime(end0)) && event.getRect().left < x;
    }

    public void setPaintSeparator(Paint paintSeparator) {
        this.paintSeparator = paintSeparator;
    }

    public void setOnClickListener(com.letit0or1.dayscheduleview.handler.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public EventsController getEventsController() {
        return eventsController;
    }

    public Paint getPaintSeparator() {
        return paintSeparator;
    }

    public Paint getPaintRectangle() {
        return paintRectangle;
    }

    public void setPaintRectangle(Paint paintRectangle) {

        this.paintRectangle = paintRectangle;
    }

    public void setTimeHourTo(int timeHourTo) {
        this.timeHourTo = timeHourTo;
        invalidate();
    }

    public void setTimeHourFrom(int timeHourFrom) {
        this.timeHourFrom = timeHourFrom;
        invalidate();
    }

    public int getTimeHourFrom() {
        return timeHourFrom;
    }

    public int getTimeHourTo() {
        return timeHourTo;
    }

    private boolean isEditable() {
        for (DrawableEvent i : getEventsController().getmDventsList()) {
            if (i.isEditable()) {
                return true;
            }
        }
        return false;
    }
}
