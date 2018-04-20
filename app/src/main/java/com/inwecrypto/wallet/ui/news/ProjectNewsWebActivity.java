package com.inwecrypto.wallet.ui.news;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.login.LoginActivity;
import com.lzy.okgo.model.Response;
import com.readystatesoftware.chuck.internal.ui.MainActivity;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class ProjectNewsWebActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{

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
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 200;

    private String decs;
    private String img;
    private Tencent mTencent;

    @Override
    protected void getBundleExtras(Bundle extras) {
        //检查版本
        checkSdkVersion();
        title = extras.getString("title");
        url = extras.getString("url");
        content = extras.getString("content");
        isShowMore = extras.getBoolean("show", true);
        id = extras.getString("id");
        decs=extras.getString("decs");
        img=extras.getString("img");
    }

    /**
     * 当系统版本大于5.0时 开启enableSlowWholeDocumentDraw 获取整个html文档内容
     */
    private void checkSdkVersion() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }
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
        mTencent = Tencent.createInstance("1106826620", App.get());

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
                                    shareWeixin(true);
//                                    Intent wechatClircleIntent = new Intent(Intent.ACTION_SEND);
//                                    wechatClircleIntent.setPackage("com.tencent.mm");
//                                    wechatClircleIntent.setType("text/plain");
//                                    // 分享出去的标题
//                                    wechatClircleIntent.putExtra(Intent.EXTRA_TEXT, title+"\n"+url.replace("newsdetail2","newsdetail"));
//                                    startActivity(wechatClircleIntent);
                                    dialog.dismiss();
                                    break;
                                case 1:
                                    shareWeixin(false);
//                                    Intent wechatIntent = new Intent(Intent.ACTION_SEND);
//                                    wechatIntent.setPackage("com.tencent.mm");
//                                    wechatIntent.setType("text/plain");
//                                    wechatIntent.putExtra(Intent.EXTRA_TEXT, title + "\n" + url.replace("newsdetail2","newsdetail"));
//                                    startActivity(wechatIntent);
                                    dialog.dismiss();
                                    break;
                                case 2:
                                    final Bundle params = new Bundle();
                                    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                                    params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
                                    params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  content);
                                    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  url);
                                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, img);
                                    params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "InweCrypto");
                                    mTencent.shareToQQ(mActivity, params, null);

//                                    Intent qqIntent = new Intent(Intent.ACTION_SEND);
//                                    qqIntent.setPackage("com.tencent.mobileqq");
//                                    qqIntent.setType("text/plain");
//                                    qqIntent.putExtra(Intent.EXTRA_TEXT, title + "\n" + url.replace("newsdetail2","newsdetail"));
//                                    startActivity(qqIntent);
//                                    dialog.dismiss();
                                    break;
                                case 3:
                                    try {
                                        Intent vIt = new Intent("android.intent.action.SEND");
                                        vIt.setPackage("org.telegram.messenger");
                                        vIt.setType("text/plain");
                                        vIt.putExtra(Intent.EXTRA_TEXT, title + "\n" + url.replace("newsdetail2","newsdetail"));
                                        mActivity.startActivity(vIt);
                                    } catch (Exception ex) {
                                        Log.e(TAG, "telegramShare:" + ex);
                                    }
                                    dialog.dismiss();
                                    break;
                                case 4:

                                    break;
                                case 5:
                                    if (null != id) {
                                        if (!App.get().isLogin()){
                                            keepTogo(LoginActivity.class);
                                            return;
                                        }
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
                                    //这里分享一个链接，更多分享配置参考官方介绍：https://dev.twitter.com/twitterkit/android/compose-tweets
                                    try {
                                        TweetComposer.Builder builder = new TweetComposer.Builder(mActivity)
                                                .text(title)
                                                .url(new URL(url.replace("newsdetail2","newsdetail")));
                                        builder.show();
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case 7:
                                    dialog.dismiss();
                                    //申请权限
                                    if (EasyPermissions.hasPermissions(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                        getSnapshot();
                                    }else {
                                        EasyPermissions.requestPermissions(mActivity, getString(R.string.duxiequanxian),
                                                WRITE_EXTERNAL_STORAGE_REQUEST_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                    }
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

    private void shareWeixin(final boolean isCircle) {

        if (null!=img&&!img.equals("")){
            Glide.with(this).load(img).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    sendToweixin(resource, isCircle);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    sendToweixin(null, isCircle);
                }
            });
        }else {
            sendToweixin(null, isCircle);
        }

    }

    private void sendToweixin(Bitmap resource, boolean isCircle) {
        WXWebpageObject webpage=new WXWebpageObject();
        webpage.webpageUrl=url.replace("newsdetail2","newsdetail");

        WXMediaMessage msg=new WXMediaMessage(webpage);
        msg.title=title;
        msg.description=decs;
        if (null!=resource){
            msg.thumbData=bmpToByteArray(resource,32600);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = isCircle?
                SendMessageToWX.Req.WXSceneTimeline:
                SendMessageToWX.Req.WXSceneSession;

        if (App.api == null) {
            App.api = WXAPIFactory.createWXAPI(mActivity, App.APP_ID, true);
        }
        App.api.sendReq(req);
    }

    /**
     * Bitmap转换成byte[]并且进行压缩,压缩到不大于maxkb
     * @param bitmap
     * @param maxkb
     * @return
     */
    public static byte[] bmpToByteArray(Bitmap bitmap, int maxkb) {
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        thumbBmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        int options = 100;
        while (output.toByteArray().length > maxkb&& options != 10) {
            output.reset(); //清空output
            thumbBmp.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到output中
            options -= 10;
        }
        return output.toByteArray();
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        getSnapshot();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
    /**
     * 获得快照
     */
    private void getSnapshot() {

        showFixLoading();

        float scale = mWebView.getScale();
        int webViewHeight = (int) (mWebView.getContentHeight()*scale+0.5);
        final Bitmap bitmap = Bitmap.createBitmap(mWebView.getWidth(),webViewHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mWebView.draw(canvas);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmapC=compressImage(bitmap,1024);
                int width=0;
                int height=0;
                try {
                    // 首先保存图片
                    File appDir = new File(Environment.getExternalStorageDirectory(), "InWeCrypto/cache");
                    if (!appDir.exists()) {
                        appDir.mkdirs();
                    }
                    final String fileName="/InWeCrypto/cache/webview_capture_"+id+"_"+System.currentTimeMillis()+".jpg";
                    String filePath = Environment.getExternalStorageDirectory().getPath()+fileName;
                    FileOutputStream fos = new FileOutputStream(filePath);
                    //压缩bitmap到输出流中
                    bitmapC.compress(Bitmap.CompressFormat.JPEG, 70, fos);
                    width=bitmapC.getWidth();
                    height=bitmapC.getHeight();
                    fos.close();
                    bitmapC.recycle();
                    bitmap.recycle();

                    final int finalWidth = width;
                    final int finalHeight = height;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //跳转页面
                            Intent intent=new Intent(mActivity,NewsShareActivity.class);
                            intent.putExtra("file",fileName);
                            intent.putExtra("id",id);
                            intent.putExtra("width", finalWidth);
                            intent.putExtra("height", finalHeight);
                            keepTogo(intent);
                        }
                    });

                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(getString(R.string.tupianbaocunshibai));
                        }
                    });
                    return;
                }finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideFixLoading();
                        }
                    });
                }
            }
        }).start();

    }

    /**
     * 压缩图片
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image,int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > size) {
            // 重置baos
            baos.reset();
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            // 每次都减少10
            options -= 15;
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
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
