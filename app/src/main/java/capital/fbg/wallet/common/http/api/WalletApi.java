package capital.fbg.wallet.common.http.api;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;

import java.util.HashMap;

import capital.fbg.wallet.AppApplication;
import capital.fbg.wallet.bean.BpsBean;
import capital.fbg.wallet.bean.CategoryBean;
import capital.fbg.wallet.bean.CommonListBean;
import capital.fbg.wallet.bean.CommonRecordBean;
import capital.fbg.wallet.bean.CountBean;
import capital.fbg.wallet.bean.GasBean;
import capital.fbg.wallet.bean.GntBean;
import capital.fbg.wallet.bean.MessageBean;
import capital.fbg.wallet.bean.MinBlockBean;
import capital.fbg.wallet.bean.OrderBean;
import capital.fbg.wallet.bean.OrderInfoBean;
import capital.fbg.wallet.bean.TokenBean;
import capital.fbg.wallet.bean.TransferABIBean;
import capital.fbg.wallet.bean.TxHashBean;
import capital.fbg.wallet.bean.ValueBean;
import capital.fbg.wallet.bean.WalletBean;
import capital.fbg.wallet.bean.WalletCountBean;
import capital.fbg.wallet.common.Constant;
import capital.fbg.wallet.common.http.LzyResponse;
import capital.fbg.wallet.common.http.Url;
import capital.fbg.wallet.common.http.callback.JsonCallback;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class WalletApi {

    public static void wallet(Object object,JsonCallback<LzyResponse<CommonListBean<WalletBean>>> callback){
        OkGo.<LzyResponse<CommonListBean<WalletBean>>>get(Url.WALLET)
                .tag(object)
                .cacheKey(Constant.WALLETS+AppApplication.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    public static void wallet(Object object,int id,JsonCallback<LzyResponse<Object>> callback){
        OkGo.<LzyResponse<Object>>delete(Url.WALLET+"/"+id)
                .tag(object)
                .execute(callback);
    }

    public static void wallet(Object object,int category_id,String name,String address,JsonCallback<LzyResponse<CommonRecordBean<WalletBean>>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("category_id",category_id+"");
        params.put("name",name);
        params.put("address",address);
        OkGo.<LzyResponse<CommonRecordBean<WalletBean>>>post(Url.WALLET)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void walletCategory(Object object,JsonCallback<LzyResponse<CommonListBean<CategoryBean>>> callback){
        OkGo.<LzyResponse<CommonListBean<CategoryBean>>>get(Url.WALLET_CATEGORY)
                .tag(object)
                .cacheKey(Constant.WALLET_CATEGORY+AppApplication.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    public static void userGnt(Object object,int wallet_category_id,int wallet_id ,JsonCallback<LzyResponse<CommonListBean<GntBean>>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("wallet_category_id",""+wallet_category_id);
        params.put("wallet_id",""+wallet_id);
        OkGo.<LzyResponse<CommonListBean<GntBean>>>get(Url.USER_GNT)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void userGnt(Object object,int id,JsonCallback<LzyResponse<Object>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("id",id+"");
        OkGo.<LzyResponse<Object>>put(Url.USER_GNT+"/"+id)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void userGntDelete(Object object,int id,JsonCallback<LzyResponse<Object>> callback){
        OkGo.<LzyResponse<Object>>delete(Url.USER_GNT+"/"+id)
                .tag(object)
                .execute(callback);
    }

    public static void gntCategory(Object object,int walletId,int wallet_category_id,JsonCallback<LzyResponse<CommonListBean<GntBean>>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("wallet_id",walletId+"");
        params.put("wallet_category_id",wallet_category_id+"");
        OkGo.<LzyResponse<CommonListBean<GntBean>>>get(Url.GNT_CATEGORY)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void userGnt(Object object,int wallet_id,String json,JsonCallback<LzyResponse<Object>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("wallet_id",wallet_id+"");
        params.put("gnt_category_ids",json);
        OkGo.<LzyResponse<Object>>post(Url.USER_GNT)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void walletOrder(Object object,int wallet_id,String flag,JsonCallback<LzyResponse<CommonListBean<OrderBean>>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("wallet_id",wallet_id+"");
        params.put("flag",flag);
        OkGo.<LzyResponse<CommonListBean<OrderBean>>>get(Url.WALLET_ORDER)
                .tag(object)
                .cacheKey(Constant.WALLET_ORDER+wallet_id+flag+AppApplication.isMain)
                .params(params)
                .execute(callback);
    }

    public static void gas(Object object,JsonCallback<LzyResponse<GasBean>> callback){
        OkGo.<LzyResponse<GasBean>>get(Url.ETH_GASPRICE)
                .tag(object)
                .execute(callback);
    }


    public static void transactionCount(Object object,String address,JsonCallback<LzyResponse<CountBean>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("address",address);
        OkGo.<LzyResponse<CountBean>>post(Url.ETH_TRANSACTION_COUNT)
                .tag(object)
                .params(params)
                .execute(callback);
    }


    public static void sendRaw(Object object,String data,JsonCallback<LzyResponse<TxHashBean>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("data",data);
        OkGo.<LzyResponse<TxHashBean>>post(Url.ETH_SEND_RAW_TRANSACTION)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void transaction(Object object,String txHash,JsonCallback<LzyResponse<OrderInfoBean>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("txHash",txHash);
        OkGo.<LzyResponse<OrderInfoBean>>post(Url.ETH_TRANSACTION)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void balance(Object object,String address,JsonCallback<LzyResponse<ValueBean>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("address",address);
        OkGo.<LzyResponse<ValueBean>>post(Url.ETH_BALANCE)
                .tag(object)
                .cacheKey(Constant.BALANCE+address+AppApplication.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .params(params)
                .execute(callback);
    }

    public static void conversion(Object object,String json,JsonCallback<LzyResponse<CommonListBean<WalletCountBean>>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("wallet_ids",json);
        OkGo.<LzyResponse<CommonListBean<WalletCountBean>>>get(Url.CONVERSION)
                .tag(object)
                .cacheKey(Constant.CONVERSION+AppApplication.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .params(params)
                .execute(callback);
    }

    public static void conversionWallet(Object object,String id,JsonCallback<LzyResponse<CommonListBean<WalletCountBean>>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("wallet_ids",id);
        OkGo.<LzyResponse<CommonListBean<WalletCountBean>>>get(Url.CONVERSION)
                .tag(object)
                .cacheKey(Constant.CONVERSION+id+AppApplication.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .params(params)
                .execute(callback);
    }

    public static void conversion(Object object,int id,JsonCallback<LzyResponse<TokenBean>> callback){
        OkGo.<LzyResponse<TokenBean>>get(Url.CONVERSION+"/"+id)
                .tag(object)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    public static void conversionErrorCache(Object object,int id,JsonCallback<LzyResponse<TokenBean>> callback){
        OkGo.<LzyResponse<TokenBean>>get(Url.CONVERSION+"/"+id)
                .tag(object)
                .cacheKey(Url.CONVERSION+"/"+id+AppApplication.isMain)
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .execute(callback);
    }

    public static void walletOrder(Object object,int wallet_id
            ,String data
            ,String pay_address
            ,String receive_address
            ,String remark
            ,String fee
            ,String handle_fee
            ,String flag,JsonCallback<LzyResponse<Object>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("wallet_id",wallet_id+"");
        params.put("data",data);
        params.put("pay_address",pay_address);
        params.put("receive_address",receive_address);
        params.put("remark",remark);
        params.put("fee",fee);
        params.put("handle_fee",handle_fee);
        params.put("flag",flag);
        OkGo.<LzyResponse<Object>>post(Url.WALLET_ORDER)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void message(Object object,int type,int page,int per_page,JsonCallback<LzyResponse<CommonListBean<MessageBean>>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("type",type+"");
        params.put("page",page+"");
        params.put("per_page",per_page+"");
        OkGo.<LzyResponse<CommonListBean<MessageBean>>>get(Url.MESSAGE)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void readMessage(Object object,int id,JsonCallback<LzyResponse<Object>> callback){
        OkGo.<LzyResponse<Object>>put(Url.MESSAGE+"/"+id)
                .tag(object)
                .execute(callback);
    }

    public static void deleteMessage(Object object,int id,JsonCallback<LzyResponse<Object>> callback){
        OkGo.<LzyResponse<Object>>delete(Url.MESSAGE+"/"+id)
                .tag(object)
                .execute(callback);
    }

    public static void transferABI(Object object,String contract,String to,String value,JsonCallback<LzyResponse<TransferABIBean>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("contract",contract);
        params.put("to",to);
        params.put("value",value);
        OkGo.<LzyResponse<TransferABIBean>>post(Url.TRANSFERABI)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void balanceof(Object object,String contract,String address, JsonCallback<LzyResponse<ValueBean>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("contract",contract);
        params.put("address",address);
        OkGo.<LzyResponse<ValueBean>>post(Url.BALANCEOF)
                .tag(object)
                .cacheKey(Constant.BALANCEOF+contract+address)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .params(params)
                .execute(callback);
    }

    public static void mimBlock(Object object,JsonCallback<LzyResponse<MinBlockBean>> callback){
        OkGo.<LzyResponse<MinBlockBean>>get(Url.MIN_BLOCK)
                .tag(object)
                .execute(callback);
    }

    public static void blockNumber(Object object,JsonCallback<LzyResponse<ValueBean>> callback){
        OkGo.<LzyResponse<ValueBean>>post(Url.EXTEND_BLOCKNUMBER)
                .tag(object)
                .execute(callback);
    }

    public static void blockPerSecond(Object object,JsonCallback<LzyResponse<BpsBean>> callback){
        OkGo.<LzyResponse<BpsBean>>post(Url.BLOCK_PER_SECOND)
                .tag(object)
                .execute(callback);
    }

}
