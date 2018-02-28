package com.inwecrypto.wallet.ui.news.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMTextMessageBody;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.CandyBowBean;
import com.inwecrypto.wallet.bean.CommonProjectBean;
import com.inwecrypto.wallet.bean.ProjectDetaileBean;
import com.inwecrypto.wallet.bean.ProjectListBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.FixSwipeRecyclerView;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.CandyBowActivity;
import com.inwecrypto.wallet.ui.news.ExchangeNoticeActivity;
import com.inwecrypto.wallet.ui.news.InweHotActivity;
import com.inwecrypto.wallet.ui.news.NoTradingActivity;
import com.inwecrypto.wallet.ui.news.NoticeActivity;
import com.inwecrypto.wallet.ui.news.TradingActivity;
import com.inwecrypto.wallet.ui.news.TradingNoticeActivity;
import com.inwecrypto.wallet.ui.news.TradingViewActivity;
import com.inwecrypto.wallet.ui.news.ZixunFragment;
import com.inwecrypto.wallet.ui.news.adapter.CProjectAdatpter;
import com.lzy.okgo.model.Response;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/2/8 14:52
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class CProjectFragment extends BaseFragment {

    @BindView(R.id.list)
    FixSwipeRecyclerView list;

    private ArrayList<CommonProjectBean> marks;
    private ArrayList<ProjectDetaileBean> data=new ArrayList<>();
    private CProjectAdatpter adatpter;
    private LinearLayoutManager linearLayoutManager;
    private ZixunFragment zixunFragment;
    private ArrayList<ProjectDetaileBean> projectlist=new ArrayList<>();

    private SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    protected int setLayoutID() {
        return R.layout.cproject_list_fragment;
    }

    @Override
    protected void initView() {

        setOpenEventBus(true);
        setLazyOpen(true);

        zixunFragment= (ZixunFragment) getParentFragment();
        marks= (ArrayList<CommonProjectBean>) getArguments().getSerializable("marks");
        adatpter=new CProjectAdatpter(mContext,R.layout.cproject_item ,data);
        linearLayoutManager=new LinearLayoutManager(mContext);
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
                    if (null!=data.get(menuBridge.getAdapterPosition()).getCommonProjectBean()){
                        data.get(menuBridge.getAdapterPosition()).getCommonProjectBean().setShow(false);
                        data.remove(menuBridge.getAdapterPosition());
                        //设置缓存文件
                        App.get().getSp().putString(App.isMain?Constant.PROJECT_JSON_MAIN:Constant.PROJECT_JSON_TEST, GsonUtils.objToJson(marks));

                        adatpter.notifyItemRemoved(menuBridge.getAdapterPosition());
                        menuBridge.closeMenu();
                    }else {
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
            }
        });

        zixunFragment.getPullExtend().setRecyclerView(list);
        setList(list);

        list.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View itemView, final int position) {
                if (data.get(position).isCommonProject()){
                    int i=0;
                    Intent intent=null;
                    switch (data.get(position).getCommonProjectBean().getId()){
                        case 0:
                            intent=new Intent(mActivity,InweHotActivity.class);
                            i=0;
                            break;
                        case 1:
                            intent=new Intent(mActivity,TradingViewActivity.class);
                            i=1;
                            break;
                        case 2:
                            intent=new Intent(mActivity,ExchangeNoticeActivity.class);
                            i=2;
                            break;
                        case 3:
                            intent=new Intent(mActivity,CandyBowActivity.class);
                            i=3;
                            break;
                        case 4:
                            intent = new Intent(mActivity,TradingNoticeActivity.class);
                            i=4;
                            break;
                        case 5:
                            intent = new Intent(mActivity,NoticeActivity.class);
                            i=5;
                            break;
                    }
                    intent.putExtra("marks",marks.get(position));
                    keepTogo(intent);
                    if (marks.get(i).isHasMessage()){
                        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(marks.get(i).getType());//指定会话消息未读数清零
                        if (null!=conversation){
                            conversation.markAllMessagesAsRead();
                        }
                        marks.get(i).setHasMessage(false);
                        refreshFix();
                    }
                }else {
                    if (1==data.get(position).getType()){
                        Intent intent=new Intent(mActivity, TradingActivity.class);
                        intent.putExtra("project",data.get(position));
                        intent.putExtra("type",0);
                        keepTogo(intent);
                    }else {
                        Intent intent=new Intent(mActivity, NoTradingActivity.class);
                        intent.putExtra("project",data.get(position));
                        intent.putExtra("type",0);
                        keepTogo(intent);
                    }
                    if (null!=data.get(position).getCategory_user()
                            &&data.get(position).getCategory_user().isIs_favorite_dot()){
                        ZixunApi.cancleDot(this, data.get(position).getId(), new JsonCallback<LzyResponse<Object>>() {
                            @Override
                            public void onSuccess(Response<LzyResponse<Object>> response) {
                                data.get(position).getCategory_user().setIs_favorite_dot(false);
                                adatpter.notifyItemChanged(position);
                            }
                        });
                    }
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void loadData() {
        ZixunApi.getFavoriteProject(this, 1, new JsonCallback<LzyResponse<ProjectListBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ProjectListBean>> response) {
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
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFERSH_SUC));
            }
        });
    }

    private void LoadSuccess(Response<LzyResponse<ProjectListBean>> response) {
        ArrayList<ProjectDetaileBean> fixed=new ArrayList<>();

        for (CommonProjectBean project:marks){
            if (project.isShow()){
                ProjectDetaileBean projectDetaileBean=new ProjectDetaileBean();
                projectDetaileBean.setCommonProject(true);
                projectDetaileBean.setCommonProjectBean(project);
                fixed.add(projectDetaileBean);
            }
        }
        ArrayList<ProjectDetaileBean> top=new ArrayList<>();
        projectlist.clear();
        ArrayList<ProjectDetaileBean> totle=new ArrayList<>();
        if (null!=response.body().data.getData()){
            for (ProjectDetaileBean projectBean:response.body().data.getData()){
                if (null!=projectBean.getCategory_user()&&projectBean.getCategory_user().isIs_top()){
                    top.add(projectBean);
                }else {
                    projectlist.add(projectBean);
                }
            }
            totle.addAll(fixed);
            projectlist.addAll(0,top);
            totle.addAll(projectlist);
        }
        data.clear();
        data.addAll(totle);
        adatpter.notifyDataSetChanged();

        if (!response.isFromCache()){
            //请求糖果盒信息
            if (marks.get(3).isShow()){
                getCandyMessage();
            }else {
                //获取聊天室信息
                getIM();
            }
        }
    }

    private void getCandyMessage() {
        ZixunApi.getCandybow(this, 1, new JsonCallback<LzyResponse<CandyBowBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CandyBowBean>> response) {
                if (null!=response.body().data){
                    if (null!=response.body().data.getList().getData()){
                        CandyBowBean.ListBean.DataBean candy=response.body().data.getList().getData().get(0);
                        marks.get(3).setTime(candy.getYear()+"-"+candy.getMonth()+"-"+candy.getDay());
                        marks.get(3).setLastMessage(candy.getDesc());
                        //设置缓存文件
                        App.get().getSp().putString(App.isMain?Constant.PROJECT_JSON_MAIN:Constant.PROJECT_JSON_TEST, GsonUtils.objToJson(marks));

                        int i=0;
                        for (ProjectDetaileBean project:data){
                            if (project.getCommonProjectBean().getId()==3){
                                adatpter.notifyItemRemoved(i);
                                return;
                            }
                            i++;
                        }
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                //获取聊天室信息
                getIM();
            }
        });
    }

    private void getIM() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();

                for (CommonProjectBean mark : marks) {
                    if (null != conversations.get(mark.getType())) {
                        if (conversations.get(mark.getType()).getUnreadMsgCount() > 0) {
                            mark.setHasMessage(true);
                        }
                        EMTextMessageBody body = (EMTextMessageBody) conversations.get(mark.getType()).getLastMessage().getBody();
                        mark.setTime("" + sdr.format(new Date(conversations.get(mark.getType()).getLastMessage().getMsgTime())));
                        String meg=body.getMessage().replace(":date",mark.getTime());
                        mark.setLastMessage(meg);
                        int count=conversations.get(mark.getType()).getUnreadMsgCount();
                        if (count>0){
                            mark.setHasMessage(true);
                        }
                    }
                }
                //设置缓存文件
                App.get().getSp().putString(App.isMain?Constant.PROJECT_JSON_MAIN:Constant.PROJECT_JSON_TEST, GsonUtils.objToJson(marks));
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adatpter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

        if (event.getEventCode()==Constant.EVENT_REFERSH&&event.getKey1()==0){
            loadData();
        }

        if (event.getEventCode()==Constant.EVENT_FIX){
            if (!marks.get(event.getKey1()).isShow()){
                marks.get(event.getKey1()).setShow(true);
                refreshFix();
            }
        }

        if (event.getEventCode()==Constant.EVENT_ZIXUN_MESSAGE){
            getIM();
        }
    }

    private void refreshFix() {
        //设置缓存文件
        App.get().getSp().putString(App.isMain?Constant.PROJECT_JSON_MAIN:Constant.PROJECT_JSON_TEST, GsonUtils.objToJson(marks));

        ArrayList<ProjectDetaileBean> fixed=new ArrayList<>();

        for (CommonProjectBean project:marks){
            if (project.isShow()){
                ProjectDetaileBean projectDetaileBean=new ProjectDetaileBean();
                projectDetaileBean.setCommonProject(true);
                projectDetaileBean.setCommonProjectBean(project);
                fixed.add(projectDetaileBean);
            }
        }
        ArrayList<ProjectDetaileBean> totle=new ArrayList<>();
        totle.addAll(fixed);
        totle.addAll(projectlist);
        data.clear();
        data.addAll(totle);
        adatpter.notifyDataSetChanged();
    }

}
