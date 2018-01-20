package com.inwecrypto.wallet.ui.wallet.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.common.util.AnimUtil;
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

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import ethmobile.Wallet;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.bean.WalletCountBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.wallet.adapter.GntAdapter;
import com.inwecrypto.wallet.common.widget.MaterialDialog;

/**
 * Created by Administrator on 2017/7/16.
 * 功能描述：
 * 版本：@version
 */

public class HotWalletActivity extends BaseActivity {

    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_watch)
    TextView tvWatch;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_ch_price)
    TextView tvChPrice;
    @BindView(R.id.eth_price)
    TextView ethPrice;
    @BindView(R.id.tv_eth_ch_price)
    TextView tvEthChPrice;
    @BindView(R.id.wallet_list)
    SwipeMenuRecyclerView walletList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayoutCompat swipeRefresh;
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.add_gnt)
    ImageView addGnt;
    @BindView(R.id.code)
    ImageView code;
    @BindView(R.id.eth_rl)
    RelativeLayout ethRl;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.titlell)
    View titlell;
    @BindView(R.id.titlePrice)
    TextView titlePrice;
    @BindView(R.id.address_rl)
    LinearLayout addressRl;

    private ArrayList<TokenBean.ListBean> data = new ArrayList<>();
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
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title.setText(R.string.wodezichan);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wallet.getType().equals("2")) {
                    showWatchSelectDialog();
                } else {
                    showSelectDialog();
                }
            }
        });

        addGnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, AddTokenActivity.class);
                intent.putExtra("id", wallet.getCategory_id());
                intent.putExtra("walletId", wallet.getId());
                keepTogo(intent);
            }
        });

        addressRl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(wallet.getAddress());
                ToastUtil.show(getString(R.string.gaidizhiyifuzhi));
                return false;
            }
        });

        addressRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ReceiveActivity.class);
                intent.putExtra("wallet", wallet);
                keepTogo(intent);
            }
        });

        adapter = new GntAdapter(this, R.layout.wallet_item, data);
        walletList.setLayoutManager(new LinearLayoutManager(this));
        walletList.setSwipeMenuCreator(new SwipeMenuCreator() {
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
        walletList.setAdapter(adapter);
        walletList.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                if (menuBridge.getPosition() == 0) {//删除
                    WalletApi.userGntDelete(mActivity, data.get(menuBridge.getAdapterPosition()).getId(), new JsonCallback<LzyResponse<Object>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<Object>> response) {
                            walletList.smoothCloseMenu();
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
                            walletList.smoothCloseMenu();
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
        ethRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null==wallet){
                    ToastUtil.show(R.string.wallet_data_error);
                    return;
                }
                Intent intent = new Intent(mActivity, TokenWalletActivity.class);
                intent.putExtra("isEht", true);
                intent.putExtra("wallet", wallet);
                keepTogo(intent);
            }
        });
        walletList.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (null==wallet){
                    ToastUtil.show(R.string.wallet_data_error);
                    return;
                }
                Intent intent = new Intent(mActivity, TokenWalletActivity.class);
                intent.putExtra("isEht", false);
                intent.putExtra("gnt", data.get(position));
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
            tvChPrice.setText(getString(R.string.zongzichan)+"(￥)");
        } else {
            tvChPrice.setText(getString(R.string.zongzichan)+"总资产($)");
        }

        tvName.setText(wallet.getName());
        tvAddress.setText(wallet.getAddress());
        Glide.with(this).load(wallet.getIcon()).crossFade().into(ivImg);
        switch (new Integer(wallet.getType())) {
            case 0:
                String wallets = App.get().getSp().getString(Constant.WALLETS_BEIFEN, "");
                if (wallets.contains(wallet.getAddress())) {
                    tvWatch.setVisibility(View.GONE);
                } else {
                    tvWatch.setVisibility(View.VISIBLE);
                    tvWatch.setText(getString(R.string.weibeifen));
                    tvWatch.setBackgroundResource(R.drawable.round_solid_pink_bg);
                }
                break;
            case 1:
                tvWatch.setVisibility(View.GONE);
                break;
            case 2:
                tvWatch.setVisibility(View.VISIBLE);
                tvWatch.setText(getString(R.string.guancha));
                tvWatch.setBackgroundResource(R.drawable.round_999dp_bule_bg);
                break;
        }

        if (timer == null) {
            timer = new Timer(true);
            task = new TimerTask() {

                @Override
                public void run() {
                    //刷新列表
                    refershData();
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
        isEth=false;
        isToken=false;
        String sb = "[" + wallet.getId() + "]";
        WalletApi.conversionWallet(mActivity, sb.toString(), new JsonCallback<LzyResponse<CommonListBean<WalletCountBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<WalletCountBean>>> response) {
                if (null==ethPrice||null==tvEthChPrice||null==tvPrice||null==titlePrice){
                    return;
                }
                ETHEther = ETHEther.multiply(new BigDecimal(0));
                ETHPrice = ETHPrice.multiply(new BigDecimal(0));
                ArrayList<WalletCountBean> walletPrices = response.body().data.getList();
                BigDecimal currentPrice = new BigDecimal(AppUtil.toD(walletPrices.get(0).getBalance().replace("0x", "0")));
                ETHEther = ETHEther.add(currentPrice).divide(Constant.pEther, 4, BigDecimal.ROUND_HALF_UP);
                if (1 == App.get().getUnit()) {
                    ETHPrice = ETHPrice.add(currentPrice.divide(Constant.pEther).multiply(new BigDecimal(walletPrices.get(0).getCategory().getCap().getPrice_cny())));
                    ethPrice.setText(ETHEther.toString());
                    tvEthChPrice.setText("≈￥" + ETHPrice.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                } else {
                    ETHPrice = ETHPrice.add(currentPrice.divide(Constant.pEther).multiply(new BigDecimal(walletPrices.get(0).getCategory().getCap().getPrice_usd())));
                    ethPrice.setText(ETHEther.toString());
                    tvEthChPrice.setText("≈$" + ETHPrice.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                }

                isEth=true;
                if (isToken){
                    //计算总金额
                    if (1 == App.get().getUnit()) {
                        tvPrice.setText(TOKENPrice.add(ETHPrice).setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
                        titlePrice.setText("(￥" + TOKENPrice.add(ETHPrice).setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString() + ")");
                    } else {
                        tvPrice.setText(TOKENPrice.add(ETHPrice).setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
                        titlePrice.setText("($" + TOKENPrice.add(ETHPrice).setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString() + ")");
                    }
                }

            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<CommonListBean<WalletCountBean>>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }
        });

        //请求代币列表
        WalletApi.conversion(mActivity, wallet.getId(), new JsonCallback<LzyResponse<TokenBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<TokenBean>> response) {
                if (null==tvPrice||null==titlePrice){
                    return;
                }
                data.clear();
                if (null != response.body().data && response.body().data.getList().size() > 0) {
                    data.addAll(response.body().data.getList());
                }
                adapter.notifyDataSetChanged();
                TOKENPrice=new BigDecimal("0.00");
                //计算金额
                for (TokenBean.ListBean token:data){
                    BigDecimal currentPrice = new BigDecimal(AppUtil.toD(token.getBalance().replace("0x", "0")));
                    if (null!=token.getGnt_category().getCap()){

                        if (1 == App.get().getUnit()) {
                         TOKENPrice=TOKENPrice.add(currentPrice.divide(Constant.pEther).multiply(new BigDecimal(token.getGnt_category().getCap().getPrice_cny())));
                        }else {
                           TOKENPrice=TOKENPrice.add(currentPrice.divide(Constant.pEther).multiply(new BigDecimal(token.getGnt_category().getCap().getPrice_usd())));
                        }
                    }
                }
                isToken=true;
                if (isEth){
                    //计算总金额
                    if (1 == App.get().getUnit()) {
                        tvPrice.setText(TOKENPrice.add(ETHPrice).setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
                        titlePrice.setText("(￥" + TOKENPrice.add(ETHPrice).setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString() + ")");
                    } else {
                        tvPrice.setText(TOKENPrice.add(ETHPrice).setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
                        titlePrice.setText("($" + TOKENPrice.add(ETHPrice).setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString() + ")");
                    }
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
                if (null!=swipeRefresh){
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
            tvWatch.setVisibility(View.GONE);
            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET));
        }
        if (event.getEventCode() == Constant.EVENT_WATCH_TRANSFER){
            wallet.setType("0");
            switch (new Integer(wallet.getType())) {
                case 0:
                    String wallets = App.get().getSp().getString(Constant.WALLETS_BEIFEN, "");
                    String walletsZjc = App.get().getSp().getString(Constant.WALLETS_ZJC_BEIFEN, "");
                    if (wallets.contains(wallet.getAddress())||walletsZjc.contains(wallet.getAddress())) {
                        tvWatch.setVisibility(View.GONE);
                    } else {
                        tvWatch.setVisibility(View.VISIBLE);
                        tvWatch.setText(getString(R.string.weibeifen));
                        tvWatch.setBackgroundResource(R.drawable.round_solid_pink_bg);
                    }
                    break;
                case 1:
                    tvWatch.setVisibility(View.GONE);
                    break;
                case 2:
                    tvWatch.setVisibility(View.VISIBLE);
                    tvWatch.setText(getString(R.string.guancha));
                    tvWatch.setBackgroundResource(R.drawable.round_999dp_bule_bg);
                    break;
            }
            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET));
        }
    }

    private void showSelectDialog() {

        View selectPopupWin = LayoutInflater.from(this).inflate(R.layout.view_popup_wallet_detaile, null, false);
        View hit = selectPopupWin.findViewById(R.id.hit);
        final TextView zhujici = (TextView) selectPopupWin.findViewById(R.id.zhujici);
        TextView keystore = (TextView) selectPopupWin.findViewById(R.id.keystore);
        TextView delete = (TextView) selectPopupWin.findViewById(R.id.delete);

        String wallets = App.get().getSp().getString(Constant.WALLETS_ZJC_BEIFEN, "");
        if (wallets.contains(wallet.getAddress())) {
            hit.setVisibility(View.GONE);
            zhujici.setVisibility(View.GONE);
        }

        final PopupWindow window = new PopupWindow(selectPopupWin, DensityUtil.dip2px(this, 100), WindowManager.LayoutParams.WRAP_CONTENT);
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
        window.showAsDropDown(txtRightTitle, 0,-DensityUtil.dip2px(this,10));
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
                View view = LayoutInflater.from(mActivity).inflate(R.layout.view_dialog_pass, null, false);
                final EditText pass = (EditText) view.findViewById(R.id.et_pass);
                TextView cancle = (TextView) view.findViewById(R.id.cancle);
                TextView ok = (TextView) view.findViewById(R.id.ok);
                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pass.getText().toString().length() == 0) {
                            ToastUtil.show(getString(R.string.qingshurumima));
                            return;
                        }
                        showLoading();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                byte[] b = new byte[0];
                                final AccountManager accountManager = AccountManager.get(mActivity);
                                Account[] accounts = accountManager.getAccountsByType("com.inwecrypto.wallet");
                                Account account = null;
                                for (int i = 0; i < accounts.length; i++) {
                                    if (accounts[i].name.equals(wallet.getAddress())) {
                                        //accountManager.getUserData(accounts[i], pass.getText().toString());
                                        b = accountManager.getUserData(accounts[i], "wallet").getBytes();
                                        account = accounts[i];
                                        break;
                                    }
                                }
                                Wallet ethWallet = null;
                                try {
                                    //ethWallet = Unichain.openETHWallet(b, pass.getText().toString());
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
                                    //zjc = ethWallet.mnemonic();
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
                                        mMaterialDialog.dismiss();
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

                mMaterialDialog = new MaterialDialog(mActivity).setView(view);
                mMaterialDialog.setBackgroundResource(R.drawable.trans_bg);
                mMaterialDialog.setCanceledOnTouchOutside(true);
                mMaterialDialog.show();
                window.dismiss();
            }
        });

        keystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(mActivity).inflate(R.layout.view_dialog_keystore, null, false);
                final EditText pass = (EditText) view.findViewById(R.id.et_pass);
                TextView cancle = (TextView) view.findViewById(R.id.cancle);
                TextView ok = (TextView) view.findViewById(R.id.ok);
                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pass.getText().toString().length() == 0) {
                            ToastUtil.show(getString(R.string.qingshurumima));
                            return;
                        }
                        showLoading();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                byte[] b = new byte[0];
                                final AccountManager accountManager = AccountManager.get(mActivity);
                                Account[] accounts = accountManager.getAccountsByType("com.inwecrypto.wallet");
                                Account account = null;
                                for (int i = 0; i < accounts.length; i++) {
                                    if (accounts[i].name.equals(wallet.getAddress())) {
                                        //accountManager.getUserData(accounts[i], pass.getText().toString());
                                        b = accountManager.getUserData(accounts[i], "wallet").getBytes();
                                        account = accounts[i];
                                        break;
                                    }
                                }
                                Wallet wal = null;
                                try {
                                    //wal = Unichain.openETHWallet(b, pass.getText().toString());
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
                                byte[] keys = new byte[0];
                                try {
                                    //keys = wal.encrypt(pass.getText().toString());
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

                                String wallets = App.get().getSp().getString(Constant.WALLETS_BEIFEN, "");
                                if (!wallets.contains(wallet.getAddress())) {
                                    wallets = wallets + wallet.getAddress() + ",";
                                    App.get().getSp().putString(Constant.WALLETS_BEIFEN, wallets);
                                }
                                final byte[] finalKeys = keys;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideLoading();
                                        mMaterialDialog.dismiss();
                                        tvWatch.setVisibility(View.GONE);
                                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET));
                                        Intent intent1 = new Intent(Intent.ACTION_SEND);
                                        try {
                                            intent1.putExtra(Intent.EXTRA_TEXT, new String(finalKeys, "utf-8"));
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        intent1.setType("text/plain");
                                        startActivity(Intent.createChooser(intent1, "share"));
                                    }
                                });
                            }
                        }).start();

                    }
                });

                mMaterialDialog = new MaterialDialog(mActivity).setView(view);
                mMaterialDialog.setBackgroundResource(R.drawable.trans_bg);
                mMaterialDialog.setCanceledOnTouchOutside(true);
                mMaterialDialog.show();
                window.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(mActivity).inflate(R.layout.view_dialog_pass, null, false);
                final EditText pass = (EditText) view.findViewById(R.id.et_pass);
                TextView cancle = (TextView) view.findViewById(R.id.cancle);
                TextView ok = (TextView) view.findViewById(R.id.ok);
                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pass.getText().toString().length() == 0) {
                            ToastUtil.show(getString(R.string.qingshurumima));
                            return;
                        }
                        showLoading();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                byte[] b = new byte[0];
                                final AccountManager accountManager = AccountManager.get(mActivity);
                                Account[] accounts = accountManager.getAccountsByType("com.inwecrypto.wallet");
                                Account account = null;

                                for (int i = 0; i < accounts.length; i++) {
                                    if (accounts[i].name.equals(wallet.getAddress())) {
                                        account = accounts[i];
                                        //accountManager.getUserData(accounts[i], pass.getText().toString());
                                        b = accountManager.getUserData(accounts[i], "wallet").getBytes();
                                        break;
                                    }
                                }
                                try {
                                    //Unichain.openETHWallet(b, pass.getText().toString());
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

                mMaterialDialog = new MaterialDialog(mActivity).setView(view);
                mMaterialDialog.setBackgroundResource(R.drawable.trans_bg);
                mMaterialDialog.setCanceledOnTouchOutside(true);
                mMaterialDialog.show();
                window.dismiss();
            }
        });
    }


    private void showWatchSelectDialog() {

        View selectPopupWin = LayoutInflater.from(this).inflate(R.layout.view_popup_wallet_detaile, null, false);
        TextView zhujici = (TextView) selectPopupWin.findViewById(R.id.zhujici);
        View hit = selectPopupWin.findViewById(R.id.hit);
        zhujici.setVisibility(View.GONE);
        hit.setVisibility(View.GONE);

        TextView keystore = (TextView) selectPopupWin.findViewById(R.id.keystore);
        keystore.setText(R.string.zhuanweierqianbao);
        TextView delete = (TextView) selectPopupWin.findViewById(R.id.delete);

        final PopupWindow window = new PopupWindow(selectPopupWin, DensityUtil.dip2px(this, 100), WindowManager.LayoutParams.WRAP_CONTENT);
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
        window.showAsDropDown(txtRightTitle, 0,-DensityUtil.dip2px(this,10));
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
                Intent intent = new Intent(mActivity, WatchImportWalletTypeActivity.class);
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
                                if (null!=mMaterialDialog){
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
