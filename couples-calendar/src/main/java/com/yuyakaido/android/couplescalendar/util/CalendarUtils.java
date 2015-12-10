package com.yuyakaido.android.couplescalendar.util;

import android.content.Context;
import android.util.DisplayMetrics;

import com.yuyakaido.android.couplescalendar.model.Date;
import com.yuyakaido.android.couplescalendar.model.DayOfWeek;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by yuyakaido on 6/8/15.
 */
public class CalendarUtils {

    private static final int TIME_ZONE_OFFSET = TimeZone.getDefault().getRawOffset();

    /**
     * dp → px
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, int dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (metrics.density * dp);
    }

    /**
     * 引数で指定した日の00:00を返す
     * （例）
     * 引数: 2015-06-24 10:10:10.100
     * 結果: 2015-06-24 00:00:00.000
     * @param dateTime
     * @return
     */
    public static DateTime getMidnight(DateTime dateTime) {
        return new DateTime(
                dateTime.getYear(),
                dateTime.getMonthOfYear(),
                dateTime.getDayOfMonth(),
                dateTime.hourOfDay().getMinimumValue(),
                dateTime.minuteOfHour().getMinimumValue(),
                dateTime.secondOfMinute().getMinimumValue(),
                dateTime.millisOfSecond().getMinimumValue());
    }

    /**
     * 時差を考慮したDateTimeを返す
     * @param dateTime
     * @return
     */
    public static DateTime getZonedDateTime(DateTime dateTime) {
        return dateTime.plusMillis(TIME_ZONE_OFFSET);
    }

    /**
     * 時差を考慮したDateTimeを返す
     * @return
     */
    public static DateTime getZonedDateTime() {
        return getZonedDateTime(new DateTime());
    }

    /**
     * 時差を考慮したDateTimeの時間を00:00にしたものを返す
     * @return
     */
    public static DateTime getZonedMidnight() {
        return getMidnight(getZonedDateTime());
    }

    /**
     * 引数で指定した月の最初を返す
     * （例）
     * 引数: 2015-06-16 10:10:10.100
     * 結果: 2015-06-01 00:00:00.000
     * @param dateTime
     * @return
     */
    public static DateTime getMinDateTimeOfMonth(DateTime dateTime) {
        return new DateTime(
                dateTime.getYear(),
                dateTime.getMonthOfYear(),
                dateTime.dayOfMonth().getMinimumValue(),
                dateTime.hourOfDay().getMinimumValue(),
                dateTime.minuteOfHour().getMinimumValue(),
                dateTime.secondOfMinute().getMinimumValue(),
                dateTime.millisOfSecond().getMinimumValue());
    }

    /**
     * 引数で指定した月の最後を返す
     * （例）
     * 引数: 2015-06-16 10:10:10.100
     * 結果: 2015-06-30 23:59:59.999
     * @param dateTime
     * @return
     */
    public static DateTime getMaxDateTimeOfMonth(DateTime dateTime) {
        return new DateTime(
                dateTime.getYear(),
                dateTime.getMonthOfYear(),
                dateTime.dayOfMonth().getMaximumValue(),
                dateTime.hourOfDay().getMaximumValue(),
                dateTime.minuteOfHour().getMaximumValue(),
                dateTime.secondOfMinute().getMaximumValue(),
                dateTime.millisOfSecond().getMaximumValue());
    }

    /**
     * 引数で指定した月の日付一覧を返す
     * @param dateTime
     * @return
     */
    public static List<Date> getDates(DateTime dateTime) {
        DayOfWeek startWeekOn = DayOfWeek.SUNDAY;
        List<Date> result = new ArrayList<>();
        DateTime fromDateTime = getMinDateTimeOfMonth(dateTime);
        DateTime toDateTime = getMaxDateTimeOfMonth(dateTime);
        // 曜日の関係で空白となる日付を計算する
        if (!(DayOfWeek.valueOf(fromDateTime) == startWeekOn)) {
            int paddingCount = fromDateTime.getDayOfWeek();
            for (int i = 0; i < paddingCount; i++) {
                Date date = new Date();
                date.setIsEmptyDate(true);
                result.add(date);
            }
        }
        // 月末までの日付を計算する
        while (fromDateTime.getMillis() <= toDateTime.getMillis()) {
            Date date = new Date();
            date.setDateTime(fromDateTime);
            result.add(date);
            fromDateTime = fromDateTime.plusDays(1);
        }
        return result;
    }

    /**
     * 同じ年月かどうかを返す
     * @param d1
     * @param d2
     * @return
     */
    public static boolean isSameMonth(DateTime d1, DateTime d2) {
        if (d1.getYear() == d2.getYear()
                && d1.getMonthOfYear() == d2.getMonthOfYear()) {
            return true;
        }
        return false;
    }

    /**
     * 同じ年月日かどうかを返す
     * @param d1
     * @param d2
     * @return
     */
    public static boolean isSameDay(DateTime d1, DateTime d2) {
        if (d1.getYear() == d2.getYear()
                && d1.getMonthOfYear() == d2.getMonthOfYear()
                && d1.getDayOfMonth() == d2.getDayOfMonth()) {
            return true;
        }
        return false;
    }

    /**
     * 12時間以上の長さかどうかを返す
     * @param duration
     * @return
     */
    public static boolean isLongerThenTwelveHours(Duration duration) {
        Duration twelveHours = new Duration(12 * 60 * 60 * 1000);
        return duration.isLongerThan(twelveHours);
    }

    /**
     * 第1引数が第2引数以上かどうかを返す
     * @param d1
     * @param d2
     * @return
     */
    public static boolean isEqualOrAfter(DateTime d1, DateTime d2) {
        if (d1.getMillis() >= d2.getMillis()) {
            return true;
        }
        return false;
    }

}
