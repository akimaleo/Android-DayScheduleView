package com.letit0or1.dayscheduleview.events;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by akimaleo on 06.09.16.
 */
public class EventsController {
    private ArrayList<DrawableEvent> events;

    public ArrayList<DrawableEvent> getEvents() {
        Collections.sort(events);
        return events;
    }

    public void setEvents(ArrayList<DrawableEvent> events) {
        Collections.sort(events);

        this.events = events;
    }
}
