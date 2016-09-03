package com.letit0or1.dayscheduleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class DayScheduleView extends View {
    private static String TAG = "DayScheduleView";
/*
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                Log.d(DEBUG_TAG, "Action was DOWN");
                return true;
            case (MotionEvent.ACTION_MOVE):
                Log.d(DEBUG_TAG, "Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP):
                Log.d(DEBUG_TAG, "Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL):
                Log.d(DEBUG_TAG, "Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE):
                Log.d(DEBUG_TAG, "Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }*/

    public DayScheduleView(Context context) {
        super(context);
        init();
    }

    public DayScheduleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DayScheduleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private Color colorEvent;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);/*
        LinearLayout linLayout = new LinearLayout(getContext());
        // установим вертикальную ориентацию
        linLayout.setOrientation(LinearLayout.VERTICAL);
        linLayout.setBackgroundColor(Color.CYAN);
        // создаем LayoutParams
        LayoutParams linLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        linLayout.setLayoutParams(linLayoutParam);

        this.addView(linLayout);
        // устанавливаем linLayout как корневой элемент экрана*/
        //canvas.clipRect(0, 0, canvas.getWidth(), 5642);
        //canvas.drawColor(Color.CYAN);
        //drawHours(canvas);
        drawSeparators(canvas);
    }

    Point startTouch = new Point();

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        dumpEvent(event);
        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG");
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 10f) {
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    //MOVE OUR LIST
                    float touchDiff = event.getY() - start.y;

                    if (touchDiff > 0)
                        Log.d(TAG, "move down");
                    else Log.d(TAG, "move up");

                    if (getDrawViewHeight() > getHeight() && touchDiff < getDrawViewHeight())
                        scroll = touchDiff;
                    else {

                        if (getDrawViewHeight() > getHeight() && (touchDiff + getDrawViewHeight()) > getHeight())
                            scroll = touchDiff;
                    }
                    invalidate();
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 200f) {
                        float scale = newDist / oldDist;
                        if ((scale > 1 && hourWidth < maxHourWidth) || (scale < 1 && hourWidth > minHourWidth))
                            hourWidth *= scale;
                    }
                }
                break;
        }

        invalidate();

        return true;
    }


    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    public float getDrawViewHeight() {
        return 24 * (separatorHeight + hourWidth);
    }

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    private void dumpEvent(MotionEvent event) {
        String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }
        sb.append("]");
        Log.d(TAG, sb.toString());
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private void drawSeparators(Canvas canvas) {

        for (int i = 0; i < 23; i++)
            //canvas.clipRect(0, i * hourWidth + hourWidth, canvas.getWidth(), i * hourWidth + hourWidth + separatorHeight);
            canvas.drawRect(
                    0,
                    i * hourWidth + hourWidth + scroll,
                    canvas.getWidth(),
                    i * hourWidth + hourWidth + separatorHeight + scroll,
                    paint);

        //canvas.clipRect(0, 0 * hourWidth + hourWidth, canvas.getWidth(), 0 * hourWidth + hourWidth + separatorHeight);
        //canvas.drawColor(Color.BLACK);

            /*
            canvas.drawLine(0,
                    i * hourWidth,
                    canvas.getWidth(),
                    i * hourWidth,
                    new Paint(Paint.DITHER_FLAG));*/
    }

    private void drawHours(Canvas canvas) {
        canvas.clipRect(54, canvas.getHeight(), canvas.getHeight(), 5642);
        canvas.drawColor(Color.CYAN);
    }

    private Paint paint;
    private float hourWidth, minHourWidth, maxHourWidth, separatorHeight, scroll;

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        separatorHeight = 0.5f;
        maxHourWidth = 100;
        minHourWidth = 20;
        hourWidth = (maxHourWidth + minHourWidth) / 2;
    }
}
