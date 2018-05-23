package com.inwecrypto.wallet.common.http.api;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.bean.ClaimUtxoBean;
import com.inwecrypto.wallet.bean.HongbaoOrderBean;
import com.inwecrypto.wallet.bean.IcoGas;
import com.inwecrypto.wallet.bean.NeoOderBean;
import com.inwecrypto.wallet.bean.NewNeoGntInfoBean;
import com.inwecrypto.wallet.bean.UtxoBean;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.inwecrypto.wallet.bean.BpsBean;
import com.inwecrypto.wallet.bean.CategoryBean;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.CommonRecordBean;
import com.inwecrypto.wallet.bean.CountBean;
import com.inwecrypto.wallet.bean.GasBean;
import com.inwecrypto.wallet.bean.GntBean;
import com.inwecrypto.wallet.bean.MessageBean;
import com.inwecrypto.wallet.bean.MinBlockBean;
import com.inwecrypto.wallet.bean.OrderBean;
import com.inwecrypto.wallet.bean.OrderInfoBean;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.bean.TransferABIBean;
import com.inwecrypto.wallet.bean.TxHashBean;
import com.inwecrypto.wallet.bean.ValueBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.bean.WalletCountBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class WalletApi {

    public static void wallet(Object object,JsonCallback<LzyResponse<CommonListBean<WalletBean>>> callback){
        OkGo.<LzyResponse<CommonListBean<WalletBean>>>get(Url.WALLET)
                .tag(object)
                .cacheKey(Constant.WALLETS+ App.isMain+(null==App.get().getLoginBean()?App.get().getSp().getString(Constant.LOGIN_NAME+App.isMain):App.get().getLoginBean().getEmail()))
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    public static void addWallet(Object object,JsonCallback<LzyResponse<CommonListBean<WalletBean>>> callback){
        OkGo.<LzyResponse<CommonListBean<WalletBean>>>get(Url.WALLET)
                .tag(object)
                .cacheKey(Constant.WALLETS+"zixun"+ App.isMain+(null==App.get().getLoginBean()?"":App.get().getLoginBean().getEmail()))
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .execute(callback);
    }

    public static void wallet(Object object,int id,JsonCallback<LzyResponse<Object>> callback){
        OkGo.<LzyResponse<Object>>delete(Url.WALLET+"/"+id)
                .tag(object)
                .execute(callback);
    }

    public static void wallet(Object object,int category_id,String name,String address,String hashAddress,JsonCallback<LzyResponse<CommonRecordBean<WalletBean>>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("category_id",category_id+"");
        params.put("name",name);
        params.put("address",address);
        params.put("address_hash160",hashAddress);
        OkGo.<LzyResponse<CommonRecordBean<WalletBean>>>post(Url.WALLET)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void walletCategory(Object object,JsonCallback<LzyResponse<CommonListBean<CategoryBean>>> callback){
        OkGo.<LzyResponse<CommonListBean<CategoryBean>>>get(Url.WALLET_CATEGORY)
                .tag(object)
                .cacheKey(Constant.WALLET_CATEGORY+ App.isMain)
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

    public static void walletOrder(Object object,int page,int wallet_id,String flag,String asset_id,JsonCallback<LzyResponse<CommonListBean<OrderBean>>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("wallet_id",wallet_id+"");
        params.put("flag",flag);
        params.put("asset_id",asset_id);
        params.put("page",page+"");
        OkGo.<LzyResponse<CommonListBean<OrderBean>>>get(Url.WALLET_ORDER)
                .tag(object)
                .cacheKey(Constant.WALLET_ORDER+wallet_id+flag+asset_id+ App.isMain)
                .params(params)
                .execute(callback);
    }

    public static void neoWalletOrder(Object object,int page,int wallet_id,String flag,String asset,JsonCallback<LzyResponse<NeoOderBean>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("wallet_id",wallet_id+"");
        params.put("flag",flag);
        params.put("asset_id",asset);
        params.put("page",page+"");
        OkGo.<LzyResponse<NeoOderBean>>get(Url.WALLET_ORDER)
                .tag(object)
                .cacheKey(Constant.WALLET_ORDER+wallet_id+flag+asset+ App.isMain)
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
                .cacheKey(Constant.BALANCE+address+ App.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .params(params)
                .execute(callback);
    }

    public static void conversion(Object object,String json,JsonCallback<LzyResponse<CommonListBean<WalletCountBean>>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("wallet_ids",json);
        OkGo.<LzyResponse<CommonListBean<WalletCountBean>>>get(Url.CONVERSION)
                .tag(object)
                .cacheKey(Constant.CONVERSION+ App.isMain)
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .params(params)
                .execute(callback);
    }

    public static void conversionWallet(Object object,String id,JsonCallback<LzyResponse<CommonListBean<WalletCountBean>>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("wallet_ids",id);
        OkGo.<LzyResponse<CommonListBean<WalletCountBean>>>get(Url.CONVERSION)
                .tag(object)
                .cacheKey(Constant.CONVERSION+id+ App.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .params(params)
                .execute(callback);
    }

    public static void conversion(Object object,int id,JsonCallback<LzyResponse<TokenBean>> callback){
        OkGo.<LzyResponse<TokenBean>>get(Url.CONVERSION+"/"+id)
                .tag(object)
                .cacheKey(Url.CONVERSION+"/"+id+ App.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    public static void conversionErrorCache(Object object,int id,JsonCallback<LzyResponse<TokenBean>> callback){
        OkGo.<LzyResponse<TokenBean>>get(Url.CONVERSION+"/"+id)
                .tag(object)
                .cacheKey(Url.CONVERSION+"/"+id+ App.isMain+(null==App.get().getLoginBean()?"":App.get().getLoginBean().getEmail()))
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
            ,String flag
            ,String asset_id
            ,JsonCallback<LzyResponse<HongbaoOrderBean>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("wallet_id",wallet_id+"");
        params.put("data",data);
        params.put("pay_address",pay_address);
        params.put("receive_address",receive_address);
        params.put("remark",remark);
        params.put("fee",fee);
        params.put("handle_fee",handle_fee);
        params.put("asset_id",asset_id);
        params.put("flag",flag);
        OkGo.<LzyResponse<HongbaoOrderBean>>post(Url.WALLET_ORDER)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void neoWalletOrder(Object object
            ,int wallet_id
            ,String data
            ,String pay_address
            ,String receive_address
            ,String remark
            ,String fee
            ,String handle_fee
            ,String flag
            ,String trade_no
            ,String asset_id
            ,JsonCallback<LzyResponse<Object>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("wallet_id",wallet_id+"");
        params.put("data",data);
        params.put("pay_address",pay_address);
        params.put("receive_address",receive_address);
        params.put("remark",remark);
        params.put("fee",fee);
        params.put("handle_fee",handle_fee);
        params.put("flag",flag);
        params.put("trade_no",trade_no);
        params.put("asset_id",asset_id);
        OkGo.<LzyResponse<Object>>post(Url.WALLET_ORDER)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void neoIcoWalletOrder(Object object
            ,int wallet_id
            ,String data
            ,String pay_address
            ,String receive_address
            ,String remark
            ,String fee
            ,String handle_fee
            ,String flag
            ,String trade_no
            ,String asset_id
            ,JsonCallback<LzyResponse<Object>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("wallet_id",wallet_id+"");
        params.put("data",data);
        params.put("pay_address",pay_address);
        params.put("receive_address",receive_address);
        params.put("remark",remark);
        params.put("fee",fee);
        params.put("handle_fee",handle_fee);
        params.put("flag",flag);
        params.put("trade_no",trade_no);
        params.put("asset_id",asset_id);
        params.put("is_token","1");
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

    public static void balanceOfToken(Object object,String contract,String address, JsonCallback<LzyResponse<ValueBean>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("contract",contract);
        params.put("address",address);
        OkGo.<LzyResponse<ValueBean>>post(Url.BALANCEOF)
                .tag(object)
                .cacheKey(Constant.BALANCEOF+contract+address)
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
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

    public static void getUtxo(Object object,String address,String type, JsonCallback<LzyResponse<UtxoBean>> callback){

        OkGo.<LzyResponse<UtxoBean>>get(Url.GET_NEO_UTXO+address+"&type="+type)
                .tag(object)
                .execute(callback);
    }

    public static void getClaimUtxo(Object object, String address, JsonCallback<LzyResponse<ClaimUtxoBean>> callback){

        OkGo.<LzyResponse<ClaimUtxoBean>>get(Url.GET_CLAIM_UTXO+address)
                .tag(object)
                .execute(callback);
    }

    public static void getNeoGntInfo(Object object, String address,String wallet, JsonCallback<LzyResponse<NewNeoGntInfoBean>> callback){
        OkGo.<LzyResponse<NewNeoGntInfoBean>>get(Url.GET_NEO_GNT_INFO+address.replace("0x","")+"&wallet="+wallet)
                .tag(object)
                .cacheKey(Url.GET_NEO_GNT_INFO+"/"+address+wallet+ App.isMain)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(callback);
    }

    public static void getIcoGas(Object object, String address, JsonCallback<LzyResponse<IcoGas>> callback){

        OkGo.<LzyResponse<IcoGas>>get(Url.GET_NEO_ICO_GAS+address)
                .tag(object)
                .execute(callback);
    }

    public static void getNep5Gas(Object object
            , String treaty_address
            , String from_address
            , String to_address
            , String amount
            , JsonCallback<LzyResponse<IcoGas>> callback){
        HashMap<String,String> params=new HashMap<>();
        params.put("treaty_address",treaty_address.replace("0x",""));
        params.put("from_address",from_address);
        params.put("to_address",to_address);
        params.put("amount",amount);

        OkGo.<LzyResponse<IcoGas>>post(Url.GET_NEO_NEP5_GAS)
                .tag(object)
                .params(params)
                .execute(callback);
    }

    public static void setSort(Object object, ArrayList<TokenBean.ListBean> neoList, JsonCallback<LzyResponse<Object>> callback){
        JSONObject params=new JSONObject();
        JSONArray list=new JSONArray();
        try {
            for (TokenBean.ListBean entry : neoList) {
                list.put(entry.getName());
            }
            params.putOpt("list",list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<LzyResponse<Object>>put(Url.GNT_SORT)
                .tag(object)
                .upJson(params)
                .execute(callback);
    }


}
