package com.inwecrypto.wallet.ui.me.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.base.BaseFragment;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.MailBean;
import com.inwecrypto.wallet.bean.MailIconBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.MeApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.event.KeyEvent;
import com.inwecrypto.wallet.ui.me.activity.MailDetaileActivity;
import com.inwecrypto.wallet.ui.me.adapter.MailListAdapter;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class MailListFragment extends BaseFragment {

    @BindView(R.id.mail_list)
    RecyclerView mailList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private ArrayList<MailBean> mails=new ArrayList<>();
    private MailListAdapter adapter;

    private int type;
    private boolean address;

    @Override
    protected int setLayoutID() {
        return R.layout.me_fragment_mail_list;
    }

    @Override
    protected void initView() {

        Bundle bundle=getArguments();
        if (null!=bundle){
            type=bundle.getInt("type");
            address=bundle.getBoolean("address");
        }
        isOpenEventBus=true;
        setLazyOpen(true);
        adapter=new MailListAdapter(mContext,R.layout.me_item_mail_list,mails);
        mailList.setLayoutManager(new LinearLayoutManager(mContext));
        mailList.setAdapter(adapter);

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
                loadData();
            }
        });

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (address) {
                    EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_KEY,new KeyEvent(mails.get(position).getAddress())));
                    mActivity.finish();
                }else {
                    Intent intent=new Intent(mActivity,MailDetaileActivity.class);
                    intent.putExtra("mail",mails.get(position));
                    intent.putExtra("type",type);
                    keepTogo(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void loadData() {
        if (type==1||type==2||type==6||type==7){
            swipeRefresh.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefresh.setRefreshing(true);
                }
            });
            MeApi.contact(mFragment,type,new JsonCallback<LzyResponse<ArrayList<MailBean>>>() {
                @Override
                public void onSuccess(Response<LzyResponse<ArrayList<MailBean>>> response) {
                    LoadSuccess(response);
                }

                @Override
                public void onCacheSuccess(Response<LzyResponse<ArrayList<MailBean>>> response) {
                    super.onCacheSuccess(response);
                    onSuccess(response);
                }

                @Override
                public void onError(Response<LzyResponse<ArrayList<MailBean>>> response) {
                    super.onError(response);
                    ToastUtil.show(getString(R.string.load_error));
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (null!=swipeRefresh){
                        swipeRefresh.setRefreshing(false);
                    }
                }
            });
        }else {
            ToastUtil.show(getString(R.string.jingqingqidai));
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()== Constant.EVENT_REFRESH&&(type==1||type==2||type==6||type==7)){
            loadData();
        }
    }

    private void LoadSuccess(Response<LzyResponse<ArrayList<MailBean>>> response) {
        boolean isSave=false;
        String mailIco= App.get().getSp().getString(Constant.MAIL_ICO,"[]");
        ArrayList<MailIconBean> mailId = GsonUtils.jsonToArrayList(mailIco, MailIconBean.class);
        HashMap<Integer,Integer> mailHash=new HashMap<>();
        for (int i=0;i<mailId.size();i++){
            mailHash.put(mailId.get(i).getId(),mailId.get(i).getIcon());
        }
        mails.clear();
        if (null!=response.body().data){
            ArrayList<MailBean> mailsBean = response.body().data;
            for (int i=0;i<mailsBean.size();i++){
                if (null==mailHash.get(mailsBean.get(i).getId())){
                    int icon= AppUtil.getRoundmIcon();
                    mailHash.put(mailsBean.get(i).getId(),icon);
                    mailsBean.get(i).setHeadImg(AppUtil.getRoundmHeadIcon(icon));
                    isSave=true;
                }else {
                    mailsBean.get(i).setHeadImg(AppUtil.getRoundmHeadIcon(mailHash.get(mailsBean.get(i).getId())));
                }
            }
            mails.addAll(mailsBean);
        }
        adapter.notifyDataSetChanged();

        if (isSave){
            mailId.clear();
            Iterator iter = mailHash.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Integer key = (Integer) entry.getKey();
                Integer val = (Integer) entry.getValue();
                mailId.add(new MailIconBean(key,val));
            }
            App.get().getSp().putString(Constant.MAIL_ICO,GsonUtils.objToJson(mailId));
        }
    }
}
