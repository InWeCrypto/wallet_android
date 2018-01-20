package com.inwecrypto.wallet.ui.info;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * 作者：xiaoji06 on 2017/12/7 10:13
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class InfoWebFragment extends BaseFragment {


    @BindView(R.id.web)
    FrameLayout web;
    @BindView(R.id.reload)
    TextView reload;
    @BindView(R.id.progress)
    MaterialProgressBar progress;

    private WebView mWebView = null;
    private final ReferenceQueue<WebView> WEB_VIEW_QUEUE = new ReferenceQueue<>();
    private String url;


    @Override
    protected int setLayoutID() {
        return R.layout.info_fragment;
    }

    @Override
    protected void initView() {
        url= App.isMain? Url.WEB_ULR:Url.WEB_TEST_ULR;
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
            public void onClick(View v) {
                if (null!=mWebView){
                    mWebView.reload();
                }
            }
        });
    }

    @Override
    protected void loadData() {
        mWebView.loadUrl(url);
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

    private WebViewClient initWebViewClient() {
        return new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Intent intent = new Intent(mActivity, CommonWebActivity.class);
//                intent.putExtra("title", view.getTitle());
//                intent.putExtra("url", url);
//                keepTogo(intent);
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

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
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
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    public boolean canBack(){
        if (mWebView!=null){
            if (mWebView.canGoBack()){
                mWebView.goBack();
                return true;
            }
        }
        return false;
    }

}
