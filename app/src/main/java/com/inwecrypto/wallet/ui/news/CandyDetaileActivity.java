package com.inwecrypto.wallet.ui.news;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CandyBowBean;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/2/9 10:28
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class CandyDetaileActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.views)
    TextView views;

    private CandyBowBean.ListBean.DataBean candy;

    @Override
    protected void getBundleExtras(Bundle extras) {
        candy= (CandyBowBean.ListBean.DataBean) extras.getSerializable("candy");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.candy_detaile_activity;
    }

    @Override
    protected void initView() {

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtMainTitle.setText(R.string.xiangqing);

        txtRightTitle.setVisibility(View.GONE);

        if (null!=candy.getImg()){
            Glide.with(this)
                    .load(candy.getImg())
                    .crossFade()
                    .into(img);
        }else {
            Glide.with(this)
                    .load("")
                    .crossFade()
                    .into(img);
        }

        name.setText(candy.getName());
        content.setText(candy.getDesc());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
