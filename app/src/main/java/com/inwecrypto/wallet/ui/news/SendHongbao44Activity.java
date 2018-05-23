package com.inwecrypto.wallet.ui.news;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.HongbaoFeeBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.login.LoginActivity;
import com.lzy.okgo.model.Response;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.Tencent;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import net.qiujuer.genius.ui.widget.Button;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 作者：xiaoji06 on 2018/4/23 12:29
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class SendHongbao44Activity extends BaseActivity {

    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.et_info)
    EditText etInfo;
    @BindView(R.id.next)
    Button next;

    private String id;
    private String redbagAddress;
    private boolean isShare;
    private String gntName;

    @Override
    protected void getBundleExtras(Bundle extras) {
        id=extras.getString("id");
        redbagAddress=extras.getString("redbagAddress");
        gntName=extras.getString("gntName");
        isShare=extras.getBoolean("isShare");
    }


    @Override
    protected int setLayoutID() {
        return R.layout.send_hongbao_44_activity;
    }

    @Override
    protected void initView() {

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_HONGBAO_REFERS));
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().length()==0){
                    ToastUtil.show(R.string.qingtianxiefasongrenmincheng);
                    return;
                }

                if (etInfo.getText().toString().length()==0){
                    ToastUtil.show(R.string.qingtianxieliuyan);
                    return;
                }

                if (isShare){
                    shareWindow();
                }else {
                    showFixLoading();
                    ZixunApi.sendRedbag(this
                            , id
                            , redbagAddress
                            , "2"
                            , ""
                            , name.getText().toString()
                            , etInfo.getText().toString()
                            , new JsonCallback<LzyResponse<HongbaoFeeBean>>() {
                                @Override
                                public void onSuccess(Response<LzyResponse<HongbaoFeeBean>> response) {
                                    isShare=true;
                                    EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_HONGBAO_SHARE,id));
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            shareWindow();
                                        }
                                    });
                                }

                                @Override
                                public void onError(Response<LzyResponse<HongbaoFeeBean>> response) {
                                    super.onError(response);
                                    ToastUtil.show(R.string.fenxiangshibaiqingchongshi);
                                }

                                @Override
                                public void onFinish() {
                                    super.onFinish();
                                    hideFixLoading();
                                }
                            });

                }
            }});
    }

    private void shareWindow() {
        FragmentManager fm = getSupportFragmentManager();
        HongbaoShareFragment improt = new HongbaoShareFragment();
        improt.show(fm, "share");
        improt.setOnNextListener(new HongbaoShareFragment.OnNextInterface() {
            @Override
            public void onNext(int type, final Dialog dialog) {
                String username="";
                try {
                    username= URLEncoder.encode(name.getText().toString(),"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String urlch=(App.isMain? Url.MAIN_HONGBAO:Url.TEST_HONGBAO)+id+"/"+redbagAddress+"?share_user="+username+"&lang="+(App.get().isZh()?"zh":"en")+"&target=draw2&symbol="+gntName+"&inwe";
                String urlen=(App.isMain? Url.MAIN_HONGBAO:Url.TEST_HONGBAO)+id+"/"+redbagAddress+"?share_user="+username+"&lang="+(App.get().isZh()?"zh":"en")+"&target=draw&symbol="+gntName+"&inwe";

                switch (type) {
                    case 0:
                        Intent intent=new Intent(mActivity,HongbaoShareActivity.class);
                        intent.putExtra("url",urlch);
                        intent.putExtra("urlen",urlen);
                        intent.putExtra("name",name.getText().toString());
                        intent.putExtra("content",etInfo.getText().toString());
                        intent.putExtra("gntName",gntName);
                        intent.putExtra("type",0);
                        finshTogo(intent);
                        dialog.dismiss();
                        break;
                    case 1:
                        Intent intent1=new Intent(mActivity,HongbaoShareActivity.class);
                        intent1.putExtra("url",urlch);
                        intent1.putExtra("urlen",urlen);
                        intent1.putExtra("name",name.getText().toString());
                        intent1.putExtra("content",etInfo.getText().toString());
                        intent1.putExtra("gntName",gntName);
                        intent1.putExtra("type",1);
                        finshTogo(intent1);
                        dialog.dismiss();
                        break;
                    case 2:
                        Intent intent2=new Intent(mActivity,HongbaoShareActivity.class);
                        intent2.putExtra("url",urlch);
                        intent2.putExtra("urlen",urlen);
                        intent2.putExtra("name",name.getText().toString());
                        intent2.putExtra("content",etInfo.getText().toString());
                        intent2.putExtra("gntName",gntName);
                        intent2.putExtra("type",2);
                        finshTogo(intent2);
                        break;
                    case 3:
                        try {
                            Intent vIt = new Intent("android.intent.action.SEND");
                            vIt.setPackage("org.telegram.messenger");
                            vIt.setType("text/plain");
                            vIt.putExtra(Intent.EXTRA_TEXT,urlen+"\n"+"You have a redpacket to pick up！");
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

    @Override
    protected void initData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_HONGBAO_REFERS));
    }
}
