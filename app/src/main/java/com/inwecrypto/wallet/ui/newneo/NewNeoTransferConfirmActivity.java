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
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.MaterialDialog;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.wallet.activity.neowallet.NeoTransferAccountsActivity;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;

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

public class NewNeoTransferConfirmActivity extends BaseActivity {


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

    private String unspent;
    private WalletBean wallet;
    private int type;
    private String price;
    private String hit;
    private String unit;
    private String to;
    private String handfee;

    @Override
    protected void getBundleExtras(Bundle extras) {
        unspent = extras.getString("unspent");
        wallet = (WalletBean) extras.getSerializable("wallet");
        type = extras.getInt("type");
        price = extras.getString("price");
        hit = extras.getString("hit");
        unit = extras.getString("unit");
        to = extras.getString("to");
        handfee = extras.getString("handfee");
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
                transfer(unspent);
            }
        });

        tvPrice.setText(price);
        tvUnit.setText(unit);
        tvServiceCharge.setText(handfee);
        etAddress.setText(to);
        etHit.setText(hit);
        tvPreServiceCharge.setVisibility(View.GONE);
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
                            if (accounts[i].name.equals(wallet.getAddress())) {
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
                            Tx tx = wallet.createAssertTx(type == 0 ? Constant.NEO_ASSETS : Constant.GAS_ASSETS, wallet.address(), to, new BigDecimal(price).doubleValue(), unspent);
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
                , to
                , hit
                , new BigDecimal(price).toPlainString()
                , "0.0000"
                , "NEO"
                , order
                , type == 0 ? Constant.NEO_ASSETS : Constant.GAS_ASSETS
                , new JsonCallback<LzyResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<Object>> response) {
                        hideFixLoading();
                        ToastUtil.show(R.string.zhuanzhangchenggong);
                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
                        AppManager.getAppManager().finishActivity(NeoTransferAccountsActivity.class);
                        finish();
                    }

                    @Override
                    public void onError(Response<LzyResponse<Object>> response) {
                        super.onError(response);
                        hideFixLoading();
                        if (response.getException().getMessage().contains("wallet_error")) {
                            ToastUtil.show(getString(R.string.inner_error) + response.getException().getMessage());
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
