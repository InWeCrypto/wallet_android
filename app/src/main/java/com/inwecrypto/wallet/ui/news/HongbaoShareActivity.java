package com.inwecrypto.wallet.ui.news;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.QuanCodeUtils;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.tencent.tauth.Tencent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 作者：xiaoji06 on 2018/3/14 14:05
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class HongbaoShareActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {


    @BindView(R.id.quxiao)
    TextView quxiao;
    @BindView(R.id.space)
    Space space;
    @BindView(R.id.code)
    ImageView code;
    @BindView(R.id.name)
    TextView nameTv;
    @BindView(R.id.content)
    TextView contentTv;
    @BindView(R.id.share)
    LinearLayout share;
    @BindView(R.id.hbfl)
    FrameLayout hbfl;
    @BindView(R.id.bg)
    ImageView bg;
    @BindView(R.id.tip)
    TextView tip;

    private String url;
    private String name;
    private String content;
    private int type;
    private String gntName;

    private Bitmap bitmap;
    private Thread thread;
    private Tencent mTencent;
    private int padding;

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 200;

    @Override
    protected void getBundleExtras(Bundle extras) {
        url = extras.getString("url");
        name = extras.getString("name");
        content = extras.getString("content");
        type = extras.getInt("type");
        gntName=extras.getString("gntName");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.hongbao_share_activity;
    }

    @Override
    protected void initView() {

        mTencent = Tencent.createInstance("1106826620", App.get());

        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nameTv.setText(name + ":");
        contentTv.setText(content);

        if (!App.get().isZh()) {
            bg.setImageResource(R.mipmap.hongbao_share_bg_en);
            tip.setText("Long press the QR code\n" +
                    "and get "+gntName);
        }else {
            tip.setText("长按识别二维码\n" +
                    "领取"+gntName);
        }

        bg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                bg.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) hbfl.getLayoutParams();
                params.width= (int) (bg.getHeight()/2668.0f*1500.0f);
                hbfl.setLayoutParams(params);
                padding= (ScreenUtils.getScreenWidth(mActivity)-params.width)/2;
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //申请权限
                if (EasyPermissions.hasPermissions(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    shareHongbao();
                } else {
                    EasyPermissions.requestPermissions(mActivity, getString(R.string.duxiequanxian),
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }

            }
        });

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //文本类型
                try {
                    bitmap = QuanCodeUtils.createQuanCode(mActivity, url);
                    if (null != code) {
                        code.post(new Runnable() {
                            @Override
                            public void run() {
                                if (null != code && null != bitmap) {
                                    code.setImageBitmap(bitmap);
                                }
                            }
                        });
                    }
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void shareHongbao() {
        showFixLoading();
        final Bitmap bitmap = getBitmapByView(hbfl);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Uri uri = shareImage(compressImage(bitmap));
                bitmap.recycle();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /** * 分享图片 */
                        Intent share_intent = new Intent();
                        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
                        share_intent.setType("image/*");  //设置分享内容的类型
                        share_intent.putExtra(Intent.EXTRA_STREAM, uri);
                        //创建分享的Dialog
                        share_intent = Intent.createChooser(share_intent, getString(R.string.fenxiangchangtu));
                        startActivity(share_intent);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }


    @Override
    protected void onDestroy() {
        if (null != thread) {
            thread.interrupt();
            thread = null;
        }
        if (null != bitmap) {
            bitmap.recycle();
            bitmap = null;
        }
        super.onDestroy();
    }

    public Uri shareImage(Bitmap bitmap) {
        try {
            // 首先保存图片
            File appDir = new File(Environment.getExternalStorageDirectory(), "InWeCrypto");
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            String fileName = "InWeCrypto_hongbao_" + System.currentTimeMillis() + ".jpg";
            File file = new File(appDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                Uri uri = Uri.fromFile(file);
                return uri;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideFixLoading();
                }
            });
        }
        return null;
    }

    public Bitmap getBitmapByView(FrameLayout v) {
//        int h = 0;
//        Bitmap bitmap = null;
//        // 获取scrollview实际高度
//        for (int i = 0; i < scrollView.getChildCount(); i++) {
//            h += scrollView.getChildAt(i).getHeight();
////            scrollView.getChildAt(i).setBackgroundColor(
////                    Color.parseColor("#ffffff"));
//        }
//        scrollView.setBackgroundColor(Color.parseColor("#ffffff"));
//        // 创建对应大小的bitmap
//        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
//                Bitmap.Config.ARGB_8888);
//        final Canvas canvas = new Canvas(bitmap);
//        scrollView.draw(canvas);

        int w = v.getWidth();

        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bmp);

        c.drawColor(Color.TRANSPARENT);

        /** 如果不设置canvas画布为白色，则生成透明 */

        int ll = quxiao.getHeight() + space.getHeight();
        v.layout(padding, ll, padding+w, h + ll);

        v.draw(c);


        return bmp;
    }

    /**
     * 压缩图片
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 3072) {
            // 重置baos
            baos.reset();
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            // 每次都减少10
            options -= 10;
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        shareHongbao();
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
