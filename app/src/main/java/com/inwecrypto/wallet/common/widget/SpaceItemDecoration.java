package com.inwecrypto.wallet.common.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 作者：xiaoji06 on 2017/11/30 10:13
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private int paddingSpace;

    public SpaceItemDecoration(int space,int paddingSpace) {
        this.space = space;
        this.paddingSpace=paddingSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        int i=parent.getChildLayoutPosition(view);
        if (i==0){
            outRect.left = 0;
            outRect.bottom = 0;
            outRect.top = 0;
            outRect.right = 0;
            return;
        }

        if (i%2 == 0) {
            outRect.left = space;
            outRect.bottom = space;
            outRect.top = 0;
            outRect.right = paddingSpace;
        }else {
            outRect.left = paddingSpace;
            outRect.bottom = space;
            outRect.top = 0;
            outRect.right = 0;
        }
    }

}