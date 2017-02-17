package com.letit0or1.dayscheduleview.entity;


/**
 * Created by akimaleo on 06.09.16.
 */
public class EventUtil {

    /*public static void groupEvents(ArrayList<DrawableEvent> events) {
        int groupId = 0;
        for (int i = 0; i < events.size(); i++) {
            for (int j = i + 1; i < events.size(); j++) {

                if (isOverlapping(events.get(i), events.get(j))) {
                    if (events.get(i).getGroup() != null) {
                        events.get(j).setGroup(events.get(i).getGroup());
                        events.get(j).getGroup().setCount(events.get(j).getGroup().getCount() + 1);
                    } else if (events.get(j).getGroup() != null) {
                        events.get(i).setGroup(events.get(j).getGroup());
                        events.get(j).getGroup().setCount(events.get(j).getGroup().getCount() + 1);
                    } else {
                        EventsGroup group = new EventsGroup(groupId++, 2);
                        events.get(i).setGroup(group);
                        events.get(j).setGroup(group);
                    }
                }
            }
        }
    }

    public int overlapCount(DrawableEvent e, ArrayList<DrawableEvent> events) {
        int count = 0;
        for (DrawableEvent i : events) {
            if (isOverlapping(e, i)) count++;
        }
        return count;
    }

    public static ArrayList<DrawableEvent> getOverlappingEventsTo(DrawableEvent e, ArrayList<DrawableEvent> events) {
        ArrayList<DrawableEvent> over = new ArrayList<>();
        over.add(e);
        for (DrawableEvent i : events) {
            if (isOverlapping(e, i)) over.add(i);
        }
        return over;
    }
*/

    public static boolean inRange(float value, float min, float max) {
        return (value > min) && (value < max);
    }

    public static boolean isOverlapping(DrawableEvent event0, DrawableEvent event1) {
        long
                start0 = event0.getEvent().getTimeFrom().getTime(),
                end0 = event0.getEvent().getTimeTo().getTime(),
                start1 = event1.getEvent().getTimeFrom().getTime(),
                end1 = event1.getEvent().getTimeTo().getTime();

        if (inRange(start0, start1, end1) || inRange(end0, start1, end1))
            return true;

        return false;
    }

}
