package com.inwecrypto.wallet.common.http.api;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.bean.PingjiaBean;
import com.inwecrypto.wallet.bean.PingjiaInfoBean;
import com.inwecrypto.wallet.bean.PingjiaReplyBean;
import com.inwecrypto.wallet.bean.ReplyBean;
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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class MeApi {

    public static void contact(Object object,int id,JsonCallback<LzyResponse<CommonListBean<MailBean>>> callback){
        OkGo.<LzyResponse<CommonListBean<MailBean>>>get(Url.USER_CONTACT+id)
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

    public static void getContact(Object object,int id,JsonCallback<LzyResponse<CommonRecordBean<MailBean>>> callback){
        OkGo.<LzyResponse<CommonRecordBean<MailBean>>>get(Url.USER_CONTACT_ADD+"/"+id)
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

    public static void feedBack(Object object,int type,String content,String contact,JsonCallback<LzyResponse<Object>> callback){
        JSONObject params=new JSONObject();
        try {
            params.putOpt("type",type);
            params.putOpt("content",content);
            params.putOpt("contact",contact);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<LzyResponse<Object>>post(Url.FEED_BACK)
                .tag(object)
                .upJson(params)
                .execute(callback);
    }

    public static void changeLang(Object object,String lang,JsonCallback<LzyResponse<Object>> callback){
        JSONObject params=new JSONObject();
        try {
            params.putOpt("lang",lang);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<LzyResponse<Object>>put(Url.USER_LANG)
                .tag(object)
                .upJson(params)
                .execute(callback);
    }

    public static void getComment(Object object,JsonCallback<LzyResponse<ArrayList<PingjiaBean>>> callback){
        OkGo.<LzyResponse<ArrayList<PingjiaBean>>>get(Url.COMMENT)
                .tag(object)
                .execute(callback);
    }

    public static void getCommentInfo(Object object,String id,String uid,JsonCallback<LzyResponse<PingjiaInfoBean>> callback){
        OkGo.<LzyResponse<PingjiaInfoBean>>get(Url.COMMENT_INFO+id+"/comment/"+uid)
                .tag(object)
                .execute(callback);
    }

    public static void getCommentReply(Object object,String id,String uid,JsonCallback<LzyResponse<PingjiaReplyBean>> callback){
        OkGo.<LzyResponse<PingjiaReplyBean>>get(Url.COMMENT_INFO+id+"/comment/"+uid+"/reply")
                .tag(object)
                .execute(callback);
    }

    public static void postCommentReply(Object object,String id,String uid,String content,JsonCallback<LzyResponse<Object>> callback){
        JSONObject params=new JSONObject();
        try {
            params.putOpt("content",content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<LzyResponse<Object>>post(Url.COMMENT_INFO+id+"/comment/"+uid+"/reply")
                .tag(object)
                .upJson(params)
                .execute(callback);
    }


    public static void postReply(Object object,String id,String uid,String type,JsonCallback<LzyResponse<ReplyBean>> callback){
        OkGo.<LzyResponse<ReplyBean>>post(Url.COMMENT_INFO+id+"/comment/"+uid+"/reply/"+type)
                .tag(object)
                .execute(callback);
    }
}
