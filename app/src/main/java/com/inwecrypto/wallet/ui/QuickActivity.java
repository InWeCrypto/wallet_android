package com.inwecrypto.wallet.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/3/8 13:41
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class QuickActivity extends BaseActivity {
    @BindView(R.id.quickline)
    ImageView quickline;
    @BindView(R.id.quick)
    ImageView quick;
    @BindView(R.id.quickbtn)
    ImageView quickbtn;
    @BindView(R.id.quickRl)
    RelativeLayout quickRl;
    @BindView(R.id.addkuang)
    ImageView addkuang;
    @BindView(R.id.addline)
    ImageView addline;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.addbtn)
    ImageView addbtn;
    @BindView(R.id.addRl)
    RelativeLayout addRl;
    @BindView(R.id.recive)
    ImageView recive;
    @BindView(R.id.recive_arrow)
    ImageView reciveArrow;
    @BindView(R.id.recivebtn)
    ImageView recivebtn;
    @BindView(R.id.reciveRl)
    RelativeLayout reciveRl;
    @BindView(R.id.listkuang)
    ImageView listkuang;
    @BindView(R.id.listline)
    ImageView listline;
    @BindView(R.id.list)
    ImageView list;
    @BindView(R.id.listbtn)
    ImageView listbtn;
    @BindView(R.id.listRl)
    RelativeLayout listRl;

    private int type=1;
    private int y;

    @Override
    protected void getBundleExtras(Bundle extras) {
        type=extras.getInt("type");
        y=extras.getInt("y");

    }

    @Override
    protected int setLayoutID() {
        return R.layout.quick_activity;
    }

    @Override
    protected void initView() {
        switch (type){
            case 1:
                if (!App.get().isZh()){
                    list.setImageResource(R.mipmap.listtipen);
                    listbtn.setImageResource(R.mipmap.ikonwen);
                }
                listRl.setVisibility(View.VISIBLE);
                listRl.setY(y- DensityUtil.dip2px(this,12));
                listbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        App.get().getSp().putBoolean(Constant.FIRST_1,false);
                        finish();
                    }
                });
                break;
            case 2:
                if (!App.get().isZh()){
                    add.setImageResource(R.mipmap.addtipen);
                    addbtn.setImageResource(R.mipmap.ikonwen);
                }
                addRl.setVisibility(View.VISIBLE);
                addbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        App.get().getSp().putBoolean(Constant.FIRST_2,false);
                        finish();
                    }
                });
                break;

            case 3:
                if (!App.get().isZh()){
                    quick.setImageResource(R.mipmap.quicktipen);
                    quickbtn.setImageResource(R.mipmap.ikonwen);
                }
                quickRl.setVisibility(View.VISIBLE);
                quickbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        App.get().getSp().putBoolean(Constant.FIRST_3,false);
                        finish();
                    }
                });
                break;

            case 4:
                if (!App.get().isZh()){
                    recive.setImageResource(R.mipmap.receivtipen);
                    recivebtn.setImageResource(R.mipmap.ikonwen);
                }
                reciveRl.setVisibility(View.VISIBLE);
                reciveRl.setY(y);
                recivebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        App.get().getSp().putBoolean(Constant.FIRST_4,false);
                        finish();
                    }
                });
                break;

        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

}
