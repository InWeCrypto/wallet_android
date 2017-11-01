package com.inwecrypto.wallet.ui.market.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.MarketRemindBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.MarketApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class MarketRemindEditActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.net_name)
    TextView netName;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.up)
    EditText up;
    @BindView(R.id.down)
    EditText down;

    private MarketRemindBean market;


    @Override
    protected void getBundleExtras(Bundle extras) {
        market= (MarketRemindBean) extras.getSerializable("market");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.market_activity_remind_edit;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.tixing);
        txtRightTitle.setText(R.string.wancheng);
        txtRightTitle.setCompoundDrawables(null,null,null,null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (up.getText().toString().length()==0||down.getText().toString().length()==0){
                    ToastUtil.show(getString(R.string.remind_hit));
                    return;
                }
                MarketApi.marketNotification(mActivity, up.getText().toString(), down.getText().toString(), market.getRelation_notification().get(0).getId(), new JsonCallback<LzyResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<Object>> response) {
                        EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_REFRESH));
                        ToastUtil.show(getString(R.string.xiugaichenggong));
                        finish();
                    }

                    @Override
                    public void onError(Response<LzyResponse<Object>> response) {
                        super.onError(response);
                        ToastUtil.show(getString(R.string.xiugaishibai));
                    }
                });

            }
        });

    }

    @Override
    protected void initData() {
        name.setText(market.getName());
        netName.setText(market.getFlag());
        if (null!=market.getRelationCap()){
            price.setText("￥"+new BigDecimal(market.getRelationCap().getPrice_cny()).setScale(2,BigDecimal.ROUND_HALF_UP));
        }else {
            price.setText("暂无数据");
        }

        up.setText(market.getRelation_notification().get(0).getUpper_limit());
        down.setText(market.getRelation_notification().get(0).getLower_limit());
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
