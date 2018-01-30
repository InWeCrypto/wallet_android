package com.inwecrypto.wallet.ui.wallet.activity.neowallet;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
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
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.MaterialDialog;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.event.KeyEvent;
import com.inwecrypto.wallet.ui.ScanActivity;
import com.inwecrypto.wallet.ui.wallet.activity.TransferAccountsActivity;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import neomobile.Neomobile;
import neomobile.Tx;
import neomobile.Wallet;

/**
 * 作者：xiaoji06 on 2018/1/4 10:55
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class NeoTokenSaleConfirmActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.amount)
    EditText etAmount;
    @BindView(R.id.neo_amount)
    TextView neoAmount;
    @BindView(R.id.fei)
    TextView fei;
    @BindView(R.id.tip)
    TextView tip;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.gas_num)
    TextView gasNum;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_unit)
    TextView tvUnit;
    @BindView(R.id.tv_service_charge)
    TextView tvServiceCharge;
    @BindView(R.id.tv_pre_service_charge)
    TextView tvPreServiceCharge;
    @BindView(R.id.tv_transfer)
    TextView tvTransfer;
    private WalletBean wallet;
    private TokenBean.RecordBean neoBean;
    private String address;
    private String amount;
    private String gas;
    private String yuangas;
    private ArrayList<UtxoBean.ResultBean> utxoBeans = new ArrayList<>();
    private MaterialDialog mMaterialDialog;

    @Override
    protected void getBundleExtras(Bundle extras) {
        wallet = (WalletBean) extras.getSerializable("wallet");
        neoBean = (TokenBean.RecordBean) extras.getSerializable("neo");
        address = extras.getString("address");
        amount = extras.getString("amount");
        gas = extras.getString("gas");
        yuangas=extras.getString("yuangas");

    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_activity_neo_token_sale_confirm;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText("Token Sale");
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keepTogo(ScanActivity.class);
            }
        });

        tvPrice.setText(amount);
        tvServiceCharge.setText(getString(R.string.lingfushouxufei)+gas);
        tvPreServiceCharge.setText("（"+getString(R.string.yuanshouxufei)+"："+yuangas+" Gas）");
        etAddress.setText(address);

        submit.setOnClickListener(new View.OnClickListener() {
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
    }

    private void transfer(final String unspent) {
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
                            wallet = Neomobile.fromKeyStore(b, pass.getText().toString().trim());
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
                            Tx tx = wallet.mintToken(address, Double.parseDouble(gas), Double.parseDouble(amount), unspent);
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
                                mMaterialDialog.dismiss();
                                getOrderInfo(finalData, finalOrder);
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

    private void getOrderInfo(String data, String order) {
        showFixLoading();

        WalletApi.neoIcoWalletOrder(this
                , wallet.getId()
                , data
                , wallet.getAddress()
                , address
                , ""
                , new BigDecimal(amount).toPlainString()
                , "0.0000"
                , "NEO"
                , order
                , Constant.NEO_ASSETS
                , new JsonCallback<LzyResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<Object>> response) {
                        hideFixLoading();
                        ToastUtil.show(R.string.zhuanzhangchenggong);
                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
                        AppManager.getAppManager().finishActivity(NeoTokenSaleActivity.class);
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
    protected void initData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_KEY) {
            KeyEvent key = (KeyEvent) event.getData();
            if (AppUtil.isAddress(key.getKey().toLowerCase())) {
                etAddress.setText(key.getKey().toLowerCase());
            } else {
                ToastUtil.show(R.string.qingshuruzhengquedeqianbaodizhi);
            }
        }
    }

}
