package com.inwecrypto.wallet.ui;/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.event.KeyEvent;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class ScanActivity extends BaseActivity implements ZBarScannerView.ResultHandler,EasyPermissions.PermissionCallbacks{

    private ZBarScannerView mScannerView;

    private static final int RC_CAMERA = 222;

    private String title;


    @Override
    protected void getBundleExtras(Bundle extras) {
        Intent intent=getIntent();
        if (null!=intent){
            String titleE=intent.getStringExtra("title");
            if (null!=titleE&&titleE.length()>0){
                title=titleE;
            }
        }
    }

    @Override
    protected int setLayoutID() {
        return R.layout.activity_wechat_capture;
    }

    @Override
    protected void initView() {
        if (null==title){
            title=getString(R.string.saoyisao);
        }
        findViewById(R.id.txt_left_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.txt_main_title)).setText(title);
        findViewById(R.id.txt_right_title).setVisibility(View.GONE);

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZBarScannerView(this);
        contentFrame.addView(mScannerView);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
            mScannerView.startCamera();          // Start camera on resume
            mScannerView.setAutoFocus(true);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.xiangjiquanxiantishi),
                    RC_CAMERA, Manifest.permission.CAMERA);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(me.dm7.barcodescanner.zbar.Result rawResult) {
        //playBeepSoundAndVibrate(true, false);
        if (rawResult == null) {
            ToastUtil.show(R.string.weifaxianerweima);
            mScannerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // If you would like to resume scanning, call this method below:
                    mScannerView.resumeCameraPreview(ScanActivity.this);
                }
            },1000);
            return;
        }
        EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_KEY,new KeyEvent(rawResult.getContents().toString())));
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        ToastUtil.show(R.string.quanxianqingqiuchenggong);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
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