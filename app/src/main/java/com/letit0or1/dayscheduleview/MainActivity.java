package com.letit0or1.dayscheduleview;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.letit0or1.dayscheduleview.events.DrawableEvent;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DayScheduleView view = (DayScheduleView) findViewById(R.id.view);
        ArrayList<DrawableEvent> testE = new ArrayList<DrawableEvent>();

        testE.add(new DrawableEvent(new Time(0, 10), new Time(1, 0)));
        testE.add(new DrawableEvent(new Time(0, 20), new Time(2, 0)));
        testE.add(new DrawableEvent(new Time(2, 30), new Time(4, 0)));
        testE.add(new DrawableEvent(new Time(3, 30), new Time(7, 0)));
        testE.add(new DrawableEvent(new Time(7, 10), new Time(8, 0)));

        testE.get(2).setRectPaint(new Paint());
        testE.get(2).getRectPaint().setColor(Color.argb(120,255,255,255));
        testE.get(2).setTextPaint(new Paint());
        testE.get(2).getTextPaint().setColor(Color.BLACK);
        testE.get(2).getTextPaint().setTextSize(30);

        view.setEvents(testE);
    }
}
