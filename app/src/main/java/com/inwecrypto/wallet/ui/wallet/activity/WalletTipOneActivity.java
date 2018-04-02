package com.inwecrypto.wallet.ui.wallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.event.BaseEventBusBean;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class WalletTipOneActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.id_flowlayout)
    TagFlowLayout idFlowlayout;
    @BindView(R.id.add)
    TextView add;

    private TagAdapter<String> adapter;
    private ArrayList<String> data=new ArrayList<>();
    private String zjc;
    private WalletBean wallet;
    private boolean isNew;
    private boolean isEth;

    @Override
    protected void getBundleExtras(Bundle extras) {
        zjc = extras.getString("zjc");
        wallet= (WalletBean) extras.getSerializable("wallet");
        isNew=extras.getBoolean("isNew");
        isEth=extras.getBoolean("isEth");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_acivity_tip_one;
    }

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,WalletTipTwoActivity.class);
                intent.putExtra("data",data);
                intent.putExtra("wallet",wallet);
                intent.putExtra("isNew",isNew);
                intent.putExtra("isEth",isEth);
                keepTogo(intent);
            }
        });
        adapter=new TagAdapter<String>(data) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.wallet_fllow_tv,
                        parent, false);
                tv.setText(s);
                return tv;
            }
        };
        idFlowlayout.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        txtMainTitle.setText(R.string.beifenzhujici);
        txtRightTitle.setVisibility(View.GONE);

        String[] zjcStr=zjc.split(" ");
        for (int i=0;i<zjcStr.length;i++){
            data.add(zjcStr[i]);
        }
        adapter.notifyDataChanged();
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
