package com.inwecrypto.wallet.ui.wallet.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import com.xw.repo.BubbleSeekBar;


import java.math.BigDecimal;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.GasBean;
import com.inwecrypto.wallet.bean.ValueBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.ui.ScanActivity;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.event.KeyEvent;
import com.inwecrypto.wallet.ui.me.activity.MailListActivity;
/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class TransferAccountsActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.iv_mail)
    ImageView ivMail;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.et_hint)
    EditText etHint;
    @BindView(R.id.tv_ok)
    TextView tvOk;
    @BindView(R.id.gas)
    TextView gas;
    @BindView(R.id.gasBar)
    BubbleSeekBar gasBar;
    @BindView(R.id.tv_currentPrice)
    TextView tvCurrentPrice;

    private WalletBean wallet;
    private boolean isClod;

    private BigDecimal distance= Constant.high.subtract(Constant.low);
    private float position;
    private String price;

    @Override
    protected void getBundleExtras(Bundle extras) {
        isClod=extras.getBoolean("isClod",false);
        wallet= (WalletBean) extras.getSerializable("wallet");
        price=extras.getString("price");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_activity_transfer_accounts;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.zhuanzhang);
        Drawable drawableInfo= getResources().getDrawable(R.mipmap.nav_scan);
        /// 这一步必须要做,否则不会显示.
        drawableInfo.setBounds(0, 0, drawableInfo.getMinimumWidth(), drawableInfo.getMinimumHeight());
        txtRightTitle.setCompoundDrawables(drawableInfo,null,null,null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keepTogo(ScanActivity.class);
            }
        });

        tvCurrentPrice.setText("("+getString(R.string.dangqianyue)+"："+price+")");

        ivMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity, MailListActivity.class);
                intent.putExtra("address",true);
                keepTogo(intent);
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppUtil.isAddress(etAddress.getText().toString())){
                    ToastUtil.show(R.string.qingtianxiezhuanzhangqianbaodizhi);
                    return;
                }

                if (etPrice.getText().toString().length()==0){
                    ToastUtil.show(R.string.qingtianxiezhuanzhangjine);
                    return;
                }
                try{
                    new BigDecimal(etPrice.getText().toString());
                }catch (Exception e){
                    ToastUtil.show(R.string.qingtianxiezhengquedejine);
                    return;
                }
                showLoading();
                //获取账户余额
                WalletApi.balance(mActivity,wallet.getAddress(), new JsonCallback<LzyResponse<ValueBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<ValueBean>> response) {
                        BigDecimal currentPrice = new BigDecimal(AppUtil.toD(response.body().data.getValue().replace("0x", "0")));
                        BigDecimal price = new BigDecimal(etPrice.getText().toString()).add(new BigDecimal(gas.getText().toString())).multiply(Constant.pEther);
                            hideLoading();
                        if (currentPrice.subtract(price).doubleValue()>=0){
                            if (!isClod){
                                Intent intent=new Intent(mActivity,TransferAccountsConfirmActivity.class);
                                intent.putExtra("address",etAddress.getText().toString());
                                intent.putExtra("price",etPrice.getText().toString());
                                intent.putExtra("gas",gas.getText().toString());
                                intent.putExtra("hit",etHint.getText().toString());
                                intent.putExtra("wallet",wallet);
                                keepTogo(intent);
                            }
                        }else {
                            ToastUtil.show(R.string.qianbaoyuebuzu);
                            return;
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<ValueBean>> response) {
                        super.onError(response);
                        ToastUtil.show(R.string.yuehuoqushibai);
                        hideLoading();
                    }
                });
            }
        });
        gas.setText(Constant.low.divide(Constant.pEther).setScale(8,BigDecimal.ROUND_DOWN).toPlainString());
    }

    @Override
    protected void initData() {
        //获取gas
        WalletApi.gas(mActivity, new JsonCallback<LzyResponse<GasBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<GasBean>> response) {
                BigDecimal parseLong;
                try{
                    parseLong=new BigDecimal(AppUtil.toD(response.body().data.getGasPrice().replace("0x", "0")));
                }catch (Exception e){
                    gas.setText(Constant.low.divide(Constant.pEther).setScale(8,BigDecimal.ROUND_DOWN).toPlainString());
                    return;
                }
                BigDecimal currentGas = parseLong.multiply(new BigDecimal(Constant.GAS_LIMIT));
                if (currentGas.subtract(Constant.low).longValue()<=0){
                    position=0;
                    gasBar.setProgress(position);
                    gas.setText(Constant.low.divide(Constant.pEther).setScale(8,BigDecimal.ROUND_DOWN).toPlainString());
                }else if (currentGas.subtract(Constant.high).longValue()>=0){
                    position=100;
                    gasBar.setProgress(position);
                    gas.setText(Constant.high.divide(Constant.pEther).setScale(8,BigDecimal.ROUND_DOWN).toPlainString());
                }else {
                    position=currentGas.subtract(Constant.low).divide(distance,2,BigDecimal.ROUND_HALF_DOWN).multiply(Constant.p100).setScale(0,BigDecimal.ROUND_HALF_DOWN).floatValue();
                    gasBar.setProgress(position);
                    gas.setText(new BigDecimal(position).divide(Constant.p100).multiply(distance).add(Constant.low).divide(Constant.pEther).setScale(8,BigDecimal.ROUND_HALF_DOWN).toPlainString());
                }
            }

            @Override
            public void onError(Response<LzyResponse<GasBean>> response) {
                super.onError(response);
                gas.setText(Constant.low.divide(Constant.pEther).setScale(8,BigDecimal.ROUND_DOWN).toPlainString());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                gasBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
                    @Override
                    public void onProgressChanged(int progress, float progressFloat) {
                        gas.setText(new BigDecimal(progressFloat).divide(Constant.p100).multiply(distance).add(Constant.low).divide(Constant.pEther).setScale(8,BigDecimal.ROUND_HALF_DOWN).toPlainString());
                    }

                    @Override
                    public void getProgressOnActionUp(int progress, float progressFloat) {
                    }

                    @Override
                    public void getProgressOnFinally(int progress, float progressFloat) {

                    }
                });
            }
        });

        //获取账户余额
        WalletApi.balance(mActivity,wallet.getAddress(), new JsonCallback<LzyResponse<ValueBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ValueBean>> response) {
                BigDecimal currentPrice = new BigDecimal(AppUtil.toD(response.body().data.getValue().replace("0x", "0")));
                tvCurrentPrice.setText("("+getString(R.string.dangqianyue)+"："+currentPrice.divide(Constant.pEther,4,BigDecimal.ROUND_DOWN).toString()+")");
            }

            @Override
            public void onError(Response<LzyResponse<ValueBean>> response) {
                super.onError(response);
                ToastUtil.show(R.string.yuehuoqushibai);
                hideLoading();
            }
        });
    }


    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()== Constant.EVENT_KEY){
            KeyEvent key= (KeyEvent) event.getData();
            if (key.getKey().startsWith("0x")&&key.getKey().length()==42){
                etAddress.setText(key.getKey());
            }else {
                ToastUtil.show(R.string.qingshuruzhengquedizhi);
            }
        }
    }

}
