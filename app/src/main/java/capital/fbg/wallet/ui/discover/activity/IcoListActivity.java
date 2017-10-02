package capital.fbg.wallet.ui.discover.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;

import butterknife.BindView;
import capital.fbg.wallet.R;
import capital.fbg.wallet.base.BaseActivity;
import capital.fbg.wallet.bean.CommonListBean;
import capital.fbg.wallet.bean.IcoListBean;
import capital.fbg.wallet.common.Constant;
import capital.fbg.wallet.common.http.LzyResponse;
import capital.fbg.wallet.common.http.api.DiscoverApi;
import capital.fbg.wallet.common.http.callback.JsonCallback;
import capital.fbg.wallet.common.util.ToastUtil;
import capital.fbg.wallet.event.BaseEventBusBean;
import capital.fbg.wallet.ui.discover.adapter.IcoListAdapter;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class IcoListActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.ioc_list)
    RecyclerView iocList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private ArrayList<IcoListBean> icos=new ArrayList<>();
    private IcoListAdapter adapter;
    private LoadMoreWrapper loadMoreWrapper;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.discover_activity_ico_list;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.ico_trade_title);
        txtRightTitle.setVisibility(View.GONE);

        adapter=new IcoListAdapter(this,R.layout.discover_item_ico_list,icos);
        loadMoreWrapper=new LoadMoreWrapper(adapter);
        iocList.setLayoutManager(new LinearLayoutManager(this));
        iocList.setAdapter(loadMoreWrapper);
        iocList.setOnScrollListener(new RecyclerView.OnScrollListener(){
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
                initData();
            }
        });

        loadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                curentPage++;
                isPullDown = false;
                initData();
            }
        });

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mActivity,IcoDetailActivity.class);
                intent.putExtra("ico",icos.get(position));
                keepTogo(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        if (isPullDown){
            swipeRefresh.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefresh.setRefreshing(true);
                }
            });
        }
        DiscoverApi.getIco(mActivity,curentPage, Constant.PAGE_SIZE, new JsonCallback<LzyResponse<CommonListBean<IcoListBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<IcoListBean>>> response) {
                LoadSuccess(response.body().data);
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<CommonListBean<IcoListBean>>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<IcoListBean>>> response) {
                super.onError(response);
                if (!isPullDown){
                    curentPage--;
                }
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void LoadSuccess(CommonListBean<IcoListBean> data) {
        swipeRefresh.setRefreshing(false);
        if (null==data||null==data.getList()){
            ToastUtil.show(getString(R.string.zanwushuju));
            return;
        }

        if (isPullDown){
            icos.clear();
            if (data.getList().size()>0){
                icos.addAll(data.getList());
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
            icos.addAll(data.getList());
        }
        loadMoreWrapper.notifyDataSetChanged();
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
