package com.inwecrypto.wallet.ui.wallet.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.wallet.adapter.MainGntAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2017/12/1 14:47
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class DaibiFragment extends BaseFragment {


    @BindView(R.id.list)
    RecyclerView walletList;

    private ArrayList<TokenBean.ListBean> data = new ArrayList<>();
    private MainGntAdapter adapter;

    private SwipRefershCanUseListener listener;

    private String jsonData="[]";


    @Override
    protected int setLayoutID() {
        return R.layout.info_fragment_daibi;
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        if (null != bundle) {
        }
        isOpenEventBus = true;

        adapter = new MainGntAdapter(mContext, R.layout.wallet_main_item, data);

        walletList.setLayoutManager(new LinearLayoutManager(mContext));
        walletList.setAdapter(adapter);
        walletList.getItemAnimator().setChangeDuration(0);
        ((SimpleItemAnimator) walletList.getItemAnimator()).setSupportsChangeAnimations(false);

        walletList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                if (null!=listener){
                    listener.OnCanUse(topRowVerticalPosition >= 0);
                }
            }
        });
        walletList.setNestedScrollingEnabled(false);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

    public void setData(ArrayList<TokenBean.ListBean> list){
        data.clear();
        data.addAll(list);
        if (null!=adapter){
            adapter.notifyDataSetChanged();
        }
    }

    public void setOnSwipRefershCanUseListener(SwipRefershCanUseListener listener){
        this.listener=listener;
    }

    public interface SwipRefershCanUseListener{
        void OnCanUse(boolean canUse);
    }
}

