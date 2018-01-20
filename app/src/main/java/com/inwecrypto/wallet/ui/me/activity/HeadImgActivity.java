package com.inwecrypto.wallet.ui.me.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.inwecrypto.wallet.App;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.LoginBean;
import com.inwecrypto.wallet.bean.OSSBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.MeApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.PhotoUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/8/9.
 * 功能描述：
 * 版本：@version
 */

public class HeadImgActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.photo_view)
    PhotoView photoView;

    private LoginBean user;
    private static final int RC_CAMERA = 222;

    @Override
    protected void getBundleExtras(Bundle extras) {
        user= (LoginBean) extras.getSerializable("user");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.me_activity_headimg;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.gerentouxiang);
        txtRightTitle.setText(R.string.xiangce);
        txtRightTitle.setCompoundDrawables(null,null,null,null);

        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyPermissions.hasPermissions(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    ComponentName componentName = intent.resolveActivity(getPackageManager());
                    if (componentName != null) {
                        startActivityForResult(intent, 1111);
                    }
                } else {
                    EasyPermissions.requestPermissions(mActivity, getString(R.string.xiangcequanxiantishi),
                            RC_CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
        });
    }

    @Override
    protected void initData() {

        if (null != user.getUser().getImg() && user.getUser().getImg().length() > 0) {
            Glide.with(this)
                    .load(user.getUser().getImg())
                    .crossFade()
                    .placeholder(R.mipmap.touxiang)
                    .into(photoView);
        }else {
            Glide.with(this)
                    .load(R.mipmap.touxiang)
                    .crossFade()
                    .into(photoView);
        }
    }

    public Uri uritempFile;
    private String protraitPath = "";// 选择图片地址
    String path;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1111 && resultCode==RESULT_OK){
            String path = PhotoUtils.parsePicturePath(this, data.getData());
            uritempFile = PhotoUtils.startPhotoZoom(this, new File(path), 2222);
        }
        if (requestCode==2222 && resultCode==RESULT_OK){
            showLoading();
            path=PhotoUtils.parsePicturePath(this, uritempFile);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        protraitPath=PhotoUtils.getThumbnail(mActivity,path);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 上传图片到云平台，并获得图片地址
                                upLoad();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        hideLoading();
                    }
                }
            }).start();
        }
    }

    private void upLoad() {
        MeApi.getSts(this, new JsonCallback<LzyResponse<OSSBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<OSSBean>> response) {
                String endpoint = "http://oss-cn-hongkong.aliyuncs.com";
                OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider( response.body().data.getCredentials().getAccessKeyId(),response.body().data.getCredentials().getAccessKeySecret(),response.body().data.getCredentials().getSecurityToken());
                ClientConfiguration conf = new ClientConfiguration();
                conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
                conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
                conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
                conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
                final OSS oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider,conf);
                final long currentTime=System.currentTimeMillis();
                // 构造上传请求
                PutObjectRequest put = new PutObjectRequest("whalewallet", "android_header_"+currentTime,protraitPath);
                oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                        String imgPath= null;
                        imgPath=oss.presignPublicObjectURL("whalewallet", "android_header_"+currentTime);
                        final String finalImgPath = imgPath;
                        MeApi.setUserInfo(mActivity,user.getUser().getNickname()==null?"":user.getUser().getNickname(),imgPath,user.getUser().getSex(), new JsonCallback<LzyResponse<Object>>() {
                            @Override
                            public void onSuccess(Response<LzyResponse<Object>> response) {
                                photoView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideLoading();
                                        user.getUser().setImg(finalImgPath);
                                        App.get().getLoginBean().getUser().setImg(finalImgPath);
                                        App.get().getSp().putString(Constant.USER_INFO, GsonUtils.objToJson(user));
                                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_USERINFO));
                                        ToastUtil.show(getString(R.string.xiugaichenggong));
                                        finish();
                                    }
                                });
                            }

                            @Override
                            public void onError(Response<LzyResponse<Object>> response) {
                                super.onError(response);
                                photoView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideLoading();
                                    }
                                });
                                ToastUtil.show(getString(R.string.xiugaishibai));
                            }
                        });
                    }
                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                        photoView.post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show(getString(R.string.tupianshangchuanshibai));
                                hideLoading();
                            }
                        });
                    }
                });
            }

            @Override
            public void onError(Response<LzyResponse<OSSBean>> response) {
                super.onError(response);
                photoView.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.show(getString(R.string.tupianshangchuanshibai));
                        hideLoading();
                    }
                });
            }
        });
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
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        ComponentName componentName = intent.resolveActivity(getPackageManager());
        if (componentName != null) {
            startActivityForResult(intent, 1111);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
