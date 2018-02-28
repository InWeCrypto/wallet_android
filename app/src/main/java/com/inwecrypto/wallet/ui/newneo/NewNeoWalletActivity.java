package com.inwecrypto.wallet.ui.newneo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AnimUtil;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.MaterialDialog;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.wallet.activity.WalletTipOneActivity;
import com.inwecrypto.wallet.ui.wallet.activity.neowallet.GetGasActivity;
import com.inwecrypto.wallet.ui.wallet.activity.neowallet.NeoNep5TokenWalletActivity;
import com.inwecrypto.wallet.ui.wallet.activity.neowallet.NeoTokenWalletActivity;
import com.lzy.okgo.model.Response;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import me.grantland.widget.AutofitTextView;
import neomobile.Neomobile;
import neomobile.Wallet;

/**
 * 作者：xiaoji06 on 2018/1/8 17:25
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class NewNeoWalletActivity extends BaseActivity {


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
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.getgas)
    TextView getgas;
    @BindView(R.id.getgasll)
    LinearLayout getgasll;
    @BindView(R.id.gas_name)
    TextView gasName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_eth_ch_price)
    TextView tvEthChPrice;
    @BindView(R.id.gas_rl)
    RelativeLayout gasRl;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.list)
    SwipeMenuRecyclerView list;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayoutCompat swipeRefresh;

    private ArrayList<TokenBean.ListBean> data = new ArrayList<>();
    private NewNeoGntAdapter adapter;

    private WalletBean wallet;

    private boolean isFinish;
    private TokenBean.RecordBean neoBean;
    private BigDecimal TOKENPrice = new BigDecimal("0.00");

    private Timer timer;
    private TimerTask task;

    private String totleChPrice = "0.00";
    private String totleUsdPrice = "0.00";
    private String neoTotleChPrice = "0.00";
    private String gasTotleChPrice = "0.00";
    private String neoTotleUsdPrice = "0.00";
    private String gasTotleUsdrice = "0.00";
    private MaterialDialog mMaterialDialog;

    @Override
    protected void getBundleExtras(Bundle extras) {
        wallet = (WalletBean) extras.getSerializable("wallet");
        isOpenEventBus = true;
    }

    @Override
    protected int setLayoutID() {
        return R.layout.newneo_wallet_activity;
    }

    @Override
    protected void initView() {
        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
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

        recive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, NewNeoReciveActivity.class);
                intent.putExtra("wallet", wallet);
                keepTogo(intent);
            }
        });

        token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == wallet || null == neoBean) {
                    ToastUtil.show(R.string.wallet_data_error);
                    return;
                }
                Intent intent = new Intent(mActivity, NewNeoAddNep5Activity.class);
                intent.putExtra("id", wallet.getCategory_id());
                intent.putExtra("walletId", wallet.getId());
                keepTogo(intent);
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

        getgasll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == wallet || null == neoBean) {
                    ToastUtil.show(R.string.wallet_data_error);
                    return;
                }
                Intent intent = new Intent(mActivity, GetGasActivity.class);
                intent.putExtra("wallet", wallet);
                intent.putExtra("neo", neoBean);
                keepTogo(intent);
            }
        });

        neoRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == wallet || null == neoBean) {
                    ToastUtil.show(R.string.wallet_data_error);
                    return;
                }
                Intent intent = new Intent(mActivity, NeoTokenWalletActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("wallet", wallet);
                intent.putExtra("neo", neoBean);
                keepTogo(intent);
            }
        });

        gasRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == wallet || null == neoBean) {
                    ToastUtil.show(R.string.wallet_data_error);
                    return;
                }
                Intent intent = new Intent(mActivity, NeoTokenWalletActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("wallet", wallet);
                intent.putExtra("neo", neoBean);
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
                ToastUtil.show(getString(R.string.jinqingqidai));
            }
        });

        adapter = new NewNeoGntAdapter(this, R.layout.newneo_token_item, data);
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

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //从网络获取钱包
                refershData();
            }
        });

        list.setAdapter(adapter);
        list.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                if (menuBridge.getPosition() == 0) {//删除
                    WalletApi.userGntDelete(mActivity, data.get(menuBridge.getAdapterPosition()).getId(), new JsonCallback<LzyResponse<Object>>() {
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
                    WalletApi.userGnt(mActivity, data.get(menuBridge.getAdapterPosition()).getId(), new JsonCallback<LzyResponse<Object>>() {
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

        list.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (null == wallet || null == neoBean) {
                    ToastUtil.show(R.string.wallet_data_error);
                    return;
                }
                Intent intent = new Intent(mActivity, NeoNep5TokenWalletActivity.class);
                intent.putExtra("wallet", wallet);
                intent.putExtra("neo", neoBean);
                intent.putExtra("token", data.get(position));
                keepTogo(intent);
            }
        });

        if (1 == App.get().getUnit()) {
            changeSee(1, App.get().getSp().getBoolean(Constant.MAIN_SEE, true));
        } else {
            changeSee(0, App.get().getSp().getBoolean(Constant.MAIN_SEE, true));
        }

        name.setText(wallet.getName());
        address.setText(wallet.getAddress());
        switch (new Integer(wallet.getType())) {
            case 0:
                String wallets = App.get().getSp().getString(Constant.WALLETS_BEIFEN, "");
                if (wallets.contains(wallet.getAddress())) {
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

        //请求资产
        WalletApi.conversion(this, wallet.getId(), new JsonCallback<LzyResponse<TokenBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<TokenBean>> response) {
                if (null == amount || null == topPrice) {
                    return;
                }
                data.clear();
                TOKENPrice = new BigDecimal("0.00");

                if (null != response.body().data && null != response.body().data.getRecord()) {
                    neoBean = response.body().data.getRecord();

                    neoPrice.setText(new BigDecimal(neoBean.getBalance()).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString());


                    tvPrice.setText(new BigDecimal(neoBean.getGnt().get(0).getBalance()).setScale(8, BigDecimal.ROUND_HALF_UP).toPlainString());

                    getgas.setText("(" + new BigDecimal(neoBean.getGnt().get(0).getAvailable()).setScale(8, BigDecimal.ROUND_HALF_UP).toPlainString() +getString(R.string.ketiqu)+")");

                    if (1 == App.get().getUnit()) {
                        neoTotleChPrice = new BigDecimal(neoBean.getBalance()).multiply(new BigDecimal(neoBean.getCap().getPrice_cny())).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                        gasTotleChPrice = new BigDecimal(neoBean.getGnt().get(0).getBalance()).multiply(new BigDecimal(neoBean.getGnt().get(0).getCap().getPrice_cny())).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                    } else {
                        neoTotleUsdPrice = new BigDecimal(neoBean.getBalance()).multiply(new BigDecimal(neoBean.getCap().getPrice_usd())).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                        gasTotleUsdrice = new BigDecimal(neoBean.getGnt().get(0).getBalance()).multiply(new BigDecimal(neoBean.getGnt().get(0).getCap().getPrice_usd())).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                    }

                    //计算总值
                    if (1 == App.get().getUnit()) {
                        BigDecimal neoBd = new BigDecimal(neoBean.getBalance()).multiply(new BigDecimal(neoBean.getCap().getPrice_cny()));
                        BigDecimal gasBd = new BigDecimal(neoBean.getGnt().get(0).getBalance()).multiply(new BigDecimal(neoBean.getGnt().get(0).getCap().getPrice_cny()));
                        BigDecimal getGasBd = new BigDecimal(neoBean.getGnt().get(0).getAvailable()).multiply(new BigDecimal(neoBean.getGnt().get(0).getCap().getPrice_cny()));
                        TOKENPrice = neoBd.add(gasBd).add(getGasBd).setScale(2, BigDecimal.ROUND_HALF_UP);
                    } else {
                        BigDecimal neoBd = new BigDecimal(neoBean.getBalance()).multiply(new BigDecimal(neoBean.getCap().getPrice_usd()));
                        BigDecimal gasBd = new BigDecimal(neoBean.getGnt().get(0).getBalance()).multiply(new BigDecimal(neoBean.getGnt().get(0).getCap().getPrice_usd()));
                        BigDecimal getGasBd = new BigDecimal(neoBean.getGnt().get(0).getAvailable()).multiply(new BigDecimal(neoBean.getGnt().get(0).getCap().getPrice_usd()));
                        TOKENPrice = neoBd.add(gasBd).add(getGasBd).setScale(2, BigDecimal.ROUND_HALF_UP);
                    }
                }

                if (null != response.body().data.getList() && response.body().data.getList().size() > 0) {
                    data.addAll(response.body().data.getList());
                }

                adapter.notifyDataSetChanged();
                //计算金额
                for (int i = 0; i < data.size(); i++) {
                    BigInteger price = new BigInteger(AppUtil.reverseArray(data.get(i).getBalance()));
                    BigDecimal currentPrice = new BigDecimal(price).divide(new BigDecimal(10).pow(Integer.parseInt(data.get(i).getDecimals())));
                    if (null != data.get(i).getGnt_category().getCap()) {
                        if (1 == App.get().getUnit()) {
                            TOKENPrice = TOKENPrice.add(currentPrice.multiply(new BigDecimal(data.get(i).getGnt_category().getCap().getPrice_cny())));
                        } else {
                            TOKENPrice = TOKENPrice.add(currentPrice.multiply(new BigDecimal(data.get(i).getGnt_category().getCap().getPrice_usd())));
                        }
                    }
                }

                //计算总金额
                if (1 == App.get().getUnit()) {
                    totleChPrice = TOKENPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                    changeSee(1, App.get().getSp().getBoolean(Constant.MAIN_SEE, true));
                } else {
                    totleUsdPrice = TOKENPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                    changeSee(0, App.get().getSp().getBoolean(Constant.MAIN_SEE, true));
                }
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<TokenBean>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<TokenBean>> response) {
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
                    String wallets = App.get().getSp().getString(Constant.WALLETS_BEIFEN, "");
                    String walletsZjc = App.get().getSp().getString(Constant.WALLETS_ZJC_BEIFEN, "");
                    if (wallets.contains(wallet.getAddress()) || walletsZjc.contains(wallet.getAddress())) {
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

        if (event.getEventCode() == Constant.EVENT_JIEDONG_DIALOG) {
            showJiedong();
        }

    }

    private void showSelectDialog() {

        View selectPopupWin = LayoutInflater.from(this).inflate(R.layout.view_popup_wallet_detaile, null, false);
        View hit = selectPopupWin.findViewById(R.id.hit);
        final View zhujici = selectPopupWin.findViewById(R.id.zhujicill);
        TextView keystore = (TextView) selectPopupWin.findViewById(R.id.keystore);
        TextView delete = (TextView) selectPopupWin.findViewById(R.id.delete);

        String wallets = App.get().getSp().getString(Constant.WALLETS_ZJC_BEIFEN, "");
        if (wallets.contains(wallet.getAddress())) {
            hit.setVisibility(View.GONE);
            zhujici.setVisibility(View.GONE);
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

        zhujici.setOnClickListener(new View.OnClickListener() {
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
                                String keystore = "";
                                final AccountManager accountManager = AccountManager.get(mActivity);
                                Account[] accounts = accountManager.getAccountsByType("com.inwecrypto.wallet");
                                Account account = null;
                                for (int i = 0; i < accounts.length; i++) {
                                    if (accounts[i].name.equals(wallet.getAddress())) {
                                        //accountManager.getUserData(accounts[i], pass.getText().toString());
                                        keystore = accountManager.getUserData(accounts[i], "wallet");
                                        account = accounts[i];
                                        break;
                                    }
                                }
                                Wallet neoWallet = null;
                                try {
                                    neoWallet = Neomobile.fromKeyStore(keystore, passWord);
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
                                    zjc = neoWallet.mnemonic(App.get().isZh()?"zh_CN":"en_US");
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
                                String keystore = "";
                                final AccountManager accountManager = AccountManager.get(mActivity);
                                Account[] accounts = accountManager.getAccountsByType("com.inwecrypto.wallet");
                                Account account = null;
                                for (int i = 0; i < accounts.length; i++) {
                                    if (accounts[i].name.equals(wallet.getAddress())) {
                                        //accountManager.getUserData(accounts[i], pass.getText().toString());
                                        keystore = accountManager.getUserData(accounts[i], "wallet");
                                        account = accounts[i];
                                        break;
                                    }
                                }
                                Wallet wal = null;
                                try {
                                    wal = Neomobile.fromKeyStore(keystore, passWord);
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
                                accountManager.setUserData(account, "type", Constant.BEIFEN);

                                String wallets = App.get().getSp().getString(Constant.WALLETS_BEIFEN, "");
                                if (!wallets.contains(wallet.getAddress())) {
                                    wallets = wallets + wallet.getAddress() + ",";
                                    App.get().getSp().putString(Constant.WALLETS_BEIFEN, wallets);
                                }
                                final String finalKeys = keystore;
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
                        if (passWord.length() == 0) {
                            ToastUtil.show(getString(R.string.qingshurumima));
                            return;
                        }
                        showLoading();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String keystore = "";
                                final AccountManager accountManager = AccountManager.get(mActivity);
                                Account[] accounts = accountManager.getAccountsByType("com.inwecrypto.wallet");
                                Account account = null;

                                for (int i = 0; i < accounts.length; i++) {
                                    if (accounts[i].name.equals(wallet.getAddress())) {
                                        account = accounts[i];
                                        //accountManager.getUserData(accounts[i], pass.getText().toString());
                                        keystore = accountManager.getUserData(accounts[i], "wallet");
                                        break;
                                    }
                                }
                                try {
                                    Neomobile.fromKeyStore(keystore, passWord);
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
                                        if (walletStr.contains(wallet.getAddress())) {
                                            walletStr = walletStr.replace(wallet.getAddress(), "");
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
        View zhujici = selectPopupWin.findViewById(R.id.zhujicill);
        View hit = selectPopupWin.findViewById(R.id.hit);
        zhujici.setVisibility(View.GONE);
        hit.setVisibility(View.GONE);
        ImageView transfer= (ImageView) selectPopupWin.findViewById(R.id.transfer);
        transfer.setImageResource(R.mipmap.neozhuanhuanxxhdpi);

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

    private void showJiedong() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_dialog_jiedong, null, false);
        TextView ok = (TextView) view.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mMaterialDialog) {
                    mMaterialDialog.dismiss();
                }
            }
        });
        mMaterialDialog = new MaterialDialog(mActivity).setView(view);
        mMaterialDialog.setBackgroundResource(R.drawable.trans_bg);
        mMaterialDialog.setCanceledOnTouchOutside(true);
        mMaterialDialog.show();
    }

    private void changeSee(int unit, boolean isSee) {
        if (isSee) {
            if (unit == 1) {
                amount.setText("￥ " + totleChPrice);
                topPrice.setText("￥ " + totleChPrice);
                neoChPrice.setText("￥" + neoTotleChPrice);
                tvEthChPrice.setText("￥" + gasTotleChPrice);
            } else {
                amount.setText("$ " + totleUsdPrice);
                topPrice.setText("$ " + totleUsdPrice);
                neoChPrice.setText("$" + neoTotleUsdPrice);
                tvEthChPrice.setText("$" + gasTotleUsdrice);
            }
            see.setImageResource(R.mipmap.openpassxxhdpi);
            topSee.setImageResource(R.mipmap.openpassxxhdpi);
        } else {
            if (unit == 1) {
                amount.setText("￥ ****");
                topPrice.setText("￥ ****");
                neoChPrice.setText("￥****");
                tvEthChPrice.setText("￥****");
            } else {
                amount.setText("$ ****");
                topPrice.setText("$ ****");
                neoChPrice.setText("$****");
                tvEthChPrice.setText("$****");
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
