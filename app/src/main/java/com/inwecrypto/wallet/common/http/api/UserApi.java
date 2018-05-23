package com.inwecrypto.wallet.common.http.api;

import com.inwecrypto.wallet.bean.ArticleListBean;
import com.inwecrypto.wallet.bean.ProjectListBean;
import com.lzy.okgo.OkGo;

import java.util.HashMap;

import com.inwecrypto.wallet.bean.LoginBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class UserApi {

    /**
     * 登录
     * @param object
     * @param email
     * @param password
     * @param callback
     */
    public static void login(Object object,String email,String password, JsonCallback<LzyResponse<LoginBean>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("email",email);
        params.put("password",password);
        OkGo.<LzyResponse<LoginBean>>post(Url.LOGIN)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    /**
     * 获取验证码
     * @param object
     * @param email
     * @param callback
     */
    public static void getCode(Object object,int type,String email, JsonCallback<LzyResponse<Object>> callback){
        JSONObject params=new JSONObject();
        try {
            params.putOpt("type",type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<LzyResponse<Object>>post(Url.SEND_CODE+email)
                .tag(object)
                .upJson(params)
                .execute(callback);
    }

    /**
     * 注册用户
     * @param object
     * @param code
     * @param email
     * @param name
     * @param password
     * @param password_confirmation
     * @param callback
     */
    public static void register(Object object,String code, String email,String name,String password,String password_confirmation,JsonCallback<LzyResponse<LoginBean>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("code",code);
        params.put("email",email);
        params.put("name",name);
        params.put("password",password);
        params.put("password_confirmation",password_confirmation);
        OkGo.<LzyResponse<LoginBean>>post(Url.REGISTER)
                .tag(object)
                .params(params)
                .execute(callback);
    }


    /**
     * 忘记密码
     * @param object
     * @param code
     * @param email
     * @param password
     * @param password_confirmation
     * @param callback
     */
    public static void forgotPass(Object object,String code, String email,String password,String password_confirmation,JsonCallback<LzyResponse<Object>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("code",code);
        params.put("email",email);
        params.put("password",password);
        params.put("password_confirmation",password_confirmation);
        OkGo.<LzyResponse<Object>>post(Url.FROGOT_PASSWORD)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    /**
     * 修改用户信息
     * @param object
     * @param img
     * @param name
     * @param callback
     */
    public static void setUserInfo(Object object,String img,String name,JsonCallback<LzyResponse<Object>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("img",img);
        params.put("name",name);
        OkGo.<LzyResponse<Object>>put(Url.USER)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    /**
     * 重置密码
     * @param object
     * @param password_old
     * @param password
     * @param password_confirmation
     * @param callback
     */
    public static void resetPassword(Object object,String password_old, String password,String password_confirmation,JsonCallback<LzyResponse<Object>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("password_old",password_old);
        params.put("password",password);
        params.put("password_confirmation",password_confirmation);
        OkGo.<LzyResponse<Object>>put(Url.RESET_PASSWORD)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    /**
     * 获取收藏列表
     * @param object
     * @param page
     * @param callback
     */
    public static void getFavorite(Object object,int page,JsonCallback<LzyResponse<ArticleListBean>> callback){
        OkGo.<LzyResponse<ArticleListBean>>get(Url.USER_FAVORITE+page)
                .tag(object)
                .execute(callback);
    }

    /**
     * 取消或收藏项目
     * @param object
     * @param id
     * @param isCollect
     * @param callback
     */
    public static void collect(Object object,String id,boolean isCollect,JsonCallback<LzyResponse<Object>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("isCollect",isCollect+"");
        OkGo.<LzyResponse<Object>>put(Url.BASE_CATEGORY +id+"/collect")
                .tag(object)
                .params(params)
                .execute(callback);
    }

    /**
     * 获取行情提醒
     * @param object
     * @param page
     * @param callback
     */
    public static void getMarketTip(Object object,int page,JsonCallback<LzyResponse<ProjectListBean>> callback){
        OkGo.<LzyResponse<ProjectListBean>>get(Url.MARKET_TIP+page)
                .tag(object)
                .execute(callback);
    }

    /**
     * 取消或添加行情提醒
     * @param object
     * @param id
     * @param is_market_follow
     * @param callback
     */
    public static void follow(Object object,String id,boolean is_market_follow,String market_hige,String market_lost,JsonCallback<LzyResponse<Object>> callback){

        JSONObject params=new JSONObject();

        try {
            params.putOpt("is_market_follow",is_market_follow);
            if (is_market_follow){
                params.putOpt("market_hige",market_hige);
                params.putOpt("market_lost",market_lost);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<LzyResponse<Object>>put(Url.BASE_CATEGORY +id+"/follow")
                .tag(object)
                .upJson(params)
                .execute(callback);
    }
}
