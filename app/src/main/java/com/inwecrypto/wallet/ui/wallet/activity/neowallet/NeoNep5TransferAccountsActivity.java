package com.inwecrypto.wallet.ui.wallet.activity.neowallet;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.IcoGas;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.bean.UtxoBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.MaterialDialog;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.event.KeyEvent;
import com.inwecrypto.wallet.ui.ScanActivity;
import com.inwecrypto.wallet.ui.me.activity.MailListActivity;
import com.inwecrypto.wallet.ui.newneo.NewNeoNep5TransferConfirmActivity;
import com.inwecrypto.wallet.ui.wallet.activity.TransferAccountsActivity;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import neomobile.Neomobile;
import neomobile.Tx;
import neomobile.Wallet;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class NeoNep5TransferAccountsActivity extends BaseActivity {


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
    @BindView(R.id.tv_currentPrice)
    TextView tvCurrentPrice;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.et_hint)
    EditText etHint;
    @BindView(R.id.tv_ok)
    TextView tvOk;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.fei)
    TextView fei;
    @BindView(R.id.tip)
    TextView tip;
    @BindView(R.id.gas_num)
    TextView gasNum;

    private WalletBean wallet;
    private boolean isClod;
    private TokenBean.ListBean tokenBean;
    private TokenBean.RecordBean neoBean;

    private MaterialDialog mMaterialDialog;
    private double gas;

    @Override
    protected void getBundleExtras(Bundle extras) {
        isClod = extras.getBoolean("isClod", false);
        wallet = (WalletBean) extras.getSerializable("wallet");
        tokenBean = (TokenBean.ListBean) extras.getSerializable("token");
        neoBean = (TokenBean.RecordBean) extras.getSerializable("neo");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_activity_neo_transfer_accounts;
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

        BigInteger price = new BigInteger(AppUtil.reverseArray(tokenBean.getBalance()));
        tvCurrentPrice.setText("(" + tokenBean.getName() + getString(R.string.keyongshuliang)+"：" + new BigDecimal(price).divide(new BigDecimal(10).pow(Integer.parseInt(tokenBean.getDecimals()))).setScale(8, BigDecimal.ROUND_DOWN).toPlainString() + ")");
        gas = new BigDecimal(neoBean.getGnt().get(0).getBalance()).setScale(8, BigDecimal.ROUND_DOWN).doubleValue();
        if (gas == 0) {
            //tvOk.setText(R.string.gasyuebuzu);
            //tvOk.setBackgroundColor(Color.parseColor("#DADADA"));
            //tvOk.setClickable(false);
            gasNum.setVisibility(View.VISIBLE);
            gasNum.setText("(Gas "+getString(R.string.keyongshuliang)+"：" + gas + ")");
        }

        ivMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MailListActivity.class);
                intent.putExtra("address", true);
                intent.putExtra("select", 1);
                keepTogo(intent);
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (type==0&&new BigDecimal(neoBean.getBalance()).doubleValue()==0){
//                    ToastUtil.show(R.string.yuebuzu);
//                    return;
//                }
//                if (gas == 0) {
//                    ToastUtil.show(R.string.gasyuebuzu);
//                    return;
//                }

                if (!AppUtil.isAddress(etAddress.getText().toString().trim())) {
                    ToastUtil.show(R.string.qingtianxiezhuanzhangqianbaodizhi);
                    return;
                }

                if (etPrice.getText().toString().trim().length() == 0) {
                    ToastUtil.show(R.string.qingtianxiezhuanzhangjine);
                    return;
                }
                try {
                    new BigDecimal(etPrice.getText().toString().trim());
                } catch (Exception e) {
                    ToastUtil.show(R.string.qingtianxiezhengquedejine);
                    return;
                }
                BigInteger price = new BigInteger(AppUtil.reverseArray(tokenBean.getBalance()));
                if (new BigDecimal(price).divide(new BigDecimal(10).pow(Integer.parseInt(tokenBean.getDecimals()))).subtract(new BigDecimal(etPrice.getText().toString().trim())).floatValue()<0){
                    ToastUtil.show(R.string.zhuanzhangtishi);
                    return;
                }

//                if (new BigDecimal(etPrice.getText().toString().trim()).toPlainString().contains(".")){
//                    ToastUtil.show(getString(R.string.zuixiaodanweishi1));
//                    return;
//                }


                showFixLoading();

                try {
                    WalletApi.getNep5Gas(this
                            , tokenBean.getGnt_category().getAddress()
                            , Neomobile.decodeAddress(wallet.getAddress())
                            , Neomobile.decodeAddress(wallet.getAddress())
                            , "1", new JsonCallback<LzyResponse<IcoGas>>() {
                                @Override
                                public void onSuccess(Response<LzyResponse<IcoGas>> response) {
                                    if (null != response && null != response.body().data.getGas_consumed()) {
                                        if (new BigDecimal(response.body().data.getGas_consumed()).doubleValue() < 10) {
                                            Intent intent = new Intent(mActivity, NewNeoNep5TransferConfirmActivity.class);
                                            intent.putExtra("wallet", wallet);
                                            intent.putExtra("token", tokenBean);
                                            intent.putExtra("to", etAddress.getText().toString().trim());
                                            intent.putExtra("price", new BigDecimal(etPrice.getText().toString().trim()).setScale(new BigDecimal(tokenBean.getDecimals()).intValue(), BigDecimal.ROUND_DOWN).toPlainString());
                                            intent.putExtra("handfee", "0.0000");
                                            intent.putExtra("hit", etHint.getText().toString().trim());
                                            intent.putExtra("unit", tokenBean.getName());
                                            intent.putExtra("yuangas", response.body().data.getGas_consumed());
                                            keepTogo(intent);
                                        } else {
                                            if (new BigDecimal(response.body().data.getGas_consumed()).doubleValue() <= gas) {
                                                Intent intent = new Intent(mActivity, NewNeoNep5TransferConfirmActivity.class);
                                                intent.putExtra("wallet", wallet);
                                                intent.putExtra("token", tokenBean);
                                                intent.putExtra("to", etAddress.getText().toString().trim());
                                                intent.putExtra("price", new BigDecimal(etPrice.getText().toString().trim()).setScale(new BigDecimal(tokenBean.getDecimals()).intValue(), BigDecimal.ROUND_DOWN).toPlainString());
                                                intent.putExtra("handfee", response.body().data.getGas_consumed());
                                                intent.putExtra("hit", etHint.getText().toString().trim());
                                                intent.putExtra("unit", tokenBean.getName());
                                                intent.putExtra("yuangas", response.body().data.getGas_consumed());
                                                keepTogo(intent);
                                            } else {
                                                ToastUtil.show(getString(R.string.shouxufeibuzu));
                                            }
                                        }
                                    } else {
                                        ToastUtil.show(getString(R.string.shouxufeihuoqushibai));
                                    }
                                }

                                @Override
                                public void onError(Response<LzyResponse<IcoGas>> response) {
                                    super.onError(response);
                                    ToastUtil.show(getString(R.string.shouxufeihuoqushibai));
                                }

                                @Override
                                public void onFinish() {
                                    super.onFinish();
                                    hideFixLoading();
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                    hideFixLoading();
                    ToastUtil.show(getString(R.string.shouxufeihuoqushibai));
                }

            }
        });
    }

    @Override
    protected void initData() {
//        try {
//            WalletApi.getNep5Gas(this
//                    , tokenBean.getGnt_category().getAddress()
//                    , Neomobile.decodeAddress(wallet.getAddress())
//                    , Neomobile.decodeAddress(wallet.getAddress())
//                    , "1", new JsonCallback<LzyResponse<IcoGas>>() {
//                        @Override
//                        public void onSuccess(Response<LzyResponse<IcoGas>> response) {
//                            if (null!=response&&null!=response.body().data&&null!=response.body().data.getGas_consumed()){
//                                fei.setText(getString(R.string.jiaoyishouxufei)+response.body().data.getGas_consumed());
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_KEY) {
            KeyEvent key = (KeyEvent) event.getData();
            if (AppUtil.isAddress(key.getKey())) {
                etAddress.setText(key.getKey());
            } else {
                ToastUtil.show(R.string.qingshuruzhengquedizhi);
            }
        }
    }

}
