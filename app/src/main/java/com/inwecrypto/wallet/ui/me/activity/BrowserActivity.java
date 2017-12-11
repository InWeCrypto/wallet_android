package com.inwecrypto.wallet.ui.me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.event.BaseEventBusBean;

import butterknife.BindView;

/**
 * Created by donghaijun on 2017/10/24.
 */

public class BrowserActivity extends BaseActivity {


    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.block)
    RelativeLayout block;
    @BindView(R.id.eth)
    RelativeLayout eth;
    @BindView(R.id.neo)
    RelativeLayout neo;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.browser_activity;
    }

    @Override
    protected void initView() {

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtRightTitle.setVisibility(View.INVISIBLE);
        txtMainTitle.setText(R.string.liulanqi);

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,CommonWebActivity.class);
                intent.putExtra("title","BLOCKCHAIN");
                intent.putExtra("url", Constant.BLOCKCHAIN);
                keepTogo(intent);
            }
        });

        eth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,CommonWebActivity.class);
                intent.putExtra("title","ETHERSCAN");
                intent.putExtra("url", Constant.ETHERSCAN);
                keepTogo(intent);
            }
        });

        neo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,CommonWebActivity.class);
                intent.putExtra("title","NEO TRACKER");
                intent.putExtra("url", Constant.NEO_TRACKER);
                keepTogo(intent);
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
