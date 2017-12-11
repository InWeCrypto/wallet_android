package com.inwecrypto.wallet.common.http.api;

import com.inwecrypto.wallet.bean.AdsBean;
import com.inwecrypto.wallet.bean.IcoBean;
import com.inwecrypto.wallet.bean.InfoMarketBean;
import com.inwecrypto.wallet.bean.InfoPriceBean;
import com.inwecrypto.wallet.bean.NewsBean;
import com.inwecrypto.wallet.bean.ProjectBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;

/**
 * 作者：xiaoji06 on 2017/11/14 20:29
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class InfoApi {

    public static void getAds(Object object,JsonCallback<LzyResponse<AdsBean>> callback){
        OkGo.<LzyResponse<AdsBean>>get(Url.HOME_AD)
                .tag(object)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);

    }

    public static void getNews(Object object,JsonCallback<LzyResponse<ArrayList<NewsBean>>> callback){
        OkGo.<LzyResponse<ArrayList<NewsBean>>>get(Url.HOME_NEWS)
                .tag(object)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);

    }

    public static void getInfo(Object object,String type,JsonCallback<LzyResponse<ArrayList<NewsBean>>> callback){
        OkGo.<LzyResponse<ArrayList<NewsBean>>>get(Url.ARTICLE_ALL+type)
                .tag(object)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);

    }

    public static void getICONews(Object object,JsonCallback<LzyResponse<ArrayList<IcoBean>>> callback){
        OkGo.<LzyResponse<ArrayList<IcoBean>>>get(Url.ARTICLE_ICO)
                .tag(object)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);

    }

    public static void getProject(Object object,JsonCallback<LzyResponse<ArrayList<ProjectBean>>> callback){
        OkGo.<LzyResponse<ArrayList<ProjectBean>>>get(Url.HOME_PROJECT)
                .tag(object)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);

    }

    //[/all | /txt | /img-txt | /video]
    public static void getInweInfo(Object object,int id,String type,JsonCallback<LzyResponse<ArrayList<NewsBean>>> callback){
        OkGo.<LzyResponse<ArrayList<NewsBean>>>get(Url.CATEGORY+id+"/articles/"+type)
                .tag(object)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);

    }

    public static void getPrice(Object object,String url,JsonCallback<LzyResponse<InfoPriceBean>> callback){
        OkGo.<LzyResponse<InfoPriceBean>>get(Url.MAIN_2_URL+"/"+url)
                .tag(object)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);

    }

    public static void getMarket(Object object,String url,JsonCallback<LzyResponse<ArrayList<InfoMarketBean>>> callback){
        OkGo.<LzyResponse<ArrayList<InfoMarketBean>>>get(Url.MAIN_2_URL+"/"+url)
                .tag(object)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);

    }

    public static void getTeam(Object object,int id,StringCallback callback){
        OkGo.<String>get(Url.TEAM_INFO+id)
                .tag(object)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);

    }

    public static void getCardPrice(Object object,String url,JsonCallback<LzyResponse<InfoPriceBean>> callback){
        OkGo.<LzyResponse<InfoPriceBean>>get(Url.MAIN_2_URL+"/"+url)
                .tag(object)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);

    }

    public static void searchArticles(Object object,String key,JsonCallback<LzyResponse<ArrayList<NewsBean>>> callback){
        OkGo.<LzyResponse<ArrayList<NewsBean>>>get(Url.SEARCH+"articles/?k="+key)
                .tag(object)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);

    }

    public static void searchIcoAssess(Object object,String key,JsonCallback<LzyResponse<ArrayList<IcoBean>>> callback){
        OkGo.<LzyResponse<ArrayList<IcoBean>>>get(Url.SEARCH+"ico_assess/?k="+key)
                .tag(object)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);

    }

    public static void searchProject(Object object,String key,JsonCallback<LzyResponse<ArrayList<ProjectBean>>> callback){
        OkGo.<LzyResponse<ArrayList<ProjectBean>>>get(Url.SEARCH+"project/?k="+key)
                .tag(object)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);

    }
}
