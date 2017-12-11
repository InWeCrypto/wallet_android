package com.inwecrypto.wallet.ui.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.AdsBean;
import com.inwecrypto.wallet.bean.InfoPriceBean;
import com.inwecrypto.wallet.bean.NewsBean;
import com.inwecrypto.wallet.bean.ProjectBean;
import com.inwecrypto.wallet.bean.ProjectShowBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.InfoApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.BarUtils;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.AutoLoopViewPager;
import com.inwecrypto.wallet.common.widget.BetterRecyclerView;
import com.inwecrypto.wallet.common.widget.MultiItemTypeSupport;
import com.inwecrypto.wallet.common.widget.SpaceItemDecoration;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.info.activity.ProjectOnlineActivity;
import com.inwecrypto.wallet.ui.info.activity.ProjectUnderlineActivity;
import com.inwecrypto.wallet.ui.info.activity.SearchActivity;
import com.inwecrypto.wallet.ui.info.adapter.ProjectAdapter;
import com.inwecrypto.wallet.ui.me.activity.CommonWebActivity;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by donghaijun on 2017/10/24.
 */

public class InfoFragment extends BaseFragment implements AutoLoopViewPager.OnGetAdsViewPager {


    @BindView(R.id.list)
    BetterRecyclerView list;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayoutCompat swipeRefresh;
    @BindView(R.id.menu)
    FrameLayout menu;
    @BindView(R.id.serch)
    FrameLayout serch;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.titleFL)
    FrameLayout titleFL;

    AutoLoopViewPager ads;
    ViewFlipper news;

    private HeaderAndFooterWrapper headerAdapter;
    private View header;
    private ArrayList<AdsBean.ListBean> adsBeans = new ArrayList<>();
    private ArrayList<NewsBean> newsBeans = new ArrayList<>();

    private ArrayList<ProjectShowBean> projectBeans = new ArrayList<>();
    private ProjectAdapter projectAdapter;
    private GridLayoutManager manager;

    @Override
    protected int setLayoutID() {
        return R.layout.info_activity_main_info;
    }

    @Override
    protected void initView() {

        isOpenEventBus = true;

        initHeader();

        initList();
    }

    private void initList() {

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (manager.findFirstVisibleItemPosition() == 0 && manager.getChildAt(0).getY() == 0) {
                    swipeRefresh.setEnabled(true);
                } else {
                    swipeRefresh.setEnabled(false);
                }
            }

        });

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        MultiItemTypeSupport multiItemSupport = new MultiItemTypeSupport<ProjectShowBean>() {
            @Override
            public int getLayoutId(int itemType) {
                //根据itemType返回item布局文件id
                switch (itemType) {
                    case 1:
                        return R.layout.info_item_card_4_small;
                    case 2:
                        return R.layout.info_item_card_big;
                }
                return 0;
            }

            @Override
            public int getItemViewType(int postion, ProjectShowBean projectBean) {
                return projectBean.getType();
            }
        };
        projectAdapter = new ProjectAdapter(getContext(), projectBeans, multiItemSupport);
        headerAdapter = new HeaderAndFooterWrapper(projectAdapter);
        headerAdapter.addHeaderView(header);
        manager = new GridLayoutManager(getContext(), 2);
        list.setLayoutManager(manager);
        list.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(getContext(), 3), DensityUtil.dip2px(getContext(), 5)));
        list.setAdapter(headerAdapter);
        list.getItemAnimator().setChangeDuration(0);
    }

    private void initHeader() {
        header = LayoutInflater.from(getActivity()).inflate(R.layout.info_view_main_info_header, null, false);
        header.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        ads= (AutoLoopViewPager) header.findViewById(R.id.ads);
        news= (ViewFlipper) header.findViewById(R.id.news);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.showMainMenu(menu, mActivity, false);
            }
        });

        serch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SearchActivity.class);
                intent.putExtra("type", 0);
                keepTogo(intent);
            }
        });

        int width = ScreenUtils.getScreenWidth(getContext());
        ads.setLayoutParams(new LinearLayout.LayoutParams(width, (int) (width / 750.0 * 368)));
        ads.setOnItemClickListener(new AutoLoopViewPager.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (null == adsBeans || adsBeans.size() == 0) {
                    return;
                }
                if (null != adsBeans.get(position).getUrl()) {
                    Intent intent = new Intent(mActivity, CommonWebActivity.class);
                    intent.putExtra("title", adsBeans.get(position).getName());
                    intent.putExtra("url", adsBeans.get(position).getUrl().startsWith("http") ? adsBeans.get(position).getUrl() : Url.WEB_ROOT + adsBeans.get(position).getUrl().replace("../", ""));
                    keepTogo(intent);
                }
            }
        });

        list.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefresh.setEnabled(topRowVerticalPosition >= 0);
            }
        });

    }

    @Override
    protected void loadData() {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });

        //请求 ads
        InfoApi.getAds(this, new JsonCallback<LzyResponse<AdsBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<AdsBean>> response) {
                if (null != response.body()) {
                    List<AdsBean.ListBean> adsList = response.body().data.getList();
                    if (null != adsList || adsList.size() > 0) {
                        adsBeans.clear();
                        adsBeans.addAll(adsList);
                        ads.setPagerAdapter(InfoFragment.this, adsBeans);
                    }
                }
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<AdsBean>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }
        });
        //请求 news
        InfoApi.getNews(this, new JsonCallback<LzyResponse<ArrayList<NewsBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArrayList<NewsBean>>> response) {
                if (null != response.body()) {
                    List<NewsBean> newsList = response.body().data;
                    if (null != newsList || newsList.size() > 0) {
                        newsBeans.clear();
                        newsBeans.addAll(newsList);
                        initNews();
                    }
                }
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<ArrayList<NewsBean>>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }
        });
        //请求 project
        InfoApi.getProject(this, new JsonCallback<LzyResponse<ArrayList<ProjectBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArrayList<ProjectBean>>> response) {
                if (null != response.body()) {
                    ArrayList<ProjectBean> bfList = response.body().data;
                    if (null != bfList || bfList.size() > 0) {
                        //排序
                        sort(bfList);
                        //添加
                        headerAdapter.notifyDataSetChanged();
                    }
                }

                if (!response.isFromCache()) {
                    swipeRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<ArrayList<ProjectBean>>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<ArrayList<ProjectBean>>> response) {
                super.onError(response);
                swipeRefresh.setRefreshing(false);
                ToastUtil.show(R.string.load_error);
            }
        });

    }

    private void initNews() {

        int count = newsBeans.size();
        int i = 0;
        for (; i < count; i = i + 2) {
            final View ll_content = View.inflate(getContext(), R.layout.info_view_news_item, null);
            View news1 = ll_content.findViewById(R.id.news1);
            TextView title1 = (TextView) ll_content.findViewById(R.id.title1);
            View news2 = ll_content.findViewById(R.id.news2);
            TextView title2 = (TextView) ll_content.findViewById(R.id.title2);

            title1.setText(newsBeans.get(i).getTitle());
            final int finalI = i;
            news1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoNews(finalI);
                }
            });

            if (count <= (i + 1)) {
                news2.setVisibility(View.GONE);
                news.addView(ll_content);
                if (count == 1) {
                    news.stopFlipping();
                }
                return;
            }
            title2.setText(newsBeans.get(i + 1).getTitle());

            news2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoNews(finalI + 1);
                }
            });
            news.addView(ll_content);
        }
    }

    private void gotoNews(int position) {
        if (null == newsBeans || newsBeans.size() == 0) {
            return;
        }
        if (null != newsBeans.get(position).getUrl()) {
            Intent intent = new Intent(mActivity, CommonWebActivity.class);
            intent.putExtra("title", newsBeans.get(position).getTitle());
            intent.putExtra("url", newsBeans.get(position).getUrl().startsWith("http") ? newsBeans.get(position).getUrl() : Url.WEB_ROOT + newsBeans.get(position).getUrl().replace("../", ""));
            keepTogo(intent);
        }
    }

    private void sort(ArrayList<ProjectBean> bfList) {
        projectBeans.clear();
        int size = bfList.size();
        byte[] use = new byte[size];
        boolean isFull = true;
        int current = 0;
        ProjectShowBean projectShowBean = null;
        int type = 0;

        for (int i = 0; i < size; i++) {
            type = bfList.get(i).getGrid_type();
            if (type == 0) {
                continue;
            }
            if (use[i] == 0 && 4 != type) {
                isFull = false;
                current = 1;
                projectShowBean = new ProjectShowBean();
                projectShowBean.setType(1);
                projectShowBean.setProject1(bfList.get(i));
                use[i] = 1;
                int innerType = -1;
                for (int j = (i + 1); j < size && current < 4; j++) {
                    innerType = bfList.get(j).getGrid_type();
                    if (use[j] == 0 && 4 != innerType) {
                        current++;
                        switch (current) {
                            case 2:
                                projectShowBean.setProject2(bfList.get(j));
                                break;
                            case 3:
                                projectShowBean.setProject3(bfList.get(j));
                                break;
                            case 4:
                                projectShowBean.setProject4(bfList.get(j));
                                break;
                        }
                        use[j] = 1;

                        if (current == 4) {
                            isFull = true;
                            projectBeans.add(projectShowBean);
                        }
                    }
                }

            } else if (4 == type) {
                use[i] = 1;
                projectShowBean = new ProjectShowBean();
                projectShowBean.setType(2);
                projectShowBean.setProject1(bfList.get(i));
                projectBeans.add(projectShowBean);
            }
        }

        if (!isFull) {
            projectBeans.add(projectShowBean);
        }

    }

    @Override
    protected void EventBean(final BaseEventBusBean event) {

        if (event.getEventCode() == Constant.EVENT_PROJECT) {
            if (null != projectBeans) {
                ProjectBean projectBean = null;
                switch (event.getKey2()) {
                    case 1:
                        projectBean = projectBeans.get(event.getKey1()).getProject1();
                        break;
                    case 2:
                        projectBean = projectBeans.get(event.getKey1()).getProject2();
                        break;
                    case 3:
                        projectBean = projectBeans.get(event.getKey1()).getProject3();
                        break;
                    case 4:
                        projectBean = projectBeans.get(event.getKey1()).getProject4();
                        break;
                }
                Intent intent = null;
                if (projectBean.getType() == 5 || projectBean.getType() == 6) {
                    intent = new Intent(mActivity, ProjectOnlineActivity.class);
                } else {
                    intent = new Intent(mActivity, ProjectUnderlineActivity.class);
                }
                intent.putExtra("project", projectBean);
                keepTogo(intent);
            }
        }
    }


    @Override
    public AutoLoopViewPager getAdsViewPager() {
        return ads;
    }

}
