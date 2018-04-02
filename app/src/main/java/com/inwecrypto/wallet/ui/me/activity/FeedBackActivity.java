package com.inwecrypto.wallet.ui.me.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.MeApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.lzy.okgo.model.Response;

import java.util.IllegalFormatCodePointException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/3/20 15:23
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class FeedBackActivity extends BaseActivity {
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.gongneng)
    RadioButton gongneng;
    @BindView(R.id.jianyi)
    RadioButton jianyi;
    @BindView(R.id.qita)
    RadioButton qita;
    @BindView(R.id.typeRg)
    RadioGroup typeRg;
    @BindView(R.id.et_info)
    EditText etInfo;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.tv_tijiao)
    TextView tvTijiao;

    private int type=1;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.feed_back_activity;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtMainTitle.setText(R.string.yijianfankui);
        txtRightTitle.setVisibility(View.GONE);

        typeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.gongneng:
                        type=1;
                        break;
                    case R.id.jianyi:
                        type=2;
                        break;
                    case R.id.qita:
                        type=3;
                        break;
                }
            }
        });

        tvTijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etInfo.getText().toString().trim().length()==0){
                    ToastUtil.show(getString(R.string.neirongbunengweikong));
                    return;
                }

                if (content.getText().toString().trim().length()==0){
                    ToastUtil.show(getString(R.string.lianxifangshibunengweikong));
                    return;
                }

                MeApi.feedBack(this
                        ,type
                        ,etInfo.getText().toString().trim()
                        ,content.getText().toString().trim()
                        , new JsonCallback<LzyResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<Object>> response) {
                        ToastUtil.show(getString(R.string.fankuichenggong));
                        finish();
                    }

                    @Override
                    public void onError(Response<LzyResponse<Object>> response) {
                        super.onError(response);
                        ToastUtil.show(getString(R.string.fankuishibai));
                    }
                });
            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
