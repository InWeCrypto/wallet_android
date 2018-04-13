package com.inwecrypto.wallet.ui.me.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.hyphenate.chat.EMClient;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.LoginBean;
import com.inwecrypto.wallet.bean.OSSBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.MeApi;
import com.inwecrypto.wallet.common.http.api.UserApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.imageloader.GlideCircleTransform;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.PhotoUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.service.MessageService;
import com.inwecrypto.wallet.ui.MainTabActivity;
import com.inwecrypto.wallet.ui.login.LoginActivity;
import com.lzy.okgo.model.Response;
import com.suke.widget.SwitchButton;

import net.qiujuer.genius.ui.widget.Button;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.inwecrypto.wallet.common.Constant.EVENT_USERINFO;

/**
 * 作者：xiaoji06 on 2018/2/6 18:25
 * github：https://github.com/xiaoji06
 * 功能：
 */
public class UserActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.b1)
    ImageView b1;
    @BindView(R.id.imgll)
    RelativeLayout imgll;
    @BindView(R.id.nicheng)
    TextView nicheng;
    @BindView(R.id.b2)
    ImageView b2;
    @BindView(R.id.nichengll)
    RelativeLayout nichengll;
    @BindView(R.id.zhanghao)
    TextView zhanghao;
    @BindView(R.id.hit)
    TextView hit;
    @BindView(R.id.passll)
    RelativeLayout passll;
    @BindView(R.id.touchId)
    SwitchButton touchId;
    @BindView(R.id.logout)
    Button logout;

    private static final int RC_CAMERA = 222;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.user_activity;
    }

    @Override
    protected void initView() {

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtMainTitle.setText(R.string.gerenshezhi);
        txtRightTitle.setVisibility(View.GONE);

        imgll.setOnClickListener(new View.OnClickListener() {
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

        nichengll.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, FixNameActivity.class);
                keepTogo(intent);
            }
        });

        passll.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, FixPassActivity.class);
                keepTogo(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFixLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EMClient.getInstance().logout(true);
                        App.get().getSp().putString(Constant.TOKEN,"");
                        App.get().getSp().putString(Constant.TEST_TOKEN,"");
                        App.get().setLoginBean(null);
                        App.get().getSp().putString(Constant.USER_INFO,"{}");
                        App.get().setLogin(false);
                        AppManager.getAppManager().finishAllActivity();
                        stopService(new Intent(mActivity, MessageService.class));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent=new Intent(mActivity, MainTabActivity.class);
                                finshTogo(intent);
                                hideFixLoading();
                            }
                        });
                    }
                }).start();
            }
        });
    }

    @Override
    protected void initData() {
        LoginBean loginBean = App.get().getLoginBean();
        if (null != loginBean) {
            if (null != loginBean.getImg() && loginBean.getImg().length() > 0) {
                Glide.with(this)
                        .load(loginBean.getImg())
                        .crossFade()
                        .error(R.mipmap.wode_touxiang)
                        .transform(new GlideCircleTransform(this))
                        .into(img);
            } else {
                Glide.with(this)
                        .load(R.mipmap.wode_touxiang)
                        .crossFade()
                        .transform(new GlideCircleTransform(this))
                        .into(img);
            }
            nicheng.setText(loginBean.getName());
            zhanghao.setText(loginBean.getEmail());
        }
        touchId.setChecked(App.get().getSp().getBoolean(Constant.TOUCH_ID, false));
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()==EVENT_USERINFO){
            initData();
        }
    }

    public Uri uritempFile;
    private String protraitPath = "";// 选择图片地址
    String path;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111 && resultCode == RESULT_OK) {
            String path = PhotoUtils.parsePicturePath(this, data.getData());
            uritempFile = PhotoUtils.startPhotoZoom(this, new File(path), 2222);
        }
        if (requestCode == 2222 && resultCode == RESULT_OK) {
            showLoading();
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
                        String imgPath = null;
                        imgPath = oss.presignPublicObjectURL("inwecrypto-china", "android_header_" + currentTime);
                        final String finalImgPath = imgPath;
                        UserApi.setUserInfo(mActivity, imgPath, App.get().getLoginBean().getName(), new JsonCallback<LzyResponse<Object>>() {
                            @Override
                            public void onSuccess(Response<LzyResponse<Object>> response) {
                                img.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideLoading();
                                        App.get().getLoginBean().setImg(finalImgPath);
                                        App.get().getSp().putString(Constant.USER_INFO, GsonUtils.objToJson(App.get().getLoginBean()));
                                        Glide.with(mActivity)
                                                .load(finalImgPath)
                                                .crossFade()
                                                .error(R.mipmap.wode_touxiang)
                                                .transform(new GlideCircleTransform(mActivity))
                                                .into(img);
                                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_USERINFO));
                                        ToastUtil.show(getString(R.string.xiugaichenggong));
                                    }
                                });
                            }

                            @Override
                            public void onError(Response<LzyResponse<Object>> response) {
                                super.onError(response);
                                img.post(new Runnable() {
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
                        img.post(new Runnable() {
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
                img.post(new Runnable() {
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
