package com.letit0or1.dayscheduleview.entity;

import android.graphics.Paint;
import android.graphics.RectF;

import com.letit0or1.dayscheduleview.Time;

/**
 * Created by akimaleo on 07.09.16.
 */
public class DrawableEvent implements Comparable<DrawableEvent> {

    private Event event;
    private boolean isReady;
    private boolean isEditable =false;
    private int overlapCount = 1;
    private int overlapIndex = 1;
    private Float rectWidth;
    private RectF rect;
    private Paint textPaint;
    private Paint rectPaint;


    public DrawableEvent(Event event) {
        this.event = event;
    }

    public DrawableEvent(Time timeFrom, Time timeTo) {
        event = new Event(timeFrom, timeTo);
    }

    public void setRect(RectF rect) {
        this.rect = rect;
    }

    public RectF getRect() {
        return rect;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public void setOverlapCount(int overlapCount) {
        this.overlapCount = overlapCount;
    }

    public void setOverlapIndex(int overlapIndex) {
        this.overlapIndex = overlapIndex;
    }

    public Event getEvent() {
        return event;
    }

    public boolean isReady() {
        return isReady;
    }

    public int getOverlapCount() {
        return overlapCount;
    }

    public int getOverlapIndex() {
        return overlapIndex;
    }

    public void setRectWidth(Float rectWidth) {
        this.rectWidth = rectWidth;
    }

    public void setRectPaint(Paint rectPaint) {
        this.rectPaint = rectPaint;
    }

    public Float getRectWidth() {
        return rectWidth;
    }

    public Paint getRectPaint() {
        return rectPaint;
    }

    public void setTextPaint(Paint textPaint) {
        this.textPaint = textPaint;
    }

    public Paint getTextPaint() {
        return textPaint;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    @Override
    public int compareTo(DrawableEvent drawableEvent) {
        if (this.event.getTimeFrom().getTime() > drawableEvent.getEvent().getTimeFrom().getTime())
            return 1;
        else if (this.event.getTimeFrom().getTime() < drawableEvent.getEvent().getTimeFrom().getTime())
            return -1;
        return 0;
    }

    @Override
    public String toString() {
        return overlapCount + " " + event.toString();
    }
}
