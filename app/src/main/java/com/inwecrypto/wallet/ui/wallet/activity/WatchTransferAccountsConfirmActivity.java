package com.inwecrypto.wallet.ui.wallet.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class WatchTransferAccountsConfirmActivity extends BaseActivity {
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_service_charge)
    TextView tvServiceCharge;
    @BindView(R.id.et_address)
    TextView etAddress;
    @BindView(R.id.et_hit)
    TextView etHit;
    @BindView(R.id.tv_transfer)
    TextView tvTransfer;
    @BindView(R.id.tv_unit)
    TextView tvUnit;

    private WalletBean wallet;
    private String data;
    private String address;
    private String hint;
    private String price;
    private String gas;
    private String hash;
    private boolean isGnt;
    private String payAddress;
    private String gnt;

    private BigDecimal pEther = new BigDecimal("1000000000000000000");

    @Override
    protected void getBundleExtras(Bundle extras) {
        isGnt = extras.getBoolean("isGnt", false);
        wallet = (WalletBean) extras.getSerializable("wallet");
        data = extras.getString("data");
        address = extras.getString("address");
        hint = extras.getString("hint");
        price = extras.getString("price");
        gas = extras.getString("gas");

        if (isGnt) {
            payAddress = extras.getString("payAddress");
            gnt = extras.getString("gnt");
        }
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_activity_clod_transfer_accounts_confirm;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.transfer_confirm);
        txtRightTitle.setVisibility(View.GONE);
        tvTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFixLoading();
                if (isGnt) {
                    WalletApi.walletOrder(mActivity, wallet.getId(), data, wallet.getAddress(), address, hint, new BigDecimal(price.toString()).multiply(pEther).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString(), new BigDecimal(gas).multiply(pEther).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString(), gnt, new JsonCallback<LzyResponse<Object>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<Object>> response) {
                            hideFixLoading();
                            ToastUtil.show("转账成功");
                            AppManager.getAppManager().finishActivity(WatchTokenTransferAccountsActivity.class);
                            AppManager.getAppManager().finishActivity(TransferAccountsActivity.class);
                            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
                            finish();
                        }

                        @Override
                        public void onError(Response<LzyResponse<Object>> response) {
                            super.onError(response);
                            hideFixLoading();
                            if (response.getException().getMessage().contains("wallet_error")) {
                                ToastUtil.show("服务器内部错误");
                                AppManager.getAppManager().finishActivity(WatchTokenTransferAccountsActivity.class);
                                AppManager.getAppManager().finishActivity(TransferAccountsActivity.class);
                                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
                                finish();
                            } else {
                                ToastUtil.show(getString(R.string.load_error));
                            }
                        }
                    });
                } else {
                    WalletApi.walletOrder(mActivity, wallet.getId(), data, wallet.getAddress(), address, hint, new BigDecimal(price.toString()).multiply(pEther).toPlainString(), new BigDecimal(gas).multiply(pEther).toPlainString(), wallet.getCategory().getName(), new JsonCallback<LzyResponse<Object>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<Object>> response) {
                            hideFixLoading();
                            ToastUtil.show("转账成功");
                            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
                            finish();
                        }

                        @Override
                        public void onError(Response<LzyResponse<Object>> response) {
                            super.onError(response);
                            hideFixLoading();
                            if (response.getException().getMessage().contains("wallet_error")) {
                                ToastUtil.show("服务器内部错误");
                                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
                                finish();
                            } else {
                                ToastUtil.show(getString(R.string.load_error));
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void initData() {
        tvPrice.setText(new BigDecimal(price).setScale(4,BigDecimal.ROUND_HALF_UP).toPlainString());
        tvServiceCharge.setText(getString(R.string.transfer_hit1) + gas);
        etAddress.setText(address);
        etHit.setText(hint);
        if (isGnt) {
            tvUnit.setText("转账金额("+gnt.toLowerCase()+")");
        }else {
            tvUnit.setText("转账金额(ether)");
        }
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
