package com.inwecrypto.wallet.ui.wallet.activity.neowallet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.ClaimUtxoBean;
import com.inwecrypto.wallet.bean.IcoGas;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.event.KeyEvent;
import com.inwecrypto.wallet.ui.ScanActivity;
import com.lzy.okgo.model.Response;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/1/4 10:55
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class NeoTokenSaleActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.amount)
    EditText amount;
    @BindView(R.id.neo_amount)
    TextView neoAmount;
    @BindView(R.id.fei)
    TextView fei;
    @BindView(R.id.tip)
    TextView tip;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.gas_num)
    TextView gasNum;
    private WalletBean wallet;
    private TokenBean.RecordBean neoBean;
    private double gas;

    @Override
    protected void getBundleExtras(Bundle extras) {
        isOpenEventBus = true;
        wallet = (WalletBean) extras.getSerializable("wallet");
        neoBean = (TokenBean.RecordBean) extras.getSerializable("neo");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_activity_neo_token_sale;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText("Token Sale");
        Drawable drawableInfo = getResources().getDrawable(R.mipmap.nav_scan);
        /// 这一步必须要做,否则不会显示.
        drawableInfo.setBounds(0, 0, drawableInfo.getMinimumWidth(), drawableInfo.getMinimumHeight());
        txtRightTitle.setCompoundDrawables(drawableInfo, null, null, null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keepTogo(ScanActivity.class);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppUtil.isAddress(etAddress.getText().toString().trim())) {
                    ToastUtil.show(getString(R.string.qingtianxieheyuedizhi));
                    return;
                }

                if (amount.getText().toString().trim().length() == 0) {
                    ToastUtil.show(R.string.qingtianxiezhuanzhangjine);
                    return;
                }
                try {
                    new BigDecimal(amount.getText().toString().trim());
                } catch (Exception e) {
                    ToastUtil.show(R.string.qingtianxiezhengquedejine);
                    return;
                }

                //获取交易GAS手续费
                WalletApi.getIcoGas(this, etAddress.getText().toString().trim(), new JsonCallback<LzyResponse<IcoGas>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<IcoGas>> response) {
                        if (null!=response&&null!=response.body().data.getGas_consumed()){
                            if (new BigDecimal(response.body().data.getGas_consumed()).doubleValue()<10){
                                Intent intent=new Intent(mActivity,NeoTokenSaleConfirmActivity.class);
                                intent.putExtra("wallet",wallet);
                                intent.putExtra("neo",neoBean);
                                intent.putExtra("address",etAddress.getText().toString().trim());
                                intent.putExtra("amount",amount.getText().toString().trim());
                                intent.putExtra("gas","0.0000");
                                intent.putExtra("yuangas",response.body().data.getGas_consumed());
                                keepTogo(intent);
                            }else {
                                if (new BigDecimal(response.body().data.getGas_consumed()).doubleValue()<gas){
                                    Intent intent=new Intent(mActivity,NeoTokenSaleConfirmActivity.class);
                                    intent.putExtra("wallet",wallet);
                                    intent.putExtra("neo",neoBean);
                                    intent.putExtra("address",etAddress.getText().toString().trim());
                                    intent.putExtra("amount",amount.getText().toString().trim());
                                    intent.putExtra("gas",response.body().data.getGas_consumed());
                                    intent.putExtra("yuangas",response.body().data.getGas_consumed());
                                    keepTogo(intent);
                                }else {
                                    ToastUtil.show(getString(R.string.shouxufeibuzu_qingshaohouchongshi));
                                }
                            }
                        }else {
                            ToastUtil.show(getString(R.string.houqushouxufeishibai_qingshaohouchongshi));
                        }
                    }
                });
            }
        });

        neoAmount.setText("(NEO "+getString(R.string.keyongshuliang)+"："+new BigDecimal(neoBean.getBalance()).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString()+")");
        gas=new BigDecimal(neoBean.getGnt().get(0).getBalance()).setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (gas==0) {
            submit.setText(R.string.dangqiangasyuebuzu);
            submit.setBackgroundColor(Color.parseColor("#DADADA"));
            submit.setClickable(false);
            gasNum.setVisibility(View.VISIBLE);
            gasNum.setText("(Gas "+getString(R.string.keyongshuliang)+"："+gas+")");
        }else {

        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_KEY) {
            KeyEvent key = (KeyEvent) event.getData();
            etAddress.setText(key.getKey().toLowerCase());
        }
    }
}
