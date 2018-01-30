package com.inwecrypto.wallet.common.http.api;

import com.inwecrypto.wallet.App;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.inwecrypto.wallet.bean.IcoOrderBean;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.MailBean;
import com.inwecrypto.wallet.bean.CommonRecordBean;
import com.inwecrypto.wallet.bean.OSSBean;
import com.inwecrypto.wallet.bean.UnitBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class MeApi {

    public static void contact(Object object,int id,JsonCallback<LzyResponse<ArrayList<MailBean>>> callback){
        OkGo.<LzyResponse<ArrayList<MailBean>>>get(Url.USER_CONTACT+id)
                .tag(object)
                .cacheKey(Constant.MAIL_LIST+id+ App.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    public static void contact(Object object,int ico_id,String name,String address,String remark,JsonCallback<LzyResponse<Object>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("category_id",ico_id+"");
        params.put("name",name);
        params.put("address",address);
        params.put("remark",remark);
        OkGo.<LzyResponse<Object>>post(Url.USER_CONTACT_ADD)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void getContact(Object object,int id,JsonCallback<LzyResponse<MailBean>> callback){
        OkGo.<LzyResponse<MailBean>>get(Url.USER_CONTACT_ADD+"/"+id)
                .tag(object)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    public static void editContact(Object object,int id,int ico_id,String name,String address,String remark,JsonCallback<LzyResponse<Object>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("category_id",ico_id+"");
        params.put("name",name);
        params.put("address",address);
        params.put("remark",remark);
        OkGo.<LzyResponse<Object>>put(Url.USER_CONTACT_ADD+"/"+id)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void deleteContact(Object object,int id,JsonCallback<LzyResponse<Object>> callback){
        OkGo.<LzyResponse<Object>>delete(Url.USER_CONTACT_ADD+"/"+id)
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
                .cacheKey(Constant.UNIT+ App.isMain)
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

    public static void check(Object object,StringCallback callback){
        OkGo.<String>get(Url.UPDATE)
                .tag(object)
                .execute(callback);
    }
}
