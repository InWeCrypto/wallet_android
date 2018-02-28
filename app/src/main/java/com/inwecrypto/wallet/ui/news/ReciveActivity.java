package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.zxing.WriterException;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.QuanCodeUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.inwecrypto.wallet.common.Constant.EVENT_WALLET_SELECT;

/**
 * 作者：xiaoji06 on 2018/2/13 12:48
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ReciveActivity extends BaseActivity {
    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.share)
    ImageView share;
    @BindView(R.id.code)
    ImageView code;
    @BindView(R.id.no_code)
    ImageView noCode;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.b2)
    ImageView b2;
    @BindView(R.id.walletrl)
    RelativeLayout walletrl;
    @BindView(R.id.no_codefl)
    FrameLayout noCodefl;

    private ArrayList<WalletBean> wallet = new ArrayList<>();
    private WalletBean currentWallet;

    private Bitmap bitmap;
    private Thread thread;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.home_recive_activity;
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
                if (null == currentWallet) {
                    ToastUtil.show(getString(R.string.zanwuqianbao));
                    return;
                }
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.putExtra(Intent.EXTRA_TEXT, currentWallet.getAddress());
                intent1.setType("text/plain");
                keepTogo(Intent.createChooser(intent1, "share"));
            }
        });

        walletrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == currentWallet) {
                    ToastUtil.show(getString(R.string.zanwuqianbao));
                    return;
                }

                Intent intent = new Intent(mActivity, RecivceWalletListActivity.class);
                intent.putExtra("wallet", wallet);
                keepTogo(intent);
            }
        });
    }

    @Override
    protected void initData() {
        //获取钱包列表
        WalletApi.wallet(mActivity, new JsonCallback<LzyResponse<CommonListBean<WalletBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                wallet.clear();
                wallet.addAll(response.body().data.getList());
                if (wallet.size() != 0) {
                    currentWallet = wallet.get(0);
                }
                //设置钱包,生成二维码
                setWallet();
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.jiazaishibai));
            }
        });
    }

    private void setWallet() {
        if (null != currentWallet) {
            noCodefl.setVisibility(View.GONE);
            if (currentWallet.getCategory_id() == 1) {
                Glide.with(this)
                        .load(R.mipmap.project_icon_eth)
                        .crossFade()
                        .into(img);
            } else {
                Glide.with(this)
                        .load(R.mipmap.neoxxhdpi)
                        .crossFade()
                        .into(img);
            }
            name.setText(currentWallet.getName());
            address.setText(currentWallet.getAddress());

            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    //文本类型
                    try {
                        bitmap = QuanCodeUtils.createQuanCode(mActivity, currentWallet.getAddress());
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
        if (event.getEventCode() == EVENT_WALLET_SELECT) {
            currentWallet = wallet.get(event.getKey1());
            setWallet();
        }
    }

}
