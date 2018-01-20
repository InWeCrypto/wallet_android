package com.inwecrypto.wallet.ui.newneo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.util.QuanCodeUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/1/8 16:57
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class NewNeoReciveActivity extends BaseActivity {

    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.share)
    ImageView share;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.address)
    TextView address;

    private WalletBean wallet;
    private Bitmap bitmap;
    private Thread thread;

    @Override
    protected void getBundleExtras(Bundle extras) {
        wallet = (WalletBean) extras.getSerializable("wallet");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.newneo_receive_activity;
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
//        LoginBean loginBean = App.get().getLoginBean();
//        if (null!=loginBean.getUser().getImg()&&loginBean.getUser().getImg().length()>0){
//            Glide.with(this)
//                    .load(loginBean.getUser().getImg())
//                    .crossFade()
//                    .placeholder(R.mipmap.clod_icon)
//                    .transform(new GlideCircleTransform(this))
//                    .into(ivHeader);
//        }
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                //文本类型
                try {
                    bitmap = QuanCodeUtils.createQuanCode(mActivity,wallet.getAddress());
                    if (null!=ivCode){
                        ivCode.post(new Runnable() {
                            @Override
                            public void run() {
                                if (null!=ivCode&&null!=bitmap){
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
        if (null!=thread){
            thread.interrupt();
            thread=null;
        }
        if (null!=bitmap){
            bitmap.recycle();
            bitmap=null;
        }
        super.onDestroy();
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
