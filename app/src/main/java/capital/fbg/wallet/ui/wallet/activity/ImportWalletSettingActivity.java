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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import capital.fbg.wallet.AppApplication;
import capital.fbg.wallet.R;
import capital.fbg.wallet.base.BaseActivity;
import capital.fbg.wallet.bean.CommonRecordBean;
import capital.fbg.wallet.bean.MailIconBean;
import capital.fbg.wallet.bean.WalletBean;
import capital.fbg.wallet.common.Constant;
import capital.fbg.wallet.common.http.LzyResponse;
import capital.fbg.wallet.common.http.api.WalletApi;
import capital.fbg.wallet.common.http.callback.JsonCallback;
import capital.fbg.wallet.common.util.AppManager;
import capital.fbg.wallet.common.util.AppUtil;
import capital.fbg.wallet.common.util.GsonUtils;
import capital.fbg.wallet.common.util.ToastUtil;
import capital.fbg.wallet.event.BaseEventBusBean;
import unichain.ETHWallet;
import unichain.Unichain;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class ImportWalletSettingActivity extends BaseActivity {

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
                } else {
                    if (etPs.getText().toString().length() < 8) {
                        ToastUtil.show("密码长度不得少于8位");
                    } else {
                        if (!isContainAll(etPs.getText().toString())) {
                            ToastUtil.show(getString(R.string.wallet_hit15));
                        }
                    }
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().length() == 0) {
                    ToastUtil.show(getString(R.string.wallet_hit18));
                    return;
                }
                if (type!=1&&type!=4&&etPs.getText().toString().length() == 0) {
                    ToastUtil.show(getString(R.string.wallet_hit_19));
                    return;
                }
                if (type!=1&&type!=4&&!isContainAll(etPs.getText().toString())) {
                    ToastUtil.show(getString(R.string.wallet_hit15));
                    return;
                }
                if (type!=1&&type!=4&&!etPs.getText().toString().equals(etPst.getText().toString())) {
                    ToastUtil.show(getString(R.string.wallet_hit_20));
                    return;
                }

                showLoading();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            ETHWallet wallet=null;
                            String address="";
                            byte[] json=new byte[0];
                            switch (type){
                                case 1:
                                    wallet=Unichain.openETHWallet(key.getBytes("utf-8"),pass);
                                    json=key.getBytes();
                                    address=wallet.address().toLowerCase();
                                    break;
                                case 2:
                                    wallet=Unichain.ethWalletFromMnemonic(key);
                                    address=wallet.address().toLowerCase();
                                    json=wallet.encrypt(etPs.getText().toString());
                                    break;
                                case 3:
                                    wallet=Unichain.ethWalletFromPrivateKey(key);
                                    address=wallet.address().toLowerCase();
                                    json=wallet.encrypt(etPs.getText().toString());
                                    break;
                                case 4:
                                    address=key;
                                    break;
                                case 5:
                                    break;
                            }

                            if (null!=wallets){
                                for (WalletBean walletBean:wallets){
                                    if (address.contains(walletBean.getAddress().toLowerCase())){
                                        mActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtil.show("该钱包已添加，不能重复添加");
                                                hideLoading();
                                            }
                                        });
                                        return;
                                    }
                                }
                            }

                            final byte[] finalJson = json;
                            final String finalAddress = address;
                            WalletApi.wallet(mActivity,type_id, etName.getText().toString(), address, new JsonCallback<LzyResponse<CommonRecordBean<WalletBean>>>() {
                                @Override
                                public void onSuccess(final Response<LzyResponse<CommonRecordBean<WalletBean>>> response) {
                                    if (4!=type){
                                        //将钱包保存到ACCOUNTMANAGER
                                        saveWallet(finalJson, finalAddress, etPs.getText().toString(),etName.getText().toString(), Constant.ZHENGCHANG);
                                    }
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideLoading();
                                            AppManager.getAppManager().finishActivity(ImportWalletActivity.class);
                                            AppManager.getAppManager().finishActivity(ImportWalletTypeActivity.class);
                                            AppManager.getAppManager().finishActivity(AddWalletListActivity.class);
                                            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET));
                                            Intent intent = new Intent(mActivity, HotWalletActivity.class);
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
                                            ToastUtil.show("钱包创建失败！请检查网络后重试");
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
                                            ToastUtil.show("请检查钱包密码是否输入正确");
                                            break;
                                        case 2:
                                            ToastUtil.show("请检查助记词是否输入正确");
                                            break;
                                        case 3:
                                            ToastUtil.show("请检查私钥是否输入正确");
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

    private void saveWallet(byte[] json, String address, String pass,String name,String type) {
        AccountManager accountManager = AccountManager.get(this);
        Account account = new Account(address, "capital.fbg.wallet");
        accountManager.addAccountExplicitly(account, pass, null);
        accountManager.setUserData(account, "wallet", new String(json));
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
