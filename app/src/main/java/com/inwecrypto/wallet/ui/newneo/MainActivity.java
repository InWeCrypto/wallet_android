package com.inwecrypto.wallet.ui.newneo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.LoginBean;
import com.inwecrypto.wallet.bean.NewNeoTokenListBean;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.bean.TotlePriceBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.bean.WalletCountBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.imageloader.GlideCircleTransform;
import com.inwecrypto.wallet.common.util.AnimUtil;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.BetterRecyclerView;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.me.MeActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/1/8 11:01
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class MainActivity extends BaseActivity {

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
    private HashMap<Integer,Integer> walletPosition=new HashMap<>();
    private ArrayList<WalletCountBean> walletCount=new ArrayList<>();
    private ArrayList<TokenBean.ListBean> neoList = new ArrayList<>();
    private BigDecimal NEOEther = new BigDecimal("0.00");
    private String neoCnyPrice="0.00";
    private String neoUsdPrice="0.00";
    private int index;

    private NewNeoMainTokenAdapter adapter;
    private boolean isSee=true;

    private String totleChPrice="0.00";
    private String totleUsdPrice="0.00";

    @Override
    protected void getBundleExtras(Bundle extras) {
        isOpenEventBus = true;
    }

    @Override
    protected int setLayoutID() {
        return R.layout.newneo_main_activity;
    }

    @Override
    protected void initView() {

        walletManagerBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,NewNeoWalletListActivity.class);
                intent.putExtra("wallet",wallet);
                keepTogo(intent);
            }
        });

        topWalletManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,NewNeoWalletListActivity.class);
                intent.putExtra("wallet",wallet);
                keepTogo(intent);
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,MeActivity.class);
                keepTogo(intent);
            }
        });

        topUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,MeActivity.class);
                keepTogo(intent);
            }
        });

        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSee=App.get().getSp().getBoolean(Constant.MAIN_SEE,true);
                App.get().getSp().putBoolean(Constant.MAIN_SEE,!isSee);
                changeSee(App.get().getUnit(),!isSee);
            }
        });

        topsee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSee=App.get().getSp().getBoolean(Constant.MAIN_SEE,true);
                App.get().getSp().putBoolean(Constant.MAIN_SEE,!isSee);
                changeSee(App.get().getUnit(),!isSee);
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
                //从网络获取钱包
                getInfoOnNet();
            }
        });

        adapter=new NewNeoMainTokenAdapter(this,R.layout.newneo_wallet_item,neoList);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (wallet.size()==0){
                    ToastUtil.show(getString(R.string.zanwuqianbao));
                    return;
                }
                //弹出钱包选择框
                FragmentManager fm = getSupportFragmentManager();
                WalletListFragment walletlist = new WalletListFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("wallets",neoList.get(position).getWallets());
                walletlist.setArguments(bundle);
                walletlist.show(fm,"list");

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        setImg();
        //获取缓存数据
        getChace();
        //从网络获取钱包
        getInfoOnNet();
    }

    private void setImg() {
        LoginBean loginBean = App.get().getLoginBean();
        if (null != loginBean) {
            if (null != loginBean.getUser().getImg() && loginBean.getUser().getImg().length() > 0) {
                Glide.with(this)
                        .load(loginBean.getUser().getImg())
                        .crossFade()
                        .placeholder(R.mipmap.touxiang)
                        .transform(new GlideCircleTransform(this))
                        .into(topUser);
                Glide.with(this)
                        .load(loginBean.getUser().getImg())
                        .crossFade()
                        .placeholder(R.mipmap.touxiang)
                        .transform(new GlideCircleTransform(this))
                        .into(userImg);
            }else{
                Glide.with(this)
                        .load(R.mipmap.touxiang)
                        .crossFade()
                        .transform(new GlideCircleTransform(this))
                        .into(topUser);
                Glide.with(this)
                        .load(R.mipmap.touxiang)
                        .crossFade()
                        .transform(new GlideCircleTransform(this))
                        .into(userImg);
            }
        }
    }

    private void getChace() {
        //获取总资产
        String totlePrice = App.get().getSp().getString(App.isMain?Constant.TOTAL_PRICE:Constant.TOTAL_TEST_PRICE, "{}");
        TotlePriceBean priceBean = GsonUtils.jsonToObj(totlePrice, TotlePriceBean.class);

        if (1 == App.get().getUnit()) {
            totleChPrice=(null==priceBean.totleCny?"0.00":priceBean.totleCny);
            changeSee(1,App.get().getSp().getBoolean(Constant.MAIN_SEE,true));
        } else {
            totleUsdPrice=(null==priceBean.totleUsd?"0.00":priceBean.totleUsd);
            changeSee(0,App.get().getSp().getBoolean(Constant.MAIN_SEE,true));
        }

        //设置缓存列表
        String totleneoGnt = App.get().getSp().getString(App.isMain ? Constant.NEO_LIST : Constant.NEO_TEST_LIST, "[]");
        ArrayList<TokenBean.ListBean> neoChaceList = GsonUtils.jsonToArrayList(totleneoGnt, TokenBean.ListBean.class);
        neoList.clear();
        neoList.addAll(neoChaceList);
        adapter.notifyDataSetChanged();
    }

    private void getInfoOnNet() {
        if (null==swipeRefresh){
            return;
        }
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
        WalletApi.wallet(mActivity, new JsonCallback<LzyResponse<CommonListBean<WalletBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                wallet.clear();
                if (null != response.body().data.getList()) {
                    walletPosition.clear();
                    String wallets = App.get().getSp().getString(Constant.WALLETS, "");
                    String wallets_beifen = App.get().getSp().getString(Constant.WALLETS_BEIFEN, "");
                    String walletsZjc = App.get().getSp().getString(Constant.WALLETS_ZJC_BEIFEN, "");
                    for (int i = 0; i < response.body().data.getList().size(); i++) {
                        if (response.body().data.getList().get(i).getCategory_id()==2){
                            if (wallets.contains(response.body().data.getList().get(i).getAddress())) {
                                if (wallets_beifen.contains(response.body().data.getList().get(i).getAddress()) || walletsZjc.contains(response.body().data.getList().get(i).getAddress())) {
                                    response.body().data.getList().get(i).setType(Constant.BEIFEN);
                                } else {
                                    response.body().data.getList().get(i).setType(Constant.ZHENGCHANG);
                                }
                            } else {
                                response.body().data.getList().get(i).setType(Constant.GUANCHA);
                            }
                            walletPosition.put(response.body().data.getList().get(i).getId(),wallet.size());
                            wallet.add(response.body().data.getList().get(i));
                        }
                    }
                }
                if (!response.isFromCache()) {
                    //获取代币列表
                    getTokenList();
                }
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.jiazaishibai));
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void getTokenList() {

        if (wallet.size()==0){
            if (1 == App.get().getUnit()) {
                totleChPrice="0.00";
                changeSee(1,App.get().getSp().getBoolean(Constant.MAIN_SEE,true));
            } else {
                totleUsdPrice="0.00";
                changeSee(0,App.get().getSp().getBoolean(Constant.MAIN_SEE,true));
            }

            neoGnt.clear();
            //添加 neo
            TokenBean.ListBean neo=new TokenBean.ListBean();
            TokenBean.ListBean.GntCategoryBeanX neoCategory=new TokenBean.ListBean.GntCategoryBeanX();
            neo.setName("NEO");
            neo.setBalance("0.0000");
            neoCategory.setIcon(R.mipmap.tokenneoxxhdpi+"");
            neo.setGnt_category(neoCategory);
            neoGnt.put("NEO",neo);

            TokenBean.ListBean gas=new TokenBean.ListBean();
            TokenBean.ListBean.GntCategoryBeanX gasCategory=new TokenBean.ListBean.GntCategoryBeanX();
            gas.setName("Gas");
            gas.setBalance("0.0000");
            gasCategory.setIcon(R.mipmap.tokenneoxxhdpi+"");
            gas.setGnt_category(gasCategory);
            neoGnt.put("Gas",gas);

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
        if(sb.length()!=1){
            sb.delete(sb.length() - 1, sb.length());
        }
        sb.append("]");
        WalletApi.conversion(this, sb.toString(), new JsonCallback<LzyResponse<CommonListBean<WalletCountBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<WalletCountBean>>> response) {
                NEOEther = NEOEther.multiply(new BigDecimal(0));
                neoCnyPrice="0.00";
                neoUsdPrice="0.00";
                neoGnt.clear();
                walletCount.clear();
                walletCount.addAll(response.body().data.getList());
                ArrayList<NewNeoTokenListBean> neoWallets=new ArrayList<>();
                //计算ethPrice
                for (WalletCountBean count : walletCount) {
                    if (null != count.getBalance()){
                        BigDecimal currentPrice = new BigDecimal(count.getBalance());
                        NEOEther = NEOEther.add(currentPrice);
                        neoCnyPrice = count.getCategory().getCap().getPrice_cny();
                        neoUsdPrice = count.getCategory().getCap().getPrice_usd();

                        NewNeoTokenListBean list=new NewNeoTokenListBean(count.getName(),count.getBalance(),count.getId(),wallet.get(walletPosition.get(count.getId())));
                        neoWallets.add(list);
                    }
                }

                //添加 neo
                TokenBean.ListBean neo=new TokenBean.ListBean();
                TokenBean.ListBean.GntCategoryBeanX neoCategory=new TokenBean.ListBean.GntCategoryBeanX();
                TokenBean.ListBean.GntCategoryBeanX.CapBeanX neoCap=new TokenBean.ListBean.GntCategoryBeanX.CapBeanX();
                neo.setName("NEO");
                neo.setBalance(NEOEther.toPlainString());
                neoCategory.setIcon(R.mipmap.tokenneoxxhdpi+"");
                neoCap.setPrice_cny(neoCnyPrice);
                neoCap.setPrice_usd(neoUsdPrice);
                neoCategory.setCap(neoCap);
                neo.setGnt_category(neoCategory);
                neo.setWallets(neoWallets);
                neoGnt.put("NEO",neo);

                index=0;
                if (walletCount.size()>0){
                    getTokenPrice();
                }else {
                    swipeRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<WalletCountBean>>> response) {
                super.onError(response);
                ToastUtil.show(R.string.jiazaishibai);
                swipeRefresh.setRefreshing(false);
            }
        });

    }

    private void getTokenPrice() {
        //请求代币列表
        WalletApi.conversionErrorCache(this, walletCount.get(index).getId(), new JsonCallback<LzyResponse<TokenBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<TokenBean>> response) {
                if (index>=walletCount.size()){
                    return;
                }
                if (null!=response.body().data.getRecord()){
                    if (response.body().data.getRecord().getCategory_id()==2){
                        TokenBean.RecordBean record = response.body().data.getRecord();

                        if (null != neoGnt.get(record.getGnt().get(0).getCap().getName())) {
                            TokenBean.ListBean bfGnt = neoGnt.get(record.getGnt().get(0).getCap().getName());
                            BigDecimal secPrice = new BigDecimal(record.getGnt().get(0).getBalance());

                            bfGnt.setBalance(secPrice.add(new BigDecimal(bfGnt.getBalance())).toString());
                            NewNeoTokenListBean list=new NewNeoTokenListBean(walletCount.get(index).getName(),record.getGnt().get(0).getBalance(),walletCount.get(index).getId(),wallet.get(walletPosition.get(walletCount.get(index).getId())));
                            bfGnt.getWallets().add(list);
                            neoGnt.put(record.getGnt().get(0).getCap().getName(), bfGnt);
                        } else {
                            ArrayList<NewNeoTokenListBean> neoWallets=new ArrayList<>();
                            TokenBean.ListBean gasBean=new TokenBean.ListBean();
                            TokenBean.ListBean.GntCategoryBeanX gasGntBean=new TokenBean.ListBean.GntCategoryBeanX();
                            TokenBean.ListBean.GntCategoryBeanX.CapBeanX gasCapBean=new TokenBean.ListBean.GntCategoryBeanX.CapBeanX();
                            gasBean.setName(record.getGnt().get(0).getCap().getName());
                            gasBean.setBalance(new BigDecimal(record.getGnt().get(0).getBalance()).toPlainString());
                            gasCapBean.setPrice_cny(record.getGnt().get(0).getCap().getPrice_cny());
                            gasCapBean.setPrice_usd(record.getGnt().get(0).getCap().getPrice_usd());
                            gasGntBean.setCap(gasCapBean);
                            gasGntBean.setIcon(R.mipmap.tokenneoxxhdpi+"");
                            gasBean.setGnt_category(gasGntBean);

                            NewNeoTokenListBean list=new NewNeoTokenListBean(walletCount.get(index).getName(),record.getGnt().get(0).getBalance(),walletCount.get(index).getId(),wallet.get(walletPosition.get(walletCount.get(index).getId())));
                            neoWallets.add(list);
                            gasBean.setWallets(neoWallets);
                            neoGnt.put(record.getGnt().get(0).getCap().getName(), gasBean);
                        }
                    }
                }

                if (null != response.body().data.getList()) {
                    for (TokenBean.ListBean gnt : response.body().data.getList()) {
                        if (null != neoGnt.get(gnt.getName())) {
                            TokenBean.ListBean bfGnt = neoGnt.get(gnt.getName());
                            BigInteger price=new BigInteger(AppUtil.reverseArray(gnt.getBalance()));
                            BigDecimal currentPrice = new BigDecimal(price).divide(new BigDecimal(10).pow(Integer.parseInt(gnt.getDecimals()==null?"0":gnt.getDecimals())));

                            NewNeoTokenListBean list=new NewNeoTokenListBean(walletCount.get(index).getName(),currentPrice.toPlainString(),walletCount.get(index).getId(),wallet.get(walletPosition.get(walletCount.get(index).getId())));
                            bfGnt.getWallets().add(list);
                            gnt.setBalance(currentPrice.add(new BigDecimal(bfGnt.getBalance())).toString());
                            gnt.setWallets(bfGnt.getWallets());
                            neoGnt.put(gnt.getName(), gnt);
                        } else {
                            BigInteger price=new BigInteger(AppUtil.reverseArray(gnt.getBalance()));
                            BigDecimal currentPrice = new BigDecimal(price).divide(new BigDecimal(10).pow(Integer.parseInt(gnt.getDecimals()==null?"0":gnt.getDecimals())));

                            gnt.setBalance(currentPrice.toString());
                            ArrayList<NewNeoTokenListBean> neoWallets=new ArrayList<>();
                            NewNeoTokenListBean list=new NewNeoTokenListBean(walletCount.get(index).getName(),currentPrice.toPlainString(),walletCount.get(index).getId(),wallet.get(walletPosition.get(walletCount.get(index).getId())));
                            neoWallets.add(list);
                            gnt.setWallets(neoWallets);
                            neoGnt.put(gnt.getName(), gnt);
                        }
                    }
                }
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<TokenBean>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                index++;
                if (index==walletCount.size()){
                    //设置列表数据
                    setDataList();
                }else if (index<walletCount.size()){
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
            } else if (val.getName().equals("Gas")) {
                if (!hasGas) {
                    neoList.add(0, val);
                } else {
                    neoList.add(1, val);
                }
            } else {
                neoList.add(val);
            }
            neoCnyPrice=neoCnyPrice.add(new BigDecimal(val.getBalance()).multiply(new BigDecimal(null==val.getGnt_category()?"0.00":null==val.getGnt_category().getCap()?"0.00":val.getGnt_category().getCap().getPrice_cny())));
            neoUsdPrice=neoUsdPrice.add(new BigDecimal(val.getBalance()).multiply(new BigDecimal(null==val.getGnt_category()?"0.00":null==val.getGnt_category().getCap()?"0.00":val.getGnt_category().getCap().getPrice_usd())));
        }

        adapter.notifyDataSetChanged();

        totleCnyPrice = ethCnyPrice.add(neoCnyPrice);
        totleUsdDecimalPrice = ethUsdPrice.add(neoUsdPrice);

        priceBean.ethCny = ethCnyPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
        priceBean.ethUsd = ethUsdPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
        priceBean.neoCny = neoCnyPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
        priceBean.neoUsd = neoUsdPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
        priceBean.totleCny = totleCnyPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
        priceBean.totleUsd = totleUsdDecimalPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();

        if (1 == App.get().getUnit()) {
            totleChPrice=(null == priceBean.totleCny ? "0.00" : priceBean.totleCny);
            changeSee(1,App.get().getSp().getBoolean(Constant.MAIN_SEE,true));
        } else {
            totleUsdPrice=(null == priceBean.totleUsd ? "0.00" : priceBean.totleUsd);
            changeSee(0,App.get().getSp().getBoolean(Constant.MAIN_SEE,true));
        }

        swipeRefresh.setRefreshing(false);

        //设置总资产
        App.get().getSp().putString(App.isMain ? Constant.TOTAL_PRICE : Constant.TOTAL_TEST_PRICE,  GsonUtils.objToJson(priceBean));

        //设置缓存列表
        //设置 neo 列表
        App.get().getSp().putString(App.isMain ? Constant.NEO_LIST : Constant.NEO_TEST_LIST, GsonUtils.objToJson(neoList));
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_WALLET||event.getEventCode() == Constant.EVENT_WALLET_DAIBI
                || event.getEventCode() == Constant.EVENT_UNIT_CHANGE) {
            OkGo.getInstance().cancelTag(this);
            getInfoOnNet();
        }

        if (event.getEventCode() ==Constant.EVENT_PASS_SEE){
            changeSee(App.get().getUnit(),App.get().getSp().getBoolean(Constant.MAIN_SEE,true));
        }

        if (event.getEventCode() == Constant.EVENT_USERINFO) {
            setImg();
        }
    }

    private void changeSee(int unit,boolean isSee) {
       if (isSee){
           if (unit==1){
               amount.setText("￥ " +  totleChPrice);
               titlePrice.setText("￥ " + totleChPrice);
           }else {
               amount.setText("$ " + totleUsdPrice);
               titlePrice.setText("$ " + totleUsdPrice);
           }
           see.setImageResource(R.mipmap.openpassqianxxhdpi);
           topsee.setImageResource(R.mipmap.openpassxxhdpi);
       }else {
           if (unit==1){
               amount.setText("￥ ****");
               titlePrice.setText("￥ ****");
           }else {
               amount.setText("$ ****");
               titlePrice.setText("$ ****");
           }
           see.setImageResource(R.mipmap.closeseeqianxxhdpi);
           topsee.setImageResource(R.mipmap.closeseexxhdpi);
       }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!App.isMain){
            addWaringView();
        }else {
            clearView();
        }
        initData();
    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //1.点击返回键条件成立
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {

            //2.点击的时间差如果大于2000，则提示用户点击两次退出
            if (System.currentTimeMillis() - mExitTime > 2000) {
                //3.保存当前时间
                mExitTime = System.currentTimeMillis();
                //4.提示
                ToastUtil.show(getString(R.string.tuichuapp));
            } else {
                //5.点击的时间差小于2000，退出。
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
