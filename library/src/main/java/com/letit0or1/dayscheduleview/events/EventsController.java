package com.letit0or1.dayscheduleview.events;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by akimaleo on 06.09.16.
 */
public class EventsController {
    private List<DrawableEvent> events;

    public EventsController(List<DrawableEvent> events) {
        if (events != null)
            Collections.sort(events);
        this.events = events;
    }

    public List<DrawableEvent> getEvents() {
        if (events != null)
            Collections.sort(events);
        return events;
    }

    public void setEvents(List<DrawableEvent> events) {
        Collections.sort(events);
        this.events = events;
    }
}
