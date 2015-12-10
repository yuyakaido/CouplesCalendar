package com.yuyakaido.android.couplescalendar.sample;

import com.yuyakaido.android.couplescalendar.model.CouplesCalendarEvent;

import java.util.Date;

/**
 * Created by yuyakaido on 9/12/15.
 */
public class SampleEvent implements CouplesCalendarEvent {

    private Date mStartAt;
    private Date mEndAt;
    private String mRRule;
    private int mEventColor;

    public void setStartAt(Date startAt) {
        mStartAt = startAt;
    }

    @Override
    public Date getStartAt() {
        return mStartAt;
    }

    public void setEndAt(Date endAt) {
        mEndAt = endAt;
    }

    @Override
    public Date getEndAt() {
        return mEndAt;
    }

    public void setRecurrenceRule(String recurrenceRule) {
        mRRule = recurrenceRule;
    }

    @Override
    public String getRecurrenceRule() {
        return mRRule;
    }

    public void setEventColor(int eventColor) {
        mEventColor = eventColor;
    }

    @Override
    public int getEventColor() {
        return mEventColor;
    }

}
