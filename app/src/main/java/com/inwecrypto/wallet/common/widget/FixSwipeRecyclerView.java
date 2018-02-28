package com.inwecrypto.wallet.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.inwecrypto.wallet.common.widget.pullextend.ExtendListHeader;
import com.inwecrypto.wallet.common.widget.pullextend.IExtendLayout;
import com.inwecrypto.wallet.common.widget.pullextend.PullExtendLayout;
import com.inwecrypto.wallet.ui.news.ZixunFragment;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

/**
 * 作者：xiaoji06 on 2018/2/8 16:19
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class FixSwipeRecyclerView extends SwipeMenuRecyclerView {

    private ExtendListHeader header;

    private PullExtendLayout pullExtend;

    public FixSwipeRecyclerView(Context context) {
        super(context);
    }

    public FixSwipeRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixSwipeRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ExtendListHeader getHeader() {
        return header;
    }

    public void setHeader(ExtendListHeader header) {
        this.header = header;
    }

    public PullExtendLayout getPullExtend() {
        return pullExtend;
    }

    public void setPullExtend(PullExtendLayout pullExtend) {
        this.pullExtend = pullExtend;
    }

    // 分别记录上次滑动的坐标(onInterceptTouchEvent)
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        int x = (int) ev.getX();
//        int y = (int) ev.getY();
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN: {
//                pullExtend.setInterceptTouchEventEnabled(true);
//                break;
//            }
//            case MotionEvent.ACTION_MOVE: {
//                int deltaX = x - mLastXIntercept;
//                int deltaY = y - mLastYIntercept;
//
//                if (header.getState().name().equals("arrivedListHeight")){
//                    pullExtend.setInterceptTouchEventEnabled(true);
//                }else {
//                    //计算Recyclerview第一个item的位置是否可见
//                    int topRowVerticalPosition=
//                            (getChildCount() == 0) ? 0 : getChildAt(0).getTop();
//                    Log.e("aaa","aaa:"+header.getState().name()+"  topRowVerticalPosition:"+topRowVerticalPosition);
//                    pullExtend.setInterceptTouchEventEnabled(topRowVerticalPosition>= 0);
//                    getParent().requestDisallowInterceptTouchEvent(topRowVerticalPosition>= 0);
//                }
//                break;
//            }
//            case MotionEvent.ACTION_UP: {
//                pullExtend.setInterceptTouchEventEnabled(true);
//                break;
//            }
//        }
//        mLastXIntercept = x;
//        mLastYIntercept = y;
        return super.dispatchTouchEvent(ev);
    }


//    @Override
//    public boolean canScrollVertically(int direction) {
//        // check if scrolling up
//        if (direction < 1) {
//            boolean original = super.canScrollVertically(direction);
//            return !original && getChildAt(0) != null && getChildAt(0).getTop() < 0 || original;
//        }
//        return super.canScrollVertically(direction);
//
//    }
}
