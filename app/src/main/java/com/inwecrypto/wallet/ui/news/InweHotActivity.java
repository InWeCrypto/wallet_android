package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.ArticleDetaileBean;
import com.inwecrypto.wallet.bean.ArticleListBean;
import com.inwecrypto.wallet.bean.CommonProjectBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.MultiItemTypeSupport;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.adapter.InwehotNewsAdapter;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/2/8 20:01
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class  InweHotActivity extends BaseActivity {


    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    ImageView txtRightTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayoutCompat swipeRefresh;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.views)
    TextView views;
    @BindView(R.id.lishizixun)
    TextView lishizixun;
    private int page = 1;
    private boolean isEnd;

    private CommonProjectBean marks;

    private InwehotNewsAdapter adapter;
    private HeaderAndFooterWrapper footer;
    private View footerView;
    private ArrayList<ArticleDetaileBean> data = new ArrayList<>();

    private boolean isFirst;

    @Override
    protected void getBundleExtras(Bundle extras) {
        marks = (CommonProjectBean) extras.getSerializable("marks");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.inweproject_activity;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.dongtai);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ProjectDetaileActivity.class);
                intent.putExtra("marks", marks);
                keepTogo(intent);
            }
        });

        line.setVisibility(View.VISIBLE);
        lishizixun.setVisibility(View.VISIBLE);

        lishizixun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, InweHotBottomHistoryActivity.class);
                keepTogo(intent);
            }
        });

        adapter = new InwehotNewsAdapter(this, data, new MultiItemTypeSupport<ArticleDetaileBean>() {
            @Override
            public int getLayoutId(int itemType) {
               if (itemType==1){
                   return R.layout.inwe_hot_quick_item;
               }else if (itemType==2){
                   return R.layout.jiaoyigonggao_item;
               }else {
                   return R.layout.inwe_hot_item;
               }
            }

            @Override
            public int getItemViewType(int position, ArticleDetaileBean articleDetaileBean) {
                return articleDetaileBean.getType()==1?1:(articleDetaileBean.getType()==16?2:0);
            }
        });

        footer=new HeaderAndFooterWrapper(adapter);
        footerView=LayoutInflater.from(this).inflate(R.layout.empty_view,null,false);
        footer.addFootView(footerView);

        swipeRefresh.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= 16) {
                    swipeRefresh.getViewTreeObserver()
                            .removeOnGlobalLayoutListener(this);
                }
                else {
                    swipeRefresh.getViewTreeObserver()
                            .removeGlobalOnLayoutListener(this);
                }
                swipeRefresh.getHeight(); // 获取高度
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) footerView.getLayoutParams();
                params.height=swipeRefresh.getHeight()/4;
                footerView.setLayoutParams(params);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        list.setLayoutManager(linearLayoutManager);
        list.setAdapter(footer);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isFirst) {
                    page++;
                } else {
                    page = 1;
                }
                isEnd = false;
                initData();
            }
        });

    }

    @Override
    protected void initData() {
        ZixunApi.getInweHot(this, page, new JsonCallback<LzyResponse<ArticleListBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArticleListBean>> response) {
                if (!response.isFromCache()) {
                    if (!isFirst) {
                        isFirst = true;
                    }
                }
                LoadSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<ArticleListBean>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
                if (page != 1) {
                    page--;
                }
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

    private void LoadSuccess(Response<LzyResponse<ArticleListBean>> response) {
        int start = 0;
        int count = 0;
        if (page == 1) {
            data.clear();
            if (1 >= response.body().data.getLast_page()) {
                isEnd = true;
                swipeRefresh.setEnabled(false);
            }
        } else {
            if (page >= response.body().data.getLast_page()) {
                isEnd = true;
                swipeRefresh.setEnabled(false);
            }
        }

        if (null != response.body().data.getData()) {
            count = response.body().data.getData().size();
            Collections.reverse(response.body().data.getData());
            data.addAll(0, response.body().data.getData());
        }
        footer.notifyItemRangeInserted(start, count);
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_NOTIFY) {
            marks.setOpenTip((Boolean) event.getData());
        }

        if (event.getEventCode() == Constant.EVENT_INWE_HOT_CLICK){
            if (null!=data&&data.size()>event.getKey1()){
                Intent intent = new Intent(mActivity, ProjectNewsWebActivity.class);
                intent.putExtra("title", data.get(event.getKey1()).getTitle());
                intent.putExtra("url", (App.isMain ? Url.MAIN_NEWS : Url.TEST_NEWS) + data.get(event.getKey1()).getId());
                intent.putExtra("id", data.get(event.getKey1()).getId());
                intent.putExtra("decs",data.get(event.getKey1()).getDesc());
                intent.putExtra("img",data.get(event.getKey1()).getImg());
                keepTogo(intent);
            }
        }
    }
}
