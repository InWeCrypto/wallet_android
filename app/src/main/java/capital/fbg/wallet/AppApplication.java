package capital.fbg.wallet;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.PatchRequest;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import capital.fbg.wallet.bean.LoginBean;
import capital.fbg.wallet.common.Constant;
import capital.fbg.wallet.common.util.AppManager;
import capital.fbg.wallet.common.util.GsonUtils;
import capital.fbg.wallet.common.util.LogUtils;
import capital.fbg.wallet.common.util.SPUtils;
import capital.fbg.wallet.service.AutoUpdateService;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/7/14.
 */

public class AppApplication extends Application{

    private static AppApplication app;
    public static int UPDATA_TYPE=-1;
    public static boolean IS_FIRST=true;
    private SPUtils sp;
    private LoginBean loginBean;
    public static boolean isMain;

    @Override
    public void onCreate() {
        super.onCreate();
        this.app=this;
        sp=new SPUtils(this, Constant.SP_NAME);
        isMain=sp.getBoolean(Constant.NET,true);
        initHttp();
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType. E_UM_NORMAL);
        CrashReport.initCrashReport(getApplicationContext(), "383376bf9a", false);
        initCloudChannel(this);
    }

    /**
     * 初始化云推送通道
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        final CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                sp.putString(Constant.OPEN_ID,pushService.getDeviceId());
            }
            @Override
            public void onFailed(String errorCode, String errorMessage) {
            }
        });
    }


    private void initHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

//        //设置log信息
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("wallet");
//        //log打印级别，决定了log显示的详细程度
//        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
//        //log颜色级别，决定了log在控制台显示的颜色
//        loggingInterceptor.setColorLevel(Level.INFO);
//        builder.addInterceptor(loggingInterceptor);
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

    public static AppApplication get(){
        return app;
    }

    public LoginBean getLoginBean() {
        if (null==loginBean){
            String json=sp.getString(Constant.USER_INFO);
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
        return sp.getInt(Constant.UNIT_TYPE,1);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level>=ComponentCallbacks2.TRIM_MEMORY_MODERATE){
            stopService(new Intent(this, AutoUpdateService.class));
            AppManager.getAppManager().finishAllActivity();
            System.exit(0);
        }
    }
}
