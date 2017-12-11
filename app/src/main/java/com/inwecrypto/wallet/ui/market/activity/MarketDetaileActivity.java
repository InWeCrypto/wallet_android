package com.inwecrypto.wallet.ui.market.activity;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
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
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.KLBean;
import com.inwecrypto.wallet.bean.MarkeListBean;
import com.inwecrypto.wallet.bean.PriceBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.MarketApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
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
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class MarketDetaileActivity extends BaseActivity {


    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.usdPrice)
    TextView usdPrice;
    @BindView(R.id.cnyPrice)
    TextView cnyPrice;
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
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    private MarkeListBean market;
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

    private boolean isFirst=true;


    @Override
    protected void getBundleExtras(Bundle extras) {
        market = (MarkeListBean) extras.getSerializable("market");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.market_activity_detaile;
    }

    @Override
    protected void initView() {
        setLightMode(false);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }


    @Override
    protected void initData() {
        title.setText(market.getEn_name().toUpperCase());

        if (null != market.getTime_data()) {
            usdPrice.setText("$" + decimalFormat.format(Float.parseFloat(market.getTime_data().getPrice_usd())));
            cnyPrice.setText("¥" + decimalFormat.format(Float.parseFloat(market.getTime_data().getPrice_cny())));
            float liang = Float.parseFloat(market.getTime_data().getVolume_usd_24h());
            if (liang < 10000) {
                volume24.setText("$" +decimalFormat.format(liang));
            } else if (liang < 100000000) {
                volume24.setText("$" +decimalFormat.format(liang / 10000) + getString(R.string.wan));
            } else {
                volume24.setText("$" +decimalFormat.format(liang / 10000 / 10000) + getString(R.string.yi));
            }

            if (market.getTime_data().getChange_24h().contains("-")) {
                charge24.setText(market.getTime_data().getChange_24h() + "%");
                charge24.setTextColor(Color.parseColor("#e50370"));
            } else {
                charge24.setText("+" + market.getTime_data().getChange_24h() + "%");
                charge24.setTextColor(Color.parseColor("#74a700"));
            }
            up24.setText("$" +new BigDecimal(market.getTime_data().getMax_price_usd_24h()).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
            down24.setText("$" +new BigDecimal(market.getTime_data().getMin_price_usd_24h()).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        }

        getChart();
    }

    private void getChart() {
        //请求K线数据
        MarketApi.getMarketKLine(mActivity, market.getUnit(), type, new JsonCallback<LzyResponse<ArrayList<KLBean>>>() {

            @Override
            public void onStart(Request<LzyResponse<ArrayList<KLBean>>, ? extends Request> request) {
                super.onStart(request);
                OkGo.getInstance().cancelTag(mActivity);
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

                    if (isFirst){
                        isFirst=false;
                    }else {
                        mChartKline.invalidate();
                        mChartVolume.invalidate();
                    }

                    mChartKline.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (null!=stateRL){
                                stateRL.setVisibility(View.GONE);
                            }
                        }
                    },400);

                }
            }

            @Override
            public void onError(Response<LzyResponse<ArrayList<KLBean>>> response) {
                super.onError(response);
                stateRL.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
                noData.setText("数据加载失败!请点击重试!");
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

        });
    }


    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            //请求分时数据
            MarketApi.getCurrentPrice(mActivity, market.getUnit(), new JsonCallback<LzyResponse<PriceBean>>() {
                @Override
                public void onSuccess(final Response<LzyResponse<PriceBean>> response) {
                    if (null != response.body().data) {
                        usdPrice.setText("$" + decimalFormat.format(Float.parseFloat(response.body().data.getPrice())));
                        //cnyPrice.setText("¥" + decimalFormat.format(Float.parseFloat(market.getTime_data().getPrice_cny())));
//                        float liang = Float.parseFloat(response.body().data.getVolume());
//                        if (liang < 10000) {
//                            volume24.setText(decimalFormat.format(liang));
//                        } else if (liang < 100000000) {
//                            volume24.setText(decimalFormat.format(liang / 10000) + getString(R.string.wan));
//                        } else {
//                            volume24.setText(decimalFormat.format(liang / 10000 / 10000) + getString(R.string.yi));
//                        }

                        if (response.body().data.get_$24h_change().contains("-")) {
                            charge24.setText(response.body().data.get_$24h_change() + "%");
                            charge24.setTextColor(Color.parseColor("#e50370"));
                        } else {
                            charge24.setText("+" + response.body().data.get_$24h_change() + "%");
                            charge24.setTextColor(Color.parseColor("#74a700"));
                        }
                        up24.setText("$" +new BigDecimal(response.body().data.get_$24h_max_price()).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                        down24.setText("$" +new BigDecimal(response.body().data.get_$24h_min_price()).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                    }
                }
            });
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        if (timer == null) {
            timer = new Timer(true);
        } else {
            timer.schedule(task, 10000, 10000);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
        if (null != task) {
            task.cancel();
            task = null;
        }
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
        mChartKline.setBorderWidth(2);//边线宽度，单位dp
        mChartKline.setDragEnabled(true);//启用图表拖拽事件
        mChartKline.setScaleYEnabled(false);//启用Y轴上的缩放
        mChartKline.setAutoScaleMinMaxEnabled(true);
        mChartKline.setTouchEnabled(true);
        mChartKline.setBorderColor(Color.parseColor("#222629"));//边线颜色
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
                Long time = new Long(Long.parseLong(original) * 1000);
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
        mChartVolume.setBorderColor(Color.parseColor("#2c3033"));//边框颜色
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
}
