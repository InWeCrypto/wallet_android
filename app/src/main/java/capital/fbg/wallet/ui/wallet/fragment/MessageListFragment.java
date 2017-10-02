package capital.fbg.wallet.ui.wallet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.mikephil.charting.formatter.IFillFormatter;
import com.lzy.okgo.model.Response;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;

import butterknife.BindView;
import capital.fbg.wallet.R;
import capital.fbg.wallet.base.BaseFragment;
import capital.fbg.wallet.bean.CommonListBean;
import capital.fbg.wallet.bean.IcoOrderBean;
import capital.fbg.wallet.bean.MailBean;
import capital.fbg.wallet.bean.MessageBean;
import capital.fbg.wallet.common.Constant;
import capital.fbg.wallet.common.http.LzyResponse;
import capital.fbg.wallet.common.http.api.MarketApi;
import capital.fbg.wallet.common.http.api.MeApi;
import capital.fbg.wallet.common.http.api.WalletApi;
import capital.fbg.wallet.common.http.callback.JsonCallback;
import capital.fbg.wallet.common.util.DensityUtil;
import capital.fbg.wallet.common.util.ToastUtil;
import capital.fbg.wallet.event.BaseEventBusBean;
import capital.fbg.wallet.ui.market.activity.MarketRemindEditActivity;
import capital.fbg.wallet.ui.me.activity.MailDetaileActivity;
import capital.fbg.wallet.ui.me.adapter.MailListAdapter;
import capital.fbg.wallet.ui.wallet.adapter.MessageAdapter;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class MessageListFragment extends BaseFragment {

    @BindView(R.id.mail_list)
    SwipeMenuRecyclerView mailList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private ArrayList<MessageBean> mails=new ArrayList<>();
    private MessageAdapter adapter;
    private LoadMoreWrapper loadMoreWrapper;

    private int type;

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_fragment_message_list;
    }

    @Override
    protected void initView() {

        Bundle bundle=getArguments();
        if (null!=bundle){
            type=bundle.getInt("type");
        }
        isOpenEventBus=true;
        adapter=new MessageAdapter(mContext,R.layout.wallet_item_message,mails);
        loadMoreWrapper=new LoadMoreWrapper(adapter);
        mailList.setLayoutManager(new LinearLayoutManager(mContext));
        mailList.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mActivity);
                deleteItem.setText(getString(R.string.delete));
                deleteItem.setTextSize(14);
                deleteItem.setTextColorResource(R.color.c_ffffff);
                deleteItem.setBackgroundColorResource(R.color.c_fdd930);
                deleteItem.setWidth(DensityUtil.dip2px(mActivity,72));
                deleteItem.setHeight(MATCH_PARENT);
                // 各种文字和图标属性设置。
                swipeRightMenu.addMenuItem(deleteItem); // 在Item左侧添加一个菜单。
            }
        });
        mailList.setAdapter(loadMoreWrapper);
        mailList.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (type==1){
                    return;
                }
                WalletApi.readMessage(mFragment, mails.get(position).getId(), new JsonCallback<LzyResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<Object>> response) {
                        mailList.smoothCloseMenu();
                        ToastUtil.show(getString(R.string.shezhiyidu));
                        loadData();
                    }
                    @Override
                    public void onError(Response<LzyResponse<Object>> response) {
                        super.onError(response);
                        ToastUtil.show(getString(R.string.shezhishibai));
                    }
                });
            }
        });
        mailList.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                    WalletApi.deleteMessage(mFragment, mails.get(menuBridge.getAdapterPosition()).getId(), new JsonCallback<LzyResponse<Object>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<Object>> response) {
                            mailList.smoothCloseMenu();
                            ToastUtil.show(getString(R.string.shanchuchenggong));
                            loadData();
                        }
                        @Override
                        public void onError(Response<LzyResponse<Object>> response) {
                            super.onError(response);
                            ToastUtil.show(getString(R.string.shanchushibai));
                        }
                    });
            }
        });

        mailList.setOnScrollListener(new RecyclerView.OnScrollListener(){
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
                isPullDown=false;
                curentPage++;
                loadData();;
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

        WalletApi.message(mFragment,type,curentPage,Constant.PAGE_SIZE,new JsonCallback<LzyResponse<CommonListBean<MessageBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<MessageBean>>> response) {
                LoadSuccess(response.body().data);
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<MessageBean>>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
                if (!isPullDown){
                    curentPage--;
                }
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()==Constant.EVENT_MESSAGE){
            loadData();
        }
    }

    private void LoadSuccess(CommonListBean<MessageBean> data) {

        if (null==data||null==data.getList()){
            swipeRefresh.setRefreshing(false);
            ToastUtil.show(getString(R.string.zanwushuju));
            return;
        }

        if (isPullDown){
            mails.clear();
            if (data.getList().size()>0){
                mails.addAll(data.getList());
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
            mails.addAll(data.getList());
        }
        swipeRefresh.setRefreshing(false);
        loadMoreWrapper.notifyDataSetChanged();
    }
}
