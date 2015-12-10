package com.yuyakaido.android.couplescalendar.model;

import android.text.TextUtils;
import com.yuyakaido.android.couplescalendar.util.CalendarUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yuyakaido on 8/14/15.
 */
public class InternalEvent implements CouplesCalendarEvent {

    private DateTime mStartAt;
    private DateTime mEndAt;
    private String mRecurrenceRule;

    private LinePosition mLinePosition;
    private DurationType mDurationType;
    private EventType mEventType = EventType.NORMAL;
    private int mEventColor;

    @Override
    public Date getStartAt() {
        return mStartAt.toDate();
    }

    public DateTime getStartAtDateTime() {
        return mStartAt;
    }

    public DateTime getZonedStartAt() {
        return CalendarUtils.getZonedDateTime(mStartAt);
    }

    public void setStartAt(DateTime startAt) {
        mStartAt = startAt;
        setDurationType();
    }

    @Override
    public Date getEndAt() {
        return mEndAt.toDate();
    }

    public DateTime getEndAtDateTime() {
        return mEndAt;
    }

    public DateTime getZonedEndAt() {
        return CalendarUtils.getZonedDateTime(mEndAt);
    }

    public void setEndAt(DateTime endAt) {
        mEndAt = endAt;
        setDurationType();
    }

    public String getRecurrenceRule() {
        return mRecurrenceRule;
    }

    public void setRecurrenceRule(String recurrenceRule) {
        mRecurrenceRule = recurrenceRule;
        if (TextUtils.isEmpty(mRecurrenceRule)) {
            mEventType = EventType.NORMAL;
        } else {
            mEventType = EventType.RECURRING;
        }
    }

    public LinePosition getLinePosition() {
        return mLinePosition;
    }

    public void setLinePosition(LinePosition linePosition) {
        mLinePosition = linePosition;
    }

    public DurationType getDurationType() {
        return mDurationType;
    }

    public void setDurationType() {
        if (mStartAt != null && mEndAt != null) {
            Duration duration = new Duration(mStartAt, mEndAt);
            if (!CalendarUtils.isSameDay(mStartAt, mEndAt)
                    && CalendarUtils.isLongerThenTwelveHours(duration)) {
                mDurationType = DurationType.MULTIPLE_DAYS;
            } else {
                mDurationType = DurationType.ONE_DAY;
            }
        }
    }

    public EventType getEventType() {
        return mEventType;
    }

    public void setEventType(EventType eventType) {
        mEventType = eventType;
    }

    @Override
    public int getEventColor() {
        return mEventColor;
    }

    public void setEventColor(int eventColor) {
        mEventColor = eventColor;
    }

    /**
     * そのイベントが属している日付リストを返す
     * （例）
     * 2015-06-25 から 2015-06-26 までのイベントだった場合
     * 2015-06-25, 2015-06-26 が返る
     * @return
     */
    public List<DateTime> getEventDays() {
        List<DateTime> days = new ArrayList<>();
        DateTime startAtDateTime = CalendarUtils.getMidnight(getZonedStartAt());
        DateTime endAtDateTime = CalendarUtils.getMidnight(getZonedEndAt());
        days.add(startAtDateTime);
        if (CalendarUtils.isSameDay(startAtDateTime, endAtDateTime)) {
            return days;
        } else {
            while (!CalendarUtils.isEqualOrAfter(startAtDateTime, endAtDateTime)) {
                startAtDateTime = startAtDateTime.plusDays(1);
                days.add(startAtDateTime);
            }
            return days;
        }
    }

}
