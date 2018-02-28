package com.inwecrypto.wallet.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import okhttp3.Call;


/**
 * 作者：xiaoji06 on 2018/1/29 17:42
 * github：https://github.com/xiaoji06
 * 功能：
 */
public class DownloadService extends Service {

    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private String version;
    private String hash;
    private String filePath;


    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            notifyMsg(getString(R.string.wenxintishi), getString(R.string.wenjianxiazaishibai), 0);
            stopSelf();
        }
        version = intent.getStringExtra("version");
        hash = intent.getStringExtra("hash");
        downloadFile(version,hash);//下载APK
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void notifyMsg(String title, String content, int progress) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);//为了向下兼容，这里采用了v7包下的NotificationCompat来构造
        builder.setSmallIcon(R.mipmap.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)).setContentTitle(title);
        if (progress > 0 && progress < 100) {
            //下载进行中
            builder.setProgress(100, progress, false);
        } else {
            builder.setProgress(0, 0, false);
        }
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentText(content);
        if (progress >= 100) {
            getInstallIntent();
        }
        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_DOWNLOAD_UPDATE,progress));
        mNotification = builder.build();
        mNotificationManager.notify(0, mNotification);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    /**
     * 安装apk文件
     *
     * @return
     */
    private void getInstallIntent() {
        //检查 hash
        final File file = new File(filePath);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (hash.equals(AppUtil.md5HashCode32(new FileInputStream(file)))){
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        //版本在7.0以上是不能直接通过uri访问的
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                            // 由于没有在Activity环境下启动Activity,设置下面的标签
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                            Uri apkUri = FileProvider.getUriForFile(getApplicationContext(), "com.inwecrypto.wallet", file);
                            //添加这一句表示对目标应用临时授权该Uri所代表的文件
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                        } else {
                            intent.setDataAndType(Uri.fromFile(file),
                                    "application/vnd.android.package-archive");
                        }
                        startActivity(intent);
                    }else {
                        ToastUtil.show(getString(R.string.feifadeapk));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    /**
     * 下载apk文件
     *
     */
    private void downloadFile(String version,String hash) {

        OkGo.<File>get(Url.APP_ADDRESS)
                .tag(this).execute(new FileCallback("Inwecrypto"+version+".apk"){

            @Override
            public void onSuccess(Response<File> response) {
                filePath=response.body().getAbsolutePath();
                //当文件下载完成后回调
                notifyMsg(getString(R.string.wenxintishi), getString(R.string.wenjianxiazaiyiwancheng), 100);
                stopSelf();
            }

            @Override
            public void onError(Response<File> response) {
                super.onError(response);
                notifyMsg(getString(R.string.wenxintishi), getString(R.string.wenjianxiazaishibai), 0);
                stopSelf();
            }

            @Override
            public void downloadProgress(Progress progress) {
                super.downloadProgress(progress);
                //progress*100为当前文件下载进度，total为文件大小
                int persent= (int) ((progress.currentSize*100L)/progress.totalSize);
                if (persent==100){
                    return;
                }
                //避免频繁刷新View，这里设置每下载10%提醒更新一次进度
                notifyMsg(getString(R.string.wenxintishi), getString(R.string.wenjianzhengzaixiazaizhong), persent);
            }
        });
    }
}
