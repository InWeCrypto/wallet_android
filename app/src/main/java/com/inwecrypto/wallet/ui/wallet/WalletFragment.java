package com.inwecrypto.wallet.ui.wallet;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.bean.TotlePriceBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.bean.WalletCountBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AnimUtil;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.common.widget.WrapContentHeightViewPager;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.MainTabActivity;
import com.inwecrypto.wallet.ui.me.adapter.CommonPagerAdapter;
import com.inwecrypto.wallet.ui.wallet.activity.MessageActivity;
import com.inwecrypto.wallet.ui.wallet.fragment.DaibiFragment;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/15.
 * 功能描述：
 * 版本：@version
 */

public class WalletFragment extends BaseFragment {


    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.tv_eth_price)
    TextView tvEthPrice;
    @BindView(R.id.ethLL)
    LinearLayout ethLL;
    @BindView(R.id.tv_neo_price)
    TextView tvNeoPrice;
    @BindView(R.id.neoRL)
    RelativeLayout neoRL;
    @BindView(R.id.lian_line)
    View lianLine;
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.titlePrice)
    TextView titlePrice;
    @BindView(R.id.titlell)
    LinearLayout titlell;
    @BindView(R.id.amin_title)
    TextView aminTitle;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.vp)
    WrapContentHeightViewPager vp;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayoutCompat swipeRefresh;

    private ArrayList<BaseFragment> fragments;
    private DaibiFragment ethFragment;
    private DaibiFragment neoFragment;
    private CommonPagerAdapter adapter;

    private BigDecimal ETHEther = new BigDecimal("0.00");
    private BigDecimal NEOEther = new BigDecimal("0.00");
    private String ethCnyPrice="0.00";
    private String ethUsdPrice="0.00";
    private String neoCnyPrice="0.00";
    private String neoUsdPrice="0.00";
    private HashMap<String, TokenBean.ListBean> ethGnt = new HashMap<>();
    private HashMap<String, TokenBean.ListBean> neoGnt = new HashMap<>();
    private ArrayList<WalletCountBean> walletCount=new ArrayList<>();

    private int lineWidth;
    private boolean isFinish;
    private int index;

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_fragment;
    }

    @Override
    protected void initView() {

        isOpenEventBus = true;

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
                    aminTitle.setVisibility(View.VISIBLE);
                    line.setVisibility(View.INVISIBLE);
                }
                if (verticalOffset + appBarLayout.getTotalScrollRange() == 0) {
                    titlell.setVisibility(View.VISIBLE);
                    aminTitle.setVisibility(View.INVISIBLE);
                    line.setVisibility(View.VISIBLE);
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
                ((MainTabActivity)mActivity).setRefresh(true);
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET));
            }
        });

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BaseEventBusBean(Constant.OPEN_CLOSE_MENU));
            }
        });
        Drawable drawable = getResources().getDrawable(R.mipmap.nav_menu);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        txtLeftTitle.setCompoundDrawables(drawable, null, null, null);

        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keepTogo(MessageActivity.class);
            }
        });
        Drawable drawableInfo = getResources().getDrawable(R.mipmap.nav_info);
        /// 这一步必须要做,否则不会显示.
        drawableInfo.setBounds(0, 0, drawableInfo.getMinimumWidth(), drawableInfo.getMinimumHeight());
        txtRightTitle.setCompoundDrawables(drawableInfo, null, null, null);

        lineWidth=(ScreenUtils.getScreenWidth(mActivity)- DensityUtil.dip2px(getContext(),58))/2;

        FrameLayout.LayoutParams params= (FrameLayout.LayoutParams) lianLine.getLayoutParams();
        params.width=lineWidth;
        lianLine.setLayoutParams(params);

        ethLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp.setCurrentItem(0);
                FrameLayout.LayoutParams params= (FrameLayout.LayoutParams) lianLine.getLayoutParams();
                params.leftMargin=0;
                lianLine.setLayoutParams(params);
            }
        });

        neoRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp.setCurrentItem(1);
                FrameLayout.LayoutParams params= (FrameLayout.LayoutParams) lianLine.getLayoutParams();
                params.leftMargin=lineWidth+DensityUtil.dip2px(getContext(),10);
                lianLine.setLayoutParams(params);
            }
        });

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                FrameLayout.LayoutParams params= (FrameLayout.LayoutParams) lianLine.getLayoutParams();
                switch (position) {
                    case 0:
                        params.leftMargin=0;
                        break;
                    case 1:
                        params.leftMargin=lineWidth+DensityUtil.dip2px(getContext(),10);
                        break;
                }
                lianLine.setLayoutParams(params);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ethFragment = new DaibiFragment();
        Bundle ethBundle = new Bundle();
        ethBundle.putString("type", "eth");
        //获取 eth 列表
        String eth = App.get().getSp().getString(App.isMain?Constant.ETH_LIST:Constant.ETH_TEST_LIST, "[]");
        ArrayList<TokenBean.ListBean> ethList = GsonUtils.jsonToArrayList(eth, TokenBean.ListBean.class);
        ethFragment.setArguments(ethBundle);
        ethFragment.setData(ethList);
        ethFragment.setOnSwipRefershCanUseListener(new DaibiFragment.SwipRefershCanUseListener() {
            @Override
            public void OnCanUse(boolean canUse) {
                swipeRefresh.setEnabled(canUse);
            }
        });

        neoFragment = new DaibiFragment();
        Bundle neoBundle = new Bundle();
        neoBundle.putString("type", "neo");
        //获取 neo 列表
        String neo = App.get().getSp().getString(App.isMain?Constant.NEO_LIST:Constant.NEO_TEST_LIST, "[]");
        ArrayList<TokenBean.ListBean> neoList = GsonUtils.jsonToArrayList(neo, TokenBean.ListBean.class);
        neoFragment.setArguments(neoBundle);
        neoFragment.setData(neoList);
        neoFragment.setOnSwipRefershCanUseListener(new DaibiFragment.SwipRefershCanUseListener() {
            @Override
            public void OnCanUse(boolean canUse) {
                swipeRefresh.setEnabled(canUse);
            }
        });

        fragments = new ArrayList<>();
        fragments.add(ethFragment);
        fragments.add(neoFragment);

        adapter = new CommonPagerAdapter(getChildFragmentManager(), fragments);
        vp.setAdapter(adapter);
    }

    @Override
    protected void loadData() {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
        //获取缓存数据
        getChace();
        //获取网络数据
        getInfoOnNet();
    }

    private void getChace() {
        //获取总资产
        String totlePrice = App.get().getSp().getString(App.isMain?Constant.TOTAL_PRICE:Constant.TOTAL_TEST_PRICE, "{}");
        TotlePriceBean priceBean = GsonUtils.jsonToObj(totlePrice, TotlePriceBean.class);

        if (1 == App.get().getUnit()) {
            tvEthPrice.setText("≈￥" + (null==priceBean.ethCny?"0.00":priceBean.ethCny));
            tvNeoPrice.setText("≈￥"+(null==priceBean.neoCny?"0.00":priceBean.neoCny));
            price.setText("￥" +  (null==priceBean.totleCny?"0.00":priceBean.totleCny));
            titlePrice.setText("(≈￥" + (null==priceBean.totleCny?"0.00":priceBean.totleCny)+ ")");
        } else {
            tvEthPrice.setText("≈$" + (null==priceBean.ethUsd?"0.00":priceBean.ethUsd));
            tvNeoPrice.setText("≈$"+(null==priceBean.neoUsd?"0.00":priceBean.neoUsd));
            price.setText("$" + (null==priceBean.totleUsd?"0.00":priceBean.totleUsd));
            titlePrice.setText("(≈$" + (null==priceBean.totleUsd?"0.00":priceBean.totleUsd) + ")");
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_MAIN_REFERSH_COMP) {
            swipeRefresh.setRefreshing(false);
        }

        if (event.getEventCode() == Constant.EVENT_WALLET_DAIBI
                || event.getEventCode() == Constant.EVENT_UNIT_CHANGE) {
            getInfoOnNet();
        }

        if (event.getEventCode() == Constant.EVENT_TOTLE_PRICE_SERVICE){
            if (null!=neoFragment&&null!=ethFragment){
                String eth = App.get().getSp().getString(App.isMain?Constant.ETH_LIST:Constant.ETH_TEST_LIST, "[]");
                ArrayList<TokenBean.ListBean> ethList = GsonUtils.jsonToArrayList(eth, TokenBean.ListBean.class);
                ethFragment.setData(ethList);
                String neo = App.get().getSp().getString(App.isMain?Constant.NEO_LIST:Constant.NEO_TEST_LIST, "[]");
                ArrayList<TokenBean.ListBean> neoList = GsonUtils.jsonToArrayList(neo, TokenBean.ListBean.class);
                neoFragment.setData(neoList);
                getChace();
            }
        }
    }

    private void getInfoOnNet() {
        if (!((MainTabActivity)mActivity).isInit()){
            return;
        }
        if (((MainTabActivity)mActivity).getWallet().size()==0){
                if (1 == App.get().getUnit()) {
                    tvEthPrice.setText("≈￥0.00");
                    tvNeoPrice.setText("≈￥0.00");
                    price.setText("￥0.00");
                    titlePrice.setText("(≈￥0.00)");
                } else {
                    tvEthPrice.setText("≈$0.00" );
                    tvNeoPrice.setText("≈$0.00");
                    price.setText("$0.00" );
                    titlePrice.setText("(≈$0.00)");
                }

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
        for (WalletBean wa : ((MainTabActivity)mActivity).getWallet()) {
                sb.append(wa.getId() + ",");
            }
        if(sb.length()!=1){
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
                    ethGnt.clear();
                    neoGnt.clear();

                    walletCount = response.body().data.getList();
                    boolean hasEth=false;
                    boolean hasNeo=false;
                    //计算ethPrice
                    for (WalletCountBean count : walletCount) {
                        //计算 eth 价格
                        if (count.getCategory_id()==1&&null != count.getBalance()) {
                            //进行计算
                            BigDecimal currentPrice = new BigDecimal(AppUtil.toD(count.getBalance().replace("0x", "0")));
                            ETHEther = ETHEther.add(currentPrice.divide(Constant.pEther));
                            ethCnyPrice=count.getCategory().getCap().getPrice_cny();
                            ethUsdPrice = count.getCategory().getCap().getPrice_usd();
                            hasEth=true;
                        }else if (count.getCategory_id()==2&&null != count.getBalance()){
                            BigDecimal currentPrice = new BigDecimal(count.getBalance());
                            NEOEther = NEOEther.add(currentPrice);
                            neoCnyPrice = count.getCategory().getCap().getPrice_cny();
                            neoUsdPrice = count.getCategory().getCap().getPrice_usd();
                            hasNeo=true;
                        }
                    }

                    //添加 eth
                    TokenBean.ListBean eth=new TokenBean.ListBean();
                    TokenBean.ListBean.GntCategoryBeanX ethCategory=new TokenBean.ListBean.GntCategoryBeanX();
                    TokenBean.ListBean.GntCategoryBeanX.CapBeanX ethCap=new TokenBean.ListBean.GntCategoryBeanX.CapBeanX();
                    eth.setName("ETH");
                    eth.setBalance(ETHEther.toPlainString());
                    ethCategory.setIcon(R.mipmap.eth+"");
                    ethCap.setPrice_cny(ethCnyPrice);
                    ethCap.setPrice_usd(ethUsdPrice);
                    ethCategory.setCap(ethCap);
                    eth.setGnt_category(ethCategory);
                    ethGnt.put("ETH",eth);

                    //添加 neo
                    TokenBean.ListBean neo=new TokenBean.ListBean();
                    TokenBean.ListBean.GntCategoryBeanX neoCategory=new TokenBean.ListBean.GntCategoryBeanX();
                    TokenBean.ListBean.GntCategoryBeanX.CapBeanX neoCap=new TokenBean.ListBean.GntCategoryBeanX.CapBeanX();
                    neo.setName("NEO");
                    neo.setBalance(NEOEther.toPlainString());
                    neoCategory.setIcon(R.mipmap.project_icon_neo+"");
                    neoCap.setPrice_cny(neoCnyPrice);
                    neoCap.setPrice_usd(neoUsdPrice);
                    neoCategory.setCap(neoCap);
                    neo.setGnt_category(neoCategory);
                    neoGnt.put("NEO",neo);

                    if (!hasNeo){
                        //添加 gas
                        TokenBean.ListBean gas=new TokenBean.ListBean();
                        TokenBean.ListBean.GntCategoryBeanX gasCategory=new TokenBean.ListBean.GntCategoryBeanX();
                        TokenBean.ListBean.GntCategoryBeanX.CapBeanX gasCap=new TokenBean.ListBean.GntCategoryBeanX.CapBeanX();
                        gas.setName("Gas");
                        gas.setBalance("0.0000");
                        gasCategory.setIcon(R.mipmap.project_icon_get_gas+"");
                        gasCap.setPrice_cny("0.00");
                        gasCap.setPrice_usd("0.00");
                        gasCategory.setCap(neoCap);
                        gas.setGnt_category(gasCategory);
                        neoGnt.put("Gas",gas);
                    }

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
                ToastUtil.show("加载失败");
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void getTokenPrice() {
        //请求代币列表
        WalletApi.conversionErrorCache(this, walletCount.get(index).getId(), new JsonCallback<LzyResponse<TokenBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<TokenBean>> response) {
                if (null!=response.body().data.getRecord()){
                    if (response.body().data.getRecord().getCategory_id()==2){
                        TokenBean.RecordBean record = response.body().data.getRecord();

                        if (null != neoGnt.get(record.getGnt().get(0).getCap().getName())) {
                            TokenBean.ListBean bfGnt = neoGnt.get(record.getGnt().get(0).getCap().getName());
                            BigDecimal secPrice = new BigDecimal(record.getGnt().get(0).getBalance());

                            bfGnt.setBalance(secPrice.add(new BigDecimal(bfGnt.getBalance())).toString());
                            neoGnt.put(record.getGnt().get(0).getCap().getName(), bfGnt);
                        } else {
                            TokenBean.ListBean gasBean=new TokenBean.ListBean();
                            TokenBean.ListBean.GntCategoryBeanX gasGntBean=new TokenBean.ListBean.GntCategoryBeanX();
                            TokenBean.ListBean.GntCategoryBeanX.CapBeanX gasCapBean=new TokenBean.ListBean.GntCategoryBeanX.CapBeanX();
                            gasBean.setName(record.getGnt().get(0).getCap().getName());
                            gasBean.setBalance(new BigDecimal(record.getGnt().get(0).getBalance()).toPlainString());
                            gasCapBean.setPrice_cny(record.getGnt().get(0).getCap().getPrice_cny());
                            gasCapBean.setPrice_usd(record.getGnt().get(0).getCap().getPrice_usd());
                            gasGntBean.setCap(gasCapBean);
                            gasGntBean.setIcon(R.mipmap.project_icon_gas+"");
                            gasBean.setGnt_category(gasGntBean);
                            neoGnt.put(record.getGnt().get(0).getCap().getName(), gasBean);
                        }
                    }
                }
                if (null != response.body().data.getList()) {
                    for (TokenBean.ListBean gnt : response.body().data.getList()) {
                        if (null != ethGnt.get(gnt.getName())) {
                            TokenBean.ListBean bfGnt = ethGnt.get(gnt.getName());
                            BigDecimal secPrice = new BigDecimal(AppUtil.toD(gnt.getBalance().replace("0x", "0")));
                            gnt.setBalance(secPrice.divide(Constant.pEther).add(new BigDecimal(bfGnt.getBalance())).toString());
                            ethGnt.put(gnt.getName(), gnt);
                        } else {
                            BigDecimal currentPrice = new BigDecimal(AppUtil.toD(gnt.getBalance().replace("0x", "0")));
                            gnt.setBalance(currentPrice.divide(Constant.pEther).toString());
                            ethGnt.put(gnt.getName(), gnt);
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
        BigDecimal totleUsdPrice = null;


        //获取 eth 列表
        ArrayList<TokenBean.ListBean> ethList = new ArrayList<>();
        Iterator ethIter = ethGnt.entrySet().iterator();
        while (ethIter.hasNext()) {
            Map.Entry entry = (Map.Entry) ethIter.next();
            TokenBean.ListBean val = (TokenBean.ListBean) entry.getValue();
            if (val.getName().equals("ETH")) {
                ethList.add(0, val);
            } else {
                ethList.add(val);
            }
            ethCnyPrice=ethCnyPrice.add(new BigDecimal(val.getBalance()).multiply(new BigDecimal(null==val.getGnt_category()?"0.00":null==val.getGnt_category().getCap()?"0.00":val.getGnt_category().getCap().getPrice_cny())));
            ethUsdPrice=ethUsdPrice.add(new BigDecimal(val.getBalance()).multiply(new BigDecimal(null==val.getGnt_category()?"0.00":null==val.getGnt_category().getCap()?"0.00":val.getGnt_category().getCap().getPrice_usd())));
        }
        ethFragment.setData(ethList);

        //获取 neo 列表
        ArrayList<TokenBean.ListBean> neoList = new ArrayList<>();
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
        neoFragment.setData(neoList);


        totleCnyPrice = ethCnyPrice.add(neoCnyPrice);
        totleUsdPrice = ethUsdPrice.add(neoUsdPrice);

        priceBean.ethCny = ethCnyPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
        priceBean.ethUsd = ethUsdPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
        priceBean.neoCny = neoCnyPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
        priceBean.neoUsd = neoUsdPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
        priceBean.totleCny = totleCnyPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
        priceBean.totleUsd = totleUsdPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();

        if (1 == App.get().getUnit()) {
            tvEthPrice.setText("≈￥" + (null == priceBean.ethCny ? "0.00" : priceBean.ethCny));
            tvNeoPrice.setText("≈￥" + (null == priceBean.neoCny ? "0.00" : priceBean.neoCny));
            price.setText("￥" + (null == priceBean.totleCny ? "0.00" : priceBean.totleCny));
            titlePrice.setText("(≈￥" + (null == priceBean.totleCny ? "0.00" : priceBean.totleCny) + ")");
        } else {
            tvEthPrice.setText("≈$" + (null == priceBean.ethUsd ? "0.00" : priceBean.ethUsd));
            tvNeoPrice.setText("≈$" + (null == priceBean.neoUsd ? "0.00" : priceBean.neoUsd));
            price.setText("$" + (null == priceBean.totleUsd ? "0.00" : priceBean.totleUsd));
            titlePrice.setText("(≈$" + (null == priceBean.totleUsd ? "0.00" : priceBean.totleUsd) + ")");
        }

        swipeRefresh.setRefreshing(false);

        //设置总资产
        App.get().getSp().putString(App.isMain ? Constant.TOTAL_PRICE : Constant.TOTAL_TEST_PRICE,  GsonUtils.objToJson(priceBean));
        //设置缓存列表
        //设置 eth 列表
        App.get().getSp().putString(App.isMain ? Constant.ETH_LIST : Constant.ETH_TEST_LIST, GsonUtils.objToJson(ethList));
        //设置 neo 列表
        App.get().getSp().putString(App.isMain ? Constant.NEO_LIST : Constant.NEO_TEST_LIST, GsonUtils.objToJson(neoList));
    }
}
