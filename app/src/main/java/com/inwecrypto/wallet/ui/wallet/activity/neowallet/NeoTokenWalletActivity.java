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
import com.inwecrypto.wallet.AppApplication;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.NeoOderBean;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.imageloader.GlideCircleTransform;
import com.inwecrypto.wallet.common.util.AnimUtil;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.EndLessOnScrollListener;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.wallet.activity.ReceiveActivity;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/16.
 * 功能描述：
 * 版本：@version
 */

public class NeoTokenWalletActivity extends BaseActivity {


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
    private int type;
    private TokenBean.RecordBean neoBean;

    private int page=0;
    private boolean isEnd;
    private boolean isShow;
    private LinearLayoutManager layoutManager;


    @Override
    protected void getBundleExtras(Bundle extras) {
        isOpenEventBus = true;
        wallet = (WalletBean) extras.getSerializable("wallet");
        type = extras.getInt("type");
        neoBean = (TokenBean.RecordBean) extras.getSerializable("neo");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_acivity_token_new;
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
        if (type == 0) {
            title.setText("NEO");
        } else {
            title.setText("GAS");
        }
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
                if (wallet.getType().equals(Constant.GUANCHA)) {
//                    PackageManager pm = getPackageManager();
//                    boolean nfc = pm.hasSystemFeature(PackageManager.FEATURE_NFC);
//                    if (!nfc) {
//                        ToastUtil.show(R.string.no_nfc_hit);
//                        return;
//                    }
                    ToastUtil.show("暂时不支持冷钱包转账!请转换为热钱包");
                    return;
                }
                //请求交易记录
                WalletApi.neoWalletOrder(this,0, wallet.getId(), "NEO", type == 0 ? Constant.NEO_ASSETS : Constant.GAS_ASSETS, new JsonCallback<LzyResponse<NeoOderBean>>() {
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
                                Intent intent = new Intent(mActivity, NeoTransferAccountsActivity.class);
                                intent.putExtra("wallet", wallet);
                                intent.putExtra("type", type);
                                intent.putExtra("neo", neoBean);
                                //intent.putExtra("price", totleEther.divide(Constant.pEther).setScale(4, BigDecimal.ROUND_HALF_UP).toString());
                                if (wallet.getType().equals(Constant.GUANCHA)) {
                                    intent.putExtra("isClod", true);
                                }
                                keepTogo(intent);
                            } else {
                                ToastUtil.show(getString(R.string.ninhaiyouweiwanchengdedingdan));
                            }
                        } else {
                            Intent intent = new Intent(mActivity, NeoTransferAccountsActivity.class);
                            intent.putExtra("wallet", wallet);
                            intent.putExtra("type", type);
                            intent.putExtra("neo", neoBean);
                            //intent.putExtra("price", totleEther.divide(Constant.pEther).setScale(4, BigDecimal.ROUND_HALF_UP).toString());
                            if (wallet.getType().equals(Constant.GUANCHA)) {
                                intent.putExtra("isClod", true);
                            }
                            keepTogo(intent);
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

        if (type == 0) {
            Glide.with(this).load(R.mipmap.project_icon_neo).transform(new GlideCircleTransform(this)).crossFade().into(ivImg);
            tvPrice.setText(new BigDecimal(neoBean.getBalance()).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString());
            if (1 == AppApplication.get().getUnit()) {
                tvChPrice.setText("≈￥" + new BigDecimal(neoBean.getBalance()).multiply(new BigDecimal(neoBean.getCap().getPrice_cny())).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                titlePrice.setText("(￥" + new BigDecimal(neoBean.getBalance()).multiply(new BigDecimal(neoBean.getCap().getPrice_cny())).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + ")");
            } else {
                tvChPrice.setText("≈$" + new BigDecimal(neoBean.getBalance()).multiply(new BigDecimal(neoBean.getCap().getPrice_usd())).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                titlePrice.setText("($" + new BigDecimal(neoBean.getBalance()).multiply(new BigDecimal(neoBean.getCap().getPrice_usd())).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + ")");
            }
        } else {
            Glide.with(this).load(R.mipmap.project_icon_gas).transform(new GlideCircleTransform(this)).crossFade().into(ivImg);
            tvPrice.setText(new BigDecimal(neoBean.getGnt().get(0).getBalance()).setScale(8, BigDecimal.ROUND_HALF_UP).toPlainString());
            if (1 == AppApplication.get().getUnit()) {
                tvChPrice.setText("≈￥" + new BigDecimal(neoBean.getGnt().get(0).getBalance()).multiply(new BigDecimal(neoBean.getGnt().get(0).getCap().getPrice_cny())).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                titlePrice.setText("(￥" + new BigDecimal(neoBean.getGnt().get(0).getBalance()).multiply(new BigDecimal(neoBean.getGnt().get(0).getCap().getPrice_cny())).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + ")");
            } else {
                tvChPrice.setText("≈$" + new BigDecimal(neoBean.getGnt().get(0).getBalance()).multiply(new BigDecimal(neoBean.getGnt().get(0).getCap().getPrice_usd())).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                titlePrice.setText("($" + new BigDecimal(neoBean.getGnt().get(0).getBalance()).multiply(new BigDecimal(neoBean.getGnt().get(0).getCap().getPrice_usd())).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + ")");
            }
        }

        adapter = new NeoRecordAdapter(this, wallet.getAddress(), R.layout.wallet_item_neo_token_transfer, mails);
        emptyWrapper=new EmptyWrapper(adapter);

        layoutManager=new LinearLayoutManager(this);
        walletList.setLayoutManager(layoutManager);
        walletList.setAdapter(emptyWrapper);
        walletList.addOnScrollListener(new EndLessOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                if (isEnd){
                    if (!isShow){
                        ToastUtil.show(getString(R.string.zanwugengduoshuju));
                        isShow=true;
                    }
                }else {
                    page++;
                    initData();
                }
            }
        });

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=0;
                isEnd=false;
                initData();
            }
        });
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (wallet.getAddress().equals(mails.get(position).getFrom())||(mails.get(position).getFrom().equals(mails.get(position).getTo()))) {
                    Intent intent = new Intent(mActivity, NeoTransferAccountsDetaileActivity.class);
                    intent.putExtra("order", mails.get(position));
                    intent.putExtra("unit", type == 0 ? "neo" : "gas");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mActivity, NeoReceiveDetaileActivity.class);
                    intent.putExtra("order", mails.get(position));
                    intent.putExtra("unit", type == 0 ? "neo" : "gas");
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
                    //刷新列表
                    refershData();
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
        refershData();
    }

    private void refershData() {
        if (null == tvPrice || null == tvChPrice || null == titlePrice) {
            return;
        }
        if (null == wallet) {
            ToastUtil.show(R.string.qianbaoshujucuowu_qingtuichuchongshi);
            return;
        }

        //请求交易记录
        WalletApi.neoWalletOrder(this,page, wallet.getId(), "NEO", type == 0 ? Constant.NEO_ASSETS : Constant.GAS_ASSETS, new JsonCallback<LzyResponse<NeoOderBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<NeoOderBean>> response) {
                LoadSuccess(response);
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<NeoOderBean>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<NeoOderBean>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
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

    private void LoadSuccess(Response<LzyResponse<NeoOderBean>> response) {
        if (page==0){
            mails.clear();
            if (null != response.body().data.getList()) {
                if (response.body().data.getList().size()<10){
                    isEnd=true;
                }
                mails.addAll(response.body().data.getList());
            }
        }else {
            if (null != response.body().data.getList()) {
                if (response.body().data.getList().size()<10){
                    isEnd=true;
                }
                mails.addAll(response.body().data.getList());
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_PRICE) {
            initData();
            //请求余额
            getBanlance();
            //EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_WALLET));
        }
    }

    private void getBanlance() {
        //请求资产
        WalletApi.conversion(this, wallet.getId(), new JsonCallback<LzyResponse<TokenBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<TokenBean>> response) {
                if (null == tvPrice || null == titlePrice) {
                    return;
                }

                if (null != response.body().data && null != response.body().data.getRecord()) {
                    neoBean = response.body().data.getRecord();
                    if (type == 0) {
                        tvPrice.setText(new BigDecimal(neoBean.getBalance()).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
                        if (1 == AppApplication.get().getUnit()) {
                            tvChPrice.setText("≈￥" + new BigDecimal(neoBean.getBalance()).multiply(new BigDecimal(neoBean.getCap().getPrice_cny())).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                            titlePrice.setText("(￥" + new BigDecimal(neoBean.getBalance()).multiply(new BigDecimal(neoBean.getCap().getPrice_cny())).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + ")");
                        } else {
                            tvChPrice.setText("≈$" + new BigDecimal(neoBean.getBalance()).multiply(new BigDecimal(neoBean.getCap().getPrice_usd())).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                            titlePrice.setText("($" + new BigDecimal(neoBean.getBalance()).multiply(new BigDecimal(neoBean.getCap().getPrice_usd())).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + ")");
                        }
                    } else {
                        tvPrice.setText(new BigDecimal(neoBean.getGnt().get(0).getBalance()).setScale(8, BigDecimal.ROUND_HALF_UP).toString());
                        if (1 == AppApplication.get().getUnit()) {
                            tvChPrice.setText("≈￥" + new BigDecimal(neoBean.getGnt().get(0).getBalance()).multiply(new BigDecimal(neoBean.getGnt().get(0).getCap().getPrice_cny())).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                            titlePrice.setText("(￥" + new BigDecimal(neoBean.getGnt().get(0).getBalance()).multiply(new BigDecimal(neoBean.getGnt().get(0).getCap().getPrice_cny())).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + ")");
                        } else {
                            tvChPrice.setText("≈$" + new BigDecimal(neoBean.getGnt().get(0).getBalance()).multiply(new BigDecimal(neoBean.getGnt().get(0).getCap().getPrice_usd())).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                            titlePrice.setText("($" + new BigDecimal(neoBean.getGnt().get(0).getBalance()).multiply(new BigDecimal(neoBean.getGnt().get(0).getCap().getPrice_usd())).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + ")");
                        }
                    }
                }
            }
        });
    }

}
