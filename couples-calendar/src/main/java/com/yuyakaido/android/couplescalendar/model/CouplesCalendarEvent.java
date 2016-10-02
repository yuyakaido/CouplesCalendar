package com.yuyakaido.android.couplescalendar.model;

/**
 * Created by yuyakaido on 6/18/15.
 */
public interface CouplesCalendarEvent {

    public enum LinePosition {
        UPPER, LOWER
    }

    public enum DurationType {
        ONE_DAY, MULTIPLE_DAYS
    }

    public enum EventType {
        NORMAL, RECURRING, TRANSIENT,
    }

    String getRecurrenceRule();
    java.util.Date getStartAt();
    java.util.Date getEndAt();
    int getEventColor();

}
