package com.inwecrypto.wallet.ui.discover;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.lzy.okgo.model.Response;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;

import butterknife.BindView;
import com.inwecrypto.wallet.AppApplication;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.ArticleBean;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.FindBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.DiscoverApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.imageloader.GlideImageLoader;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.discover.activity.ArticleDetailActivity;
import com.inwecrypto.wallet.ui.discover.activity.IcoListActivity;
import com.inwecrypto.wallet.ui.discover.adapter.ArticleAdapter;
import com.inwecrypto.wallet.ui.discover.adapter.IcoAdapter;

/**
 * Created by Administrator on 2017/7/15.
 * 功能描述：
 * 版本：@version
 */

public class DiscoverFragment extends BaseFragment {

    @BindView(R.id.zixun_list)
    RecyclerView zixunList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayoutCompat swipeRefresh;

    private ArrayList<ArticleBean> articlelist=new ArrayList<>();
    private ArticleAdapter articleAdapter;
    private HeaderAndFooterWrapper headerAndFooterWrapper;
    private LoadMoreWrapper loadMoreWrapper;

    private View headerView;

    private Banner bannerImg;
    private ArrayList<FindBean.Banner> banner=new ArrayList<>();
    private ArrayList<String> bannerString=new ArrayList<>();
    private ArrayList<String> bannerTitle=new ArrayList<>();

    private RecyclerView rvIcon;
    private View space;
    private ArrayList<FindBean.Ico> icos=new ArrayList<>();
    private IcoAdapter icoAdapter;

    @Override
    protected int setLayoutID() {
        return R.layout.discover_fragment;
    }

    @Override
    protected void initView() {

        headerView = LayoutInflater.from(mContext).inflate(R.layout.discover_fragment_header, null);

        space=headerView.findViewById(R.id.space);
        bannerImg = (Banner) headerView.findViewById(R.id.banner_img);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bannerImg.getLayoutParams();
        params.width = ScreenUtils.getScreenWidth(mActivity);
        params.height = (int) (params.width / 750.0*411);
        bannerImg.setLayoutParams(params);
        //设置图片加载器
        bannerImg.setImageLoader(new GlideImageLoader());
        //设置图片集合
        bannerImg.setImages(bannerString);
        bannerImg.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        bannerImg.setIndicatorGravity(BannerConfig.CENTER);
        bannerImg.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent=new Intent(mActivity,ArticleDetailActivity.class);
                intent.putExtra("id",banner.get(position).getId());
                intent.putExtra("title",banner.get(position).getDetail().getTitle());
                keepTogo(intent);
            }
        });

        rvIcon = (RecyclerView) headerView.findViewById(R.id.rv_icon);
        rvIcon.setLayoutManager(new GridLayoutManager(mContext,4));
        icoAdapter=new IcoAdapter(mContext,R.layout.discover_item_ico,icos);
        rvIcon.setAdapter(icoAdapter);
        icoAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                keepTogo(IcoListActivity.class);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        articleAdapter = new ArticleAdapter(mContext,R.layout.discover_item_article, articlelist);
        headerAndFooterWrapper=new HeaderAndFooterWrapper(articleAdapter);
        headerAndFooterWrapper.addHeaderView(headerView);
        loadMoreWrapper=new LoadMoreWrapper(headerAndFooterWrapper);
        zixunList.setLayoutManager(new LinearLayoutManager(mActivity));
        zixunList.setAdapter(loadMoreWrapper);
        zixunList.setOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefresh.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curentPage = 1;
                isPullDown = true;
                loadData();
            }
        });
        loadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                curentPage++;
                isPullDown = false;
                //请求文章列表
                getArticle();
            }
        });

        articleAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mActivity,ArticleDetailActivity.class);
                intent.putExtra("id",articlelist.get(position-1).getId());
                intent.putExtra("title",articlelist.get(position-1).getDetail().getTitle());
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
        if (AppApplication.get().getSp().getBoolean(Constant.IS_CLOD,false)){
            return;
        }
        if (isPullDown){
            swipeRefresh.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefresh.setRefreshing(true);
                }
            });
        }
        //请求头部信息
        getHeaderInfo();
        //请求文章列表
        getArticle();
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

    public void getHeaderInfo() {
        DiscoverApi.getFind(mFragment,new JsonCallback<LzyResponse<FindBean>>(){
            @Override
            public void onSuccess(Response<LzyResponse<FindBean>> response) {

                banner.clear();
                bannerString.clear();
                bannerTitle.clear();
                if (null!=response.body().data.getBanner() && response.body().data.getBanner().size()>0){
                    banner.addAll(response.body().data.getBanner());
                    for (FindBean.Banner ban:banner){
                        bannerString.add(ban.getDetail().getImg());
                        bannerTitle.add(ban.getDetail().getTitle());
                    }
                }
                //设置图片集合
                bannerImg.setImages(bannerString);
                bannerImg.setBannerTitles(bannerTitle);
                //banner设置方法全部调用完毕时最后调用
                bannerImg.start();

                icos.clear();
                if (null!=response.body().data.getIco()&&response.body().data.getIco().size()>0){
                    rvIcon.setVisibility(View.VISIBLE);
                    space.setVisibility(View.VISIBLE);
                    icos.addAll(response.body().data.getIco());
                }else {
                    rvIcon.setVisibility(View.GONE);
                    space.setVisibility(View.GONE);
                }
                icoAdapter.notifyDataSetChanged();
                articleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<FindBean>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }
        });
    }

    public void getArticle() {
        DiscoverApi.getArticle(mFragment,curentPage,Constant.PAGE_SIZE,new JsonCallback<LzyResponse<CommonListBean<ArticleBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<ArticleBean>>> response) {
                doSuccess(response.body().data);
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<CommonListBean<ArticleBean>>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<ArticleBean>>> response) {
                super.onError(response);
                if (!isPullDown){
                    curentPage--;
                }
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void doSuccess(CommonListBean<ArticleBean> data) {
        if (null==data||null==data.getList()){
            swipeRefresh.setRefreshing(false);
            ToastUtil.show(getString(R.string.zanwushuju));
            return;
        }

        if (isPullDown){
            articlelist.clear();
            if (data.getList().size()>0){
                articlelist.addAll(data.getList());
                if (data.getList().size()==20){
                    loadMoreWrapper.setLoadMoreView(R.layout.view_footer_recycler_layout);
                }
            }else {
                loadMoreWrapper.setLoadMoreView(0);
            }
        }else {
            if (data.getList().size()>0&&data.getList().size()<20){
                loadMoreWrapper.setLoadMoreView(0);
            }
            articlelist.addAll(data.getList());
        }
        swipeRefresh.setRefreshing(false);
        loadMoreWrapper.notifyDataSetChanged();
    }
}
