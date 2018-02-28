package com.inwecrypto.wallet.ui.news;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.TradingProjectDetaileBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.RatingBar;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/2/9 15:23
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class TradingProjectActivity extends BaseActivity {

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
    @BindView(R.id.symble)
    TextView symble;
    @BindView(R.id.block_chain)
    TextView blockChain;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.charge)
    TextView charge;
    @BindView(R.id.volume)
    TextView volume;
    @BindView(R.id.no)
    TextView no;
    @BindView(R.id.ratingbar)
    RatingBar ratingbar;
    @BindView(R.id.fenshu)
    TextView fenshu;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.paiming)
    TextView paiming;
    @BindView(R.id.shizhi)
    TextView shizhi;
    @BindView(R.id.liuliang)
    TextView liuliang;
    @BindView(R.id.zongliang)
    TextView zongliang;
    @BindView(R.id.icojiage)
    TextView icojiage;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.chart)
    FrameLayout chart;
    @BindView(R.id.explore)
    LinearLayout explore;
    @BindView(R.id.wallet)
    LinearLayout wallet;
    @BindView(R.id.tokenholder)
    LinearLayout tokenholder;
    private TradingProjectDetaileBean project;

    @Override
    protected void getBundleExtras(Bundle extras) {
        project = (TradingProjectDetaileBean) extras.getSerializable("project");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.trading_project_activity;
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

        txtRightTitle.setText(R.string.pingjia);
        txtRightTitle.setCompoundDrawables(null, null, null, null);

        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //输入密码
                FragmentManager fm = getSupportFragmentManager();
                ProjectStartFragment input = new ProjectStartFragment();
                Bundle bundle=new Bundle();
                if (null!=project.getCategory_user()&&null!=project.getCategory_user().getScore()){
                    bundle.putBoolean("isSb",true);
                    bundle.putString("num",project.getCategory_user().getScore());
                }else {
                    bundle.putBoolean("isSb",false);
                    bundle.putString("num","0.0");
                }
                input.setArguments(bundle);
                input.show(fm,"start");
                input.setOnNextListener(new ProjectStartFragment.OnNextInterface() {
                    @Override
                    public void onNext(final float fen, final Dialog dialog) {
                        ZixunApi.projectScore(this, project.getId(), fen, new JsonCallback<LzyResponse<Object>>() {
                                    @Override
                                    public void onSuccess(Response<LzyResponse<Object>> response) {
                                        ToastUtil.show(getString(R.string.pingfenchenggong));
                                        if (null==project.getCategory_user()){
                                            TradingProjectDetaileBean.CategoryUserBean categoryUserBean=new TradingProjectDetaileBean.CategoryUserBean();
                                            categoryUserBean.setScore(fen+"");
                                            project.setCategory_user(categoryUserBean);
                                        }else {
                                            project.getCategory_user().setScore(fen+"");
                                        }
                                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PINGLUN,fen+""));
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onError(Response<LzyResponse<Object>> response) {
                                        super.onError(response);
                                        ToastUtil.show(getString(R.string.caozuoshibai));
                                    }
                                }
                        );
                    }
                });

            }
        });

        if (null!=project.getImg()){
            Glide.with(this)
                    .load(project.getImg())
                    .priority(Priority.LOW)
                    .crossFade()
                    .into(img);
        }

        name.setText(project.getName());
        symble.setText("(" + project.getLong_name() + ")");
        blockChain.setText(project.getIndustry());

        if (null!=project.getCategory_score()){
            if (App.get().isZh()){
                no.setText("第" + project.getCategory_score().getSort() + "名");
            }else {
                no.setText("No." + project.getCategory_score().getSort());
            }
            ratingbar.setStar((float) project.getCategory_score().getValue());
            fenshu.setText(new BigDecimal(project.getCategory_score().getValue()).setScale(1, RoundingMode.HALF_UP).toPlainString()+"分");
        }else {
            ratingbar.setStar(0);
            fenshu.setText("0"+getString(R.string.fenshu));
        }

        if (null!=project.getCategory_desc()){
            time.setText(project.getCategory_desc().getStart_at() + "-" + project.getCategory_desc().getEnd_at());
        }

        if (null!=project.getIco()){
            paiming.setText(project.getIco().getRank());
            shizhi.setText(project.getIco().getMarket_cap_usd());
            liuliang.setText(project.getIco().getAvailable_supply());
            zongliang.setText(project.getIco().getTotal_supply());
            icojiage.setText(project.getIco_price());

            if (App.get().isZh()){
                price.setText(null==project.getIco().getPrice_cny()?"¥0.00":("¥"+project.getIco().getPrice_cny()));
                volume.setText("Volume (24h)："+(null==project.getIco().get_$24h_volume_cny()?"":project.getIco().get_$24h_volume_cny()));
            }else {
                price.setText(null==project.getIco().getPrice_usd()?"$0.00":("$"+project.getIco().getPrice_usd()));
                volume.setText("Volume (24h)："+(null==project.getIco().get_$24h_volume_usd()?"":project.getIco().get_$24h_volume_usd()));
            }
            if (project.getIco().getPercent_change_24h().contains("-")){
                charge.setTextColor(Color.parseColor("#FF680F"));
                charge.setText(null==project.getIco().getPercent_change_24h()?"(-0.00%)":("("+project.getIco().getPercent_change_24h()+"%)"));
            }else {
                charge.setTextColor(Color.parseColor("#008C55"));
                charge.setText(null==project.getIco().getPercent_change_24h()?"(+0.00%)":("(+"+project.getIco().getPercent_change_24h()+"%)"));
            }
        }

        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null==project.getCategory_explorer()
                        ||project.getCategory_explorer().size()==0){
                    ToastUtil.show(getString(R.string.wuliulanqi));
                    return;
                }
                //弹出选择框
                showExploreDialog();
            }
        });

        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null==project.getCategory_wallet()
                        ||project.getCategory_wallet().size()==0){
                    ToastUtil.show(getString(R.string.wuqianbao));
                    return;
                }
                //弹出选择框
                showWalletDialog();
            }
        });

        tokenholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null==project.getToken_holder()||project.getToken_holder().length()==0){
                    ToastUtil.show(getString(R.string.wutokenholder));
                    return;
                }

                Intent intent=new Intent(mActivity,ProjectNewsWebActivity.class);
                intent.putExtra("title","TokenHolder");
                intent.putExtra("url",project.getToken_holder());
                intent.putExtra("show",false);
                keepTogo(intent);
            }
        });

    }

    private void showWalletDialog() {
        View selectPopupWin = LayoutInflater.from(this).inflate(R.layout.popup_bottom_layout, null, false);
        RecyclerView list = (RecyclerView) selectPopupWin.findViewById(R.id.list);

        final ArrayList<String> data=new ArrayList<>();
        for (TradingProjectDetaileBean.CategoryWalletBean wallet:project.getCategory_wallet())
        {
            data.add(wallet.getName());
        }

        list.setLayoutManager(new LinearLayoutManager(this));
        CommonAdapter<String> adapter = new CommonAdapter<String>(this, R.layout.popup_bottom_item, data) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                if (position == (mDatas.size() - 1)) {
                    holder.getView(R.id.line).setVisibility(View.INVISIBLE);
                } else {
                    holder.getView(R.id.line).setVisibility(View.VISIBLE);
                }
                holder.setText(R.id.title, s);
            }
        };
        list.setAdapter(adapter);


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
        wallet.getLocationOnScreen(location);
        //这里就可自定义在上方和下方了 ，这种方式是为了确定在某个位置，某个控件的左边，右边，上边，下边都可以
        window.showAtLocation(wallet, Gravity.NO_GRAVITY, (int) (location[0] + (wallet.getWidth()-width)/2), (location[1] -(popupHeight*3/2)));

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mActivity,ProjectNewsWebActivity.class);
                intent.putExtra("title",project.getCategory_wallet().get(position).getName());
                intent.putExtra("url",project.getCategory_wallet().get(position).getUrl());
                intent.putExtra("show",false);
                keepTogo(intent);
                window.dismiss();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void showExploreDialog() {
        View selectPopupWin = LayoutInflater.from(this).inflate(R.layout.popup_bottom_layout, null, false);
        RecyclerView list = (RecyclerView) selectPopupWin.findViewById(R.id.list);

        final ArrayList<String> data=new ArrayList<>();
        for (TradingProjectDetaileBean.CategoryExplorerBean explorer:project.getCategory_explorer())
        {
            data.add(explorer.getName());
        }

        list.setLayoutManager(new LinearLayoutManager(this));
        CommonAdapter<String> adapter = new CommonAdapter<String>(this, R.layout.popup_bottom_item, data) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                if (position == (mDatas.size() - 1)) {
                    holder.getView(R.id.line).setVisibility(View.INVISIBLE);
                } else {
                    holder.getView(R.id.line).setVisibility(View.VISIBLE);
                }
                holder.setText(R.id.title, s);
            }
        };
        list.setAdapter(adapter);

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
        explore.getLocationOnScreen(location);
        //这里就可自定义在上方和下方了 ，这种方式是为了确定在某个位置，某个控件的左边，右边，上边，下边都可以
        window.showAtLocation(explore, Gravity.NO_GRAVITY, (int) (location[0] + (explore.getWidth()-width)/2), location[1] - popupHeight);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mActivity,ProjectNewsWebActivity.class);
                intent.putExtra("title",project.getCategory_explorer().get(position).getName());
                intent.putExtra("url",project.getCategory_explorer().get(position).getUrl());
                intent.putExtra("show",false);
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

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
