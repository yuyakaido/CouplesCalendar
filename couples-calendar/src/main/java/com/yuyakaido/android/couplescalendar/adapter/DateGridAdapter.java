package com.yuyakaido.android.couplescalendar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.yuyakaido.android.couplescalendar.R;
import com.yuyakaido.android.couplescalendar.model.CouplesCalendarEvent;
import com.yuyakaido.android.couplescalendar.model.Date;
import com.yuyakaido.android.couplescalendar.model.DayOfWeek;
import com.yuyakaido.android.couplescalendar.model.InternalEvent;
import com.yuyakaido.android.couplescalendar.model.Theme;
import com.yuyakaido.android.couplescalendar.util.CalendarUtils;
import com.yuyakaido.android.couplescalendar.util.EventCache;
import com.yuyakaido.android.couplescalendar.view.CheckedView;
import com.yuyakaido.android.couplescalendar.view.EventLine;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by yuyakaido on 6/8/15.
 */
public class DateGridAdapter extends ArrayAdapter<Date> {

    private static final int DAY_OF_WEEK_COUNT = 7;

    private EventCache mEventCache;
    private Theme mTheme;
    private DateTime mToday;

    public DateGridAdapter(
            Context context,
            EventCache eventCache,
            Theme theme,
            DateTime thisMonth,
            DateTime today) {
        super(context, 0, CalendarUtils.getDates(thisMonth));
        mEventCache = eventCache;
        mTheme = theme;
        mToday = today;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_date_grid, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.date_grid_view_tag_key_view_holder, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(
                    R.id.date_grid_view_tag_key_view_holder);
        }

        Date date = getItem(position);
        convertView.setTag(R.id.date_grid_view_tag_key_date_time, date.getDateTime());

        // 実装を簡単にするため、一旦全てのViewを隠す
        holder.hideAll();

        if (!date.isEmptyDate()) {
            // 第2週以降はborderを表示する
            if (position >= DAY_OF_WEEK_COUNT) {
                holder.border.setVisibility(View.VISIBLE);
            }

            // イベントの点を設定する
            setupEventDot(date, holder);

            // イベントの帯を設定する
            setupEventLine(date, holder);

            // 背景を設定する
            setupBackground(date, holder);

            // 日付を設定する
            setupDate(date, holder);
        }

        return convertView;
    }

    /**
     * イベントの帯を設定する
     * @param date
     * @param holder
     */
    private void setupEventLine(Date date, ViewHolder holder) {
        List<InternalEvent> multipleDaysEvents = mEventCache
                .getMultipleDaysEventsOn(date.getDateTime());
        if (multipleDaysEvents != null) {
            for (InternalEvent event : multipleDaysEvents) {
                // 下にイベントがある場合
                drawEventLine(date, multipleDaysEvents, event,
                        CouplesCalendarEvent.LinePosition.LOWER, holder.lowerEventLine);
                // 上にイベントがある場合
                drawEventLine(date, multipleDaysEvents, event,
                        CouplesCalendarEvent.LinePosition.UPPER, holder.upperEventLine);
            }
        }
    }

    /**
     * イベントの帯を描画する
     * @param date
     * @param multipleDaysEvents
     * @param event
     * @param linePosition
     * @param eventLine
     */
    private void drawEventLine(
            Date date,
            List<InternalEvent> multipleDaysEvents,
            InternalEvent event,
            CouplesCalendarEvent.LinePosition linePosition,
            EventLine eventLine) {
        if (event.getLinePosition() == linePosition) {
            eventLine.setVisibility(View.VISIBLE);
            if (!eventLine.hasLeftColor()) {
                eventLine.setLeftColor(event.getEventColor());
                // そのイベントが開始日の場合は左側を丸くする
                if (CalendarUtils.isSameDay(event.getZonedStartAt(), date.getDateTime())) {
                    eventLine.setHasLeftEdge(true);
                    eventLine.invalidate();
                }
            }

            // そのイベントが終了日の場合
            if (CalendarUtils.isSameDay(event.getZonedEndAt(), date.getDateTime())) {
                InternalEvent other = getOtherEventIn(multipleDaysEvents, event, linePosition);
                // 他のイベントが存在していない場合は右側を丸くする
                if (other == null) {
                    eventLine.setHasRightEdge(true);
                    eventLine.invalidate();
                } else { // 他のイベントが存在する場合は衝突描画にする
                    eventLine.setHasConflict(true);
                    if (!eventLine.hasRightColor()) {
                        eventLine.setRightColor(other.getEventColor());
                    }
                    eventLine.invalidate();
                }
            }
        }
    }

    /**
     * 引数で指定したイベントと同じ位置にあるイベントを返す
     * @param events
     * @param event
     * @param linePosition
     * @return
     */
    private InternalEvent getOtherEventIn(
            List<InternalEvent> events,
            InternalEvent event,
            CouplesCalendarEvent.LinePosition linePosition) {
        for (InternalEvent other : events) {
            if (!other.equals(event)
                    && other.getLinePosition() == linePosition) {
                return other;
            }
        }
        return null;
    }

    /**
     * イベントの点を設定する
     * @param date
     * @param holder
     */
    private void setupEventDot(Date date, ViewHolder holder) {
        List<InternalEvent> oneDayEvents = mEventCache
                .getOneDayEventsOn(date.getDateTime());
        if (oneDayEvents != null) {
            // 1つ目のイベントがあれば左側の点を表示する
            if (oneDayEvents.size() >= 1) {
                drawEventDot(oneDayEvents.get(0), holder.firstEvent);
            }
            // 2つ目のイベントがあれば右側の点を表示する
            if (oneDayEvents.size() >= 2) {
                drawEventDot(oneDayEvents.get(1), holder.secondEvent);
            }
            // 3つ目のイベントがあればプラスアイコンを表示する
            if (oneDayEvents.size() >= 3) {
                drawThirdEventView(holder);
            }
        }
    }

    /**
     * イベントの点を描画する
     * @param event
     * @param view
     */
    private void drawEventDot(InternalEvent event, View view) {
        StateListDrawable drawable = new StateListDrawable();
        ShapeDrawable normalDrawable = new ShapeDrawable(new OvalShape());
        if (event.getEventColor() == 0) {
            normalDrawable.getPaint().setColor(getContext().getResources().getColor(
                    Theme.getDefaultTheme().getEventColorId()));
        } else {
            normalDrawable.getPaint().setColor(event.getEventColor());
        }
        ShapeDrawable checkedDrawable = new ShapeDrawable(new OvalShape());
        checkedDrawable.getPaint().setColor(Color.WHITE);
        drawable.addState(new int[]{-android.R.attr.state_checked}, normalDrawable);
        drawable.addState(new int[]{android.R.attr.state_checked}, checkedDrawable);
        view.setVisibility(View.VISIBLE);
        view.setBackgroundDrawable(drawable);
    }

    /**
     * プラスアイコンを描画する
     * @param holder
     */
    private void drawThirdEventView(ViewHolder holder) {
        holder.thirdEventBase.setVisibility(View.VISIBLE);
        holder.thirdEventPlus.setVisibility(View.VISIBLE);
        holder.thirdEventBase.setColorFilter(
                getContext().getResources().getColor(mTheme.getSelectedCellColorId()),
                PorterDuff.Mode.SRC_IN);
        holder.thirdEventPlus.setColorFilter(
                getContext().getResources().getColor(R.color.cc_while),
                PorterDuff.Mode.SRC_IN);
    }

    /**
     * 日付セルの文字を設定する
     * @param date
     * @param holder
     */
    private void setupDate(Date date, ViewHolder holder) {
        holder.date.setVisibility(View.VISIBLE);
        holder.date.setText(String.valueOf(date.getDateTime().getDayOfMonth()));
        if (DayOfWeek.isSunday(date.getDateTime())) {
            holder.date.setTextColor(getContext().getResources()
                    .getColorStateList(R.color.selector_sunday_text_color));
        } else if (DayOfWeek.isSaturday(date.getDateTime())) {
            holder.date.setTextColor(getContext().getResources()
                    .getColorStateList(R.color.selector_saturday_text_color));
        }
    }

    /**
     * 日付セルの背景を設定する
     * @param date
     * @param holder
     */
    private void setupBackground(Date date, ViewHolder holder) {
        if (date.getDateTime().equals(mToday)) {
            holder.background.setBackgroundResource(mTheme.getTodayCellDrawableId());
        } else {
            holder.background.setBackgroundResource(mTheme.getSelectedCellDrawableId());
        }
    }

    public static class ViewHolder {
        public CheckedTextView date;
        public CheckedView firstEvent;
        public CheckedView secondEvent;
        public ImageView thirdEventBase;
        public ImageView thirdEventPlus;
        public EventLine upperEventLine;
        public EventLine lowerEventLine;
        public CheckedView background;
        public View border;

        public ViewHolder(View view) {
            date = (CheckedTextView) view.findViewById(R.id.item_date_grid_date);
            firstEvent = (CheckedView) view.findViewById(R.id.item_date_grid_first_event);
            secondEvent = (CheckedView) view.findViewById(R.id.item_date_grid_second_event);
            upperEventLine = (EventLine) view.findViewById(R.id.item_date_grid_upper_line);
            lowerEventLine = (EventLine) view.findViewById(R.id.item_date_grid_lower_line);
            thirdEventBase = (ImageView) view.findViewById(R.id.item_date_grid_third_event_base);
            thirdEventPlus = (ImageView) view.findViewById(R.id.item_date_grid_third_event_plus);
            background = (CheckedView) view.findViewById(R.id.item_date_grid_background);
            border = view.findViewById(R.id.item_date_grid_border);
        }

        /**
         * 全てのViewを隠す
         */
        public void hideAll() {
            date.setVisibility(View.INVISIBLE);
            firstEvent.setVisibility(View.INVISIBLE);
            secondEvent.setVisibility(View.INVISIBLE);
            thirdEventBase.setVisibility(View.INVISIBLE);
            thirdEventPlus.setVisibility(View.INVISIBLE);
            upperEventLine.setVisibility(View.INVISIBLE);
            upperEventLine.setHasLeftEdge(false);
            upperEventLine.setHasRightEdge(false);
            upperEventLine.setHasConflict(false);
            upperEventLine.setHasLeftColor(false);
            upperEventLine.setHasRightColor(false);
            lowerEventLine.setVisibility(View.INVISIBLE);
            lowerEventLine.setHasLeftEdge(false);
            lowerEventLine.setHasRightEdge(false);
            lowerEventLine.setHasConflict(false);
            lowerEventLine.setHasLeftColor(false);
            lowerEventLine.setHasRightColor(false);
            border.setVisibility(View.INVISIBLE);
        }
    }

}
