package com.inwecrypto.wallet.ui.me.activity;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class CommonWebActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.web)
    FrameLayout web;
    @BindView(R.id.progress)
    MaterialProgressBar progress;
    @BindView(R.id.reload)
    TextView reload;

    private WebView mWebView = null;
    private final ReferenceQueue<WebView> WEB_VIEW_QUEUE = new ReferenceQueue<>();
    private String title;
    private String url;
    private RotateAnimation rotate;
    private boolean isFinish=true;

    @Override
    protected void getBundleExtras(Bundle extras) {
        title=extras.getString("title");
        url=extras.getString("url");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.discover_article_detail;
    }

    @Override
    protected void initView() {
        txtMainTitle.setText(title);
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Drawable drawable= getResources().getDrawable(R.mipmap.icon_refresh);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        txtRightTitle.setCompoundDrawables(drawable,null,null,null);

        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=mWebView){
                    startAnimat();
                    mWebView.reload();
                }
            }
        });

        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView.destroy();
        } else {
            final WeakReference<WebView> webViewWeakReference =
                    new WeakReference<>(new WebView(mActivity.getApplicationContext()), WEB_VIEW_QUEUE);
            mWebView = webViewWeakReference.get();
            mWebView = createWebView(mWebView);
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
                    ToastUtil.show("请检查网络是否连接！");
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
                // handler.cancel();      //表示挂起连接，为默认方式
                // handler.handleMessage(null);    //可做其他处理
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (progress!=null){
                    if (progress.getVisibility() == View.GONE) {
                        progress.setVisibility(View.VISIBLE);
                    }
                }
                if (reload!=null){
                    reload.setVisibility(View.GONE);
                }
            }


            // 旧版本，会在新版本中也可能被调用，所以加上一个判断，防止重复显示
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    return;
                }
                if (reload!=null){
                    reload.setVisibility(View.VISIBLE);
                }
                stopAnimat();
            }

            // 新版本，只会在Android6及以上调用
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (request.isForMainFrame()){ // 或者： if(request.getUrl().toString() .equals(getUrl()))
                    if (reload!=null){
                        reload.setVisibility(View.VISIBLE);
                    }
                }
                stopAnimat();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                stopAnimat();
            }
        };
    }


    public WebChromeClient initWebChromeClient(){
       return new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    if (progress!=null){
                        progress.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progress.setVisibility(View.GONE);
                                stopAnimat();
                            }
                        }, 600);
                    }
                }
            }


            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (title.contains("404")){
                    if (reload!=null){
                        reload.setVisibility(View.VISIBLE);
                    }
                }
            }
        };
    }

    public WebView createWebView(WebView webView) {

        WebView.setWebContentsDebuggingEnabled(true);
        //不能横向滚动
        webView.setHorizontalScrollBarEnabled(false);
        //不能纵向滚动
        webView.setVerticalScrollBarEnabled(false);
        //允许截图
        webView.setDrawingCacheEnabled(true);
        //屏蔽长按事件
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        //初始化WebSettings
        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //隐藏缩放控件
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        //禁止缩放
        settings.setSupportZoom(false);
        //文件权限
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowContentAccess(true);

        if (NetworkUtils.isConnected(mActivity.getApplicationContext())) {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
        }

        //缓存相关
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        return webView;
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
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
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
        mWebView.loadUrl(url);
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

    public  void startAnimat(){
        if (txtRightTitle==null){
            return;
        }
        isFinish=false;
        txtRightTitle.clearAnimation();

        rotate  = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setDuration(1600);//设置动画持续周期
        rotate.setRepeatCount(-1);//设置重复次数
        rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        rotate.setStartOffset(10);//执行前的等待时间
        txtRightTitle.setAnimation(rotate);
    }

    public void stopAnimat(){
        if (!isFinish){
            txtRightTitle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (rotate!=null){
                        rotate.cancel();
                    }
                    if (txtRightTitle!=null){
                        txtRightTitle.clearAnimation();
                    }
                    isFinish=true;
                }
            },1600);
        }

    }

}
