package com.inwecrypto.wallet.ui.news;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.TradingProjectDetaileBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.RatingBar;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.adapter.IcoProjectAdapter;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/2/9 15:23
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class NoTradingProjectActivity extends BaseActivity {


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
    @BindView(R.id.symble)
    TextView symble;
    @BindView(R.id.block_chain)
    TextView blockChain;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.charge)
    TextView charge;
    @BindView(R.id.no)
    TextView no;
    @BindView(R.id.ratingbar)
    RatingBar ratingbar;
    @BindView(R.id.fenshu)
    TextView fenshu;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.info)
    FrameLayout info;
    @BindView(R.id.ico_list)
    RecyclerView icoList;
    @BindView(R.id.mPieChart)
    PieChart mPieChart;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.chart)
    FrameLayout chart;
    @BindView(R.id.explore)
    LinearLayout explore;
    @BindView(R.id.wallet)
    LinearLayout wallet;

    private TradingProjectDetaileBean project;
    private IcoProjectAdapter adapter;

    private WebView mWebView = null;
    private final ReferenceQueue<WebView> WEB_VIEW_QUEUE = new ReferenceQueue<>();


    @Override
    protected void getBundleExtras(Bundle extras) {
        project = (TradingProjectDetaileBean) extras.getSerializable("project");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.no_trading_project_activity;
    }

    @Override
    protected void initView() {

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtMainTitle.setText(project.getUnit());

        txtRightTitle.setText(R.string.pingjia);
        txtRightTitle.setCompoundDrawables(null, null, null, null);

        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //输入密码
                FragmentManager fm = getSupportFragmentManager();
                ProjectStartFragment input = new ProjectStartFragment();
                Bundle bundle=new Bundle();
                if (null!=project.getCategory_user()&&null!=project.getCategory_user().getScore()){
                    bundle.putBoolean("isSb",false);
                    bundle.putString("num",project.getCategory_user().getScore());
                }else {
                    bundle.putBoolean("isSb",true);
                    bundle.putString("num","0.0");
                }
                input.setArguments(bundle);
                input.show(fm,"start");
                input.setOnNextListener(new ProjectStartFragment.OnNextInterface() {
                    @Override
                    public void onNext(final float fen, final Dialog dialog) {
                        ZixunApi.projectScore(this, project.getId(), fen, new JsonCallback<LzyResponse<Object>>() {
                                    @Override
                                    public void onSuccess(Response<LzyResponse<Object>> response) {
                                        ToastUtil.show(getString(R.string.pingfenchenggong));
                                        if (null==project.getCategory_user()){
                                            TradingProjectDetaileBean.CategoryUserBean categoryUserBean=new TradingProjectDetaileBean.CategoryUserBean();
                                            categoryUserBean.setScore(fen+"");
                                            project.setCategory_user(categoryUserBean);
                                        }else {
                                            project.getCategory_user().setScore(fen+"");
                                        }
                                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PINGLUN,fen+""));
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onError(Response<LzyResponse<Object>> response) {
                                        super.onError(response);
                                        ToastUtil.show(R.string.caozuoshibai);
                                    }
                                }
                        );
                    }
                });

            }
        });

        if (null != project.getImg()) {
            Glide.with(this)
                    .load(project.getImg())
                    .crossFade()
                    .into(img);
        }

        name.setText(project.getName());
        symble.setText("(" + project.getLong_name() + ")");
        blockChain.setText(project.getIndustry());
        if (null!=project.getCategory_score()){
            if (App.get().isZh()){
                no.setText("第" + project.getCategory_score().getSort() + "名");
            }else {
                no.setText("No." + project.getCategory_score().getSort());
            }
            ratingbar.setStar((float) project.getCategory_score().getValue());
            fenshu.setText(new BigDecimal(project.getCategory_score().getValue()).setScale(1, RoundingMode.HALF_UP).toPlainString()+"分");
        }else {
            ratingbar.setStar(0);
            fenshu.setText("0"+getString(R.string.fenshu));
        }

        explore.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View v) {
                if (null == project.getCategory_explorer()
                        ||project.getCategory_explorer().size()==0) {
                    ToastUtil.show(getString(R.string.wuliulanqi));
                    return;
                }
                //弹出选择框
                showExploreDialog();
            }
        });

        wallet.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View v) {
                if (null == project.getCategory_wallet()
                        ||project.getCategory_wallet().size()==0) {
                    ToastUtil.show(getString(R.string.wuqianbao));
                    return;
                }
                //弹出选择框
                showWalletDialog();
            }
        });

        if (null != project.getCategory_structure()) {
            //设置 ico 结构
            adapter = new IcoProjectAdapter(this, R.layout.ico_project_item, project.getCategory_structure());
            icoList.setAdapter(adapter);
            icoList.setLayoutManager(new LinearLayoutManager(this));
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
                });
            }

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mWebView.setLayoutParams(params);
            info.addView(mWebView);
            mWebView.loadData(project.getCategory_desc().getContent(), "text/html; charset=UTF-8", null);
        }

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
        ArrayList<String> names=new ArrayList<>();

        int i = 0;
        for (TradingProjectDetaileBean.CategoryStructureBean structureBean : project.getCategory_structure()) {
            entries.add(new Entry(structureBean.getPercentage(), i));
            colors.add(Color.parseColor(null==structureBean.getColor_value()?"#ffffff":structureBean.getColor_value()));
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


    @Override
    protected void initData() {

    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void showWalletDialog() {
        View selectPopupWin = LayoutInflater.from(this).inflate(R.layout.popup_bottom_layout, null, false);
        RecyclerView list = (RecyclerView) selectPopupWin.findViewById(R.id.list);

        final ArrayList<String> data = new ArrayList<>();
        for (TradingProjectDetaileBean.CategoryWalletBean wallet : project.getCategory_wallet()) {
            data.add(wallet.getName());
        }

        list.setLayoutManager(new LinearLayoutManager(this));
        CommonAdapter<String> walletAdapter = new CommonAdapter<String>(this, R.layout.popup_bottom_item, data) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                if (position == (mDatas.size() - 1)) {
                    holder.getView(R.id.line).setVisibility(View.INVISIBLE);
                } else {
                    holder.getView(R.id.line).setVisibility(View.VISIBLE);
                }
                holder.setText(R.id.title, s);
            }
        };
        list.setAdapter(walletAdapter);


        selectPopupWin.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = selectPopupWin.getMeasuredHeight();  //获取测量后的高度
        int[] location = new int[2];

        int width = (int) (ScreenUtils.getScreenWidth(this) / 2.1);

        final PopupWindow window = new PopupWindow(selectPopupWin, width, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.update();
        wallet.getLocationOnScreen(location);
        //这里就可自定义在上方和下方了 ，这种方式是为了确定在某个位置，某个控件的左边，右边，上边，下边都可以
        window.showAtLocation(wallet, Gravity.NO_GRAVITY, (int) (location[0] + (wallet.getWidth() - width) / 2), location[1] - popupHeight);
        walletAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mActivity,ProjectNewsWebActivity.class);
                intent.putExtra("title",project.getCategory_wallet().get(position).getName());
                intent.putExtra("url",project.getCategory_wallet().get(position).getUrl());
                intent.putExtra("show",false);
                keepTogo(intent);
                window.dismiss();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void showExploreDialog() {
        View selectPopupWin = LayoutInflater.from(this).inflate(R.layout.popup_bottom_layout, null, false);
        RecyclerView list = (RecyclerView) selectPopupWin.findViewById(R.id.list);

        final ArrayList<String> data = new ArrayList<>();
        for (TradingProjectDetaileBean.CategoryExplorerBean explorer : project.getCategory_explorer()) {
            data.add(explorer.getName());
        }

        list.setLayoutManager(new LinearLayoutManager(this));
        CommonAdapter<String> exploreAdapter = new CommonAdapter<String>(this, R.layout.popup_bottom_item, data) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                if (position == (mDatas.size() - 1)) {
                    holder.getView(R.id.line).setVisibility(View.INVISIBLE);
                } else {
                    holder.getView(R.id.line).setVisibility(View.VISIBLE);
                }
                holder.setText(R.id.title, s);
            }
        };
        list.setAdapter(exploreAdapter);

        selectPopupWin.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = selectPopupWin.getMeasuredHeight();  //获取测量后的高度
        int[] location = new int[2];

        int width = (int) (ScreenUtils.getScreenWidth(this) / 2.1);

        final PopupWindow window = new PopupWindow(selectPopupWin, width, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.update();
        explore.getLocationOnScreen(location);
        //这里就可自定义在上方和下方了 ，这种方式是为了确定在某个位置，某个控件的左边，右边，上边，下边都可以
        window.showAtLocation(explore, Gravity.NO_GRAVITY, (int) (location[0] + (explore.getWidth() - width) / 2), location[1] - popupHeight);

        exploreAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mActivity,ProjectNewsWebActivity.class);
                intent.putExtra("title",project.getCategory_explorer().get(position).getName());
                intent.putExtra("url",project.getCategory_explorer().get(position).getUrl());
                intent.putExtra("show",false);
                keepTogo(intent);
                window.dismiss();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
