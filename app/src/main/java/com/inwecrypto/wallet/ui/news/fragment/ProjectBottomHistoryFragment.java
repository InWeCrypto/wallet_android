package com.inwecrypto.wallet.ui.news.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.ArticleDetaileBean;
import com.inwecrypto.wallet.bean.ArticleListBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.EndLessOnScrollListener;
import com.inwecrypto.wallet.common.widget.MultiItemTypeSupport;
import com.inwecrypto.wallet.common.widget.WebView4Scroll;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.ProjectNewsWebActivity;
import com.inwecrypto.wallet.ui.news.adapter.InweHotHistoryAdapter;
import com.inwecrypto.wallet.ui.news.adapter.InwehotNewsAdapter;
import com.inwecrypto.wallet.ui.news.adapter.InwehotNewsHistoryAdapter;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/2/9 17:07
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ProjectBottomHistoryFragment extends BaseFragment {
    @BindView(R.id.mail_list)
    RecyclerView list;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private int type=-1;
    private int id;

    private int page = 1;
    private boolean isEnd;
    private boolean isShow;

    private LinearLayoutManager layoutManager;

    private InwehotNewsHistoryAdapter adapter;
    private ArrayList<ArticleDetaileBean> data=new ArrayList<>();
    private EndLessOnScrollListener scrollListener;

    @Override
    protected int setLayoutID() {
        return R.layout.me_fragment_mail_list;
    }

    @Override
    protected void initView() {
        setOpenEventBus(true);

        type=getArguments().getInt("type",1);

        adapter = new InwehotNewsHistoryAdapter(getContext(), data,type, new MultiItemTypeSupport<ArticleDetaileBean>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType==1){
                    return R.layout.inwe_hot_history_quick_item;
                }else {
                    return R.layout.inwe_hot_history_item;
                }
            }

            @Override
            public int getItemViewType(int position, ArticleDetaileBean articleDetaileBean) {
                return (articleDetaileBean.getType()==1||articleDetaileBean.getType()==16)?1:0;
            }
        });
        layoutManager = new LinearLayoutManager(mContext);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
        scrollListener=new EndLessOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                if (isEnd) {
                    if (!isShow && page != 1) {
                        ToastUtil.show(getString(R.string.zanwugengduoshuju));
                        isShow = true;
                    }
                } else {
                    page++;
                    loadData();
                }
            }
        };
        list.addOnScrollListener(scrollListener);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isEnd = false;
                isShow=false;
                scrollListener.reset();
                loadData();
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
    protected void loadData() {
        String typeStr="is_not_category";
        switch (type){
            case 1:
                typeStr="is_not_category&type=[1,2,3,6,16]";
                break;
            case 2:
                typeStr="is_not_category&type=16";
                break;
            case 3:
                typeStr="is_not_category&type=1";
                break;
            case 4:
                typeStr="is_not_category&type=[2,3,6]";
                break;
        }
        ZixunApi.getInweHotHistory(this, page, typeStr, new JsonCallback<LzyResponse<ArticleListBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArticleListBean>> response) {
                LoadSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<ArticleListBean>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
                if (page!=1){
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

    private void LoadSuccess(final Response<LzyResponse<ArticleListBean>> response) {
        if (page==1){
            data.clear();
            if (1>=response.body().data.getLast_page()){
                isEnd=true;
            }
        }else {
            if (page>=response.body().data.getLast_page()){
                isEnd=true;
            }
        }
        if (null!=response.body().data.getData()){
            data.addAll(response.body().data.getData());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()== Constant.EVENT_INWE_HOT_HISTORY_CLICK){
            if (null!=data&&type==event.getKey1()){
                Intent intent=new Intent(mActivity,ProjectNewsWebActivity.class);
                intent.putExtra("title",data.get(event.getKey2()).getTitle());
                intent.putExtra("url", (App.isMain? Url.MAIN_NEWS:Url.TEST_NEWS)+data.get(event.getKey2()).getId());
                intent.putExtra("id",data.get(event.getKey2()).getId());
                intent.putExtra("decs",data.get(event.getKey2()).getDesc());
                intent.putExtra("img",data.get(event.getKey2()).getImg());
                keepTogo(intent);
            }
        }
    }
}
