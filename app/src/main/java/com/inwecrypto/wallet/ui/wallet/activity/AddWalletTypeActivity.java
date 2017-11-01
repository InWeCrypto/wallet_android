package com.inwecrypto.wallet.ui.wallet.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    @Override
    protected void getBundleExtras(Bundle extras) {
        type_id = extras.getInt("type_id");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_acivity_add_wallet_type;
    }

    @Override
    protected void initView() {
        txtMainTitle.setText(R.string.add_wallet_title);
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
                Intent intent = new Intent(mActivity, AddWalletSettingActivity.class);
                intent.putExtra("type_id", type_id);
                keepTogo(intent);
            }
        });

        clod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * 使用hasSystemFeature方法可以检查设备是否其他功能。如陀螺仪，NFC，蓝牙等等，
                 */
                PackageManager pm = getPackageManager();
                boolean nfc = pm.hasSystemFeature(PackageManager.FEATURE_NFC);
                if (nfc){
                    if (EasyPermissions.hasPermissions(mActivity, Manifest.permission.NFC)) {
                        Intent intent = new Intent(mActivity, AddWalletClodSettingActivity.class);
                        intent.putExtra("type_id", type_id);
                        keepTogo(intent);
                    } else {
                        EasyPermissions.requestPermissions(mActivity, "为了您能够创建钱包，需要获得NFC权限",
                                0, Manifest.permission.CAMERA);
                    }
                }else {
                    ToastUtil.show("您的手机不支持nfc!请使用热钱包创建！");
                }
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
        Toast.makeText(this,"权限请求成功！",Toast.LENGTH_SHORT).show();
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
