package com.inwecrypto.wallet.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.lzy.okgo.OkGo;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xiaoji on 2017/4/11.
 */

public abstract class BaseFragment extends Fragment {

    protected final String TAG=this.getClass().getSimpleName();

    public boolean isShow;
    private View rootView;
    protected BaseActivity mActivity;
    protected Context mContext;
    private Unbinder mUnbinder;


    protected boolean isOpenEventBus;
    protected boolean isPullDown = true;
    protected int curentPage=1;
    protected Fragment mFragment=this;

    private boolean isViewInit;
    private boolean isLoadDate;
    protected boolean isLazyOpen;
    protected boolean isLoadSuccess;
    protected boolean isFirst=true;


    /**
     * 获得全局的，防止使用getActivity()为空
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (BaseActivity) context;
        this.mContext=context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null==savedInstanceState){
            isShow=true;
        }else {
            isShow=savedInstanceState.getBoolean("isShow");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView=inflater.inflate(setLayoutID(),null,false);
        mUnbinder=  ButterKnife.bind(this, rootView);
        initView();
        isViewInit=true;
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isLazyOpen){
            if (isShow&&isViewInit&&getUserVisibleHint()&&!isLoadDate){
                loadData();
                isLoadDate=true;
            }
        }else {
            if (isShow){
                loadData();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            isShow=true;
            initShow();
        }else {
            isShow=false;
            initHide();
        }
    }

    protected void initHide() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isLazyOpen&&isViewInit&&isVisibleToUser&&!isLoadDate){
            loadData();
            isLoadDate=true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG,TAG+"==onSaveInstanceState");
        outState.putBoolean("isShow",isShow);
    }

    public BaseFragment setLazyOpen(boolean lazyOpen) {
        isLazyOpen = lazyOpen;
        return this;
    }


    /**
     * 设置布局id
     * @return
     */
    protected abstract int setLayoutID();

    /**
     * 设置布局
     * @return
     */
    protected abstract void initView();

    /**
     * 加载数据
     */
    protected abstract void loadData();


    /**
     * eventbus在主线程接收方法
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN,sticky=true)
    public void onEventMainThread(BaseEventBusBean event) {
        if (event != null) {
            EventBean(event);
        }
    }

    protected abstract void EventBean(BaseEventBusBean event);

    @Override
    public void onStart() {
        super.onStart();
        if (isOpenEventBus){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isOpenEventBus){
            EventBus.getDefault().unregister(this);//反注册EventBus
        }
    }

    protected void initShow() {}

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(null!=mUnbinder&&mUnbinder != Unbinder.EMPTY){
            mUnbinder.unbind();
        }

        OkGo.getInstance().cancelTag(this);
    }

    /**
     * 检测网络是否连接
     */
    protected boolean isNetConnect(){
        return NetworkUtils.isConnected(mActivity); // NetUtil 是我自己封装的类
    }

    public boolean isOpenEventBus() {
        return isOpenEventBus;
    }

    public void setOpenEventBus(boolean openEventBus) {
        isOpenEventBus = openEventBus;
    }

    /**
     * 不关闭当前页面跳转
     * @param cls 要跳转的Activity
     */
    public void keepTogo(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(mActivity, cls);
        this.startActivity(intent);
    }
    /**
     * 不关闭当前页面跳转
     * @param intent intent活动
     */
    public void keepTogo(Intent intent) {
        this.startActivity(intent);
    }

    private RecyclerView list;

    public RecyclerView getList() {
        return list;
    }

    public void setList(RecyclerView list) {
        this.list = list;
    }

}
