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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.List;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.common.util.ScreenUtils;

/**
 * 广告轮播，默认自动滚动，滚动间隔4秒
 * Created by xiaoji on 2016/11/10.
 */

public class AutoLoopViewPager extends FrameLayout implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private List<String> data;
    private MyPagerAdapter mAdapter;
    private Context context;
    private int currentViewPagerItem;
    private MyHandler myHandler;
    private boolean isAutoPlay=true;
    private boolean isTouch;
    private long delayedTime = 4000;
    private String type;
    private int totleSize;

    public AutoLoopViewPager(Context context) {
        super(context);
        init(context);
    }

    public AutoLoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoLoopViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context=context;
        View view= LayoutInflater.from(context).inflate(R.layout.view_autoloop_viewpager,null);
        mViewPager=(ViewPager)view.findViewById(R.id.live_view_pager);
        this.addView(view);
        final ViewConfiguration conf = ViewConfiguration.get(getContext());
        mPagingTouchSlop = conf.getScaledTouchSlop() * 2;
    }

    /**
     * 设置图片适配器
     * @param parent 界面
     * @param data 广告数据
     */
    public void setPagerAdapter(OnGetAdsViewPager parent, List<String> data){
        if (null == data || data.size()<=0){
            return;
        }

        this.data=data;
        mViewPager.removeAllViews();
        if (isAutoPlay){
            if (null == myHandler){
                myHandler=new MyHandler(parent);
            }
            myHandler.removeMessages(0);
        }

        totleSize=data.size();
        mAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);

        //让其在最大值的中间开始滑动, 一定要在 mBottomImages初始化之前完成
        int mid =0;
        if (totleSize > 1){
            int midMax=MyPagerAdapter.MAX_SCROLL_VALUE / 2;
            mid = midMax-midMax%totleSize;
        }
        mViewPager.setCurrentItem(mid);
        currentViewPagerItem = mid;

        if (isAutoPlay && totleSize > 1){
            myHandler.sendEmptyMessageDelayed(0,delayedTime);
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
                    myHandler.removeMessages(0);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    isTouch = true;
                    myHandler.sendEmptyMessageDelayed(0,delayedTime);
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
            String url=data.get(position);

            ImageView ret = new ImageView(context);
            int width= ScreenUtils.getScreenWidth(context);
            int height= (int) (width/750.0*411);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width,height);
            ret.setLayoutParams(params);
            ret.setScaleType(ImageView.ScaleType.CENTER_CROP);

            final int finalPosition = position;
            ret.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null!=listener){
                        listener.onClick(finalPosition);
                    }
                }
            });

            if(!"".equals(url)){
                Glide.with(context)
                        .load(url)
                        .crossFade()
                        .into(ret);
            }
            container.addView(ret);
            return ret;
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

    ///////////////////////////////////////////////////////////////////////////
    // 为防止内存泄漏, 声明自己的Handler并弱引用Activity
    ///////////////////////////////////////////////////////////////////////////
    private static class MyHandler extends Handler {
        private WeakReference<OnGetAdsViewPager> mWeakReference;

        public MyHandler(OnGetAdsViewPager parent) {
            mWeakReference = new WeakReference<OnGetAdsViewPager>(parent);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    OnGetAdsViewPager parent = mWeakReference.get();
                    if (null == parent){
                        return;
                    }
                    if (null == parent.getAdsViewPager().getHandler()){
                        return;
                    }
                    if (parent.getAdsViewPager().isTouch) {
                        parent.getAdsViewPager().mViewPager.setCurrentItem(++parent.getAdsViewPager().currentViewPagerItem);
                        parent.getAdsViewPager().myHandler.sendEmptyMessageDelayed(0,parent.getAdsViewPager().delayedTime);
                    }else {
                        parent.getAdsViewPager().getHandler().removeMessages(0);
                    }
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

    public interface OnGetAdsViewPager{
        AutoLoopViewPager getAdsViewPager();
    }
}
