package capital.fbg.wallet.common.http.api;

import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;

import capital.fbg.wallet.AppApplication;
import capital.fbg.wallet.bean.ArticleBean;
import capital.fbg.wallet.bean.CommonListBean;
import capital.fbg.wallet.bean.CommonRecordBean;
import capital.fbg.wallet.bean.FindBean;
import capital.fbg.wallet.bean.IcoGasBean;
import capital.fbg.wallet.bean.IcoListBean;
import capital.fbg.wallet.bean.UnitBean;
import capital.fbg.wallet.common.Constant;
import capital.fbg.wallet.common.http.LzyResponse;
import capital.fbg.wallet.common.http.Url;
import capital.fbg.wallet.common.http.callback.JsonCallback;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class DiscoverApi {

    public static void getFind(Object object,JsonCallback<LzyResponse<FindBean>> callback){
        OkGo.<LzyResponse<FindBean>>get(Url.FIND)
                .tag(object)
                .cacheKey(Constant.DISCOVER_BANNER+ AppApplication.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    public static void getArticle(Object object,int page,int per_page,JsonCallback<LzyResponse<CommonListBean<ArticleBean>>> callback){
        HashMap<String,String> parmas=new HashMap<>();
        parmas.put("page",page+"");
        parmas.put("per_page",per_page+"");
        OkGo.<LzyResponse<CommonListBean<ArticleBean>>>get(Url.ARTICLE)
                .tag(object)
                .cacheKey("ARTICLE"+page+AppApplication.isMain)
                .cacheMode(page==1?CacheMode.FIRST_CACHE_THEN_REQUEST:CacheMode.NO_CACHE)
                .params(parmas)
                .execute(callback);
    }

    public static void getIco(Object object,int page, int per_page, JsonCallback<LzyResponse<CommonListBean<IcoListBean>>> callback){
        HashMap<String,String> parmas=new HashMap<>();
        parmas.put("page",page+"");
        parmas.put("per_page",per_page+"");
        OkGo.<LzyResponse<CommonListBean<IcoListBean>>>get(Url.ICO)
                .tag(object)
                .cacheKey("ICO"+page+AppApplication.isMain)
                .cacheMode(page==1?CacheMode.FIRST_CACHE_THEN_REQUEST:CacheMode.NO_CACHE)
                .params(parmas)
                .execute(callback);
    }

    public static void getArticle(Object object,int id,StringCallback callback){
        OkGo.<String>get(Url.ARTICLE+"/"+id)
                .tag(object)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    public static void getGas(Object object,int category_id,int type,JsonCallback<LzyResponse<CommonRecordBean<IcoGasBean>>> callback){
        HashMap<String,String> parmas=new HashMap<>();
        parmas.put("category_id",category_id+"");
        parmas.put("type",type+"");
        OkGo.<LzyResponse<CommonRecordBean<IcoGasBean>>>get(Url.GAS)
                .tag(object)
                .params(parmas)
                .execute(callback);
    }

    public static void icoOrder(Object object
            ,int wallet_id
            ,int ico_id
            ,String trade_no
            ,String pay_address
            ,String receive_address
            ,String fee
            ,String handle_fee
            ,String hash
            ,JsonCallback<LzyResponse<Object>> callback){
        HashMap<String,String> parmas=new HashMap<>();
        parmas.put("category_id",wallet_id+"");
        parmas.put("rtype",ico_id+"");
        parmas.put("trade_no",trade_no);
        parmas.put("pay_address",pay_address);
        parmas.put("receive_address",receive_address);
        parmas.put("fee",fee);
        parmas.put("handle_fee",handle_fee);
        parmas.put("hash",hash);
        OkGo.<LzyResponse<Object>>post(Url.ICO_ORDER)
                .tag(object)
                .params(parmas)
                .execute(callback);
    }
}
