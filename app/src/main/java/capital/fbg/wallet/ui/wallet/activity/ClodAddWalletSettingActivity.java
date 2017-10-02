package capital.fbg.wallet.ui.wallet.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.model.Response;

import java.util.Random;

import butterknife.BindView;
import capital.fbg.wallet.AppApplication;
import capital.fbg.wallet.R;
import capital.fbg.wallet.base.BaseActivity;
import capital.fbg.wallet.bean.CommonRecordBean;
import capital.fbg.wallet.bean.WalletBean;
import capital.fbg.wallet.common.Constant;
import capital.fbg.wallet.common.http.LzyResponse;
import capital.fbg.wallet.common.http.api.WalletApi;
import capital.fbg.wallet.common.http.callback.JsonCallback;
import capital.fbg.wallet.common.util.AppUtil;
import capital.fbg.wallet.common.util.ToastUtil;
import capital.fbg.wallet.event.BaseEventBusBean;
import unichain.ETHWallet;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class ClodAddWalletSettingActivity extends BaseActivity {
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

    @Override
    protected void getBundleExtras(Bundle extras) {
        type_id=extras.getInt("type_id");
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
        txtMainTitle.setText(getString(R.string.add_wallet_title));
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
                if (etPs.getText().toString().length() == 0) {
                    ToastUtil.show("请先填写密码");
                }else {
                    if (etPs.getText().toString().length()<8){
                        ToastUtil.show("密码长度不得少于8位");
                    }else {
                        if (!isContainAll(etPs.getText().toString())){
                            ToastUtil.show(getString(R.string.wallet_hit15));
                        }
                    }
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().length()==0){
                    ToastUtil.show(getString(R.string.wallet_hit18));
                    return;
                }
                if (etPs.getText().toString().length()==0){
                    ToastUtil.show(getString(R.string.wallet_hit_19));
                    return;
                }
                if (!isContainAll(etPs.getText().toString())){
                    ToastUtil.show(getString(R.string.wallet_hit15));
                    return;
                }
                if (!etPs.getText().toString().equals(etPst.getText().toString())){
                    ToastUtil.show(getString(R.string.wallet_hit_20));
                    return;
                }
                showLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ETHWallet wallet = new ETHWallet();
                        String address="";
                        final byte[] json;
                        try {
                            wallet.encrypt(etPs.getText().toString());
                            address=wallet.address().toLowerCase();
                            json=wallet.encrypt(etPs.getText().toString());
                            final int icon= AppUtil.getRoundmIcon();
                            //将钱包保存到ACCOUNTMANAGER
                            saveWallet(json, address,etPs.getText().toString(),etName.getText().toString(),icon);
                            final String finalAddress = address;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideLoading();
                                    Intent intent=new Intent(mActivity,ClodAddEthSuccessActivity.class);
                                    intent.putExtra("address", finalAddress);
                                    intent.putExtra("icon",AppUtil.getIcon(icon));
                                    finshTogo(intent);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideLoading();
                                    ToastUtil.show(getString(R.string.wallet_hit21));
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }

    private void saveWallet(byte[] json,String address,String pass,String name,int icon) {
        AccountManager accountManager = AccountManager.get(this);
        Account account=new Account(address,"capital.fbg.wallet");
        accountManager.addAccountExplicitly(account, pass, null);
        accountManager.setUserData(account,"wallet", new String(json));
        accountManager.setUserData(account, "name", name);
        accountManager.setUserData(account, "type", Constant.CLOD);
        accountManager.setUserData(account,"icon",icon+"");
        accountManager.setUserData(account, "wallet_type","clod");
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

    /**
     * 规则3：必须同时包含大小写字母及数字
     * 是否包含
     *
     * @param str
     * @return
     */
    public static boolean isContainAll(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLowerCase = false;//定义一个boolean值，用来表示是否包含字母
        boolean isUpperCase = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLowerCase(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLowerCase = true;
            } else if (Character.isUpperCase(str.charAt(i))) {
                isUpperCase = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLowerCase && isUpperCase && str.matches(regex);
        return isRight;
    }

}
