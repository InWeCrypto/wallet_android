package com.inwecrypto.wallet.ui.news.fragment;

import android.net.http.SslError;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.FocusFinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.TradingProjectDetaileBean;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.widget.BetterRecyclerView;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.NoTradingProjectActivity;
import com.inwecrypto.wallet.ui.news.TradingProjectActivity;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/3/15 11:10
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ProjectXiangmuFragment extends BaseFragment {
    @BindView(R.id.web)
    FrameLayout web;

    private WebView mWebView = null;
    private final ReferenceQueue<WebView> WEB_VIEW_QUEUE = new ReferenceQueue<>();

    private TradingProjectDetaileBean project;

    String htmlText = "<html>" + "<head>" + "<style type=\"text/css\">" + "body{padding-left: 10px;padding-right: 10px;}" + "</style>" + "</head>";
    private boolean isPull;
    private float y;
    private float x;

    @Override
    protected int setLayoutID() {
        return R.layout.project_xiangmugailan_fragment;
    }

    @Override
    protected void initView() {
        project = (TradingProjectDetaileBean) getArguments().getSerializable("project");
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

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(params);
        int padding= DensityUtil.dip2px(mContext,20);
        mWebView.setPadding(padding,padding,padding,padding);
        web.addView(mWebView);
        mWebView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                switch (ev.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        y=ev.getY();
                        x=ev.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mWebView.getScrollY()==0){
                            if ((ev.getY()-y)>=0){
                                isPull=true;
                            }else {
                                isPull=false;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                boolean isShow=false;
                if (getActivity() instanceof TradingProjectActivity){
                    isShow=((TradingProjectActivity)getActivity()).isShow();
                }
                if (getActivity() instanceof NoTradingProjectActivity){
                    isShow=((NoTradingProjectActivity)getActivity()).isShow();
                }

                if (isShow){
                    ((WebView)v).requestDisallowInterceptTouchEvent(false);
                }else {
                    if (mWebView.getScrollY()==0){
                        if (isPull){
                            ((WebView)v).requestDisallowInterceptTouchEvent(false);
                        }else {
                            ((WebView)v).requestDisallowInterceptTouchEvent(true);
                        }
                    }else {
                        ((WebView)v).requestDisallowInterceptTouchEvent(true);
                    }
                }

                return false;
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


            // 旧版本，会在新版本中也可能被调用，所以加上一个判断，防止重复显示
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("aaa","view.getHeight():"+view.getHeight());
                Log.e("aaa","view.getHeight():"+web.getHeight());
            }
        };
    }


    public WebChromeClient initWebChromeClient() {
        return new WebChromeClient();
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
    protected void loadData() {
        if (null!=project.getCategory_presentation()&&null!=project.getCategory_presentation().getContent()){
            mWebView.loadData(htmlText
                    +project.getCategory_presentation().getContent().replace("<!doctype html>","<!doctype html><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=5.0, user-scalable=0\">")
                    , "text/html; charset=UTF-8", null);
        }else {
            mWebView.loadData(htmlText, "text/html; charset=UTF-8", null);
        }
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
