package capital.fbg.wallet.ui.market.activity;

import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import capital.fbg.wallet.R;
import capital.fbg.wallet.base.BaseActivity;
import capital.fbg.wallet.bean.MarkeListBean;
import capital.fbg.wallet.common.http.LzyResponse;
import capital.fbg.wallet.common.http.api.MarketApi;
import capital.fbg.wallet.common.http.callback.JsonCallback;
import capital.fbg.wallet.common.widget.MyLineChart;
import capital.fbg.wallet.common.widget.MyMarkeView;
import capital.fbg.wallet.common.widget.SwipeRefreshLayoutCompat;
import capital.fbg.wallet.event.BaseEventBusBean;
import capital.fbg.wallet.ui.market.adapter.MarketChartBean;

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
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_net)
    TextView tvNet;
    @BindView(R.id.tv_us_price)
    TextView tvUsPrice;
    @BindView(R.id.tv_cn_price)
    TextView tvCnPrice;
    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_hight)
    TextView tvHight;
    @BindView(R.id.tv_low)
    TextView tvLow;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.chart)
    MyLineChart chart;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayoutCompat swipeRefresh;
    @BindView(R.id.no_data)
    TextView noData;
    @BindView(R.id.progress)
    ProgressWheel progress;
    @BindView(R.id.staterl)
    RelativeLayout staterl;

    private MarkeListBean.DataBean market;
    private int type = 1;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private SimpleDateFormat shiFormat = new SimpleDateFormat("HH:mm");

    private SimpleDateFormat dayFormat = new SimpleDateFormat("MM-dd");

    private Timer timer = new Timer(true);

    private boolean isChace;

    private int index;

    ArrayList<MarketChartBean.ListBean> data = new ArrayList<>();

    @Override
    protected void getBundleExtras(Bundle extras) {
        market = (MarkeListBean.DataBean) extras.getSerializable("market");
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
        txtMainTitle.setText(R.string.market_detail);
        txtRightTitle.setVisibility(View.GONE);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.fen:
                        type = 1;
                        break;
                    case R.id.shi:
                        type = 2;
                        break;
                    case R.id.tian:
                        type = 3;
                        break;
                    case R.id.zhou:
                        type = 4;
                        break;
                }
                staterl.setVisibility(View.VISIBLE);
                noData.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                getChart();
            }
        });

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getChart();
            }
        });

        chart.setView(swipeRefresh);
        chart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        swipeRefresh.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        swipeRefresh.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        initChart();
    }

    private void initChart() {
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(true);
        chart.setHighlightPerDragEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.animateXY(1000, 1000);
        Description d = new Description();
        d.setText("");
        d.setTextColor(Color.WHITE);
        chart.setDescription(d);

        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        xAxis.setTextSize(10f);//设置字体
        xAxis.setTextColor(Color.WHITE);//设置字体颜色
        xAxis.setDrawAxisLine(true);//是否绘制轴线
        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        // xAxis.setAvoidFirstLastClipping(true);//图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘
//        设置x轴显示标签数量  还有一个重载方法第二个参数为布尔值强制设置数量 如果启用会导致绘制点出现偏差
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(6,false);
        xAxis.setAxisLineColor(Color.WHITE);//设置x轴线颜色
        xAxis.setAxisLineWidth(0.5f);//设置x轴线宽度
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(
                new IAxisValueFormatter(){
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        if (data.size()==0){
                            return "";
                        }
                        String lab = "";
                        switch (type) {
                            case 1:
                                lab = shiFormat.format(new Date(data.get((int) value % data.size()).getTimestamp()* 1000));
                                break;
                            case 2:
                                lab = shiFormat.format(new Date(data.get((int) value % data.size()).getTimestamp()* 1000));
                                break;
                            case 3:
                                lab = dayFormat.format(new Date(data.get((int) value % data.size()).getTimestamp()* 1000));
                                break;
                            case 4:
                                lab = dayFormat.format(new Date(data.get((int) value % data.size()).getTimestamp()* 1000));
                                break;
                        }
                        return lab;
                    }

                    @Override
                    public int getDecimalDigits() {
                        return 0;
                    }
                }
        );

        //获取右边的轴线
        YAxis rightAxis = chart.getAxisRight();
        //设置图表右边的y轴禁用
        rightAxis.setEnabled(false);
        //获取左边的轴线
        YAxis leftAxis = chart.getAxisLeft();
//        //设置网格线为虚线效果
//        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        //是否绘制0所在的网格线
        leftAxis.setEnabled(true);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawAxisLine(true);//是否绘制轴线
//        xAxis.setDrawGridLines(true);//设置x轴上每个点对应的线
        leftAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        leftAxis.setTextSize(10f);//设置字体
        leftAxis.setTextColor(Color.WHITE);//设置字体颜色
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisLineColor(Color.WHITE);//设置x轴线颜色
        leftAxis.setAxisLineWidth(0.5f);//设置x轴线宽度
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        IMarker marker = new MyMarkeView(this, R.layout.chart_hit_view);
        chart.setMarker(marker);
    }

    @Override
    protected void initData() {

        tvName.setText(market.getName());
        tvNet.setText(market.getSource());
        if (null != market.getRelationCap()) {
            tvUsPrice.setText("$" + decimalFormat.format(Float.parseFloat(market.getRelationCap().getPrice_usd())));
            tvCnPrice.setText("¥" + decimalFormat.format(Float.parseFloat(market.getRelationCap().getPrice_cny())));
            float liang = Float.parseFloat(market.getRelationCap().getVolume_cny_24h());
            if (liang < 10000) {
                tvAmount.setText(decimalFormat.format(liang));
            } else if (liang < 100000000) {
                tvAmount.setText(decimalFormat.format(liang / 10000) + "万");
            } else {
                tvAmount.setText(decimalFormat.format(liang / 10000 / 10000) + "亿");
            }

            if (market.getRelationCap().getPercent_change_24h().contains("-")) {
                tvRate.setText(market.getRelationCap().getPercent_change_24h() + "%");
                tvRate.setBackgroundColor(Color.parseColor("#fe5864"));
            } else {
                tvRate.setText("+" + market.getRelationCap().getPercent_change_24h() + "%");
                tvRate.setBackgroundColor(Color.parseColor("#66b7fb"));
            }
        }

        if (null != market.getRelationCapMin()) {
            tvStart.setText("¥" + decimalFormat.format(Float.parseFloat(market.getRelationCapMin().getPrice_cny_first())));
            tvHight.setText("¥" + decimalFormat.format(Float.parseFloat(market.getRelationCapMin().getPrice_cny_high())));
            tvLow.setText("¥" + decimalFormat.format(Float.parseFloat(market.getRelationCapMin().getPrice_cny_low())));
        }

        getChart();
    }

    private void getChart() {
        //请求分时数据
        MarketApi.marketDetail(mActivity, market.getId(), type, new JsonCallback<LzyResponse<MarketChartBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<MarketChartBean>> response) {
                if (!isChace) {
                    setUpUi(response.body().data.getRecord());
                }
                data.clear();
                data.addAll(response.body().data.getList());
                if (data.size() == 0) {
                    staterl.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.VISIBLE);
                    noData.setText("暂无数据");
                    progress.setVisibility(View.GONE);
                    return;
                } else {
                    staterl.setVisibility(View.GONE);
                }
                setChartData();
            }

            @Override
            public void onError(Response<LzyResponse<MarketChartBean>> response) {
                super.onError(response);
                staterl.setVisibility(View.VISIBLE);
                noData.setVisibility(View.VISIBLE);
                noData.setText("加载失败");
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<MarketChartBean>> response) {
                super.onCacheSuccess(response);
                isChace = true;
                onSuccess(response);
                isChace = false;
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (null!=swipeRefresh){
                    swipeRefresh.setRefreshing(false);
                }
            }
        });
    }

    private void setChartData() {
        chart.clear();
        chart.resetTracking();
        chart.resetViewPortOffsets();
        chart.setScaleMinima(1.0f, 1.0f);
        chart.getViewPortHandler().refresh(new Matrix(), chart, true);

        ArrayList<Entry> yVals = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            yVals.add(new Entry(i, new BigDecimal(data.get(i).getPrice_cny_last()).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()));
        }
        if (yVals.size() == 0) {
            chart.setVisibility(View.INVISIBLE);
        } else {
            chart.setVisibility(View.VISIBLE);
        }
        LineDataSet set;
//        //判断图表中原来是否有数据
//        if (chart.getData() != null &&
//                chart.getData().getDataSetCount() > 0) {
//            //获取数据
//            set = (LineDataSet) chart.getData().getDataSetByIndex(0);
//            set.setValues(yVals);
//            //刷新数据
//            chart.getData().notifyDataChanged();
//            chart.notifyDataSetChanged();
//            chart.invalidate();
//        } else {
        //设置数据1  参数1：数据源 参数2：图例名称
        set = new LineDataSet(yVals, "价格走势");
        set.setColor(Color.parseColor("#fdd930"));
        set.setValueTextColor(Color.parseColor("#fdd930"));
        set.setCircleColorHole(Color.parseColor("#fdd930"));
        set.setCircleColor(Color.parseColor("#fdd930"));
        set.setDrawCircles(false);
        set.setLineWidth(1f);//设置线宽
        set.setCircleRadius(3f);//设置焦点圆心的大小
        set.setHighlightEnabled(true);
        set.enableDashedHighlightLine(10f, 0f, 0f);//点击后的高亮线的显示样式
        set.setHighlightLineWidth(0.5f);//设置点击交点后显示高亮线宽
        set.setHighLightColor(Color.WHITE);//设置点击交点后显示交高亮线的颜色
        set.setVisible(true);
        set.setDrawFilled(false);//设置禁用范围背景填充
        set.setDrawValues(false);

        //保存LineDataSet集合
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set); // add the datasets
        //创建LineData对象 属于LineChart折线图的数据集合
        LineData data = new LineData(dataSets);
        // 添加到图表中
        chart.setData(data);
        //绘制图表
        chart.invalidate();
//        }

    }

    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            //请求分时数据
            MarketApi.marketDetail(mActivity, market.getId(), type, new JsonCallback<LzyResponse<MarketChartBean>>() {
                @Override
                public void onSuccess(final Response<LzyResponse<MarketChartBean>> response) {
                    data.clear();
                    data.addAll(response.body().data.getList());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setUpUi(response.body().data.getRecord());
                            setChartData();
                        }
                    });
                }
            });
        }
    };

    private void setUpUi(MarketChartBean.RecordBean record) {
        tvName.setText(record.getName());
        tvNet.setText(record.getSource());

        if (null != record.getRelationCap()) {
            tvUsPrice.setText("$" + decimalFormat.format(Float.parseFloat(record.getRelationCap().getPrice_usd())));
            tvCnPrice.setText("¥" + decimalFormat.format(Float.parseFloat(record.getRelationCap().getPrice_cny())));
            float liang = Float.parseFloat(record.getRelationCap().getVolume_cny_24h());
            if (liang < 10000) {
                tvAmount.setText(decimalFormat.format(liang));
            } else if (liang < 100000000) {
                tvAmount.setText(decimalFormat.format(liang / 10000) + "万");
            } else {
                tvAmount.setText(decimalFormat.format(liang / 10000 / 10000) + "亿");
            }

            if (market.getRelationCap().getPercent_change_24h().contains("-")) {
                tvRate.setText(record.getRelationCap().getPercent_change_24h() + "%");
                tvRate.setBackgroundColor(Color.parseColor("#fe5864"));
            } else {
                tvRate.setText("+" + record.getRelationCap().getPercent_change_24h() + "%");
                tvRate.setBackgroundColor(Color.parseColor("#66b7fb"));
            }
        }

        if (null != record.getRelationCapMin()) {
            tvStart.setText("¥" + decimalFormat.format(Float.parseFloat(record.getRelationCapMin().getPrice_cny_first())));
            tvHight.setText("¥" + decimalFormat.format(Float.parseFloat(record.getRelationCapMin().getPrice_cny_high())));
            tvLow.setText("¥" + decimalFormat.format(Float.parseFloat(record.getRelationCapMin().getPrice_cny_low())));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (timer == null) {
            timer = new Timer(true);
        } else {
            timer.schedule(task, 30000, 30000);
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
}
