package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonProjectBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.CacheUtils;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.login.LoginActivity;
import com.suke.widget.SwitchButton;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/2/8 20:24
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ProjectDetaileActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.historyll)
    RelativeLayout historyll;
    @BindView(R.id.push)
    SwitchButton push;
    @BindView(R.id.pushrl)
    RelativeLayout pushrl;

    private CommonProjectBean marks;

    @Override
    protected void getBundleExtras(Bundle extras) {
        marks= (CommonProjectBean) extras.getSerializable("marks");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.project_detaile_activity;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtRightTitle.setVisibility(View.GONE);

        txtMainTitle.setText(getString(marks.getName()));

        name.setText(getString(marks.getName()));

        Glide.with(this)
                .load(marks.getImg())
                .priority(Priority.LOW)
                .crossFade()
                .into(img);

        if (marks.isOpenTip()){
            push.setChecked(true);
        }else {
            push.setChecked(false);
        }

        historyll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=null;
                switch (marks.getId()){
                    case 0:
                        intent=new Intent(mActivity,InweHotBottomHistoryActivity.class);
                        keepTogo(intent);
                        break;
                    case 1:
                        intent=new Intent(mActivity,InweViewHistoryActivity.class);
                        keepTogo(intent);
                        break;
                    case 2:
                        intent=new Intent(mActivity,TradingViewHistoryActivity.class);
                        keepTogo(intent);
                        break;
                    case 3:
//                        intent=new Intent(mActivity,TradingNoticeHistoryActivity.class);
//                        keepTogo(intent);
                        break;
                    case 4:
                        intent=new Intent(mActivity,NoticeHistoryActivity.class);
                        keepTogo(intent);
                        break;
                }
            }
        });

        if (!App.get().isLogin()){
            pushrl.setClickable(true);
            pushrl.setFocusable(true);
            pushrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    keepTogo(LoginActivity.class);
                }
            });
            push.setEnabled(false);
            push.setClickable(false);
            push.setFocusable(false);
        }else {

            push.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                    if (!App.get().isLogin()){
                        keepTogo(LoginActivity.class);
                        return;
                    }
                    //获取缓存文件
                    ArrayList<CommonProjectBean> list = CacheUtils.getCache((App.isMain?Constant.PROJECT_JSON_MAIN:Constant.PROJECT_JSON_TEST)
                            +(null==App.get().getLoginBean()?"":App.get().getLoginBean().getEmail()));
                    for (CommonProjectBean projectBean:list){
                        if (projectBean.getId()==marks.getId()){
                            projectBean.setOpenTip(isChecked);
                            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_NOTIFY,isChecked));
                            //设置缓存文件
                            CacheUtils.setCache((App.isMain?Constant.PROJECT_JSON_MAIN:Constant.PROJECT_JSON_TEST)
                                    +(null==App.get().getLoginBean()?"":App.get().getLoginBean().getEmail()),list);
                            return;
                        }
                    }
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
}
