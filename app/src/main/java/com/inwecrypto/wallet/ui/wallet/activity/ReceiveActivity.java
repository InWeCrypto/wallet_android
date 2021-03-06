package com.inwecrypto.wallet.ui.wallet.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.zxing.WriterException;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.LoginBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.imageloader.GlideCircleTransform;
import com.inwecrypto.wallet.common.util.QuanCodeUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class ReceiveActivity extends BaseActivity {

    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.share)
    ImageView share;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    private WalletBean wallet;
    private Bitmap bitmap;
    private Thread thread;

    @Override
    protected void getBundleExtras(Bundle extras) {
        wallet = (WalletBean) extras.getSerializable("wallet");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_activity_receive;
    }

    @Override
    protected void initView() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.putExtra(Intent.EXTRA_TEXT, wallet.getAddress());
                intent1.setType("text/plain");
                keepTogo(Intent.createChooser(intent1, "share"));
            }
        });

        ivCode.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivCode.getLayoutParams();
                params.height = ivCode.getMeasuredWidth();
                ivCode.setLayoutParams(params);
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setPrimaryClip(ClipData.newPlainText(null, wallet.getAddress()));
                ToastUtil.show(getString(R.string.fuzhichenggong));
            }
        });
    }

    @Override
    protected void initData() {
        address.setText(wallet.getAddress());
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //文本类型
                try {
                    bitmap = QuanCodeUtils.createQuanCode(mActivity, wallet.getAddress());
                    if (null != ivCode) {
                        ivCode.post(new Runnable() {
                            @Override
                            public void run() {
                                if (null != ivCode && null != bitmap) {
                                    ivCode.setImageBitmap(bitmap);
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

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
