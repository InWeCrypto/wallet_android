package com.inwecrypto.wallet.ui.news.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.ProjectDetaileBean;
import com.inwecrypto.wallet.bean.ProjectListBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.CustomLinearLayoutManager;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.NoTradingActivity;
import com.inwecrypto.wallet.ui.news.TradingActivity;
import com.inwecrypto.wallet.ui.news.ZixunFragment;
import com.inwecrypto.wallet.ui.news.adapter.ProjectAdatpter;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.ElementType;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/2/8 14:52
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ProjectFragment extends BaseFragment {

    @BindView(R.id.list)
    RecyclerView list;

    private int type;
    private ArrayList<ProjectDetaileBean> data=new ArrayList<>();
    private ProjectAdatpter adatpter;
    private CustomLinearLayoutManager linearLayoutManager;

    private ZixunFragment zixunFragment;

    @Override
    protected int setLayoutID() {
        return R.layout.project_list_fragment;
    }

    @Override
    protected void initView() {
        setOpenEventBus(true);
        setLazyOpen(true);
        type=getArguments().getInt("type");

        zixunFragment= (ZixunFragment) getParentFragment();
        adatpter=new ProjectAdatpter(mContext,R.layout.cproject_item ,data,type);
        linearLayoutManager=new CustomLinearLayoutManager(mContext);
        list.setLayoutManager(linearLayoutManager);
        list.setAdapter(adatpter);

        setList(list);

        adatpter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (1==data.get(position).getType()){
                    Intent intent=new Intent(mActivity, TradingActivity.class);
                    intent.putExtra("project",data.get(position));
                    intent.putExtra("type",type);
                    keepTogo(intent);
                }else {
                    Intent intent=new Intent(mActivity, NoTradingActivity.class);
                    intent.putExtra("project",data.get(position));
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
    public void onResume() {
        super.onResume();
        if (!isFirst&&!isLoadSuccess&&NetworkUtils.isConnected(getContext())){
            loadData();
        }
    }

    @Override
    protected void loadData() {
        ZixunApi.getAllProject(this,1, new JsonCallback<LzyResponse<ProjectListBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ProjectListBean>> response) {
                isLoadSuccess=true;
                LoadSuccess(response);
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<ProjectListBean>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<ProjectListBean>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                isFirst=false;
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFERSH_SUC));
            }
        });
    }

    private void LoadSuccess(Response<LzyResponse<ProjectListBean>> response) {

        ArrayList<ProjectDetaileBean> top=new ArrayList<>();
        ArrayList<ProjectDetaileBean> totle=new ArrayList<>();
        if (null!=response.body().data.getData()){
            for (ProjectDetaileBean projectBean:response.body().data.getData()){
                if (null!=projectBean.getCategory_user()&&projectBean.getCategory_user().isIs_top()){
                    top.add(projectBean);
                }else {
                    totle.add(projectBean);
                }
            }
            totle.addAll(0,top);
        }

        data.clear();
        data.addAll(totle);
        adatpter.notifyDataSetChanged();

        if (!response.isFromCache()){
            if (data.size()>0){
                //请求行情数据
                if (type==1){
                    ArrayList<String> units=new ArrayList<>();
                    for (ProjectDetaileBean projectDetaileBean:data){
                        units.add(projectDetaileBean.getUnit());
                    }
                    ZixunApi.getICOranks(this,units, new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                JSONObject json=new JSONObject(response.body());
                                if (4000==json.getInt("code")){
                                    JSONObject prices=json.getJSONObject("data");
                                    int i=0;
                                    for (ProjectDetaileBean projectDetaileBean:data){
                                        JSONObject unit = prices.getJSONObject(projectDetaileBean.getUnit());
                                        if ("-".equals(unit.getString("id"))){
                                            projectDetaileBean.setHasMarket(false);
                                            continue;
                                        }else {
                                            projectDetaileBean.setHasMarket(true);
                                        }

                                        if (App.get().getUnit()==1){
                                            try{
                                                projectDetaileBean.setPrice(unit.getString("price_cny"));
                                            }catch (Exception e){
                                                projectDetaileBean.setPrice("0.00");
                                            }
                                        }else {
                                            try{
                                                projectDetaileBean.setPrice(unit.getString("price_usd"));
                                            }catch (Exception e){
                                                projectDetaileBean.setPrice("0.00");
                                            }
                                        }
                                        try{
                                            projectDetaileBean.setCharge(unit.getString("percent_change_24h"));
                                        }catch (Exception e){
                                            projectDetaileBean.setCharge("+0.00%");
                                        }
                                        adatpter.notifyItemChanged(i++);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()== Constant.EVENT_REFERSH&&event.getKey1()==type){
            loadData();
        }

        if (event.getEventCode() ==Constant.EVENT_UNIT_CHANGE){
            adatpter.notifyDataSetChanged();
        }
    }
}
