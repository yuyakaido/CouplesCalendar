package com.yuyakaido.android.couplescalendar.model;

import org.joda.time.DateTime;

/**
 * Created by yuyakaido on 6/8/15.
 */
public class Date {

    private DateTime mDateTime;
    private boolean mIsEmptyDate;

    public DateTime getDateTime() {
        return mDateTime;
    }

    public void setDateTime(DateTime dateTime) {
        mDateTime = dateTime;
    }

    public boolean isEmptyDate() {
        return mIsEmptyDate;
    }

    public void setIsEmptyDate(boolean isEmptyDate) {
        mIsEmptyDate = isEmptyDate;
    }

}
