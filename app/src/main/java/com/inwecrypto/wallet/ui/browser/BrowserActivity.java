package com.inwecrypto.wallet.ui.browser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.me.activity.CommonWebActivity;

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
        return R.layout.browser_fragment;
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
        txtMainTitle.setText("浏览器");

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,CommonWebActivity.class);
                intent.putExtra("title","BLOCKCHAIN");
                intent.putExtra("url", "https://blockchain.info");
                keepTogo(intent);
            }
        });

        eth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,CommonWebActivity.class);
                intent.putExtra("title","ETHERSCAN");
                intent.putExtra("url", "https://ethscan.io");
                keepTogo(intent);
            }
        });

        neo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,CommonWebActivity.class);
                intent.putExtra("title","NEO TRACKER");
                intent.putExtra("url", "https://neotracker.io");
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
