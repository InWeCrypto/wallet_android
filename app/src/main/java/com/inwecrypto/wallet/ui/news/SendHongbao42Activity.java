package com.inwecrypto.wallet.ui.news;

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
import com.lzy.okgo.model.Response;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import net.qiujuer.genius.ui.widget.Button;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/4/23 12:29
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class SendHongbao42Activity extends BaseActivity {


    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.et_info)
    EditText etInfo;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.url)
    EditText url;

    private String id;
    private String redbagAddress;
    private boolean isShare;
    private String gntName;

    @Override
    protected void getBundleExtras(Bundle extras) {
        id = extras.getString("id");
        redbagAddress = extras.getString("redbagAddress");
        isShare = extras.getBoolean("isShare");
        gntName=extras.getString("gntName");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.send_hongbao_42_activity;
    }

    @Override
    protected void initView() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().length() == 0) {
                    ToastUtil.show(getString(R.string.qingtianxiefasongrenmincheng));
                    return;
                }

                if (etInfo.getText().toString().length() == 0) {
                    ToastUtil.show(getString(R.string.qingtianxieliuyan));
                    return;
                }

                if (url.getText().toString().length() == 0){
                    ToastUtil.show(getString(R.string.qingtianxieurl));
                    return;
                }

                if (isShare){
                    Intent intent=new Intent(mActivity,HongbaoShareWebActivity.class);
                    String username="";
                    try {
                        username= URLEncoder.encode(name.getText().toString(),"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String urlch=(App.isMain? Url.MAIN_HONGBAO:Url.TEST_HONGBAO)+id+"/"+redbagAddress+"?share_user="+username+"&lang="+(App.get().isZh()?"zh":"en")+"&target=draw2&symbol="+gntName+"&inwe";
                    String urlen=(App.isMain? Url.MAIN_HONGBAO:Url.TEST_HONGBAO)+id+"/"+redbagAddress+"?share_user="+username+"&lang="+(App.get().isZh()?"zh":"en")+"&target=draw&symbol="+gntName+"&inwe";

                    intent.putExtra("url",urlch);
                    intent.putExtra("urlen",urlen);
                    intent.putExtra("name",name.getText().toString());
                    intent.putExtra("gntName",gntName);
                    intent.putExtra("content",etInfo.getText().toString());
                    finshTogo(intent);
                }else {
                    showFixLoading();
                    ZixunApi.sendRedbag(this
                            , id
                            , redbagAddress
                            , "3"
                            , url.getText().toString()
                            , name.getText().toString()
                            , etInfo.getText().toString()
                            , new JsonCallback<LzyResponse<HongbaoFeeBean>>() {
                                @Override
                                public void onSuccess(Response<LzyResponse<HongbaoFeeBean>> response) {
                                    isShare=true;
                                    EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_HONGBAO_SHARE,id));

                                    Intent intent=new Intent(mActivity,HongbaoShareWebActivity.class);
                                    String username="";
                                    try {
                                        username= URLEncoder.encode(name.getText().toString(),"utf-8");
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    String urlch=(App.isMain? Url.MAIN_HONGBAO:Url.TEST_HONGBAO)+id+"/"+redbagAddress+"?share_user="+username+"&lang="+(App.get().isZh()?"zh":"en")+"&target=draw2&symbol="+gntName+"&inwe";
                                    String urlen=(App.isMain? Url.MAIN_HONGBAO:Url.TEST_HONGBAO)+id+"/"+redbagAddress+"?share_user="+username+"&lang="+(App.get().isZh()?"zh":"en")+"&target=draw&symbol="+gntName+"&inwe";

                                    intent.putExtra("url",urlch);
                                    intent.putExtra("urlen",urlen);
                                    intent.putExtra("gntName",gntName);
                                    intent.putExtra("name",name.getText().toString());
                                    intent.putExtra("content",etInfo.getText().toString());
                                    finshTogo(intent);
                                }

                                @Override
                                public void onError(Response<LzyResponse<HongbaoFeeBean>> response) {
                                    super.onError(response);
                                    ToastUtil.show(getString(R.string.fenxiangshibaiqingchongshi));
                                }

                                @Override
                                public void onFinish() {
                                    super.onFinish();
                                    hideFixLoading();
                                }
                            });
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

}
