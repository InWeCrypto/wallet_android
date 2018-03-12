package com.inwecrypto.wallet.ui.newneo;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.LoginBean;
import com.inwecrypto.wallet.bean.NewNeoTokenListBean;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.bean.TotlePriceBean;
import com.inwecrypto.wallet.bean.UpdateBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.bean.WalletCountBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.MeApi;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.imageloader.GlideCircleTransform;
import com.inwecrypto.wallet.common.util.AnimUtil;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.CacheUtils;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.BetterRecyclerView;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.service.DownloadService;
import com.inwecrypto.wallet.ui.QuickActivity;
import com.inwecrypto.wallet.ui.login.LoginActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.db.CacheManager;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/1/8 11:01
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class WalletFragment extends BaseFragment {

    @BindView(R.id.wallet_manager_bottom)
    ImageView walletManagerBottom;
    @BindView(R.id.amount)
    TextView amount;
    @BindView(R.id.see)
    ImageView see;
    @BindView(R.id.user_img)
    ImageView userImg;
    @BindView(R.id.right)
    FrameLayout right;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.title_price)
    TextView titlePrice;
    @BindView(R.id.charge)
    TextView charge;
    @BindView(R.id.topsee)
    ImageView topsee;
    @BindView(R.id.top_wallet_manager)
    ImageView topWalletManager;
    @BindView(R.id.top_user)
    ImageView topUser;
    @BindView(R.id.titlell)
    LinearLayout titlell;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.list)
    BetterRecyclerView list;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayoutCompat swipeRefresh;

    private boolean isFinish;
    private ArrayList<WalletBean> wallet = new ArrayList<>();
    private HashMap<String, TokenBean.ListBean> neoGnt = new HashMap<>();
    private HashMap<Integer, Integer> walletPosition = new HashMap<>();
    private ArrayList<WalletCountBean> walletCount = new ArrayList<>();
    private ArrayList<TokenBean.ListBean> neoList = new ArrayList<>();
    private BigDecimal ETHEther = new BigDecimal("0.00");
    private BigDecimal NEOEther = new BigDecimal("0.00");
    private String ethCnyPrice="0.00";
    private String ethUsdPrice="0.00";
    private String neoCnyPrice="0.00";
    private String neoUsdPrice="0.00";
    private int index;

    private NewNeoMainTokenAdapter adapter;
    private boolean isSee = true;

    private String totleChPrice = "0.00";
    private String totleUsdPrice = "0.00";

    private boolean needRefresh;

    private HeaderAndFooterWrapper header;

    @Override
    protected int setLayoutID() {
        return R.layout.newneo_main_activity;
    }


    private Timer timer;
    private TimerTask task;

    @Override
    protected void initView() {

        setOpenEventBus(true);

        walletManagerBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()){
                    keepTogo(LoginActivity.class);
                    return;
                }
                Intent intent = new Intent(mActivity, NewNeoWalletListActivity.class);
                intent.putExtra("wallet", wallet);
                keepTogo(intent);
            }
        });

        topWalletManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()){
                    keepTogo(LoginActivity.class);
                    return;
                }
                Intent intent = new Intent(mActivity, NewNeoWalletListActivity.class);
                intent.putExtra("wallet", wallet);
                keepTogo(intent);
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mActivity, MeActivity.class);
//                keepTogo(intent);
            }
        });

        topUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mActivity, MeActivity.class);
//                keepTogo(intent);
            }
        });

        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()){
                    return;
                }
                boolean isSee = App.get().getSp().getBoolean(Constant.MAIN_SEE, true);
                App.get().getSp().putBoolean(Constant.MAIN_SEE, !isSee);
                changeSee(App.get().getUnit(), !isSee);
            }
        });

        topsee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()){
                    return;
                }
                boolean isSee = App.get().getSp().getBoolean(Constant.MAIN_SEE, true);
                App.get().getSp().putBoolean(Constant.MAIN_SEE, !isSee);
                changeSee(App.get().getUnit(), !isSee);
            }
        });

        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    swipeRefresh.setEnabled(true);
                } else {
                    swipeRefresh.setEnabled(false);
                }
                if (verticalOffset == 0) {
                    titlell.setVisibility(View.INVISIBLE);
                }
                if (verticalOffset + appBarLayout.getTotalScrollRange() == 0) {
                    titlell.setVisibility(View.VISIBLE);
                    if (!isFinish) {
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

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!App.get().isLogin()){
                    swipeRefresh.setRefreshing(false);
                    return;
                }
                //从网络获取钱包
                getInfoOnNet();
            }
        });

        adapter = new NewNeoMainTokenAdapter(mActivity, R.layout.newneo_wallet_item, neoList);
        header=new HeaderAndFooterWrapper(adapter);
        View view=LayoutInflater.from(getContext()).inflate(R.layout.wallet_fragment_header,null,false);
        view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        header.addHeaderView(view);
        list.setLayoutManager(new LinearLayoutManager(mActivity));
        list.setAdapter(header);
        list.setNestedScrollingEnabled(false);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //弹出钱包选择框
                FragmentManager fm = mActivity.getSupportFragmentManager();
                WalletListFragment walletlist = new WalletListFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("wallets", neoList.size()>0?neoList.get(position-1).getWallets():null);
                walletlist.setArguments(bundle);
                walletlist.show(fm, "list");
                walletlist.setOnNextListener(new WalletListFragment.OnNextInterface() {
                    @Override
                    public void onNext(Dialog dialog) {
                        dialog.cancel();
                        FragmentManager fm = mActivity.getSupportFragmentManager();
                        CreateWalletFragment create = new CreateWalletFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("wallets", wallet);
                        create.setArguments(bundle);
                        create.show(fm, "create");
                    }
                });

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        if (timer == null) {
            timer = new Timer(true);
            task = new TimerTask() {

                @Override
                public void run() {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //刷新列表
                            loadData();
                        }
                    });
                }
            };
            timer.schedule(task, 30000, 30000);
        }
        if (null == swipeRefresh) {
            return;
        }

        if (App.get().getSp().getBoolean(Constant.FIRST_1,true)){
            swipeRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int[] location=new int[2];
                    walletManagerBottom.getLocationOnScreen(location);
                    Intent intent=new Intent(mActivity,QuickActivity.class);
                    intent.putExtra("type",1);
                    intent.putExtra("y",location[1]);
                    keepTogo(intent);
                }
            },300);
        }

        if (!App.get().isLogin()){
            return;
        }
        //获取缓存数据
        getChace();
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });

    }

    @Override
    protected void loadData() {
        if (!App.get().isLogin()){
            swipeRefresh.setRefreshing(false);
            return;
        }

        setImg();
        OkGo.getInstance().cancelTag(this);
        //从网络获取钱包
        getInfoOnNet();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (needRefresh){
            needRefresh=false;
            //loadData();
        }
        if (!isFirst&&!isLoadSuccess&&NetworkUtils.isConnected(getContext())&&App.get().isLogin()){
            swipeRefresh.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefresh.setRefreshing(true);
                }
            });
            loadData();
        }
    }

    private void setImg() {
        LoginBean loginBean = App.get().getLoginBean();
        if (null != loginBean) {
            if (null != loginBean.getImg() && loginBean.getImg().length() > 0) {
                Glide.with(this)
                        .load(loginBean.getImg())
                        .crossFade()
                        .placeholder(R.mipmap.touxiang)
                        .transform(new GlideCircleTransform(mActivity))
                        .into(topUser);
                Glide.with(this)
                        .load(loginBean.getImg())
                        .crossFade()
                        .placeholder(R.mipmap.touxiang)
                        .transform(new GlideCircleTransform(mActivity))
                        .into(userImg);
            } else {
                Glide.with(this)
                        .load(R.mipmap.touxiang)
                        .crossFade()
                        .transform(new GlideCircleTransform(mActivity))
                        .into(topUser);
                Glide.with(this)
                        .load(R.mipmap.touxiang)
                        .crossFade()
                        .transform(new GlideCircleTransform(mActivity))
                        .into(userImg);
            }
        }
    }

    private void getChace() {
        //获取总资产缓存
        TotlePriceBean priceBean =CacheUtils.getCache((App.isMain ? Constant.TOTAL_PRICE : Constant.TOTAL_TEST_PRICE)+(null==App.get().getLoginBean()?"":App.get().getLoginBean().getEmail()));
        if (null==priceBean){
            priceBean=new TotlePriceBean();
        }
        if (1 == App.get().getUnit()) {
            totleChPrice = (null == priceBean.totleCny ? "0.00" : priceBean.totleCny);
            changeSee(1, App.get().getSp().getBoolean(Constant.MAIN_SEE, true));
        } else {
            totleUsdPrice = (null == priceBean.totleUsd ? "0.00" : priceBean.totleUsd);
            changeSee(0, App.get().getSp().getBoolean(Constant.MAIN_SEE, true));
        }

        //获取代币缓存列表
        ArrayList<TokenBean.ListBean> tokenChaceList =CacheUtils.getCache((App.isMain ? Constant.NEO_LIST : Constant.NEO_TEST_LIST)+(null==App.get().getLoginBean()?"":App.get().getLoginBean().getEmail()));
        if (null==tokenChaceList){
            tokenChaceList=new ArrayList<>();
        }
        neoList.clear();
        neoList.addAll(tokenChaceList);
        adapter.notifyDataSetChanged();

        //获取钱包缓存列表
        LzyResponse<CommonListBean<WalletBean>> chaceList= CacheUtils.getCache(Constant.WALLETS+ App.isMain);
        if (null!=chaceList&&null!=chaceList.data){
            getWalletList(chaceList.data);
        }
    }

    private void getInfoOnNet() {
        WalletApi.wallet(mActivity, new JsonCallback<LzyResponse<CommonListBean<WalletBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                getWalletList(response.body().data);
                if (!response.isFromCache()) {
                    //获取代币列表
                    getTokenList();
                }
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                super.onCacheSuccess(response);
                //onSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.jiazaishibai));
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void getWalletList(CommonListBean<WalletBean> response) {
        wallet.clear();
        if (null != response.getList()) {
            walletPosition.clear();
            String wallets = App.get().getSp().getString(Constant.WALLETS, "");
            String wallets_beifen = App.get().getSp().getString(Constant.WALLETS_BEIFEN, "");
            String walletsZjc = App.get().getSp().getString(Constant.WALLETS_ZJC_BEIFEN, "");
            for (int i = 0; i < response.getList().size(); i++) {
                    if (wallets.contains(response.getList().get(i).getAddress())) {
                        if (wallets_beifen.contains(response.getList().get(i).getAddress()) || walletsZjc.contains(response.getList().get(i).getAddress())) {
                            response.getList().get(i).setType(Constant.BEIFEN);
                        } else {
                            response.getList().get(i).setType(Constant.ZHENGCHANG);
                        }
                    } else {
                        response.getList().get(i).setType(Constant.GUANCHA);
                    }
                    walletPosition.put(response.getList().get(i).getId(), wallet.size());
                    wallet.add(response.getList().get(i));
            }
        }
    }

    private void getTokenList() {

        if (wallet.size() == 0) {
            if (1 == App.get().getUnit()) {
                totleChPrice = "0.00";
                changeSee(1, App.get().getSp().getBoolean(Constant.MAIN_SEE, true));
            } else {
                totleUsdPrice = "0.00";
                changeSee(0, App.get().getSp().getBoolean(Constant.MAIN_SEE, true));
            }

            neoGnt.clear();
            //添加 neo
            TokenBean.ListBean neo = new TokenBean.ListBean();
            TokenBean.ListBean.GntCategoryBeanX neoCategory = new TokenBean.ListBean.GntCategoryBeanX();
            neo.setName("NEO");
            neo.setBalance("0.0000");
            neoCategory.setIcon(R.mipmap.tokenneoxxhdpi + "");
            neo.setGnt_category(neoCategory);
            neoGnt.put("NEO", neo);

            //添加 neo
            TokenBean.ListBean eth = new TokenBean.ListBean();
            TokenBean.ListBean.GntCategoryBeanX ethCategory = new TokenBean.ListBean.GntCategoryBeanX();
            eth.setName("ETH");
            eth.setBalance("0.0000");
            ethCategory.setIcon(R.mipmap.eth_icon + "");
            eth.setGnt_category(ethCategory);
            neoGnt.put("ETH", eth);

            setDataList();
            swipeRefresh.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefresh.setRefreshing(false);
                }
            });
            return;
        }
        //获取每个钱包的eth 和 neo 余额
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (WalletBean wa : wallet) {
            sb.append(wa.getId() + ",");
        }
        if (sb.length() != 1) {
            sb.delete(sb.length() - 1, sb.length());
        }
        sb.append("]");
        WalletApi.conversion(this, sb.toString(), new JsonCallback<LzyResponse<CommonListBean<WalletCountBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<WalletCountBean>>> response) {
                ETHEther = ETHEther.multiply(new BigDecimal(0));
                NEOEther = NEOEther.multiply(new BigDecimal(0));
                ethCnyPrice="0.00";
                ethUsdPrice="0.00";
                neoCnyPrice="0.00";
                neoUsdPrice="0.00";

                neoGnt.clear();
                walletCount.clear();
                walletCount.addAll(response.body().data.getList());
                ArrayList<NewNeoTokenListBean> neoWallets = new ArrayList<>();
                ArrayList<NewNeoTokenListBean> ethWallets = new ArrayList<>();
                //计算ethPrice
                for (WalletCountBean count : walletCount) {
                    //计算 eth 价格
                    if (count.getCategory_id()==1&&null != count.getBalance()) {
                        //进行计算
                        BigDecimal currentPrice = new BigDecimal(AppUtil.toD(count.getBalance().replace("0x", "0")));
                        ETHEther = ETHEther.add(currentPrice.divide(Constant.pEther));
                        if(null!=count.getCategory().getCap()){
                            ethCnyPrice=count.getCategory().getCap().getPrice_cny();
                            ethUsdPrice = count.getCategory().getCap().getPrice_usd();
                        }else {
                            ethCnyPrice= "0.00";
                            ethUsdPrice = "0.00";
                        }
                        NewNeoTokenListBean list = new NewNeoTokenListBean(count.getName()
                                , currentPrice.divide(Constant.pEther).setScale(4,BigDecimal.ROUND_DOWN).toPlainString()
                                , count.getId()
                                , wallet.get(walletPosition.get(count.getId())));
                        ethWallets.add(list);
                    }else if (count.getCategory_id()==2&&null != count.getBalance()){
                        BigDecimal currentPrice = new BigDecimal(count.getBalance());
                        NEOEther = NEOEther.add(currentPrice);
                        if(null!=count.getCategory().getCap()){
                            neoCnyPrice = count.getCategory().getCap().getPrice_cny();
                            neoUsdPrice = count.getCategory().getCap().getPrice_usd();
                        }else {
                            neoCnyPrice = "0.00";
                            neoUsdPrice = "0.00";
                        }
                        NewNeoTokenListBean list = new NewNeoTokenListBean(count.getName(), count.getBalance(), count.getId(), wallet.get(walletPosition.get(count.getId())));
                        neoWallets.add(list);
                    }
                }

                //添加 neo
                TokenBean.ListBean neo = new TokenBean.ListBean();
                TokenBean.ListBean.GntCategoryBeanX neoCategory = new TokenBean.ListBean.GntCategoryBeanX();
                TokenBean.ListBean.GntCategoryBeanX.CapBeanX neoCap = new TokenBean.ListBean.GntCategoryBeanX.CapBeanX();
                neo.setName("NEO");
                neo.setBalance(NEOEther.toPlainString());
                neoCategory.setIcon(R.mipmap.tokenneoxxhdpi + "");
                neoCap.setPrice_cny(neoCnyPrice);
                neoCap.setPrice_usd(neoUsdPrice);
                neoCategory.setCap(neoCap);
                neo.setGnt_category(neoCategory);
                neo.setWallets(neoWallets);
                neoGnt.put("NEO", neo);

                //添加 eth
                TokenBean.ListBean eth=new TokenBean.ListBean();
                TokenBean.ListBean.GntCategoryBeanX ethCategory=new TokenBean.ListBean.GntCategoryBeanX();
                TokenBean.ListBean.GntCategoryBeanX.CapBeanX ethCap=new TokenBean.ListBean.GntCategoryBeanX.CapBeanX();
                eth.setName("ETH");
                eth.setBalance(ETHEther.toPlainString());
                ethCategory.setIcon(R.mipmap.eth_icon+"");
                ethCap.setPrice_cny(ethCnyPrice);
                ethCap.setPrice_usd(ethUsdPrice);
                ethCategory.setCap(ethCap);
                eth.setGnt_category(ethCategory);
                eth.setWallets(ethWallets);
                neoGnt.put("ETH",eth);

                index = 0;
                if (walletCount.size() > 0) {
                    getTokenPrice();
                } else {
                    swipeRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<WalletCountBean>>> response) {
                super.onError(response);
                if (NetworkUtils.isConnected(getContext())){
                    ToastUtil.show(R.string.jiazaishibai);
                }
                swipeRefresh.setRefreshing(false);
            }
        });

    }

    private void getTokenPrice() {
        //请求代币列表
        WalletApi.conversionErrorCache(this, walletCount.get(index).getId(), new JsonCallback<LzyResponse<TokenBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<TokenBean>> response) {
                if (index >= walletCount.size()) {
                    return;
                }
                if (null != response.body().data.getRecord()) {
                    if (response.body().data.getRecord().getCategory_id() == 2) {
                        TokenBean.RecordBean record = response.body().data.getRecord();

                        if (null!=record.getGnt().get(0).getCap()&&null != neoGnt.get(record.getGnt().get(0).getCap().getName()+"(NEO)")) {
                            TokenBean.ListBean bfGnt = neoGnt.get(record.getGnt().get(0).getCap().getName()+"(NEO)");
                            BigDecimal secPrice = new BigDecimal(record.getGnt().get(0).getBalance());

                            bfGnt.setBalance(secPrice.add(new BigDecimal(bfGnt.getBalance())).toString());
                            NewNeoTokenListBean list = new NewNeoTokenListBean(walletCount.get(index).getName(), record.getGnt().get(0).getBalance(), walletCount.get(index).getId(), wallet.get(walletPosition.get(walletCount.get(index).getId())));
                            bfGnt.getWallets().add(list);
                            neoGnt.put(record.getGnt().get(0).getCap().getName()+"(NEO)", bfGnt);
                        } else if (null!=record.getGnt().get(0).getCap()){
                            ArrayList<NewNeoTokenListBean> neoWallets = new ArrayList<>();
                            TokenBean.ListBean gasBean = new TokenBean.ListBean();
                            TokenBean.ListBean.GntCategoryBeanX gasGntBean = new TokenBean.ListBean.GntCategoryBeanX();
                            TokenBean.ListBean.GntCategoryBeanX.CapBeanX gasCapBean = new TokenBean.ListBean.GntCategoryBeanX.CapBeanX();
                            gasBean.setName(record.getGnt().get(0).getCap().getName());
                            gasBean.setBalance(new BigDecimal(record.getGnt().get(0).getBalance()).toPlainString());
                            gasCapBean.setPrice_cny(record.getGnt().get(0).getCap().getPrice_cny());
                            gasCapBean.setPrice_usd(record.getGnt().get(0).getCap().getPrice_usd());
                            gasGntBean.setCap(gasCapBean);
                            gasGntBean.setIcon(R.mipmap.tokenneoxxhdpi + "");
                            gasBean.setGnt_category(gasGntBean);

                            NewNeoTokenListBean list = new NewNeoTokenListBean(walletCount.get(index).getName(), record.getGnt().get(0).getBalance(), walletCount.get(index).getId(), wallet.get(walletPosition.get(walletCount.get(index).getId())));
                            neoWallets.add(list);
                            gasBean.setWallets(neoWallets);
                            gasBean.setType(2);
                            neoGnt.put(record.getGnt().get(0).getCap().getName()+"(NEO)", gasBean);
                        }
                    }
                }

                if (null != response.body().data.getList()) {
                    if (response.body().data.getRecord().getCategory_id() == 2){
                        for (TokenBean.ListBean gnt : response.body().data.getList()) {
                            if (null != neoGnt.get(gnt.getName()+"(NEO)")) {
                                TokenBean.ListBean bfGnt = neoGnt.get(gnt.getName()+"(NEO)");
                                BigInteger price = new BigInteger(AppUtil.reverseArray(gnt.getBalance()));
                                BigDecimal currentPrice = new BigDecimal(price).divide(new BigDecimal(10).pow(Integer.parseInt(gnt.getDecimals() == null ? "0" : gnt.getDecimals())));

                                NewNeoTokenListBean list = new NewNeoTokenListBean(walletCount.get(index).getName(), currentPrice.toPlainString(), walletCount.get(index).getId(), wallet.get(walletPosition.get(walletCount.get(index).getId())));
                                bfGnt.getWallets().add(list);
                                gnt.setBalance(currentPrice.add(new BigDecimal(bfGnt.getBalance())).toPlainString());
                                gnt.setWallets(bfGnt.getWallets());
                                neoGnt.put(gnt.getName()+"(NEO)", gnt);
                            } else {
                                BigInteger price = new BigInteger(AppUtil.reverseArray(gnt.getBalance()));
                                BigDecimal currentPrice = new BigDecimal(price).divide(new BigDecimal(10).pow(Integer.parseInt(gnt.getDecimals() == null ? "0" : gnt.getDecimals())));

                                gnt.setBalance(currentPrice.toPlainString());
                                ArrayList<NewNeoTokenListBean> neoWallets = new ArrayList<>();
                                NewNeoTokenListBean list = new NewNeoTokenListBean(walletCount.get(index).getName(), currentPrice.toPlainString(), walletCount.get(index).getId(), wallet.get(walletPosition.get(walletCount.get(index).getId())));
                                neoWallets.add(list);
                                gnt.setWallets(neoWallets);
                                gnt.setType(2);
                                neoGnt.put(gnt.getName()+"(NEO)", gnt);
                            }
                        }
                    }else {
                        for (TokenBean.ListBean gnt : response.body().data.getList()) {
                            if (null != neoGnt.get(gnt.getName()+"(ETH)")) {
                                TokenBean.ListBean bfGnt = neoGnt.get(gnt.getName()+"(ETH)");
                                BigDecimal secPrice = new BigDecimal(AppUtil.toD(gnt.getBalance().replace("0x", "0"))).divide(AppUtil.decimal(gnt.getDecimals()));

                                gnt.setBalance(secPrice.add(new BigDecimal(bfGnt.getBalance())).toPlainString());
                                NewNeoTokenListBean list = new NewNeoTokenListBean(walletCount.get(index).getName(), secPrice.toPlainString(), walletCount.get(index).getId(), wallet.get(walletPosition.get(walletCount.get(index).getId())));
                                bfGnt.getWallets().add(list);
                                gnt.setWallets(bfGnt.getWallets());
                                gnt.setType(1);
                                neoGnt.put(gnt.getName()+"(ETH)", gnt);
                            } else {
                                BigDecimal currentPrice = new BigDecimal(AppUtil.toD(gnt.getBalance().replace("0x", "0"))).divide(AppUtil.decimal(gnt.getDecimals()));
                                gnt.setBalance(currentPrice.toPlainString());
                                ArrayList<NewNeoTokenListBean> neoWallets = new ArrayList<>();
                                NewNeoTokenListBean list = new NewNeoTokenListBean(walletCount.get(index).getName(), currentPrice.toPlainString(), walletCount.get(index).getId(), wallet.get(walletPosition.get(walletCount.get(index).getId())));
                                neoWallets.add(list);
                                gnt.setWallets(neoWallets);
                                gnt.setType(1);
                                neoGnt.put(gnt.getName()+"(ETH)", gnt);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                index++;
                if (index == walletCount.size()) {
                    //设置列表数据
                    setDataList();
                } else if (index < walletCount.size()) {
                    getTokenPrice();
                }
            }
        });
    }

    private void setDataList() {

        TotlePriceBean priceBean = new TotlePriceBean();
        BigDecimal ethCnyPrice = new BigDecimal("0.00");
        BigDecimal ethUsdPrice = new BigDecimal("0.00");
        BigDecimal neoCnyPrice = new BigDecimal("0.00");
        BigDecimal neoUsdPrice = new BigDecimal("0.00");
        BigDecimal totleCnyPrice = null;
        BigDecimal totleUsdDecimalPrice = null;

        //获取 neo 列表
        neoList.clear();
        Iterator neoIter = neoGnt.entrySet().iterator();
        boolean hasGas = false;
        while (neoIter.hasNext()) {
            Map.Entry entry = (Map.Entry) neoIter.next();
            TokenBean.ListBean val = (TokenBean.ListBean) entry.getValue();
            if (val.getName().equals("NEO")) {
                neoList.add(0, val);
                hasGas = true;
            } else if (val.getName().equals("ETH")) {
                if (!hasGas) {
                    neoList.add(0, val);
                } else {
                    neoList.add(1, val);
                }
            } else {
                neoList.add(val);
            }
            neoCnyPrice = neoCnyPrice.add(new BigDecimal(val.getBalance()).multiply(new BigDecimal(null == val.getGnt_category() ? "0.00" : null == val.getGnt_category().getCap() ? "0.00" : val.getGnt_category().getCap().getPrice_cny())));
            neoUsdPrice = neoUsdPrice.add(new BigDecimal(val.getBalance()).multiply(new BigDecimal(null == val.getGnt_category() ? "0.00" : null == val.getGnt_category().getCap() ? "0.00" : val.getGnt_category().getCap().getPrice_usd())));
        }

        header.notifyDataSetChanged();

        totleCnyPrice = ethCnyPrice.add(neoCnyPrice);
        totleUsdDecimalPrice = ethUsdPrice.add(neoUsdPrice);

        priceBean.ethCny = ethCnyPrice.setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
        priceBean.ethUsd = ethUsdPrice.setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
        priceBean.neoCny = neoCnyPrice.setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
        priceBean.neoUsd = neoUsdPrice.setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
        priceBean.totleCny = totleCnyPrice.setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
        priceBean.totleUsd = totleUsdDecimalPrice.setScale(2, BigDecimal.ROUND_DOWN).toPlainString();

        if (1 == App.get().getUnit()) {
            totleChPrice = (null == priceBean.totleCny ? "0.00" : priceBean.totleCny);
            changeSee(1, App.get().getSp().getBoolean(Constant.MAIN_SEE, true));
        } else {
            totleUsdPrice = (null == priceBean.totleUsd ? "0.00" : priceBean.totleUsd);
            changeSee(0, App.get().getSp().getBoolean(Constant.MAIN_SEE, true));
        }

        swipeRefresh.setRefreshing(false);

        CacheUtils.setCache((App.isMain ? Constant.TOTAL_PRICE : Constant.TOTAL_TEST_PRICE)+(null==App.get().getLoginBean()?"":App.get().getLoginBean().getEmail()),priceBean);

        CacheUtils.setCache((App.isMain ? Constant.NEO_LIST : Constant.NEO_TEST_LIST)+(null==App.get().getLoginBean()?"":App.get().getLoginBean().getEmail()),neoList);
        isLoadSuccess=true;
        isFirst=false;
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_WALLET || event.getEventCode() == Constant.EVENT_WALLET_DAIBI
                || event.getEventCode() == Constant.EVENT_UNIT_CHANGE) {
            needRefresh=true;
            loadData();
        }

        if (event.getEventCode() == Constant.EVENT_PASS_SEE) {
            changeSee(App.get().getUnit(), App.get().getSp().getBoolean(Constant.MAIN_SEE, true));
        }

        if (event.getEventCode() == Constant.EVENT_USERINFO) {
            setImg();
        }
    }

    private void changeSee(int unit, boolean isSee) {
        if (isSee) {
            if (unit == 1) {
                amount.setText("￥ " + totleChPrice);
                titlePrice.setText("￥ " + totleChPrice);
            } else {
                amount.setText("$ " + totleUsdPrice);
                titlePrice.setText("$ " + totleUsdPrice);
            }
            see.setImageResource(R.mipmap.openpassqianxxhdpi);
            topsee.setImageResource(R.mipmap.openpassxxhdpi);
        } else {
            if (unit == 1) {
                amount.setText("￥ ****");
                titlePrice.setText("￥ ****");
            } else {
                amount.setText("$ ****");
                titlePrice.setText("$ ****");
            }
            see.setImageResource(R.mipmap.closeseeqianxxhdpi);
            topsee.setImageResource(R.mipmap.closeseexxhdpi);
        }
    }


    @Override
    public void onDestroy() {
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
