package com.inwecrypto.wallet.ui.wallet.activity.neowallet;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.ClaimUtxoBean;
import com.inwecrypto.wallet.bean.NeoOderBean;
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
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.newneo.InputPassFragment;
import com.inwecrypto.wallet.ui.wallet.activity.TransferAccountsActivity;
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

public class GetGasActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.gas)
    TextView gasTv;
    @BindView(R.id.cannot_get_gas)
    TextView cannotGetGas;
    @BindView(R.id.gas_num)
    TextView gasNum;
    @BindView(R.id.get_all)
    TextView getAll;
    @BindView(R.id.get_btn)
    TextView getBtn;
    @BindView(R.id.fanwei)
    TextView fanwei;
    @BindView(R.id.jiedong_gas)
    TextView jiedongGas;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private WalletBean wallet;
    private TokenBean.RecordBean neoBean;
    private boolean isClod;

    @Override
    protected void getBundleExtras(Bundle extras) {
        wallet = (WalletBean) extras.getSerializable("wallet");
        neoBean = (TokenBean.RecordBean) extras.getSerializable("neo");
        isClod=extras.getBoolean("");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_activity_neo_get_gas;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.tiqugas);
        txtRightTitle.setVisibility(View.GONE);
        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wallet.getType().equals(Constant.GUANCHA)) {
//                    PackageManager pm = getPackageManager();
//                    boolean nfc = pm.hasSystemFeature(PackageManager.FEATURE_NFC);
//                    if (!nfc) {
//                        ToastUtil.show(R.string.no_nfc_hit);
//                        return;
//                    }
                    ToastUtil.show(getString(R.string.gaslengqianbaotishi));
                    return;
                }

                showFixLoading();

                //先获取订单列表检查是否有未完成的订单
                //请求交易记录
                WalletApi.neoWalletOrder(this, 0,wallet.getId(),"NEO",Constant.GAS_ASSETS, new JsonCallback<LzyResponse<NeoOderBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<NeoOderBean>> response) {
                        if (null!=response&&null!=response.body().data.getList()){
                            boolean canGetGas=true;
                            for (int i=0;i<response.body().data.getList().size();i++){
                                if (response.body().data.getList().get(i).getConfirmTime().equals("")){
                                    canGetGas=false;
                                    break;
                                }
                            }
                            if (canGetGas){
                                getGas();
                            }else {
                                hideFixLoading();
                                ToastUtil.show(getString(R.string.ninhaiyouweiwanchengdedingdan));
                            }
                        }else {
                            getGas();
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<NeoOderBean>> response) {
                        super.onError(response);
                        hideFixLoading();
                        ToastUtil.show(getString(R.string.load_error));
                    }

                });

            }

        });

        jiedongGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wallet.getType().equals(Constant.GUANCHA)) {
//                    PackageManager pm = getPackageManager();
//                    boolean nfc = pm.hasSystemFeature(PackageManager.FEATURE_NFC);
//                    if (!nfc) {
//                        ToastUtil.show(R.string.no_nfc_hit);
//                        return;
//                    }
                    ToastUtil.show(R.string.gaslengqianbaotishi);
                    return;
                }
                if (new BigDecimal(neoBean.getBalance()).intValue()==0){
                    ToastUtil.show(getString(R.string.bukejiedongtishi));
                    return;
                }
                showFixLoading();
                //请求交易记录
                WalletApi.neoWalletOrder(this,0, wallet.getId(), "NEO",Constant.NEO_ASSETS, new JsonCallback<LzyResponse<NeoOderBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<NeoOderBean>> response) {
                        if (null != response && null != response.body().data.getList()) {
                            boolean canGetGas = true;
                            for (int i = 0; i < response.body().data.getList().size(); i++) {
                                if (response.body().data.getList().get(i).getConfirmTime().equals("")) {
                                    canGetGas = false;
                                    break;
                                }
                            }
                            if (canGetGas) {
                                //自己向自己转一次账
                                transferSelf();
                            } else {
                                hideFixLoading();
                                ToastUtil.show(getString(R.string.ninhaiyouweiwanchengdedingdan));
                            }
                        } else {
                            //自己向自己转一次账
                            transferSelf();
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<NeoOderBean>> response) {
                        super.onError(response);
                        hideFixLoading();
                        ToastUtil.show(getString(R.string.load_error));
                    }
                });
            }
        });

        getAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (neoBean != null) {
                    gasNum.setText(neoBean.getGnt().get(0).getAvailable());
                }
            }
        });

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refershData();
            }
        });

        gasTv.setText(neoBean.getGnt().get(0).getAvailable());
        cannotGetGas.setText(getResources().getString(R.string.buketiqugas) + neoBean.getGnt().get(0).getUnavailable());
        gasNum.setText(neoBean.getGnt().get(0).getAvailable());
        //fanwei.setText(getResources().getString(R.string.ketiqu)+"1.0000-"+neoBean.getGnt().get(0).getAvailable());
    }

    @Override
    protected void initData() {

    }

    private void refershData() {

        //请求资产
        WalletApi.conversion(this, wallet.getId(), new JsonCallback<LzyResponse<TokenBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<TokenBean>> response) {
                if (null==gasTv||null==cannotGetGas||null==gasNum){
                    return;
                }
                if (null!=response.body().data&&null!=response.body().data.getRecord()){
                    neoBean=response.body().data.getRecord();
                    gasTv.setText(neoBean.getGnt().get(0).getAvailable());
                    cannotGetGas.setText(getResources().getString(R.string.buketiqugas) + neoBean.getGnt().get(0).getUnavailable());
                    gasNum.setText(neoBean.getGnt().get(0).getAvailable());
                }
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<TokenBean>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<TokenBean>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (null!=swipeRefresh){
                    swipeRefresh.setRefreshing(false);
                }
            }
        });

    }

    private void getGas() {
        //获取 GAS
        WalletApi.getClaimUtxo(this, wallet.getAddress(), new JsonCallback<LzyResponse<ClaimUtxoBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ClaimUtxoBean>> response) {
                if (null==response||null==response.body().data.getResult()){
                    ToastUtil.show(getString(R.string.tiqugasshibaiqingshaohouchongshi));
                    return;
                }
                String utxo= GsonUtils.objToJson(response.body().data.getResult().getClaims());
                transfer(utxo);
            }

            @Override
            public void onError(Response<LzyResponse<ClaimUtxoBean>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.tiqugasshibaiqingshaohouchongshi));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideFixLoading();
            }
        });
    }


    @Override
    protected void EventBean(BaseEventBusBean event) {
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
                        neomobile.Wallet wallet = null;
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
                            Tx tx=wallet.createClaimTx(new BigDecimal(neoBean.getGnt().get(0).getAvailable()).doubleValue(),wallet.address(),unspent);
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
                                dialog.dismiss();
                                getOrderInfo(finalData, finalOrder);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private void getOrderInfo(String data,String order) {
        showFixLoading();
        WalletApi.neoWalletOrder(this
                , wallet.getId()
                , data
                , wallet.getAddress()
                , wallet.getAddress()
                , ""
                , new BigDecimal(neoBean.getGnt().get(0).getAvailable()).toPlainString()
                , "0.0000"
                , "NEO"
                , order
                , Constant.GAS_ASSETS
                , new JsonCallback<LzyResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<Object>> response) {
                        hideFixLoading();
                        ToastUtil.show(getString(R.string.tiquchenggong));
                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
                        AppManager.getAppManager().finishActivity(TransferAccountsActivity.class);
                        finish();
                    }

                    @Override
                    public void onError(Response<LzyResponse<Object>> response) {
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


    private void transferSelf() {

        WalletApi.getUtxo(this, wallet.getAddress(), "neo-asset-id", new JsonCallback<LzyResponse<UtxoBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<UtxoBean>> response) {
                if (null!=response){
                    if (isClod){
                        Intent intent = new Intent(mActivity,WatchNeoTransferAccountsNfcActivity.class);
                        intent.putExtra("wallet",wallet);
                        intent.putExtra("address",wallet.getAddress());
                        intent.putExtra("price",new BigDecimal(neoBean.getBalance()).doubleValue());
                        intent.putExtra("type",0);
                        intent.putExtra("unspent",response.body());
                        keepTogo(intent);
                    }else {
                        if (null!=response.body().data.getResult()){
                            String utxo=GsonUtils.objToJson(response.body().data.getResult());
                            srartTransfer(utxo);
                        }else {
                            ToastUtil.show(getString(R.string.huoquyueshibaiqingshaohouchognshi));
                        }
                    }
                }
            }

            @Override
            public void onError(Response<LzyResponse<UtxoBean>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.huoquyueshibaiqingshaohouchognshi));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideFixLoading();
            }
        });
    }

    private void srartTransfer(final String unspent) {
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
                        neomobile.Wallet wallet = null;
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
                        String order="";
                        try {
                            Tx tx=wallet.createAssertTx(Constant.NEO_ASSETS,wallet.address(),wallet.address(),new BigDecimal(neoBean.getBalance()).doubleValue(),unspent);
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
                                dialog.dismiss();
                                getSelfOrderInfo(finalData, finalOrder);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private void getSelfOrderInfo(String data,String order) {
        showFixLoading();

        WalletApi.neoWalletOrder(this
                , wallet.getId()
                , data
                , wallet.getAddress()
                , wallet.getAddress()
                , ""
                , new BigDecimal(neoBean.getBalance()).toPlainString()
                , "0.0000"
                , "NEO"
                , order
                , Constant.NEO_ASSETS
                , new JsonCallback<LzyResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<Object>> response) {
                        hideFixLoading();
                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
                        EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_JIEDONG_DIALOG));
                        finish();
                    }

                    @Override
                    public void onError(Response<LzyResponse<Object>> response) {
                        super.onError(response);
                        hideFixLoading();
                        if (response.getException().getMessage().contains("wallet_error")){
                            ToastUtil.show(R.string.inner_error);
                            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
                            finish();
                        }else {
                            ToastUtil.show(getString(R.string.load_error));
                        }
                    }
                });

    }
}
