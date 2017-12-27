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
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;

import butterknife.BindView;
import neomobile.Neomobile;
import unichain.ETHWallet;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class AddNeoWalletSettingActivity extends BaseActivity {

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

    private int type_id;
    private ArrayList<WalletBean> wallets;

    @Override
    protected void getBundleExtras(Bundle extras) {
        type_id=extras.getInt("type_id");
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
                }else {
                    if (etPs.getText().toString().trim().length()<8){
                        ToastUtil.show(R.string.mimachangdu);
                    }else {
                        if (!AppUtil.isContainAll(etPs.getText().toString().trim())){
                            ToastUtil.show(getString(R.string.mimayaoqiu));
                        }
                    }
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().trim().length()==0){
                    ToastUtil.show(getString(R.string.qingtianxiemingcheng));
                    return;
                }
                if (etPs.getText().toString().trim().length()==0){
                    ToastUtil.show(getString(R.string.qingtianxiemima));
                    return;
                }
                if (!AppUtil.isContainAll(etPs.getText().toString().trim())){
                    ToastUtil.show(getString(R.string.mimayaoqiu));
                    return;
                }
                if (!etPs.getText().toString().trim().equals(etPst.getText().toString().trim())){
                    ToastUtil.show(getString(R.string.liangcimiamshurubuyiyang));
                    return;
                }
                showLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            neomobile.Wallet neowallet = Neomobile.new_();
                            final String keyStore=neowallet.toKeyStore(etPs.getText().toString().trim());
                            String address=neowallet.address();
                            final String finalAddress = address;
                            if (null!=wallets){
                                for (WalletBean walletBean:wallets){
                                    if (address.contains(walletBean.getAddress())){
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
                            WalletApi.wallet(mActivity,type_id,etName.getText().toString().trim() , address, new JsonCallback<LzyResponse<CommonRecordBean<WalletBean>>>() {
                                @Override
                                public void onSuccess(final Response<LzyResponse<CommonRecordBean<WalletBean>>> response) {
                                    //将钱包保存到ACCOUNTMANAGER
                                    saveWallet(keyStore, finalAddress,etPs.getText().toString().trim(),etName.getText().toString().trim(), Constant.ZHENGCHANG);
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideLoading();
                                            Intent intent=new Intent(mActivity,AddNeoSuccessActivity.class);
                                            WalletBean walletBean=response.body().data.getRecord();
                                            walletBean.setType(Constant.ZHENGCHANG);
                                            String mailIco=AppApplication.get().getSp().getString(Constant.WALLET_ICO,"[]");
                                            ArrayList<MailIconBean> mailId = GsonUtils.jsonToArrayList(mailIco, MailIconBean.class);
                                            int icon= AppUtil.getRoundmIcon();
                                            mailId.add(new MailIconBean(walletBean.getId(),icon));
                                            AppApplication.get().getSp().putString(Constant.WALLET_ICO,GsonUtils.objToJson(mailId));
                                            walletBean.setIcon(AppUtil.getIcon(icon));
                                            WalletBean.CategoryBean category=new WalletBean.CategoryBean();
                                            category.setName("NEO");
                                            walletBean.setCategory(category);
                                            intent.putExtra("wallet",walletBean);
                                            finshTogo(intent);
                                        }
                                    });
                                }

                                @Override
                                public void onError(final Response<LzyResponse<CommonRecordBean<WalletBean>>> response) {
                                    super.onError(response);
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtil.show(getString(R.string.wallet_inner_creat_error)+response.message());
                                            hideLoading();
                                        }
                                    });
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(R.string.wallet_inner_creat_error);
                                    hideLoading();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }

    private void saveWallet(String json,String address,String pass,String name,String type) {
        AccountManager accountManager = AccountManager.get(this);
        Account account=new Account(address,"com.inwecrypto.wallet");
        accountManager.addAccountExplicitly(account, pass, null);
        accountManager.setUserData(account,"wallet", json);
        accountManager.setUserData(account, "name", name);
        accountManager.setUserData(account, "type", type);
        accountManager.setUserData(account, "wallet_type","hot");

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
