package com.inwecrypto.wallet.ui.news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.ProjectDetaileBean;
import com.inwecrypto.wallet.bean.ProjectListBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.FixSwipeRecyclerView;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.login.LoginActivity;
import com.inwecrypto.wallet.ui.news.NoTradingActivity;
import com.inwecrypto.wallet.ui.news.TradingActivity;
import com.inwecrypto.wallet.ui.news.adapter.CProjectAdatpter;
import com.lzy.okgo.model.Response;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;

import net.qiujuer.genius.ui.widget.Button;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：xiaoji06 on 2018/2/8 14:52
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class CProjectFragment extends BaseFragment {

    @BindView(R.id.list)
    FixSwipeRecyclerView list;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.loginll)
    LinearLayout loginll;

    private ArrayList<ProjectDetaileBean> data = new ArrayList<>();
    private CProjectAdatpter adatpter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<ProjectDetaileBean> projectlist = new ArrayList<>();

    @Override
    protected int setLayoutID() {
        return R.layout.cproject_list_fragment;
    }

    @Override
    protected void initView() {

        setOpenEventBus(true);
        setLazyOpen(true);

        adatpter = new CProjectAdatpter(mContext, R.layout.cproject_item, data);
        linearLayoutManager = new LinearLayoutManager(mContext);
        list.setLayoutManager(linearLayoutManager);

        list.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mActivity);
                deleteItem.setText(getString(R.string.quxiaoshoucang));
                deleteItem.setTextSize(14);
                deleteItem.setTextColorResource(R.color.c_ffffff);
                deleteItem.setBackgroundColorResource(R.color.c_FF841C);
                deleteItem.setWidth(DensityUtil.dip2px(mActivity, 110));
                deleteItem.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                // 各种文字和图标属性设置。
                swipeRightMenu.addMenuItem(deleteItem); // 在Item左侧添加一个菜单。
            }
        });
        list.setAdapter(adatpter);

        list.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(final SwipeMenuBridge menuBridge) {
                if (menuBridge.getPosition() == 0) {//取消收藏
                    ZixunApi.collectProject(this, data.get(menuBridge.getAdapterPosition()).getId(), false, new JsonCallback<LzyResponse<Object>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<Object>> response) {
                            loadData();
                            menuBridge.closeMenu();
                        }

                        @Override
                        public void onError(Response<LzyResponse<Object>> response) {
                            super.onError(response);
                            ToastUtil.show(getString(R.string.caozuoshibai));
                        }
                    });
                }
            }
        });

        list.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View itemView, final int position) {
                if (1 == data.get(position).getType()) {
                    Intent intent = new Intent(mActivity, TradingActivity.class);
                    intent.putExtra("project", data.get(position));
                    intent.putExtra("type", 0);
                    keepTogo(intent);
                } else {
                    Intent intent = new Intent(mActivity, NoTradingActivity.class);
                    intent.putExtra("project", data.get(position));
                    intent.putExtra("type", 0);
                    keepTogo(intent);
                }
                if (null != data.get(position).getCategory_user()
                        && data.get(position).getCategory_user().isIs_favorite_dot()) {
                    ZixunApi.cancleDot(this, data.get(position).getId(), new JsonCallback<LzyResponse<Object>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<Object>> response) {
                            data.get(position).getCategory_user().setIs_favorite_dot(false);
                            adatpter.notifyItemChanged(position);
                        }
                    });
                }
            }
        });

        if (App.get().isLogin()){
            loginll.setVisibility(View.GONE);
        }else {
            loginll.setVisibility(View.VISIBLE);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    keepTogo(LoginActivity.class);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (App.get().isLogin()){
            loginll.setVisibility(View.GONE);
            if (!isFirst && !isLoadSuccess && NetworkUtils.isConnected(getContext())) {
                loadData();
            }
        }else {
            loginll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void loadData() {
        if (!App.get().isLogin()) {
            return;
        }
        ZixunApi.getFavoriteProject(this, 1, new JsonCallback<LzyResponse<ProjectListBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ProjectListBean>> response) {
                isLoadSuccess = true;
                LoadSuccess(response);
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<ProjectListBean>> response) {
                super.onCacheSuccess(response);
                try{
                    onSuccess(response);
                }catch (Exception e){
                }
            }

            @Override
            public void onError(Response<LzyResponse<ProjectListBean>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                isFirst = false;
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFERSH_SUC));
            }
        });
    }

    private void LoadSuccess(Response<LzyResponse<ProjectListBean>> response) {
        ArrayList<ProjectDetaileBean> top = new ArrayList<>();
        projectlist.clear();
        ArrayList<ProjectDetaileBean> totle = new ArrayList<>();
        if (null != response.body().data.getData()) {
            for (ProjectDetaileBean projectBean : response.body().data.getData()) {
                if (null != projectBean.getCategory_user() && projectBean.getCategory_user().isIs_top()) {
                    top.add(projectBean);
                } else {
                    projectlist.add(projectBean);
                }
            }
            projectlist.addAll(0, top);
            totle.addAll(projectlist);
        }
        data.clear();
        data.addAll(totle);
        adatpter.notifyDataSetChanged();

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

        if (event.getEventCode() == Constant.EVENT_REFERSH && event.getKey1() == 0) {
            loadData();
        }

        if (event.getEventCode() ==Constant.EVENT_UNIT_CHANGE){
            adatpter.notifyDataSetChanged();
        }

    }

    public boolean isFirst(){
        if (null==list)return true;
        return !list.canScrollVertically(-1);
    }
}
