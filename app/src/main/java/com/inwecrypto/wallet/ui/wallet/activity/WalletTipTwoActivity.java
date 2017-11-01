package com.inwecrypto.wallet.ui.wallet.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import com.inwecrypto.wallet.AppApplication;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class WalletTipTwoActivity extends BaseActivity {


    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.top)
    TagFlowLayout top;
    @BindView(R.id.below)
    TagFlowLayout below;
    @BindView(R.id.add)
    TextView add;

    private TagAdapter<String> topAdapter;
    private TagAdapter<String> belowAdapter;
    private ArrayList<String> data;
    private ArrayList<String> showData = new ArrayList<>();
    private ArrayList<String> selectData = new ArrayList<>();

    private WalletBean wallet;

    @Override
    protected void getBundleExtras(Bundle extras) {
        wallet= (WalletBean) extras.getSerializable("wallet");
        data = extras.getStringArrayList("data");
        showData.addAll(data);
        Collections.shuffle(showData);
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_acivity_tip_two;
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
                if (showData.size()>0){
                    ToastUtil.show("请将全部的助记词选择完毕");
                    return;
                }
                boolean isRight=true;
                for (int i=0;i<data.size();i++){
                    if (!data.get(i).equals(selectData.get(i))){
                        isRight=false;
                        break;
                    }
                }
                if (isRight){
                    ToastUtil.show("助记词备份成功！");

                    String wallets = AppApplication.get().getSp().getString(Constant.WALLETS_ZJC_BEIFEN, "");
                    if (!wallets.contains(wallet.getAddress())) {
                        wallets = wallets + wallet.getAddress() + ",";
                        AppApplication.get().getSp().putString(Constant.WALLETS_ZJC_BEIFEN, wallets);
                    }
                    AppManager.getAppManager().finishActivity(WalletTipOneActivity.class);
                    EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET));
                    EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_TIP_SUCCESS));
                    finish();
                }else {
                    ToastUtil.show("助记词顺序错误，请重新检查");
                }
            }
        });

        belowAdapter = new TagAdapter<String>(showData) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.wallet_fllow_tv_bg,
                        parent, false);
                tv.setText(s);
                return tv;
            }
        };
        below.setAdapter(belowAdapter);

        below.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                selectData.add(showData.get(position));
                showData.remove(position);
                belowAdapter.notifyDataChanged();
                topAdapter.notifyDataChanged();
                return true;
            }
        });


        topAdapter = new TagAdapter<String>(selectData) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.wallet_fllow_tv_bg,
                        parent, false);
                tv.setText(s);
                return tv;
            }
        };
        top.setAdapter(topAdapter);

        top.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                showData.add(selectData.get(position));
                selectData.remove(position);
                belowAdapter.notifyDataChanged();
                topAdapter.notifyDataChanged();
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        txtMainTitle.setText("备份助记词");
        txtRightTitle.setVisibility(View.GONE);

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
