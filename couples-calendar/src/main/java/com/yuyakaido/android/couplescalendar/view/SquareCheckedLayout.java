package com.yuyakaido.android.couplescalendar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;

/**
 * Created by yuyakaido on 6/10/15.
 */
public class SquareCheckedLayout extends RelativeLayout implements Checkable {

    private static final int[] CHECKED_STATE_ARRAY = {android.R.attr.state_checked};

    private boolean mIsChecked;

    public SquareCheckedLayout(Context context) {
        super(context);
    }

    public SquareCheckedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareCheckedLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
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
