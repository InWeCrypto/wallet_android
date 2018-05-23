package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.DrawRecordBean;
import com.inwecrypto.wallet.bean.ErrorBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.fragment.HongbaoSelectWalletListFragment;
import com.lzy.okgo.model.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/4/24 17:33
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ScanHongbaoActivity extends BaseActivity {
    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.kai)
    View kai;
    @BindView(R.id.from)
    TextView from;
    @BindView(R.id.tec)
    ImageView tec;

    private String url;
    private String id;
    private String address;
    private String name = "";

    private ArrayList<WalletBean> wallet = new ArrayList<>();

    @Override
    protected void getBundleExtras(Bundle extras) {
        url = extras.getString("url");
        String[] urls = url.split("/");
        id = urls[urls.length - 2];
        String part1 = urls[urls.length - 1];
        String[] part1s = part1.split("\\?");
        address = part1s[0];
        String[] params = part1s[1].split("&");
        String nameS = params[0].replace("share_user=", "");
        try {
            name = URLDecoder.decode(nameS, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int setLayoutID() {
        return R.layout.scan_hongbao_activity;
    }

    @Override
    protected void initView() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (App.get().isZh()) {
            from.setText(name + "给您发了一个红包");
        } else {
            from.setText(name + "Send you a Red Packet");
            tec.setImageResource(R.mipmap.hongbao_tec_en);
        }

        kai.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                kai.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) kai.getLayoutParams();
                params.width = kai.getMeasuredHeight();
                kai.setLayoutParams(params);
            }
        });

        kai.setOnClickListener(new View.OnClickListener() {
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
                                if (response.body().data.getList().get(i).getCategory_id() == 1) {
                                    wallet.add(response.body().data.getList().get(i));
                                }
                            }
                        }

                        if (wallet.size() == 0) {
                            ToastUtil.show(getString(R.string.nihaimeiyouethqianbao));
                        } else {
                            wallet.get(0).setSelect(true);
                            //弹出钱包选择框
                            FragmentManager fm = mActivity.getSupportFragmentManager();
                            HongbaoSelectWalletListFragment walletlist = new HongbaoSelectWalletListFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("wallet", wallet);
                            walletlist.setArguments(bundle);
                            walletlist.show(fm, "list");
                            walletlist.setLister(new HongbaoSelectWalletListFragment.NextLister() {
                                @Override
                                public void onNext(int position) {
                                    getHongbao(position);
                                }
                            });
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

    private void getHongbao(int position) {
        showFixLoading();
        ZixunApi.getRedbag(this
                , id
                , address.toLowerCase()
                , wallet.get(position).getAddress().toLowerCase()
                , new JsonCallback<LzyResponse<DrawRecordBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<DrawRecordBean>> response) {
                        Intent intent = new Intent(mActivity, HongbaoGetRecordDetailActivity.class);
                        intent.putExtra("redbag", response.body().data);
                        finshTogo(intent);
                    }

                    @Override
                    public void onError(Response<LzyResponse<DrawRecordBean>> response) {
                        super.onError(response);
                        if (response.getException().getMessage().contains("4006")) {
                            String[] errors = response.getException().getMessage().split("_");
                            try {
                                ErrorBean errorBean = GsonUtils.jsonToObj(errors[2], ErrorBean.class);
                                ToastUtil.show(errorBean.getMsg());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtil.show(getString(R.string.lingqushibai));
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        hideFixLoading();
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
