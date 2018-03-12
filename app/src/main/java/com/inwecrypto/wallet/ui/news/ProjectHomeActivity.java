package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.EventLog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.TradingProjectDetaileBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.RatingBar;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.login.LoginActivity;
import com.inwecrypto.wallet.ui.news.adapter.ProjectHomeAdapter;
import com.lzy.okgo.model.Response;
import com.suke.widget.SwitchButton;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.math.RoundingMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/2/9 15:34
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ProjectHomeActivity extends BaseActivity {
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    ImageView txtRightTitle;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.block_chain)
    TextView blockChain;
    @BindView(R.id.ratingbar)
    RatingBar ratingbar;
    @BindView(R.id.fenshu)
    TextView fenshu;
    @BindView(R.id.state)
    TextView state;
    @BindView(R.id.web)
    TextView web;
    @BindView(R.id.webll)
    LinearLayout webll;
    @BindView(R.id.historyll)
    LinearLayout historyll;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.dingzhi)
    SwitchButton dingzhi;
    @BindView(R.id.zhidingrl)
    RelativeLayout zhidingrl;

    private TradingProjectDetaileBean project;

    private ProjectHomeAdapter adapter;

    @Override
    protected void getBundleExtras(Bundle extras) {

        project= (TradingProjectDetaileBean) extras.getSerializable("project");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.project_home_activity;
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

        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()){
                    keepTogo(LoginActivity.class);
                    return;
                }
                if(null!=project.getCategory_user()&&project.getCategory_user().isIs_favorite()){
                    collectProject(false);
                }else {
                    collectProject(true);
                }
            }
        });

        if(null!=project.getCategory_user()&&project.getCategory_user().isIs_favorite()){
            //已收藏
            txtRightTitle.setImageResource(R.mipmap.xiangmuzhuye_xing_cio);
        }else {
            //未收藏
            txtRightTitle.setImageResource(R.mipmap.zhuye_jiaoyizhong_xing_ico);
        }

        if(null!=project.getImg()){
            Glide.with(this)
                    .load(project.getImg())
                    .crossFade()
                    .into(img);
        }
        name.setText(project.getName()+"("+project.getLong_name()+")");
        blockChain.setText(project.getIndustry());
        if (null!=project.getCategory_score()){
            ratingbar.setStar((float) project.getCategory_score().getValue());
            fenshu.setText(new BigDecimal(project.getCategory_score().getValue()).setScale(1, BigDecimal.ROUND_DOWN).toPlainString()+" "+getString(R.string.yipingfen));
        }

        state.setText(project.getType_name());
        web.setText(project.getWebsite());
        webll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,ProjectNewsWebActivity.class);
                intent.putExtra("title",project.getName());
                intent.putExtra("url",project.getWebsite());
                intent.putExtra("show",false);
                keepTogo(intent);
            }
        });

        historyll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,ProjectHistoryActivity.class);
                intent.putExtra("project",project);
                keepTogo(intent);
            }
        });

        adapter=new ProjectHomeAdapter(this,R.layout.project_home_item,project.getCategory_media());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                // 直接禁止垂直滑动
                return false;
            }
        };
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mActivity,ProjectNewsWebActivity.class);
                intent.putExtra("title",project.getCategory_media().get(position).getName());
                intent.putExtra("url",project.getCategory_media().get(position).getUrl());
                intent.putExtra("show",false);
                keepTogo(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        txtMainTitle.setFocusableInTouchMode(true);
        txtMainTitle.requestFocus();


        if (project.isIs_top()){
            dingzhi.setChecked(true);
        }else {
            dingzhi.setChecked(false);
        }

        if (!App.get().isLogin()){
            zhidingrl.setClickable(true);
            zhidingrl.setFocusable(true);
            zhidingrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    keepTogo(LoginActivity.class);
                }
            });
            dingzhi.setEnabled(false);
            dingzhi.setClickable(false);
            dingzhi.setFocusable(false);
        }else {
            dingzhi.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SwitchButton view, final boolean isChecked) {
                    ZixunApi.projectTop(this, project.getId(), isChecked, new JsonCallback<LzyResponse<Object>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<Object>> response) {
                            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_DINGZHI,isChecked));
                        }

                        @Override
                        public void onError(Response<LzyResponse<Object>> response) {
                            super.onError(response);
                            ToastUtil.show(R.string.caozuoshibai);
                            dingzhi.setChecked(!isChecked);
                        }
                    });
                }

            });
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

    private void collectProject(final boolean isCllect){
        ZixunApi.collectProject(this, project.getId()+"", isCllect, new JsonCallback<LzyResponse<Object>>() {
            @Override
            public void onSuccess(Response<LzyResponse<Object>> response) {
                if (isCllect){
                    //已收藏
                    txtRightTitle.setImageResource(R.mipmap.xiangmuzhuye_xing_cio);
                }else {
                    //未收藏
                    txtRightTitle.setImageResource(R.mipmap.zhuye_jiaoyizhong_xing_ico);
                }
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_SHOUCANG,isCllect));
            }

            @Override
            public void onError(Response<LzyResponse<Object>> response) {
                super.onError(response);
                ToastUtil.show(R.string.caozuoshibai);
            }
        });
    }

}
