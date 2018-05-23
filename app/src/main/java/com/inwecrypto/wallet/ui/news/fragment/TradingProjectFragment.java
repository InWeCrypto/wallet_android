package com.inwecrypto.wallet.ui.news.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.TradingProjectDetaileBean;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.ProjectNewsWebActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/3/15 11:09
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class TradingProjectFragment extends BaseFragment {
    @BindView(R.id.paiming)
    TextView paiming;
    @BindView(R.id.shizhi)
    TextView shizhi;
    @BindView(R.id.liuliang)
    TextView liuliang;
    @BindView(R.id.zongliang)
    TextView zongliang;
    @BindView(R.id.icojiage)
    TextView icojiage;
    @BindView(R.id.liulanqi)
    RecyclerView liulanqi;
    @BindView(R.id.qianbao)
    RecyclerView qianbao;
    @BindView(R.id.daibifenbufl)
    FrameLayout daibifenbufl;

    private TradingProjectDetaileBean project;

    private ArrayList<String> liulanqiData = new ArrayList<>();

    private ArrayList<String> qianbaoData = new ArrayList<>();

    private EmptyWrapper liulanqiEmpty;

    private EmptyWrapper qianbaoEmpty;

    @Override
    protected int setLayoutID() {
        return R.layout.trading_project_fragment;
    }

    @Override
    protected void initView() {

        project = (TradingProjectDetaileBean) getArguments().getSerializable("project");

        if (null != project.getIco()) {
            if (App.get().isZh()) {
                paiming.setText("第" + project.getIco().getRank() + "名");
            } else {
                paiming.setText("No." + project.getIco().getRank());
            }

            if (App.get().isZh()){
                if (null!=project.getIco().getMarket_cap_usd()){
                    shizhi.setText("¥"+new BigDecimal(project.getIco().getMarket_cap_cny()).divide(new BigDecimal(1000000)).toPlainString() +" million");
                }else {
                    shizhi.setText("¥ -- million");
                }
            }else {
                if (null!=project.getIco().getMarket_cap_usd()){
                    shizhi.setText("$ "+new BigDecimal(project.getIco().getMarket_cap_usd()).divide(new BigDecimal(1000000)).toPlainString() +" million");
                }else {
                    shizhi.setText("$ -- million");
                }
            }

            if (null!=project.getIco().getAvailable_supply()){
                liuliang.setText(new BigDecimal(project.getIco().getAvailable_supply()).divide(new BigDecimal(1000000)).toPlainString()+" million "+project.getUnit());
            }else {
                liuliang.setText("-- million "+project.getUnit());
            }
            if (null!=project.getIco().getTotal_supply()){
                zongliang.setText(new BigDecimal(project.getIco().getTotal_supply()).divide(new BigDecimal(1000000)).toPlainString()+" million "+project.getUnit());
            }else {
                zongliang.setText("-- million "+project.getUnit());
            }
            if (null!=project.getIco_price()){
                icojiage.setText(project.getIco_price());
            }else {
                icojiage.setText("--");
            }
        }

        if (null!=project.getCategory_wallet()){
            for (TradingProjectDetaileBean.CategoryExplorerBean explorer : project.getCategory_explorer()) {
                liulanqiData.add(explorer.getName());
            }
        }

        liulanqi.setLayoutManager(new LinearLayoutManager(mContext));
        CommonAdapter<String> lilanqiAdapter = new CommonAdapter<String>(mContext, R.layout.project_wallet_expr_item, liulanqiData) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                if (position == (mDatas.size() - 1)) {
                    holder.getView(R.id.line).setVisibility(View.INVISIBLE);
                } else {
                    holder.getView(R.id.line).setVisibility(View.VISIBLE);
                }
                holder.setText(R.id.name, s);
            }
        };
        liulanqiEmpty=new EmptyWrapper(lilanqiAdapter);
        liulanqiEmpty.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.project_wallet_expr_empty,null,false));
        liulanqi.setAdapter(liulanqiEmpty);
        liulanqi.setNestedScrollingEnabled(false);

        lilanqiAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mActivity, ProjectNewsWebActivity.class);
                intent.putExtra("title", project.getCategory_explorer().get(position).getName());
                intent.putExtra("url", project.getCategory_explorer().get(position).getUrl());
                intent.putExtra("show", false);
                keepTogo(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


        if (null!=project.getCategory_wallet()){
            for (TradingProjectDetaileBean.CategoryWalletBean wallet : project.getCategory_wallet()) {
                qianbaoData.add(wallet.getName());
            }
        }

        qianbao.setLayoutManager(new LinearLayoutManager(mContext));
        CommonAdapter<String> qianbaoAdapter = new CommonAdapter<String>(mContext, R.layout.project_wallet_expr_item, qianbaoData) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                if (position == (mDatas.size() - 1)) {
                    holder.getView(R.id.line).setVisibility(View.INVISIBLE);
                } else {
                    holder.getView(R.id.line).setVisibility(View.VISIBLE);
                }
                holder.setText(R.id.name, s);
            }
        };
        qianbaoEmpty=new EmptyWrapper(qianbaoAdapter);
        qianbaoEmpty.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.project_wallet_expr_empty,null,false));
        qianbao.setAdapter(qianbaoEmpty);
        qianbao.setNestedScrollingEnabled(false);

        qianbaoAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mActivity, ProjectNewsWebActivity.class);
                intent.putExtra("title", project.getCategory_wallet().get(position).getName());
                intent.putExtra("url", project.getCategory_wallet().get(position).getUrl());
                intent.putExtra("show", false);
                keepTogo(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        daibifenbufl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == project.getToken_holder() || project.getToken_holder().length() == 0) {
                    ToastUtil.show(getString(R.string.wutokenholder));
                    return;
                }

                Intent intent = new Intent(mActivity, ProjectNewsWebActivity.class);
                intent.putExtra("title", "TokenHolder");
                intent.putExtra("url", project.getToken_holder());
                intent.putExtra("show", false);
                keepTogo(intent);
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
