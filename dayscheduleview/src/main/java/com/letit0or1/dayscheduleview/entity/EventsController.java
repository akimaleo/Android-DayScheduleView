package com.letit0or1.dayscheduleview.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by akimaleo on 06.09.16.
 */
public class EventsController {
    private List<DrawableEvent> mDventsList;

    public EventsController(List<DrawableEvent> mDventsList) {
        if (mDventsList != null)
            Collections.sort(mDventsList);
        this.mDventsList = mDventsList;
    }

    public List<DrawableEvent> getmDventsList() {
        if (mDventsList == null) mDventsList = new ArrayList<>();
        Collections.sort(mDventsList);
        return mDventsList;
    }

    public void setmDventsList(List<DrawableEvent> mDventsList) {
        Collections.sort(mDventsList);
        this.mDventsList = mDventsList;
    }
}
