package com.yuyakaido.android.couplescalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.yuyakaido.android.couplescalendar.util.CalendarUtils;

/**
 * Created by yuyakaido on 6/24/15.
 */
public class EventLine extends View {

    /**
     * 左側を丸めるかどうか
     */
    private boolean mHasLeftEdge;

    /**
     * 右側を丸めるかどうか
     */
    private boolean mHasRightEdge;

    /**
     * 同日に複数のイベントがあり、衝突があるかどうか
     */
    private boolean mHasConflict;

    /**
     * 左側のイベント色
     */
    private int mLeftColor;

    /**
     * 左側のイベント色が設定されているかどうか
     */
    private boolean mHasLeftColor;

    /**
     * 右側のイベント色
     */
    private int mRightColor;

    /**
     * 右側のイベント色が設定されているかどうか
     */
    private boolean mHasRightColor;

    public EventLine(Context context) {
        super(context);
    }

    public EventLine(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventLine(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
        params.setMargins(0, params.topMargin, 0, params.bottomMargin);
        setLayoutParams(params);
        if (mHasConflict) {
            drawConflictLine(canvas);
        } else {
            drawCircleEdgeLine(canvas);
        }
    }

    public void setHasLeftEdge(boolean hasLeftEdge) {
        mHasLeftEdge = hasLeftEdge;
    }

    public void setHasRightEdge(boolean hasRightEdge) {
        mHasRightEdge = hasRightEdge;
    }

    public void setHasConflict(boolean hasConflict) {
        mHasConflict = hasConflict;
    }

    public void setLeftColor(int color) {
        mLeftColor = color;
        mHasLeftColor = true;
    }

    public void setHasLeftColor(boolean hasLeftColor) {
        mHasLeftColor = hasLeftColor;
    }

    public boolean hasLeftColor() {
        return mHasLeftColor;
    }

    public void setRightColor(int color) {
        mRightColor = color;
        mHasRightColor = true;
    }

    public void setHasRightColor(boolean hasRightColor) {
        mHasRightColor = hasRightColor;
    }

    public boolean hasRightColor() {
        return mHasRightColor;
    }

    private void drawConflictLine(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);

        Paint leftPaint = new Paint();
        leftPaint.setAntiAlias(true);
        leftPaint.setColor(mLeftColor);

        Paint rightPaint = new Paint();
        rightPaint.setAntiAlias(true);
        rightPaint.setColor(mRightColor);

        Paint whitePaint = new Paint();
        whitePaint.setAntiAlias(true);
        whitePaint.setColor(Color.WHITE);

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        int rightConflictWidth = CalendarUtils.dp2px(getContext(), 8);
        int rightConflictLeft = canvasWidth - rightConflictWidth;
        int conflictCircleRadius = canvasHeight / 2;

        canvas.drawRect(rightConflictLeft, 0, rightConflictLeft + rightConflictWidth, canvasHeight, rightPaint);
        RectF rightConflictCircle = new RectF(rightConflictLeft - conflictCircleRadius, 0, rightConflictLeft + conflictCircleRadius, canvasHeight);
        canvas.drawArc(rightConflictCircle, 270, 180, true, whitePaint);

        int leftConflictLeft = rightConflictLeft - canvasHeight;

        canvas.drawRect(0, 0, leftConflictLeft, canvasHeight, leftPaint);

        RectF leftConflictCircle = new RectF(leftConflictLeft - conflictCircleRadius, 0, leftConflictLeft + conflictCircleRadius, canvasHeight);
        canvas.drawArc(leftConflictCircle, 0, 360, true, leftPaint);
    }

    private void drawCircleEdgeLine(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mLeftColor);

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int leftPadding = 0;
        if (mHasLeftEdge) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
            params.setMargins(CalendarUtils.dp2px(getContext(), 4),
                    params.topMargin, params.rightMargin, params.bottomMargin);
            setLayoutParams(params);
            leftPadding = getHeight() / 2;
        }
        int rightPadding = 0;
        if (mHasRightEdge) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
            params.setMargins(params.leftMargin, params.topMargin,
                    CalendarUtils.dp2px(getContext(), 4), params.bottomMargin);
            setLayoutParams(params);
            rightPadding = getHeight() / 2;
        }
        int rectLeft = leftPadding;
        int rectRight = canvasWidth - rightPadding;

        canvas.drawRect(rectLeft, 0, rectRight, canvasHeight, paint);

        RectF leftRectF = new RectF(rectLeft - leftPadding, 0, rectLeft + leftPadding, canvasHeight);
        RectF rightRectF = new RectF(rectRight - rightPadding, 0, rectRight + rightPadding, canvasHeight);
        canvas.drawArc(leftRectF, 0, 360, true, paint);
        canvas.drawArc(rightRectF, 0, 360, true, paint);
    }

}
