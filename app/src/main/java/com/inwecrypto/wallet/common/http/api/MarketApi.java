package com.inwecrypto.wallet.common.http.api;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;

import java.util.HashMap;

import com.inwecrypto.wallet.AppApplication;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.MarkeListBean;
import com.inwecrypto.wallet.bean.MarketAddBean;
import com.inwecrypto.wallet.bean.MarketRemindBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.ui.market.adapter.MarketChartBean;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class MarketApi {

    public static void market(Object object, JsonCallback<LzyResponse<CommonListBean<MarkeListBean>>> callback){
        OkGo.<LzyResponse<CommonListBean<MarkeListBean>>>get(Url.MARKET_CATEGORY)
                .tag(object)
                .cacheKey(Constant.MARKET+ AppApplication.isMain)
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
                .cacheKey(Constant.MARKET+id+""+type+AppApplication.isMain)
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .params(params)
                .execute(callback);
    }

}
