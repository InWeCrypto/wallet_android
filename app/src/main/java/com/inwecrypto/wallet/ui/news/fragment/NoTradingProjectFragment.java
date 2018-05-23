package com.inwecrypto.wallet.ui.news.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.TradingProjectDetaileBean;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.ProjectNewsWebActivity;
import com.inwecrypto.wallet.ui.news.adapter.IcoProjectAdapter;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：xiaoji06 on 2018/3/15 11:09
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class NoTradingProjectFragment extends BaseFragment {

    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.info)
    FrameLayout info;
    @BindView(R.id.ico_list)
    RecyclerView icoList;
    @BindView(R.id.mPieChart)
    PieChart mPieChart;
    @BindView(R.id.liulanqi)
    RecyclerView liulanqi;
    @BindView(R.id.qianbao)
    RecyclerView qianbao;

    private TradingProjectDetaileBean project;

    private ArrayList<String> liulanqiData = new ArrayList<>();

    private ArrayList<String> qianbaoData = new ArrayList<>();

    private EmptyWrapper liulanqiEmpty;

    private EmptyWrapper qianbaoEmpty;

    private IcoProjectAdapter adapter;

    private WebView mWebView = null;
    private final ReferenceQueue<WebView> WEB_VIEW_QUEUE = new ReferenceQueue<>();

    @Override
    protected int setLayoutID() {
        return R.layout.no_trading_project_fragment;
    }

    @Override
    protected void initView() {

        project = (TradingProjectDetaileBean) getArguments().getSerializable("project");

        if (null!=project.getCategory_desc()
                &&null!=project.getCategory_desc().getStart_at()
                &&null!=project.getCategory_desc().getEnd_at()){
            time.setText(project.getCategory_desc().getStart_at().split(" ")[0] + " - " + project.getCategory_desc().getEnd_at().split(" ")[0]);
        }

        if (null != project.getCategory_structure()) {
            //设置 ico 结构
            adapter = new IcoProjectAdapter(mContext, R.layout.ico_project_item, project.getCategory_structure());
            icoList.setAdapter(adapter);
            icoList.setLayoutManager(new LinearLayoutManager(mContext));
            icoList.setNestedScrollingEnabled(false);
            //图表
            initChart();
        }

        if (null != project.getCategory_desc()) {
            if (mWebView != null) {
                mWebView.removeAllViews();
                mWebView.destroy();
            } else {
                final WeakReference<WebView> webViewWeakReference =
                        new WeakReference<>(new WebView(mActivity.getApplicationContext()), WEB_VIEW_QUEUE);
                mWebView = webViewWeakReference.get();
                mWebView = AppUtil.createWebView(mWebView, mActivity);
                mWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                        handler.proceed();    //表示等待证书响应
                        //super.onReceivedSslError(view, handler, error);
                        // handler.cancel();      //表示挂起连接，为默认方式
                        // handler.handleMessage(null);    //可做其他处理
                    }
                });
            }

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mWebView.setLayoutParams(params);
            info.addView(mWebView);
            mWebView.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
            mWebView.loadData(project.getCategory_desc().getContent(), "text/html; charset=UTF-8", null);
        }


        if (null != project.getCategory_wallet()) {
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
        liulanqiEmpty = new EmptyWrapper(lilanqiAdapter);
        liulanqiEmpty.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.project_wallet_expr_empty, null, false));
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


        if (null != project.getCategory_wallet()) {
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
        qianbaoEmpty = new EmptyWrapper(qianbaoAdapter);
        qianbaoEmpty.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.project_wallet_expr_empty, null, false));
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
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

    private void initChart() {
        mPieChart.setUsePercentValues(true);

        mPieChart.setDescription("");  //设置描述信息

        mPieChart.setExtraOffsets(5, 10, 5, 5);  //设置间距

        mPieChart.setDragDecelerationFrictionCoef(0.95f);

        mPieChart.setCenterText("");  //设置饼状图中间文字，我需求里面并没有用到这个。。

        mPieChart.setDrawHoleEnabled(true);

        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.WHITE);

        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);

        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setTouchEnabled(false);  //设置是否响应点击触摸

        mPieChart.setDrawCenterText(true);  //设置是否绘制中心区域文字

        mPieChart.setRotationAngle(0); //设置旋转角度

        mPieChart.setRotationEnabled(true); //设置是否旋转

        mPieChart.setHighlightPerTapEnabled(false);  //设置是否高亮显示触摸的区域

        mPieChart.setData(getDefaultPieData());  //设置数据

        mPieChart.setDrawMarkerViews(false);  //设置是否绘制标记

        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);  //设置动画效果

        mPieChart.getLegend().setEnabled(false);
    }

    private PieData getDefaultPieData() {

        ArrayList<Entry> entries = new ArrayList<Entry>();
        ArrayList<Integer> colors = new ArrayList<>();  //控制不同绘制区域的颜色
        ArrayList<String> names = new ArrayList<>();

        int i = 0;
        for (TradingProjectDetaileBean.CategoryStructureBean structureBean : project.getCategory_structure()) {
            entries.add(new Entry(structureBean.getPercentage(), i));
            colors.add(Color.parseColor(null == structureBean.getColor_value() ? "#ffffff" : structureBean.getColor_value()));
            names.add(structureBean.getDesc());
            i++;
        }


        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setSliceSpace(0f);

        dataSet.setSelectionShift(5f);

        dataSet.setColors(colors);

        PieData data = new PieData(names, dataSet);

        data.setValueFormatter(new PercentFormatter());

        data.setValueTextSize(11f);

        data.setValueTextColor(Color.TRANSPARENT);

        mPieChart.highlightValues(null);

        mPieChart.setDrawMarkerViews(false);

        return data;
    }
}
