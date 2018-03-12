package com.inwecrypto.wallet.ui.wallet.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.inwecrypto.wallet.common.Constant;
import com.lzy.okgo.model.Response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CountBean;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.bean.TransferABIBean;
import com.inwecrypto.wallet.bean.TransferBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import me.grantland.widget.AutofitTextView;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class WatchTokenTransferAccountsConfirmActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

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
    AutofitTextView etAddress;
    @BindView(R.id.et_hit)
    TextView etHit;
    @BindView(R.id.tv_transfer)
    TextView tvTransfer;

    private WalletBean wallet;
    private TokenBean.ListBean gnt;
    private String address;
    private String price;
    private String gas;
    private String hit;
    private String data;
    private String nonce;

    private String oxPrice;
    private String oxGas;

    @Override
    protected void getBundleExtras(Bundle extras) {
        gnt= (TokenBean.ListBean) extras.getSerializable("gnt");
        address = extras.getString("address");
        price = extras.getString("price");
        oxPrice = "0x" + new BigInteger(new BigDecimal(price).multiply(Constant.pEther).setScale(0,BigDecimal.ROUND_DOWN).toPlainString(),10).toString(16);
        gas = extras.getString("gas");
        oxGas = "0x" + new BigInteger(new BigDecimal(gas).multiply(Constant.pEther).divide(new BigDecimal(gnt.getGnt_category().getGas()), 0,BigDecimal.ROUND_DOWN).toPlainString(),10).toString(16);
        hit = extras.getString("hit");
        wallet = (WalletBean) extras.getSerializable("wallet");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_activity_transfer_accounts_confirm;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.zhuanzhuangqueren);
        txtRightTitle.setVisibility(View.GONE);
        tvTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                transferWatch();
            }
        });
    }

    @Override
    protected void initData() {
        tvPrice.setText(new BigDecimal(price).setScale(4,BigDecimal.ROUND_DOWN).toPlainString());
        tvServiceCharge.setText(getString(R.string.lingfushouxufei) + gas);
        etAddress.setText(address);
        etHit.setText(hit);
    }

    private void transferWatch() {

        WalletApi.transactionCount(mActivity,wallet.getAddress(), new JsonCallback<LzyResponse<CountBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CountBean>> response) {
                nonce=response.body().data.getCount();
                WalletApi.transferABI(mActivity, gnt.getGnt_category().getAddress(), address, oxPrice, new JsonCallback<LzyResponse<TransferABIBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<TransferABIBean>> response) {
                        hideLoading();
                        data=response.body().data.getData();
                        if (EasyPermissions.hasPermissions(mActivity, Manifest.permission.NFC)) {
                            goNext();
                        } else {
                            EasyPermissions.requestPermissions(mActivity, getString(R.string.nfc_hit),
                                    0, Manifest.permission.NFC);
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<TransferABIBean>> response) {
                        super.onError(response);
                        hideLoading();
                        ToastUtil.show(getString(R.string.zhuanzhang_error));
                    }
                });
            }

            @Override
            public void onError(Response<LzyResponse<CountBean>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
                hideLoading();
            }
        });
    }

    private void goNext() {
        Intent intent=new Intent(mActivity,WarchEthTransferAccountsNfcActivity.class);
        intent.putExtra("isGnt",true);
        intent.putExtra("wallet",wallet);
        intent.putExtra("asset_id",gnt.getGnt_category().getAddress().toLowerCase());
        intent.putExtra("address",address);
        intent.putExtra("hint",hit);
        intent.putExtra("price",price);
        intent.putExtra("gas",gas);
        intent.putExtra("gnt",gnt.getName());
        TransferBean codeJson=new TransferBean(wallet.getAddress()
                ,nonce
                ,oxGas
                ,gnt.getGnt_category().getAddress()
                ,data
                ,price
                ,gas
                ,"0x"+new BigInteger(gnt.getGnt_category().getGas(),10).toString(16)
                ,hit
                ,2);
        intent.putExtra("code",codeJson);
        finshTogo(intent);
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        ToastUtil.show(R.string.quanxianqingqiuchenggong);
        goNext();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
