package com.inwecrypto.wallet.ui.wallet.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
import me.drakeet.materialdialog.MaterialDialog;
import me.grantland.widget.AutofitTextView;
import unichain.ETHWallet;
import unichain.Unichain;

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
    private MaterialDialog mMaterialDialog;

    private String nonce;
    private String oxPrice;
    private String oxGas;
    private BigDecimal pEther = new java.math.BigDecimal("1000000000000000000");
    private String hash;

    @Override
    protected void getBundleExtras(Bundle extras) {
        address = extras.getString("address");
        price = extras.getString("price");
        oxPrice = "0x" + new BigInteger(new BigDecimal(price).multiply(pEther).setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString(),10).toString(16);
        gas = extras.getString("gas");
        oxGas = "0x" + new BigInteger(new BigDecimal(gas).multiply(pEther).divide(new BigDecimal(Constant.GAS_LIMIT), 0,BigDecimal.ROUND_HALF_UP).toPlainString(),10).toString(16);
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
        txtMainTitle.setText(R.string.transfer_confirm);
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
        tvPrice.setText(new BigDecimal(price).setScale(4,BigDecimal.ROUND_HALF_UP).toPlainString());
        tvServiceCharge.setText(getString(R.string.transfer_hit1) + gas);
        etAddress.setText(address);
        etHit.setText(hit);
    }

    private void transfer() {
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
                                    ToastUtil.show(getString(R.string.wallet_hit27));
                                    hideFixLoading();
                                }
                            });
                            return;
                        }
                        String data = "";
                        try {
                            data = "0x" + conver16HexStr(wallet.transferCurrency(nonce, oxGas,"0x" + new BigInteger(new BigDecimal(Constant.GAS_LIMIT).setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString(),10).toString(16), address, oxPrice));
                        } catch (Exception e) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show("转账失败！请稍后重试");
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
        WalletApi.walletOrder(mActivity, wallet.getId(), data, wallet.getAddress(), address, hit, new BigDecimal(price).multiply(pEther).setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString(), new BigDecimal(gas).multiply(pEther).setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString(), wallet.getCategory().getName(), new JsonCallback<LzyResponse<Object>>() {
            @Override
            public void onSuccess(Response<LzyResponse<Object>> response) {
                hideFixLoading();
                ToastUtil.show("转账成功");
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
                AppManager.getAppManager().finishActivity(TransferAccountsActivity.class);
                finish();
            }

            @Override
            public void onError(Response<LzyResponse<Object>> response) {
                super.onError(response);
                hideFixLoading();
                if (response.getException().getMessage().contains("wallet_error")){
                    ToastUtil.show("服务器内部错误");
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

    /**
     * byte数组转换为十六进制的字符串
     **/
    public static String conver16HexStr(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            if ((b[i] & 0xff) < 0x10)
                result.append("0");
            result.append(Long.toString(b[i] & 0xff, 16));
        }
        return result.toString().toUpperCase();
    }
}
