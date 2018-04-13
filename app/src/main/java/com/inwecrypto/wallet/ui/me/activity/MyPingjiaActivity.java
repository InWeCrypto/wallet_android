package com.inwecrypto.wallet.ui.me.activity;

import android.Manifest;
import android.Manifest.permission;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.PingjiaBean;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.RatingBar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.NewsShareActivity;

import net.qiujuer.genius.ui.widget.Button;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 作者：xiaoji06 on 2018/4/4 15:28
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class MyPingjiaActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{

    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.pingfenbg)
    ImageView pingfenbg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.long_name)
    TextView longName;
    @BindView(R.id.net)
    TextView net;
    @BindView(R.id.fenshu)
    TextView fenshu;
    @BindView(R.id.ratingbar)
    RatingBar ratingbar;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.detaile)
    FrameLayout detaile;
    @BindView(R.id.share)
    Button share;
    @BindView(R.id.imgll)
    FrameLayout imgll;

    private boolean isFromMe;
    private PingjiaBean pingjia;

    private static final int RC_READ = 222;

    @Override
    protected void getBundleExtras(Bundle extras) {
        isFromMe=extras.getBoolean("isFromMe",false);
        pingjia= (PingjiaBean) extras.getSerializable("pingjia");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.my_pingjia_activity_layout;
    }

    @Override
    protected void initView() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (isFromMe){
            title.setText(R.string.wodepingjia);
        }else {
            title.setText(R.string.fabuchenggong);
        }

        Glide.with(this).load(pingjia.getCategory().getImg()).crossFade().into(img);
        name.setText(pingjia.getCategory().getName());
        longName.setText(pingjia.getCategory().getLong_name());
        net.setText(pingjia.getCategory().getIndustry());
        String leve="";
        if (Float.parseFloat(pingjia.getScore())<=1){
            leve=getString(R.string.henbumanyi);
        }else if (Float.parseFloat(pingjia.getScore())<=2){
            leve=getString(R.string.bumanyi);
        }else if (Float.parseFloat(pingjia.getScore())<=3){
            leve=getString(R.string.yiban);
        }else if (Float.parseFloat(pingjia.getScore())<=4){
            leve=getString(R.string.tuijian);
        }else if (Float.parseFloat(pingjia.getScore())<=5){
            leve=getString(R.string.feichangtuijian);
        }

        fenshu.setText(pingjia.getScore()+getString(R.string.fenshu)+"("+leve+")");
        ratingbar.setStar(Float.parseFloat(pingjia.getScore()));
        if (null==pingjia.getCategory_comment()||"".equals(pingjia.getCategory_comment())){
            content.setText(R.string.morenpingjia);
        }else {
            content.setText(pingjia.getCategory_comment());
        }

        int width=ScreenUtils.getScreenWidth(this)- DensityUtil.dip2px(this,108);
        FrameLayout.LayoutParams params= (FrameLayout.LayoutParams) pingfenbg.getLayoutParams();
        params.height= (int) (width/1590.0*261.0);
        pingfenbg.setLayoutParams(params);

        detaile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,PingjiaXiangqingActivity.class);
                intent.putExtra("pingjia",pingjia);
                intent.putExtra("isFromMe",isFromMe);
                keepTogo(intent);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyPermissions.hasPermissions(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //获取图片
                    getImage();
                } else {
                    EasyPermissions.requestPermissions(mActivity, getString(R.string.duxiequanxian),
                            RC_READ, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        });
    }

    private void getImage() {
        showFixLoading();

        final Bitmap bitmap=loadBitmapFromView(imgll);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmapC=compressImage(bitmap,1024);
                int width=0;
                int height=0;
                try {
                    // 首先保存图片
                    File appDir = new File(Environment.getExternalStorageDirectory(), "InWeCrypto/cache");
                    if (!appDir.exists()) {
                        appDir.mkdirs();
                    }
                    final String fileName="/InWeCrypto/cache/webview_capture_pingjia_"+System.currentTimeMillis()+".jpg";
                    String filePath = Environment.getExternalStorageDirectory().getPath()+fileName;
                    FileOutputStream fos = new FileOutputStream(filePath);
                    //压缩bitmap到输出流中
                    bitmapC.compress(Bitmap.CompressFormat.JPEG, 70, fos);
                    width=bitmapC.getWidth();
                    height=bitmapC.getHeight();
                    fos.close();
                    bitmapC.recycle();
                    bitmap.recycle();

                    final int finalWidth = width;
                    final int finalHeight = height;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //跳转页面
                            Intent intent=new Intent(mActivity,NewsShareActivity.class);
                            intent.putExtra("file",fileName);
                            intent.putExtra("id","pingjia");
                            intent.putExtra("width", finalWidth);
                            intent.putExtra("height", finalHeight);
                            keepTogo(intent);
                        }
                    });

                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(getString(R.string.tupianbaocunshibai));
                        }
                    });
                    return;
                }finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideFixLoading();
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 压缩图片
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image,int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > size) {
            // 重置baos
            baos.reset();
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            // 每次都减少10
            options -= 15;
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    @Override
    protected void initData() {

    }

    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.parseColor("#F6F6F6"));
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, (int) v.getY(), w, h+(int) v.getY());
        v.draw(c);

        return bmp;
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
        //获取图片
        getImage();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
