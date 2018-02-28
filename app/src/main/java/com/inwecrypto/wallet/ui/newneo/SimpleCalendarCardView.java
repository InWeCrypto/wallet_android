package com.inwecrypto.wallet.ui.newneo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

/**
 * 作者：xiaoji06 on 2018/2/26 11:36
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class SimpleCalendarCardView extends MonthView {

    private int mRadius;

    public SimpleCalendarCardView(Context context) {
        super(context);
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
        mSchemePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onLoopStart(int x, int y) {

    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        mSelectedPaint.setColor(Color.parseColor("#FF7800"));
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        mSchemePaint.setStyle(Paint.Style.FILL);
        mSchemePaint.setColor(Color.parseColor("#F0EFEF"));
        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;

        if (isSelected){
            mCurDayTextPaint.setColor(Color.parseColor("#FFFFFF"));
            mCurMonthTextPaint.setColor(Color.parseColor("#FFFFFF"));
            mSchemeTextPaint.setColor(Color.parseColor("#FFFFFF"));
        }else {
            mSchemeTextPaint.setColor(Color.parseColor("#333333"));
            mCurDayTextPaint.setColor(Color.parseColor("#FF7800"));
            if (calendar.isCurrentDay()){
                mCurMonthTextPaint.setColor(Color.parseColor("#FF7800"));
            }else {
                mCurMonthTextPaint.setColor(Color.parseColor("#333333"));
            }
        }
        if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            (calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint));
        }
    }
}
