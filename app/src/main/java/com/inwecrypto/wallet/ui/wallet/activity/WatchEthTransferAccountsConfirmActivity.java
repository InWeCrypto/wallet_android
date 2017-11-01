package com.inwecrypto.wallet.ui.wallet.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.lzy.okgo.model.Response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CountBean;
import com.inwecrypto.wallet.bean.TransferBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.QuanCodeUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.event.KeyEvent;
import com.inwecrypto.wallet.ui.ScanActivity;
import me.drakeet.materialdialog.MaterialDialog;
import me.grantland.widget.AutofitTextView;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class WatchEthTransferAccountsConfirmActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{
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
    private String address;
    private String price;
    private String gas;
    private String hit;
    private MaterialDialog mMaterialDialog;

    private String oxPrice;
    private String oxGas;
    private BigDecimal pEther = new BigDecimal("1000000000000000000");

    @Override
    protected void getBundleExtras(Bundle extras) {
        address = extras.getString("address");
        price = extras.getString("price");
        oxPrice = "0x" + new BigInteger(new BigDecimal(price).multiply(pEther).setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString(),10).toString(16);
        gas = extras.getString("gas");
        oxGas = "0x" + new BigInteger(new BigDecimal(gas).multiply(pEther).divide(new BigDecimal(Constant.GAS_LIMIT), 0,BigDecimal.ROUND_HALF_UP).toPlainString(),10).toString(16);
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
        txtMainTitle.setText(R.string.transfer_confirm);
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
        tvPrice.setText(new BigDecimal(price).setScale(4,BigDecimal.ROUND_HALF_UP).toPlainString());
        tvServiceCharge.setText(getString(R.string.transfer_hit1) + gas);
        etAddress.setText(address);
        etHit.setText(hit);
    }

    private String nonce;
    private String hash;
    private Bitmap bitmap;

    private void transferWatch() {

        WalletApi.transactionCount(mActivity,wallet.getAddress(), new JsonCallback<LzyResponse<CountBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CountBean>> response) {
                hideLoading();
                nonce=response.body().data.getCount();
                if (EasyPermissions.hasPermissions(mActivity, Manifest.permission.NFC)) {
                    goNext();
                } else {
                    EasyPermissions.requestPermissions(mActivity, "为了您能够创建钱包，需要获得NFC权限",
                            0, Manifest.permission.CAMERA);
                }
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
        intent.putExtra("isGnt",false);
        intent.putExtra("wallet",wallet);
        intent.putExtra("address",address);
        intent.putExtra("hint",hit);
        intent.putExtra("price",price);
        intent.putExtra("gas",gas);
        TransferBean codeJson=new TransferBean(wallet.getAddress()
                ,nonce
                ,oxGas
                ,address
                ,oxPrice
                ,price
                ,gas
                ,""
                ,hit
                ,1);
        intent.putExtra("code",codeJson);
        finshTogo(intent);
    }

    private void transfer() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_dialog_watch_code, null, false);
        final ImageView code= (ImageView) view.findViewById(R.id.code);
        TextView ok= (TextView) view.findViewById(R.id.ok);
        code.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) code.getLayoutParams();
                params.height=code.getMeasuredWidth();
                code.setLayoutParams(params);
            }
        });
        final TransferBean codeJson=new TransferBean(wallet.getAddress()
                ,nonce
                ,oxGas
                ,address
                ,oxPrice
                ,price
                ,gas
                ,""
                ,hit
                ,1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //文本类型
                try {
                    bitmap = QuanCodeUtils.createQuanCode(mActivity,new Gson().toJson(codeJson));
                    code.post(new Runnable() {
                        @Override
                        public void run() {
                            code.setImageBitmap(bitmap);
                        }
                    });
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keepTogo(ScanActivity.class);
            }
        });

        mMaterialDialog = new MaterialDialog(mActivity).setView(view);
        mMaterialDialog.setBackgroundResource(R.drawable.trans_bg);
        mMaterialDialog.setCanceledOnTouchOutside(true);
        mMaterialDialog.show();
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()== Constant.EVENT_KEY){
            KeyEvent key= (KeyEvent) event.getData();
                if (null==key.getKey()||key.getKey().length()==0||!key.getKey().contains("0x")){
                    ToastUtil.show("请扫描正确的二维码");
                    return;
                }
                mMaterialDialog.dismiss();
                AppManager.getAppManager().finishActivity(TransferAccountsActivity.class);
                Intent intent=new Intent(this,WatchTransferAccountsConfirmActivity.class);
                intent.putExtra("isGnt",false);
                intent.putExtra("wallet",wallet);
                intent.putExtra("data",key.getKey());
                intent.putExtra("address",address);
                intent.putExtra("hint",hit);
                intent.putExtra("price",price);
                intent.putExtra("gas",gas);
                finshTogo(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Toast.makeText(this,"权限请求成功！",Toast.LENGTH_SHORT).show();
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
