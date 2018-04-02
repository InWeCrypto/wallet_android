package com.inwecrypto.wallet.ui.newneo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
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
import com.inwecrypto.wallet.event.KeyEvent;
import com.inwecrypto.wallet.ui.ScanActivity;
import com.inwecrypto.wallet.ui.wallet.activity.HotWalletActivity;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ethmobile.Ethmobile;
import neomobile.Neomobile;
import neomobile.Wallet;

/**
 * 作者：xiaoji06 on 2018/1/8 14:07
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class  NewImprotWalletActivity extends BaseActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.keystore)
    TextView keystore;
    @BindView(R.id.l1)
    View l1;
    @BindView(R.id.keystorell)
    LinearLayout keystorell;
    @BindView(R.id.word)
    TextView word;
    @BindView(R.id.l2)
    View l2;
    @BindView(R.id.wordll)
    LinearLayout wordll;
    @BindView(R.id.key)
    TextView key;
    @BindView(R.id.l3)
    View l3;
    @BindView(R.id.keyll)
    LinearLayout keyll;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.l4)
    View l4;
    @BindView(R.id.addressll)
    LinearLayout addressll;
    @BindView(R.id.scan)
    ImageView scan;
    @BindView(R.id.et_info)
    EditText etInfo;
    @BindView(R.id.tv_import)
    TextView tvImport;
    private int type = 1;
    private ArrayList<WalletBean> wallets;
    private String scanKey;
    private String pass;
    private String name;

    private boolean isNeo;

    private String type1="";
    private String type2="";
    private String type3="";
    private String type4="";

    @Override
    protected void getBundleExtras(Bundle extras) {
        wallets = (ArrayList<WalletBean>) extras.getSerializable("wallets");
        isNeo=extras.getBoolean("isNeo");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.newneo_improtwallet_activity;
    }

    @Override
    protected void initView() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ScanActivity.class);
                intent.putExtra("type", 1);
                keepTogo(intent);
            }
        });

        keystorell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType(1);
            }
        });

        wordll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType(2);
            }
        });

        keyll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType(3);
            }
        });

        addressll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType(4);
            }
        });

        etInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (type){
                    case 1:
                        type1=s.toString();
                        break;
                    case 2:
                        type2=s.toString();
                        break;
                    case 3:
                        type3=s.toString();
                        break;
                    case 4:
                        type4=s.toString();
                        break;
                }
            }
        });

        tvImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etInfo.getText().toString().length() == 0) {
                    ToastUtil.show(getString(R.string.tianxieneirongbuenngweikong));
                    return;
                }

                scanKey = etInfo.getText().toString().trim();

                //判断数据是否输入正确
                FragmentManager fm = getSupportFragmentManager();
                switch (type) {
                    case 1:
                        if (!scanKey.contains("{")) {
                            ToastUtil.show(getString(R.string.qingshuruzhengquedekeystore));
                            return;
                        }
                        //密码确认
                        InputPassFragment input = new InputPassFragment();
                        input.show(fm, "input");
                        input.setOnNextListener(new InputPassFragment.OnNextInterface() {
                            @Override
                            public void onNext(String passWord, Dialog dialog) {
                                pass = passWord;
                                //弹出钱包名称选择框
                                showWalletName(1);
                            }
                        });
                        break;
                    case 2:
                    case 3:
                        //弹出钱包名称选择框
                        showWalletName(2);
                        break;
                    case 4:
                        //弹出钱包名称选择框
                        showWalletName(1);
                        break;
                }

            }
        });
    }

    private void showWalletName(int i) {
        FragmentManager fm = getSupportFragmentManager();
        ImportWalletFragment improt = new ImportWalletFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", i);
        improt.setArguments(bundle);
        improt.show(fm, "improt");
        improt.setOnNextListener(new ImportWalletFragment.OnNextInterface() {
            @Override
            public void onNext(String nameInput, String passInput, Dialog dialog) {
                name = nameInput;
                if (type != 1 && type != 4) {
                    pass = passInput;
                }
                importWallet();

            }
        });
    }

    private void importWallet() {
        showLoading();

        if (isNeo){
            importNeoWallet();
        }else {
            importEthWallet();
        }
    }

    private void importEthWallet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    ethmobile.Wallet wallet=null;
                    String address="";
                    String json="";
                    switch (type){
                        case 1:
                            wallet=Ethmobile.fromKeyStore(scanKey,pass);
                            address=wallet.address();
                            json=scanKey;
                            if (!scanKey.toLowerCase().contains(address.toLowerCase().replace("0x",""))){
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.show(R.string.qingshuruzhengquedekeystore);
                                        hideLoading();
                                    }});
                                return;
                            }
                            break;
                        case 2:
                            wallet=Ethmobile.fromMnemonic(scanKey,App.get().isZh()?"zh_CN":"en_US");
                            address=wallet.address();
                            json=wallet.toKeyStore(pass);
                            break;
                        case 3:
                            wallet=Ethmobile.fromPrivateKey(AppUtil.hexStringToBytes(scanKey));
                            address=wallet.address();
                            json=wallet.toKeyStore(pass);
                            break;
                        case 4:
                            address=scanKey;
                            break;
                        case 5:
                            break;
                    }

                    if (null!=wallets){
                        for (WalletBean walletBean:wallets){
                            if (scanKey.toLowerCase().contains(walletBean.getAddress().replace("0x","").toLowerCase())){
                                ToastUtil.show(getString(R.string.wallet_has_add_error));
                                return;
                            }
                        }
                    }

                    final String finalJson = json;
                    final String finalAddress = address.toLowerCase();
                    WalletApi.wallet(mActivity,1, name, address,"", new JsonCallback<LzyResponse<CommonRecordBean<WalletBean>>>() {
                        @Override
                        public void onSuccess(final Response<LzyResponse<CommonRecordBean<WalletBean>>> response) {
                            if (4!=type){
                                //将钱包保存到ACCOUNTMANAGER
                                saveWallet(finalJson, finalAddress, pass,name, Constant.ZHENGCHANG);
                            }
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideLoading();
                                    AppManager.getAppManager().finishActivity(NewNeoWalletListActivity.class);
                                    EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET));
                                    Intent intent = new Intent(mActivity, HotWalletActivity.class);
                                    WalletBean walletBean=response.body().data.getRecord();
                                    if (type==4){
                                        walletBean.setType(Constant.GUANCHA);
                                    }else {
                                        walletBean.setType(Constant.ZHENGCHANG);
                                    }
                                    WalletBean.CategoryBean categoryBean=new WalletBean.CategoryBean();
                                    categoryBean.setName("ETH");
                                    walletBean.setCategory(categoryBean);
                                    String mailIco= App.get().getSp().getString(Constant.WALLET_ICO,"[]");
                                    ArrayList<MailIconBean> mailId = GsonUtils.jsonToArrayList(mailIco, MailIconBean.class);
                                    int icon= AppUtil.getRoundmIcon();
                                    mailId.add(new MailIconBean(walletBean.getId(),icon));
                                    App.get().getSp().putString(Constant.WALLET_ICO,GsonUtils.objToJson(mailId));
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

    private void importNeoWallet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Wallet wallet = null;
                    String address = "";
                    String keystory = "";
                    switch (type) {
                        case 1:
                            wallet = Neomobile.fromKeyStore(scanKey, pass);
                            keystory = scanKey;
                            address = wallet.address();
                            break;
                        case 2:
                            wallet = Neomobile.fromMnemonic(scanKey, App.get().isZh()?"zh_CN":"en_US");
                            address = wallet.address();
                            keystory = wallet.toKeyStore(pass);
                            break;
                        case 3:
                            wallet = Neomobile.fromWIF(scanKey);
                            address = wallet.address();
                            keystory = wallet.toKeyStore(pass);
                            break;
                        case 4:
                            address = scanKey;
                            break;
                    }


                    if (!AppUtil.isNeoAddress(address)) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show(getString(R.string.qingshuruzhengquedeneoqianbaodizhi));
                                hideLoading();
                            }
                        });
                        return;
                    }

                    if (null != wallets) {
                        for (WalletBean walletBean : wallets) {
                            if (address.toLowerCase().contains(walletBean.getAddress().toLowerCase())) {
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
                    final String finalAddress = address.toLowerCase();
                    String hashAddress = "";
                    try {
                        hashAddress = Neomobile.decodeAddress(address);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideLoading();
                                ToastUtil.show(getString(R.string.wallet_creat_error));
                            }
                        });
                        return;
                    }
                    WalletApi.wallet(mActivity, 2, name, address, hashAddress, new JsonCallback<LzyResponse<CommonRecordBean<WalletBean>>>() {
                        @Override
                        public void onSuccess(final Response<LzyResponse<CommonRecordBean<WalletBean>>> response) {
                            if (4 != type) {
                                //将钱包保存到ACCOUNTMANAGER
                                saveWallet(finalKeystory, finalAddress.toLowerCase(), pass, name, Constant.ZHENGCHANG);
                            }
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideLoading();
                                    AppManager.getAppManager().finishActivity(NewNeoWalletListActivity.class);
                                    EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET));
                                    Intent intent = new Intent(mActivity, NewNeoWalletActivity.class);
                                    WalletBean walletBean = response.body().data.getRecord();
                                    if (type == 4) {
                                        walletBean.setType(Constant.GUANCHA);
                                    } else {
                                        walletBean.setType(Constant.ZHENGCHANG);
                                    }
                                    String mailIco = App.get().getSp().getString(Constant.WALLET_ICO, "[]");
                                    ArrayList<MailIconBean> mailId = GsonUtils.jsonToArrayList(mailIco, MailIconBean.class);
                                    int icon = AppUtil.getRoundmIcon();
                                    mailId.add(new MailIconBean(walletBean.getId(), icon));
                                    App.get().getSp().putString(Constant.WALLET_ICO, GsonUtils.objToJson(mailId));
                                    walletBean.setIcon(AppUtil.getIcon(icon));
                                    intent.putExtra("wallet", walletBean);
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

    private void selectType(int i) {
        if (type == i) {
            return;
        }
        type = i;
        l1.setVisibility(View.INVISIBLE);
        l2.setVisibility(View.INVISIBLE);
        l3.setVisibility(View.INVISIBLE);
        l4.setVisibility(View.INVISIBLE);

        keystore.setTextColor(getResources().getColor(R.color.c_333333));
        word.setTextColor(getResources().getColor(R.color.c_333333));
        key.setTextColor(getResources().getColor(R.color.c_333333));
        address.setTextColor(getResources().getColor(R.color.c_333333));

        switch (type) {
            case 1:
                etInfo.setHint(R.string.keystore_hit);
                etInfo.setText(type1);
                keystore.setTextColor(getResources().getColor(R.color.c_0A9234));
                l1.setVisibility(View.VISIBLE);
                break;
            case 2:
                etInfo.setHint(R.string.zhujici_hit);
                etInfo.setText(type2);
                word.setTextColor(getResources().getColor(R.color.c_0A9234));
                l2.setVisibility(View.VISIBLE);
                break;
            case 3:
                etInfo.setHint(R.string.siyao_hit);
                etInfo.setText(type3);
                key.setTextColor(getResources().getColor(R.color.c_0A9234));
                l3.setVisibility(View.VISIBLE);
                break;
            case 4:
                etInfo.setHint(R.string.guancha_hit);
                etInfo.setText(type4);
                address.setTextColor(getResources().getColor(R.color.c_0A9234));
                l4.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void initData() {

    }

    private void saveWallet(String json, String address, String pass, String name, String type) {
        AccountManager accountManager = AccountManager.get(this);
        Account account = new Account(address, "com.inwecrypto.wallet");
        accountManager.addAccountExplicitly(account, pass, null);
        accountManager.setUserData(account, "wallet", json);
        accountManager.setUserData(account, "name", name);
        accountManager.setUserData(account, "type", type);
        accountManager.setUserData(account, "wallet_type", "hot");

        account = null;
        String wallets = App.get().getSp().getString(Constant.WALLETS, "");
        if (!wallets.contains(address)) {
            wallets = wallets + address + ",";
            App.get().getSp().putString(Constant.WALLETS, wallets);
        }
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_KEY) {
            KeyEvent keyEvent = (KeyEvent) event.getData();
            switch (type) {
                case 1:
                    if (!keyEvent.getKey().contains("{")) {
                        ToastUtil.show(getString(R.string.qingshuruzhengquedekeystore));
                        return;
                    }
                    break;
                case 4:
                    if (!AppUtil.isNeoAddress(keyEvent.getKey().trim())) {
                        ToastUtil.show(getString(R.string.qingshuruzhengquedeneoqianbaodizhi));
                        return;
                    }
                    break;
            }
            scanKey = keyEvent.getKey().trim();
            etInfo.setText(keyEvent.getKey().trim());
        }
    }
}
