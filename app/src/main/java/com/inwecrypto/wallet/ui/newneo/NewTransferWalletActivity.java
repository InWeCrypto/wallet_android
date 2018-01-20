package com.inwecrypto.wallet.ui.newneo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.event.KeyEvent;
import com.inwecrypto.wallet.ui.ScanActivity;
import com.inwecrypto.wallet.ui.wallet.activity.WatchImportWalletTypeActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import neomobile.Neomobile;
import neomobile.Wallet;

/**
 * 作者：xiaoji06 on 2018/1/8 14:07
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class NewTransferWalletActivity extends BaseActivity {


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
    @BindView(R.id.title)
    TextView title;
    private int type = 1;
    private String scanKey;
    private String pass;

    private WalletBean watchWallet;

    @Override
    protected void getBundleExtras(Bundle extras) {
        watchWallet = (WalletBean) extras.getSerializable("wallet");
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

        title.setText("转化钱包");

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

        addressll.setVisibility(View.INVISIBLE);

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
                            ToastUtil.show("请输入正确的Keystore");
                            return;
                        }
                        //密码确认
                        InputPassFragment input = new InputPassFragment();
                        input.show(fm, "input");
                        input.setOnNextListener(new InputPassFragment.OnNextInterface() {
                            @Override
                            public void onNext(String passWord, Dialog dialog) {
                                pass = passWord;
                                importWallet();
                            }
                        });
                        break;
                    case 2:
                    case 3:
                        //弹出钱包名称选择框
                        showWalletName(2);
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
        bundle.putString("name", watchWallet.getName());
        bundle.putBoolean("isEdit", false);
        improt.setArguments(bundle);
        improt.show(fm, "improt");
        improt.setOnNextListener(new ImportWalletFragment.OnNextInterface() {
            @Override
            public void onNext(String nameInput, String passInput, Dialog dialog) {
                if (type != 1) {
                    pass = passInput;
                }
                importWallet();

            }
        });
    }

    private void importWallet() {
        showLoading();

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
                            if (!address.equals(watchWallet.getAddress())) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.show(R.string.qingshurugaiqianbaodezhengquekeystore);
                                        hideLoading();
                                    }
                                });
                                return;
                            }
                            break;
                        case 2:
                            wallet = Neomobile.fromMnemonic(scanKey,App.get().isZh()?"zh_CN":"en_US");
                            address = wallet.address();
                            keystory = wallet.toKeyStore(pass);
                            if (!address.equals(watchWallet.getAddress())) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.show(R.string.qingshurugaiqianbaodezhengquezhujici);
                                        hideLoading();
                                    }
                                });
                                return;
                            }
                            break;
                        case 3:
                            wallet = Neomobile.fromWIF(scanKey);
                            address = wallet.address();
                            keystory = wallet.toKeyStore(pass);
                            if (!address.equals(watchWallet.getAddress())) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.show(R.string.qingshurugaiqianbaodezhengquesiyao);
                                        hideLoading();
                                    }
                                });
                                return;
                            }
                            break;
                        case 4:
                            address = scanKey;
                            break;
                    }


                    if (!AppUtil.isNeoAddress(address)) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show("请填写正确的NEO钱包");
                                hideLoading();
                            }
                        });
                        return;
                    }

                    saveWallet(keystory, address, pass, watchWallet.getName(), Constant.ZHENGCHANG);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoading();
                            ToastUtil.show(getString(R.string.qianbaozhuanhuachenggong));
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
                keystore.setTextColor(getResources().getColor(R.color.c_0A9234));
                l1.setVisibility(View.VISIBLE);
                break;
            case 2:
                etInfo.setHint(R.string.zhujici_hit);
                word.setTextColor(getResources().getColor(R.color.c_0A9234));
                l2.setVisibility(View.VISIBLE);
                break;
            case 3:
                etInfo.setHint(R.string.siyao_hit);
                key.setTextColor(getResources().getColor(R.color.c_0A9234));
                l3.setVisibility(View.VISIBLE);
                break;
            case 4:
                etInfo.setHint(R.string.guancha_hit);
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
                        ToastUtil.show("请输入正确的Keystore");
                        return;
                    }
                    break;
                case 4:
                    if (!AppUtil.isNeoAddress(keyEvent.getKey().trim())) {
                        ToastUtil.show("请输入正确的NEO钱包地址");
                        return;
                    }
                    break;
            }
            scanKey = keyEvent.getKey().trim();
            etInfo.setText(keyEvent.getKey().trim());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
