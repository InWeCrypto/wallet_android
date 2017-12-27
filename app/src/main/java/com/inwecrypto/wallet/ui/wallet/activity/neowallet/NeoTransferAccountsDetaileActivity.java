package com.inwecrypto.wallet.ui.wallet.activity.neowallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.AppApplication;
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

public class NeoTransferAccountsDetaileActivity extends BaseActivity {

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

    private NeoOderBean.ListBean order;
    private String unit;

    @Override
    protected void getBundleExtras(Bundle extras) {
        order = (NeoOderBean.ListBean) extras.getSerializable("order");
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
        BigDecimal b = new BigDecimal(order.getValue());
        if (order.getFrom().equals(order.getTo())){
            price.setText(b.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString());
        }else {
            price.setText("-" + b.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString());
        }
        BigDecimal s = new BigDecimal("0");
        shouxufei.setText(getString(R.string.lingfushouxufei) + s.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString());
        if (null==order.getConfirmTime()||order.getConfirmTime().equals("")) {
            Glide.with(this).load(R.mipmap.icon_processing).crossFade().into(status);
        } else {
            Glide.with(this).load(R.mipmap.icon_complete).crossFade().into(status);
        }
        tvWalletAddress.setText(null != order.getFrom() ? order.getFrom() : "");
        tvGetAddress.setText(null != order.getTo() ? order.getTo() : "");
        tvTime.setText("".equals(order.getCreateTime())?"":AppUtil.getTime(order.getCreateTime()));
        tvGetTime.setText(null==order.getRemark()?"":order.getRemark());

        tvWalletAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(order.getTx());
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
                cm.setText(order.getTx());
                ToastUtil.show(R.string.qianbaodizhifuzhi);
            }
        });

        tvOrder.setText(Html.fromHtml("<u>" + getString(R.string.jiaoyidanhao) + order.getTx() + "<u>"));
        tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CommonWebActivity.class);
                intent.putExtra("title", getString(R.string.chaxunjiaoyi));
                intent.putExtra("url", (AppApplication.isMain?NEO_ORDER_ULR:NEO_ORDER_TEST_ULR) + order.getTx().replace("0x",""));
                keepTogo(intent);
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
