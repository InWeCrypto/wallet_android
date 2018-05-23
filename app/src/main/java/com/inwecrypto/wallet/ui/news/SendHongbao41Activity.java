package com.inwecrypto.wallet.ui.news;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

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
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.HongbaoFeeBean;
import com.inwecrypto.wallet.bean.OSSBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.MeApi;
import com.inwecrypto.wallet.common.http.api.UserApi;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.imageloader.GlideCircleTransform;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.PhotoUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.lzy.okgo.model.Response;

import net.qiujuer.genius.ui.widget.Button;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 作者：xiaoji06 on 2018/4/23 12:29
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class SendHongbao41Activity extends BaseActivity implements EasyPermissions.PermissionCallbacks {


    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.et_info)
    EditText etInfo;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.delete)
    ImageView delete;
    @BindView(R.id.add_img)
    FrameLayout addImg;
    @BindView(R.id.next)
    Button next;

    private static final int RC_CAMERA = 222;

    private String id;
    private String redbagAddress;
    private String imgPath;
    private String gntName;

    private boolean isShare;

    @Override
    protected void getBundleExtras(Bundle extras) {
        id=extras.getString("id");
        redbagAddress=extras.getString("redbagAddress");
        isShare = extras.getBoolean("isShare");
        gntName=extras.getString("gntName");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.send_hongbao_41_activity;
    }

    @Override
    protected void initView() {

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addImg.setOnClickListener(new View.OnClickListener() {
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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add.setImageResource(R.mipmap.red_add);
                imgPath=null;
                delete.setVisibility(View.GONE);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().length()==0){
                    ToastUtil.show(R.string.qingtianxiefasongrenmincheng);
                    return;
                }

                if (etInfo.getText().toString().length()==0){
                    ToastUtil.show(R.string.qingtianxieliuyan);
                    return;
                }

                if (null==imgPath){
                    ToastUtil.show(getString(R.string.qingxuanzetupian));
                    return;
                }

                if (isShare){
                    Intent intent=new Intent(mActivity,HongbaoShareWebActivity.class);
                    String username="";
                    try {
                        username= URLEncoder.encode(name.getText().toString(),"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String urlch=(App.isMain? Url.MAIN_HONGBAO:Url.TEST_HONGBAO)+id+"/"+redbagAddress+"?share_user="+username+"&lang="+(App.get().isZh()?"zh":"en")+"&target=draw2&symbol="+gntName+"&inwe";
                    String urlen=(App.isMain? Url.MAIN_HONGBAO:Url.TEST_HONGBAO)+id+"/"+redbagAddress+"?share_user="+username+"&lang="+(App.get().isZh()?"zh":"en")+"&target=draw&symbol="+gntName+"&inwe";

                    intent.putExtra("url",urlch);
                    intent.putExtra("urlen",urlen);
                    intent.putExtra("name",name.getText().toString());
                    intent.putExtra("gntName",gntName);
                    intent.putExtra("content",etInfo.getText().toString());
                    finshTogo(intent);
                }else {
                    showFixLoading();
                    ZixunApi.sendRedbag(this
                            , id
                            , redbagAddress
                            , "1"
                            , imgPath
                            , name.getText().toString()
                            , etInfo.getText().toString()
                            , new JsonCallback<LzyResponse<HongbaoFeeBean>>() {
                                @Override
                                public void onSuccess(Response<LzyResponse<HongbaoFeeBean>> response) {
                                    isShare=true;
                                    EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_HONGBAO_SHARE,id));

                                    Intent intent=new Intent(mActivity,HongbaoShareWebActivity.class);
                                    String username="";
                                    try {
                                        username= URLEncoder.encode(name.getText().toString(),"utf-8");
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    String urlch=(App.isMain? Url.MAIN_HONGBAO:Url.TEST_HONGBAO)+id+"/"+redbagAddress+"?share_user="+username+"&lang="+(App.get().isZh()?"zh":"en")+"&target=draw2&symbol="+gntName+"&inwe";
                                    String urlen=(App.isMain? Url.MAIN_HONGBAO:Url.TEST_HONGBAO)+id+"/"+redbagAddress+"?share_user="+username+"&lang="+(App.get().isZh()?"zh":"en")+"&target=draw&symbol="+gntName+"&inwe";

                                    intent.putExtra("url",urlch);
                                    intent.putExtra("urlen",urlen);
                                    intent.putExtra("name",name.getText().toString());
                                    intent.putExtra("gntName",gntName);
                                    intent.putExtra("content",etInfo.getText().toString());
                                    finshTogo(intent);
                                }

                                @Override
                                public void onError(Response<LzyResponse<HongbaoFeeBean>> response) {
                                    super.onError(response);
                                    ToastUtil.show(getString(R.string.fenxiangshibaiqinchongshi));
                                }

                                @Override
                                public void onFinish() {
                                    super.onFinish();
                                    hideFixLoading();
                                }
                            });
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

    public Uri uritempFile;
    private String protraitPath = "";// 选择图片地址
    String path;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111 && resultCode == RESULT_OK) {
            String path = PhotoUtils.parsePicturePath(this, data.getData());
            uritempFile = PhotoUtils.startPhotoZ(this, new File(path), 2222);
        }
        if (requestCode == 2222 && resultCode == RESULT_OK) {
            showFixLoading();
            path = PhotoUtils.parsePicturePath(this, uritempFile);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        protraitPath = PhotoUtils.getThumbnail(mActivity, path);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 上传图片到云平台，并获得图片地址
                                upLoad();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        hideFixLoading();
                    }
                }
            }).start();
        }
    }

    private void upLoad() {
        MeApi.getSts(this, new JsonCallback<LzyResponse<OSSBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<OSSBean>> response) {
                String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
                OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(response.body().data.getCredentials().getAccessKeyId(), response.body().data.getCredentials().getAccessKeySecret(), response.body().data.getCredentials().getSecurityToken());
                ClientConfiguration conf = new ClientConfiguration();
                conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
                conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
                conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
                conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
                final OSS oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider, conf);
                final long currentTime = System.currentTimeMillis();
                // 构造上传请求
                PutObjectRequest put = new PutObjectRequest("inwecrypto-china", "android_header_" + currentTime, protraitPath);
                oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                        imgPath = oss.presignPublicObjectURL("inwecrypto-china", "android_header_" + currentTime);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                delete.setVisibility(View.VISIBLE);
                                Glide.with(mActivity)
                                        .load(imgPath)
                                        .crossFade()
                                        .error(R.mipmap.wode_touxiang)
                                        .into(add);
                                hideFixLoading();
                            }
                        });
                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                        add.post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show(getString(R.string.tupianshangchuanshibai));
                                hideFixLoading();
                            }
                        });
                    }
                });
            }

            @Override
            public void onError(Response<LzyResponse<OSSBean>> response) {
                super.onError(response);
                add.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.show(getString(R.string.tupianshangchuanshibai));
                        hideFixLoading();
                    }
                });
            }
        });
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
