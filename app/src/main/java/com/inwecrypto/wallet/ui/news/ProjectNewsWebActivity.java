package com.inwecrypto.wallet.ui.news;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.lzy.okgo.model.Response;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class ProjectNewsWebActivity extends BaseActivity {

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
    private String title;
    private String url;
    private RotateAnimation rotate;
    private boolean isFinish = true;
    private String content;
    private boolean isShowMore = true;
    private String id;

    @Override
    protected void getBundleExtras(Bundle extras) {
        title = extras.getString("title");
        url = extras.getString("url");
        content = extras.getString("content");
        isShowMore = extras.getBoolean("show", true);
        id = extras.getString("id");
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

        if (isShowMore) {
            txtRightTitle.setVisibility(View.VISIBLE);
            txtRightTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getSupportFragmentManager();
                    NewsShareFragment improt = new NewsShareFragment();
                    improt.show(fm, "share");
                    improt.setOnNextListener(new NewsShareFragment.OnNextInterface() {
                        @Override
                        public void onNext(int type, final Dialog dialog) {
                            switch (type) {
                                case 0:
                                    Intent wechatClircleIntent = new Intent(Intent.ACTION_SEND);
                                    wechatClircleIntent.setPackage("com.tencent.mm");
                                    wechatClircleIntent.setType("text/plain");
                                    wechatClircleIntent.putExtra(Intent.EXTRA_TEXT, title + "\n" + url);
                                    startActivity(wechatClircleIntent);
                                    dialog.dismiss();
                                    break;
                                case 1:
                                    Intent wechatIntent = new Intent(Intent.ACTION_SEND);
                                    wechatIntent.setPackage("com.tencent.mm");
                                    wechatIntent.setType("text/plain");
                                    wechatIntent.putExtra(Intent.EXTRA_TEXT, title + "\n" + url);
                                    startActivity(wechatIntent);
                                    dialog.dismiss();
                                    break;
                                case 2:
                                    Intent qqIntent = new Intent(Intent.ACTION_SEND);
                                    qqIntent.setPackage("com.tencent.mobileqq");
                                    qqIntent.setType("text/plain");
                                    qqIntent.putExtra(Intent.EXTRA_TEXT, title + "\n" + url);
                                    startActivity(qqIntent);
                                    dialog.dismiss();
                                    break;
                                case 3:
                                    Uri content_url = Uri.parse("https://t.me/share/url?text=" + title + "&url=" + url + "");
                                    Intent intent = new Intent(Intent.ACTION_VIEW, content_url);
                                    startActivity(intent);
                                    dialog.dismiss();
                                    break;
                                case 4:

                                    break;
                                case 5:
                                    if (null != id) {
                                        ZixunApi.newsCollect(this, id, true, new JsonCallback<LzyResponse<Object>>() {
                                            @Override
                                            public void onSuccess(Response<LzyResponse<Object>> response) {
                                                ToastUtil.show(getString(R.string.shoucangchenggong));
                                                dialog.dismiss();

                                            }

                                            @Override
                                            public void onError(Response<LzyResponse<Object>> response) {
                                                super.onError(response);
                                                ToastUtil.show(getString(R.string.caozuoshibai));
                                            }
                                        });
                                    }
                                    break;
                                case 6:
                                    ToastUtil.show(getString(R.string.jingqingqidai));
                                    break;
                            }
                        }
                    });
                }
            });
        } else {
            txtRightTitle.setVisibility(View.GONE);
        }

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
                super.onReceivedSslError(view, handler, error);
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
                stopAnimat();
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
                stopAnimat();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                stopAnimat();
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
                                    stopAnimat();
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
        if (null != url) {
            mWebView.loadUrl(url);
        }

        if (null != content) {
            mWebView.loadData(content, "text/html; charset=UTF-8", null);
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

    public void startAnimat() {
        if (txtRightTitle == null) {
            return;
        }
        isFinish = false;
        txtRightTitle.clearAnimation();

        rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setDuration(1600);//设置动画持续周期
        rotate.setRepeatCount(-1);//设置重复次数
        rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        rotate.setStartOffset(10);//执行前的等待时间
        txtRightTitle.setAnimation(rotate);
    }

    public void stopAnimat() {
        if (!isFinish) {
            txtRightTitle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (rotate != null) {
                        rotate.cancel();
                    }
                    if (txtRightTitle != null) {
                        txtRightTitle.clearAnimation();
                    }
                    isFinish = true;
                }
            }, 1600);
        }

    }
}
