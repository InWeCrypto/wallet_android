package capital.fbg.wallet.ui;/*
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
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.client.android.AutoScannerView;
import com.google.zxing.client.android.BaseCaptureActivity;


import org.greenrobot.eventbus.EventBus;

import java.util.List;

import capital.fbg.wallet.R;
import capital.fbg.wallet.common.Constant;
import capital.fbg.wallet.common.util.ToastUtil;
import capital.fbg.wallet.event.BaseEventBusBean;
import capital.fbg.wallet.event.KeyEvent;
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
public final class ScanActivity extends BaseCaptureActivity implements EasyPermissions.PermissionCallbacks{

    private static final String TAG = ScanActivity.class.getSimpleName();

    private SurfaceView surfaceView;
    private AutoScannerView autoScannerView;

    private static final int RC_CAMERA = 222;
    private String title="扫一扫";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_capture);

        Intent intent=getIntent();
        if (null!=intent){
            String titleE=intent.getStringExtra("title");
            if (null!=titleE&&titleE.length()>0){
                title=titleE;
            }
        }

        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        autoScannerView = (AutoScannerView) findViewById(R.id.autoscanner_view);
        findViewById(R.id.txt_left_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.txt_main_title)).setText(title);
        findViewById(R.id.txt_right_title).setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            initCamer();
            autoScannerView.setCameraManager(cameraManager);
        } else {
            EasyPermissions.requestPermissions(this, "为了您能够正常使用扫一扫功能，超级码需要获得相机权限",
                    RC_CAMERA, Manifest.permission.CAMERA);
        }
    }

    @Override
    public SurfaceView getSurfaceView() {
        return (surfaceView == null) ? (SurfaceView) findViewById(R.id.preview_view) : surfaceView;
    }

    @Override
    public void dealDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        Log.i(TAG, "dealDecode ~~~~~ " + rawResult.getText() + " " + barcode + " " + scaleFactor);
        playBeepSoundAndVibrate(true, false);
        if (rawResult == null) {
            ToastUtil.show(getString(R.string.no_qrcode));
            autoScannerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    reScan();
                }
            },1000);
            return;
        }
        EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_KEY,new KeyEvent(rawResult.getText().toString())));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Toast.makeText(this,"权限请求成功！",Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        EasyPermissions.somePermissionPermanentlyDenied(this,perms);
    }
}