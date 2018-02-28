package com.inwecrypto.wallet.ui.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.ArticleDetaileBean;
import com.inwecrypto.wallet.bean.ArticleListBean;
import com.inwecrypto.wallet.bean.CandyBowBean;
import com.inwecrypto.wallet.bean.ExchangeNoticeBean;
import com.inwecrypto.wallet.bean.ProjectDetaileBean;
import com.inwecrypto.wallet.bean.TradingProjectDetaileBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.market.activity.MarketDetaileActivity;
import com.inwecrypto.wallet.ui.news.adapter.ExchangeNoticeAdapater;
import com.inwecrypto.wallet.ui.news.adapter.InwehotAdapter;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/2/9 10:57
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class TradingActivity extends BaseActivity {


    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title1)
    ImageView txtRightTitle1;
    @BindView(R.id.txt_right_title2)
    ImageView txtRightTitle2;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayoutCompat swipeRefresh;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.chart)
    FrameLayout chart;
    @BindView(R.id.gaikuang)
    LinearLayout gaikuang;
    @BindView(R.id.history)
    TextView history;
    @BindView(R.id.projet)
    TextView projet;

    private InwehotAdapter adapter;
    private ArrayList<ArticleDetaileBean> data=new ArrayList<>();

    private int page=1;
    private boolean isEnd;

    private boolean isFirst;

    private ProjectDetaileBean project;

    private TradingProjectDetaileBean tradingProject;

    private int type;

    @Override
    protected void getBundleExtras(Bundle extras) {
        project= (ProjectDetaileBean) extras.getSerializable("project");
        type=extras.getInt("type");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.trading_activity;
    }

    @Override
    protected void initView() {

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtMainTitle.setText(project.getUnit());

        txtRightTitle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null==tradingProject){
                    ToastUtil.show(getString(R.string.shujuhuoqushibai));
                    return;
                }
                Intent intent=new Intent(mActivity,ProjectHomeActivity.class);
                intent.putExtra("project",tradingProject);
                keepTogo(intent);
            }
        });

        txtRightTitle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=project.getCategory_user()&&project.getCategory_user().isIs_favorite()){
                    collectProject(false);
                }else {
                    collectProject(true);
                }
            }
        });

        if(null!=project.getCategory_user()&&project.getCategory_user().isIs_favorite()){
            //已收藏
            txtRightTitle2.setImageResource(R.mipmap.xiangmuzhuye_xing_cio);
        }else {
            //未收藏
            txtRightTitle2.setImageResource(R.mipmap.zhuye_jiaoyizhong_xing_ico);
        }

        adapter=new InwehotAdapter(this,R.layout.inwe_hot_item,data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        list.setLayoutManager(linearLayoutManager);
        list.setAdapter(adapter);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isFirst){
                    page++;
                }else {
                    page=1;
                }
                isEnd=false;
                initData();
            }
        });

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mActivity,ProjectNewsWebActivity.class);
                intent.putExtra("title",data.get(position).getTitle());
                intent.putExtra("url", (App.isMain? Url.MAIN_NEWS:Url.TEST_NEWS)+data.get(position).getId());
                intent.putExtra("id",data.get(position).getId());
                keepTogo(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        gaikuang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出选择框
                showSelectDialog();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null==tradingProject){
                    ToastUtil.show(getString(R.string.shujuhuoqushibai));
                    return;
                }
                Intent intent=new Intent(mActivity,ProjectHistoryActivity.class);
                intent.putExtra("project",tradingProject);
                keepTogo(intent);
            }
        });

        projet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null==tradingProject){
                    ToastUtil.show(getString(R.string.shujuhuoqushibai));
                    return;
                }
                if (null!=tradingProject&&null!=tradingProject.getCategory_presentation()){
                    Intent intent=new Intent(mActivity,ProjectNewsWebActivity.class);
                    intent.putExtra("title",project.getName());
                    intent.putExtra("content", tradingProject.getCategory_presentation().getContent());
                    intent.putExtra("show",false);
                    keepTogo(intent);
                }
            }
        });
    }

    private void showSelectDialog() {
        View selectPopupWin = LayoutInflater.from(this).inflate(R.layout.popup_bottom_layout, null, false);
        RecyclerView list = (RecyclerView) selectPopupWin.findViewById(R.id.list);

        final ArrayList<String> data=new ArrayList<>();
        data.add(getString(R.string.xiangmuquanlan));
        data.add(getString(R.string.shishihangqing));
        data.add(getString(R.string.jiaoyishichang));

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new CommonAdapter<String>(this,R.layout.popup_bottom_item,data){
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                if (position==(mDatas.size()-1)){
                    holder.getView(R.id.line).setVisibility(View.INVISIBLE);
                }else {
                    holder.getView(R.id.line).setVisibility(View.VISIBLE);
                }
                holder.setText(R.id.title,s);
            }
        });

        selectPopupWin.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = selectPopupWin.getMeasuredHeight();  //获取测量后的高度
        int[] location = new int[2];

        int width=(int) (ScreenUtils.getScreenWidth(this)/3.2);

        final PopupWindow window = new PopupWindow(selectPopupWin, width, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.update();
        gaikuang.getLocationOnScreen(location);
        //这里就可自定义在上方和下方了 ，这种方式是为了确定在某个位置，某个控件的左边，右边，上边，下边都可以
        window.showAtLocation(gaikuang, Gravity.NO_GRAVITY, (int) (location[0] + (gaikuang.getWidth()-width)/2), location[1] - popupHeight);

        ((CommonAdapter)list.getAdapter()).setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (null==tradingProject){
                    ToastUtil.show(getString(R.string.shujuhuoqushibai));
                    return;
                }
                Intent intent=null;
                switch (position){
                    case 0:
                        intent=new Intent(mActivity,TradingProjectActivity.class);
                        break;
                    case 1:
                        intent=new Intent(mActivity,MarketDetaileActivity.class);
                        break;
                    case 2:
                        intent=new Intent(mActivity,TradingMarketActivity.class);
                        break;
                }
                intent.putExtra("project",tradingProject);
                keepTogo(intent);
                window.dismiss();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void initData() {

        //获取项目详情
        getProjectDetaile();

        //获取项目新闻
        ZixunApi.getProjectNews(this,project.getId(), page, new JsonCallback<LzyResponse<ArticleListBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArticleListBean>> response) {
                if (!response.isFromCache()){
                    if (!isFirst){
                        isFirst=true;
                    }
                }
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

    private void getProjectDetaile() {
        ZixunApi.getProjectDetaile(this, project.getId(), new JsonCallback<LzyResponse<TradingProjectDetaileBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<TradingProjectDetaileBean>> response) {
                tradingProject=response.body().data;
            }

            @Override
            public void onError(Response<LzyResponse<TradingProjectDetaileBean>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.xiangmuxinxihuoqushibai));
            }
        });
    }

    private void LoadSuccess(Response<LzyResponse<ArticleListBean>> response) {
        int start=0;
        int count=0;
        if (page==1){
            data.clear();
            if (1>=response.body().data.getLast_page()){
                isEnd=true;
                swipeRefresh.setEnabled(false);
            }
        }else {
            if (page>=response.body().data.getLast_page()){
                isEnd=true;
                swipeRefresh.setEnabled(false);
            }
        }

        if (null!=response.body().data.getData()){
            count=response.body().data.getData().size();
            Collections.reverse(response.body().data.getData());
            data.addAll(0,response.body().data.getData());
        }
        adapter.notifyItemRangeInserted(start,count);
    }

    private void collectProject(final boolean isCllect){
        ZixunApi.collectProject(this, project.getId(), isCllect, new JsonCallback<LzyResponse<Object>>() {
            @Override
            public void onSuccess(Response<LzyResponse<Object>> response) {
                changeStart(isCllect);
            }

            @Override
            public void onError(Response<LzyResponse<Object>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.caozuoshibai));
            }
        });
    }

    private void changeStart(boolean isCllect) {
        if (isCllect){
            //已收藏
            txtRightTitle2.setImageResource(R.mipmap.xiangmuzhuye_xing_cio);
        }else {
            //未收藏
            txtRightTitle2.setImageResource(R.mipmap.zhuye_jiaoyizhong_xing_ico);
        }
        if (null==project.getCategory_user()){
            ProjectDetaileBean.CategoryUserBean categoryUserBean=new ProjectDetaileBean.CategoryUserBean();
            categoryUserBean.setIs_favorite(isCllect);
            project.setCategory_user(categoryUserBean);
        }else {
            project.getCategory_user().setIs_favorite(isCllect);
        }
        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFERSH,0));
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()==Constant.EVENT_SHOUCANG){
            changeStart((Boolean) event.getData());
        }

        if (event.getEventCode()==Constant.EVENT_DINGZHI){
            if (null==project.getCategory_user()){
                project.setCategory_user(new ProjectDetaileBean.CategoryUserBean());
            }
            project.getCategory_user().setIs_top((Boolean) event.getData());
            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFERSH,type));
        }

        if (event.getEventCode()==Constant.EVENT_PINGLUN){
            if (null==project.getCategory_user()){
                project.setCategory_user(new ProjectDetaileBean.CategoryUserBean());
            }
            project.getCategory_user().setScore((String) event.getData());
            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFERSH,type));
        }

        if (event.getEventCode()==Constant.EVENT_TIP){
            if (null==project.getCategory_user()){
                project.setCategory_user(new ProjectDetaileBean.CategoryUserBean());
            }
            project.getCategory_user().setIs_market_follow(true);
            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFERSH,type));
        }
    }
}
