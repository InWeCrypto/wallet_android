package capital.fbg.wallet.ui.market.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import capital.fbg.wallet.AppApplication;
import capital.fbg.wallet.R;
import capital.fbg.wallet.base.BaseActivity;
import capital.fbg.wallet.bean.CommonListBean;
import capital.fbg.wallet.bean.MarketRemindBean;
import capital.fbg.wallet.common.Constant;
import capital.fbg.wallet.common.http.LzyResponse;
import capital.fbg.wallet.common.http.api.MarketApi;
import capital.fbg.wallet.common.http.callback.JsonCallback;
import capital.fbg.wallet.common.util.ToastUtil;
import capital.fbg.wallet.event.BaseEventBusBean;
import capital.fbg.wallet.ui.market.adapter.MarketRemindAddAdapter;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class MarketRemindAddActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.market_list)
    SwipeMenuRecyclerView marketList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private ArrayList<MarketRemindBean> marketBeens=new ArrayList<>();
    private MarketRemindAddAdapter adapter;

    @Override
    protected void getBundleExtras(Bundle extras) {
        isOpenEventBus=true;
    }

    @Override
    protected int setLayoutID() {
        return R.layout.market_activity_remind;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.tixing);
        txtRightTitle.setText("完成");
        txtRightTitle.setCompoundDrawables(null,null,null,null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    StringBuilder sb=new StringBuilder();
                    sb.append("[");
                    for (MarketRemindBean market:marketBeens){
                        if (market.getRelation_notification_count()==1){
                            if (market.getRelation_notification().size()==0){
                                if (null==market.getUp()||null==market.getLow()||market.getLow().length()==0||market.getLow().length()==0){
                                    ToastUtil.show("请填写提醒价格！");
                                    return;
                                }
                            }else {
                                if (market.getRelation_notification().get(0).getUpper_limit().length()==0||market.getRelation_notification().get(0).getLower_limit().length()==0){
                                    ToastUtil.show("请填写提醒价格！");
                                    return;
                                }
                            }
                            if (market.getRelation_notification().size()==0){
                                sb.append("{\"market_id\":"+market.getId()
                                        +",\"upper_limit\":"+market.getUp()
                                        +",\"lower_limit\":"+market.getLow()+"},");
                            }else {
                                sb.append("{\"market_id\":"+market.getId()
                                        +",\"upper_limit\":"+market.getRelation_notification().get(0).getUpper_limit()
                                        +",\"lower_limit\":"+market.getRelation_notification().get(0).getLower_limit()+"},");
                            }
                        }
                    }
                    if(sb.length()!=1){
                        sb.delete(sb.length() - 1, sb.length());
                    }
                    sb.append("]");
                    String currency="";
                    if (1== AppApplication.get().getUnit()){
                        currency="cny";
                    }else {
                        currency="usd";
                    }
                    MarketApi.marketNotification(mActivity, sb.toString(),currency, new JsonCallback<LzyResponse<Object>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<Object>> response) {
                            ToastUtil.show(getString(R.string.tianjichenggong));
                            EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_REFRESH));
                            finish();
                        }

                        @Override
                        public void onError(Response<LzyResponse<Object>> response) {
                            super.onError(response);
                            ToastUtil.show(getString(R.string.mail_hit7));
                        }
                    });
            }
        });

        adapter=new MarketRemindAddAdapter(this,R.layout.market_item_add_remind_market,marketBeens);
        marketList.setLayoutManager(new LinearLayoutManager(this));
        marketList.setAdapter(adapter);
        marketList.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (marketBeens.get(position).getRelation_notification_count()==1){
                    marketBeens.get(position).setRelation_notification_count(0);
                }else {
                    marketBeens.get(position).setRelation_notification_count(1);
                }
                adapter.notifyDataSetChanged();
            }
        });
        marketList.setOnScrollListener(new RecyclerView.OnScrollListener(){
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
                initData();
            }
        });
    }

    @Override
    protected void initData() {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
        MarketApi.marketNotification(mActivity, new JsonCallback<LzyResponse<CommonListBean<MarketRemindBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<MarketRemindBean>>> response) {
                LoadSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<MarketRemindBean>>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
                if (null!=swipeRefresh){
                    swipeRefresh.setRefreshing(false);
                }
            }
        });
    }

    private void LoadSuccess(Response<LzyResponse<CommonListBean<MarketRemindBean>>> response) {
        marketBeens.clear();
        if (null!=response.body().data.getList()){
            marketBeens.addAll(response.body().data.getList());
        }
        adapter.notifyDataSetChanged();
        if (null!=swipeRefresh){
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()== Constant.EVENT_REFRESH){
            initData();
        }
    }

}
