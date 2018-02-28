package com.inwecrypto.wallet.ui.market.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.KLBean;
import com.inwecrypto.wallet.bean.PriceBean;
import com.inwecrypto.wallet.bean.TradingProjectDetaileBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.MarketApi;
import com.inwecrypto.wallet.common.http.api.UserApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.common.widget.chart.CoupleChartGestureListener;
import com.inwecrypto.wallet.common.widget.chart.MyBottomMarkerView;
import com.inwecrypto.wallet.common.widget.chart.MyCombinedChart;
import com.inwecrypto.wallet.common.widget.chart.MyHMarkerView;
import com.inwecrypto.wallet.common.widget.chart.MyLeftMarkerView;
import com.inwecrypto.wallet.common.widget.chart.MyUtils;
import com.inwecrypto.wallet.common.widget.chart.VolFormatter;
import com.inwecrypto.wallet.common.widget.chart.bean.DataParse;
import com.inwecrypto.wallet.common.widget.chart.bean.KLineBean;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.me.activity.AddMarketTipFragment;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class MarketDetaileActivity extends BaseActivity {


    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.usdPrice)
    TextView usdPrice;
    @BindView(R.id.cnyPrice)
    TextView cnyPrice;
    @BindView(R.id.tip)
    ImageView tip;
    @BindView(R.id.charge24)
    TextView charge24;
    @BindView(R.id.volume24)
    TextView volume24;
    @BindView(R.id.up24)
    TextView up24;
    @BindView(R.id.down24)
    TextView down24;
    @BindView(R.id.rb_f1)
    RadioButton rbF1;
    @BindView(R.id.rb_f5)
    RadioButton rbF5;
    @BindView(R.id.rb_f15)
    RadioButton rbF15;
    @BindView(R.id.rb_f30)
    RadioButton rbF30;
    @BindView(R.id.rb_h1)
    RadioButton rbH1;
    @BindView(R.id.rb_d1)
    RadioButton rbD1;
    @BindView(R.id.rb_z1)
    RadioButton rbZ1;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.current_kai)
    TextView currentKai;
    @BindView(R.id.current_gao)
    TextView currentGao;
    @BindView(R.id.current_di)
    TextView currentDi;
    @BindView(R.id.current_shou)
    TextView currentShou;
    @BindView(R.id.current_liang)
    TextView currentLiang;
    @BindView(R.id.selectLL)
    LinearLayout selectLL;
    @BindView(R.id.kline_chart_k)
    MyCombinedChart mChartKline;
    @BindView(R.id.kline_chart_volume)
    MyCombinedChart mChartVolume;
    @BindView(R.id.ma5)
    TextView ma5;
    @BindView(R.id.ma10)
    TextView ma10;
    @BindView(R.id.ma30)
    TextView ma30;
    @BindView(R.id.maLL)
    LinearLayout maLL;
    @BindView(R.id.no_data)
    TextView noData;
    @BindView(R.id.progress)
    ProgressWheel progress;
    @BindView(R.id.stateRL)
    RelativeLayout stateRL;

    private String type = "1m";
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    //X轴标签的类
    protected XAxis xAxisKline, xAxisVolume;
    //Y轴左侧的线
    protected YAxis axisLeftKline, axisLeftVolume;
    //Y轴右侧的线
    protected YAxis axisRightKline, axisRightVolume;

    //K线图数据
    private ArrayList<KLineBean> kLineDatas;

    private Timer timer = new Timer(true);
    private DataParse mData;

    private SimpleDateFormat minFormat = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");

    private boolean isFirst = true;

    private TradingProjectDetaileBean project;

    private RotateAnimation rotate;
    private boolean isFinish = true;

    @Override
    protected void getBundleExtras(Bundle extras) {
        project = (TradingProjectDetaileBean) extras.getSerializable("project");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.market_activity_detaile;
    }

    @Override
    protected void initView() {

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Drawable drawable = getResources().getDrawable(R.mipmap.icon_refresh);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        txtRightTitle.setCompoundDrawables(drawable, null, null, null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimat();
                getChart();
            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                ViewPortHandler chartK = mChartKline.getViewPortHandler();
                chartK.setMinMaxScaleX(1, 1);

                switch (checkedId) {
                    case R.id.rb_f1:
                        if (type.equals("1m")) {
                            return;
                        }
                        type = "1m";
                        break;
                    case R.id.rb_f5:
                        if (type.equals("5m")) {
                            return;
                        }
                        type = "5m";
                        break;
                    case R.id.rb_f15:
                        if (type.equals("15m")) {
                            return;
                        }
                        type = "15m";
                        break;
                    case R.id.rb_f30:
                        if (type.equals("30m")) {
                            return;
                        }
                        type = "30m";
                        break;
                    case R.id.rb_h1:
                        if (type.equals("1h")) {
                            return;
                        }
                        type = "1h";
                        break;
                    case R.id.rb_d1:
                        if (type.equals("1d")) {
                            return;
                        }
                        type = "1d";
                        break;
                    case R.id.rb_z1:
                        if (type.equals("1w")) {
                            return;
                        }
                        type = "1w";
                        break;
                }
                getChart();
            }
        });

        initChartKline();
        initChartVolume();
        setChartListener();

        if (null!=project.getCategory_user()){
            if (project.getCategory_user().isIs_market_follow()){
                tip.setImageResource(R.mipmap.shishihangqing_xiaoxi);
            }else {
                tip.setImageResource(R.mipmap.shishihangqing_xiaoxihui);
            }
        }else {
            tip.setImageResource(R.mipmap.shishihangqing_xiaoxihui);
        }


        tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                final AddMarketTipFragment improt = new AddMarketTipFragment();
                Bundle bundle = new Bundle();
                bundle.putString("price", null==project.getIco().getPrice_usd()?"0.00":project.getIco().getPrice_usd());
                bundle.putString("high",null==project.getCategory_user().getMarket_hige()?"0":project.getCategory_user().getMarket_hige());
                bundle.putString("low",null==project.getCategory_user().getMarket_lost()?"0":project.getCategory_user().getMarket_lost());
                improt.setArguments(bundle);
                improt.show(fm, "tip");
                improt.setOnNextListener(new AddMarketTipFragment.OnNextInterface() {
                    @Override
                    public void onNext(String hight, String low, Dialog dialog) {
                        showFixLoading();
                        UserApi.follow(mActivity
                                , project.getId()+""
                                , true
                                , hight
                                , low
                                , new JsonCallback<LzyResponse<Object>>() {
                                    @Override
                                    public void onSuccess(Response<LzyResponse<Object>> response) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                tip.setImageResource(R.mipmap.shishihangqing_xiaoxi);
                                                improt.dismiss();
                                                ToastUtil.show(getString(R.string.tianjiatixingchenggong));
                                                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_TIP));
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(Response<LzyResponse<Object>> response) {
                                        super.onError(response);
                                        ToastUtil.show(getString(R.string.tianjiatixingshibai));
                                    }

                                    @Override
                                    public void onFinish() {
                                        super.onFinish();
                                        hideFixLoading();
                                    }
                                });
                    }
                });
            }
        });
    }


    @Override
    protected void initData() {
        txtMainTitle.setText(project.getUnit());

        getChart();
    }

    private void getChart() {
        OkGo.getInstance().cancelTag(mActivity);
        //请求K线数据
        MarketApi.getMarketKLine(mActivity, project.getUnit(), type, new JsonCallback<LzyResponse<ArrayList<KLBean>>>() {

            @Override
            public void onStart(Request<LzyResponse<ArrayList<KLBean>>, ? extends Request> request) {
                super.onStart(request);
                stateRL.setVisibility(View.VISIBLE);
                progress.setVisibility(View.VISIBLE);
                progress.spin();
                noData.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(Response<LzyResponse<ArrayList<KLBean>>> response) {
                if (null != response) {
                    if (null == response.body().data || response.body().data.size() == 0) {
                        stateRL.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                        noData.setText(R.string.zanwushuju);
                        noData.setOnClickListener(null);
                        return;
                    }

                    mData = new DataParse();
                    mData.parseKLine(response.body().data);
                    kLineDatas = mData.getKLineDatas();
                    mData.initLineDatas(kLineDatas);

                    setMarkerViewButtom(mData, mChartKline);
                    setMarkerView(mData, mChartVolume);

                    setKLineByChart(mChartKline);
                    setVolumeByChart(mChartVolume);

                    mChartKline.moveViewToX(kLineDatas.size() - 1);
                    mChartVolume.moveViewToX(kLineDatas.size() - 1);

                    if (isFirst) {
                        isFirst = false;
                    } else {
                        mChartKline.invalidate();
                        mChartVolume.invalidate();
                    }

                    mChartKline.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (null != stateRL) {
                                stateRL.setVisibility(View.GONE);
                            }
                        }
                    }, 400);

                }
            }

            @Override
            public void onError(Response<LzyResponse<ArrayList<KLBean>>> response) {
                super.onError(response);
                stateRL.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
                noData.setText(R.string.shujujiazaishibaiqingdianjichongshi);
                noData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getChart();
                    }
                });
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<ArrayList<KLBean>>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                stopAnimat();
            }
        });

        MarketApi.getCurrentPrice(this, project.getUnit(), new JsonCallback<LzyResponse<PriceBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<PriceBean>> response) {
                if (null != response.body().data) {
                    PriceBean priceBean = response.body().data;
                    usdPrice.setText("$" + decimalFormat.format(Float.parseFloat(priceBean.getPrice())));
                    cnyPrice.setText("¥" + decimalFormat.format(Float.parseFloat(priceBean.getPrice_cny())));

                    float liang = App.get().isZh() ? Float.parseFloat(priceBean.getVolume_cny()) : Float.parseFloat(priceBean.getVolume());

                    String unit = App.get().isZh() ? "¥" : "$";

                    if (liang < 10000) {
                        volume24.setText(unit + decimalFormat.format(liang));
                    } else if (liang < 100000000) {
                        volume24.setText(unit + decimalFormat.format(liang / 10000) + getString(R.string.wan));
                    } else {
                        volume24.setText(unit + decimalFormat.format(liang / 10000 / 10000) + getString(R.string.yi));
                    }

                    if (priceBean.get_$24h_change_cny().contains("-")) {
                        charge24.setText(new BigDecimal(App.get().isZh() ? priceBean.get_$24h_change_cny() : priceBean.get_$24h_change()).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%");
                        charge24.setTextColor(Color.parseColor("#e50370"));
                    } else {
                        charge24.setText("+" + new BigDecimal(App.get().isZh() ? priceBean.get_$24h_change_cny() : priceBean.get_$24h_change()).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%");
                        charge24.setTextColor(Color.parseColor("#74a700"));
                    }
                    up24.setText(unit + new BigDecimal(App.get().isZh() ? priceBean.get_$24h_max_price_cny() : priceBean.get_$24h_max_price()).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                    down24.setText(unit + new BigDecimal(App.get().isZh() ? priceBean.get_$24h_min_price_cny() : priceBean.get_$24h_min_price()).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                }
            }
        });
    }


//    TimerTask task = new TimerTask() {
//
//        @Override
//        public void run() {
//            //请求分时数据
//            MarketApi.getCurrentPrice(mActivity, market.getUnit(), new JsonCallback<LzyResponse<PriceBean>>() {
//                @Override
//                public void onSuccess(final Response<LzyResponse<PriceBean>> response) {
//                    if (null != response.body().data) {
//                        usdPrice.setText("$" + decimalFormat.format(Float.parseFloat(response.body().data.getPrice())));
//                        //cnyPrice.setText("¥" + decimalFormat.format(Float.parseFloat(market.getTime_data().getPrice_cny())));
////                        float liang = Float.parseFloat(response.body().data.getVolume());
////                        if (liang < 10000) {
////                            volume24.setText(decimalFormat.format(liang));
////                        } else if (liang < 100000000) {
////                            volume24.setText(decimalFormat.format(liang / 10000) + getString(R.string.wan));
////                        } else {
////                            volume24.setText(decimalFormat.format(liang / 10000 / 10000) + getString(R.string.yi));
////                        }
//
//                        if (response.body().data.get_$24h_change().contains("-")) {
//                            charge24.setText(new BigDecimal(response.body().data.get_$24h_change()).setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString() + "%");
//                            charge24.setTextColor(Color.parseColor("#e50370"));
//                        } else {
//                            charge24.setText("+" + new BigDecimal(response.body().data.get_$24h_change()).setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString() + "%");
//                            charge24.setTextColor(Color.parseColor("#74a700"));
//                        }
//                        up24.setText("$" +new BigDecimal(response.body().data.get_$24h_max_price()).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
//                        down24.setText("$" +new BigDecimal(response.body().data.get_$24h_min_price()).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
//                    }
//                }
//            });
//        }
//    };


    @Override
    protected void onStart() {
        super.onStart();
//        if (timer == null) {
//            timer = new Timer(true);
//        } else {
//            timer.schedule(task, 10000, 10000);
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (null != timer) {
//            timer.cancel();
//            timer = null;
//        }
//        if (null != task) {
//            task.cancel();
//            task = null;
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

    /**
     * 初始化上面的chart公共属性
     */
    private void initChartKline() {
        mChartKline.setDrawBorders(true);//是否绘制边线
        mChartKline.setBorderWidth(1);//边线宽度，单位dp
        mChartKline.setDragEnabled(true);//启用图表拖拽事件
        mChartKline.setScaleYEnabled(false);//启用Y轴上的缩放
        mChartKline.setAutoScaleMinMaxEnabled(true);
        mChartKline.setTouchEnabled(true);
        mChartKline.setBorderColor(Color.parseColor("#eeeeee"));//边线颜色
        mChartKline.setDescription("");//右下角对图表的描述信息
        mChartKline.setMinOffset(0f);
        mChartKline.setExtraOffsets(0f, 0f, 0f, 3f);
        mChartKline.setPinchZoom(true);
        mChartKline.setDoubleTapToZoomEnabled(false);
        mChartKline.setHighlightPerTapEnabled(false);
        mChartKline.setHighlightPerDragEnabled(false);

        mChartKline.setHightLightSelect(new MyCombinedChart.OnHightLightSelect() {
            @Override
            public void isHightLightShow(boolean isShow) {
                if (isShow) {
                    selectLL.setVisibility(View.VISIBLE);
                    maLL.setVisibility(View.VISIBLE);
                } else {
                    selectLL.setVisibility(View.GONE);
                    maLL.setVisibility(View.GONE);
                }
            }
        });


        Legend lineChartLegend = mChartKline.getLegend();
        lineChartLegend.setEnabled(false);//是否绘制 Legend 图例
        lineChartLegend.setForm(Legend.LegendForm.CIRCLE);

        //bar x y轴
        xAxisKline = mChartKline.getXAxis();
        xAxisKline.setDrawLabels(true); //是否显示X坐标轴上的刻度，默认是true
        xAxisKline.setDrawGridLines(true);//是否显示X坐标轴上的刻度竖线，默认是true
        xAxisKline.setDrawAxisLine(false); //是否绘制坐标轴的线，即含有坐标的那条线，默认是true
        xAxisKline.enableGridDashedLine(10f, 10f, 0f);//虚线表示X轴上的刻度竖线(float lineLength, float spaceLength, float phase)三个参数，1.线长，2.虚线间距，3.虚线开始坐标
        xAxisKline.setTextColor(Color.parseColor("#8a8d8e"));//设置字的颜色
        xAxisKline.setPosition(XAxis.XAxisPosition.BOTTOM);//设置值显示在什么位置
        xAxisKline.setAvoidFirstLastClipping(true);//设置首尾的值是否自动调整，避免被遮挡
        xAxisKline.setValueFormatter(new XAxisValueFormatter() {
            @Override
            public String getXValue(String original, int index, ViewPortHandler viewPortHandler) {
                Long time = Long.parseLong(original);
                String d = "";
                if (type.equals("1d") || type.equals("1w")) {
                    d = dayFormat.format(time);
                } else {
                    d = minFormat.format(time);
                }
                return d;
            }
        });

        axisLeftKline = mChartKline.getAxisLeft();
        axisLeftKline.setDrawGridLines(true);
        axisLeftKline.setDrawAxisLine(false);
        axisLeftKline.setDrawZeroLine(false);
        axisLeftKline.setDrawLabels(true);
        axisLeftKline.enableGridDashedLine(10f, 10f, 0f);
        axisLeftKline.setTextColor(Color.parseColor("#8a8d8e"));
//      axisLeftKline.setGridColor(getResources().getColor(R.color.minute_grayLine));
        axisLeftKline.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisLeftKline.setLabelCount(4, false); //第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
        axisLeftKline.setSpaceTop(10);//距离顶部留白

        axisRightKline = mChartKline.getAxisRight();
        axisRightKline.setDrawLabels(false);
        axisRightKline.setDrawGridLines(false);
        axisRightKline.setDrawAxisLine(false);

        mChartKline.setDragDecelerationEnabled(true);
        mChartKline.setDragDecelerationFrictionCoef(0.5f);

        mChartKline.animateXY(1000, 1000);
    }

    /**
     * 初始化下面的chart公共属性
     */
    private void initChartVolume() {
        mChartVolume.setDrawBorders(true);  //边框是否显示
        mChartVolume.setBorderWidth(1);//边框的宽度，float类型，dp单位
        mChartVolume.setBorderColor(Color.parseColor("#eeeeee"));//边框颜色
        mChartVolume.setDescription(""); //图表默认右下方的描述，参数是String对象
        mChartVolume.setDragEnabled(true);// 是否可以拖拽
        mChartVolume.setScaleYEnabled(false); //是否可以缩放 仅y轴
        mChartVolume.setAutoScaleMinMaxEnabled(true);
        mChartVolume.setMinOffset(3f);
        mChartVolume.setTouchEnabled(true);
        mChartVolume.setExtraOffsets(0f, 5f, 0f, 5f);
        mChartVolume.setPinchZoom(true);
        mChartVolume.setDoubleTapToZoomEnabled(false);
        mChartVolume.setHighlightPerTapEnabled(false);
        mChartVolume.setHighlightPerDragEnabled(false);

        Legend combinedchartLegend = mChartVolume.getLegend(); // 设置比例图标示，就是那个一组y的value的
        combinedchartLegend.setEnabled(false);//是否绘制比例图

        //bar x y轴
        xAxisVolume = mChartVolume.getXAxis();
        xAxisVolume.setEnabled(false);

        axisLeftVolume = mChartVolume.getAxisLeft();
        axisLeftVolume.setAxisMinValue(0);//设置Y轴坐标最小为多少
        axisLeftVolume.setDrawGridLines(true);
        axisLeftVolume.setDrawAxisLine(false);
        axisLeftVolume.setDrawLabels(true);
        axisLeftVolume.enableGridDashedLine(10f, 10f, 0f);
        axisLeftVolume.setTextColor(Color.parseColor("#8a8d8e"));
        axisLeftVolume.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisLeftVolume.setShowOnlyMinMax(true);
        //axisLeftVolume.setLabelCount(1, false); //第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
        axisLeftVolume.setSpaceTop(4);//距离顶部留白

        axisRightVolume = mChartVolume.getAxisRight();
        axisRightVolume.setDrawLabels(false);
        axisRightVolume.setDrawGridLines(false);
        axisRightVolume.setDrawAxisLine(false);

        mChartVolume.setDragDecelerationEnabled(true);
        mChartVolume.setDragDecelerationFrictionCoef(0.5f);

        mChartVolume.animateXY(1000, 1000);
    }

    private void setChartListener() {
        // 将K线控的滑动事件传递给交易量控件
        mChartKline.setOnChartGestureListener(new CoupleChartGestureListener(mChartKline, new Chart[]{mChartVolume}));
        // 将交易量控件的滑动事件传递给K线控件
        mChartVolume.setOnChartGestureListener(new CoupleChartGestureListener(mChartVolume, new Chart[]{mChartKline}));

        mChartKline.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                Highlight highlight = new Highlight(h.getXIndex(), h.getValue(), h.getDataIndex(), h.getDataSetIndex());

                float touchY = h.getTouchY() - mChartKline.getHeight();
                Highlight h1 = mChartVolume.getHighlightByTouchPoint(h.getXIndex(), touchY);
                highlight.setTouchY(touchY);
                if (null == h1) {
                    highlight.setTouchYValue(0);
                } else {
                    highlight.setTouchYValue(h1.getTouchYValue());
                }
                mChartVolume.highlightValues(new Highlight[]{highlight});
                updateText(e.getXIndex());
            }

            @Override
            public void onNothingSelected() {
                mChartVolume.highlightValue(null);
            }
        });

        mChartVolume.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Highlight highlight = new Highlight(h.getXIndex(), h.getValue(), h.getDataIndex(), h.getDataSetIndex());
                float touchY = h.getTouchY() + mChartKline.getHeight();
                Highlight h1 = mChartKline.getHighlightByTouchPoint(h.getXIndex(), touchY);
                highlight.setTouchY(touchY);
                if (null == h1) {
                    highlight.setTouchYValue(0);
                } else {
                    highlight.setTouchYValue(h1.getTouchYValue());
                }
                mChartKline.highlightValues(new Highlight[]{highlight});
                updateText(e.getXIndex());
            }

            @Override
            public void onNothingSelected() {
                mChartKline.highlightValue(null);
            }
        });
    }


    private void setMarkerViewButtom(DataParse mData, MyCombinedChart combinedChart) {
        MyLeftMarkerView leftMarkerView = new MyLeftMarkerView(this, R.layout.mymarkerview);
        MyHMarkerView hMarkerView = new MyHMarkerView(this, R.layout.mymarkerview_line);
        MyBottomMarkerView bottomMarkerView = new MyBottomMarkerView(this, R.layout.mymarkerview);
        combinedChart.setMarker(leftMarkerView, bottomMarkerView, hMarkerView, mData);
    }

    private void setMarkerView(DataParse mData, MyCombinedChart combinedChart) {
        MyLeftMarkerView leftMarkerView = new MyLeftMarkerView(this, R.layout.mymarkerview);
        MyHMarkerView hMarkerView = new MyHMarkerView(this, R.layout.mymarkerview_line);
        combinedChart.setMarker(leftMarkerView, hMarkerView, mData);
    }

    private void setKLineByChart(MyCombinedChart combinedChart) {
        CandleDataSet set = new CandleDataSet(mData.getCandleEntries(), "");
        set.setHighlightEnabled(true);
        set.setDrawHorizontalHighlightIndicator(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setShadowWidth(1f);
        set.setValueTextSize(10f);
        set.setDecreasingColor(Color.parseColor("#e50370"));//设置开盘价高于收盘价的颜色
        set.setDecreasingPaintStyle(Paint.Style.FILL);
        set.setIncreasingColor(Color.parseColor("#74a700"));//设置开盘价地狱收盘价的颜色
        set.setIncreasingPaintStyle(Paint.Style.FILL);
        set.setNeutralColor(Color.parseColor("#74a700"));//设置开盘价等于收盘价的颜色
        set.setShadowColorSameAsCandle(true);
        set.setHighlightLineWidth(1f);
        set.setHighLightColor(Color.parseColor("#717f8c"));
        set.setDrawValues(true);
        set.setValueTextColor(Color.parseColor("#717f8c"));

        CandleData candleData = new CandleData(mData.getXVals(), set);
        mData.initKLineMA(kLineDatas);
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        /******此处修复如果显示的点的个数达不到MA均线的位置所有的点都从0开始计算最小值的问题******************************/
        sets.add(setMaLine(5, mData.getXVals(), mData.getMa5DataL()));
        sets.add(setMaLine(10, mData.getXVals(), mData.getMa10DataL()));
        sets.add(setMaLine(30, mData.getXVals(), mData.getMa30DataL()));

        LineData lineData = new LineData(mData.getXVals(), sets);

        CombinedData combinedData = new CombinedData(mData.getXVals());
        combinedData.setData(lineData);
        combinedData.setData(candleData);
        combinedChart.setData(combinedData);

        setHandler(combinedChart);
    }

    private void setVolumeByChart(MyCombinedChart combinedChart) {
        String unit = MyUtils.getVolUnit(mData.getVolmax());
        String wan = getString(R.string.wan);
        String yi = getString(R.string.yi);
        int u = 1;
        if (wan.equals(unit)) {
            u = 4;
        } else if (yi.equals(unit)) {
            u = 8;
        }
        combinedChart.getAxisLeft().setValueFormatter(new VolFormatter((int) Math.pow(10, u)));

        BarDataSet set = new BarDataSet(mData.getBarEntries(), "成交量");
        set.setBarSpacePercent(20); //bar空隙
        set.setHighlightEnabled(true);
        set.setHighLightAlpha(255);
        set.setHighLightColor(Color.parseColor("#717f8c"));
        set.setDrawValues(false);


        List<Integer> list = new ArrayList<>();
        list.add(Color.parseColor("#74a700"));
        list.add(Color.parseColor("#e50370"));
        set.setColors(list);
        BarData barData = new BarData(mData.getXVals(), set);

        CombinedData combinedData = new CombinedData(mData.getXVals());
        combinedData.setData(barData);
        combinedChart.setData(combinedData);

        setHandler(combinedChart);
    }


    private void setHandler(MyCombinedChart combinedChart) {
        final ViewPortHandler viewPortHandlerBar = combinedChart.getViewPortHandler();
        float xscale = culcMaxscale(mData.getXVals().size());
        viewPortHandlerBar.setMaximumScaleX(xscale);
        Matrix touchmatrix = viewPortHandlerBar.getMatrixTouch();
        touchmatrix.postScale(xscale, 1f);
    }


    private float culcMaxscale(float count) {
        float max = 1;
        max = count / 50;
        return max;
    }

    @NonNull
    private LineDataSet setMaLine(int ma, ArrayList<String> xVals, ArrayList<Entry> lineEntries) {
        LineDataSet lineDataSetMa = new LineDataSet(lineEntries, "ma" + ma);
        if (ma == 5) {
            lineDataSetMa.setHighlightEnabled(true);
            lineDataSetMa.setDrawHorizontalHighlightIndicator(false);
            lineDataSetMa.setHighLightColor(Color.parseColor("#717f8c"));
        } else {/*此处必须得写*/
            lineDataSetMa.setHighlightEnabled(false);
        }
        lineDataSetMa.setDrawValues(false);
        if (ma == 5) {
            lineDataSetMa.setColor(Color.parseColor("#20acea"));
        } else if (ma == 10) {
            lineDataSetMa.setColor(Color.parseColor("#f0c706"));
        } else if (ma == 30) {
            lineDataSetMa.setColor(Color.parseColor("#ff79d4"));
        }
        lineDataSetMa.setLineWidth(1f);
        lineDataSetMa.setDrawCircles(false);
        lineDataSetMa.setAxisDependency(YAxis.AxisDependency.LEFT);

        lineDataSetMa.setHighlightEnabled(false);
        return lineDataSetMa;
    }

    private void updateText(int index) {
        if (index >= 0 && index < kLineDatas.size()) {
            KLineBean klData = kLineDatas.get(index);
            currentKai.setText(MyUtils.getDecimalFormatVol(klData.open));
            currentShou.setText(MyUtils.getDecimalFormatVol(klData.close));
            currentGao.setText(MyUtils.getDecimalFormatVol(klData.high));
            currentDi.setText(MyUtils.getDecimalFormatVol(klData.low));

            int unit = MyUtils.getVolUnitNum(klData.vol);
            currentLiang.setText(MyUtils.getVolUnitText((int) Math.pow(10, unit), klData.vol));
        }

        int newIndex = index;
        if (null != mData.getMa5DataL() && mData.getMa5DataL().size() > 0) {
            if (newIndex >= 0 && newIndex < mData.getMa5DataL().size())
                ma5.setText("MA5" + MyUtils.getDecimalFormatVol(mData.getMa5DataL().get(newIndex).getVal()));
        }
        if (null != mData.getMa10DataL() && mData.getMa10DataL().size() > 0) {
            if (newIndex >= 0 && newIndex < mData.getMa10DataL().size())
                ma10.setText("MA10" + MyUtils.getDecimalFormatVol(mData.getMa10DataL().get(newIndex).getVal()));
        }
        if (null != mData.getMa30DataL() && mData.getMa30DataL().size() > 0) {
            if (newIndex >= 0 && newIndex < mData.getMa30DataL().size())
                ma30.setText("MA30" + MyUtils.getDecimalFormatVol(mData.getMa30DataL().get(newIndex).getVal()));
        }
    }

    public void startAnimat() {
        if (txtRightTitle == null) {
            return;
        }
        isFinish = false;
        txtRightTitle.clearAnimation();

        rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setDuration(1600);//设置动画持续周期
        rotate.setRepeatCount(-1);//设置重复次数
        rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        rotate.setStartOffset(10);//执行前的等待时间
        txtRightTitle.setAnimation(rotate);
    }

    public void stopAnimat() {
        if (!isFinish) {
            txtRightTitle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (rotate != null) {
                        rotate.cancel();
                    }
                    if (txtRightTitle != null) {
                        txtRightTitle.clearAnimation();
                    }
                    isFinish = true;
                }
            }, 1600);
        }

    }
}
