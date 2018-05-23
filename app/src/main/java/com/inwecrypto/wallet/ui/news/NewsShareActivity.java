package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/3/14 14:05
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class NewsShareActivity extends BaseActivity {

    @BindView(R.id.top)
    ImageView top;
    @BindView(R.id.content)
    ImageView content;
    @BindView(R.id.bottom)
    ImageView bottom;
    @BindView(R.id.btmSl)
    ScrollView btmSl;
    @BindView(R.id.save)
    LinearLayout save;
    @BindView(R.id.share)
    LinearLayout share;
    @BindView(R.id.quxiao)
    TextView quxiao;

    private String file;
    private int width;
    private int height;
    private String id;

    @Override
    protected void getBundleExtras(Bundle extras) {
        file=extras.getString("file");
        width=extras.getInt("width");
        height=extras.getInt("height");
        id=extras.getString("id");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.news_share_activity;
    }

    @Override
    protected void initView() {
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFixLoading();
                final Bitmap bitmap=getBitmapByView(btmSl);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //获取图片
                        saveImageToGallery(compressImage(bitmap));
                        bitmap.recycle();
                    }
                }).start();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFixLoading();
                final Bitmap bitmap=getBitmapByView(btmSl);
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
                                share_intent = Intent.createChooser(share_intent, "分享长图");
                                startActivity(share_intent);
                            }
                        });
                    }
                }).start();

            }
        });

        LinearLayout.LayoutParams topLayoutParams= (LinearLayout.LayoutParams) top.getLayoutParams();
        topLayoutParams.height= (int) (ScreenUtils.getScreenWidth(this)/2250.0*318.0);
        top.setLayoutParams(topLayoutParams);

        LinearLayout.LayoutParams bottomLayoutParams= (LinearLayout.LayoutParams) bottom.getLayoutParams();
        bottomLayoutParams.height= (int) ((ScreenUtils.getScreenWidth(this)- DensityUtil.dip2px(this,30))/2070.0*1137.0);
        bottom.setLayoutParams(bottomLayoutParams);

        if (!App.get().isZh()){
            bottom.setImageResource(R.mipmap.xiazaiyemian_en);
        }

        File fileBtmp = new File(Environment.getExternalStorageDirectory(), file);
        Glide.with(this)
                .load(fileBtmp)
                .crossFade()
                .into(content);

        LinearLayout.LayoutParams contentLayoutParams= (LinearLayout.LayoutParams) content.getLayoutParams();
        contentLayoutParams.height= (int) ((ScreenUtils.getScreenWidth(this))/width*height*1.0);
        content.setLayoutParams(contentLayoutParams);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

    public void saveImageToGallery(Bitmap bitmap) {
        try {
            // 首先保存图片
            File appDir = new File(Environment.getExternalStorageDirectory(), "InWeCrypto");
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            String fileName = "InWeCrypto_"+id+"_"+System.currentTimeMillis() + ".jpg";
            final File file = new File(appDir, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            // 其次把文件插入到系统图库
            MediaStore.Images.Media.insertImage(this.getContentResolver(),
                        file.getAbsolutePath(), fileName, null);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //最后通知图库更新
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);//扫描单个文件
                    intent.setData(Uri.fromFile(file));//给图片的绝对路径
                    sendBroadcast(intent);

                    ToastUtil.show("已经保存到:"+file.getAbsolutePath());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideFixLoading();
                }
            });
        }
    }

    public Uri shareImage(Bitmap bitmap) {
        try {
            // 首先保存图片
            File appDir = new File(Environment.getExternalStorageDirectory(), "InWeCrypto");
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            String fileName = "InWeCrypto_"+id+"_"+System.currentTimeMillis() + ".jpg";
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

    public static Bitmap getBitmapByView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        // 获取scrollview实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(
                    Color.parseColor("#ffffff"));
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);

        return bitmap;
    }

    /**
     * 压缩图片
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 1024) {
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

}
