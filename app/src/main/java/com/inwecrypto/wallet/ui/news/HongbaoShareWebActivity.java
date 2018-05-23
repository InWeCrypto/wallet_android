package com.inwecrypto.wallet.ui.news;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class HongbaoShareWebActivity extends BaseActivity {


    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    ImageView txtRightTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.web)
    FrameLayout web;
    @BindView(R.id.reload)
    TextView reload;
    @BindView(R.id.progress)
    MaterialProgressBar progress;

    private WebView mWebView = null;
    private final ReferenceQueue<WebView> WEB_VIEW_QUEUE = new ReferenceQueue<>();
    private String url;
    private String name;
    private String content;
    private boolean isShowShare;
    private String urlen;
    private String gntName;

    @Override
    protected void getBundleExtras(Bundle extras) {
        //检查版本
        checkSdkVersion();
        url = extras.getString("url");
        urlen=extras.getString("urlen");
        name = extras.getString("name");
        content = extras.getString("content");
        gntName=extras.getString("gntName");
        isShowShare = extras.getBoolean("showShare",true);
    }

    /**
     * 当系统版本大于5.0时 开启enableSlowWholeDocumentDraw 获取整个html文档内容
     */
    private void checkSdkVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }
    }

    @Override
    protected int setLayoutID() {
        return R.layout.hongbao_share_detail;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_HONGBAO_REFERS));
                finish();
            }
        });

        if (isShowShare){
            txtRightTitle.setVisibility(View.VISIBLE);
        }else {
            txtRightTitle.setVisibility(View.GONE);
        }
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                HongbaoShareFragment improt = new HongbaoShareFragment();
                improt.show(fm, "share");
                improt.setOnNextListener(new HongbaoShareFragment.OnNextInterface() {
                    @Override
                    public void onNext(int type, final Dialog dialog) {
                        switch (type) {
                            case 0:
                                Intent intent = new Intent(mActivity, HongbaoShareActivity.class);
                                intent.putExtra("url", url);
                                intent.putExtra("name", name);
                                intent.putExtra("content", content);
                                intent.putExtra("type", 0);
                                intent.putExtra("gntName",gntName);
                                keepTogo(intent);
                                dialog.dismiss();
                                break;
                            case 1:
                                Intent intent1 = new Intent(mActivity, HongbaoShareActivity.class);
                                intent1.putExtra("url", url);
                                intent1.putExtra("name", name);
                                intent1.putExtra("content", content);
                                intent1.putExtra("type", 1);
                                intent1.putExtra("gntName",gntName);
                                keepTogo(intent1);
                                dialog.dismiss();
                                break;
                            case 2:
                                Intent intent2 = new Intent(mActivity, HongbaoShareActivity.class);
                                intent2.putExtra("url", url);
                                intent2.putExtra("name", name);
                                intent2.putExtra("content", content);
                                intent2.putExtra("type", 2);
                                intent2.putExtra("gntName",gntName);
                                keepTogo(intent2);
                                dialog.dismiss();
                                break;
                            case 3:
                                try {
                                    Intent vIt = new Intent("android.intent.action.SEND");
                                    vIt.setPackage("org.telegram.messenger");
                                    vIt.setType("text/plain");
                                    vIt.putExtra(Intent.EXTRA_TEXT, urlen+"\n"+"You have a redpacket to pick up！");
                                    mActivity.startActivity(vIt);
                                } catch (Exception ex) {
                                    Log.e(TAG, "telegramShare:" + ex);
                                }
                                dialog.dismiss();
                                break;
                            case 4:
                                //这里分享一个链接，更多分享配置参考官方介绍：https://dev.twitter.com/twitterkit/android/compose-tweets
                                try {
                                    TweetComposer.Builder builder = new TweetComposer.Builder(mActivity)
                                            .text("You have a redpacket to pick up！")
                                            .url(new URL(urlen));
                                    builder.show();
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                });
            }
        });

        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView.destroy();
        } else {
            final WeakReference<WebView> webViewWeakReference =
                    new WeakReference<>(new WebView(mActivity.getApplicationContext()), WEB_VIEW_QUEUE);
            mWebView = webViewWeakReference.get();
            mWebView = AppUtil.createWebView(mWebView, mActivity);
            mWebView.setWebViewClient(initWebViewClient());
            mWebView.setWebChromeClient(initWebChromeClient());
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(params);
        web.addView(mWebView);

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtils.isConnected(mActivity.getApplicationContext())) {
                    if (null != mWebView) {
                        mWebView.reload();
                    }
                } else {
                    ToastUtil.show(R.string.qingjianchawangluoshifoulianjie);
                }
            }
        });

    }

    private WebViewClient initWebViewClient() {
        return new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();    //表示等待证书响应
                //super.onReceivedSslError(view, handler, error);
                // handler.cancel();      //表示挂起连接，为默认方式
                // handler.handleMessage(null);    //可做其他处理
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (progress != null) {
                    if (progress.getVisibility() == View.GONE) {
                        progress.setVisibility(View.VISIBLE);
                    }
                }
                if (reload != null) {
                    reload.setVisibility(View.GONE);
                }
            }


            // 旧版本，会在新版本中也可能被调用，所以加上一个判断，防止重复显示
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return;
                }
                if (reload != null) {
                    reload.setVisibility(View.VISIBLE);
                }
            }

            // 新版本，只会在Android6及以上调用
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (request.isForMainFrame()) { // 或者： if(request.getUrl().toString() .equals(getUrl()))
                    if (reload != null) {
                        reload.setVisibility(View.VISIBLE);
                    }
                }
            }
        };
    }


    public WebChromeClient initWebChromeClient() {
        return new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    if (progress != null) {
                        progress.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (progress != null) {
                                    progress.setVisibility(View.GONE);
                                }
                            }
                        }, 600);
                    }
                }
            }


            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (title.contains("404")) {
                    if (reload != null) {
                        reload.setVisibility(View.VISIBLE);
                    }
                }
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }


    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html; charset=UTF-8", null, null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }


    @Override
    protected void initData() {
        if (null != urlen) {
            mWebView.loadUrl(urlen);
        }

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_HONGBAO_REFERS));
    }
}
