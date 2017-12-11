package com.inwecrypto.wallet.common.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.NewsBean;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 广告轮播，默认自动滚动，滚动间隔4秒
 * Created by xiaoji on 2016/11/10.
 */

public class AutoLoopNewsViewPager extends FrameLayout implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private List<NewsBean> data;
    private MyPagerAdapter mAdapter;
    private Context context;
    private int currentViewPagerItem;
    private MyHandler myHandler;
    private boolean isAutoPlay=true;
    private boolean isTouch;
    private long delayedTime = 4000;
    private int totleSize;
    private LinearLayout pointLL;
    private List<View> points;

    public AutoLoopNewsViewPager(Context context) {
        super(context);
        init(context);
    }

    public AutoLoopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoLoopNewsViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context=context;
        View view= LayoutInflater.from(context).inflate(R.layout.view_autoloop_viewpager,null);
        mViewPager=(ViewPager)view.findViewById(R.id.live_view_pager);
        pointLL=(LinearLayout)view.findViewById(R.id.pointLL);
        this.addView(view);
        final ViewConfiguration conf = ViewConfiguration.get(getContext());
        mPagingTouchSlop = conf.getScaledTouchSlop() * 2;
    }

    /**
     * 设置图片适配器
     * @param parent 界面
     * @param data 广告数据
     */
    public void setPagerAdapter(OnGetNewsAdsViewPager parent, List<NewsBean> data){
        if (null == data || data.size()<=0){
            return;
        }

        this.data=data;
        mViewPager.removeAllViews();
        if (isAutoPlay){
            if (null == myHandler){
                myHandler=new MyHandler(parent);
            }
            myHandler.removeMessages(1);
        }

        totleSize=data.size();
        mAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
        //让其在最大值的中间开始滑动, 一定要在 mBottomImages初始化之前完成
        int mid =0;
        if (totleSize > 1){
            int midMax= MyPagerAdapter.MAX_SCROLL_VALUE / 2;
            mid = midMax-midMax%totleSize;
        }
        mViewPager.setCurrentItem(mid);
        currentViewPagerItem = mid;

        if (isAutoPlay && totleSize > 1){
            myHandler.sendEmptyMessageDelayed(1,delayedTime);
        }
    }

    private float x;
    private float y;
    private int mPagingTouchSlop;

    public void setScroll(ScrollView scrollly) {
        this.scrollly = scrollly;
    }
    private ScrollView scrollly;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (null!=mAdapter && null!=myHandler && isAutoPlay  && totleSize > 1){
            int action = ev.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    isTouch = false;
                    myHandler.removeMessages(1);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    isTouch = true;
                    myHandler.sendEmptyMessageDelayed(1,delayedTime);
                    break;
            }
        }
        if (null != data && data.size()>0 && null!=scrollly){

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x=ev.getX();
                    y=ev.getY();
                    scrollly.requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float offsetX=ev.getX()-x;
                    float offsetY=ev.getY()-y;

                    if (Math.abs(offsetX) > mPagingTouchSlop && (Math.abs(offsetX) >= Math.abs(offsetY))){
                        scrollly.requestDisallowInterceptTouchEvent(true);
                    }else if (Math.abs(offsetY)>mPagingTouchSlop){
                        scrollly.requestDisallowInterceptTouchEvent(false);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    scrollly.requestDisallowInterceptTouchEvent(false);
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        currentViewPagerItem = position;
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public class MyPagerAdapter extends PagerAdapter {

        public static final int MAX_SCROLL_VALUE = 10000;

        /**
         * @param container
         * @param position
         * @return 对position进行求模操作
         * 因为当用户向左滑时position可能出现负值，所以必须进行处理
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            //对ViewPager页号求摸取出View列表中要显示的项
            position %= totleSize;

            View view=LayoutInflater.from(container.getContext()).inflate(R.layout.info_view_news,null,false);
            TextView title= (TextView) view.findViewById(R.id.title);
            TextView content= (TextView) view.findViewById(R.id.content);

            title.setText(null==data.get(position).getTitle()?"":data.get(position).getTitle());
            content.setText(null==data.get(position).getDesc()?"":data.get(position).getDesc());

            final int finalPosition = position;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null!=listener){
                        listener.onClick(finalPosition);
                    }
                }
            });

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }


        @Override
        public int getCount() {
            int ret = 0;
            if (totleSize > 0) {
                if (totleSize==1){
                    ret=1;
                }else {
                    ret = MAX_SCROLL_VALUE;
                }
            }
            return ret;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (View) object;
        }
    }

    public void gotoLeft(){
        if (null == getHandler()){
            return;
        }
        mViewPager.setCurrentItem(--currentViewPagerItem);
        getHandler().removeMessages(1);
    }

    public void gotoRight(){
        if (null == getHandler()){
            return;
        }
        mViewPager.setCurrentItem(++currentViewPagerItem);
        getHandler().removeMessages(1);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 为防止内存泄漏, 声明自己的Handler并弱引用Activity
    ///////////////////////////////////////////////////////////////////////////
    private static class MyHandler extends Handler {
        private WeakReference<OnGetNewsAdsViewPager> mWeakReference;

        public MyHandler(OnGetNewsAdsViewPager parent) {
            mWeakReference = new WeakReference<OnGetNewsAdsViewPager>(parent);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    OnGetNewsAdsViewPager parent = mWeakReference.get();
                    if (null == parent){
                        return;
                    }
                    if (null == parent.getAdsNewsViewPager().getHandler()){
                        return;
                    }
                    parent.getAdsNewsViewPager().mViewPager.setCurrentItem(++parent.getAdsNewsViewPager().currentViewPagerItem);
                    parent.getAdsNewsViewPager().myHandler.sendEmptyMessageDelayed(1,parent.getAdsNewsViewPager().delayedTime);
                    break;
            }

        }
    }

    public ViewPager getViewPager(){
        return null==mViewPager?null:mViewPager;
    }

    /**
     * 设置是否自动换页
     * @param isAutoPlay
     */
    public void setAutoPlay(boolean isAutoPlay){
        this.isAutoPlay=isAutoPlay;
    }

    /**
     * 设置自动换页时间
     * @param delayedTime
     */
    public void setDelayedTime(int delayedTime){
        this.delayedTime=delayedTime;
    }

    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public interface OnGetNewsAdsViewPager{
        AutoLoopNewsViewPager getAdsNewsViewPager();
    }
}
