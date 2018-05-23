package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonPageBean;
import com.inwecrypto.wallet.bean.SendHongbaoBean;
import com.inwecrypto.wallet.bean.ValueBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.adapter.HongbaoAdapter;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/4/23 16:03
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class HongbaoRecordActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private HongbaoAdapter adapter;
    private ArrayList<SendHongbaoBean> data=new ArrayList<>();

    private int page=1;
    private boolean isEnd;
    private boolean isInit;

    private LoadMoreWrapper loadMoreWrapper;
    private EmptyWrapper emptyWrapper;
    private View loadMore;

    public static int speed = 10000;
    private Timer timer;
    private TimerTask task;
    public int currentBlock;

    private ArrayList<WalletBean> wallet = new ArrayList<>();

    @Override
    protected void getBundleExtras(Bundle extras) {
        wallet= (ArrayList<WalletBean>) extras.getSerializable("wallet");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.hongbao_record_activity;
    }

    @Override
    protected void initView() {

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtMainTitle.setText(R.string.fasongjilu);

        txtRightTitle.setVisibility(View.GONE);

        adapter=new HongbaoAdapter(this,R.layout.hongbao_record_adapter,data);

        emptyWrapper=new EmptyWrapper(adapter);
        emptyWrapper.setEmptyView(R.layout.empty_list_layout);
        loadMoreWrapper=new LoadMoreWrapper(emptyWrapper);
        loadMore= LayoutInflater.from(this).inflate(R.layout.load_more_layout,null,false);
        loadMore.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        loadMoreWrapper.setLoadMoreView(loadMore);
        loadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (isEnd||!isInit){
                    return;
                }
                page++;
                ((TextView)loadMore.findViewById(R.id.load_more)).setText(R.string.jiazaizhong);
                initData();
            }
        });
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(loadMoreWrapper);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                isEnd=false;
                ((TextView)loadMore.findViewById(R.id.load_more)).setText("");
                initData();
            }
        });

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mActivity,HongbaoRecordDetailActivity.class);
                intent.putExtra("redbag",data.get(position));
                keepTogo(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });

        setBlock();
    }

    @Override
    protected void initData() {

        ZixunApi.getSendRecord(this, wallet, page, new JsonCallback<LzyResponse<CommonPageBean<SendHongbaoBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonPageBean<SendHongbaoBean>>> response) {
                setSuccess(response.body().data);
                isInit=true;
            }

            @Override
            public void onError(Response<LzyResponse<CommonPageBean<SendHongbaoBean>>> response) {
                super.onError(response);
                if (page!=1){
                    page--;
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (null==swipeRefresh)return;
                swipeRefresh.setRefreshing(false);
            }
        });

    }

    private void setSuccess(CommonPageBean<SendHongbaoBean> response) {

        if (null==swipeRefresh)return;

        if (response.getCurrent_page()==1){
            data.clear();
        }

        //最后一页
        if (response.getCurrent_page()==response.getLast_page()){
            isEnd=true;
            if (data.size()!=0){
                ((TextView)loadMore.findViewById(R.id.load_more)).setText(R.string.zanwugengduoshuju);
            }
        }

        if (null!=response.getData()){
            data.addAll(response.getData());
        }
        adapter.setCurrentBlock(currentBlock);
        loadMoreWrapper.notifyDataSetChanged();
    }


    private void setBlock() {
        //保存 minBlock
        currentBlock = new BigDecimal(App.get().getSp().getString(Constant.CURRENT_BLOCK, "0")).intValue();

        adapter.setCurrentBlock(currentBlock);

        if (timer == null) {
            timer = new Timer(true);
            task = new TimerTask() {

                @Override
                public void run() {
                    getBlock();
                }
            };
            timer.schedule(task, speed, speed);
        }
    }

    private void getBlock() {
        //获取当前块高
        WalletApi.blockNumber(this, new JsonCallback<LzyResponse<ValueBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ValueBean>> response) {
                //0x181d02
                //进行计算
                BigDecimal currentPrice = new BigDecimal(AppUtil.toD(response.body().data.getValue().replace("0x", "0")));
                currentBlock = currentPrice.intValue();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //刷新数据
                        adapter.setCurrentBlock(currentBlock);
                        emptyWrapper.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
        if (null != task) {
            task.cancel();
            task = null;
        }
        super.onDestroy();
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
