package com.inwecrypto.wallet.ui.wallet.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.bean.WalletCountBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AnimUtil;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.CacheUtils;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.MaterialDialog;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.QuickActivity;
import com.inwecrypto.wallet.ui.login.LoginActivity;
import com.inwecrypto.wallet.ui.newneo.InputPassFragment;
import com.inwecrypto.wallet.ui.newneo.NewTransferWalletActivity;
import com.inwecrypto.wallet.ui.newneo.NewsProjectActivity;
import com.inwecrypto.wallet.ui.wallet.adapter.GntAdapter;
import com.lzy.okgo.model.Response;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import ethmobile.Ethmobile;
import ethmobile.Wallet;
import me.grantland.widget.AutofitTextView;

/**
 * Created by Administrator on 2017/7/16.
 * 功能描述：
 * 版本：@version
 */

public class HotWalletActivity extends BaseActivity {


    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.recive)
    ImageView recive;
    @BindView(R.id.name_ll)
    LinearLayout nameLl;
    @BindView(R.id.amount)
    AutofitTextView amount;
    @BindView(R.id.see)
    ImageView see;
    @BindView(R.id.charge)
    TextView charge;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.state)
    TextView state;
    @BindView(R.id.card_rl)
    RelativeLayout cardRl;
    @BindView(R.id.token)
    LinearLayout token;
    @BindView(R.id.tokensale)
    LinearLayout tokensale;
    @BindView(R.id.news)
    LinearLayout news;
    @BindView(R.id.left)
    FrameLayout left;
    @BindView(R.id.right)
    FrameLayout right;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.top_left)
    FrameLayout topLeft;
    @BindView(R.id.top_price)
    TextView topPrice;
    @BindView(R.id.top_see)
    ImageView topSee;
    @BindView(R.id.top_right)
    FrameLayout topRight;
    @BindView(R.id.titlell)
    LinearLayout titlell;
    @BindView(R.id.iv_neo_img)
    ImageView ivNeoImg;
    @BindView(R.id.neo_ll)
    LinearLayout neoLl;
    @BindView(R.id.neoname)
    TextView neoname;
    @BindView(R.id.neo_price)
    TextView neoPrice;
    @BindView(R.id.neo_ch_price)
    TextView neoChPrice;
    @BindView(R.id.neo_rl)
    RelativeLayout neoRl;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.list)
    SwipeMenuRecyclerView list;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayoutCompat swipeRefresh;
    @BindView(R.id.card_bg)
    ImageView cardBg;
    @BindView(R.id.hide)
    ImageView hide;
    @BindView(R.id.hidell)
    LinearLayout hidell;

    private ArrayList<TokenBean.ListBean> data = new ArrayList<>();
    private ArrayList<TokenBean.ListBean> showData = new ArrayList<>();
    private GntAdapter adapter;

    private WalletBean wallet;

    private MaterialDialog mMaterialDialog;
    private BigDecimal ETHEther = new BigDecimal("0.00");
    private BigDecimal ETHPrice = new BigDecimal("0.00");
    private BigDecimal TOKENPrice = new BigDecimal("0.00");
    private boolean isFinish;

    private Timer timer;
    private TimerTask task;

    private boolean isEth;
    private boolean isToken;

    private String totleChPrice = "0.00";
    private String totleUsdPrice = "0.00";
    private String neoTotleChPrice = "0.00";
    private String neoTotleUsdPrice = "0.00";

    private boolean isHide;

    @Override
    protected void getBundleExtras(Bundle extras) {
        wallet = (WalletBean) extras.getSerializable("wallet");
        isOpenEventBus = true;
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_acivity_hot;
    }

    @Override
    protected void initView() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cardBg.getLayoutParams();
        params.height = (int) ((ScreenUtils.getScreenWidth(this) - DensityUtil.dip2px(this, 52)) / 1956.0 * 1176.0);
        cardBg.setLayoutParams(params);

        SlidrConfig config = new SlidrConfig.Builder()
                .primaryColor(Color.parseColor("#000000"))
                .secondaryColor(Color.parseColor("#000000"))
                .position(SlidrPosition.LEFT)
                .sensitivity(1f)
                .scrimColor(Color.BLACK)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(2400)
                .distanceThreshold(0.25f)
                .edge(true)
                .edgeSize(0.18f)
                .build();// The % of the screen that counts as the edge, default 18%
//                                .listener(new SlidrListener(){...})

        Slidr.attach(this, config);
        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (null==swipeRefresh){
                    return;
                }
                if (verticalOffset >= 0) {
                    swipeRefresh.setEnabled(true);
                } else {
                    swipeRefresh.setEnabled(false);
                }
                if (verticalOffset == 0) {
                    titlell.setVisibility(View.GONE);
                }
                if (verticalOffset + appBarLayout.getTotalScrollRange() == 0) {
                    if (!isFinish) {
                        titlell.setVisibility(View.VISIBLE);
                        AnimUtil.startShowAnimation(titlell);
                        isFinish = true;
                    }
                } else {
                    if (isFinish) {
                        AnimUtil.startHideAnimation(titlell);
                        isFinish = false;
                    }
                }
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wallet.getType().equals("2")) {
                    showWatchSelectDialog();
                } else {
                    showSelectDialog();
                }
            }
        });

        topRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wallet.getType().equals("2")) {
                    showWatchSelectDialog();
                } else {
                    showSelectDialog();
                }
            }
        });

        token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()){
                    keepTogo(LoginActivity.class);
                    return;
                }
                Intent intent = new Intent(mActivity, AddTokenActivity.class);
                intent.putExtra("id", wallet.getCategory_id());
                intent.putExtra("walletId", wallet.getId());
                keepTogo(intent);
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(wallet.getAddress());
                ToastUtil.show(getString(R.string.gaidizhiyifuzhi));
            }
        });

        tokensale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(R.string.jinqingqidai);
//                if (null == wallet || null == neoBean) {
//                    ToastUtil.show(R.string.wallet_data_error);
//                    return;
//                }
//                Intent intent = new Intent(mActivity, NeoTokenSaleActivity.class);
//                intent.putExtra("wallet", wallet);
//                intent.putExtra("neo", neoBean);
//                keepTogo(intent);
            }
        });

        recive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ReceiveActivity.class);
                intent.putExtra("wallet", wallet);
                keepTogo(intent);
            }
        });

        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSee = App.get().getSp().getBoolean(Constant.MAIN_SEE, true);
                App.get().getSp().putBoolean(Constant.MAIN_SEE, !isSee);
                changeSee(App.get().getUnit(), !isSee);
                EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_PASS_SEE));
            }
        });

        topSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSee = App.get().getSp().getBoolean(Constant.MAIN_SEE, true);
                App.get().getSp().putBoolean(Constant.MAIN_SEE, !isSee);
                changeSee(App.get().getUnit(), !isSee);
                EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_PASS_SEE));
            }
        });

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()){
                    keepTogo(LoginActivity.class);
                    return;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("[\"ETH\"");

                for (TokenBean.ListBean token : data) {
                    sb.append(",\"" + token.getName().toUpperCase() + "\"");
                }
                sb.append("]");
                Intent intent = new Intent(mActivity, NewsProjectActivity.class);
                intent.putExtra("projects", sb.toString());
                keepTogo(intent);
            }
        });

        adapter = new GntAdapter(this, R.layout.wallet_item, showData);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mActivity);
                deleteItem.setText(getString(R.string.shanchu));
                deleteItem.setTextSize(14);
                deleteItem.setTextColorResource(R.color.c_ffffff);
                deleteItem.setBackgroundColorResource(R.color.c_E86438);
                deleteItem.setWidth(DensityUtil.dip2px(mActivity, 72));
                deleteItem.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                // 各种文字和图标属性设置。
                swipeRightMenu.addMenuItem(deleteItem); // 在Item左侧添加一个菜单。

                SwipeMenuItem upItem = new SwipeMenuItem(mActivity);
                upItem.setText(getString(R.string.dingzhi));
                upItem.setTextSize(14);
                upItem.setTextColorResource(R.color.c_ffffff);
                upItem.setBackgroundColorResource(R.color.c_bababb);
                upItem.setWidth(DensityUtil.dip2px(mActivity, 72));
                upItem.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                // 各种文字和图标属性设置。
                swipeRightMenu.addMenuItem(upItem); // 在Item右侧添加一个菜单。
            }
        });
        list.setAdapter(adapter);
        list.setNestedScrollingEnabled(false);
        list.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                if (!App.get().isLogin()){
                    keepTogo(LoginActivity.class);
                    return;
                }
                if (menuBridge.getPosition() == 0) {//删除
                    WalletApi.userGntDelete(mActivity, showData.get(menuBridge.getAdapterPosition()).getId(), new JsonCallback<LzyResponse<Object>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<Object>> response) {
                            list.smoothCloseMenu();
                            ToastUtil.show(getString(R.string.shanchuchenggong));
                            initData();
                            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET));
                        }

                        @Override
                        public void onError(Response<LzyResponse<Object>> response) {
                            super.onError(response);
                            ToastUtil.show(getString(R.string.shanchushibai));
                        }
                    });

                } else if (menuBridge.getPosition() == 1) {//顶置
                    WalletApi.userGnt(mActivity, showData.get(menuBridge.getAdapterPosition()).getId(), new JsonCallback<LzyResponse<Object>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<Object>> response) {
                            list.smoothCloseMenu();
                            ToastUtil.show(getString(R.string.dingzhichenggong));
                            initData();
                        }

                        @Override
                        public void onError(Response<LzyResponse<Object>> response) {
                            super.onError(response);
                            ToastUtil.show(getString(R.string.dingzhishibai));
                        }
                    });
                }
            }
        });
        neoRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == wallet) {
                    ToastUtil.show(R.string.wallet_data_error);
                    return;
                }
                Intent intent = new Intent(mActivity, TokenWalletActivity.class);
                intent.putExtra("isEht", true);
                intent.putExtra("wallet", wallet);
                keepTogo(intent);
            }
        });
        list.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (null == wallet) {
                    ToastUtil.show(R.string.wallet_data_error);
                    return;
                }
                Intent intent = new Intent(mActivity, TokenWalletActivity.class);
                intent.putExtra("isEht", false);
                intent.putExtra("gnt", showData.get(position));
                intent.putExtra("wallet", wallet);
                keepTogo(intent);
            }
        });
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        if (1 == App.get().getUnit()) {
            changeSee(1, App.get().getSp().getBoolean(Constant.MAIN_SEE, true));
        } else {
            changeSee(0, App.get().getSp().getBoolean(Constant.MAIN_SEE, true));
        }

        isHide=App.get().getSp().getBoolean(Constant.HIDE,false);
        if (isHide){
            hide.setImageResource(R.mipmap.hide_zero_selecte);
        }else {
            hide.setImageResource(R.mipmap.hide_zero_normal);
        }

        hidell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHide){
                    isHide=false;
                    hide.setImageResource(R.mipmap.hide_zero_normal);
                }else {
                    isHide=true;
                    hide.setImageResource(R.mipmap.hide_zero_selecte);
                }
                showData.clear();
                //刷新数据
                if (isHide){
                    for (int i=0;i<data.size();i++){
                        BigDecimal currentPrice = new BigDecimal(AppUtil.toD(data.get(i).getBalance().replace("0x", "0")));
                        if (currentPrice.floatValue()!=0){
                            showData.add(data.get(i));
                        }
                    }
                }else {
                    showData.addAll(data);
                }

                adapter.notifyDataSetChanged();
                App.get().getSp().putBoolean(Constant.HIDE,isHide);
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_HIDE,isHide));
            }
        });

        name.setText(wallet.getName());
        address.setText(wallet.getAddress());
        switch (new Integer(wallet.getType())) {
            case 0:
                String wallets = App.get().getSp().getString(Constant.WALLETS_BEIFEN, "").toLowerCase();
                if (wallets.contains(wallet.getAddress().toLowerCase())) {
                    state.setVisibility(View.GONE);
                } else {
                    state.setVisibility(View.VISIBLE);
                    state.setText(R.string.weibeifen);
                }
                break;
            case 1:
                state.setVisibility(View.GONE);
                break;
            case 2:
                state.setVisibility(View.VISIBLE);
                state.setText(getString(R.string.guancha));
                break;
        }

        if (timer == null) {
            timer = new Timer(true);
            task = new TimerTask() {

                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //刷新列表
                            refershData();
                        }
                    });

                }
            };
            timer.schedule(task, 30000, 30000);
        }

        if (App.get().getSp().getBoolean(Constant.FIRST_4, true)) {
            swipeRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int[] location = new int[2];
                    recive.getLocationOnScreen(location);
                    Intent intent = new Intent(mActivity, QuickActivity.class);
                    intent.putExtra("type", 4);
                    intent.putExtra("y", location[1]);
                    keepTogo(intent);
                }
            }, 300);
        }
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
        isEth = false;
        isToken = false;
        String sb = "[" + wallet.getId() + "]";

        if (!App.get().isLogin()){
            LzyResponse<CommonListBean<WalletCountBean>> response= CacheUtils.getCache(Constant.CONVERSION+sb.toString()+ App.isMain);
            if (null!=response){
                setBanlance(response);
            }
        }else {
            WalletApi.conversionWallet(mActivity, sb.toString(), new JsonCallback<LzyResponse<CommonListBean<WalletCountBean>>>() {
                @Override
                public void onSuccess(Response<LzyResponse<CommonListBean<WalletCountBean>>> response) {
                    setBanlance(response.body());

                }

                @Override
                public void onCacheSuccess(Response<LzyResponse<CommonListBean<WalletCountBean>>> response) {
                    super.onCacheSuccess(response);
                    onSuccess(response);
                }
            });
        }

        if (!App.get().isLogin()){
            LzyResponse<TokenBean> response= CacheUtils.getCache(Url.CONVERSION+"/"+wallet.getId()+ App.isMain);
            if (null!=response){
                setGnt(response);
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
        }else {
            //请求代币列表
            WalletApi.conversion(mActivity, wallet.getId(), new JsonCallback<LzyResponse<TokenBean>>() {
                @Override
                public void onSuccess(Response<LzyResponse<TokenBean>> response) {
                    setGnt(response.body());
                }

                @Override
                public void onCacheSuccess(Response<LzyResponse<TokenBean>> response) {
                    super.onCacheSuccess(response);
                    onSuccess(response);
                }

                @Override
                public void onError(Response<LzyResponse<TokenBean>> response) {
                    super.onError(response);
                    if (App.get().isLogin()){
                        if (NetworkUtils.isConnected(mActivity)) {
                            ToastUtil.show(getString(R.string.load_error));
                        }
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
    }

    private void setGnt(LzyResponse<TokenBean> response) {
        if (null == amount || null == topPrice) {
            return;
        }
        data.clear();
        showData.clear();
        if (null != response.data && response.data.getList().size() > 0) {
            data.addAll(response.data.getList());
        }
        if (App.get().getSp().getBoolean(Constant.HIDE,false)){
            for (int i=0;i<data.size();i++){
                BigDecimal currentPrice = new BigDecimal(AppUtil.toD(data.get(i).getBalance().replace("0x", "0")));
                if (currentPrice.floatValue()!=0){
                    showData.add(data.get(i));
                }
            }
        }else {
            showData.addAll(data);
        }
        adapter.notifyDataSetChanged();
        TOKENPrice = new BigDecimal("0.00");
        //计算金额
        for (TokenBean.ListBean token : data) {
            BigDecimal currentPrice = new BigDecimal(AppUtil.toD(token.getBalance().replace("0x", "0")));
            if (null != token.getGnt_category().getCap()) {

                if (1 == App.get().getUnit()) {
                    TOKENPrice = TOKENPrice.add(currentPrice.divide(AppUtil.decimal(token.getDecimals())).multiply(new BigDecimal(token.getGnt_category().getCap().getPrice_cny())));
                } else {
                    TOKENPrice = TOKENPrice.add(currentPrice.divide(AppUtil.decimal(token.getDecimals())).multiply(new BigDecimal(token.getGnt_category().getCap().getPrice_usd())));
                }
            }
        }
        isToken = true;
        if (isEth) {
            //计算总金额
            if (1 == App.get().getUnit()) {
                totleChPrice = TOKENPrice.add(ETHPrice).setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
                changeSee(1, App.get().getSp().getBoolean(Constant.MAIN_SEE, true));
            } else {
                totleUsdPrice = TOKENPrice.add(ETHPrice).setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
                changeSee(0, App.get().getSp().getBoolean(Constant.MAIN_SEE, true));
            }
        }
    }

    private void setBanlance(LzyResponse<CommonListBean<WalletCountBean>> response) {
        if (null == topPrice || null == amount || null == neoPrice || null == neoChPrice) {
            return;
        }
        ETHEther = ETHEther.multiply(new BigDecimal(0));
        ETHPrice = ETHPrice.multiply(new BigDecimal(0));
        ArrayList<WalletCountBean> walletPrices = response.data.getList();
        BigDecimal currentPrice = new BigDecimal(AppUtil.toD(walletPrices.get(0).getBalance().replace("0x", "0")));
        ETHEther = ETHEther.add(currentPrice).divide(Constant.pEther, 4, BigDecimal.ROUND_DOWN);
        if (1 == App.get().getUnit()) {
            ETHPrice = ETHPrice.add(currentPrice.divide(Constant.pEther).multiply(new BigDecimal(walletPrices.get(0).getCategory().getCap().getPrice_cny())));
            neoPrice.setText(ETHEther.toString());
            neoTotleChPrice = ETHPrice.setScale(2, BigDecimal.ROUND_DOWN).toString();
        } else {
            ETHPrice = ETHPrice.add(currentPrice.divide(Constant.pEther).multiply(new BigDecimal(walletPrices.get(0).getCategory().getCap().getPrice_usd())));
            neoPrice.setText(ETHEther.toString());
            neoTotleUsdPrice = ETHPrice.setScale(2, BigDecimal.ROUND_DOWN).toString();
        }

        isEth = true;
        if (isToken) {
            //计算总金额
            if (1 == App.get().getUnit()) {
                totleChPrice = TOKENPrice.add(ETHPrice).setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
                changeSee(1, App.get().getSp().getBoolean(Constant.MAIN_SEE, true));
            } else {
                totleUsdPrice = TOKENPrice.add(ETHPrice).setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
                changeSee(0, App.get().getSp().getBoolean(Constant.MAIN_SEE, true));
            }
        }
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_PRICE || event.getEventCode() == Constant.EVENT_REFRESH) {
            initData();
            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET));
        }
        if (event.getEventCode() == Constant.EVENT_TIP_SUCCESS) {
            state.setVisibility(View.GONE);
            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET));
        }
        if (event.getEventCode() == Constant.EVENT_WATCH_TRANSFER) {
            wallet.setType("0");
            switch (new Integer(wallet.getType())) {
                case 0:
                    String wallets = App.get().getSp().getString(Constant.WALLETS_BEIFEN, "").toLowerCase();
                    String walletsZjc = App.get().getSp().getString(Constant.WALLETS_ZJC_BEIFEN, "").toLowerCase();
                    if (wallets.contains(wallet.getAddress().toLowerCase()) || walletsZjc.contains(wallet.getAddress().toLowerCase())) {
                        state.setVisibility(View.GONE);
                    } else {
                        state.setVisibility(View.VISIBLE);
                        state.setText(getString(R.string.weibeifen));
                    }
                    break;
                case 1:
                    state.setVisibility(View.GONE);
                    break;
                case 2:
                    state.setVisibility(View.VISIBLE);
                    state.setText(getString(R.string.guancha));
                    break;
            }
            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET));
        }
    }

    private void showSelectDialog() {

        View selectPopupWin = LayoutInflater.from(this).inflate(R.layout.view_popup_wallet_detaile, null, false);
        View hit = selectPopupWin.findViewById(R.id.hit);
        final View zhujicill = selectPopupWin.findViewById(R.id.zhujicill);
        TextView keystore = (TextView) selectPopupWin.findViewById(R.id.keystore);
        TextView delete = (TextView) selectPopupWin.findViewById(R.id.delete);

        String wallets = App.get().getSp().getString(Constant.WALLETS_ZJC_BEIFEN, "").toLowerCase();
        if (wallets.contains(wallet.getAddress().toLowerCase())) {
            hit.setVisibility(View.GONE);
            zhujicill.setVisibility(View.GONE);
        }

        final PopupWindow window = new PopupWindow(selectPopupWin, DensityUtil.dip2px(this, 140), WindowManager.LayoutParams.WRAP_CONTENT);
        // 产生背景变暗效果
        WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.alpha = 0.4f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.update();
        window.showAsDropDown(right, 0, -DensityUtil.dip2px(this, 10));
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                mActivity.getWindow().setAttributes(lp);
            }
        });

        zhujicill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //输入密码
                FragmentManager fm = getSupportFragmentManager();
                InputPassFragment input = new InputPassFragment();
                input.show(fm, "input");
                input.setOnNextListener(new InputPassFragment.OnNextInterface() {
                    @Override
                    public void onNext(final String passWord, final Dialog dialog) {
                        if (passWord.length() == 0) {
                            ToastUtil.show(getString(R.string.qingshurumima));
                            return;
                        }
                        showLoading();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String b = "";
                                final AccountManager accountManager = AccountManager.get(mActivity);
                                Account[] accounts = accountManager.getAccountsByType("com.inwecrypto.wallet");
                                Account account = null;
                                for (int i = 0; i < accounts.length; i++) {
                                    if (accounts[i].name.toLowerCase().equals(wallet.getAddress().toLowerCase())) {
                                        //accountManager.getUserData(accounts[i], pass.getText().toString());
                                        b = accountManager.getUserData(accounts[i], "wallet");
                                        account = accounts[i];
                                        break;
                                    }
                                }
                                Wallet ethWallet = null;
                                try {
                                    ethWallet = Ethmobile.fromKeyStore(b, passWord);
                                } catch (Exception e) {
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtil.show(getString(R.string.mimacuowuqingchongshi));
                                            hideLoading();
                                        }
                                    });
                                    return;
                                }
                                String zjc = "";
                                try {
                                    zjc = ethWallet.mnemonic(App.get().isZh() ? "zh_CN" : "en_US");
                                } catch (Exception e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideLoading();
                                            ToastUtil.show(getString(R.string.beifenci_error));
                                        }
                                    });
                                    return;
                                }
                                final String finalZjc = zjc;
                                accountManager.setUserData(account, "type", Constant.BEIFEN);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideLoading();
                                        dialog.dismiss();
                                        Intent intent = new Intent(mActivity, WalletTipOneActivity.class);
                                        intent.putExtra("zjc", finalZjc);
                                        intent.putExtra("wallet", wallet);
                                        keepTogo(intent);
                                    }
                                });
                            }
                        }).start();
                    }
                });
                window.dismiss();
            }
        });

        keystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //输入密码
                FragmentManager fm = getSupportFragmentManager();
                InputPassFragment input = new InputPassFragment();
                input.show(fm, "input");
                input.setOnNextListener(new InputPassFragment.OnNextInterface() {
                    @Override
                    public void onNext(final String passWord, final Dialog dialog) {
                        if (passWord.length() == 0) {
                            ToastUtil.show(getString(R.string.qingshurumima));
                            return;
                        }
                        showLoading();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String b = "";
                                final AccountManager accountManager = AccountManager.get(mActivity);
                                Account[] accounts = accountManager.getAccountsByType("com.inwecrypto.wallet");
                                Account account = null;
                                for (int i = 0; i < accounts.length; i++) {
                                    if (accounts[i].name.toLowerCase().equals(wallet.getAddress().toLowerCase())) {
                                        //accountManager.getUserData(accounts[i], pass.getText().toString());
                                        b = accountManager.getUserData(accounts[i], "wallet");
                                        account = accounts[i];
                                        break;
                                    }
                                }
                                Wallet wal = null;
                                try {
                                    wal = Ethmobile.fromKeyStore(b, passWord);
                                } catch (Exception e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideLoading();
                                            ToastUtil.show(getString(R.string.mimacuowuqingchongshi));
                                        }
                                    });
                                    return;
                                }
                                String keys = "";
                                try {
                                    keys = wal.toKeyStore(passWord);
                                } catch (Exception e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideLoading();
                                            ToastUtil.show(getString(R.string.keystory_error));
                                        }
                                    });
                                    return;
                                }
                                accountManager.setUserData(account, "type", Constant.BEIFEN);

                                String wallets = App.get().getSp().getString(Constant.WALLETS_BEIFEN, "").toLowerCase();
                                if (!wallets.contains(wallet.getAddress().toLowerCase())) {
                                    wallets = wallets + wallet.getAddress().toLowerCase() + ",";
                                    App.get().getSp().putString(Constant.WALLETS_BEIFEN, wallets);
                                }
                                final String finalKeys = keys;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideLoading();
                                        dialog.dismiss();
                                        state.setVisibility(View.GONE);
                                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET));
                                        Intent intent1 = new Intent(Intent.ACTION_SEND);
                                        intent1.putExtra(Intent.EXTRA_TEXT, finalKeys);
                                        intent1.setType("text/plain");
                                        startActivity(Intent.createChooser(intent1, "share"));
                                    }
                                });
                            }
                        }).start();
                    }
                });
                window.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //输入密码
                FragmentManager fm = getSupportFragmentManager();
                InputPassFragment input = new InputPassFragment();
                input.show(fm, "input");
                input.setOnNextListener(new InputPassFragment.OnNextInterface() {
                    @Override
                    public void onNext(final String passWord, final Dialog dialog) {
                        if (!App.get().isLogin()){
                            keepTogo(LoginActivity.class);
                            return;
                        }
                        if (passWord.length() == 0) {
                            ToastUtil.show(getString(R.string.qingshurumima));
                            return;
                        }
                        showLoading();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String b = "";
                                final AccountManager accountManager = AccountManager.get(mActivity);
                                Account[] accounts = accountManager.getAccountsByType("com.inwecrypto.wallet");
                                Account account = null;

                                for (int i = 0; i < accounts.length; i++) {
                                    if (accounts[i].name.toLowerCase().equals(wallet.getAddress().toLowerCase())) {
                                        account = accounts[i];
                                        //accountManager.getUserData(accounts[i], pass.getText().toString());
                                        b = accountManager.getUserData(accounts[i], "wallet");
                                        break;
                                    }
                                }
                                try {
                                    Ethmobile.fromKeyStore(b, passWord);
                                } catch (Exception e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideLoading();
                                            ToastUtil.show(getString(R.string.password_error));
                                        }
                                    });
                                    return;
                                }

                                final Account finalAccount = account;
                                WalletApi.wallet(mActivity, wallet.getId(), new JsonCallback<LzyResponse<Object>>() {
                                    @Override
                                    public void onSuccess(Response<LzyResponse<Object>> response) {
                                        if (null != finalAccount) {
                                            accountManager.removeAccount(finalAccount, null, null);
                                        }
                                        String walletStr = App.get().getSp().getString(Constant.WALLETS, "");
                                        if (walletStr.contains(wallet.getAddress().toLowerCase())) {
                                            walletStr = walletStr.replace(wallet.getAddress().toLowerCase(), "");
                                            App.get().getSp().putString(Constant.WALLETS, walletStr);
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                hideLoading();
                                                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET));
                                                ToastUtil.show(getString(R.string.shanchuchenggong));
                                                finish();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(Response<LzyResponse<Object>> response) {
                                        super.onError(response);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                hideLoading();
                                                ToastUtil.show(getString(R.string.shanchushibai));
                                            }
                                        });
                                    }
                                });
                            }
                        }).start();
                    }
                });
                window.dismiss();
            }
        });
    }


    private void showWatchSelectDialog() {

        View selectPopupWin = LayoutInflater.from(this).inflate(R.layout.view_popup_wallet_detaile, null, false);
        LinearLayout zhujici = (LinearLayout) selectPopupWin.findViewById(R.id.zhujicill);
        View hit = selectPopupWin.findViewById(R.id.hit);
        zhujici.setVisibility(View.GONE);
        hit.setVisibility(View.GONE);

        TextView keystore = (TextView) selectPopupWin.findViewById(R.id.keystore);
        keystore.setText(R.string.zhuanweierqianbao);
        TextView delete = (TextView) selectPopupWin.findViewById(R.id.delete);

        final PopupWindow window = new PopupWindow(selectPopupWin, DensityUtil.dip2px(this, 140), WindowManager.LayoutParams.WRAP_CONTENT);
        // 产生背景变暗效果
        WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.alpha = 0.4f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.update();
        window.showAsDropDown(right, 0, -DensityUtil.dip2px(this, 10));
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                mActivity.getWindow().setAttributes(lp);
            }
        });


        keystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, NewTransferWalletActivity.class);
                intent.putExtra("wallet", wallet);
                intent.putExtra("isNeo", false);
                keepTogo(intent);
                window.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(mActivity).inflate(R.layout.view_dialog_delete_watch, null, false);
                View ok = view.findViewById(R.id.ok);
                View cancle = view.findViewById(R.id.cancle);
                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!App.get().isLogin()){
                            keepTogo(LoginActivity.class);
                            return;
                        }
                        WalletApi.wallet(mActivity, wallet.getId(), new JsonCallback<LzyResponse<Object>>() {
                            @Override
                            public void onSuccess(Response<LzyResponse<Object>> response) {
                                ToastUtil.show(getString(R.string.shanchuchenggong));
                                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET));
                                finish();
                            }

                            @Override
                            public void onError(Response<LzyResponse<Object>> response) {
                                super.onError(response);
                                ToastUtil.show(getString(R.string.shanchushibai));
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                if (null != mMaterialDialog) {
                                    mMaterialDialog.dismiss();
                                }
                            }
                        });
                    }
                });
                mMaterialDialog = new MaterialDialog(mActivity).setView(view);
                mMaterialDialog.setBackgroundResource(R.drawable.trans_bg);
                mMaterialDialog.setCanceledOnTouchOutside(true);
                mMaterialDialog.show();
                window.dismiss();
            }
        });
    }

    private void changeSee(int unit, boolean isSee) {
        if (isSee) {
            if (unit == 1) {
                amount.setText("￥ " + totleChPrice);
                topPrice.setText("￥ " + totleChPrice);
                neoChPrice.setText("￥" + neoTotleChPrice);
            } else {
                amount.setText("$ " + totleUsdPrice);
                topPrice.setText("$ " + totleUsdPrice);
                neoChPrice.setText("$" + neoTotleUsdPrice);
            }
            see.setImageResource(R.mipmap.openpassxxhdpi);
            topSee.setImageResource(R.mipmap.openpassxxhdpi);
        } else {
            if (unit == 1) {
                amount.setText("￥ ****");
                topPrice.setText("￥ ****");
                neoChPrice.setText("￥****");
            } else {
                amount.setText("$ ****");
                topPrice.setText("$ ****");
                neoChPrice.setText("$****");
            }
            see.setImageResource(R.mipmap.closeseexxhdpi);
            topSee.setImageResource(R.mipmap.closeseexxhdpi);
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
        if (null != task) {
            task.cancel();
            task = null;
        }
    }
}
