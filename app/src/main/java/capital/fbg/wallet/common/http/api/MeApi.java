package capital.fbg.wallet.common.http.api;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;

import java.util.HashMap;

import capital.fbg.wallet.AppApplication;
import capital.fbg.wallet.bean.IcoOrderBean;
import capital.fbg.wallet.bean.CommonListBean;
import capital.fbg.wallet.bean.MailBean;
import capital.fbg.wallet.bean.CommonRecordBean;
import capital.fbg.wallet.bean.OSSBean;
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

public class MeApi {

    public static void contact(Object object,JsonCallback<LzyResponse<CommonListBean<MailBean>>> callback){
        OkGo.<LzyResponse<CommonListBean<MailBean>>>get(Url.CONTACT)
                .tag(object)
                .cacheKey(Constant.MAIL_LIST+ AppApplication.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    public static void contact(Object object,int category_id,String name,String address,String remark,JsonCallback<LzyResponse<Object>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("category_id",category_id+"");
        params.put("name",name);
        params.put("address",address);
        params.put("remark",remark);
        OkGo.<LzyResponse<Object>>post(Url.CONTACT)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void getContact(Object object,int id,JsonCallback<LzyResponse<CommonRecordBean<MailBean>>> callback){
        OkGo.<LzyResponse<CommonRecordBean<MailBean>>>get(Url.CONTACT+"/"+id)
                .tag(object)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    public static void editContact(Object object,int id,int category_id,String name,String address,String remark,JsonCallback<LzyResponse<Object>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("category_id",category_id+"");
        params.put("name",name);
        params.put("address",address);
        params.put("remark",remark);
        OkGo.<LzyResponse<Object>>put(Url.CONTACT+"/"+id)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void deleteContact(Object object,int id,JsonCallback<LzyResponse<Object>> callback){
        OkGo.<LzyResponse<Object>>delete(Url.CONTACT+"/"+id)
                .tag(object)
                .execute(callback);
    }

    public static void getIcoOrder(Object object,int page,int per_page,JsonCallback<LzyResponse<CommonListBean<IcoOrderBean>>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("page",page+"");
        params.put("per_page",per_page+"");
        OkGo.<LzyResponse<CommonListBean<IcoOrderBean>>>get(Url.ICO_ORDER)
                .tag(object)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .params(params)
                .execute(callback);
    }

    public static void getIcoOrderDetail(Object object,int id,JsonCallback<LzyResponse<CommonRecordBean<IcoOrderBean>>> callback){
        OkGo.<LzyResponse<CommonRecordBean<IcoOrderBean>>>get(Url.ICO_ORDER+"/"+id)
                .tag(object)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    public static void getUnit(Object object,JsonCallback<LzyResponse<CommonListBean<UnitBean>>> callback){
        OkGo.<LzyResponse<CommonListBean<UnitBean>>>get(Url.MONETARY_UNIT)
                .tag(object)
                .cacheKey(Constant.UNIT+AppApplication.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    public static void setUnit(Object object,int id,JsonCallback<LzyResponse<Object>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("monetary_unit_id",id+"");
        OkGo.<LzyResponse<Object>>post(Url.MONETARY_UNIT)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void setUserInfo(Object object,String nickname,String img,int sex,JsonCallback<LzyResponse<Object>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("nickname",nickname);
        params.put("img",img);
        params.put("sex",sex+"");
        OkGo.<LzyResponse<Object>>post(Url.USER)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void getSts(Object object,JsonCallback<LzyResponse<OSSBean>> callback){
        OkGo.<LzyResponse<OSSBean>>get(Url.STS)
                .tag(object)
                .execute(callback);
    }
}
