package com.inwecrypto.wallet.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.TotlePriceBean;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.LocalTokenBean;
import com.inwecrypto.wallet.bean.LocalWalletBean;
import com.inwecrypto.wallet.bean.LocalWalletCountBean;
import com.inwecrypto.wallet.bean.MarkeListBean;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.bean.WalletCountBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.LogUtils;
import com.inwecrypto.wallet.event.BaseEventBusBean;

/**
 * Created by Administrator on 2017/8/20.
 * 功能描述：
 * 版本：@version
 */

public class AutoUpdateService extends IntentService {
    private BigDecimal ETHEther = new BigDecimal("0.00");
    private BigDecimal NEOEther = new BigDecimal("0.00");
    private String ethCnyPrice="0.00";
    private String ethUsdPrice="0.00";
    private String neoCnyPrice="0.00";
    private String neoUsdPrice="0.00";

    private HashMap<String, TokenBean.ListBean> ethGnt = new HashMap<>();
    private HashMap<String, TokenBean.ListBean> neoGnt = new HashMap<>();

    private boolean isFirst=true;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AutoUpdateService(String name) {
        super(name);
    }

    public AutoUpdateService(){
        super("wallet service");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        update();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (App.IS_FIRST){
            App.IS_FIRST=false;
            return super.onStartCommand(intent, flags, startId);
        }
        if (App.UPDATA_TYPE==-1){
            App.UPDATA_TYPE=1;
        }else if (App.UPDATA_TYPE==1){
            App.UPDATA_TYPE=2;
        }else {
            App.UPDATA_TYPE=1;
        }
        OkGo.getInstance().cancelTag(this);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtMillis = SystemClock.elapsedRealtime() + 30 * 1000;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pendingIntent);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis, pendingIntent);

        return super.onStartCommand(intent, flags, startId);
    }

    private void update() {
        if (isFirst){
            isFirst=false;
            return;
        }
        if (App.UPDATA_TYPE==1){
            try {
                okhttp3.Response response = OkGo.<LzyResponse<CommonListBean<MarkeListBean>>>get(Url.MARKET_CATEGORY)
                        .headers("ct", App.get().getSp().getString(App.isMain?Constant.TOKEN:Constant.TEST_TOKEN,""))
                        .tag(this)
                        .cacheKey(Constant.MARKET+ App.isMain)
                        .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                        .execute();
                if (response.code()==200){
                    String body=response.body().string().toString();
                    if (body.contains("4000")){
                        EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_MARKET));
                    };
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                okhttp3.Response response = OkGo.<LzyResponse<CommonListBean<WalletBean>>>get(Url.WALLET)
                        .headers("ct", App.get().getSp().getString(App.isMain?Constant.TOKEN:Constant.TEST_TOKEN,""))
                        .tag(this)
                        .cacheKey(Constant.WALLETS+ App.isMain)
                        .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                        .execute();
                if (response.code()==200) {
                    String body = response.body().string().toString();
                    if (body.contains("4000")) {
                        LocalWalletBean localMarketBean = GsonUtils.jsonToObj(body, LocalWalletBean.class);
                        if (localMarketBean.data.getList().size()==0){
                            return;
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("[");
                        for (WalletBean wa : localMarketBean.data.getList()) {
                            sb.append(wa.getId() + ",");
                        }
                        if(sb.length()!=1){
                            sb.delete(sb.length() - 1, sb.length());
                        }
                        sb.append("]");
                        HashMap<String, String> params = new HashMap<>();
                        params.put("wallet_ids", sb.toString());

                        okhttp3.Response priceResponse = OkGo.<LzyResponse<CommonListBean<WalletCountBean>>>get(Url.CONVERSION)
                                .tag(this)
                                .cacheKey(Constant.CONVERSION+ App.isMain)
                                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                                .headers("ct", App.get().getSp().getString(App.isMain?Constant.TOKEN:Constant.TEST_TOKEN,""))
                                .params(params)
                                .execute();

                        if (priceResponse.code() == 200) {
                            String priceBody = priceResponse.body().string().toString();
                            if (priceBody.contains("4000")) {
                                LocalWalletCountBean walletCount = GsonUtils.jsonToObj(priceBody, LocalWalletCountBean.class);
                                ETHEther = ETHEther.multiply(new BigDecimal(0));
                                NEOEther = NEOEther.multiply(new BigDecimal(0));
                                ethCnyPrice="0.00";
                                ethUsdPrice="0.00";
                                neoCnyPrice="0.00";
                                neoUsdPrice="0.00";

                                boolean hasEth=false;
                                boolean hasNeo=false;
                                ethGnt.clear();
                                neoGnt.clear();
                                //计算ethPrice
                                for (WalletCountBean count : walletCount.data.getList()) {
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

                                    //请求代币列表
                                    okhttp3.Response tokenResponse = OkGo.<LzyResponse<TokenBean>>get(Url.CONVERSION + "/" + count.getId())
                                            .tag(this)
                                            .cacheKey(Url.CONVERSION+"/"+count.getId()+ App.isMain)
                                            .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                                            .headers("ct", App.get().getSp().getString(App.isMain?Constant.TOKEN:Constant.TEST_TOKEN,""))
                                            .execute();
                                    if (tokenResponse.code() == 200) {
                                        String tokenBody = tokenResponse.body().string().toString();
                                        if (tokenBody.contains("4000")) {
                                            LocalTokenBean localTokenBean = GsonUtils.jsonToObj(tokenBody, LocalTokenBean.class);
                                            if (null!=localTokenBean.data.getRecord()){
                                                if (localTokenBean.data.getRecord().getCategory_id()==2){
                                                    TokenBean.RecordBean record = localTokenBean.data.getRecord();

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

                                                    return;
                                                }
                                            }
                                            if (null != localTokenBean.data.getList()) {
                                                for (TokenBean.ListBean gnt : localTokenBean.data.getList()) {
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
                                    }
                                }
                                if (hasEth){
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
                                }

                                if (hasNeo){
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
                                }


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

                                totleCnyPrice = ethCnyPrice.add(neoCnyPrice);
                                totleUsdPrice = ethUsdPrice.add(neoUsdPrice);

                                priceBean.ethCny = ethCnyPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                                priceBean.ethUsd = ethUsdPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                                priceBean.neoCny = neoCnyPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                                priceBean.neoUsd = neoUsdPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                                priceBean.totleCny = totleCnyPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                                priceBean.totleUsd = totleUsdPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();

                                //设置总资产
                                App.get().getSp().putString(App.isMain ? Constant.TOTAL_PRICE : Constant.TOTAL_TEST_PRICE,  GsonUtils.objToJson(priceBean));
                                //设置缓存列表
                                //设置 eth 列表
                                App.get().getSp().putString(App.isMain ? Constant.ETH_LIST : Constant.ETH_TEST_LIST, GsonUtils.objToJson(ethList));
                                //设置 neo 列表
                                App.get().getSp().putString(App.isMain ? Constant.NEO_LIST : Constant.NEO_TEST_LIST, GsonUtils.objToJson(neoList));

                                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_TOTLE_PRICE_SERVICE));
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        LogUtils.e("service","停止更新服务");
    }
}
