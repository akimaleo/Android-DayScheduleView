package com.letit0or1.dayscheduleview.handler;

import com.letit0or1.dayscheduleview.Time;
import com.letit0or1.dayscheduleview.entity.DrawableEvent;

/**
 * Created by akimaleo on 13.09.16.
 */
public interface OnClickListener {
    void onSingleTap(int x, int y, Time touchTime, DrawableEvent e);

    void onLongTap(int x, int y, Time touchTime, DrawableEvent e);
}
