package com.letit0or1.dayscheduleview;

import android.app.Activity;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.letit0or1.dayscheduleview.R;
import com.letit0or1.dayscheduleview.events.DrawableEvent;

import java.util.ArrayList;


public class MainActivity extends Activity {
    private DayScheduleView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (DayScheduleView) findViewById(R.id.dayScheduleView);
        addTestEvents();

        //Define variable paint for separator
        int dashSize = 8;
        Paint dashPaint = new Paint();
        dashPaint.setColor(Color.parseColor("#32404e"));
        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setStrokeWidth(dashSize);
        dashPaint.setPathEffect(new DashPathEffect(new float[]{5, dashSize}, 0));

        view.setPaintSeparator(dashPaint);


        //Define variable paint for separator
        /*Paint dashPaint = new Paint();
        dashPaint.setARGB(255, 255, 70, 200);
        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, 0));

        //Define variable paint for rectangle
        Paint paintRectangle = new Paint();
        paintRectangle.setColor(Color.argb(100, 50, 60, 255));
        //But, in Android Manifest.xml for blur effect, set
        //android:hardwareAccelerated="false"
        paintRectangle.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));

        //Apply changes for view
        view.setPaintSeparator(dashPaint);
        view.setPaintRectangle(paintRectangle);*/
    }

    void addTestEvents() {
        ArrayList<DrawableEvent> testE = new ArrayList<DrawableEvent>();
//        testE.add(new DrawableEvent(new Time(0, 0), new Time(1, 0)));
//        testE.add(new DrawableEvent(new Time(0, 1), new Time(1, 50)));
//        testE.add(new DrawableEvent(new Time(0, 20), new Time(2, 0)));
//        testE.add(new DrawableEvent(new Time(2, 30), new Time(4, 0)));
//        testE.add(new DrawableEvent(new Time(3, 30), new Time(7, 0)));
//        testE.add(new DrawableEvent(new Time(7, 10), new Time(8, 0)));
        testE.add(new DrawableEvent(new Time(15, 10), new Time(18, 0)));
        testE.add(new DrawableEvent(new Time(17, 10), new Time(20, 0)));
        view.setEvents(testE);
    }
}
