package com.inwecrypto.wallet.ui.me.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.model.Response;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonRecordBean;
import com.inwecrypto.wallet.bean.IcoOrderBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.MeApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class IcoOrderDetaileActivity extends BaseActivity {

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
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.type)
    TextView type;

    private int id;

    @Override
    protected void getBundleExtras(Bundle extras) {
        id = extras.getInt("id");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.me_activity_ico_order_detaile;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.ico_order_detail_title);
        txtRightTitle.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        MeApi.getIcoOrderDetail(mActivity,id, new JsonCallback<LzyResponse<CommonRecordBean<IcoOrderBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonRecordBean<IcoOrderBean>>> response) {
                setData(response.body().data.getRecord());
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<CommonRecordBean<IcoOrderBean>>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<CommonRecordBean<IcoOrderBean>>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
            }
        });
    }

    private void setData(IcoOrderBean record) {
        price.setText(record.getFee());
        type.setText(getString(R.string.ico_order_hit1)+record.getIco().getCny());
        tvWalletAddress.setText(record.getPay_address());
        tvHeyueAddress.setText(record.getReceive_address());
        tvTime.setText(record.getCreated_at());
        tvName.setText(record.getIco().getTitle());
        tvOrder.setText(getString(R.string.ico_order_hit7)+record.getId());
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
