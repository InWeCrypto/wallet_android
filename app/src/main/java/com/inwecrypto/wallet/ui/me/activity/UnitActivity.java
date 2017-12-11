package com.inwecrypto.wallet.ui.me.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;


import butterknife.BindView;
import com.inwecrypto.wallet.AppApplication;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.event.BaseEventBusBean;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class UnitActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.select)
    ImageView select;
    @BindView(R.id.cn)
    RelativeLayout cn;
    @BindView(R.id.select2)
    ImageView select2;
    @BindView(R.id.us)
    RelativeLayout us;

    private int selectPosition = -1;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.me_activity_unit;
    }

    @Override
    protected void initView() {
        txtMainTitle.setText(R.string.huobidanwei);
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtRightTitle.setVisibility(View.GONE);

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectPosition == -1) {
                    finish();
                } else {
                    AppApplication.get().getSp().putInt(Constant.UNIT_TYPE,selectPosition);
                    EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_UNIT_CHANGE));
                    finish();
                }
            }
        });

        cn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.setImageResource(R.mipmap.list_btn_selected);
                select2.setImageResource(R.mipmap.list_btn_default);
                selectPosition=1;
            }
        });

        us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.setImageResource(R.mipmap.list_btn_default);
                select2.setImageResource(R.mipmap.list_btn_selected);
                selectPosition=2;
            }
        });

    }

    @Override
    protected void initData() {
        if (1== AppApplication.get().getUnit()){
            select.setImageResource(R.mipmap.list_btn_selected);
            select2.setImageResource(R.mipmap.list_btn_default);
            selectPosition=1;
        }else {
            select.setImageResource(R.mipmap.list_btn_default);
            select2.setImageResource(R.mipmap.list_btn_selected);
            selectPosition=2;
        }

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
