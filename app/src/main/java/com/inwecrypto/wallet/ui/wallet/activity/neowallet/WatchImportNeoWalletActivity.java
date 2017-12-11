package com.inwecrypto.wallet.ui.wallet.activity.neowallet;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.inwecrypto.wallet.AppApplication;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.MaterialDialog;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.event.KeyEvent;
import com.inwecrypto.wallet.ui.ScanActivity;
import com.inwecrypto.wallet.ui.wallet.activity.WatchImportWalletSettingActivity;
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

public class WatchImportNeoWalletActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.hit)
    TextView hit;
    @BindView(R.id.et_info)
    EditText etInfo;
    @BindView(R.id.tv_import)
    TextView tvImport;

    private int type;

    private MaterialDialog mMaterialDialog;

    private WalletBean watchWallet;

    @Override
    protected void getBundleExtras(Bundle extras) {
        type=extras.getInt("type");
        watchWallet= (WalletBean) extras.getSerializable("wallet");
        isOpenEventBus=true;
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_acivity_import_wallet;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Drawable drawableInfo= getResources().getDrawable(R.mipmap.import_scan);
        /// 这一步必须要做,否则不会显示.
        drawableInfo.setBounds(0, 0, drawableInfo.getMinimumWidth(), drawableInfo.getMinimumHeight());
        txtRightTitle.setCompoundDrawables(drawableInfo,null,null,null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity, ScanActivity.class);
                intent.putExtra("type",1);
                keepTogo(intent);
            }
        });

        tvImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etInfo.getText().toString().length() == 0) {
                    ToastUtil.show(R.string.tianxieneirongbunengweikong);
                    return;
                }
                switch (type){
                    case 1:
                        impotKeystore(etInfo.getText().toString());
                        break;
                    case 2:
                        impotAnquanma(etInfo.getText().toString());
                        break;
                    case 3:
                        impotKey(etInfo.getText().toString());
                        break;
                }
            }
        });
        switch (type){
            case 1:
                txtMainTitle.setText(R.string.tianjiakeystore);
                hit.setText(R.string.keystore_hit);
                break;
            case 2:
                txtMainTitle.setText(R.string.tianjiazhujici);
                hit.setText(R.string.zhujici_hit);
                break;
            case 3:
                txtMainTitle.setText(R.string.tianjiamingwensiyao);
                hit.setText(R.string.siyao_hit);
                break;
        }
    }

    private void impotKeystore(final String key) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_dialog_pass, null, false);
        final EditText pass= (EditText) view.findViewById(R.id.et_pass);
        TextView cancle= (TextView) view.findViewById(R.id.cancle);
        TextView ok= (TextView) view.findViewById(R.id.ok);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass.getText().toString().length()==0){
                    ToastUtil.show(getString(R.string.qingshurumima));
                    return;
                }
                importKey(pass.getText().toString());
            }
        });

        mMaterialDialog = new MaterialDialog(mActivity).setView(view);
        mMaterialDialog.setBackgroundResource(R.drawable.trans_bg);
        mMaterialDialog.setCanceledOnTouchOutside(true);
        mMaterialDialog.show();
    }

    private void impotAnquanma(String key) {
        Intent intent=new Intent(mActivity,WatchImportNeoWalletSettingActivity.class);
        intent.putExtra("wallet",watchWallet);
        intent.putExtra("pass","");
        intent.putExtra("key",key);
        intent.putExtra("type",type);
        keepTogo(intent);
    }

    private void impotKey(String key) {
        Intent intent=new Intent(mActivity,WatchImportNeoWalletSettingActivity.class);
        intent.putExtra("wallet",watchWallet);
        intent.putExtra("pass","");
        intent.putExtra("key",key);
        intent.putExtra("type",type);
        keepTogo(intent);
    }

    private void importKey(final String pass){
        showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    neomobile.Wallet wallet= Neomobile.fromKeyStore(etInfo.getText().toString().trim(),pass.trim());

                    //将钱包保存到ACCOUNTMANAGER
                    saveWallet(etInfo.getText().toString().trim()
                            , wallet.address()
                            , pass
                            , watchWallet.getName()
                            , Constant.ZHENGCHANG);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoading();
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
                            ToastUtil.show(getString(R.string.qingjianchaqianbaomimashifoushuruzhengque));
                            hideLoading();
                        }
                    });
                }
            }
        }).start();
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
        if (event.getEventCode()== Constant.EVENT_KEY){
            KeyEvent keyEvent= (KeyEvent) event.getData();
            etInfo.setText(keyEvent.getKey());
        }
    }
}
