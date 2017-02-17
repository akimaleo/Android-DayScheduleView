package com.letit0or1.dayscheduleview.handler;

import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;

import com.letit0or1.dayscheduleview.DayScheduleView;
import com.letit0or1.dayscheduleview.entity.DrawableEvent;
import com.letit0or1.dayscheduleview.entity.Event;

/**
 * Created by akimaleo on 11.01.17.
 */

public class EditEvent {
    private String LOG = "EditEvent";
    private DayScheduleView dView;
    private int mStartTouchY, mStartEventTop, mStartEventBottom;
    private DrawableEvent editable;

    public EditEvent(DayScheduleView view) {
        dView = view;
    }


    public void dispatchEdit(MotionEvent mEvent) {

//        if (!isEditable()) return;

        final int action = MotionEventCompat.getActionMasked(mEvent);


        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(mEvent);
                final float y = MotionEventCompat.getY(mEvent, pointerIndex);
                Log.i(LOG, "pointer down: y = " + y);
                mStartTouchY = (int) y;
                if ((editable != null && dView.getDrawableEvent(mEvent.getX(), mEvent.getY(0)).contains(editable))) {
                    dView.canScroll = false;
                    saveStartPosition();
                } else dView.canScroll = true;

                break;
            }

            case MotionEvent.ACTION_MOVE: {
                if (isEditable() && dView.getDrawableEvent(mEvent.getX(), mEvent.getY(0)).contains(editable)) {

                    if (mEvent.getPointerCount() > 1) return;

                    final float y = MotionEventCompat.getY(mEvent, 0);
                    // Calculate the distance moved

                    int dy = (int) (y - mStartTouchY);

                    Log.i("pointer move", "dy: " + dy);

                    RectF pos = editable.getRect();
                    pos.top = mStartEventTop + dy;
                    pos.bottom = mStartEventBottom + dy;
                    editable.setEvent(getEventByRect(pos));
                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                if (isEditable() && dView.getDrawableEvent(mEvent.getX(), mEvent.getY(0)).contains(editable)) {
//                    editable.setEditable(false);
                }
                break;
            }
        }
    }

    private boolean isEditable() {
        if (editable != null && editable.isEditable() == true) {
            return true;
        }
        for (DrawableEvent i : dView.getEventsController().getmDventsList()) {
            if (i.isEditable()) {
                editable = i;

                // Save the ID of this pointer (for dragging)
                saveStartPosition();

                editable.setEditable(true);
                return true;
            }
        }
        dView.canScroll = true;
        return false;
    }

    private Event getEventByRect(RectF r) {
        return new Event(dView.getTimeByPx((int) r.top), dView.getTimeByPx((int) r.bottom));
    }

    private void saveStartPosition() {
        RectF pos = editable.getRect();
        mStartEventTop = (int) pos.top;
        mStartEventBottom = (int) pos.bottom;
    }

}
