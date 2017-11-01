package com.inwecrypto.wallet.ui.wallet.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Html;
import android.view.View;
import android.widget.TextView;


import java.math.BigDecimal;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.OrderBean;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.me.activity.CommonWebActivity;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class ReceiveDetaileActivity extends BaseActivity {
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.tv_wallet_address)
    TextView tvWalletAddress;
    @BindView(R.id.tv_heyue_address)
    TextView tvHeyueAddress;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.tv_unit)
    TextView tvUnit;

    private OrderBean order;
    private BigDecimal pEther = new BigDecimal("1000000000000000000");
    private String unit;

    @Override
    protected void getBundleExtras(Bundle extras) {
        order = (OrderBean) extras.getSerializable("order");
        unit=extras.getString("unit");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_activity_receive_detaile;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.receive_detaile_title);
        txtRightTitle.setVisibility(View.GONE);
        tvUnit.setText("("+unit+")");
    }

    @Override
    protected void initData() {
        BigDecimal b = new BigDecimal(order.getFee());
        price.setText("+"+b.divide(pEther).setScale(4,BigDecimal.ROUND_HALF_UP).toPlainString());

        tvWalletAddress.setText(null!=order.getPay_address()?order.getPay_address():"");
        tvHeyueAddress.setText(null!=order.getReceive_address()?order.getReceive_address():"");
        tvTime.setText(order.getRemark());

        tvWalletAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(order.getTrade_no());
                ToastUtil.show("钱包地址已复制到剪贴板");
            }
        });

        tvHeyueAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(order.getTrade_no());
                ToastUtil.show("钱包地址已复制到剪贴板");
            }
        });

        tvOrder.setText(Html.fromHtml("<u>"+getString(R.string.transfer_hit2)+order.getTrade_no()+"<u>"));
        tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity, CommonWebActivity.class);
                intent.putExtra("title","查询交易");
                intent.putExtra("url", Url.ORDER_ULR+order.getTrade_no());
                keepTogo(intent);
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
