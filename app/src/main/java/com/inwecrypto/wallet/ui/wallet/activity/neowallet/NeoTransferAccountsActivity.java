package com.inwecrypto.wallet.ui.wallet.activity.neowallet;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.bean.UtxoBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.MaterialDialog;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.event.KeyEvent;
import com.inwecrypto.wallet.ui.ScanActivity;
import com.inwecrypto.wallet.ui.me.activity.MailListActivity;
import com.inwecrypto.wallet.ui.newneo.NewNeoTransferConfirmActivity;
import com.lzy.okgo.model.Response;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class NeoTransferAccountsActivity extends BaseActivity {


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
    @BindView(R.id.gasfeell)
    LinearLayout gasfeell;
    @BindView(R.id.gas_num)
    TextView gasNum;

    private WalletBean wallet;
    private boolean isClod;
    private int type;
    private TokenBean.RecordBean neoBean;

    @Override
    protected void getBundleExtras(Bundle extras) {
        isClod = extras.getBoolean("isClod", false);
        wallet = (WalletBean) extras.getSerializable("wallet");
        type = extras.getInt("type");
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

        tvCurrentPrice.setText("(" + getString(R.string.dangqianyue) + "：" + (type == 0 ? new BigDecimal(neoBean.getBalance()).setScale(0, BigDecimal.ROUND_DOWN).toPlainString() : new BigDecimal(neoBean.getGnt().get(0).getBalance()).setScale(8, BigDecimal.ROUND_DOWN).toPlainString()) + ")");

        gasfeell.setVisibility(View.INVISIBLE);

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

                if (type == 0 && new BigDecimal(neoBean.getBalance()).doubleValue() == 0) {
                    ToastUtil.show(R.string.yuebuzu);
                    return;
                }

                if (type != 0 && new BigDecimal(neoBean.getGnt().get(0).getBalance()).doubleValue() == 0) {
                    ToastUtil.show(R.string.yuebuzu);
                    return;
                }

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

                if (type == 0 && new BigDecimal(etPrice.getText().toString().trim()).toPlainString().contains(".")) {
                    ToastUtil.show(getString(R.string.zuixiaodanweishi1));
                    return;
                }

                if (type == 0 && (new BigDecimal(neoBean.getBalance()).doubleValue() - new BigDecimal(etPrice.getText().toString().trim()).floatValue()) < 0) {
                    ToastUtil.show(getString(R.string.zhuanzhangtishi));
                    return;
                }

                if (type != 0 && (new BigDecimal(neoBean.getGnt().get(0).getBalance()).doubleValue() - new BigDecimal(etPrice.getText().toString().trim()).floatValue()) < 0) {
                    ToastUtil.show(getString(R.string.zhuanzhangtishi));
                    return;
                }

                showFixLoading();

                WalletApi.getUtxo(this, wallet.getAddress(), type == 0 ? "neo-asset-id" : "neo-gas-asset-id", new JsonCallback<LzyResponse<UtxoBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<UtxoBean>> response) {
                        if (null != response) {
                            if (!isClod){
                                if (null != response.body().data.getResult()) {

                                    String utxo = GsonUtils.objToJson(response.body().data.getResult());
                                    //跳转到确认页面
                                    Intent intent = new Intent(mActivity, NewNeoTransferConfirmActivity.class);
                                    intent.putExtra("unspent", utxo);
                                    intent.putExtra("wallet", wallet);
                                    intent.putExtra("type", type);
                                    intent.putExtra("price", new BigDecimal(etPrice.getText().toString().trim()).setScale(4,BigDecimal.ROUND_DOWN).toPlainString());
                                    intent.putExtra("hit", etHint.getText().toString().trim());
                                    if (type==0){
                                        intent.putExtra("unit", "(NEO)");
                                    }else {
                                        intent.putExtra("unit", "(Gas)");
                                    }
                                    intent.putExtra("to", etAddress.getText().toString().trim());
                                    intent.putExtra("handfee", getString(R.string.shouxufei)+"：0.0000");
                                    keepTogo(intent);
                                } else {
                                    ToastUtil.show(getString(R.string.huoquyueshibaiqingshaohouchognshi));
                                }
                            }
                        }
                        hideFixLoading();
                    }

                    @Override
                    public void onError(Response<LzyResponse<UtxoBean>> response) {
                        super.onError(response);
                        hideFixLoading();
                        ToastUtil.show(getString(R.string.huoquyueshibaiqingshaohouchognshi));
                    }
                });
            }
        });
    }

    @Override
    protected void initData() {

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
