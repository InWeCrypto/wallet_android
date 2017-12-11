package com.inwecrypto.wallet.ui.wallet.activity.neowallet;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.inwecrypto.wallet.AppApplication;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.wallet.activity.WatchImportWalletActivity;
import com.inwecrypto.wallet.ui.wallet.activity.WatchImportWalletTypeActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import neomobile.Neomobile;
import unichain.ETHWallet;
import unichain.Unichain;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class WatchImportNeoWalletSettingActivity extends BaseActivity {

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

    private WalletBean watchWallet;
    @Override
    protected void getBundleExtras(Bundle extras) {
        pass = extras.getString("pass");
        key = extras.getString("key");
        type = extras.getInt("type");
        watchWallet= (WalletBean) extras.getSerializable("wallet");
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
        txtMainTitle.setText(R.string.zhuanhuaqianbao);
        txtRightTitle.setVisibility(View.GONE);
        etName.setText(watchWallet.getName());
        etName.setEnabled(false);
        etPst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPs.getText().toString().length() == 0) {
                    ToastUtil.show(R.string.qingxiantianxiemima);
                } else {
                    if (etPs.getText().toString().length() < 8) {
                        ToastUtil.show(R.string.mimachangdu);
                    } else {
                        if (!AppUtil.isContainAll(etPs.getText().toString())) {
                            ToastUtil.show(getString(R.string.mimayaoqiu));
                        }
                    }
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().length() == 0) {
                    ToastUtil.show(getString(R.string.qingtianxiemingcheng));
                    return;
                }
                if (type!=1&&type!=4&&etPs.getText().toString().length() == 0) {
                    ToastUtil.show(getString(R.string.qingtianxiemima));
                    return;
                }
                if (type!=1&&type!=4&&!AppUtil.isContainAll(etPs.getText().toString())) {
                    ToastUtil.show(getString(R.string.mimayaoqiu));
                    return;
                }
                if (type!=1&&type!=4&&!etPs.getText().toString().equals(etPst.getText().toString())) {
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
                            String json="";
                            switch (type){
                                case 1:
                                    wallet= Neomobile.fromKeyStore(key.trim(),pass.trim());
                                    json=key.trim();
                                    address=wallet.address();
                                    break;
                                case 2:
                                    wallet=Neomobile.fromMnemonic(key.trim());
                                    address=wallet.address();
                                    json=wallet.toKeyStore(etPs.getText().toString());
                                    break;
                                case 3:
                                    wallet=Neomobile.fromWIF(key.trim());
                                    address=wallet.address();
                                    json=wallet.toKeyStore(etPs.getText().toString());
                                    break;
                                case 4:
                                    address=key;
                                    break;
                                case 5:
                                    break;
                            }

                            //将钱包保存到ACCOUNTMANAGER
                            saveWallet(json, address, etPs.getText().toString().trim(),etName.getText().toString().trim(), Constant.ZHENGCHANG);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(getString(R.string.qianbaozhuanhuachenggong));
                                    AppManager.getAppManager().finishActivity(WatchImportNeoWalletActivity.class);
                                    AppManager.getAppManager().finishActivity(WatchImportWalletTypeActivity.class);
                                    EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WATCH_TRANSFER));
                                    EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET));
                                    finish();
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    switch (type) {
                                        case 2:
                                            ToastUtil.show(R.string.qingjianchazhujicishifoushuruzhengque);
                                            break;
                                        case 3:
                                            ToastUtil.show(R.string.qingjianchasiyaoshifoushuruzhengque);
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
        accountManager.setUserData(account, "wallet", json);
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
