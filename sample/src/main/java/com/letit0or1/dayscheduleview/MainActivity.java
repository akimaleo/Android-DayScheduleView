package com.letit0or1.dayscheduleview;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;

import com.letit0or1.dayscheduleview.entity.DrawableEvent;
import com.letit0or1.dayscheduleview.entity.Event;
import com.letit0or1.dayscheduleview.handler.OnClickListener;

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
        view.setOnClickListener(new OnClickListener() {

            private DrawableEvent event;
            private long duration = 2 * 60 * 60 * 1000;

            private Paint editable;
            private Paint current;

            @Override
            public void onSingleTap(int x, int y, Time touchTime, ArrayList<DrawableEvent> e) {
                init();
                if (e.size() == 0) {
                    if (event == null) {
                        event = view.createEvent(new Event(touchTime, new Time(touchTime.getTime() + duration)), false);
                    }
                    event.setEditable(false);
                    event.setRectPaint(current);
                }
            }

            @Override
            public void onLongTap(int x, int y, Time touchTime, ArrayList<DrawableEvent> e) {
                init();
                if (e.contains(event)) {
                    event.setEditable(true);
                    event.setResizable(false);
                    event.setRectPaint(editable);
                }
            }

            void init() {
                if (editable == null) {
                    editable = new Paint();
                    current = new Paint();

                    editable.setColor(Color.BLUE);
                    current.setColor(Color.WHITE);
                }
                view.invalidate();
            }
        });

    }

    void addTestEvents() {
        ArrayList<DrawableEvent> testE = new ArrayList<DrawableEvent>();
        testE.add(new DrawableEvent(new Time(0, 0), new Time(1, 0)));
        testE.add(new DrawableEvent(new Time(0, 1), new Time(3, 50)));
        testE.add(new DrawableEvent(new Time(0, 20), new Time(2, 0)));
        testE.add(new DrawableEvent(new Time(2, 30), new Time(4, 0)));
        testE.add(new DrawableEvent(new Time(3, 30), new Time(7, 0)));
        testE.add(new DrawableEvent(new Time(7, 10), new Time(8, 0)));
        testE.add(new DrawableEvent(new Time(15, 10), new Time(18, 0)));
        testE.add(new DrawableEvent(new Time(17, 10), new Time(20, 0)));
        view.setEvents(testE);
    }
}
