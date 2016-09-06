package com.letit0or1.dayscheduleview;


import java.util.ArrayList;

/**
 * Created by akimaleo on 06.09.16.
 */
public class ArethmLine {

    private static boolean isOverlapping(Event event0, Event event1) {
        long
                start0 = event0.getTimeFrom().getTime(),
                end0 = event0.getTimeTo().getTime(),
                start1 = event1.getTimeFrom().getTime(),
                end1 = event1.getTimeTo().getTime();

        if ((inRange(start0, start1, end1) || inRange(end0, start1, end1))) {
            return true;
        }
        return false;
    }

    public int overlapCount(Event e, ArrayList<Event> events) {
        int count = 0;
        for (Event i : events) {
            if (isOverlapping(e, i)) count++;
        }
        return count;
    }

    public static ArrayList<Event> getOverlapingEventsTo(Event e, ArrayList<Event> events) {
        ArrayList<Event> over = new ArrayList<>();
        for (Event i : events) {
            if (isOverlapping(e, i)) over.add(i);
        }
        return over;
    }

    private static boolean inRange(float value, float min, float max) {
        return (value > min) && (value < max);
    }
}
