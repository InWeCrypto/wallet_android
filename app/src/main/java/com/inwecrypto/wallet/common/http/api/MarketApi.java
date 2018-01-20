package com.inwecrypto.wallet.common.http.api;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.bean.KLBean;
import com.inwecrypto.wallet.bean.PriceBean;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;

import java.util.ArrayList;
import java.util.HashMap;

import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.MarkeListBean;
import com.inwecrypto.wallet.bean.MarketAddBean;
import com.inwecrypto.wallet.bean.MarketRemindBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.bean.MarketChartBean;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class MarketApi {

    public static void market(Object object, JsonCallback<LzyResponse<ArrayList<MarkeListBean>>> callback){
        OkGo.<LzyResponse<ArrayList<MarkeListBean>>>get(Url.USER_TICKER)
                .tag(object)
                .cacheKey(Constant.MARKET+ App.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    public static void marketAdd(Object object,int is_all, JsonCallback<LzyResponse<CommonListBean<MarketAddBean>>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("is_all",is_all+"");
        OkGo.<LzyResponse<CommonListBean<MarketAddBean>>>get(Url.MARKET_CATEGORY)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void marketReamindAll(Object object,int is_all, JsonCallback<LzyResponse<CommonListBean<MarketRemindBean>>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("is_all",is_all+"");
        OkGo.<LzyResponse<CommonListBean<MarketRemindBean>>>get(Url.MARKET_CATEGORY)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void market(Object object,String ids, JsonCallback<LzyResponse<Object>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("market_ids",ids);
        OkGo.<LzyResponse<Object>>post(Url.MARKET_CATEGORY)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void marketNotification(Object object, JsonCallback<LzyResponse<CommonListBean<MarketRemindBean>>> callback){
        OkGo.<LzyResponse<CommonListBean<MarketRemindBean>>>get(Url.MARKET_NOTIFICATION)
                .tag(object)
                .execute(callback);
    }

    public static void marketNotification(Object object,int id, JsonCallback<LzyResponse<Object>> callback){
        OkGo.<LzyResponse<Object>>delete(Url.MARKET_NOTIFICATION+"/"+id)
                .tag(object)
                .execute(callback);
    }

    public static void marketNotification(Object object,String upper_limit,String lower_limit,int id, JsonCallback<LzyResponse<Object>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("upper_limit",upper_limit);
        params.put("lower_limit",lower_limit);
        OkGo.<LzyResponse<Object>>put(Url.MARKET_NOTIFICATION+"/"+id)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void marketNotification(Object object,String market_arr,String currency, JsonCallback<LzyResponse<Object>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("market_arr",market_arr);
        params.put("currency",currency);
        OkGo.<LzyResponse<Object>>post(Url.MARKET_NOTIFICATION)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void marketDetail(Object object,int id,int type, JsonCallback<LzyResponse<MarketChartBean>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("type",type+"");
        OkGo.<LzyResponse<MarketChartBean>>get(Url.MARKET_CATEGORY+"/"+id)
                .tag(object)
                .cacheKey(Constant.MARKET+id+""+type+ App.isMain)
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .params(params)
                .execute(callback);
    }

    public static void getMarket(Object object, JsonCallback<LzyResponse<CommonListBean<MarkeListBean>>> callback){
        OkGo.<LzyResponse<CommonListBean<MarkeListBean>>>get(Url.USER_TICKER)
                .tag(object)
                .cacheKey(Constant.MARKET+Url.USER_TICKER+ App.isMain)
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .execute(callback);
    }

    public static void selectMarket(Object object, JsonCallback<LzyResponse<CommonListBean<MarketAddBean>>> callback){
        OkGo.<LzyResponse<CommonListBean<MarketAddBean>>>get(Url.USER_TICKER_OPTIONS)
                .tag(object)
                .cacheKey(Constant.MARKET+Url.USER_TICKER_OPTIONS+ App.isMain)
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .execute(callback);
    }

    public static void setMarket(Object object, String markets, JsonCallback<LzyResponse<Object>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("market_ids",markets);
        OkGo.<LzyResponse<Object>>put(Url.USER_TICKER)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void getAllMarket(Object object, JsonCallback<LzyResponse<ArrayList<MarketAddBean>>> callback){
        HashMap<String,String> params=new HashMap<>();
        OkGo.<LzyResponse<ArrayList<MarketAddBean>>>get(Url.USER_TICKER_OPTIONS)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void getMarketKLine(Object object,String ico_type,String interval, JsonCallback<LzyResponse<ArrayList<KLBean>>> callback){
        OkGo.<LzyResponse<ArrayList<KLBean>>>get(Url.K_LINE+ico_type+"/usdt/"+interval)
                .tag(object)
                .cacheKey(Constant.MARKET+ico_type+interval+ App.isMain)
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .execute(callback);
    }

    public static void getCurrentPrice(Object object,String ico_type, JsonCallback<LzyResponse<PriceBean>> callback){
        OkGo.<LzyResponse<PriceBean>>get(Url.CURRENT_PRICE+ico_type)
                .tag(object)
                .cacheKey(Constant.MARKET+ico_type+ App.isMain)
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .execute(callback);
    }

}
