package com.inwecrypto.wallet.ui.me.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.HongbaoShareFragment;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import net.qiujuer.genius.ui.widget.Button;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/5/17 20:22
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class AppShareActivity extends BaseActivity {
    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.code)
    ImageView code;
    @BindView(R.id.share)
    Button share;

    private Tencent mTencent;
    private String chUrl="http://inwecrypto.com/zh/platform";
    private String enUrl="http://inwecrypto.com/en/platform";
    private String url;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.app_share_activity;
    }

    @Override
    protected void initView() {
        mTencent = Tencent.createInstance("1106826620", App.get());

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (!App.get().isZh()){
            code.setImageResource(R.mipmap.app_code_en);
            url=enUrl;
        }else {
            url=chUrl;
        }

        share.setOnClickListener(new View.OnClickListener() {
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
                                shareWeixin(true);
                                dialog.dismiss();
                                break;
                            case 1:
                                shareWeixin(false);
                                dialog.dismiss();
                                break;
                            case 2:
                                final Bundle params = new Bundle();
                                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                                params.putString(QQShare.SHARE_TO_QQ_TITLE, getString(R.string.appfenxiangtitle));
                                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  url);
                                params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "InweCrypto");
                                mTencent.shareToQQ(mActivity, params, null);
                                dialog.dismiss();
                                break;
                            case 3:
                                try {
                                    Intent vIt = new Intent("android.intent.action.SEND");
                                    vIt.setPackage("org.telegram.messenger");
                                    vIt.setType("text/plain");
                                    vIt.putExtra(Intent.EXTRA_TEXT, getString(R.string.appfenxiangtitle) + "\n" + url);
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
                                            .text(getString(R.string.appfenxiangtitle))
                                            .url(new URL(url));
                                    builder.show();
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                                dialog.dismiss();
                                break;
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void initData() {

    }

    private void shareWeixin(final boolean isCircle) {

        sendToweixin(null, isCircle);
    }

    private void sendToweixin(Bitmap resource, boolean isCircle) {
        WXWebpageObject webpage=new WXWebpageObject();
        webpage.webpageUrl=url;

        WXMediaMessage msg=new WXMediaMessage(webpage);
        msg.title=getString(R.string.appfenxiangtitle);
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


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
