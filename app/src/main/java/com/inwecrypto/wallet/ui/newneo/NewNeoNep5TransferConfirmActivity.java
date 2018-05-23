package com.inwecrypto.wallet.ui.newneo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.bean.UtxoBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.MaterialDialog;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.wallet.activity.TransferAccountsActivity;
import com.inwecrypto.wallet.ui.wallet.activity.neowallet.NeoNep5TransferAccountsActivity;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.grantland.widget.AutofitTextView;
import neomobile.Neomobile;
import neomobile.Tx;
import neomobile.Wallet;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class NewNeoNep5TransferConfirmActivity extends BaseActivity {


    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_service_charge)
    TextView tvServiceCharge;
    @BindView(R.id.et_address)
    AutofitTextView etAddress;
    @BindView(R.id.et_hit)
    TextView etHit;
    @BindView(R.id.tv_transfer)
    TextView tvTransfer;
    @BindView(R.id.tv_unit)
    TextView tvUnit;
    @BindView(R.id.tv_pre_service_charge)
    TextView tvPreServiceCharge;

    private WalletBean wallet;
    private TokenBean.ListBean tokenBean;
    private String price;
    private String hit;
    private String unit;
    private String to;
    private String handfee;
    private ArrayList<UtxoBean.ResultBean> utxoBeans = new ArrayList<>();
    private String yuangas;
    private long amount;

    @Override
    protected void getBundleExtras(Bundle extras) {
        wallet = (WalletBean) extras.getSerializable("wallet");
        tokenBean = (TokenBean.ListBean) extras.getSerializable("token");
        price = extras.getString("price");
        hit = extras.getString("hit");
        unit = extras.getString("unit");
        to = extras.getString("to");
        handfee = extras.getString("handfee");
        yuangas=extras.getString("yuangas");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.newneo_wallet_activity_transfer_confirm;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.zhuanzhangqueren);
        txtRightTitle.setVisibility(View.GONE);
        tvTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFixLoading();
                utxoBeans.clear();
                WalletApi.getUtxo(this, wallet.getAddress(), "neo-gas-asset-id", new JsonCallback<LzyResponse<UtxoBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<UtxoBean>> response) {
                        if (null != response) {
                            if (null != response.body().data.getResult()) {
                                utxoBeans.addAll(response.body().data.getResult());
                                WalletApi.getUtxo(this, wallet.getAddress(), "neo-asset-id", new JsonCallback<LzyResponse<UtxoBean>>() {
                                    @Override
                                    public void onSuccess(Response<LzyResponse<UtxoBean>> response) {
                                        if (null != response) {
                                            if (null != response.body().data.getResult()) {
                                                utxoBeans.addAll(response.body().data.getResult());
                                                String utxo = GsonUtils.objToJson(utxoBeans);
                                                transfer(utxo);
                                            } else {
                                                ToastUtil.show(getString(R.string.huoquyueshibaiqingshaohouchognshi));
                                            }
                                        }
                                        hideFixLoading();
                                    }

                                    @Override
                                    public void onError(Response<LzyResponse<UtxoBean>> response) {
                                        super.onError(response);
                                        hideFixLoading();
                                        ToastUtil.show(getString(R.string.huoquyueshibaiqingshaohouchognshi));
                                    }
                                });
                            } else {
                                ToastUtil.show(getString(R.string.huoquyueshibaiqingshaohouchognshi));
                            }
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<UtxoBean>> response) {
                        super.onError(response);
                        hideFixLoading();
                        ToastUtil.show(getString(R.string.huoquyueshibaiqingshaohouchognshi));
                    }
                });
            }
        });

        tvPrice.setText(price);
        tvUnit.setText(unit);
        tvServiceCharge.setText(getString(R.string.shouxufei)+"："+handfee+" Gas");
        tvPreServiceCharge.setText("（"+getString(R.string.yuanshouxufei)+"："+yuangas+" Gas）");
        etAddress.setText(to);
        etHit.setText(hit);
    }

    @Override
    protected void initData() {

    }

    private void transfer(final String unspent) {
        //输入密码
        FragmentManager fm = getSupportFragmentManager();
        InputPassFragment input = new InputPassFragment();
        input.show(fm, "input");
        input.setOnNextListener(new InputPassFragment.OnNextInterface() {
            @Override
            public void onNext(final String passWord, final Dialog dialog) {
                if (passWord.length() == 0) {
                    ToastUtil.show(getString(R.string.qingshurumima));
                    return;
                }
                showFixLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String b = "";
                        final AccountManager accountManager = AccountManager.get(mActivity);
                        Account[] accounts = accountManager.getAccountsByType("com.inwecrypto.wallet");
                        for (int i = 0; i < accounts.length; i++) {
                            if (accounts[i].name.toLowerCase().equals(wallet.getAddress().toLowerCase())) {
                                //accountManager.getUserData(accounts[i], pass.getText().toString());
                                b = accountManager.getUserData(accounts[i], "wallet");
                                break;
                            }
                        }
                        Wallet wallet = null;
                        try {
                            wallet = Neomobile.fromKeyStore(b, passWord);
                        } catch (Exception e) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(getString(R.string.mimacuowuqingchongshi));
                                    hideFixLoading();
                                }
                            });
                            return;
                        }
                        String data = "";
                        String order = "";
                        try {
                            amount = new BigDecimal(price).multiply(new BigDecimal(10).pow(Integer.parseInt(tokenBean.getDecimals()))).longValue();
                            Tx tx = wallet.createNep5Tx(tokenBean.getGnt_category().getAddress()
                                    , Neomobile.decodeAddress(wallet.address())
                                    , Neomobile.decodeAddress(etAddress.getText().toString().trim())
                                    , amount
                                    , unspent);
                            data = tx.getData();
                            order = "0x" + tx.getID();
                        } catch (Exception e) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(R.string.zhuanzhangshibaiqingchongshi);
                                    hideFixLoading();
                                }
                            });
                            return;
                        }
                        final String finalData = data;
                        final String finalOrder = order;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideFixLoading();
                                dialog.dismiss();
                                getOrderInfo(finalData, finalOrder);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private void getOrderInfo(String data, String order) {
        showFixLoading();

        WalletApi.neoWalletOrder(this
                , wallet.getId()
                , data
                , wallet.getAddress()
                , etAddress.getText().toString()
                , hit
                , amount+""
                , "0.0000"
                , "NEO"
                , order
                , tokenBean.getGnt_category().getAddress()
                , new JsonCallback<LzyResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<Object>> response) {
                        hideFixLoading();
                        ToastUtil.show(R.string.zhuanzhangchenggong);
                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
                        AppManager.getAppManager().finishActivity(NeoNep5TransferAccountsActivity.class);
                        finish();
                    }

                    @Override
                    public void onError(Response<LzyResponse<Object>> response) {
                        super.onError(response);
                        hideFixLoading();
                        if (response.getException().getMessage().contains("wallet_error")) {
                            if (response.getException().getMessage().contains("4006")){
                                ToastUtil.show(R.string.ninhaiyouweiwanchengdedingdan);
                            }else {
                                ToastUtil.show(getString(R.string.inner_error) + response.getException().getMessage());
                            }
                        } else {
                            ToastUtil.show(getString(R.string.load_error));
                        }
                    }
                });

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
