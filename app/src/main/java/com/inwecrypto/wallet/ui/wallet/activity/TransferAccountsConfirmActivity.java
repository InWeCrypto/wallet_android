package com.inwecrypto.wallet.ui.wallet.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.inwecrypto.wallet.bean.HongbaoOrderBean;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.ui.newneo.InputPassFragment;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CountBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.common.widget.MaterialDialog;

import ethmobile.Ethmobile;
import ethmobile.Wallet;
import me.grantland.widget.AutofitTextView;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class TransferAccountsConfirmActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
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

    private WalletBean wallet;
    private String address;
    private String price;
    private String gas;
    private String hit;

    private String nonce;
    private String oxPrice;
    private String oxGas;

    @Override
    protected void getBundleExtras(Bundle extras) {
        address = extras.getString("address");
        price = extras.getString("price");
        oxPrice = "0x" + new BigInteger(new BigDecimal(price).multiply(Constant.pEther).setScale(0,BigDecimal.ROUND_DOWN).toPlainString(),10).toString(16);
        gas = extras.getString("gas");
        oxGas = "0x" + new BigInteger(new BigDecimal(gas).multiply(Constant.pEther).divide(new BigDecimal(Constant.GAS_LIMIT), 0,BigDecimal.ROUND_DOWN).toPlainString(),10).toString(16);
        hit = extras.getString("hit");
        wallet = (WalletBean) extras.getSerializable("wallet");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_activity_transfer_accounts_confirm;
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
                WalletApi.transactionCount(mActivity, wallet.getAddress(), new JsonCallback<LzyResponse<CountBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<CountBean>> response) {
                        hideFixLoading();
                        nonce = response.body().data.getCount();
                        transfer();
                    }

                    @Override
                    public void onError(Response<LzyResponse<CountBean>> response) {
                        super.onError(response);
                        ToastUtil.show(getString(R.string.load_error));
                        hideFixLoading();
                    }
                });
            }
        });
    }

    @Override
    protected void initData() {
        tvPrice.setText(new BigDecimal(price).setScale(4,BigDecimal.ROUND_DOWN).toPlainString());
        tvServiceCharge.setText(getString(R.string.lingfushouxufei) + gas);
        etAddress.setText(address);
        etHit.setText(hit);
    }

    private void transfer() {
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
                            wallet = Ethmobile.fromKeyStore(b, passWord);
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
                        try {
                            //AppUtil.conver16HexStr
                            data = "0x" + wallet.transfer(nonce
                                    , address
                                    , oxPrice
                                    , oxGas
                                    ,"0x" + new BigInteger(new BigDecimal(Constant.GAS_LIMIT).setScale(0,BigDecimal.ROUND_DOWN).toPlainString(),10).toString(16));
                        } catch (Exception e) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show( R.string.zhuanzhangshibaiqingchongshi);
                                    hideFixLoading();
                                }
                            });
                            return;
                        }
                        final String finalData = data;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideFixLoading();
                                getOrderInfo(finalData);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private void getOrderInfo(String data) {
        showFixLoading();
        WalletApi.walletOrder(mActivity
                , wallet.getId()
                , data
                , wallet.getAddress()
                , address
                , hit
                , oxPrice
                , oxGas
                , wallet.getCategory().getName()
                , Constant.ETH_ORDER_ASSET_ID
                , new JsonCallback<LzyResponse<HongbaoOrderBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<HongbaoOrderBean>> response) {
                hideFixLoading();
                ToastUtil.show(R.string.zhuanzhangchenggong);
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
                AppManager.getAppManager().finishActivity(TransferAccountsActivity.class);
                finish();
            }

            @Override
            public void onError(Response<LzyResponse<HongbaoOrderBean>> response) {
                super.onError(response);
                hideFixLoading();
                if (response.getException().getMessage().contains("wallet_error")){
                    ToastUtil.show(R.string.inner_error);
                    EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
                    AppManager.getAppManager().finishActivity(TransferAccountsActivity.class);
                    finish();
                }else {
                    ToastUtil.show(getString(R.string.load_error));
                }
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
