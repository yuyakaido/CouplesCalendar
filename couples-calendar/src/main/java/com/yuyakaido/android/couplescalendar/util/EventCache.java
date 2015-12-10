package com.yuyakaido.android.couplescalendar.util;

import android.text.TextUtils;
import com.yuyakaido.android.couplescalendar.model.CouplesCalendarEvent;
import com.yuyakaido.android.couplescalendar.model.InternalEvent;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by yuyakaido on 6/13/15.
 */
public class EventCache {

    private static final Comparator<DateKey> DATE_GRID_COMPARATOR = new Comparator<DateKey>() {
        @Override
        public int compare(DateKey dateKey1, DateKey dateKey2) {
            return dateKey1.getDateTime().compareTo(dateKey2.getDateTime());
        }
    };

    /**
     * ロックオブジェクト
     */
    private static final Object mLock = new Object();

    /**
     * イベントを保持する
     */
    private EventHolder mEventHolder;

    /**
     * キャッシュ済みの月を保持する
     */
    private Set<MonthKey> mCachedMonths;

    /**
     * イベントのキャッシュを保持する
     */
    private Map<DateKey, DateValue> mCache;

    /**
     * 初期化が完了したかどうか
     */
    private boolean mIsInitialized;

    /**
     * キャッシュを初期化する
     * @param events
     * @param showedMonth
     */
    public void init(List<CouplesCalendarEvent> events, DateTime showedMonth) {
        synchronized (mLock) {
            mEventHolder = new EventHolder(events);
            mCachedMonths = new HashSet<>();
            mCache = new TreeMap<>(DATE_GRID_COMPARATOR);
            createDefaultCache(showedMonth);
            mIsInitialized = true;
        }
    }

    /**
     * 初期化が完了しているかどうかを返す
     * @return
     */
    public boolean isInitialized() {
        return mIsInitialized;
    }

    /**
     * 引数で指定した日付の単日イベントを返す
     * @param dateTime
     * @return
     */
    public List<InternalEvent> getOneDayEventsOn(DateTime dateTime) {
        if (!isInitialized()) {
            return null;
        }
        DateValue dateValue = mCache.get(new DateKey(dateTime));
        if (dateValue == null) {
            return null;
        }
        return dateValue.getOneDayEvents();
    }

    /**
     * 引数で指定した日付の複数日イベントを返す
     * @param dateTime
     * @return
     */
    public List<InternalEvent> getMultipleDaysEventsOn(DateTime dateTime) {
        if (!isInitialized()) {
            return null;
        }
        DateValue dateValue = mCache.get(new DateKey(dateTime));
        if (dateValue == null) {
            return null;
        }
        return dateValue.getMultipleDaysEvents();
    }

    /**
     * 引数で指定した月のイベントキャッシュを生成する
     * @param monthDateTime
     */
    public void createCacheIn(DateTime monthDateTime) {
        synchronized (mLock) {
            // キャッシュが生成済みの場合には何もしない
            if (mCachedMonths.contains(new MonthKey(monthDateTime))) {
                return;
            }

            // まずは親イベントを展開してTransientEventを生成する
            createTransientEvents(monthDateTime);

            // 指定された月の最初と最後を計算する
            DateTime fromDateTime = CalendarUtils.getMinDateTimeOfMonth(monthDateTime);
            DateTime toDateTime = CalendarUtils.getMaxDateTimeOfMonth(monthDateTime);

            // 指定した範囲に含まれているイベントを全て取得する
            List<InternalEvent> events = mEventHolder.getAllEvents(fromDateTime, toDateTime);

            for (InternalEvent event : events) {
                event.setLinePosition(null);

                // イベントが属している日付を取得してキャッシュに突っ込む
                // （例）
                // 2015-06-25 から 2015-06-26 までのイベントだった場合
                // 2015-06-25, 2015-06-26 それぞれに同じイベントがキャッシュされる
                List<DateTime> dateTimes = event.getEventDays();
                for (DateTime dateTime : dateTimes) {
                    // 指定月以外の予定はキャッシュしない
                    if (dateTime.isBefore(fromDateTime) || dateTime.isAfter(toDateTime)) {
                        continue;
                    }

                    DateKey dateKey = new DateKey(dateTime);
                    // すでにイベントキャッシュが存在している場合は追加する
                    if (mCache.containsKey(dateKey)) {
                        DateValue dateValue = mCache.get(dateKey);
                        dateValue.add(event);
                    } else { // キャッシュが存在しない日付だった場合には新たに追加する
                        DateValue dateValue = new DateValue();
                        dateValue.add(event);
                        mCache.put(dateKey, dateValue);
                    }
                }
            }

            // 複数日のイベントの位置を計算する
            parseMultipleDays();

            // キャッシュを生成した月を生成済みとして登録する
            mCachedMonths.add(new MonthKey(monthDateTime));
        }
    }

    /**
     * 引数で指定した月とその前後の月のキャッシュを生成する
     * @param dateTime
     */
    public void createDefaultCache(DateTime dateTime) {
        synchronized (mLock) {
            createCacheIn(dateTime.minusMonths(1));
            createCacheIn(dateTime);
            createCacheIn(dateTime.plusMonths(1));
        }
    }

    /**
     * 繰り返しを持つ親イベントを展開する
     * @param monthDateTime
     */
    private void createTransientEvents(DateTime monthDateTime) {
        // イベントの生成範囲を取得する（引数で渡された月の最初と最後）
        DateTime fromDateTime = CalendarUtils.getMinDateTimeOfMonth(monthDateTime);
        DateTime toDateTime = CalendarUtils.getMaxDateTimeOfMonth(monthDateTime);

        // 親イベントを全て取得する
        List<InternalEvent> recurringEvents = mEventHolder.getAllRecurringEvents();

        for (InternalEvent recurringEvent : recurringEvents) {
            // RecurrenceRuleをもとに親イベントを展開する
            // （例）
            // RecurrenceRule = FREQ=WEEKLY, StartAt = 2015-06-01 の場合
            // 2015-06-01, 2015-06-08, 2015-06-15, 2015-06-22, 2015-06-29 に展開される
            List<DateTime> dateTimes = RRuleUtils.getDates(
                    recurringEvent.getRecurrenceRule(),
                    recurringEvent.getStartAtDateTime(),
                    fromDateTime,
                    toDateTime);

            // TransientEventを生成する
            for (DateTime startAt : dateTimes) {
                // TransientEventの終了時間を設定する（Duration分だけ未来にズラす）
                Duration duration = new Duration(recurringEvent.getStartAtDateTime(), recurringEvent.getEndAtDateTime());
                DateTime endAt = startAt.plusSeconds((int) duration.getStandardSeconds());

                InternalEvent transientEvent = new InternalEvent();
                transientEvent.setStartAt(startAt);
                transientEvent.setEndAt(endAt);
                transientEvent.setEventType(CouplesCalendarEvent.EventType.TRANSIENT);
                transientEvent.setEventColor(recurringEvent.getEventColor());

                mEventHolder.addTransientEvent(transientEvent);
            }
        }
    }

    /**
     * イベントの帯を計算する
     */
    private void parseMultipleDays() {
        for (Map.Entry<DateKey, DateValue> entry : mCache.entrySet()) {
            DateKey key = entry.getKey();
            DateValue dateValue = entry.getValue();

            if (dateValue == null || dateValue.getMultipleDaysEvents().isEmpty()) {
                continue;
            }

            boolean isLower = false;
            boolean isUpper = false;
            boolean isLowerConflict = false;
            boolean isUpperConflict = false;

            // StartAtが早いイベントが下になり、遅いものは上になる
            for (InternalEvent event : dateValue.getMultipleDaysEvents()) {
                if (event.getLinePosition() == CouplesCalendarEvent.LinePosition.LOWER) {
                    isLower = true;
                } else if (event.getLinePosition() == CouplesCalendarEvent.LinePosition.UPPER) {
                    isUpper = true;
                }
            }

            for (InternalEvent event : dateValue.getMultipleDaysEvents()) {
                // すでに位置が確定している場合には計算を行わない
                if (event.getLinePosition() == CouplesCalendarEvent.LinePosition.LOWER
                        || event.getLinePosition() == CouplesCalendarEvent.LinePosition.UPPER) {
                    continue;
                }

                if (!isLower && !isUpper) { // 下も上も空いている場合
                    event.setLinePosition(CouplesCalendarEvent.LinePosition.LOWER);
                    isLower = true;
                } else if (isLower && !isUpper) { // 下が埋まっていて、上が空いている場合
                    event.setLinePosition(CouplesCalendarEvent.LinePosition.UPPER);
                    isUpper = true;
                } else if (!isLower && isUpper) { // 下が空いていて、上が埋まっている場合
                    event.setLinePosition(CouplesCalendarEvent.LinePosition.LOWER);
                    isLower = true;
                } else if (isLower && isUpper) { // 下も上も埋まっている場合
                    // 上下の線が埋まっている時、終了日のイベントは何も描画しない
                    boolean isLastDayOfEvent = CalendarUtils.isSameDay(new DateTime(event.getEndAt()), key.getDateTime());
                    if (isLastDayOfEvent) {
                        continue;
                    }

                    InternalEvent firstEvent = dateValue.getFirstEvent();
                    InternalEvent secondEvent = dateValue.getSecondEvent();

                    // イベントの終了日かどうかを計算する
                    boolean isLastDayOfFirstEvent = CalendarUtils.isSameDay(new DateTime(firstEvent.getEndAt()), key.getDateTime());
                    boolean isLastDayOfSecondEvent = CalendarUtils.isSameDay(new DateTime(secondEvent.getEndAt()), key.getDateTime());

                    // 上下両方のイベントが終了日の場合
                    if (isLastDayOfFirstEvent && isLastDayOfSecondEvent) {
                        if (!isLowerConflict && !isUpperConflict) {
                            event.setLinePosition(CouplesCalendarEvent.LinePosition.LOWER);
                            isLowerConflict = true;
                        } else if (isLowerConflict && !isUpperConflict) {
                            event.setLinePosition(CouplesCalendarEvent.LinePosition.UPPER);
                            isUpperConflict = true;
                        } else if (isLowerConflict && isUpperConflict) {
                            continue;
                        }
                    } else if (isLastDayOfFirstEvent) { // 下のイベントが終了日の場合
                        if (firstEvent.getLinePosition() == CouplesCalendarEvent.LinePosition.LOWER && !isLowerConflict) {
                            event.setLinePosition(CouplesCalendarEvent.LinePosition.LOWER);
                            isLowerConflict = true;
                        } else if (firstEvent.getLinePosition() == CouplesCalendarEvent.LinePosition.UPPER && !isUpperConflict) {
                            event.setLinePosition(CouplesCalendarEvent.LinePosition.UPPER);
                            isUpperConflict = true;
                        } else {
                            continue;
                        }
                    } else if (isLastDayOfFirstEvent) { // 上のイベントが終了日の場合
                        if (secondEvent.getLinePosition() == CouplesCalendarEvent.LinePosition.LOWER && !isLowerConflict) {
                            event.setLinePosition(CouplesCalendarEvent.LinePosition.LOWER);
                            isLowerConflict = true;
                        } else if (secondEvent.getLinePosition() == CouplesCalendarEvent.LinePosition.UPPER && !isUpperConflict) {
                            event.setLinePosition(CouplesCalendarEvent.LinePosition.UPPER);
                            isUpperConflict = true;
                        } else {
                            continue;
                        }
                    }
                }
            }
        }
    }

    private static class EventHolder {

        private static final Comparator<InternalEvent> INTERNAL_EVENT_COMPARATOR = new Comparator<InternalEvent>() {
            @Override
            public int compare(InternalEvent event1, InternalEvent event2) {
                return event1.getStartAt().compareTo(event2.getStartAt());
            }
        };

        private List<InternalEvent> mAllEvents;

        public EventHolder(List<CouplesCalendarEvent> events) {
            mAllEvents = new ArrayList<>();
            for (CouplesCalendarEvent event : events) {
                InternalEvent internalEvent = new InternalEvent();
                internalEvent.setStartAt(new DateTime(event.getStartAt()));
                internalEvent.setEndAt(new DateTime(event.getEndAt()));
                internalEvent.setRecurrenceRule(event.getRecurrenceRule());
                internalEvent.setEventColor(event.getEventColor());
                mAllEvents.add(internalEvent);
            }
        }

        /**
         * TransientEventを追加する
         * @param transientEvent
         */
        public void addTransientEvent(InternalEvent transientEvent) {
            mAllEvents.add(transientEvent);
        }

        /**
         * 繰り返しを持つ親イベントを全て取得する
         * @return
         */
        public List<InternalEvent> getAllRecurringEvents() {
            List<InternalEvent> result = new ArrayList<>();
            for (InternalEvent event : mAllEvents) {
                if (!TextUtils.isEmpty(event.getRecurrenceRule())) {
                    result.add(event);
                }
            }
            return result;
        }

        /**
         * 引数で指定した範囲に含まれているイベントを全て取得する（親イベントを除く）
         * @param fromDateTime
         * @param toDateTime
         * @return
         */
        public List<InternalEvent> getAllEvents(DateTime fromDateTime, DateTime toDateTime) {
            List<InternalEvent> result = new ArrayList<>();
            for (InternalEvent event : mAllEvents) {
                if (event.getEventType() != CouplesCalendarEvent.EventType.RECURRING
                        && (((fromDateTime.getMillis() <= event.getZonedStartAt().getMillis()) && (event.getZonedStartAt().getMillis() <= toDateTime.getMillis()))
                        || ((fromDateTime.getMillis() <= event.getZonedEndAt().getMillis()) && (event.getZonedEndAt().getMillis() <= toDateTime.getMillis()))
                        || ((event.getZonedStartAt().getMillis() <= fromDateTime.getMillis()) && (toDateTime.getMillis() <= event.getZonedEndAt().getMillis())))) {
                    result.add(event);
                }
            }
            Collections.sort(result, INTERNAL_EVENT_COMPARATOR);
            return result;
        }

    }

    /**
     * キャッシュ生成済みの月を表すクラス
     */
    private static class MonthKey {
        private DateTime mMonthDateTime;

        public MonthKey(DateTime dateTime) {
            mMonthDateTime = dateTime;
        }

        public DateTime getDateTime() {
            return mMonthDateTime;
        }

        /**
         * 年月が一致すれば同じと判断する
         * @param o
         * @return
         */
        @Override
        public boolean equals(Object o) {
            if (o instanceof MonthKey) {
                MonthKey monthKey = (MonthKey) o;
                DateTime dateTime = monthKey.getDateTime();
                if (CalendarUtils.isSameMonth(dateTime, mMonthDateTime)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            // contains()は以下の順番で判定するため、オーバーライドする
            // 1. HashCodeが一致するかどうか
            // 2. equals()がtrueを返すかどうか
            return 0;
        }
    }

    /**
     * キャッシュ生成済みの日付を表すクラス
     */
    private static class DateKey {
        private DateTime mDateTime;

        public DateKey(DateTime dateTime) {
            mDateTime = dateTime;
        }

        public DateTime getDateTime() {
            return mDateTime;
        }

        /**
         * 年月日が一致すれば同じと判断する
         * @param o
         * @return
         */
        @Override
        public boolean equals(Object o) {
            if (o instanceof DateKey) {
                DateKey dateKey = (DateKey) o;
                DateTime dateTime = dateKey.getDateTime();
                if (CalendarUtils.isSameDay(dateTime, mDateTime)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            // contains()は以下の順番で判定するため、オーバーライドする
            // 1. HashCodeが一致するかどうか
            // 2. equals()がtrueを返すかどうか
            return 0;
        }

    }

    /**
     * 生成済みのイベントキャッシュを保持するクラス
     */
    private static class DateValue {
        // 1日だけのイベント配列
        private List<InternalEvent> mOneDayEvents = new ArrayList<>();
        // 複数日のイベント配列
        private List<InternalEvent> mMultipleDaysEvents = new ArrayList<>();

        public List<InternalEvent> getOneDayEvents() {
            return mOneDayEvents;
        }

        public List<InternalEvent> getMultipleDaysEvents() {
            return mMultipleDaysEvents;
        }

        public void add(InternalEvent event) {
            if (event.getDurationType() == CouplesCalendarEvent.DurationType.MULTIPLE_DAYS) {
                mMultipleDaysEvents.add(event);
            } else {
                mOneDayEvents.add(event);
            }
        }

        /**
         * 1つ目のイベントを返す
         * @return
         */
        public InternalEvent getFirstEvent() {
            if (mMultipleDaysEvents.size() >= 1) {
                return mMultipleDaysEvents.get(0);
            }
            return null;
        }

        /**
         * 2つ目のイベントを返す
         * @return
         */
        public InternalEvent getSecondEvent() {
            if (mMultipleDaysEvents.size() >= 2) {
                return mMultipleDaysEvents.get(1);
            }
            return null;
        }

    }

}
