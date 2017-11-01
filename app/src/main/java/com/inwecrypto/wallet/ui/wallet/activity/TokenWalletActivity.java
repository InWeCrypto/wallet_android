package com.inwecrypto.wallet.ui.wallet.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import com.inwecrypto.wallet.AppApplication;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.BpsBean;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.MinBlockBean;
import com.inwecrypto.wallet.bean.OrderBean;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.bean.ValueBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.bean.WalletCountBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.imageloader.GlideCircleTransform;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.wallet.adapter.RecordAdapter;

/**
 * Created by Administrator on 2017/7/16.
 * 功能描述：
 * 版本：@version
 */

public class TokenWalletActivity extends BaseActivity {


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
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
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

    private TokenBean.ListBean gnt;
    private ArrayList<OrderBean> mails = new ArrayList<>();
    private RecordAdapter adapter;

    private boolean isEth;

    private BigDecimal totleEther = new BigDecimal("0.0000");
    private BigDecimal totlePrice = new BigDecimal("0.00");
    private BigDecimal pEther = new BigDecimal("1000000000000000000");

    private String flag;

    public static int minBlock = 12;
    public static double currentBlock;
    public static int speed = 10000;
    private boolean isFinish;
    private Timer timer;
    private TimerTask task;
    private int screenWidth;
    private int hitWidth;
    private int distance;
    private BigDecimal tokenEther=new BigDecimal("0.0000");

    @Override
    protected void getBundleExtras(Bundle extras) {
        isOpenEventBus = true;
        wallet = (WalletBean) extras.getSerializable("wallet");
        isEth = extras.getBoolean("isEht");
        if (!isEth) {
            gnt = (TokenBean.ListBean) extras.getSerializable("gnt");
        }
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_acivity_token_new;
    }

    @Override
    protected void initView() {

        //获取屏幕宽度
        screenWidth=ScreenUtils.getScreenWidth(this);
        //获取提示宽度
        tvHit.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tvHit.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                hitWidth = tvHit.getMeasuredWidth();
                distance=(screenWidth-hitWidth)/2- DensityUtil.dip2px(mActivity,24);
            }
        });
        //获取滑动距离

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (isEth) {
            title.setText("ETH");
        } else {
            title.setText(gnt.getName());
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
                        startShowAnimation();
                        startMoveLeftAnimation();
                        isFinish = true;
                    }
                } else {
                    if (isFinish) {
                        startHideAnimation();
                        startMoveRightAnimation();
                        isFinish = false;
                    }
                }
            }
        });
        llZhuanzhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEth) {
                    if (wallet.getType().equals(Constant.GUANCHA)){
                        PackageManager pm = getPackageManager();
                        boolean nfc = pm.hasSystemFeature(PackageManager.FEATURE_NFC);
                        if (!nfc){
                            ToastUtil.show("您的手机不支持nfc!请使用热钱包创建！");
                            return;
                        }
                    }
                    Intent intent = new Intent(mActivity, TransferAccountsActivity.class);
                    intent.putExtra("wallet", wallet);
                    intent.putExtra("price", totleEther.divide(pEther).setScale(4, BigDecimal.ROUND_HALF_UP).toString());
                    if (wallet.getType().equals(Constant.GUANCHA)) {
                        intent.putExtra("isClod", true);
                    }
                    keepTogo(intent);
                } else {
                    if (wallet.getType().equals(Constant.GUANCHA)) {
                        PackageManager pm = getPackageManager();
                        boolean nfc = pm.hasSystemFeature(PackageManager.FEATURE_NFC);
                        if (!nfc){
                            ToastUtil.show("您的手机不支持nfc!请使用热钱包创建！");
                            return;
                        }
                        Intent intent = new Intent(mActivity, WatchTokenTransferAccountsActivity.class);
                        intent.putExtra("wallet", wallet);
                        intent.putExtra("price", tokenEther.divide(pEther).setScale(4, BigDecimal.ROUND_HALF_UP).toString());
                        intent.putExtra("gnt", gnt);
                        keepTogo(intent);
                    } else {
                        Intent intent = new Intent(mActivity, TokenTransferAccountsActivity.class);
                        intent.putExtra("wallet", wallet);
                        intent.putExtra("gnt", gnt);
                        intent.putExtra("price", tokenEther.divide(pEther).setScale(4, BigDecimal.ROUND_HALF_UP).toString());
                        keepTogo(intent);
                    }
                }
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

        if (isEth) {
            Glide.with(this).load(R.mipmap.eth).transform(new GlideCircleTransform(this)).crossFade().into(ivImg);
        } else {
            Glide.with(this).load(gnt.getGnt_category().getIcon()).transform(new GlideCircleTransform(this)).crossFade().into(ivImg);
            BigDecimal currentPrice = new BigDecimal(AppUtil.toD(gnt.getBalance().replace("0x", "0")));
            tvPrice.setText(currentPrice.divide(pEther, 4, BigDecimal.ROUND_HALF_UP).toString());
            if (1 == AppApplication.get().getUnit()) {
                tvChPrice.setText("≈￥" + currentPrice.divide(pEther).multiply(new BigDecimal(gnt.getGnt_category().getCap().getPrice_cny())).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                titlePrice.setText("(￥" + currentPrice.divide(pEther).multiply(new BigDecimal(gnt.getGnt_category().getCap().getPrice_cny())).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + ")");
            } else {
                tvChPrice.setText("≈$" + currentPrice.divide(pEther).multiply(new BigDecimal(gnt.getGnt_category().getCap().getPrice_usd())).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                titlePrice.setText("($" + currentPrice.divide(pEther).multiply(new BigDecimal(gnt.getGnt_category().getCap().getPrice_usd())).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + ")");
            }
        }

        adapter = new RecordAdapter(this, R.layout.wallet_item_token_transfer, mails, wallet.getAddress(),isEth?"ether":gnt.getName().toLowerCase());
        walletList.setLayoutManager(new LinearLayoutManager(this));
        walletList.setAdapter(adapter);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (wallet.getAddress().equals(mails.get(position).getPay_address())) {
                    Intent intent = new Intent(mActivity, TransferAccountsDetaileActivity.class);
                    intent.putExtra("order", mails.get(position));
                    intent.putExtra("unit",isEth?"ether":gnt.getName().toLowerCase());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mActivity, ReceiveDetaileActivity.class);
                    intent.putExtra("order", mails.get(position));
                    intent.putExtra("unit",isEth?"ether":gnt.getName().toLowerCase());
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        startRound();
    }

    private void startRound() {
        //获取最小确认块数
        WalletApi.mimBlock(mActivity, new JsonCallback<LzyResponse<MinBlockBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<MinBlockBean>> response) {
                minBlock = Integer.parseInt(response.body().data.getMin_block_num());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                //获取当前块高
                WalletApi.blockNumber(mActivity, new JsonCallback<LzyResponse<ValueBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<ValueBean>> response) {
                        //0x181d02
                        //进行计算
                        BigDecimal currentPrice = new BigDecimal(AppUtil.toD(response.body().data.getValue().replace("0x", "0")));
                        currentBlock = currentPrice.doubleValue();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        //获取轮询速度
                        WalletApi.blockPerSecond(mActivity, new JsonCallback<LzyResponse<BpsBean>>() {
                            @Override
                            public void onSuccess(Response<LzyResponse<BpsBean>> response) {
                                //开启轮询
                                speed = (int) (1.0 / response.body().data.getBps());
                                if (speed < 5000) {
                                    speed = 10000;
                                }
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                if (timer == null) {
                                    timer = new Timer(true);
                                    task = new TimerTask() {

                                        @Override
                                        public void run() {
                                            //获取当前块高
                                            WalletApi.blockNumber(this, new JsonCallback<LzyResponse<ValueBean>>() {
                                                @Override
                                                public void onSuccess(Response<LzyResponse<ValueBean>> response) {
                                                    //0x181d02
                                                    //进行计算
                                                    BigDecimal currentPrice = new BigDecimal(AppUtil.toD(response.body().data.getValue().replace("0x", "0")));
                                                    currentBlock = currentPrice.doubleValue();
                                                }
                                            });
                                            //刷新列表
                                            refershData();
                                        }
                                    };
                                    timer.schedule(task, speed, speed);
                                }
                            }
                        });
                    }
                });
            }

        });
    }

    @Override
    protected void initData() {

        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
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
            ToastUtil.show("钱包数据错误，请退出重试");
            return;
        }
        if (isEth) {
            flag = wallet.getCategory().getName();
            String sb = "[" + wallet.getId() + "]";
            WalletApi.conversionWallet(mActivity, sb.toString(), new JsonCallback<LzyResponse<CommonListBean<WalletCountBean>>>() {
                @Override
                public void onSuccess(Response<LzyResponse<CommonListBean<WalletCountBean>>> response) {
                    if (null == tvPrice || null == tvChPrice || null == titlePrice) {
                        return;
                    }
                    totleEther = totleEther.multiply(new BigDecimal(0));
                    totlePrice = totlePrice.multiply(new BigDecimal(0));
                    ArrayList<WalletCountBean> walletPrices = response.body().data.getList();
                    BigDecimal currentPrice = new BigDecimal(AppUtil.toD(walletPrices.get(0).getBalance().replace("0x", "0")));
                    totleEther = totleEther.add(currentPrice);
                    if (1 == AppApplication.get().getUnit()) {
                        totlePrice = totlePrice.add(currentPrice.divide(pEther).multiply(new BigDecimal(walletPrices.get(0).getCategory().getCap().getPrice_cny()))).setScale(2, BigDecimal.ROUND_HALF_UP);
                        tvPrice.setText(totleEther.divide(pEther).setScale(4, BigDecimal.ROUND_HALF_UP).toString());
                        tvChPrice.setText("≈￥" + totlePrice.toString());
                        titlePrice.setText("(￥" + totlePrice.toString() + ")");
                    } else {
                        totlePrice = totlePrice.add(currentPrice.divide(pEther).multiply(new BigDecimal(walletPrices.get(0).getCategory().getCap().getPrice_usd()))).setScale(2, BigDecimal.ROUND_HALF_UP);
                        tvPrice.setText(totleEther.divide(pEther).setScale(4, BigDecimal.ROUND_HALF_UP).toString());
                        tvChPrice.setText("≈$" + totlePrice.toString());
                        titlePrice.setText("($" + totlePrice.toString() + ")");
                    }
                }

                @Override
                public void onCacheSuccess(Response<LzyResponse<CommonListBean<WalletCountBean>>> response) {
                    super.onCacheSuccess(response);
                    onSuccess(response);
                }

            });
        } else {
            flag = gnt.getName();
            //请求用户资产
            WalletApi.balanceof(mActivity, gnt.getGnt_category().getAddress(), wallet.getAddress(), new JsonCallback<LzyResponse<ValueBean>>() {
                @Override
                public void onSuccess(Response<LzyResponse<ValueBean>> response) {
                    if (null==tvPrice||null==tvChPrice||null==titlePrice){
                        return;
                    }
                    //进行计算
                    tokenEther = new BigDecimal(AppUtil.toD(response.body().data.getValue().replace("0x", "0")));
                    tvPrice.setText(tokenEther.divide(pEther).setScale(4, BigDecimal.ROUND_HALF_UP).toString());
                    if (1 == AppApplication.get().getUnit()) {
                        tvChPrice.setText("≈￥" + tokenEther.divide(pEther).multiply(new BigDecimal(gnt.getGnt_category().getCap().getPrice_cny())).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        titlePrice.setText("(￥" + tokenEther.divide(pEther).multiply(new BigDecimal(gnt.getGnt_category().getCap().getPrice_cny())).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + ")");
                    } else {
                        tvChPrice.setText("≈$" + tokenEther.divide(pEther).multiply(new BigDecimal(gnt.getGnt_category().getCap().getPrice_usd())).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        titlePrice.setText("($" + tokenEther.divide(pEther).multiply(new BigDecimal(gnt.getGnt_category().getCap().getPrice_usd())).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + ")");
                    }
                }

                @Override
                public void onCacheSuccess(Response<LzyResponse<ValueBean>> response) {
                    super.onCacheSuccess(response);
                    onSuccess(response);
                }
            });
        }

        //请求交易记录
        WalletApi.walletOrder(this, wallet.getId(), flag, new JsonCallback<LzyResponse<CommonListBean<OrderBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<OrderBean>>> response) {
                LoadSuccess(response);
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<CommonListBean<OrderBean>>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<OrderBean>>> response) {
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

    private void LoadSuccess(Response<LzyResponse<CommonListBean<OrderBean>>> response) {
        mails.clear();
        if (null != response.body().data.getList()) {
            mails.addAll(response.body().data.getList());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_PRICE) {
            initData();
            EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_WALLET));
        }
    }

    public void startHideAnimation() {
        //清除动画
        titlell.clearAnimation();
        /**
         * @param fromAlpha 开始的透明度，取值是0.0f~1.0f，0.0f表示完全透明， 1.0f表示和原来一样
         * @param toAlpha 结束的透明度，同上
         */
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        //设置动画持续时长
        alphaAnimation.setDuration(300);
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
        alphaAnimation.setFillAfter(true);
        //开始动画
        titlell.startAnimation(alphaAnimation);
    }

    public void startShowAnimation() {
        //清除动画
        titlell.clearAnimation();
        /**
         * @param fromAlpha 开始的透明度，取值是0.0f~1.0f，0.0f表示完全透明， 1.0f表示和原来一样
         * @param toAlpha 结束的透明度，同上
         */
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);

        //设置动画持续时长
        alphaAnimation.setDuration(300);
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
        alphaAnimation.setFillAfter(true);
        //开始动画
        titlell.startAnimation(alphaAnimation);
    }

    private void startMoveRightAnimation() {
        //清除动画
        tvHit.clearAnimation();
        /**
         * @param fromAlpha 开始的透明度，取值是0.0f~1.0f，0.0f表示完全透明， 1.0f表示和原来一样
         * @param toAlpha 结束的透明度，同上
         */
        TranslateAnimation animation = new TranslateAnimation(-distance,0, 0, 0);
        animation.setDuration(800);
        animation.setRepeatCount(0);//动画的重复次数
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
        animation.setFillAfter(true);
        //开始动画
        tvHit.startAnimation(animation);
    }

    private void startMoveLeftAnimation() {
        //清除动画
        tvHit.clearAnimation();
        /**
         * @param fromAlpha 开始的透明度，取值是0.0f~1.0f，0.0f表示完全透明， 1.0f表示和原来一样
         * @param toAlpha 结束的透明度，同上
         */
        TranslateAnimation animation = new TranslateAnimation(0, -distance, 0, 0);
        animation.setDuration(800);
        animation.setRepeatCount(0);//动画的重复次数
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
        animation.setFillAfter(true);
        //开始动画
        tvHit.startAnimation(animation);
    }

}
