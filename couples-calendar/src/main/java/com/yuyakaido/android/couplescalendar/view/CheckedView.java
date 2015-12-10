package com.yuyakaido.android.couplescalendar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

/**
 * Created by yuyakaido on 6/10/15.
 */
public class CheckedView extends View implements Checkable {

    private static final int[] CHECKED_STATE_ARRAY = {android.R.attr.state_checked};

    private boolean mIsChecked;

    public CheckedView(Context context) {
        super(context);
    }

    public CheckedView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckedView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setChecked(boolean b) {
        mIsChecked = b;
        refreshDrawableState();
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mIsChecked);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (mIsChecked) {
            mergeDrawableStates(drawableState, CHECKED_STATE_ARRAY);
        }
        return drawableState;
    }

}
