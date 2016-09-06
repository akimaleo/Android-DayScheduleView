package com.letit0or1.dayscheduleview;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

/**
 * Created by akimaleo on 06.09.16.
 */

public class Time {

    private long timeMillis;

    public Time(long time) {
        this.timeMillis = time;
    }

    public Time(int hour, int minute) {
        timeMillis = hour * 60 * 60 * 1000 + minute * 60 * 1000;
    }

    public int getHour() {
        return (int) (timeMillis / 1000 / 60 / 60);
    }

    public int getMinute() {
        return (int) (timeMillis / 1000 / 60) % 60;
    }

    public void setHour(int hour) {
        hour = hour * 1000 * 60 * 60;
        long min = (timeMillis / 1000 / 60) % 60;
        timeMillis %= (1000 * 60 * 60);
        timeMillis += hour;
    }

    public void setMinute(int minute) {
        minute = (minute * 60 * 1000) % (60 * (1000 * 60));
        long difMins = timeMillis % (60 * 1000*60);
        timeMillis -= difMins;
        timeMillis += minute;
    }

    public long getTime() {
        return timeMillis;
    }

    @Override
    public String toString() {
        return (getHour() < 10 ? "0" + getHour() : getHour()) + ":" + (getMinute() < 10 ? "0" + getMinute() : getMinute());
    }
}
