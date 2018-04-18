package com.inwecrypto.wallet.ui.news.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.PingjiaBean;
import com.inwecrypto.wallet.bean.ProjectAnalysisBean;
import com.inwecrypto.wallet.bean.ProjectDetailAnalysisBean;
import com.inwecrypto.wallet.bean.TagBean;
import com.inwecrypto.wallet.bean.TradingProjectDetaileBean;
import com.inwecrypto.wallet.bean.XiangmuPingjiaBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.EndLessOnScrollListener;
import com.inwecrypto.wallet.common.widget.RatingBar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.event.PingjiaEvent;
import com.inwecrypto.wallet.ui.me.activity.PingjiaXiangqingActivity;
import com.inwecrypto.wallet.ui.me.adapter.XiangmuPingjiaAdapter;
import com.inwecrypto.wallet.ui.news.adapter.PingjiaChartAdapter;
import com.inwecrypto.wallet.ui.news.adapter.PingjiaDataChartAdapter;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.IllegalFormatCodePointException;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：xiaoji06 on 2018/4/4 17:35
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class YonghuPingjiaFragment extends BaseFragment implements OnChartValueSelectedListener {

    @BindView(R.id.my_ratingbar)
    RatingBar myRatingbar;
    @BindView(R.id.my_pingfen_ll)
    LinearLayout myPingfenLl;
    @BindView(R.id.fenshu)
    TextView fenshu;
    @BindView(R.id.ratingbar)
    RatingBar ratingbar;
    @BindView(R.id.pinglun_num)
    TextView pinglunNum;
    @BindView(R.id.v1)
    View v1;
    @BindView(R.id.n1)
    TextView n1;
    @BindView(R.id.v2)
    View v2;
    @BindView(R.id.n2)
    TextView n2;
    @BindView(R.id.v3)
    View v3;
    @BindView(R.id.n3)
    TextView n3;
    @BindView(R.id.v4)
    View v4;
    @BindView(R.id.n4)
    TextView n4;
    @BindView(R.id.v5)
    View v5;
    @BindView(R.id.n5)
    TextView n5;
    @BindView(R.id.tuijian)
    TextView tuijian;
    @BindView(R.id.tuijian_view)
    View tuijianView;
    @BindView(R.id.butuijian_view)
    View butuijianView;
    @BindView(R.id.butuijian)
    TextView butuijian;
    @BindView(R.id.hidell)
    LinearLayout hidell;
    @BindView(R.id.chartll)
    LinearLayout chartll;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.my_fenshu)
    TextView myFenshu;
    @BindView(R.id.tuijianll)
    LinearLayout tuijianll;
    @BindView(R.id.ri)
    TextView ri;
    @BindView(R.id.l1)
    View l1;
    @BindView(R.id.rill)
    LinearLayout rill;
    @BindView(R.id.zhou)
    TextView zhou;
    @BindView(R.id.l2)
    View l2;
    @BindView(R.id.zhoull)
    LinearLayout zhoull;
    @BindView(R.id.id_flowlayout)
    TagFlowLayout idFlowlayout;
    @BindView(R.id.scroll)
    NestedScrollView scroll;
    @BindView(R.id.top_ratingbar)
    RatingBar topRatingbar;
    @BindView(R.id.top_fenshu)
    TextView topFenshu;
    @BindView(R.id.yincang_iv)
    ImageView yincangIv;
    @BindView(R.id.yincang_tv)
    TextView yincangTv;
    @BindView(R.id.top_num)
    TextView topNum;
    @BindView(R.id.id_top_flowlayout)
    TagFlowLayout idTopFlowlayout;
    @BindView(R.id.topll)
    LinearLayout topll;
    @BindView(R.id.chart)
    RecyclerView chart;
    @BindView(R.id.up1)
    TextView up1;
    @BindView(R.id.up2)
    TextView up2;
    @BindView(R.id.down1)
    TextView down1;
    @BindView(R.id.down2)
    TextView down2;
    @BindView(R.id.data_chart)
    RecyclerView dataChart;
    @BindView(R.id.chart_detaile)
    LinearLayout chartDetaile;
    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.chose_detaile)
    TextView choseDetaile;

    private TagAdapter<String> tagAdapter;
    private ArrayList<String> tagData = new ArrayList<>();
    private String tagType = "";
    private ArrayList<TagBean> tagBeans = new ArrayList<>();

    private TagAdapter<String> topTagAdapter;
    private ArrayList<String> topTagData = new ArrayList<>();

    private String type = "day";
    private boolean isHide;
    private TradingProjectDetaileBean project;
    private int viewHight;

    private ArrayList<PingjiaBean> data = new ArrayList<>();
    private XiangmuPingjiaAdapter adapter;

    private int page = 1;
    private boolean isEnd;
    private boolean isShow;
    private LinearLayoutManager layoutManager;
    private EndLessOnScrollListener scrollListener;

    private ArrayList<ProjectDetailAnalysisBean> chartData = new ArrayList<>();
    private PingjiaChartAdapter chartAdapter;
    private PingjiaDataChartAdapter dataChartAdapter;

    private boolean needScroll = false;
    private int chartWidth;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfA = new SimpleDateFormat("MM.dd");

    private String start;
    private String end;

    @Override
    protected int setLayoutID() {
        return R.layout.yonghupingjia_fragment_layout;
    }

    @Override
    protected void initView() {

        setOpenEventBus(true);

        project = (TradingProjectDetaileBean) getArguments().getSerializable("project");

        viewHight = ScreenUtils.getScreenWidth(getContext()) - DensityUtil.dip2px(getContext(), 255);

        tagAdapter = new TagAdapter<String>(tagData) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.pingjia_fllow_tv,
                        parent, false);
                tv.setText(s);
                return tv;
            }

        };
        idFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                switch (position) {
                    case 0:
                        tagType = "";
                        break;
                    case 1:
                        tagType = "popular";
                        break;
                    case 2:
                        tagType = "recently";
                        break;
                    case 3:
                        tagType = "most";
                        break;
                }
                if (position > 3) {
                    tagType = tagBeans.get(position - 4).getKey();
                }
                Set<Integer> set = new HashSet<>();
                set.add(position);
                topTagAdapter.setSelectedList(set);
                tagAdapter.setSelectedList(set);
                needScroll = true;
                page=1;
                isEnd=false;
                isShow=false;
                scrollListener.reset();
                //获取tag标签
                getProjectList();
                return true;
            }

        });
        idFlowlayout.setAdapter(tagAdapter);
        idFlowlayout.setMaxSelectCount(1);


        topTagAdapter = new TagAdapter<String>(topTagData) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.pingjia_fllow_tv,
                        parent, false);
                tv.setText(s);
                return tv;
            }

        };
        idTopFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                switch (position) {
                    case 0:
                        tagType = "";
                        break;
                    case 1:
                        tagType = "popular";
                        break;
                    case 2:
                        tagType = "recently";
                        break;
                    case 3:
                        tagType = "most";
                        break;
                }
                if (position > 3) {
                    tagType = tagBeans.get(position - 4).getKey();
                }
                Set<Integer> set = new HashSet<>();
                set.add(position);
                tagAdapter.setSelectedList(set);
                topTagAdapter.setSelectedList(set);
                needScroll = true;
                page=1;
                isEnd=false;
                isShow=false;
                scrollListener.reset();
                //获取tag标签
                getProjectList();
                return true;
            }

        });
        idTopFlowlayout.setAdapter(topTagAdapter);
        idTopFlowlayout.setMaxSelectCount(1);

        scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if ((scrollY + DensityUtil.dip2px(getContext(), 37)) <= idFlowlayout.getY()) {
                    topll.setVisibility(View.GONE);
                } else {
                    topll.setVisibility(View.VISIBLE);
                }
            }
        });

        adapter = new XiangmuPingjiaAdapter(getContext(), R.layout.yonghupingjia_item_layout, data);
        layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
        list.setNestedScrollingEnabled(false);
        list.setFocusable(false);
        scrollListener = new EndLessOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                if (isEnd) {
                    if (!isShow && page != 1) {
                        ToastUtil.show(getString(R.string.zanwugengduoshuju));
                        isShow = true;
                    }
                } else {
                    page++;
                    getProjectList();
                }
            }
        };

        list.addOnScrollListener(scrollListener);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mActivity, PingjiaXiangqingActivity.class);
                intent.putExtra("pingjia", data.get(position));
                intent.putExtra("position", position);
                intent.putExtra("isFromMe", false);
                keepTogo(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        rill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "day";
                l2.setVisibility(View.INVISIBLE);

                chartDetaile.setVisibility(View.INVISIBLE);

                zhou.setTextColor(Color.parseColor("#ABABAB"));

                ri.setTextColor(getResources().getColor(R.color.c_FF6806));
                l1.setVisibility(View.VISIBLE);
                getChart();
            }
        });

        zhoull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "week";
                l1.setVisibility(View.INVISIBLE);

                chartDetaile.setVisibility(View.INVISIBLE);

                ri.setTextColor(Color.parseColor("#ABABAB"));

                zhou.setTextColor(getResources().getColor(R.color.c_FF6806));
                l2.setVisibility(View.VISIBLE);
                getChart();
            }
        });

        hidell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHide) {
                    isHide = false;
                    yincangIv.setImageResource(R.mipmap.on_icon);
                    yincangTv.setText(R.string.yincangtubiao);
                    chartll.setVisibility(View.VISIBLE);
                } else {
                    isHide = true;
                    yincangIv.setImageResource(R.mipmap.under_icon);
                    yincangTv.setText(R.string.zhankaitunbiao);
                    chartll.setVisibility(View.GONE);
                }
            }
        });

        chartAdapter = new PingjiaChartAdapter(getContext(), R.layout.pingjia_chart_item, chartData);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        chart.setLayoutManager(manager);
        chart.setAdapter(chartAdapter);
        chart.setFocusable(false);

        chartAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                chartDetaile.setVisibility(View.VISIBLE);
                try {
                    choseDetaile.setText(sdfA.format(sdf.parse(chartData.get(position).getDate_range()))
                            +" "+getString(R.string.tuijianpingjia)+" "+chartData.get(position).getLike()+getString(R.string.tiao_null)
                            +"    "+getString(R.string.butuijianpingjia)+" "+chartData.get(position).getVery_dissatisfied()+getString(R.string.tiao_null));
                } catch (ParseException e) {
                    e.printStackTrace();
                    choseDetaile.setText(" "+getString(R.string.tuijianpingjia)+" "+chartData.get(position).getLike()+getString(R.string.tiao_null)+"    "+getString(R.string.butuijianpingjia)+" "+chartData.get(position).getVery_dissatisfied()+"条"
                    );
                }

                page=1;
                isEnd=false;
                isShow=false;
                scrollListener.reset();
                //请求列表
                if ("day".equals(type)){
                    start=chartData.get(position).getDate_range();
                    end=chartData.get(position).getDate_range();
                }else {
                    try {
                        start=chartData.get(position).getDate_range();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(format.parse(chartData.get(position).getDate_range()));
                        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
                        Date today = calendar.getTime();
                        end= format.format(today);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                getCommentProjectList();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartDetaile.setVisibility(View.GONE);
            }
        });

        //增加整体布局监听
        ViewTreeObserver vto = chart.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                chart.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                chartWidth = chart.getWidth();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dataChart.getLayoutParams();
                params.width = chartWidth;
                dataChart.setLayoutParams(params);
            }
        });

        dataChartAdapter = new PingjiaDataChartAdapter(getContext(), R.layout.pingjia_data_char_item, chartData);
        dataChart.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        dataChart.setAdapter(dataChartAdapter);
        dataChart.setFocusable(false);

        chart.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    // note: scrollBy() not trigger OnScrollListener
                    // This is a known issue. It is caused by the fact that RecyclerView does not know how LayoutManager will handle the scroll or if it will handle it at all.
                    dataChart.scrollBy(dx, dy);
                }
            }
        });

        dataChart.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    chart.scrollBy(dx, dy);
                }
            }
        });

    }


    private void scrollTo() {
        scroll.smoothScrollTo(0, (int) (list.getY() - DensityUtil.dip2px(getContext(), 120)));
    }

    @Override
    protected void loadData() {

        if (App.get().isLogin()) {
            ZixunApi.getCurComment(mActivity, project.getId() + "", new JsonCallback<LzyResponse<PingjiaBean>>() {
                @Override
                public void onSuccess(Response<LzyResponse<PingjiaBean>> response) {
                    PingjiaBean pingjiaBean = response.body().data;
                    if (null == pingjiaBean) {
                        myPingfenLl.setVisibility(View.GONE);
                    } else {
                        if (response.body().data.getIs_category_comment()==0){
                            myPingfenLl.setVisibility(View.GONE);
                        }else {
                            myPingfenLl.setVisibility(View.VISIBLE);
                            myRatingbar.setStar(Float.parseFloat(pingjiaBean.getScore()));
                            myFenshu.setText(pingjiaBean.getScore() + getString(R.string.fenshu));
                        }
                    }
                }
            });
        }

        ZixunApi.getProjectAnalysis(this, project.getId() + "", new JsonCallback<LzyResponse<ProjectAnalysisBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ProjectAnalysisBean>> response) {
                ProjectAnalysisBean analysisBean = response.body().data;
                fenshu.setText(new BigDecimal(null == analysisBean.getScore_avg() ? "0" : analysisBean.getScore_avg()).setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString());
                ratingbar.setStar(Float.parseFloat(null == analysisBean.getScore_avg() ? "0" : analysisBean.getScore_avg()));
                pinglunNum.setText(analysisBean.getTotal() + getString(R.string.yipingjia));

                topFenshu.setText(new BigDecimal(null == analysisBean.getScore_avg() ? "0" : analysisBean.getScore_avg()).setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString());
                topRatingbar.setStar(Float.parseFloat(null == analysisBean.getScore_avg() ? "0" : analysisBean.getScore_avg()));
                topNum.setText(analysisBean.getTotal() + getString(R.string.yipingjia));

                BigDecimal totle = new BigDecimal(analysisBean.getTotal());

                if (totle.floatValue() != 0) {
                    float per1 = new BigDecimal(analysisBean.getLike()).divide(totle, 2, BigDecimal.ROUND_HALF_UP).setScale(BigDecimal.ROUND_HALF_UP, 2).floatValue();
                    LinearLayout.LayoutParams p1 = (LinearLayout.LayoutParams) v1.getLayoutParams();
                    p1.width = (int) (viewHight * per1);
                    v1.setLayoutParams(p1);
                    n1.setText((per1 * 100) + "%");

                    float per2 = new BigDecimal(analysisBean.getRecommend()).divide(totle, 2, BigDecimal.ROUND_HALF_UP).setScale(BigDecimal.ROUND_HALF_UP, 2).floatValue();
                    LinearLayout.LayoutParams p2 = (LinearLayout.LayoutParams) v2.getLayoutParams();
                    p2.width = (int) (viewHight * per2);
                    v2.setLayoutParams(p2);
                    n2.setText((per2 * 100) + "%");

                    float per3 = new BigDecimal(analysisBean.getGood()).divide(totle, 2, BigDecimal.ROUND_HALF_UP).setScale(BigDecimal.ROUND_HALF_UP, 2).floatValue();
                    LinearLayout.LayoutParams p3 = (LinearLayout.LayoutParams) v3.getLayoutParams();
                    p3.width = (int) (viewHight * per3);
                    v3.setLayoutParams(p3);
                    n3.setText((per3 * 100) + "%");

                    float per4 = new BigDecimal(analysisBean.getDissatisfied()).divide(totle, 2, BigDecimal.ROUND_HALF_UP).setScale(BigDecimal.ROUND_HALF_UP, 2).floatValue();
                    LinearLayout.LayoutParams p4 = (LinearLayout.LayoutParams) v4.getLayoutParams();
                    p4.width = (int) (viewHight * per4);
                    v4.setLayoutParams(p4);
                    n4.setText((per4 * 100) + "%");

                    float per5 = new BigDecimal(analysisBean.getVery_dissatisfied()).divide(totle, 2, BigDecimal.ROUND_HALF_UP).setScale(BigDecimal.ROUND_HALF_UP, 2).floatValue();
                    LinearLayout.LayoutParams p5 = (LinearLayout.LayoutParams) v5.getLayoutParams();
                    p5.width = (int) (viewHight * per5);
                    v5.setLayoutParams(p5);
                    n5.setText((per5 * 100) + "%");

                    int good = analysisBean.getLike() + analysisBean.getRecommend();
                    int bad = analysisBean.getDissatisfied() + analysisBean.getVery_dissatisfied();
                    BigDecimal totleTuijian = new BigDecimal(good + bad);
                    int width = tuijianll.getWidth();
                    float preGood = 0;
                    float preBad = 0;

                    if (totleTuijian.intValue() != 0) {
                        preGood = new BigDecimal(good).divide(totleTuijian, 2, BigDecimal.ROUND_HALF_UP).setScale(BigDecimal.ROUND_HALF_UP, 2).floatValue();
                        preBad = new BigDecimal(bad).divide(totleTuijian, 2, BigDecimal.ROUND_HALF_UP).setScale(BigDecimal.ROUND_HALF_UP, 2).floatValue();
                    }
                    tuijian.setText("(" + good + getString(R.string.ren) + "," + (preGood * 100) + "%)");
                    butuijian.setText("(" + bad + getString(R.string.ren) + "," + (preBad * 100) + "%)");

                    LinearLayout.LayoutParams pGood = (LinearLayout.LayoutParams) tuijianView.getLayoutParams();
                    LinearLayout.LayoutParams pBad = (LinearLayout.LayoutParams) butuijianView.getLayoutParams();
                    if (good == bad) {
                        pGood.width = (int) (width * 0.5f);
                        pBad.width = (int) (width * 0.5f);
                    } else {
                        pGood.width = (int) (width * preGood);
                        pBad.width = (int) (width * preBad);
                    }
                    tuijianView.setLayoutParams(pGood);
                    butuijianView.setLayoutParams(pBad);
                } else {
                    n1.setText("0.0%");
                    n2.setText("0.0%");
                    n3.setText("0.0%");
                    n4.setText("0.0%");
                    n5.setText("0.0%");

                    tuijian.setText("(0" + getString(R.string.ren) + ",0.0%)");
                    butuijian.setText("(0" + getString(R.string.ren) + ",0.0%)");

                    LinearLayout.LayoutParams pGood = (LinearLayout.LayoutParams) tuijianView.getLayoutParams();
                    LinearLayout.LayoutParams pBad = (LinearLayout.LayoutParams) butuijianView.getLayoutParams();
                    int width = tuijianll.getWidth();
                    pGood.width = (int) (width * 0.5f);
                    pBad.width = (int) (width * 0.5f);
                    tuijianView.setLayoutParams(pGood);
                    butuijianView.setLayoutParams(pBad);
                }
            }
        });

        getChart();

        //获取tag标签
        getProjectTag();

        getProjectList();
    }

    private void getProjectTag() {
        tagData.clear();
        tagData.add(getString(R.string.quanbu));
        tagData.add(getString(R.string.ermen));
        tagData.add(getString(R.string.zuixin));
        tagData.add(getString(R.string.zuiyoujiazhi));

        topTagData.clear();
        topTagData.add(getString(R.string.quanbu));
        topTagData.add(getString(R.string.ermen));
        topTagData.add(getString(R.string.zuixin));
        topTagData.add(getString(R.string.zuiyoujiazhi));

        ZixunApi.getProjectTag(this, project.getId() + "", new JsonCallback<LzyResponse<ArrayList<TagBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArrayList<TagBean>>> response) {
                tagBeans.clear();
                if (null != response.body().data) {
                    tagBeans.addAll(response.body().data);
                    for (TagBean tag : response.body().data) {
                        tagData.add(tag.getName() + "(" + tag.getNumber() + ")");
                        topTagData.add(tag.getName() + "(" + tag.getNumber() + ")");
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                tagAdapter.notifyDataChanged();
                tagAdapter.setSelectedList(0);
                topTagAdapter.notifyDataChanged();
                topTagAdapter.setSelectedList(0);
            }
        });
    }

    private void getProjectList() {
        ZixunApi.getProjectComment(this, project.getId() + "", tagType,page, new JsonCallback<LzyResponse<XiangmuPingjiaBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<XiangmuPingjiaBean>> response) {
                if (page == 1) {
                    data.clear();
                }

                if (null != response.body().data.getData()) {
                    data.addAll(response.body().data.getData());
                }

                if (page >= response.body().data.getCurrent_page()) {
                    //最后一页
                    isEnd = true;
                }
                adapter.notifyDataSetChanged();

                if (needScroll) {
                    needScroll = false;
                    scrollTo();
                }
            }

            @Override
            public void onError(Response<LzyResponse<XiangmuPingjiaBean>> response) {
                super.onError(response);
                if (page != 1) {
                    page--;
                }
            }
        });
    }


    private void getCommentProjectList() {
        ZixunApi.getProjectComment(this, project.getId() + "", start,end,page, new JsonCallback<LzyResponse<XiangmuPingjiaBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<XiangmuPingjiaBean>> response) {
                if (page == 1) {
                    data.clear();
                }

                if (null != response.body().data.getData()) {
                    data.addAll(response.body().data.getData());
                }

                if (page >= response.body().data.getCurrent_page()) {
                    //最后一页
                    isEnd = true;
                }
                adapter.notifyDataSetChanged();

                if (needScroll) {
                    needScroll = false;
                    scrollTo();
                }
            }

            @Override
            public void onError(Response<LzyResponse<XiangmuPingjiaBean>> response) {
                super.onError(response);
                if (page != 1) {
                    page--;
                }
            }
        });
    }

    private void getChart() {

        ZixunApi.getProjectDetailAnalysis(this, project.getId() + "", type, new JsonCallback<LzyResponse<ArrayList<ProjectDetailAnalysisBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArrayList<ProjectDetailAnalysisBean>>> response) {
                //获取大值 设置范围
                chartData.clear();
                if (null != response.body().data) {
                    chartData.addAll(response.body().data);
                }
                Collections.reverse(chartData);
                int max = 10;
                int mid = 5;
                if (null != chartData) {
                    for (ProjectDetailAnalysisBean line : chartData) {
                        if (line.getLike() > max) {
                            max = line.getLike();
                        }
                        if (line.getVery_dissatisfied() > max) {
                            max = line.getVery_dissatisfied();
                        }
                    }

                    if (max < 10) {
                        max = 10;
                    } else if (max < 100) {
                        max = (int) Math.ceil(max / 10.0) * 10;
                    } else if (max < 1000) {
                        max = (int) Math.ceil(max / 100.0) * 100;
                    } else if (max < 10000) {
                        max = (int) Math.ceil(max / 1000.0) * 1000;
                    } else {
                        max = (int) Math.ceil(max / 10000.0) * 10000;
                    }
                    mid = max / 2;

                    up1.setText("" + max);
                    up2.setText("" + mid);
                    down1.setText("" + mid);
                    down2.setText("" + max);
                }

                chartAdapter.setParams(max, chartWidth);
                chartAdapter.notifyDataSetChanged();

                dataChartAdapter.setParams(max, chartWidth);
                dataChartAdapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()==Constant.EVENT_PINGJIA_SUC){
            loadData();
        }
        if (event.getEventCode() == Constant.EVENT_PINGJIA) {
            PingjiaEvent pingjiaEvent = (PingjiaEvent) event.getData();
            PingjiaBean item = data.get(pingjiaEvent.position);
            if (null != item.getUser_click_comment()&& item.getUser_click_comment().size()!=0) {
                if (item.getUser_click_comment().get(0).getUp() != pingjiaEvent.up) {
                    if (pingjiaEvent.up == 0) {
                        item.setUser_click_comment_up_count(item.getUser_click_comment_up_count() - 1);
                    } else {
                        item.setUser_click_comment_up_count(item.getUser_click_comment_up_count() + 1);
                    }
                    item.getUser_click_comment().get(0).setUp(pingjiaEvent.up);
                }

                if (item.getUser_click_comment().get(0).getDown() != pingjiaEvent.down) {
                    if (pingjiaEvent.down == 0) {
                        item.setUser_click_comment_down_count(item.getUser_click_comment_down_count() - 1);
                    } else {
                        item.setUser_click_comment_down_count(item.getUser_click_comment_down_count() + 1);
                    }
                    item.getUser_click_comment().get(0).setDown(pingjiaEvent.down);
                }

                if (item.getUser_click_comment().get(0).getEqual() != pingjiaEvent.smile) {
                    if (pingjiaEvent.smile == 0) {
                        item.setUser_click_comment_equal_count(item.getUser_click_comment_equal_count() - 1);
                    } else {
                        item.setUser_click_comment_equal_count(item.getUser_click_comment_equal_count() + 1);
                    }
                    item.getUser_click_comment().get(0).setEqual(pingjiaEvent.smile);
                }
            } else {
                PingjiaBean.UserClickCommentBean userClickCommentBean = new PingjiaBean.UserClickCommentBean();
                userClickCommentBean.setUp(pingjiaEvent.up);
                userClickCommentBean.setDown(pingjiaEvent.down);
                userClickCommentBean.setEqual(pingjiaEvent.smile);
                item.getUser_click_comment().add(userClickCommentBean);
            }

            if (item.getComment_count() != pingjiaEvent.msg) {
                item.setComment_count(pingjiaEvent.msg);
            }
            adapter.notifyItemChanged(pingjiaEvent.position);
        }
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
