package com.inwecrypto.wallet.ui.wallet.activity.neowallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.NeoOderBean;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.me.activity.CommonWebActivity;

import java.math.BigDecimal;

import butterknife.BindView;

import static com.inwecrypto.wallet.common.http.Url.NEO_ORDER_TEST_ULR;
import static com.inwecrypto.wallet.common.http.Url.NEO_ORDER_ULR;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class NeoReceiveDetaileActivity extends BaseActivity {
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
    @BindView(R.id.tv_get_time)
    TextView tvGetTime;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.tv_unit)
    TextView tvUnit;

    private NeoOderBean.ListBean order;
    private String unit;

    @Override
    protected void getBundleExtras(Bundle extras) {
        order = (NeoOderBean.ListBean) extras.getSerializable("order");
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
        txtMainTitle.setText(R.string.shoukuanxiangqing);
        txtRightTitle.setVisibility(View.GONE);
        tvUnit.setText("("+unit+")");
    }

    @Override
    protected void initData() {
        BigDecimal b = new BigDecimal(order.getValue());
        price.setText("+"+b.setScale(4,BigDecimal.ROUND_HALF_UP).toPlainString());

        tvWalletAddress.setText(null!=order.getFrom()?order.getFrom():"");
        tvHeyueAddress.setText(null!=order.getTo()?order.getTo():"");
        tvTime.setText(null==order.getRemark()?"":order.getRemark());
        tvGetTime.setText("".equals(order.getCreateTime())?"":AppUtil.getTime(order.getCreateTime()));

        tvWalletAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(order.getFrom());
                ToastUtil.show(R.string.qianbaodizhifuzhi);
            }
        });

        tvHeyueAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(order.getTo());
                ToastUtil.show(R.string.qianbaodizhifuzhi);
            }
        });

        tvOrder.setText(Html.fromHtml("<u>"+getString(R.string.jiaoyidanhao)+order.getTx()+"<u>"));
        tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity, CommonWebActivity.class);
                intent.putExtra("title",getString(R.string.chaxunjiaoyi));
                intent.putExtra("url", (App.isMain?NEO_ORDER_ULR:NEO_ORDER_TEST_ULR)+order.getTx().replace("0x",""));
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
                cm.setText(order.getTx());
                ToastUtil.show(R.string.jiaoyidanhaofuzhi);
                return true;
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
