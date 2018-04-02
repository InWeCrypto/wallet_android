package com.inwecrypto.wallet.common.http.api;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.bean.ArticleListBean;
import com.inwecrypto.wallet.bean.ArticleTagsBean;
import com.inwecrypto.wallet.bean.CandyBowBean;
import com.inwecrypto.wallet.bean.CommonDataBean;
import com.inwecrypto.wallet.bean.CommonRecordBean;
import com.inwecrypto.wallet.bean.ExchangeNoticeListBean;
import com.inwecrypto.wallet.bean.ProjectListBean;
import com.inwecrypto.wallet.bean.ProjectMarketBean;
import com.inwecrypto.wallet.bean.Rank1Bean;
import com.inwecrypto.wallet.bean.SearchBean;
import com.inwecrypto.wallet.bean.TradingProjectDetaileBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.bean.YaoqinBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.CommonDataSource;

/**
 * 作者：xiaoji06 on 2018/2/9 20:36
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ZixunApi {

    /**
     * 获取收藏项目
     * @param object
     * @param page
     * @param callback
     */
    public static void getFavoriteProject(Object object,int page,JsonCallback<LzyResponse<ProjectListBean>> callback){
        OkGo.<LzyResponse<ProjectListBean>>get(Url.PROJECT_FAVORITE+page+"&per_page=1000")
                .tag(object)
                .cacheKey(Url.PROJECT_FAVORITE+App.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    /**
     * 获取项目
     * @param object
     * @param type
     * @param page
     * @param callback
     */
    public static void getProject(Object object,int type,int page,JsonCallback<LzyResponse<ProjectListBean>> callback){
        OkGo.<LzyResponse<ProjectListBean>>get(Url.PROJECT+type+"&per_page=1000&page="+page)
                .tag(object)
                .cacheKey(Url.PROJECT+type+App.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    /**
     * 获取项目
     * @param object
     * @param page
     * @param callback
     */
    public static void getAllProject(Object object,int page,JsonCallback<LzyResponse<ProjectListBean>> callback){
        OkGo.<LzyResponse<ProjectListBean>>get(Url.ALL_PROJECT+"per_page=1000&page="+page)
                .tag(object)
                .cacheKey(Url.ALL_PROJECT+App.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    /**
     * 获取inwe 热点
     * @param object
     * @param page
     * @param callback
     */
    public static void getInweHot(Object object,int page,JsonCallback<LzyResponse<ArticleListBean>> callback){
        OkGo.<LzyResponse<ArticleListBean>>get(Url.INWE_HOT+page)
                .tag(object)
                .execute(callback);
    }


    /**
     * 获取inwe 热点
     * @param object
     * @param page
     * @param callback
     */
    public static void getInweHotHistory(Object object,int page,String type,JsonCallback<LzyResponse<ArticleListBean>> callback){
        OkGo.<LzyResponse<ArticleListBean>>get(Url.INWE_HOT_HISTORY+page+"&"+type)
                .tag(object)
                .execute(callback);
    }

    /**
     * 获取inwe 热点
     * @param object
     * @param page
     * @param callback
     */
    public static void getInweView(Object object,int page,JsonCallback<LzyResponse<ArticleListBean>> callback){
        OkGo.<LzyResponse<ArticleListBean>>get(Url.INWE_VIEW+page)
                .tag(object)
                .execute(callback);
    }

    /**
     * 获取TradingView
     * @param object
     * @param page
     * @param callback
     */
    public static void getTradingView(Object object,int page,JsonCallback<LzyResponse<ArticleListBean>> callback){
        OkGo.<LzyResponse<ArticleListBean>>get(Url.TRADING_VIEW+page)
                .tag(object)
                .execute(callback);
    }

    /**
     * 获取交易所公告
     * @param object
     * @param page
     * @param callback
     */
    public static void  getExchangeNotice(Object object,int page,JsonCallback<LzyResponse<ExchangeNoticeListBean>> callback){
        OkGo.<LzyResponse<ExchangeNoticeListBean>>get(Url.EXCHANGE_NOTICE+page)
                .tag(object)
                .execute(callback);
    }

    /**
     * 获取历史空头日历
     * @param object
     * @param page
     * @param callback
     */
    public static void  getCandybow(Object object,int page,JsonCallback<LzyResponse<CandyBowBean>> callback){
        OkGo.<LzyResponse<CandyBowBean>>get(Url.CANDY_BOW+page)
                .tag(object)
                .execute(callback);
    }

    /**
     * 获取空头日历
     * @param object
     * @param page
     * @param callback
     */
    public static void  getCandybow(Object object,int year,int mounth,int page,JsonCallback<LzyResponse<CandyBowBean>> callback){
        OkGo.<LzyResponse<CandyBowBean>>get(Url.CANDY_BOW+page+"&year="+year+"&mounth="+mounth+"&per_page=31")
                .tag(object)
                .execute(callback);
    }

    /**
     * 获取空头日历
     * @param object
     * @param page
     * @param callback
     */
    public static void  getCandybowDay(Object object,int year,int mounth,int day,int page,JsonCallback<LzyResponse<CandyBowBean>> callback){
        OkGo.<LzyResponse<CandyBowBean>>get(Url.CANDY_BOW+page+"&year="+year+"&month="+mounth+"&day="+day+"&per_page=100")
                .tag(object)
                .execute(callback);
    }

    /**
     * 获取inwe 热点
     * @param object
     * @param page
     * @param callback
     */
    public static void getProjectNews(Object object,String id,int page,JsonCallback<LzyResponse<ArticleListBean>> callback){
        OkGo.<LzyResponse<ArticleListBean>>get(Url.PROJECT_NEWS+id+"&page="+page)
                .tag(object)
                .execute(callback);
    }

    /**
     * 收藏项目
     * @param object
     * @param id
     * @param isCollect
     * @param callback
     */
    public static void collectProject(Object object,String id,boolean isCollect,JsonCallback<LzyResponse<Object>> callback){

        JSONObject params=new JSONObject();
        try {
            params.putOpt("enable",isCollect);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<LzyResponse<Object>>put(Url.BASE_CATEGORY+id+"/collect")
                .tag(object)
                .upJson(params)
                .execute(callback);
    }

    /**
     * 获取项目交易市场
     * @param object
     * @param unit
     * @param callback
     */
    public static void getMarkets(Object object,String unit,JsonCallback<LzyResponse<ArrayList<ProjectMarketBean>>> callback){
        OkGo.<LzyResponse<ArrayList<ProjectMarketBean>>>get(Url.PROJECT_MARKETS+unit+"/all")
                .tag(object)
                .execute(callback);
    }

    /**
     * 获取项目详情
     * @param object
     * @param id
     * @param callback
     */
    public static void getProjectDetaile(Object object,String id,JsonCallback<LzyResponse<TradingProjectDetaileBean>> callback){
        OkGo.<LzyResponse<TradingProjectDetaileBean>>get(Url.PROJECT_DEDTAILE+id)
                .tag(object)
                .execute(callback);
    }


    /**
     * 获取关键字项目
     * @param object
     * @param key
     * @param page
     * @param callback
     */
    public static void getProjectKey(Object object,String key,int page,JsonCallback<LzyResponse<ProjectListBean>> callback){
        OkGo.<LzyResponse<ProjectListBean>>get(Url.SEARCH_PROJECT+key+"&page="+page)
                .tag(object)
                .execute(callback);
    }

    /**
     * 获取关键字新闻
     * @param object
     * @param key
     * @param page
     * @param callback
     */
    public static void getNewsKey(Object object,String key,int page,JsonCallback<LzyResponse<SearchBean>> callback){
        OkGo.<LzyResponse<SearchBean>>get(Url.SEARCH_NEWS+key+"&page="+page)
                .tag(object)
                .execute(callback);
    }

    /**
     * 获取资讯标签
     * @param object
     * @param callback
     */
    public static void getHistoryTags(Object object,JsonCallback<LzyResponse<ArticleTagsBean>> callback){
        OkGo.<LzyResponse<ArticleTagsBean>>get(Url.ARTICLE_TAGS)
                .tag(object)
                .execute(callback);

    }

    /**
     * 获取历史资讯
     * @param object
     * @param id
     * @param tag
     * @param page
     * @param callback
     */
    public static void getHistoryNews(Object object,int id,int tag,int page,JsonCallback<LzyResponse<ArticleListBean>> callback){
        String url="";
        if (tag==-1){
            url=Url.PROJECT_NEWS+id+"&page="+page;
        }else {
            url=Url.PROJECT_NEWS+id+"&tag_id="+tag+"&page="+page;
        }
        OkGo.<LzyResponse<ArticleListBean>>get(url)
                .tag(object)
                .execute(callback);
    }

    /**
     * 获取 ico价格
     * @param object
     * @param units
     * @param callback
     */
    public static void getICOranks(Object object,ArrayList<String> units,StringCallback callback){
        JSONObject params=new JSONObject();
        JSONArray icolist=new JSONArray();
        try {
            for (String unit:units){
                icolist.put(unit);
            }
            params.putOpt("ico_list",icolist);
            params.putOpt("currency","cny");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<String>post(Url.ICO_RANKS)
                .tag(object)
                .upJson(params)
                .execute(callback);

    }

    /**
     * 项目置顶
     * @param object
     * @param id
     * @param isTop
     * @param callback
     */
    public static void projectTop(Object object,int id,boolean isTop,JsonCallback<LzyResponse<Object>> callback){

        JSONObject params=new JSONObject();
        try {
            params.putOpt("enable",isTop);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<LzyResponse<Object>>put(Url.SET_TOP+id+"/set_top")
                .tag(object)
                .upJson(params)
                .execute(callback);

    }

    /**
     * 项目评分
     * @param object
     * @param id
     * @param score
     * @param callback
     */
    public static void projectScore(Object object,int id,float score,JsonCallback<LzyResponse<Object>> callback){

        JSONObject params=new JSONObject();
        try {
            params.putOpt("score",score);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<LzyResponse<Object>>put(Url.SET_TOP+id+"/score")
                .tag(object)
                .upJson(params)
                .execute(callback);

    }

    /**
     * 文章收藏
     * @param object
     * @param id
     * @param enable
     * @param callback
     */
    public static void  newsCollect(Object object,String id,boolean enable,JsonCallback<LzyResponse<Object>> callback){

        JSONObject params=new JSONObject();
        try {
            params.putOpt("enable",enable);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<LzyResponse<Object>>put(Url.NEWS_COLLECT+id+"/collect")
                .tag(object)
                .upJson(params)
                .execute(callback);

    }


    /**
     * 获取邀请码
     * @param object
     * @param callback
     */
    public static void getYaoqinKey(Object object,JsonCallback<LzyResponse<YaoqinBean>> callback){
        OkGo.<LzyResponse<YaoqinBean>>get(Url.GET_YAOQIN)
                .tag(object)
                .execute(callback);
    }


    /**
     * 取消项目红点
     * @param object
     * @param callback
     */
    public static void cancleDot(Object object,String id,JsonCallback<LzyResponse<Object>> callback){
        OkGo.<LzyResponse<Object>>put(Url.CANCLE_DOT+id+"/undot")
                .tag(object)
                .execute(callback);
    }

    /**
     * 获取指定项目
     * @param object
     * @param projects
     * @param callback
     */
    public static void getFixProject(Object object, String projects,JsonCallback<LzyResponse<ProjectListBean>> callback){
        OkGo.<LzyResponse<ProjectListBean>>get(Url.FIX_PROJECT+projects+"&per_page=1000&page=1")
                .tag(object)
                .cacheKey(Url.FIX_PROJECT+projects+App.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    /**
     * 获取轮播图
     * @param object
     * @param callback
     */
    public static void getScrollInweHot(Object object,JsonCallback<LzyResponse<ArticleListBean>> callback){
        OkGo.<LzyResponse<ArticleListBean>>get(Url.ARTICLE_SCROLL)
                .tag(object)
                .cacheKey(Url.ARTICLE_SCROLL+App.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }


    /**
     * 获取市值排行
     * @param object
     * @param callback
     */
    public static void getMarketCap(Object object,JsonCallback<LzyResponse<ArrayList<Rank1Bean>>> callback){
        OkGo.<LzyResponse<ArrayList<Rank1Bean>>>get(Url.MARKET_CAP)
                .tag(object)
                .cacheKey(Url.MARKET_CAP+App.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

}
