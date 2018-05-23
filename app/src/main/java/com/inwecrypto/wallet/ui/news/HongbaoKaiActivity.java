package com.inwecrypto.wallet.ui.news;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/5/2 10:41
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class HongbaoKaiActivity extends BaseActivity {
    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.bg)
    ImageView bg;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.bgfl)
    FrameLayout bgfl;
    @BindView(R.id.tec)
    ImageView tec;

    private ArrayList<WalletBean> wallet = new ArrayList<>();

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.hongbaokai_activity_layout;
    }

    @Override
    protected void initView() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bgfl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFixLoading();
                WalletApi.wallet(mActivity, new JsonCallback<LzyResponse<CommonListBean<WalletBean>>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                        wallet.clear();
                        if (null != response.body().data.getList()) {
                            String wallets = App.get().getSp().getString(Constant.WALLETS, "").toLowerCase();
                            String wallets_beifen = App.get().getSp().getString(Constant.WALLETS_BEIFEN, "").toLowerCase();
                            String walletsZjc = App.get().getSp().getString(Constant.WALLETS_ZJC_BEIFEN, "").toLowerCase();
                            for (int i = 0; i < response.body().data.getList().size(); i++) {
                                if (wallets.contains(response.body().data.getList().get(i).getAddress().toLowerCase())) {
                                    if (wallets_beifen.contains(response.body().data.getList().get(i).getAddress().toLowerCase()) || walletsZjc.contains(response.body().data.getList().get(i).getAddress().toLowerCase())) {
                                        response.body().data.getList().get(i).setType(Constant.BEIFEN);
                                    } else {
                                        response.body().data.getList().get(i).setType(Constant.ZHENGCHANG);
                                    }
                                } else {
                                    response.body().data.getList().get(i).setType(Constant.GUANCHA);
                                }
                                if (response.body().data.getList().get(i).getCategory_id()==1){
                                    wallet.add(response.body().data.getList().get(i));
                                }
                            }
                        }

                        if (wallet.size()>0){

                        }else {
                            ToastUtil.show(R.string.ninhaimeiyouethqianbao);
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                        super.onError(response);
                        ToastUtil.show(getString(R.string.jiazaishibai));
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        hideFixLoading();
                    }
                });
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
