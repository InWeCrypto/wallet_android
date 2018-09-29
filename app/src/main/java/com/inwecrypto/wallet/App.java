package com.inwecrypto.wallet;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.LocaleUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.inwecrypto.wallet.bean.LoginBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.SPUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.twitter.sdk.android.core.Twitter;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.wanjian.cockroach.Cockroach;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/7/14.
 */

public class App extends Application{

    private static App app;
    public static int UPDATA_TYPE=-1;
    public static boolean IS_FIRST=true;
    private SPUtils sp;
    private LoginBean loginBean;
    public static boolean isMain;
    private int defaultLangue=1;
    private boolean isLogin;

    public static final String APP_ID="wxd346a4033d5a09a3";
    public static IWXAPI api;

    @Override
    public void onCreate() {
        super.onCreate();
        this.app=this;

        //初始化Twitter
        Twitter.initialize(this);

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        UMConfigure.init(this,"5ad2169df43e481ae4000266","", UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        //初始化微信
        api = WXAPIFactory.createWXAPI(this,APP_ID,false);
        api.registerApp(APP_ID);

        //初始化SP
        sp=new SPUtils(this, Constant.SP_NAME);
        isMain=sp.getBoolean(Constant.NET,false);
        if (sp.getBoolean(Constant.UNIT_CHANGE,false)){
            if (isZh()){
                defaultLangue=1;
            }else {
                defaultLangue=2;
            }
        }

        if (null == App.get().getSp().getString(App.isMain?Constant.TOKEN:Constant.TEST_TOKEN) || "".equals(App.get().getSp().getString(App.isMain?Constant.TOKEN:Constant.TEST_TOKEN))){
            isLogin=false;
        }else {
            isLogin=true;
        }

        installCockroach();
        //初始化bugly
        CrashReport.initCrashReport(getApplicationContext(), Constant.CRASH_ID, false);
        //初始化网络请求
        initHttp();

        //初始化环形
        EMOptions options = new EMOptions();
        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        try {
            EMClient.getInstance().changeAppkey(
                    isMain?"1109180116115999#online":"1109180116115999#test");
        } catch (HyphenateException e) {
            e.printStackTrace();
        }

        if (!sp.getBoolean(Constant.LANGUE_CHANGE,false)){
            if (isLocle()){
                sp.putBoolean(Constant.IS_CHINESE,true);
                defaultLangue=1;
            }else {
                sp.putBoolean(Constant.IS_CHINESE,false);
                defaultLangue=2;
            }
        }
        Locale _UserLocale= LocaleUtils.getUserLocale(this);
        LocaleUtils.updateLocale(this, _UserLocale);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Locale _UserLocale=LocaleUtils.getUserLocale(this);
        //系统语言改变了应用保持之前设置的语言
        if (_UserLocale != null) {
            Locale.setDefault(_UserLocale);
            Configuration _Configuration = new Configuration(newConfig);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                _Configuration.setLocale(_UserLocale);
            } else {
                _Configuration.locale =_UserLocale;
            }
            getResources().updateConfiguration(_Configuration, getResources().getDisplayMetrics());
        }
    }

    public static App get(){
        return app;
    }

    public LoginBean getLoginBean() {
        if (null==loginBean){
            String json=sp.getString(Constant.USER_INFO,"{}");
            loginBean= GsonUtils.jsonToObj(json,LoginBean.class);
        }
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public SPUtils getSp() {
        return sp;
    }

    public int getUnit() {//1.人民币  2.美元
        return sp.getInt(Constant.UNIT_TYPE,defaultLangue);
    }


    /**
     * 初始化网络请求
     */
    private void initHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //设置log信息
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("wallet");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);

//        //方法一：信任所有证书,不安全有风险
//        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
//        //方法二：自定义信任规则，校验服务端证书
//        HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
//        //方法三：使用预埋证书，校验服务端证书（自签名证书）
//        HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
//        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
//        HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
//        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
//        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//        builder.hostnameVerifier(new SafeHostnameVerifier());

        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
//        HttpHeaders headers = new HttpHeaders();
//        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
//        headers.put("commonHeaderKey2", "commonHeaderValue2");
//        HttpParams params = new HttpParams();
//        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
//        params.put("commonParamsKey2", "这里支持中文参数");
        //-------------------------------------------------------------------------------------//

        OkGo.getInstance().init(this)//必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);                           //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//                .addCommonHeaders(headers)                      //全局公共头
//                .addCommonParams(params);                       //全局公共参数
    }


//
//    protected void setupLeakCanary() {
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        enabledStrictMode();
//        LeakCanary.install(this);
//    }

    private void installCockroach() {
        Cockroach.install(new Cockroach.ExceptionHandler() {

            // handlerException内部建议手动try{  你的异常处理逻辑  }catch(Throwable e){ } ，以防handlerException内部再次抛出异常，导致循环调用handlerException

            @Override
            public void handlerException(final Thread thread, final Throwable throwable) {
                //开发时使用Cockroach可能不容易发现bug，所以建议开发阶段在handlerException中用Toast谈个提示框，
                //由于handlerException可能运行在非ui线程中，Toast又需要在主线程，所以new了一个new Handler(Looper.getMainLooper())，
                //所以千万不要在下面的run方法中执行耗时操作，因为run已经运行在了ui线程中。
                //new Handler(Looper.getMainLooper())只是为了能弹出个toast，并无其他用途
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //建议使用下面方式在控制台打印异常，这样就可以在Error级别看到红色log
                            Log.e("AndroidRuntime", "--->CockroachException:" + thread + "<---", throwable);
                            BaseActivity activity= (BaseActivity) AppManager.getAppManager().currentActivity();
                            activity.hideFixLoading();
                            activity.hideLoading();
                            ToastUtil.show("Exception Happend\n" + thread + "\n" + throwable.toString());
//                          throw new RuntimeException("..."+(i++));
                        } catch (Throwable e) {

                        }
                    }
                });
            }
        });

    }

    public boolean isZh() {
        return sp.getBoolean(Constant.IS_CHINESE,true);
    }

    public boolean isLocle(){
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
