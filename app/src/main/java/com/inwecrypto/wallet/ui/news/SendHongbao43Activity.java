package com.inwecrypto.wallet.ui.news;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

import net.qiujuer.genius.ui.widget.Button;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/4/23 12:29
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class SendHongbao43Activity extends BaseActivity {


    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.et_info)
    EditText etInfo;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.frame)
    TextView frame;
    @BindView(R.id.copy)
    TextView copy;

    private String id;
    private String redbagAddress;
    private boolean isShare;
    private String gntName;

    @Override
    protected void getBundleExtras(Bundle extras) {
        id = extras.getString("id");
        redbagAddress = extras.getString("redbagAddress");
        isShare= extras.getBoolean("isShare");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.send_hongbao_43_activity;
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
                if (name.getText().toString().length() == 0) {
                    ToastUtil.show(R.string.qingtianxiefasongrenmincheng);
                    return;
                }

                if (etInfo.getText().toString().length() == 0) {
                    ToastUtil.show(R.string.qingtianxieliuyan);
                    return;
                }

                if (isShare){
                    String username="";
                    try {
                        username= URLEncoder.encode(name.getText().toString(),"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    frame.setText("<iframe height=498 width=510 src='"+
                            (App.isMain? Url.MAIN_HONGBAO:Url.TEST_HONGBAO)+id+"/"+redbagAddress+"?share_user="+username+"&lang="+(App.get().isZh()?"zh":"en")+"&target=draw&symbol="+gntName+"&inwe"
                            +"'/>");

                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setPrimaryClip(ClipData.newPlainText(null, frame.getText().toString()));
                    ToastUtil.show(getString(R.string.fuzhichenggong));
                }else {
                    showFixLoading();
                    ZixunApi.sendRedbag(this
                            , id
                            , redbagAddress
                            , "4"
                            , ""
                            , name.getText().toString()
                            , etInfo.getText().toString()
                            , new JsonCallback<LzyResponse<HongbaoFeeBean>>() {
                                @Override
                                public void onSuccess(Response<LzyResponse<HongbaoFeeBean>> response) {

                                    isShare=true;
                                    EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_HONGBAO_SHARE,id));

                                    String username="";
                                    try {
                                        username= URLEncoder.encode(name.getText().toString(),"utf-8");
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    frame.setText("<iframe height=498 width=510 src='"+
                                            (App.isMain? Url.MAIN_HONGBAO:Url.TEST_HONGBAO)+id+"/"+redbagAddress+"?share_user="+username+"&lang="+(App.get().isZh()?"zh":"en")+"&target=draw&symbol="+gntName+"&inwe"
                                            +"'/>");

                                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    // 将文本内容放到系统剪贴板里。
                                    cm.setPrimaryClip(ClipData.newPlainText(null, frame.getText().toString()));
                                    ToastUtil.show(getString(R.string.fuzhichenggong));
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

            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setPrimaryClip(ClipData.newPlainText(null, frame.getText().toString()));
                ToastUtil.show(getString(R.string.fuzhichenggong));
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
