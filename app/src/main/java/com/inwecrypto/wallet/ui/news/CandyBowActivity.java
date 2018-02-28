package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CandyBowBean;
import com.inwecrypto.wallet.bean.CommonProjectBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.adapter.CandyBowAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/2/9 09:34
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class CandyBowActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    ImageView txtRightTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.left)
    FrameLayout left;
    @BindView(R.id.data)
    TextView dataTitle;
    @BindView(R.id.right)
    FrameLayout right;
    @BindView(R.id.calendarView)
    CalendarView calendarView;
    @BindView(R.id.current_day)
    TextView currentDay;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayoutCompat swipeRefresh;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.views)
    TextView views;
    private CommonProjectBean marks;

    private int page = 1;
    private boolean isEnd;
    private boolean isShow;

    private CandyBowAdapter adapter;
    private ArrayList<CandyBowBean.ListBean.DataBean> data=new ArrayList<>();
    private LinearLayoutManager layoutManager;

    @Override
    protected void getBundleExtras(Bundle extras) {
        marks = (CommonProjectBean) extras.getSerializable("marks");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.candy_bow_activity;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtMainTitle.setText(marks.getName());
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,ProjectDetaileActivity.class);
                intent.putExtra("marks",marks);
                keepTogo(intent);
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.scrollToPre();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.scrollToNext();
            }
        });

        calendarView.setOnDateSelectedListener(new CalendarView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Calendar calendar, boolean isClick) {
                OkGo.getInstance().cancelTag(mActivity);
                if (null!=adapter){
                    data.clear();
                    adapter.notifyDataSetChanged();
                }
                currentDay.setText(calendarView.getSelectedCalendar().getDay()+"/"+calendarView.getSelectedCalendar().getMonth());
                page=1;
                getDay();
            }
        });

        calendarView.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                dataTitle.setText(month+"/"+year);
                getMonth();
            }
        });

        calendarView.setOnYearChangeListener(new CalendarView.OnYearChangeListener() {
            @Override
            public void onYearChange(int year) {
                dataTitle.setText(calendarView.getSelectedCalendar().getMonth()+"/"+year);
            }
        });

        //设置当前时间
        dataTitle.setText(calendarView.getCurMonth()+"/"+calendarView.getCurYear());
        currentDay.setText(calendarView.getCurDay()+"/"+calendarView.getCurMonth());

        adapter = new CandyBowAdapter(this, R.layout.candy_bow_item, data);
        layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mActivity,CandyDetaileActivity.class);
                intent.putExtra("candy",data.get(position));
                keepTogo(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isEnd = false;
                getDay();
            }
        });

        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
    }

    @Override
    protected void initData() {
        getMonth();
        getDay();
    }

    private void getDay() {
        //获取当天日期空头日历
        ZixunApi.getCandybowDay(this
                , calendarView.getSelectedCalendar().getYear()
                , calendarView.getSelectedCalendar().getMonth()
                , calendarView.getSelectedCalendar().getDay(),page, new JsonCallback<LzyResponse<CandyBowBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<CandyBowBean>> response) {
                        LoadSuccess(response);
                    }

                    @Override
                    public void onError(Response<LzyResponse<CandyBowBean>> response) {
                        super.onError(response);
                        ToastUtil.show(getString(R.string.load_error));
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (null != swipeRefresh) {
                            swipeRefresh.setRefreshing(false);
                        }
                    }
                });
    }

    private void getMonth() {
        //获取当月空头日历
        ZixunApi.getCandybow(this
                , calendarView.getSelectedCalendar().getYear()
                , calendarView.getSelectedCalendar().getMonth(),page, new JsonCallback<LzyResponse<CandyBowBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<CandyBowBean>> response) {
                        //
                        if (null!=response.body().data.getList().getData()){
                            List<Calendar> candyList=new ArrayList<>();
                            for (CandyBowBean.ListBean.DataBean data:response.body().data.getList().getData()){
                                Calendar calendar=new Calendar();
                                calendar.setDay(data.getDay());
                                calendar.setMonth(data.getMonth());
                                calendar.setYear(data.getYear());
                                candyList.add(calendar);
                            }
                            calendarView.setSchemeDate(candyList);
                            calendarView.setSchemeColor(Color.parseColor("#F0EFEF"),Color.parseColor("#333333"),Color.parseColor("#333333"));
                        }
                    }
                });
    }

    private void LoadSuccess(Response<LzyResponse<CandyBowBean>> response) {
        data.clear();
        if (null!=response.body().data.getList().getData()){
            data.addAll(response.body().data.getList().getData());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
