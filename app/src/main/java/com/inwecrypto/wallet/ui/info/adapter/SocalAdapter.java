package com.inwecrypto.wallet.ui.info.adapter;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * 作者：xiaoji06 on 2017/12/6 14:03
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class SocalAdapter extends PagerAdapter {
    private List<TextView> mListViews;
    public SocalAdapter(List<TextView> mListViews) {
        this.mListViews = mListViews;
    }
    @Override
    public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
        arg0.removeView(mListViews.get(arg1));
    }
    @Override
    public Parcelable saveState() {
        return null;
    }
    @Override
    public void startUpdate(ViewGroup container) {
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mListViews.get(position), 0);
        return mListViews.get(position);
    }
    @Override
    public void finishUpdate(ViewGroup container) {
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }
    @Override
    public int getCount() {
        if (mListViews != null) {
            return mListViews.size();
        }
        return 0;
    }

}
