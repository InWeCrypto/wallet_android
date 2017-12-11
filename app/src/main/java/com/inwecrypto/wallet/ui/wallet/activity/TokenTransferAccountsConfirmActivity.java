package com.inwecrypto.wallet.ui.wallet.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.inwecrypto.wallet.common.util.AppUtil;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CountBean;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.bean.TransferABIBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.common.widget.MaterialDialog;
import unichain.ETHWallet;
import unichain.Unichain;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class TokenTransferAccountsConfirmActivity extends BaseActivity {

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
    TextView etAddress;
    @BindView(R.id.et_hit)
    TextView etHit;
    @BindView(R.id.tv_transfer)
    TextView tvTransfer;

    private WalletBean wallet;
    private TokenBean.ListBean gnt;
    private String address;
    private String price;
    private String gas;
    private String hit;
    private MaterialDialog mMaterialDialog;

    private String nonce;
    private String oxPrice;
    private String oxGas;

    @Override
    protected void getBundleExtras(Bundle extras) {
        address = extras.getString("address");
        gnt = (TokenBean.ListBean) extras.getSerializable("gnt");
        price = extras.getString("price");
        oxPrice = "0x" + new BigInteger(new BigDecimal(price).multiply(Constant.pEther).setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString(),10).toString(16);
        gas = extras.getString("gas");
        oxGas = "0x" + new BigInteger(new BigDecimal(gas).multiply(Constant.pEther).divide(new BigDecimal(gnt.getGnt_category().getGas()), 0,BigDecimal.ROUND_HALF_UP).toPlainString(),10).toString(16);
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
                        WalletApi.transferABI(mActivity, gnt.getGnt_category().getAddress(), etAddress.getText().toString(), oxPrice, new JsonCallback<LzyResponse<TransferABIBean>>() {
                            @Override
                            public void onSuccess(Response<LzyResponse<TransferABIBean>> response) {
                                hideFixLoading();
                                transfer(response.body().data.getData());
                            }

                            @Override
                            public void onError(Response<LzyResponse<TransferABIBean>> response) {
                                super.onError(response);
                                ToastUtil.show(R.string.zhuanzhangshibaiqingchongshi);
                                hideFixLoading();
                            }
                        });
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
        tvPrice.setText(new BigDecimal(price).setScale(4,BigDecimal.ROUND_HALF_UP).toPlainString());
        tvServiceCharge.setText(getString(R.string.lingfushouxufei) + gas);
        etAddress.setText(address);
        etHit.setText(hit);
    }

    private void transfer(final String tokenData) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_dialog_pass, null, false);
        final EditText pass = (EditText) view.findViewById(R.id.et_pass);
        TextView cancle = (TextView) view.findViewById(R.id.cancle);
        TextView ok = (TextView) view.findViewById(R.id.ok);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass.getText().toString().length() == 0) {
                    ToastUtil.show(getString(R.string.qingshurumima));
                    return;
                }
                showFixLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        byte[] b = new byte[0];
                        final AccountManager accountManager = AccountManager.get(mActivity);
                        Account[] accounts = accountManager.getAccountsByType("com.inwecrypto.wallet");
                        for (int i = 0; i < accounts.length; i++) {
                            if (accounts[i].name.equals(wallet.getAddress())) {
                                //accountManager.getUserData(accounts[i], pass.getText().toString());
                                b = accountManager.getUserData(accounts[i], "wallet").getBytes();
                                break;
                            }
                        }
                        ETHWallet wallet = null;
                        try {
                            wallet = Unichain.openETHWallet(b, pass.getText().toString());
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
                            data = "0x" + AppUtil.conver16HexStr(wallet.transferToken(nonce, oxGas,"0x" + new BigInteger(new BigDecimal(gnt.getGnt_category().getGas()).setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString(),10).toString(16), gnt.getGnt_category().getAddress(), tokenData.getBytes("utf-8")));
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideFixLoading();
                                mMaterialDialog.dismiss();
                                getOrderInfo(finalData);
                            }
                        });
                    }
                }).start();

            }
        });

        mMaterialDialog = new MaterialDialog(mActivity).setView(view);
        mMaterialDialog.setBackgroundResource(R.drawable.trans_bg);
        mMaterialDialog.setCanceledOnTouchOutside(true);
        mMaterialDialog.show();
    }

    private void getOrderInfo(String data) {
        showFixLoading();
        WalletApi.walletOrder(mActivity, wallet.getId(), data, wallet.getAddress(), address, hit, new BigDecimal(price).multiply(Constant.pEther).setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString(), new BigDecimal(gas).multiply(Constant.pEther).setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString(), gnt.getName(), new JsonCallback<LzyResponse<Object>>() {
            @Override
            public void onSuccess(Response<LzyResponse<Object>> response) {
                hideFixLoading();
                ToastUtil.show(R.string.zhuanzhangchenggong);
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
                AppManager.getAppManager().finishActivity(TokenTransferAccountsActivity.class);
                finish();
            }

            @Override
            public void onError(Response<LzyResponse<Object>> response) {
                super.onError(response);
                hideFixLoading();
                if (response.getException().getMessage().contains("wallet_error")){
                    ToastUtil.show(getString(R.string.fuwuqineibucuowu));
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
