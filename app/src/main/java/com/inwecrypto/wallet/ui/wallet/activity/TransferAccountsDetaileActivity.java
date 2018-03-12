package com.inwecrypto.wallet.ui.wallet.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.math.BigDecimal;

import butterknife.BindView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.OrderBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.me.activity.CommonWebActivity;

import static com.inwecrypto.wallet.common.http.Url.ORDER_TEST_ULR;
import static com.inwecrypto.wallet.common.http.Url.ORDER_ULR;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class TransferAccountsDetaileActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.tv_wallet_address)
    TextView tvWalletAddress;
    @BindView(R.id.tv_get_address)
    TextView tvGetAddress;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_get_time)
    TextView tvGetTime;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.shouxufei)
    TextView shouxufei;
    @BindView(R.id.status)
    ImageView status;
    @BindView(R.id.tv_unit)
    TextView tvUnit;

    private OrderBean order;
    private String unit;

    @Override
    protected void getBundleExtras(Bundle extras) {
        order = (OrderBean) extras.getSerializable("order");
        unit=extras.getString("unit");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_activity_transfer_accounts_detaile;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.zhuanzhangxiangqing);
        txtRightTitle.setVisibility(View.GONE);
        tvUnit.setText("("+unit+")");
    }

    @Override
    protected void initData() {
        String priceNum="0.0000";
        if (order.getFee().startsWith("0x")){
            priceNum=AppUtil.toD(order.getFee());
        }else {
            priceNum=order.getFee();
        }
        BigDecimal b = new BigDecimal(priceNum);
        price.setText("-" + b.divide(Constant.pEther).setScale(4, BigDecimal.ROUND_DOWN).toPlainString());

        String gasNum="0.0000";
        if (null!=order.getHandle_fee()){
            if (order.getHandle_fee().startsWith("0x")){
                gasNum=AppUtil.toD(order.getHandle_fee());
            }else {
                gasNum=order.getHandle_fee();
            }
        }
        BigDecimal s = new BigDecimal(gasNum);
        shouxufei.setText(getString(R.string.lingfushouxufei) + s.divide(Constant.pEther).setScale(4, BigDecimal.ROUND_DOWN).toPlainString());

        if (order.getStatus() == 0) {
            Glide.with(this).load(R.mipmap.icon_complete).crossFade().into(status);
        } else if (order.getStatus() == 1) {
            Glide.with(this).load(R.mipmap.icon_processing).crossFade().into(status);
        } else {
            Glide.with(this).load(R.mipmap.icon_complete).crossFade().into(status);
        }
        tvWalletAddress.setText(null != order.getPay_address() ? order.getPay_address() : "");
        tvGetAddress.setText(null != order.getReceive_address() ? order.getReceive_address() : "");
        tvTime.setText(order.getCreated_at().contains("T")? AppUtil.getTime(order.getCreated_at()):order.getCreated_at());
        tvGetTime.setText(null==order.getRemark()?"":order.getRemark());

        tvWalletAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(order.getPay_address());
                ToastUtil.show(R.string.qianbaodizhifuzhi);
            }
        });

        tvGetAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(order.getReceive_address());
                ToastUtil.show(R.string.qianbaodizhifuzhi);
            }
        });

        tvOrder.setText(Html.fromHtml("<u>" + getString(R.string.jiaoyidanhao) + order.getTrade_no() + "<u>"));
        tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CommonWebActivity.class);
                intent.putExtra("title", getString(R.string.chaxunjiaoyi));
                intent.putExtra("url", (App.isMain?ORDER_ULR:ORDER_TEST_ULR) + order.getTrade_no());
                keepTogo(intent);
            }
        });

        tvOrder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(order.getTrade_no());
                ToastUtil.show(getString(R.string.jiaoyidanhaofuzhi));
                return true;
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
