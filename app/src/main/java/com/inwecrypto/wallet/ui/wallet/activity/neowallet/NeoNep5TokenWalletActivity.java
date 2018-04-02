package com.inwecrypto.wallet.ui.wallet.activity.neowallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.ClaimUtxoBean;
import com.inwecrypto.wallet.bean.NeoOderBean;
import com.inwecrypto.wallet.bean.NewNeoGntInfoBean;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.imageloader.GlideCircleTransform;
import com.inwecrypto.wallet.common.util.AnimUtil;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.CacheUtils;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.EndLessOnScrollListener;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.login.LoginActivity;
import com.inwecrypto.wallet.ui.wallet.activity.ReceiveActivity;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import neomobile.Neomobile;

/**
 * Created by Administrator on 2017/7/16.
 * 功能描述：
 * 版本：@version
 */

public class NeoNep5TokenWalletActivity extends BaseActivity {


    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_ch_price)
    TextView tvChPrice;
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.wallet_list)
    RecyclerView walletList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayoutCompat swipeRefresh;
    @BindView(R.id.ll_zhuanzhang)
    LinearLayout llZhuanzhang;
    @BindView(R.id.ll_shoukuan)
    LinearLayout llShoukuan;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.titlell)
    View titlell;
    @BindView(R.id.titlePrice)
    TextView titlePrice;
    @BindView(R.id.tv_hit)
    TextView tvHit;

    private WalletBean wallet;

    private ArrayList<NeoOderBean.ListBean> mails = new ArrayList<>();
    private NeoRecordAdapter adapter;
    private EmptyWrapper emptyWrapper;


    public static int speed = 10000;
    private boolean isFinish;
    private Timer timer;
    private TimerTask task;
    private int screenWidth;
    private int hitWidth;
    private int distance;
    private TokenBean.ListBean tokenBean;

    private int page=0;
    private boolean isEnd;
    private boolean isShow;
    private LinearLayoutManager layoutManager;

    private BigDecimal tokenPrice;

    private TokenBean.RecordBean neoBean;
    private EndLessOnScrollListener scrollListener;


    @Override
    protected void getBundleExtras(Bundle extras) {
        isOpenEventBus = true;
        wallet = (WalletBean) extras.getSerializable("wallet");
        tokenBean = (TokenBean.ListBean) extras.getSerializable("token");
        neoBean= (TokenBean.RecordBean) extras.getSerializable("neo");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.newneo_wallet_acivity_token;
    }

    @Override
    protected void initView() {

        //获取屏幕宽度
        screenWidth = ScreenUtils.getScreenWidth(this);
        //获取提示宽度
        tvHit.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tvHit.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                hitWidth = tvHit.getMeasuredWidth();
                distance = (screenWidth - hitWidth) / 2 - DensityUtil.dip2px(mActivity, 24);
            }
        });
        //获取滑动距离

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title.setText(tokenBean.getName());
        txtRightTitle.setVisibility(View.GONE);

        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    swipeRefresh.setEnabled(true);
                } else {
                    swipeRefresh.setEnabled(false);
                }
                if (verticalOffset == 0) {
                    title.setVisibility(View.VISIBLE);
                    titlell.setVisibility(View.INVISIBLE);
                }
                if (verticalOffset + appBarLayout.getTotalScrollRange() == 0) {
                    if (!isFinish) {
                        title.setVisibility(View.INVISIBLE);
                        titlell.setVisibility(View.VISIBLE);
                        AnimUtil.startShowAnimation(titlell);
                        AnimUtil.startMoveLeftAnimation(tvHit, distance);
                        isFinish = true;
                    }
                } else {
                    if (isFinish) {
                        AnimUtil.startHideAnimation(titlell);
                        AnimUtil.startMoveRightAnimation(tvHit, distance);
                        isFinish = false;
                    }
                }
            }
        });
        llZhuanzhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()){
                    keepTogo(LoginActivity.class);
                    return;
                }
                if (wallet.getType().equals(Constant.GUANCHA)) {
//                    PackageManager pm = getPackageManager();
//                    boolean nfc = pm.hasSystemFeature(PackageManager.FEATURE_NFC);
//                    if (!nfc) {
//                        ToastUtil.show(R.string.no_nfc_hit);
//                        return;
//                    }
                    ToastUtil.show(getString(R.string.nep5lengqianbaotishi));
                    return;
                }
                //请求交易记录
                WalletApi.neoWalletOrder(this,0, wallet.getId(), "NEO", tokenBean.getGnt_category().getAddress(), new JsonCallback<LzyResponse<NeoOderBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<NeoOderBean>> response) {
                        if (null != response && null != response.body().data.getList()) {
                            boolean canGetGas = true;
                            for (int i = 0; i < response.body().data.getList().size(); i++) {
                                if (response.body().data.getList().get(i).getConfirmTime().equals("")) {
                                    canGetGas = false;
                                    break;
                                }
                            }
                            if (canGetGas) {
                                Intent intent = new Intent(mActivity, NeoNep5TransferAccountsActivity.class);
                                intent.putExtra("wallet", wallet);
                                intent.putExtra("token", tokenBean);
                                intent.putExtra("neo",neoBean);
                                keepTogo(intent);
                            } else {
                                ToastUtil.show(getString(R.string.ninhaiyouweiwanchengdedingdan));
                            }
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<NeoOderBean>> response) {
                        super.onError(response);
                        ToastUtil.show(getString(R.string.load_error));
                    }
                });
            }
        });

        llShoukuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ReceiveActivity.class);
                intent.putExtra("wallet", wallet);
                keepTogo(intent);
            }
        });

        if (null!=tokenBean.getGnt_category()&&null!=tokenBean.getGnt_category().getIcon()){
            Glide.with(this).load(tokenBean.getGnt_category().getIcon()).crossFade().into(ivImg);
        }

        BigInteger price=new BigInteger(AppUtil.reverseArray(tokenBean.getBalance()));
        tokenPrice=new BigDecimal(price).divide(new BigDecimal(10).pow(Integer.parseInt(tokenBean.getDecimals())));
        tvPrice.setText(tokenPrice.setScale(4,BigDecimal.ROUND_DOWN).toPlainString());

        boolean isSee=App.get().getSp().getBoolean(Constant.MAIN_SEE,true);
        if (null!=tokenBean.getGnt_category()&&null!=tokenBean.getGnt_category().getCap()){
            if (isSee){
                if (1 == App.get().getUnit()) {
                    tvChPrice.setText("≈￥" + tokenPrice.multiply(new BigDecimal(tokenBean.getGnt_category().getCap().getPrice_cny())).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
                    titlePrice.setText("(￥" + tokenPrice.multiply(new BigDecimal(tokenBean.getGnt_category().getCap().getPrice_cny())).setScale(2, BigDecimal.ROUND_DOWN).toPlainString() + ")");
                } else {
                    tvChPrice.setText("≈$" + tokenPrice.multiply(new BigDecimal(tokenBean.getGnt_category().getCap().getPrice_usd())).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
                    titlePrice.setText("($" + tokenPrice.multiply(new BigDecimal(tokenBean.getGnt_category().getCap().getPrice_usd())).setScale(2, BigDecimal.ROUND_DOWN).toPlainString() + ")");
                }
            }else {
                if (1 == App.get().getUnit()) {
                    tvChPrice.setText("≈￥****");
                    titlePrice.setText("(￥****)");
                } else {
                    tvChPrice.setText("≈$****");
                    titlePrice.setText("($****)");
                }
            }
        }else {
            if (isSee){
                if (1 == App.get().getUnit()) {
                    tvChPrice.setText("≈￥0.00");
                    titlePrice.setText("(￥0.00)");
                } else {
                    tvChPrice.setText("≈$0.00");
                    titlePrice.setText("($0.00)");
                }
            }else {
                if (1 == App.get().getUnit()) {
                    tvChPrice.setText("￥****");
                    titlePrice.setText("(￥****)");
                } else {
                    tvChPrice.setText("$****");
                    titlePrice.setText("($****)");
                }
            }
        }

        adapter = new NeoRecordAdapter(this, wallet.getAddress(), R.layout.wallet_item_neo_token_transfer, mails,tokenBean.getDecimals());
        emptyWrapper=new EmptyWrapper(adapter);

        layoutManager=new LinearLayoutManager(this);
        walletList.setLayoutManager(layoutManager);
        walletList.setAdapter(emptyWrapper);
        scrollListener=new EndLessOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                if (isEnd){
                    if (!isShow&&page!=0){
                        ToastUtil.show(getString(R.string.zanwugengduoshuju));
                        isShow=true;
                    }
                }else {
                    page++;
                    initData();
                }
            }
        };
        walletList.addOnScrollListener(scrollListener);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=0;
                isEnd=false;
                isShow=false;
                scrollListener.reset();
                initData();
            }
        });
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position<0||position>=mails.size()){
                    return;
                }
                if (wallet.getAddress().equals(mails.get(position).getFrom())||(mails.get(position).getFrom().equals(mails.get(position).getTo()))) {
                    Intent intent = new Intent(mActivity, NeoTransferAccountsDetaileActivity.class);
                    intent.putExtra("order", mails.get(position));
                    intent.putExtra("decimals",tokenBean.getDecimals());
                    intent.putExtra("unit", tokenBean.getSymbol());
                    intent.putExtra("isTnc",true);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mActivity, NeoReceiveDetaileActivity.class);
                    intent.putExtra("order", mails.get(position));
                    intent.putExtra("decimals",tokenBean.getDecimals());
                    intent.putExtra("unit", tokenBean.getSymbol());
                    intent.putExtra("isTnc",true);
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        startRound();
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
    }

    private void startRound() {
        if (timer == null) {
            timer = new Timer(true);
            task = new TimerTask() {

                @Override
                public void run() {
                    if (page==0){
                        isEnd=false;
                        isShow=false;
                        scrollListener.reset();
                        //刷新列表
                        refershData();
                    }
                }
            };
            timer.schedule(task, speed, speed);
        }
    }

    @Override
    protected void initData() {
        refershData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (page==0){
            isEnd=false;
            isShow=false;
            scrollListener.reset();
            refershData();
        }
    }

    private void refershData() {
        if (null == tvPrice || null == tvChPrice || null == titlePrice) {
            return;
        }
        if (null == wallet) {
            ToastUtil.show(R.string.qianbaoshujucuowu_qingtuichuchongshi);
            return;
        }

        //请求余额
        getBanlance();

        if (!App.get().isLogin()){
            LzyResponse<NeoOderBean> response= CacheUtils.getCache(Constant.WALLET_ORDER+wallet.getId()+"NEO"+tokenBean.getGnt_category().getAddress()+ App.isMain);
            if (null!=response){
                LoadSuccess(response);
            }
            if (null != swipeRefresh) {
                swipeRefresh.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
            return;
        }

        //请求交易记录
        WalletApi.neoWalletOrder(this,page, wallet.getId(), "NEO", tokenBean.getGnt_category().getAddress(), new JsonCallback<LzyResponse<NeoOderBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<NeoOderBean>> response) {
                LoadSuccess(response.body());
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<NeoOderBean>> response) {
                super.onCacheSuccess(response);
                if (page==0){
                    onSuccess(response);
                }
            }

            @Override
            public void onError(Response<LzyResponse<NeoOderBean>> response) {
                super.onError(response);
                if (NetworkUtils.isConnected(mActivity)){
                    ToastUtil.show(getString(R.string.load_error));
                }
                if (page!=0){
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

    @Override
    protected void onDestroy() {
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
        if (null != task) {
            task.cancel();
            task = null;
        }
        super.onDestroy();
    }

    private void LoadSuccess(LzyResponse<NeoOderBean> response) {
        if (page==0){
            mails.clear();
            if (null != response.data.getList()) {
                if (response.data.getList().size()<10){
                    isEnd=true;
                }
                mails.addAll(response.data.getList());
            }
        }else {
            if (null != response.data.getList()) {
                if (response.data.getList().size()<10){
                    isEnd=true;
                }
                mails.addAll(response.data.getList());
            }
        }
        emptyWrapper.notifyDataSetChanged();
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_PRICE) {
            page=0;
            isEnd=false;
            isShow=false;
            scrollListener.reset();
            initData();
            //请求余额
            getBanlance();
        }
    }

    private void getBanlance() {

        //请求资产
        try {
            if (!App.get().isLogin()){
                LzyResponse<NewNeoGntInfoBean> response= CacheUtils.getCache(Url.GET_NEO_GNT_INFO+"/"+tokenBean.getGnt_category().getAddress()+Neomobile.decodeAddress(wallet.getAddress())+ App.isMain);
                if (null!=response){
                    setBanlance(response);
                }
                return;
            }

            WalletApi.getNeoGntInfo(this, tokenBean.getGnt_category().getAddress(), Neomobile.decodeAddress(wallet.getAddress()), new JsonCallback<LzyResponse<NewNeoGntInfoBean>>() {
                @Override
                public void onSuccess(Response<LzyResponse<NewNeoGntInfoBean>> response) {
                    setBanlance(response.body());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBanlance(LzyResponse<NewNeoGntInfoBean> response) {
        if (null == tvPrice || null == titlePrice) {
            return;
        }
        if (null==response.data.getBalance()){
            return;
        }
        tokenBean.setBalance(response.data.getBalance());
        BigInteger price=new BigInteger(AppUtil.reverseArray(response.data.getBalance()));
        tokenPrice=new BigDecimal(price).divide(new BigDecimal(10).pow(Integer.parseInt(response.data.getDecimals())));
        tvPrice.setText(tokenPrice.setScale(4, BigDecimal.ROUND_DOWN).toPlainString());
    }

}
