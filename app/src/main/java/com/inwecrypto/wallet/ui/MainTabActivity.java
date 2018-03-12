package com.inwecrypto.wallet.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.UpdateBean;
import com.inwecrypto.wallet.bean.YaoqinBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.MeApi;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.FragmentTabHost;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.service.DownloadService;
import com.inwecrypto.wallet.service.MessageService;
import com.inwecrypto.wallet.ui.me.MeFragment;
import com.inwecrypto.wallet.ui.me.activity.YaoqinActivity;
import com.inwecrypto.wallet.ui.newneo.WalletFragment;
import com.inwecrypto.wallet.ui.news.ZixunFragment;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.inwecrypto.wallet.common.http.Url.MAIN_YAOQIN;
import static com.inwecrypto.wallet.common.http.Url.TEST_YAOQIN;

/**
 * Created by xiaoji on 2017/7/14.
 * 功能描述：
 * 版本：@version
 */

public class MainTabActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.content)
    FrameLayout content;
    private LinearLayout[] tabs = new LinearLayout[3];
    private ImageView[] imgs = new ImageView[3];
    private TextView[] titles = new TextView[3];
    private int[] imgIds = new int[]{R.mipmap.zixun_ico_s, R.mipmap.qianbao_ico_s, R.mipmap.wode_ico_s};
    private int[] imgClickIds = new int[]{R.mipmap.zixun_ico, R.mipmap.qianbao_ico, R.mipmap.wode_ico};
    private int[] titleStrs = new int[]{R.string.zixun, R.string.qianbao, R.string.wode};
    private int[] colors = new int[]{Color.parseColor("#BBBBBB"), Color.parseColor("#008c55")};
    private BaseFragment[] fragments = new BaseFragment[3];
    private String[] tags = new String[]{"f1", "f2", "f3"};
    private FragmentManager manager;
    private int currentIndex = -1;

    private WebView mWebView;

    private boolean isYaoqin;

    private UpdateBean updateBean;
    private AlertDialog alertDialog;

    private boolean isStartService;

    private static final int REQUEST_PERMISSION_STORAGE = 0x01;
    private ProgressDialog pd6;

    @Override
    protected void getBundleExtras(Bundle extras) {
        isYaoqin = extras.getBoolean("isYaoqin", false);
    }

    @Override
    protected int setLayoutID() {
        return R.layout.activity_main_tab;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        tabs[0] = (LinearLayout) findViewById(R.id.tab1);
        tabs[1] = (LinearLayout) findViewById(R.id.tab2);
        tabs[2] = (LinearLayout) findViewById(R.id.tab3);
        for (int i = 0; i < 3; i++) {
            imgs[i] = (ImageView) tabs[i].findViewById(R.id.img);
            imgs[i].setImageResource(imgIds[i]);
            titles[i] = (TextView) tabs[i].findViewById(R.id.title);
            titles[i].setText(titleStrs[i]);
        }
        initTab(savedInstanceState);
        initListener();

        //预加载网页
        mWebView = new WebView(this);
        mWebView.loadUrl(Constant.MAIN_WEB);
        mWebView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mWebView.removeAllViews();
                mWebView.destroy();
                mWebView = null;
            }
        }, 2000);

        if (App.get().isLogin()){
            //开启服务
            startService(new Intent(mActivity, MessageService.class));

            if (isYaoqin && App.get().isZh()) {
                content.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ZixunApi.getYaoqinKey(this, new JsonCallback<LzyResponse<YaoqinBean>>() {
                            @Override
                            public void onSuccess(final Response<LzyResponse<YaoqinBean>> response) {
                                if (response.body().data.isCandy_bow_stat()) {
                                    //获取邀请码
                                    Intent intent = new Intent(mActivity, YaoqinActivity.class);
                                    String token = App.get().getSp().getString(App.isMain ? Constant.TOKEN : Constant.TEST_TOKEN);
                                    intent.putExtra("url", (App.isMain ? MAIN_YAOQIN : TEST_YAOQIN) + response.body().data.getCode() + "&token=" + token);
                                    keepTogo(intent);
                                }
                            }
                        });
                    }
                },2000);
            }
        }

        //检查更新
        checkUpdate();

        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();
    }

    private void checkUpdate() {
        MeApi.check(this, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                updateBean = GsonUtils.jsonToObj(response.body(), UpdateBean.class);
                if (Integer.parseInt(updateBean.getVersionCode()) > AppUtil.getVersion(mActivity)) {
                    isStartService = false;
                    //弹出下载框
                    alertDialog = new AlertDialog.Builder(mActivity)
                            .setTitle(R.string.banbengengxin)
                            .setMessage(updateBean.getUpdateHit())
                            .setCancelable(updateBean.getForce().equals("1") ? false : true)
                            .setPositiveButton(R.string.gengxin, null)
                            .setNegativeButton(R.string.quxiao, null)
                            .create();
                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isStartService) {
                                ToastUtil.show(getString(R.string.zhengzaihoutaixiazai));
                                return;
                            }
                            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
                            } else {
                                startUpdateService();
                            }
                        }
                    });

                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (updateBean.getForce().equals("0")) {
                                alertDialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }

    private void startUpdateService() {

        //显示一个
        alertDialog.dismiss();
        if (!updateBean.getForce().equals("0")) {
            isStartService = true;
        }
        ToastUtil.show(getString(R.string.zhengzaixiazaizhong));
        Intent intent = new Intent(mActivity, DownloadService.class);
        intent.putExtra("version", updateBean.getVersionCode());
        intent.putExtra("hash", updateBean.getHash());
        mActivity.startService(intent);

        pd6 = new ProgressDialog(this);
        pd6.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        pd6.setCancelable(false);// 设置是否可以通过点击Back键取消
        pd6.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        pd6.setTitle(R.string.tishi);
        pd6.setMax(100);
        if (updateBean.getForce().equals("0")) {
            pd6.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.houtaixiazai), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            pd6.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.quxiao), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    stopService(new Intent(mActivity, DownloadService.class));
                    dialog.dismiss();
                }
            });
        }
        pd6.setMessage(getString(R.string.zhengzaixiazaizhong));
        pd6.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //获取权限
                startUpdateService();
            } else {
                ToastUtil.show(getString(R.string.quanxianbeijingzhi_wufaxiazaiapk));
            }
        }
    }

    private void initTab(Bundle savedInstanceState) {
        manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        int index = 1;
        if (null != savedInstanceState) {
            for (int i = 0; i < 3; i++) {
                if (null != manager.findFragmentByTag(tags[i])) {
                    fragments[i] = (BaseFragment) manager.findFragmentByTag(tags[i]);
                    fragments[i].setUserVisibleHint(false);
                    if (fragments[i].isShow) {
                        index = i + 1;
                    }
                    ft.hide(fragments[i]);
                }
            }
            ft.commitAllowingStateLoss();
        }

        changeTab(index);
    }

    private void initListener() {
        for (int i = 0; i < 3; i++) {
            tabs[i].setOnClickListener(this);
        }
    }

    private void changeTab(int i) {

        if (currentIndex == i) {
            return;
        }

        int befer = currentIndex;

        FragmentTransaction ft = manager.beginTransaction();

        if (currentIndex != -1 && null != fragments[currentIndex - 1]) {
            ft.hide(fragments[currentIndex - 1]);
            fragments[currentIndex - 1].setUserVisibleHint(false);
        }

        switch (i) {
            case 1:
                addTab(ft, 1, null == fragments[0] ? new ZixunFragment() : null);
                break;
            case 2:
                addTab(ft, 2, null == fragments[1] ? new WalletFragment() : null);
                break;
            case 3:
                addTab(ft, 3, null == fragments[2] ? new MeFragment() : null);
                break;
        }

        ft.commitAllowingStateLoss();

        changeIcon(befer - 1, i - 1);
    }

    private void changeIcon(int current, int i) {
        if (current != -2) {
            imgs[current].setImageResource(imgIds[current]);
            titles[current].setTextColor(colors[0]);
        }
        imgs[i].setImageResource(imgClickIds[i]);
        titles[i].setTextColor(colors[1]);
    }

    private void addTab(FragmentTransaction ft, int index, BaseFragment fragment) {
        int position = index - 1;
        if (fragments[position] == null) {
            fragments[position] = fragment;
            ft.add(R.id.content, fragment, tags[position]);
        }
        ft.show(fragments[position]);
        fragments[position].setUserVisibleHint(true);
        currentIndex = index;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab1:
                changeTab(1);
                break;
            case R.id.tab2:
                changeTab(2);
                break;
            case R.id.tab3:
                changeTab(3);
                break;
        }
    }


    @Override
    protected void initData() {
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_DOWNLOAD_UPDATE) {
            if (null != pd6) {
                pd6.setProgress(event.getKey1());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //1.点击返回键条件成立
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {

            //2.点击的时间差如果大于2000，则提示用户点击两次退出
            if (System.currentTimeMillis() - mExitTime > 2000) {
                //3.保存当前时间
                mExitTime = System.currentTimeMillis();
                //4.提示
                ToastUtil.show(getString(R.string.tuichuapp));
            } else {
                //5.点击的时间差小于2000，退出。
                AppManager.getAppManager().finishAllActivity();
                // 杀掉进程
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (App.get().getSp().getBoolean(Constant.NEED_RESTART, false)) {
            App.get().getSp().putBoolean(Constant.NEED_RESTART, false);
            Intent restart = getIntent();
            finish();
            startActivity(restart);
        }

        if (intent.getBooleanExtra("isRe", false)) {
            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_ZIXUN_MESSAGE));
        }
    }
}
