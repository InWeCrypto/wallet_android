package com.inwecrypto.wallet.ui.wallet.activity.neowallet;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.BpsBean;
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
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.event.KeyEvent;
import com.inwecrypto.wallet.ui.ScanActivity;
import com.inwecrypto.wallet.ui.me.activity.MailListActivity;
import com.inwecrypto.wallet.ui.wallet.activity.TransferAccountsActivity;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;

import butterknife.BindView;
import neomobile.Neomobile;
import neomobile.Tx;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class NeoTransferAccountsActivity extends BaseActivity {


    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.iv_mail)
    ImageView ivMail;
    @BindView(R.id.tv_currentPrice)
    TextView tvCurrentPrice;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.et_hint)
    EditText etHint;
    @BindView(R.id.tv_ok)
    TextView tvOk;

    private WalletBean wallet;
    private boolean isClod;
    private int type;
    private TokenBean.RecordBean neoBean;

    private MaterialDialog mMaterialDialog;

    @Override
    protected void getBundleExtras(Bundle extras) {
        isClod = extras.getBoolean("isClod", false);
        wallet = (WalletBean) extras.getSerializable("wallet");
        type = extras.getInt("type");
        neoBean= (TokenBean.RecordBean) extras.getSerializable("neo");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_activity_neo_transfer_accounts;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.zhuanzhang);
        Drawable drawableInfo = getResources().getDrawable(R.mipmap.nav_scan);
        /// 这一步必须要做,否则不会显示.
        drawableInfo.setBounds(0, 0, drawableInfo.getMinimumWidth(), drawableInfo.getMinimumHeight());
        txtRightTitle.setCompoundDrawables(drawableInfo, null, null, null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keepTogo(ScanActivity.class);
            }
        });

        tvCurrentPrice.setText("("+getString(R.string.dangqianyue)+"："+(type==0?new BigDecimal(neoBean.getBalance()).setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString():new BigDecimal(neoBean.getGnt().get(0).getBalance()).setScale(8,BigDecimal.ROUND_HALF_UP).toPlainString())+")");

        ivMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MailListActivity.class);
                intent.putExtra("address", true);
                intent.putExtra("select",2);
                keepTogo(intent);
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type==0&&new BigDecimal(neoBean.getBalance()).doubleValue()==0){
                    ToastUtil.show(R.string.yuebuzu);
                    return;
                }

                if (type!=0&&new BigDecimal(neoBean.getGnt().get(0).getBalance()).doubleValue()==0){
                    ToastUtil.show(R.string.yuebuzu);
                    return;
                }

                if (!AppUtil.isAddress(etAddress.getText().toString().trim())) {
                    ToastUtil.show(R.string.qingtianxiezhuanzhangqianbaodizhi);
                    return;
                }

                if (etPrice.getText().toString().trim().length() == 0) {
                    ToastUtil.show(R.string.qingtianxiezhuanzhangjine);
                    return;
                }
                try {
                    new BigDecimal(etPrice.getText().toString().trim());
                } catch (Exception e) {
                    ToastUtil.show(R.string.qingtianxiezhengquedejine);
                    return;
                }

                if (type==0&&new BigDecimal(etPrice.getText().toString().trim()).toPlainString().contains(".")){
                    ToastUtil.show(getString(R.string.zuixiaodanweishi1));
                    return;
                }

                if (type==0&&(new BigDecimal(neoBean.getBalance()).doubleValue()-new BigDecimal(etPrice.getText().toString().trim()).floatValue())<0){
                    ToastUtil.show(getString(R.string.zhuanzhangtishi));
                    return;
                }

                if (type!=0&&(new BigDecimal(neoBean.getGnt().get(0).getBalance()).doubleValue()-new BigDecimal(etPrice.getText().toString().trim()).floatValue())<0){
                    ToastUtil.show(getString(R.string.zhuanzhangtishi));
                    return;
                }

                showFixLoading();

                WalletApi.getUtxo(this, wallet.getAddress(), type==0?"neo-asset-id":"neo-gas-asset-id", new JsonCallback<LzyResponse<UtxoBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<UtxoBean>> response) {
                        if (null!=response){
                            if (isClod){
                                Intent intent = new Intent(mActivity,WatchNeoTransferAccountsNfcActivity.class);
                                intent.putExtra("wallet",wallet);
                                intent.putExtra("address",etAddress.getText().toString().trim());
                                intent.putExtra("price",new BigDecimal(etPrice.getText().toString().trim()).doubleValue());
                                intent.putExtra("type",type);
                                intent.putExtra("unspent",response.body());
                                keepTogo(intent);
                            }else {
                                String utxo=GsonUtils.objToJson(response.body().data.getResult());
                                transfer(utxo);
                            }
                        }
                        hideFixLoading();
                    }

                    @Override
                    public void onError(Response<LzyResponse<UtxoBean>> response) {
                        super.onError(response);
                        hideFixLoading();
                    }
                });
            }
        });
    }

    @Override
    protected void initData() {

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
                        neomobile.Wallet wallet = null;
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
                        String order="";
                        try {
                            Tx tx=wallet.createAssertTx(type==0?Constant.NEO_ASSETS:Constant.GAS_ASSETS,wallet.address(),etAddress.getText().toString().trim(),new BigDecimal(etPrice.getText().toString().trim()).doubleValue(),unspent);
                            data=tx.getData();
                            order="0x"+tx.getID();
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

    private void getOrderInfo(String data,String order) {
        showFixLoading();

        WalletApi.neoWalletOrder(this
                , wallet.getId()
                , data
                , wallet.getAddress()
                , etAddress.getText().toString()
                , etHint.getText().toString()
                , new BigDecimal(etPrice.getText().toString().trim()).toPlainString()
                , "0.0000"
                , "NEO"
                , order
                , type==0?Constant.NEO_ASSETS:Constant.GAS_ASSETS
                , new JsonCallback<LzyResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<Object>> response) {
                        hideFixLoading();
                        ToastUtil.show(R.string.zhuanzhangchenggong);
                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
                        AppManager.getAppManager().finishActivity(TransferAccountsActivity.class);
                        finish();
                    }

                    @Override
                    public void onError(Response<LzyResponse<Object>> response) {
                        super.onError(response);
                        hideFixLoading();
                        if (response.getException().getMessage().contains("wallet_error")){
                            ToastUtil.show(getString(R.string.inner_error)+response.getException().getMessage());
                        }else {
                            ToastUtil.show(getString(R.string.load_error));
                        }
                    }
                });

    }


    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_KEY) {
            KeyEvent key = (KeyEvent) event.getData();
            if (AppUtil.isAddress(key.getKey())) {
                etAddress.setText(key.getKey());
            } else {
                ToastUtil.show(R.string.qingshuruzhengquedizhi);
            }
        }
    }
}
