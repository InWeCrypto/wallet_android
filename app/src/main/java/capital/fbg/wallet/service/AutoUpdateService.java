package capital.fbg.wallet.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import capital.fbg.wallet.AppApplication;
import capital.fbg.wallet.bean.CommonListBean;
import capital.fbg.wallet.bean.LocalMarketBean;
import capital.fbg.wallet.bean.LocalTokenBean;
import capital.fbg.wallet.bean.LocalWalletBean;
import capital.fbg.wallet.bean.LocalWalletCountBean;
import capital.fbg.wallet.bean.MarkeListBean;
import capital.fbg.wallet.bean.TokenBean;
import capital.fbg.wallet.bean.WalletBean;
import capital.fbg.wallet.bean.WalletCountBean;
import capital.fbg.wallet.common.Constant;
import capital.fbg.wallet.common.http.LzyResponse;
import capital.fbg.wallet.common.http.Url;
import capital.fbg.wallet.common.http.api.MarketApi;
import capital.fbg.wallet.common.http.api.WalletApi;
import capital.fbg.wallet.common.http.callback.JsonCallback;
import capital.fbg.wallet.common.util.AppUtil;
import capital.fbg.wallet.common.util.GsonUtils;
import capital.fbg.wallet.common.util.LogUtils;
import capital.fbg.wallet.event.BaseEventBusBean;
import capital.fbg.wallet.event.RefreshEvent;

import static capital.fbg.wallet.common.Constant.EVENT_MAIN_PRICE;

/**
 * Created by Administrator on 2017/8/20.
 * 功能描述：
 * 版本：@version
 */

public class AutoUpdateService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AutoUpdateService(String name) {
        super(name);
    }

    public AutoUpdateService(){
        super("wallet service");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        update();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (AppApplication.IS_FIRST){
            AppApplication.IS_FIRST=false;
            return super.onStartCommand(intent, flags, startId);
        }
        if (AppApplication.UPDATA_TYPE==-1){
            AppApplication.UPDATA_TYPE=1;
        }else if (AppApplication.UPDATA_TYPE==1){
            AppApplication.UPDATA_TYPE=2;
        }else {
            AppApplication.UPDATA_TYPE=1;
        }
        OkGo.getInstance().cancelTag(this);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtMillis = SystemClock.elapsedRealtime() + 30 * 1000;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pendingIntent);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis, pendingIntent);

        return super.onStartCommand(intent, flags, startId);
    }

    private void update() {
        if (AppApplication.UPDATA_TYPE==1){
            try {
                okhttp3.Response response = OkGo.<LzyResponse<CommonListBean<MarkeListBean>>>get(Url.MARKET_CATEGORY)
                        .headers("ct", AppApplication.get().getSp().getString(Constant.TOKEN,""))
                        .tag(this)
                        .cacheKey(Constant.MARKET+AppApplication.isMain)
                        .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                        .execute();
                if (response.code()==200){
                    String body=response.body().string().toString();
                    if (body.contains("4000")){
                        EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_MARKET));
                    };
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                HashMap<String,TokenBean.ListBean> totleGnt=new HashMap<>();
                okhttp3.Response response = OkGo.<LzyResponse<CommonListBean<WalletBean>>>get(Url.WALLET)
                        .headers("ct", AppApplication.get().getSp().getString(Constant.TOKEN,""))
                        .tag(this)
                        .cacheKey(Constant.WALLETS+AppApplication.isMain)
                        .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                        .execute();
                if (response.code()==200) {
                    String body = response.body().string().toString();
                    if (body.contains("4000")) {
                        LocalWalletBean localMarketBean = GsonUtils.jsonToObj(body, LocalWalletBean.class);
                        StringBuilder sb = new StringBuilder();
                        sb.append("[");
                        for (WalletBean wa : localMarketBean.data.getList()) {
                            sb.append(wa.getId() + ",");
                        }
                        if(sb.length()!=1){
                            sb.delete(sb.length() - 1, sb.length());
                        }
                        sb.append("]");
                        HashMap<String, String> params = new HashMap<>();
                        params.put("wallet_ids", sb.toString());

                        okhttp3.Response priceResponse = OkGo.<LzyResponse<CommonListBean<WalletCountBean>>>get(Url.CONVERSION)
                                .tag(this)
                                .cacheKey(Constant.CONVERSION+AppApplication.isMain)
                                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                                .headers("ct", AppApplication.get().getSp().getString(Constant.TOKEN,""))
                                .params(params)
                                .execute();

                        if (priceResponse.code() == 200) {
                            String priceBody = priceResponse.body().string().toString();
                            if (priceBody.contains("4000")) {
                                LocalWalletCountBean localWalletBean = GsonUtils.jsonToObj(priceBody, LocalWalletCountBean.class);
                                BigDecimal pEther= new java.math.BigDecimal("1000000000000000000");
                                ArrayList<WalletCountBean> walletCount = localWalletBean.data.getList();
                                for (WalletCountBean count : walletCount) {
                                    //请求代币列表
                                    okhttp3.Response tokenResponse = OkGo.<LzyResponse<TokenBean>>get(Url.CONVERSION + "/" + count.getId())
                                            .tag(this)
                                            .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                                            .headers("ct", AppApplication.get().getSp().getString(Constant.TOKEN,""))
                                            .execute();
                                    if (tokenResponse.code() == 200) {
                                        String tokenBody = tokenResponse.body().string().toString();
                                        if (tokenBody.contains("4000")) {
                                            LocalTokenBean localTokenBean = GsonUtils.jsonToObj(tokenBody, LocalTokenBean.class);
                                            if (null != localTokenBean.data.getList()) {
                                                for (TokenBean.ListBean gnt : localTokenBean.data.getList()) {
                                                    if (null != totleGnt.get(gnt.getName())) {
                                                        TokenBean.ListBean bfGnt = totleGnt.get(gnt.getName());

                                                        BigDecimal secPrice = new BigDecimal(AppUtil.toD(gnt.getBalance().replace("0x", "0")));

                                                        gnt.setBalance(secPrice.divide(pEther).add(new BigDecimal(bfGnt.getBalance())).toString());
                                                        totleGnt.put(gnt.getName(), gnt);
                                                    } else {
                                                        BigDecimal currentPrice = new BigDecimal(AppUtil.toD(gnt.getBalance().replace("0x", "0")));
                                                        gnt.setBalance(currentPrice.divide(pEther).toString());
                                                        totleGnt.put(gnt.getName(), gnt);
                                                    }
                                                }
                                            }
                                            ;
                                        }
                                    }
                                }

                                TokenBean tokenBean=new TokenBean();
                                ArrayList<TokenBean.ListBean> data=new ArrayList<>();
                                Iterator iter = totleGnt.entrySet().iterator();

                                while (iter.hasNext()) {
                                    Map.Entry entry = (Map.Entry) iter.next();
                                    TokenBean.ListBean val = (TokenBean.ListBean) entry.getValue();
                                    data.add(val);
                                }
                                tokenBean.setList(data);
                                //将数据保存到sp中
                                if (AppApplication.isMain){
                                    AppApplication.get().getSp().putString(Constant.TOTOLE_GNT, GsonUtils.objToJson(tokenBean));
                                }else {
                                    AppApplication.get().getSp().putString(Constant.TOTOLE_GNT_TEST, GsonUtils.objToJson(tokenBean));
                                }
                                EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_TOTLE_PRICE_SERVICE));
                                ;
                            }
                        }
                        ;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        LogUtils.e("service","停止更新服务");
    }
}
