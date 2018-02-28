package com.inwecrypto.wallet.base;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.BarUtils;
import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.login.LoginActivity;
import com.inwecrypto.wallet.common.widget.MaterialDialog;

import java.util.Locale;

/**
 * Created by Administrator on 2017/7/14.
 */

public abstract class BaseActivity extends AppCompatActivity{

    private static final int SLIDE_TRANSITION_TIME = 1 * 1000;//滑动转场的时间
    protected Fade mFadeTransition;
    protected BaseActivity mActivity;

    private Unbinder mUnbinder;

    private MaterialDialog dialog;
    private View dialogView;

    private MaterialDialog fixDialog;
    private View fixDialogView;

    protected boolean isOpenEventBus;

    private boolean isLightMode=true;

    public static final String EXTRA_TRANSITION = "EXTRA_TRANSITION";
    public static final String TRANSITION_FADE_FAST = "FADE_FAST";
    public static final String TRANSITION_FADE_SLOW = "FADE_SLOW";
    public static final String TRANSITION_SLIDE_RIGHT = "SLIDE_RIGHT";
    public static final String TRANSITION_SLIDE_BOTTOM = "SLIDE_BOTTOM";
    public static final String TRANSITION_EXPLODE = "EXPLODE";
    public static final String TRANSITION_EXPLODE_BOUNCE = "EXPLODE_BOUNCE";


    /** 日志输出标志 **/
    protected final String TAG = this.getClass().getSimpleName();
    private View title;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=this;
        AppManager.getAppManager().addActivity(this);
        Bundle extras = getIntent().getExtras();
        if (null!=extras){
            getBundleExtras(extras);
        }
        String transition = getIntent().getStringExtra(EXTRA_TRANSITION);
        if (null != transition) {
            switch (transition) {
                case TRANSITION_SLIDE_RIGHT:
                    Transition transitionSlideRight =
                            TransitionInflater.from(this).inflateTransition(R.transition.slide_right);
                    getWindow().setEnterTransition(transitionSlideRight);
                    break;
                case TRANSITION_SLIDE_BOTTOM:
                    Transition transitionSlideBottom =
                            TransitionInflater.from(this).inflateTransition(R.transition.slide_bottom);
                    getWindow().setEnterTransition(transitionSlideBottom);
                    break;
                case TRANSITION_FADE_FAST:
                    Transition transitionFadeFast =
                            TransitionInflater.from(this).inflateTransition(R.transition.fade_fast);
                    getWindow().setEnterTransition(transitionFadeFast);
                    break;
                case TRANSITION_FADE_SLOW:
                    Transition transitionFadeSlow =
                            TransitionInflater.from(this).inflateTransition(R.transition.fade_slow);
                    getWindow().setEnterTransition(transitionFadeSlow);
                    break;
                case TRANSITION_EXPLODE:
                    Transition transitionExplode =
                            TransitionInflater.from(this).inflateTransition(R.transition.explode);
                    getWindow().setEnterTransition(transitionExplode);
                    break;
                case TRANSITION_EXPLODE_BOUNCE:
                    Transition transitionExplodeBounce =
                            TransitionInflater.from(this).inflateTransition(R.transition.explode_bounce);
                    getWindow().setEnterTransition(transitionExplodeBounce);
                    break;
            }
        }
        setContentView(setLayoutID());
        EventBus.getDefault().register(this);
        mUnbinder=ButterKnife.bind(this);
        initView();
        initView(savedInstanceState);
        initData();
        setupWindowAnimation();//5.0以上的动画
        BarUtils.statusBarLightMode(this,isLightMode());
        if (!App.isMain){
            addWaringView();
        }
    }

    @Override
    protected void onResume() {
        // 语言状态检测
        recoverLanguage();
        super.onResume();
    }

    /**
     * 通过updateConfiguration方法修改Resource的Locale,连带修改Resource内Asset的Local.
     */
    private void recoverLanguage() {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        // 从Preference中取出语言设置
        configuration.locale = App.get().getSp().getBoolean(Constant.IS_CHINESE)?Locale.CHINESE:Locale.ENGLISH;
        resources.updateConfiguration(configuration, metrics);
    }

    protected void addWaringView() {
        title=LayoutInflater.from(this).inflate(R.layout.test_title_view,null,false);
        title.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        View view=getWindow().getDecorView();
        if (view instanceof ViewGroup){
            ViewGroup root= (ViewGroup) view;
            root.addView(title);
        }
    }

    protected void clearView(){
        if (null!=title){
            View view=title.getRootView();
            if (view instanceof ViewGroup){
                ViewGroup root= (ViewGroup) view;
                root.removeView(title);
            }
        }
    }

    protected void initView(Bundle savedInstanceState) {
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
     * 初始化布局
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    public boolean isLightMode() {
        return isLightMode;
    }

    public void setLightMode(boolean lightMode) {
        isLightMode = lightMode;
    }

    /**
     * eventbus在主线程接收方法
     *
     * @param event
     */
    @Subscribe (threadMode = ThreadMode.MAIN,sticky = true)
    public void onEventMainThread(BaseEventBusBean event) {
        if (event != null) {
            if (event.getEventCode()== Constant.EVENT_TOKEN){
                ToastUtil.show(getString(R.string.token_error));
                keepTogo(LoginActivity.class);
            }else {
                EventBean(event);
            }
        }
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

    protected boolean isFixLoadingShow(){
        if (null!=fixDialog){
            return fixDialog.isShow();
        }
        return false;
    }

    public void hideFixLoading(){
        if (null!=fixDialog){
            fixDialog.dismiss();
        }
    }

    public  void hideLoading(){
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
     *
     * @param intent intent活动
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void finshTogoTransition(Intent intent, String transition) {
        intent.putExtra(EXTRA_TRANSITION, transition);
        ActivityOptions transitionActivity =
                ActivityOptions.makeSceneTransitionAnimation(this);
        startActivity(intent, transitionActivity.toBundle());
        this.finish();
    }

    /**
     * 不关闭当前页面跳转
     *
     * @param intent 要跳转的Activity
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void keepTogoTransition(Intent intent,String transition) {
        intent.putExtra(EXTRA_TRANSITION, transition);
        ActivityOptions transitionActivity =
                ActivityOptions.makeSceneTransitionAnimation(this);
        startActivity(intent, transitionActivity.toBundle());
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
}
