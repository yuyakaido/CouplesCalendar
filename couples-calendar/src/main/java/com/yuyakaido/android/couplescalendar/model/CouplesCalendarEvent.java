package com.yuyakaido.android.couplescalendar.model;

/**
 * Created by yuyakaido on 6/18/15.
 */
public interface CouplesCalendarEvent {
    String getRecurrenceRule();
    java.util.Date getStartAt();
    java.util.Date getEndAt();
    int getEventColor();
}
