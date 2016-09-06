package com.letit0or1.dayscheduleview;


import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by akimaleo on 06.09.16.
 */
public class Event implements Comparable<Event> {

    private Time timeFrom;
    private Time timeTo;
    private long duration;
    private boolean isConfirmed = false;
    private RectF rect;

    public Event(Time timeFrom, Time timeTo) {
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        duration = timeFrom.getTime() - timeTo.getTime();
    }

    public Event(Time timeFrom, long duration) {
        this.timeFrom = timeFrom;
        this.duration = duration;
        timeTo = new Time(timeFrom.getTime() + duration);
    }

    @Override
    public String toString() {
        return timeFrom.getTime() / 1000 / 60 / 60 + ":" + timeTo.getTime() / 1000 / 60;
    }

    @Override
    public int compareTo(Event event) {
        if (this.timeFrom.getTime() > event.timeFrom.getTime())
            return 1;
        else if (this.timeFrom.getTime() < event.timeFrom.getTime())
            return -1;
        return 0;
    }

    public Time getTimeFrom() {
        return timeFrom;
    }

    public Time getTimeTo() {
        return timeTo;
    }

    public long getDuration() {
        return duration;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }


    public void setRect(RectF rect) {
        this.rect = rect;
    }

    public RectF getRect() {

        return rect;
    }

}
