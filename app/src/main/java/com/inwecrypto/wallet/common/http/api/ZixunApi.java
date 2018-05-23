package com.inwecrypto.wallet.common.http.api;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.bean.ArticleListBean;
import com.inwecrypto.wallet.bean.ArticleTagsBean;
import com.inwecrypto.wallet.bean.CandyBowBean;
import com.inwecrypto.wallet.bean.CommonDataBean;
import com.inwecrypto.wallet.bean.CommonPageBean;
import com.inwecrypto.wallet.bean.CommonRecordBean;
import com.inwecrypto.wallet.bean.DrawRecordBean;
import com.inwecrypto.wallet.bean.ExchangeNoticeListBean;
import com.inwecrypto.wallet.bean.HongbaoAuthBean;
import com.inwecrypto.wallet.bean.HongbaoDetailBean;
import com.inwecrypto.wallet.bean.HongbaoFeeBean;
import com.inwecrypto.wallet.bean.HongbaoGntBean;
import com.inwecrypto.wallet.bean.HongbaoMinGas;
import com.inwecrypto.wallet.bean.HongbaoNumBean;
import com.inwecrypto.wallet.bean.HongbaoRecordDetaileBean;
import com.inwecrypto.wallet.bean.MarketCapBean;
import com.inwecrypto.wallet.bean.MarketPriceBean;
import com.inwecrypto.wallet.bean.PingjiaBean;
import com.inwecrypto.wallet.bean.ProjectAnalysisBean;
import com.inwecrypto.wallet.bean.ProjectDetailAnalysisBean;
import com.inwecrypto.wallet.bean.ProjectListBean;
import com.inwecrypto.wallet.bean.ProjectMarketBean;
import com.inwecrypto.wallet.bean.Rank1Bean;
import com.inwecrypto.wallet.bean.Rank2Bean;
import com.inwecrypto.wallet.bean.Rank3Bean;
import com.inwecrypto.wallet.bean.Rank4Bean;
import com.inwecrypto.wallet.bean.Rank5Bean;
import com.inwecrypto.wallet.bean.SearchBean;
import com.inwecrypto.wallet.bean.SendHongbaoBean;
import com.inwecrypto.wallet.bean.TagBean;
import com.inwecrypto.wallet.bean.TradingProjectDetaileBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.bean.XiangmuPingjiaBean;
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

import retrofit2.http.PATCH;

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

    /**
     * 获取市值排行
     * @param object
     * @param callback
     */
    public static void getExchanges(Object object,JsonCallback<LzyResponse<ArrayList<Rank2Bean>>> callback){
        OkGo.<LzyResponse<ArrayList<Rank2Bean>>>get(Url.EXCHANGES)
                .tag(object)
                .cacheKey(Url.EXCHANGES+App.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    /**
     * 获取市值排行
     * @param object
     * @param callback
     */
    public static void getDapp(Object object,JsonCallback<LzyResponse<ArrayList<Rank3Bean>>> callback){
        OkGo.<LzyResponse<ArrayList<Rank3Bean>>>get(Url.DAPP)
                .tag(object)
                .cacheKey(Url.DAPP+App.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    /**
     * 获取市值排行
     * @param object
     * @param callback
     */
    public static void getCategoryRanking(Object object,JsonCallback<LzyResponse<ArrayList<Rank4Bean>>> callback){
        OkGo.<LzyResponse<ArrayList<Rank4Bean>>>get(Url.CATEGORY_RANKING)
                .tag(object)
                .cacheKey(Url.CATEGORY_RANKING+App.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    /**
     * 获取市值排行
     * @param object
     * @param callback
     */
    public static void getHotArticle(Object object,JsonCallback<LzyResponse<Rank5Bean>> callback){
        OkGo.<LzyResponse<Rank5Bean>>get(Url.ARTICLE_RANKING)
                .tag(object)
                .cacheKey(Url.ARTICLE_RANKING+App.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }


    /**
     * 获取当前评价
     * @param object
     * @param callback
     */
    public static void getCurComment(Object object,String id,JsonCallback<LzyResponse<PingjiaBean>> callback){
        OkGo.<LzyResponse<PingjiaBean>>get(Url.COMMENT_INFO+id+"/get_cur_user_comment")
                .tag(object)
                .execute(callback);
    }

    /**
     * 获取当前评价
     * @param object
     * @param callback
     */
    public static void postComment(Object object,String id
            ,float score
            ,String comment
            ,int type,JsonCallback<LzyResponse<PingjiaBean>> callback){
        JSONObject params=new JSONObject();
        try {
            params.putOpt("score",score);
            params.putOpt("category_comment",comment);
            params.putOpt("category_comment_tag_id",type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<LzyResponse<PingjiaBean>>post(Url.COMMENT_INFO+id+"/comment")
                .tag(object)
                .upJson(params)
                .execute(callback);
    }


    /**
     * 获取项目分析
     * @param object
     * @param callback
     */
    public static void getProjectAnalysis(Object object,String id,JsonCallback<LzyResponse<ProjectAnalysisBean>> callback){
        OkGo.<LzyResponse<ProjectAnalysisBean>>get(Url.COMMENT_INFO+id+"/rough_analysis")
                .tag(object)
                .execute(callback);
    }

    /**
     * 获取详细评价
     * @param object
     * @param callback
     */
    public static void getProjectDetailAnalysis(Object object,String id,String type,JsonCallback<LzyResponse<ArrayList<ProjectDetailAnalysisBean>>> callback){
        OkGo.<LzyResponse<ArrayList<ProjectDetailAnalysisBean>>>get(Url.COMMENT_INFO+id+"/detail_analysis/"+type)
                .tag(object)
                .execute(callback);
    }

    /**
     * 获取 项目评价标签
     * @param object
     * @param callback
     */
    public static void getProjectTag(Object object,String id,JsonCallback<LzyResponse<ArrayList<TagBean>>> callback){
        OkGo.<LzyResponse<ArrayList<TagBean>>>get(Url.COMMENT_INFO+id+"/comment_tags")
                .tag(object)
                .execute(callback);
    }


    /**
     * 获取 项目评价
     * @param object
     * @param callback
     */
    public static void getProjectComment(Object object,String id,String filter,int page,JsonCallback<LzyResponse<XiangmuPingjiaBean>> callback){
        OkGo.<LzyResponse<XiangmuPingjiaBean>>get(Url.COMMENT_INFO+id+"/comment?page="+page+"&filter="+filter)
                .tag(object)
                .execute(callback);
    }


    /**
     * 获取 项目评价
     * @param object
     * @param callback
     */
    public static void getProjectComment(Object object,String id,String start,String end,int page,JsonCallback<LzyResponse<XiangmuPingjiaBean>> callback){
        OkGo.<LzyResponse<XiangmuPingjiaBean>>get(Url.COMMENT_INFO+id+"/comment?page="+page+"&start="+start+"&end="+end)
                .tag(object)
                .execute(callback);
    }


    /**
     * 获取 项目评价
     * @param object
     * @param callback
     */
    public static void getMarketCap(Object object,String token,JsonCallback<LzyResponse<MarketCapBean>> callback){
        OkGo.<LzyResponse<MarketCapBean>>get(Url.RANK_MARKET_CAP+token)
                .tag(object)
                .execute(callback);
    }

    /**
     * 获取 项目评价
     * @param object
     * @param callback
     */
    public static void getMarketPrice(Object object,JsonCallback<LzyResponse<MarketPriceBean>> callback){
        OkGo.<LzyResponse<MarketPriceBean>>get(Url.TOTLE_MARKET)
                .tag(object)
                .execute(callback);
    }

    /**
     * 获取 红包代币
     * @param object
     * @param callback
     */
    public static void getGntCategory(Object object,int id,JsonCallback<LzyResponse<ArrayList<HongbaoGntBean>>> callback){
        OkGo.<LzyResponse<ArrayList<HongbaoGntBean>>>get(Url.GET_GNT_CATEGORY+id)
                .tag(object)
                .execute(callback);
    }

    /**
     * 获取 领取的红包列表
     * @param object
     * @param callback
     */
    public static void getDrawRecord(Object object,ArrayList<WalletBean> walletBeans,int page,JsonCallback<LzyResponse<CommonPageBean<DrawRecordBean>>> callback){

        JSONObject params=new JSONObject();
        JSONArray wallets=new JSONArray();

        for (WalletBean wallet:walletBeans){
            wallets.put(wallet.getAddress().toLowerCase());
        }

        try {
            params.putOpt("wallet_addrs",wallets);
            params.putOpt("page",page);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<LzyResponse<CommonPageBean<DrawRecordBean>>>post(Url.DRAW_RECORD)
                .tag(object)
                .upJson(params)
                .execute(callback);
    }

    /**
     * 获取 获取红包的详情
     * @param object
     * @param callback
     */
    public static void getSendRecord(Object object,String id,JsonCallback<LzyResponse<HongbaoDetailBean>> callback){
        OkGo.<LzyResponse<HongbaoDetailBean>>get(Url.SEND_RECORD+id)
                .tag(object)
                .execute(callback);
    }

    /**
     * 获取 获取红包的详情
     * @param object
     * @param callback
     */
    public static void getSendRecord(Object object,ArrayList<WalletBean> walletBeans,int page,JsonCallback<LzyResponse<CommonPageBean<SendHongbaoBean>>> callback){

        JSONObject params=new JSONObject();
        JSONArray wallets=new JSONArray();

        for (WalletBean wallet:walletBeans){
            wallets.put(wallet.getAddress().toLowerCase());
        }

        try {
            params.putOpt("wallet_addrs",wallets);
            params.putOpt("page",page);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<LzyResponse<CommonPageBean<SendHongbaoBean>>>post(Url.SEND_RECORD_LIST)
                .tag(object)
                .upJson(params)
                .execute(callback);
    }

    /**
     * 授权
     * @param object
     * @param callback
     */
    public static void authRedbag(Object object
            ,String redbag_addr
            ,String redbag_symbol
            ,String redbag_number
            ,String redbag
            ,String auth_tx_nonce
            ,String auth_gas
            ,String repeat_id
            ,String asset_id
            ,String pay_address
            ,String receive_address
            ,String fee
            ,String handle_fee
            ,String remark
            ,String data
            ,JsonCallback<LzyResponse<HongbaoAuthBean>> callback){

        JSONObject params=new JSONObject();
        JSONObject transcation=new JSONObject();
        try {
            params.putOpt("redbag_addr",redbag_addr.toLowerCase());
            params.putOpt("redbag_symbol",redbag_symbol);
            params.putOpt("redbag_number",redbag_number);
            params.putOpt("redbag",redbag);
            params.putOpt("auth_tx_nonce",auth_tx_nonce);
            params.putOpt("auth_gas",auth_gas);
            params.putOpt("repeat_id",repeat_id);

            transcation.putOpt("asset_id",asset_id);
            transcation.putOpt("pay_address",pay_address.toLowerCase());
            transcation.putOpt("receive_address",receive_address.toLowerCase());
            transcation.putOpt("fee",fee);
            transcation.putOpt("handle_fee",handle_fee);
            transcation.putOpt("remark",remark);
            transcation.putOpt("data",data);

            params.putOpt("transaction_param",transcation);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<LzyResponse<HongbaoAuthBean>>post(Url.REDBAG_AUTH)
                .tag(object)
                .upJson(params)
                .execute(callback);
    }

    /**
     * 支付手续费
     * @param object
     * @param callback
     */
    public static void redbagFee(Object object
            ,String id
            ,String redbag_addr
            ,String fee
            ,String fee_addr
            ,String redbag_id
            ,String redbag_tx_nonce
            ,String repeat_id
            ,String asset_id
            ,String pay_address
            ,String receive_address
            ,String order_fee
            ,String handle_fee
            ,String remark
            ,String data
            ,JsonCallback<LzyResponse<HongbaoFeeBean>> callback){

        JSONObject params=new JSONObject();
        JSONObject transcation=new JSONObject();
        try {
            params.putOpt("fee",fee);
            params.putOpt("fee_addr",fee_addr.toLowerCase());
            params.putOpt("redbag_id",redbag_id);
            params.putOpt("redbag_tx_nonce",redbag_tx_nonce);
            params.putOpt("repeat_id",repeat_id);

            transcation.putOpt("asset_id",asset_id);
            transcation.putOpt("pay_address",pay_address.toLowerCase());
            transcation.putOpt("receive_address",receive_address.toLowerCase());
            transcation.putOpt("fee",order_fee);
            transcation.putOpt("handle_fee",handle_fee);
            transcation.putOpt("remark",remark);
            transcation.putOpt("data",data);

            params.putOpt("transaction_param",transcation);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<LzyResponse<HongbaoFeeBean>>post(Url.REDBAG_FEE+id+"/"+redbag_addr)
                .tag(object)
                .upJson(params)
                .execute(callback);
    }

    /**
     * 发送红包
     * @param object
     * @param callback
     */
    public static void sendRedbag(Object object
            ,String id
            ,String redbag_addr
            ,String share_type
            ,String share_attr
            ,String share_user
            ,String share_msg
            ,JsonCallback<LzyResponse<HongbaoFeeBean>> callback){

        JSONObject params=new JSONObject();

        try {
            params.putOpt("share_type",share_type);
            params.putOpt("share_type",share_type);
            params.putOpt("share_attr",share_attr);
            params.putOpt("share_user",share_user);
            params.putOpt("share_msg",share_msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<LzyResponse<HongbaoFeeBean>>post(Url.REDBAG_SEND+id+"/"+redbag_addr)
                .tag(object)
                .upJson(params)
                .execute(callback);
    }


    /**
     * 获取 获取红包个数
     * @param object
     * @param callback
     */
    public static void getSendRecordNum(Object object,ArrayList<WalletBean> walletBeans,JsonCallback<LzyResponse<HongbaoNumBean>> callback){

        JSONObject params=new JSONObject();
        JSONArray wallets=new JSONArray();

        for (WalletBean wallet:walletBeans){
            wallets.put(wallet.getAddress().toLowerCase());
        }

        try {
            params.putOpt("wallet_addrs",wallets);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<LzyResponse<HongbaoNumBean>>post(Url.REDBAG_SEND_COUNT)
                .tag(object)
                .upJson(params)
                .execute(callback);
    }

    /**
     * 收红包
     * @param object
     * @param callback
     */
    public static void getRedbag(Object object
            ,String id
            ,String redbag_addr
            ,String wallet_addr
            ,JsonCallback<LzyResponse<DrawRecordBean>> callback){

        JSONObject params=new JSONObject();

        try {
            params.putOpt("wallet_addr",wallet_addr.toLowerCase());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<LzyResponse<DrawRecordBean>>post(Url.REDBAG_DRAW+id+"/"+redbag_addr.toLowerCase())
                .tag(object)
                .upJson(params)
                .execute(callback);
    }


    /**
     * 获取红包详情数据
     * @param object
     * @param id
     * @param callback
     */
    public static void getRedbagDetaile(Object object
            ,String id,JsonCallback<LzyResponse<HongbaoRecordDetaileBean>> callback){
        OkGo.<LzyResponse<HongbaoRecordDetaileBean>>get(Url.REDBAG_SEND_RECORD+id)
                .tag(object)
                .execute(callback);
    }


    public static void ethRpc(Object object
            ,JSONObject rpc,JsonCallback<LzyResponse<String>> callback){
        OkGo.<LzyResponse<String>>post(Url.RPC)
                .tag(object)
                .upJson(rpc)
                .execute(callback);
    }


    public static void getRedbagId(Object object,JsonCallback<LzyResponse<String>> callback){
        OkGo.<LzyResponse<String>>get(Url.REDBAG_ID)
                .tag(object)
                .execute(callback);
    }

    public static void getMinGas(Object object,JsonCallback<LzyResponse<HongbaoMinGas>> callback){
        OkGo.<LzyResponse<HongbaoMinGas>>get(Url.MIN_GAS)
                .tag(object)
                .execute(callback);
    }

}
