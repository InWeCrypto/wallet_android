package com.inwecrypto.wallet.common.http.api;

import com.lzy.okgo.OkGo;

import java.util.HashMap;

import com.inwecrypto.wallet.bean.LoginBean;
import com.inwecrypto.wallet.bean.UserInfoBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class UserApi {

    public static void login(Object object,String openId, JsonCallback<LzyResponse<LoginBean>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("open_id",openId);
        OkGo.<LzyResponse<LoginBean>>post(Url.AUTH)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void register(Object object,String appId, String secret,String jsCode,String grantType,JsonCallback<LzyResponse<LoginBean>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("app_id",appId);
        params.put("secret",secret);
        params.put("js_code",jsCode);
        params.put("grant_type",grantType);
        OkGo.<LzyResponse<LoginBean>>post(Url.AUTH)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void userInfo(Object object, JsonCallback<LzyResponse<UserInfoBean>> callback){
        OkGo.<LzyResponse<UserInfoBean>>get(Url.USER_INFO)
                .tag(object)
                .execute(callback);
    }
}
