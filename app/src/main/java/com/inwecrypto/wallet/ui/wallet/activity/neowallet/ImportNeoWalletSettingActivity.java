package com.inwecrypto.wallet.ui.wallet.activity.neowallet;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.inwecrypto.wallet.AppApplication;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonRecordBean;
import com.inwecrypto.wallet.bean.MailIconBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.wallet.activity.AddWalletListActivity;
import com.inwecrypto.wallet.ui.wallet.activity.ImportWalletActivity;
import com.inwecrypto.wallet.ui.wallet.activity.ImportWalletTypeActivity;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import neomobile.Neomobile;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class ImportNeoWalletSettingActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.add)
    TextView add;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_ps)
    EditText etPs;
    @BindView(R.id.et_pst)
    EditText etPst;
    @BindView(R.id.hit1)
    TextView hit1;
    @BindView(R.id.hit2)
    View hit2;
    @BindView(R.id.hit3)
    TextView hit3;
    @BindView(R.id.hit4)
    View hit4;

    private String pass;
    private String key;
    private int type;
    private int type_id;
    private ArrayList<WalletBean> wallets;


    @Override
    protected void getBundleExtras(Bundle extras) {
        pass = extras.getString("pass");
        key = extras.getString("key");
        type = extras.getInt("type");
        type_id = extras.getInt("type_id");
        wallets= (ArrayList<WalletBean>) extras.getSerializable("wallets");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_acivity_add_wallet_setting;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(getString(R.string.tianjiaqianbao));
        txtRightTitle.setVisibility(View.GONE);
        etPst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPs.getText().toString().trim().length() == 0) {
                    ToastUtil.show(R.string.qingxiantianxiemima);
                } else {
                    if (etPs.getText().toString().trim().length() < 8) {
                        ToastUtil.show(R.string.mimachangdu);
                    } else {
                        if (!AppUtil.isContainAll(etPs.getText().toString().trim())) {
                            ToastUtil.show(getString(R.string.mimayaoqiu));
                        }
                    }
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().trim().length() == 0) {
                    ToastUtil.show(getString(R.string.qingtianxiemingcheng));
                    return;
                }
                if (type!=1&&type!=4&&etPs.getText().toString().trim().length() == 0) {
                    ToastUtil.show(getString(R.string.qingtianxiemima));
                    return;
                }
                if (type!=1&&type!=4&&!AppUtil.isContainAll(etPs.getText().toString().trim())) {
                    ToastUtil.show(getString(R.string.mimayaoqiu));
                    return;
                }
                if (type!=1&&type!=4&&!etPs.getText().toString().trim().equals(etPst.getText().toString().trim())) {
                    ToastUtil.show(getString(R.string.liangcimiamshurubuyiyang));
                    return;
                }

                showLoading();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            neomobile.Wallet wallet=null;
                            String address="";
                            String keystory="";
                            switch (type){
                                case 1:
                                    wallet= Neomobile.fromKeyStore(key,pass);
                                    keystory=key;
                                    address=wallet.address();
                                    break;
                                case 2:
                                    wallet=Neomobile.fromMnemonic(key);
                                    address=wallet.address();
                                    keystory=wallet.toKeyStore(etPs.getText().toString());
                                    break;
                                case 3:
                                    wallet=Neomobile.fromWIF(key);
                                    address=wallet.address();
                                    keystory=wallet.toKeyStore(etPs.getText().toString());
                                    break;
                                case 4:
                                    address=key;
                                    break;
                                case 5:
                                    break;
                            }


                            if (!AppUtil.isNeoAddress(address)){
                                mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                       ToastUtil.show("请填写正确的NEO钱包");
                                       hideLoading();
                                    }
                                    });
                                return;
                            }

                            if (null!=wallets){
                                for (WalletBean walletBean:wallets){
                                    if (address.contains(walletBean.getAddress().toLowerCase())){
                                        mActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtil.show(R.string.wallet_has_add_error);
                                                hideLoading();
                                            }
                                        });
                                        return;
                                    }
                                }
                            }

                            final String finalKeystory = keystory;
                            final String finalAddress = address;
                            WalletApi.wallet(mActivity,type_id, etName.getText().toString(), address, new JsonCallback<LzyResponse<CommonRecordBean<WalletBean>>>() {
                                @Override
                                public void onSuccess(final Response<LzyResponse<CommonRecordBean<WalletBean>>> response) {
                                    if (4!=type){
                                        //将钱包保存到ACCOUNTMANAGER
                                        saveWallet(finalKeystory, finalAddress, etPs.getText().toString(),etName.getText().toString(), Constant.ZHENGCHANG);
                                    }
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideLoading();
                                            AppManager.getAppManager().finishActivity(ImportWalletActivity.class);
                                            AppManager.getAppManager().finishActivity(ImportWalletTypeActivity.class);
                                            AppManager.getAppManager().finishActivity(AddWalletListActivity.class);
                                            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET));
                                            Intent intent = new Intent(mActivity, NeoWalletActivity.class);
                                            WalletBean walletBean=response.body().data.getRecord();
                                            if (type==4){
                                                walletBean.setType(Constant.GUANCHA);
                                            }else {
                                                walletBean.setType(Constant.ZHENGCHANG);
                                            }
                                            String mailIco=AppApplication.get().getSp().getString(Constant.WALLET_ICO,"[]");
                                            ArrayList<MailIconBean> mailId = GsonUtils.jsonToArrayList(mailIco, MailIconBean.class);
                                            int icon= AppUtil.getRoundmIcon();
                                            mailId.add(new MailIconBean(walletBean.getId(),icon));
                                            AppApplication.get().getSp().putString(Constant.WALLET_ICO,GsonUtils.objToJson(mailId));
                                            walletBean.setIcon(AppUtil.getIcon(icon));
                                            intent.putExtra("wallet",walletBean);
                                            finshTogo(intent);
                                        }
                                    });
                                }

                                @Override
                                public void onError(Response<LzyResponse<CommonRecordBean<WalletBean>>> response) {
                                    super.onError(response);
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideLoading();
                                            ToastUtil.show(R.string.wallet_creat_error);
                                        }
                                    });
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (type) {
                                        case 1:
                                            ToastUtil.show(R.string.jianchamima);
                                            break;
                                        case 2:
                                            ToastUtil.show(R.string.jianchazhujici);
                                            break;
                                        case 3:
                                            ToastUtil.show(R.string.jianchasiyao);
                                            break;
                                    }
                                    hideLoading();
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        switch (type){
            case 1:
                hit1.setVisibility(View.INVISIBLE);
                hit2.setVisibility(View.INVISIBLE);
                hit3.setVisibility(View.INVISIBLE);
                hit4.setVisibility(View.INVISIBLE);
                etPs.setVisibility(View.INVISIBLE);
                etPst.setVisibility(View.INVISIBLE);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                hit1.setVisibility(View.INVISIBLE);
                hit2.setVisibility(View.INVISIBLE);
                hit3.setVisibility(View.INVISIBLE);
                hit4.setVisibility(View.INVISIBLE);
                etPs.setVisibility(View.INVISIBLE);
                etPst.setVisibility(View.INVISIBLE);
                break;
            case 5:
                break;
        }
    }

    private void saveWallet(String json, String address, String pass,String name,String type) {
        AccountManager accountManager = AccountManager.get(this);
        Account account = new Account(address, "com.inwecrypto.wallet");
        accountManager.addAccountExplicitly(account, pass, null);
        accountManager.setUserData(account, "wallet",json);
        accountManager.setUserData(account, "name", name);
        accountManager.setUserData(account, "type", type);
        accountManager.setUserData(account, "wallet_type","hot");

        account=null;
        String wallets= AppApplication.get().getSp().getString(Constant.WALLETS,"");
        if (!wallets.contains(address)){
            wallets=wallets+address+",";
            AppApplication.get().getSp().putString(Constant.WALLETS,wallets);
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
