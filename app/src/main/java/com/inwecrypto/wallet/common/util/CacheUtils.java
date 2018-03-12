package com.inwecrypto.wallet.common.util;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.bean.TotlePriceBean;
import com.inwecrypto.wallet.common.Constant;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.db.CacheManager;

/**
 * 作者：xiaoji06 on 2018/3/8 10:44
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class CacheUtils {

    public static <T> T getCache(String key){
        CacheEntity<T> totlePriceCache=(CacheEntity<T>) CacheManager.getInstance().get(key);
        if (null!=totlePriceCache&&null!=totlePriceCache.getData()){
            return totlePriceCache.getData();
        }else {
            return null;
        }
    }

    public static <T> void setCache(String key,T t){
        CacheEntity<T> cacheEntity=new CacheEntity<>();
        cacheEntity.setData(t);
        if (null==CacheManager.getInstance().get(key)){
            cacheEntity.setKey(key);
            CacheManager.getInstance().insert(cacheEntity);
        }else {
            CacheManager.getInstance().replace(key,cacheEntity);
        }
    };

}
