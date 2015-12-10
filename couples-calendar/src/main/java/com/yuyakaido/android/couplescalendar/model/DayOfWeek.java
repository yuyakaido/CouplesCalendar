package com.yuyakaido.android.couplescalendar.model;

import org.joda.time.DateTime;

/**
 * Created by yuyakaido on 6/9/15.
 */
public enum DayOfWeek {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    private int mId;

    private DayOfWeek(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    public static DayOfWeek valueOf(DateTime dateTime) {
        int dayOfWeek = dateTime.getDayOfWeek();
        for (DayOfWeek d : DayOfWeek.values()) {
            if (d.getId() == dayOfWeek) {
                return d;
            }
        }
        return null;
    }

    /**
     * 引数で指定したDateTimeが日曜日かどうかを返す
     * @param dateTime
     * @return
     */
    public static boolean isSunday(DateTime dateTime) {
        if (DayOfWeek.valueOf(dateTime) == DayOfWeek.SUNDAY) {
            return true;
        }
        return false;
    }

    /**
     * 引数で指定したDateTimeが土曜日かどうかを返す
     * @param dateTime
     * @return
     */
    public static boolean isSaturday(DateTime dateTime) {
        if (DayOfWeek.valueOf(dateTime) == DayOfWeek.SATURDAY) {
            return true;
        }
        return false;
    }

}
