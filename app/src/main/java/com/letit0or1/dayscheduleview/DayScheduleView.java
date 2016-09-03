package com.letit0or1.dayscheduleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;


public class DayScheduleView extends View {

    private static String TAG = "DayScheduleView";

    private Paint paintSeparator, paintText;
    private float hourHeight, minHourHeight, maxHourHeight, separatorHeight, scroll, scale;

    private ScaleGestureDetector mGestureScaleDetector;
    private ScaleGestureDetector.OnScaleGestureListener mScaleListener = new ScaleGestureDetector.OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            setHourHeight(scaleGestureDetector.getScaleFactor() * hourHeight);
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

    private GestureDetectorCompat mGestureDetector;
    private final GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //MOVE OUR LIST
            if (getDrawViewHeight() > getHeight())
                if (distanceY < 0) {
                    //Log.d(TAG, "scroll down");
                    if (scroll < 0)
                        scroll -= distanceY;
                    else scroll = 0;
                } else {
                    //Log.d(TAG, "scroll up");
                    if (getDrawViewEnd() > getHeight())
                        scroll -= distanceY;
                    else scroll = getHeight()-getDrawViewHeight();
                }
            invalidate();
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    };


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

    public void setHourHeight(float height) {

        hourHeight = Math.max(minHourHeight, Math.min(height, maxHourHeight));
        invalidate();
    }

    private void init() {
        mGestureScaleDetector = new ScaleGestureDetector(getContext(), mScaleListener);
        mGestureDetector = new GestureDetectorCompat(getContext(), mGestureListener);

        paintSeparator = new Paint();
        paintSeparator.setColor(Color.GRAY);

        paintText = new Paint();
        //paintText.setTextSize(minHourHeight);
        paintText.setColor(Color.BLACK);

        separatorHeight = 0.5f;
        maxHourHeight = 100;
        minHourHeight = 20;
        hourHeight = (maxHourHeight + minHourHeight) / 2;


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSepAndHours(canvas);
        //Log.i(TAG,"View END: "+ getDrawViewEnd()+"\nView START: "+getDrawViewStart()+"\nView HEIGHT: " + getDrawViewHeight());
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        mGestureDetector.onTouchEvent(event);
        mGestureScaleDetector.onTouchEvent(event);

        return true;
    }

    public float getDrawViewHeight() {
        return 24 * (separatorHeight + hourHeight);
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
                    0,
                    i * getStep() + scroll,
                    canvas.getWidth(),
                    i * getStep() + scroll,
                    paintSeparator);

            canvas.drawText(i + "", 0, i * getStep() + scroll, paintText);
        }

    }

    private void drawHours(Canvas canvas) {
        canvas.clipRect(54, canvas.getHeight(), canvas.getHeight(), 5642);
        canvas.drawColor(Color.CYAN);
    }


}
