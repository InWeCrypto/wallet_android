package com.inwecrypto.wallet.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.inwecrypto.wallet.AppApplication;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.BarUtils;
import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.common.util.StringUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.login.LoginActivity;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 2017/7/14.
 */

public abstract class BaseActivity extends AppCompatActivity{

    private static final int SLIDE_TRANSITION_TIME = 1 * 1000;//滑动转场的时间
    protected Fade mFadeTransition;
    protected Activity mActivity;

    private Unbinder mUnbinder;

    protected boolean isPullDown = true;
    protected int curentPage=1;

    private MaterialDialog dialog;
    private View dialogView;

    private MaterialDialog fixDialog;
    private View fixDialogView;

    protected MaterialDialog netDialog;
    private View netView;

    protected boolean isOpenEventBus;


    /** 日志输出标志 **/
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=this;
        AppManager.getAppManager().addActivity(this);
        Bundle extras = getIntent().getExtras();
        if (null!=extras){
            getBundleExtras(extras);
        }
        setContentView(setLayoutID());
        EventBus.getDefault().register(this);
        mUnbinder=ButterKnife.bind(this);
        setPresenter();
        initView();
        initData();
        setupWindowAnimation();//5.0以上的动画
        BarUtils.statusBarLightMode(this);
    }

    /**
     * Bundle  传递数据
     *
     * @param extras
     */
    protected abstract void getBundleExtras(Bundle extras);

    /**
     * 设置布局id
     * @return
     */
    protected abstract int setLayoutID();

    /**
     * 初始化presenter
     */
    protected void setPresenter() {}

    /**
     * 初始化布局
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();


    /**
     * eventbus在主线程接收方法
     *
     * @param event
     */
    @Subscribe (threadMode = ThreadMode.MAIN,sticky = true)
    public void onEventMainThread(BaseEventBusBean event) {
        if (event != null) {
            if (event.getEventCode()== Constant.EVENT_TOKEN){
                ToastUtil.show("验证过期，请重新登录");
                keepTogo(LoginActivity.class);
            }else {
                if (AppApplication.get().getSp().getBoolean(Constant.IS_CLOD,false)){
                    if (event.getEventCode() == Constant.EVENT_CLOD_NET){
                        //启动dialog
                        if (null==netDialog){
                            showNetDialog();
                        }
                    }
                    if (event.getEventCode() == Constant.EVENT_CLOD_UNNET){
                        //隐藏dialog
                        if (null!=netDialog){
                            netDialog.dismiss();
                            netDialog=null;
                        }
                    }
                }
                EventBean(event);
            }
        }
    }

    protected void showNetDialog() {
        netView= LayoutInflater.from(this).inflate(R.layout.view_dialog_net,null);
        View ok=netView.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isConnected(mActivity)) {
                    ToastUtil.show("请确保手机处于飞行模式！");
                    return;
                }else {
                    netDialog.dismiss();
                }
            }
        });
        netDialog = new MaterialDialog(mActivity).setView(netView);
        netDialog.setBackgroundResource(R.drawable.trans_bg);
        netDialog.setCanceledOnTouchOutside(false);
        netDialog.show();
    }

    /**
     * EventBus接收信息的方法，开启后才会调用
     *
     * @param event
     */
    protected abstract void EventBean(BaseEventBusBean event);

    @Override
    protected void onDestroy() {
        OkGo.getInstance().cancelTag(this);
        EventBus.getDefault().unregister(this);
        if(mUnbinder !=Unbinder.EMPTY){
            mUnbinder.unbind();
        }
        destroyPresenter();

        AppManager.getAppManager().finishActivity(this);
        super.onDestroy();
    }

    protected void showLoading(){
        if (null==dialog){
            dialogView= LayoutInflater.from(this).inflate(R.layout.view_loading,null);
            dialog = new MaterialDialog(mActivity).setView(dialogView);
            dialog.setBackgroundResource(R.drawable.trans_bg);
            dialog.setCanceledOnTouchOutside(true);
        }
        if (null!=dialog){
            dialog.show();
        }
    }

    protected void showFixLoading(){
        if (null==fixDialog){
            fixDialogView= LayoutInflater.from(this).inflate(R.layout.view_loading,null);
            fixDialog = new MaterialDialog(mActivity).setView(fixDialogView);
            fixDialog.setBackgroundResource(R.drawable.trans_bg);
            fixDialog.setCanceledOnTouchOutside(false);
        }
        if (null!=fixDialog){
            fixDialog.show();
        }
    }

    protected void hideFixLoading(){
        if (null!=fixDialog){
            fixDialog.dismiss();
        }
    }

    protected  void hideLoading(){
        if (null!=dialog){
            dialog.dismiss();
        }
    }



    /**
     * 清除presenter防止内存泄漏
     */
    protected void destroyPresenter() {}

    /**
     * 设置动画
     */
    protected  void setupWindowAnimation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFadeTransition = new Fade();
            mFadeTransition.setDuration(SLIDE_TRANSITION_TIME);
            getWindow().setEnterTransition(mFadeTransition);
            getWindow().setExitTransition(mFadeTransition);
        }
    }
    /**
     * 检测网络是否连接
     */
    protected boolean isNetConnect(){
        return NetworkUtils.isConnected(this); // NetUtil 是我自己封装的类
    }

    /**
     * 关闭当前页面跳转
     * @param cls 要跳转的Activity
     */
    public void finshTogo(Class<?> cls) {
        keepTogo(cls);
        this.finish();
    }
    /**
     * 关闭当前页面跳转
     * @param intent intent活动
     */
    public void finshTogo(Intent intent) {
        keepTogo(intent);
        this.finish();
    }
    /**
     * 关闭当前页面跳转
     * @param cls 要跳转的Activity
     * @param bundle Bundle对象
     */
    public void finshTogo(Class<?> cls, Bundle bundle) {
        keepTogo(cls, bundle);
        this.finish();
    }
    /**
     * 不关闭当前页面跳转
     * @param cls 要跳转的Activity
     */
    public void keepTogo(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
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
     * 不关闭当前页面跳转
     * @param cls 要跳转的Activity
     * @param bundle Bundle对象
     */
    public void keepTogo(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(this, cls);
        this.startActivity(intent);
    }
    /**
     * 返回结果页面跳转
     * @param intent Intent对象
     * @param requestCode 访问代码
     */
    public void forResultTogo(Intent intent, int requestCode) {
        this.startActivityForResult(intent,requestCode);
    }

    /**
     * 显示Toast消息
     *
     * @param message 消息文本字符串
     */
    public final void showToast(@NonNull String message) {
        if (StringUtils.isEmpty(message)){return;}
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示Toast消息
     *
     * @param resId 消息文本字符串资源ID
     */
    public final void showToast(@StringRes int resId) {
        Toast.makeText(this,resId,Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示Toast消息
     *
     * @param message 消息文本字符串
     */
    public final void showLongToast(@NonNull String message) {
        if (StringUtils.isEmpty(message)){return;}
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    /**
     * 显示Toast消息
     *
     * @param resId 消息文本字符串资源ID
     */
    public final void showLongToast(@StringRes int resId) {
        Toast.makeText(this,resId,Toast.LENGTH_LONG).show();
    }


}
