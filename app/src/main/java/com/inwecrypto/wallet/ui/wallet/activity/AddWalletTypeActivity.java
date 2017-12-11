package com.inwecrypto.wallet.ui.wallet.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.wallet.activity.neowallet.AddNeoWalletClodSettingActivity;
import com.inwecrypto.wallet.ui.wallet.activity.neowallet.AddNeoWalletSettingActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by donghaijun on 2017/10/31.
 */

public class AddWalletTypeActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.hot)
    LinearLayout hot;
    @BindView(R.id.clod)
    LinearLayout clod;
    private int type_id;
    private ArrayList<WalletBean> wallets;

    @Override
    protected void getBundleExtras(Bundle extras) {
        type_id = extras.getInt("type_id");
        wallets= (ArrayList<WalletBean>) extras.getSerializable("wallets");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_acivity_add_wallet_type;
    }

    @Override
    protected void initView() {
        txtMainTitle.setText(R.string.tianjiaqianbao);
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtRightTitle.setVisibility(View.GONE);

        hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (type_id==1){
                    intent = new Intent(mActivity, AddWalletSettingActivity.class);
                }else if (type_id==2){
                    intent = new Intent(mActivity, AddNeoWalletSettingActivity.class);
                }
                intent.putExtra("type_id", type_id);
                intent.putExtra("wallets",wallets);
                keepTogo(intent);
            }
        });

        clod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToastUtil.show("暂时不支持生成冷钱包!");
//                /*
//                 * 使用hasSystemFeature方法可以检查设备是否其他功能。如陀螺仪，NFC，蓝牙等等，
//                 */
//                PackageManager pm = getPackageManager();
//                boolean nfc = pm.hasSystemFeature(PackageManager.FEATURE_NFC);
//                if (nfc){
//                    if (EasyPermissions.hasPermissions(mActivity, Manifest.permission.NFC)) {
//                        Intent intent = null;
//                        if (type_id==1){
//                            intent = new Intent(mActivity, AddWalletClodSettingActivity.class);
//                        }else if (type_id==2){
//                            intent = new Intent(mActivity, AddNeoWalletClodSettingActivity.class);
//                        }
//                        intent.putExtra("wallets",wallets);
//                        intent.putExtra("type_id", type_id);
//                        keepTogo(intent);
//                    } else {
//                        EasyPermissions.requestPermissions(mActivity, getString(R.string.nfc_hit),
//                                0, Manifest.permission.NFC);
//                    }
//                }else {
//                    ToastUtil.show(getString(R.string.no_nfc_hit));
//                }
            }
        });
    }

    @Override
    protected void initData() {

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
        Intent intent = new Intent(mActivity, AddWalletClodSettingActivity.class);
        intent.putExtra("type_id", type_id);
        keepTogo(intent);
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
