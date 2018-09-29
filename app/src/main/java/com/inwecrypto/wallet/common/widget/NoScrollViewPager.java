package com.inwecrypto.wallet.common.widget;

/**
 * 作者：xiaoji06 on 2018/2/8 15:06
 * github：https://github.com/xiaoji06
 * 功能：
 */
/**
 * 禁止viewpager页面的滑动的效果
 * Created by Administrator on 2017/6/1.
 */

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.inwecrypto.wallet.ui.news.fragment.CProjectFragment;
import com.inwecrypto.wallet.ui.news.fragment.ProjectFragment;

import java.lang.annotation.ElementType;

public class NoScrollViewPager extends ViewPager {
    private boolean noScroll = false;

    public boolean isDispath() {
        return isDispath;
    }

    public void setDispath(boolean dispath) {
        isDispath = dispath;
    }

    private boolean isDispath;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
                /* return false;//super.onTouchEvent(arg0); */
        switch(arg0.getAction()){
        }


        return !noScroll && super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                preDowny=ev.getY();
//                preDownx=ev.getX();
//                break;
//            case MotionEvent.ACTION_MOVE:
//
//                if (isClose){
//
//                    if (Math.abs((preDowny-ev.getY()))>Math.abs((preDownx-ev.getX()))){
//                        if (null==f1||null==f2){
//                            getParent().requestDisallowInterceptTouchEvent(false);
//                        }else {
//                            if (f2.isHidden()){
//                                if (f2.isFirst()&&preDowny<ev.getY()){
//                                    getParent().requestDisallowInterceptTouchEvent(false);
//                                }
//                            }else {
//                                if (f1.isFirst()&&preDowny<ev.getY()){
//                                    getParent().requestDisallowInterceptTouchEvent(false);
//                                }
//                            }
//                            getParent().requestDisallowInterceptTouchEvent(true);
//                        }
//                    }else {
//                        getParent().requestDisallowInterceptTouchEvent(false);
//                    }
//                }else {
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                }
//                preDowny=ev.getY();
//                preDownx=ev.getX();
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                break;
//        }
        return !noScroll && super.onInterceptTouchEvent(ev);
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }

    public boolean isClose;

    public CProjectFragment getF1() {
        return f1;
    }

    public void setF1(CProjectFragment f1) {
        this.f1 = f1;
    }

    public ProjectFragment getF2() {
        return f2;
    }

    public void setF2(ProjectFragment f2) {
        this.f2 = f2;
    }

    private CProjectFragment f1;
    private ProjectFragment f2;

    private float preDowny;
    private float preDownx;


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        //false 去除滚动效果
        super.setCurrentItem(item,false);
    }

}
