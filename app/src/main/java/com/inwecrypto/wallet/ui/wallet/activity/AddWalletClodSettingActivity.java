package com.inwecrypto.wallet.ui.wallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;

import butterknife.BindView;
import ethmobile.Ethmobile;
import ethmobile.Wallet;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class AddWalletClodSettingActivity extends BaseActivity {

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
                if (etPs.getText().toString().length() == 0) {
                    ToastUtil.show(getString(R.string.qingxiantianxiemima));
                }else {
                    if (etPs.getText().toString().length()<8){
                        ToastUtil.show(getString(R.string.mimachangdu));
                    }else {
                        if (!AppUtil.isContainAll(etPs.getText().toString())){
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
                if (etPs.getText().toString().length() == 0) {
                    ToastUtil.show(getString(R.string.qingtianxiemima));
                    return;
                }
                if (!AppUtil.isContainAll(etPs.getText().toString())) {
                    ToastUtil.show(getString(R.string.mimayaoqiu));
                    return;
                }
                if (!etPs.getText().toString().equals(etPst.getText().toString())) {
                    ToastUtil.show(getString(R.string.liangcimiamshurubuyiyang));
                    return;
                }

                showLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Wallet wallet = Ethmobile.fromKeyStore("","");
                            String address="";
                            final byte[] json;
//                            json=wallet.encrypt(etPs.getText().toString());
//                            address=wallet.address();

                            final String finalAddress = address;
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideLoading();
                                    Intent intent = new Intent(mActivity, AddWalletClodNfcActivity.class);
                                    intent.putExtra("name", etName.getText().toString());
                                    intent.putExtra("type_id",type_id);
                                    intent.putExtra("address", finalAddress);
                                    //intent.putExtra("json",json);
                                    startActivity(intent);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(getString(R.string.wallet_inner_creat_error));
                                    hideLoading();
                                }
                            });
                        }
                    }
                }).start();

            }});
    }


    @Override
    protected void initData() {
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
