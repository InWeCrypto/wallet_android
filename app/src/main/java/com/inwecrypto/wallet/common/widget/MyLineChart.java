package com.inwecrypto.wallet.common.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.LineChart;

/**
 * Created by Administrator on 2017/9/5.
 * 功能描述：
 * 版本：@version
 */

public class MyLineChart extends LineChart {

    public MyLineChart(Context context) {
        super(context);
    }

    public MyLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private SwipeRefreshLayout view;
    public void setView(SwipeRefreshLayout view){
        this.view=view;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (null!=view){
                    view.setEnabled(false);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (null!=view){
                    view.setEnabled(true);
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
