package com.letit0or1.dayscheduleview.entity;


import com.letit0or1.dayscheduleview.Time;

/**
 * Created by akimaleo on 06.09.16.
 */
public class Event implements Comparable<Event> {

    private Time timeFrom;
    private Time timeTo;
    private long duration;

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
        return timeFrom.toString() + ":" + timeTo.toString();
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


}
