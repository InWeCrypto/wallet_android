package com.inwecrypto.wallet.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.common.util.StringUtils;
import com.inwecrypto.wallet.event.BaseEventBusBean;

/**
 * Created by xiaoji on 2017/4/11.
 */

public abstract class BaseTabFragment extends Fragment {

    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    protected Activity mActivity;
    protected boolean isFirst;
    private Unbinder mUnbinder;
    protected boolean isOpenLazy=true;// 是否打开懒加载,打开的话将isOpenLazy设为true
    protected boolean isOpenEventBus;
    protected Context mContext;
    protected boolean isPullDown = true;
    protected int curentPage=1;
    private View viewContent;
    private boolean isInit;
    protected Fragment mFragment;


    /**
     * 获得全局的，防止使用getActivity()为空
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity)context;
        mContext = getContext();
        isFirst=true;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mFragment=this;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            viewContent = inflater.inflate(setLayoutID(), container, false);
            mUnbinder=  ButterKnife.bind(this, viewContent);
            initView();
            if (getUserVisibleHint()&&!isInit&&isAdded()) {
                loadData();
                isInit = true;
            }
        return viewContent;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser &&!isInit && isOpenLazy&&isAdded()) {
            loadData();
            isInit=true;
        }
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
    public void onDestroy() {
        super.onDestroy();

        if(null!=mUnbinder&&mUnbinder != Unbinder.EMPTY){
            mUnbinder.unbind();
        }

        OkGo.getInstance().cancelTag(this);
    }

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

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst){
            initShow();
        }else {
            isFirst=false;
        }
    }

    protected void initShow() {

    }

    /**
     * 检测网络是否连接
     */
    protected boolean isNetConnect(){
        return NetworkUtils.isConnected(mActivity); // NetUtil 是我自己封装的类
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


    /**
     * 显示Toast消息
     *
     * @param message 消息文本字符串
     */
    public final void showToast(@NonNull String message) {
        if (StringUtils.isEmpty(message)){return;}
        Toast.makeText(mActivity,message,Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示Toast消息
     *
     * @param resId 消息文本字符串资源ID
     */
    public final void showToast(@StringRes int resId) {
        Toast.makeText(mActivity,resId,Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示Toast消息
     *
     * @param message 消息文本字符串
     */
    public final void showLongToast(@NonNull String message) {
        if (StringUtils.isEmpty(message)){return;}
        Toast.makeText(mActivity,message,Toast.LENGTH_LONG).show();
    }

    /**
     * 显示Toast消息
     *
     * @param resId 消息文本字符串资源ID
     */
    public final void showLongToast(@StringRes int resId) {
        Toast.makeText(mActivity,resId,Toast.LENGTH_LONG).show();
    }
}
