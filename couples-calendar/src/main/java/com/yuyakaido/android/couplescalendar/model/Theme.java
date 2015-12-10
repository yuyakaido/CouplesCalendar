package com.yuyakaido.android.couplescalendar.model;

import com.yuyakaido.android.couplescalendar.R;

/**
 * Created by yuyakaido on 6/17/15.
 */
public enum Theme {
    RED(
            R.color.cc_selected_cell_red_theme,
            R.color.cc_today_cell_red_theme,
            R.drawable.selector_selected_cell_red_theme,
            R.drawable.selector_today_cell_red_theme,
            R.color.cc_event_red_theme),
    ORANGE(
            R.color.cc_selected_cell_orange_theme,
            R.color.cc_today_cell_orange_theme,
            R.drawable.selector_selected_cell_orange_theme,
            R.drawable.selector_today_cell_orange_theme,
            R.color.cc_event_orange_theme),
    YELLOW(
            R.color.cc_selected_cell_yellow_theme,
            R.color.cc_today_cell_yellow_theme,
            R.drawable.selector_selected_cell_yellow_theme,
            R.drawable.selector_toady_cell_yellow_theme,
            R.color.cc_event_yellow_theme),
    GREEN(
            R.color.cc_selected_cell_green_theme,
            R.color.cc_today_cell_green_theme,
            R.drawable.selector_selected_cell_green_theme,
            R.drawable.selector_today_cell_green_theme,
            R.color.cc_event_green_theme),
    TIFFANY_BLUE(
            R.color.cc_selected_cell_tiffany_blue_theme,
            R.color.cc_today_cell_tiffany_blue_theme,
            R.drawable.selector_selected_cell_tiffany_blue_theme,
            R.drawable.selector_today_cell_tiffany_blue_theme,
            R.color.cc_event_tiffany_blue_theme),
    LIGHT_BLUE(
            R.color.cc_selected_cell_light_blue_theme,
            R.color.cc_today_cell_light_blue_theme,
            R.drawable.selector_selected_cell_light_blue_theme,
            R.drawable.selector_today_cell_light_blue_theme,
            R.color.cc_event_light_blue_theme),
    BLUE(
            R.color.cc_selected_cell_blue_theme,
            R.color.cc_today_cell_blue_theme,
            R.drawable.selector_selected_cell_blue_theme,
            R.drawable.selector_today_cell_blue_theme,
            R.color.cc_event_blue_theme),
    PURPLE(
            R.color.cc_selected_cell_purple_theme,
            R.color.cc_today_cell_purple_theme,
            R.drawable.selector_selected_cell_purple_theme,
            R.drawable.selector_today_cell_purple_theme,
            R.color.cc_event_purple_theme),
    PINK(
            R.color.cc_selected_cell_pink_theme,
            R.color.cc_today_cell_pink_theme,
            R.drawable.selector_selected_cell_pink_theme,
            R.drawable.selector_today_cell_pink_theme,
            R.color.cc_event_pink_theme),
    NAVY_BLUE(
            R.color.cc_selected_cell_navy_blue_theme,
            R.color.cc_today_cell_navy_blue_theme,
            R.drawable.selector_selected_cell_navy_blue_theme,
            R.drawable.selector_today_cell_navy_blue_theme,
            R.color.cc_event_navy_blue_theme);

    private int mSelectedCellColorId;
    private int mTodayCellColorId;
    private int mSelectedCellDrawableId;
    private int mTodayCellDrawableId;
    private int mEventColorId;

    private Theme(
            int selectedCellColorId,
            int todayCellColorId,
            int selectedCellDrawableId,
            int todayCellDrawableId,
            int eventColorId) {
        mSelectedCellColorId = selectedCellColorId;
        mTodayCellColorId = todayCellColorId;
        mSelectedCellDrawableId = selectedCellDrawableId;
        mTodayCellDrawableId = todayCellDrawableId;
        mEventColorId = eventColorId;
    }

    public int getSelectedCellColorId() {
        return mSelectedCellColorId;
    }

    public int getTodayCellColorId() {
        return mTodayCellColorId;
    }

    public int getSelectedCellDrawableId() {
        return mSelectedCellDrawableId;
    }

    public int getTodayCellDrawableId() {
        return mTodayCellDrawableId;
    }

    public int getEventColorId() {
        return mEventColorId;
    }

    public static Theme getDefaultTheme() {
        return RED;
    }

}
