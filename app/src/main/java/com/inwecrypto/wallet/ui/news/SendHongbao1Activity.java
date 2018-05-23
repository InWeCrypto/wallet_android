package com.inwecrypto.wallet.ui.news;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.CountBean;
import com.inwecrypto.wallet.bean.HongbaoAuthBean;
import com.inwecrypto.wallet.bean.HongbaoGntBean;
import com.inwecrypto.wallet.bean.HongbaoMinGas;
import com.inwecrypto.wallet.bean.HongbaoOrderBean;
import com.inwecrypto.wallet.bean.MaxBean;
import com.inwecrypto.wallet.bean.ValueBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.MeApi;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.VerticalSwipeRefreshLayout;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.newneo.InputPassFragment;
import com.inwecrypto.wallet.ui.news.fragment.HongbaoWalletListFragment;
import com.inwecrypto.wallet.ui.wallet.activity.TokenTransferAccountsActivity;
import com.inwecrypto.wallet.ui.wallet.activity.TransferAccountsActivity;
import com.lzy.okgo.model.Response;
import com.xw.repo.BubbleSeekBar;

import net.qiujuer.genius.ui.widget.Button;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.IllegalFormatCodePointException;

import butterknife.BindView;
import ethmobile.EthCall;
import ethmobile.Ethmobile;
import ethmobile.Wallet;

/**
 * 作者：xiaoji06 on 2018/4/23 12:29
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class SendHongbao1Activity extends BaseActivity {


    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.asset)
    TextView assetTv;
    @BindView(R.id.assetfl)
    FrameLayout assetfl;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.tv_unit)
    TextView tvUnit;
    @BindView(R.id.et_num)
    EditText etNum;
    @BindView(R.id.max_red)
    TextView maxRed;
    @BindView(R.id.gas)
    TextView gas;
    @BindView(R.id.gasBar)
    BubbleSeekBar gasBar;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.wallet_list)
    LinearLayout walletList;
    @BindView(R.id.swipeRefersh)
    VerticalSwipeRefreshLayout swipeRefersh;

    private HongbaoGntBean hongbaoGntBean;

    private int index;

    private int walletIndex;

    private boolean isGetNum;

    private ArrayList<WalletBean> wallets = new ArrayList<>();
    private BigDecimal distance= Constant.high.subtract(Constant.low);
    private String max="10";
    private String nonce;
    private String oxPrice;
    private String oxGas;
    private HongbaoMinGas minGas;
    private BigDecimal min;

    private BigDecimal high;

    private boolean isUpGas;
    private String selectAddress;
    private String hongbaofei;
    private int hongbaoCount;
    private String preGas;
    private String id;

    @Override
    protected void getBundleExtras(Bundle extras) {
        isUpGas=extras.getBoolean("isUpGas",false);
        if (isUpGas){
            id=extras.getString("id");
            selectAddress=extras.getString("selectAddress");
            hongbaofei=extras.getString("hongbaofei");
            hongbaoCount=extras.getInt("hongbaoCount");
            hongbaoGntBean= (HongbaoGntBean) extras.getSerializable("gnt");
            preGas=extras.getString("preGas");
        }
    }

    @Override
    protected int setLayoutID() {
        return R.layout.send_hongbao_1_activity;
    }

    @Override
    protected void initView() {
        if (isUpGas){
            assetTv.setText(hongbaoGntBean.getName());
            tvUnit.setText(hongbaoGntBean.getName());
            etPrice.setText(hongbaofei);
            etNum.setText(hongbaoCount+"");
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_HONGBAO_REFERS));
                finish();
            }
        });

        assetfl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUpGas)return;
                Intent intent=new Intent(mActivity,HongbaoAssetActivity.class);
                keepTogo(intent);
            }
        });

        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()!=0){
                    if (null==hongbaoGntBean){
                        ToastUtil.show(getString(R.string.qingxianxuanzeethzichan));
                        etPrice.setText("");
                    }
                }
            }
        });

        etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()!=0){
                    if (null==hongbaoGntBean){
                        ToastUtil.show(getString(R.string.qingxianxuanzeethzichan));
                        etNum.setText("");
                    }
                }
            }
        });

        walletList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUpGas)return;
                if (isGetNum){
                    //弹出钱包选择框
                    FragmentManager fm = mActivity.getSupportFragmentManager();
                    HongbaoWalletListFragment walletlist = new HongbaoWalletListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("wallet", wallets);
                    walletlist.setArguments(bundle);
                    walletlist.show(fm, "list");
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null==hongbaoGntBean){
                    ToastUtil.show(getString(R.string.qingxianxuanzeethzichan));
                    return;
                }

                if (etPrice.getText().toString().length()==0){
                    ToastUtil.show(getString(R.string.qingtianxiehongboafafangjine));
                    return;
                }

                try{
                    if (new BigDecimal(etPrice.getText().toString()).floatValue()>new BigDecimal(wallets.get(walletIndex).getToken_num()).floatValue()){
                        ToastUtil.show(R.string.yuebuzu);
                        return;
                    }
                }catch (Exception e){
                    ToastUtil.show(getString(R.string.qingtianxiezhengqueshuliang));
                    return;
                }

                try{
                    if (Float.parseFloat(etPrice.getText().toString())<=0){
                        ToastUtil.show(getString(R.string.qingtianxiezhengqueshuliang));
                        return;
                    }
                }catch (Exception e){
                    ToastUtil.show(getString(R.string.qingtianxiezhengqueshuliang));
                    return;
                }

                if (etNum.getText().toString().length()==0){
                    ToastUtil.show(getString(R.string.qingtianxiehongbaofafanggeshu));
                    return;
                }

                try{
                    if (Integer.parseInt(etNum.getText().toString())<=0){
                        ToastUtil.show(getString(R.string.qingtianxiezhengqueshuliang));
                        return;
                    }
                }catch (Exception e){
                    ToastUtil.show(getString(R.string.qingtianxiezhengqueshuliang));
                    return;
                }

                if (new BigDecimal(etNum.getText().toString()).subtract(new BigDecimal(max)).floatValue()>0){
                    ToastUtil.show(getString(R.string.fasonghongbaoshuliangtaiduo));
                    return;
                }

                if (wallets.get(walletIndex).getType().endsWith(Constant.GUANCHA)){
                    ToastUtil.show(getString(R.string.bunengshiyongguanchaqianbao));
                    return;
                }

                oxPrice = "0x" + new BigInteger(new BigDecimal(etPrice.getText().toString()).multiply(AppUtil.decimal(hongbaoGntBean.getDecimals())).setScale(0, BigDecimal.ROUND_DOWN).toPlainString(), 10).toString(16);
                oxGas = "0x" + new BigInteger(new BigDecimal(gas.getText().toString()).multiply(Constant.pEther).divide(new BigDecimal(hongbaoGntBean.getGas()).divide(new BigDecimal(6),0, BigDecimal.ROUND_DOWN),0, BigDecimal.ROUND_DOWN).toPlainString(), 10).toString(16);
                //
                showFixLoading();
                //获取账户余额
                WalletApi.balance(mActivity,wallets.get(walletIndex).getAddress(), new JsonCallback<LzyResponse<ValueBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<ValueBean>> response) {
                        BigDecimal currentPrice = new BigDecimal(AppUtil.toD(response.body().data.getValue().replace("0x", "0")));
                        BigDecimal price = new BigDecimal(gas.getText().toString()).multiply(Constant.pEther);
                        if (currentPrice.subtract(price).doubleValue()>=0){

                            WalletApi.transactionCount(mActivity, wallets.get(walletIndex).getAddress(), new JsonCallback<LzyResponse<CountBean>>() {
                                @Override
                                public void onSuccess(Response<LzyResponse<CountBean>> response) {
                                    hideFixLoading();
                                    nonce = response.body().data.getCount();
                                    transfer();
                                }

                                @Override
                                public void onError(Response<LzyResponse<CountBean>> response) {
                                    super.onError(response);
                                    ToastUtil.show(getString(R.string.load_error));
                                    hideFixLoading();
                                }
                            });

                        }else {
                            ToastUtil.show(R.string.qianbaoyuebuzu);
                            return;
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<ValueBean>> response) {
                        super.onError(response);
                        ToastUtil.show(R.string.yuehuoqushibai);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        hideFixLoading();
                    }
                });

            }
        });

        min=Constant.low.setScale(8,BigDecimal.ROUND_DOWN);
        gasBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                gas.setText(new BigDecimal(progressFloat).divide(Constant.p100).multiply(distance).add(min).divide(Constant.pEther).setScale(8, BigDecimal.ROUND_HALF_DOWN).toPlainString());
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });
        gasBar.setProgress(10);

        swipeRefersh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefersh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        showFixLoading();
    }

    @Override
    protected void initData() {

        WalletApi.wallet(mActivity, new JsonCallback<LzyResponse<CommonListBean<WalletBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                wallets.clear();
                if (null != response.body().data.getList()) {
                    String walletsStr = App.get().getSp().getString(Constant.WALLETS, "").toLowerCase();
                    String wallets_beifen = App.get().getSp().getString(Constant.WALLETS_BEIFEN, "").toLowerCase();
                    String walletsZjc = App.get().getSp().getString(Constant.WALLETS_ZJC_BEIFEN, "").toLowerCase();
                    for (int i = 0; i < response.body().data.getList().size(); i++) {
                        if (walletsStr.contains(response.body().data.getList().get(i).getAddress().toLowerCase())) {
                            if (wallets_beifen.contains(response.body().data.getList().get(i).getAddress().toLowerCase()) || walletsZjc.contains(response.body().data.getList().get(i).getAddress().toLowerCase())) {
                                response.body().data.getList().get(i).setType(Constant.BEIFEN);
                            } else {
                                response.body().data.getList().get(i).setType(Constant.ZHENGCHANG);
                            }
                        } else {
                            response.body().data.getList().get(i).setType(Constant.GUANCHA);
                        }
                        if (response.body().data.getList().get(i).getCategory_id()==1){
                            wallets.add(response.body().data.getList().get(i));
                        }
                    }
                }

                if (isUpGas){
                    for (int i=0;i<wallets.size();i++){
                        if (wallets.get(i).getAddress().toLowerCase().equals(selectAddress.toLowerCase())){
                            //获取滑动条范围
                            setProgress();
                            setGnt();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.jiazaishibai));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (null==swipeRefersh)return;
                swipeRefersh.setRefreshing(false);
                hideFixLoading();
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if(event.getEventCode()==Constant.EVENT_HONGBAO_GNT){
            walletList.setVisibility(View.VISIBLE);
            hongbaoGntBean= (HongbaoGntBean) event.getData();
            //获取滑动条范围
            setProgress();
            setGnt();
        }

        if (event.getEventCode()==Constant.EVENT_HONGBAO_ADDRESS_GNT){
            setAddress(event.getKey1());
        }
    }

    private void setAddress(int position) {
        selectAddress=wallets.get(position).getAddress();
        address.setText(wallets.get(position).getAddress());
        num.setText(wallets.get(position).getToken_num()+ hongbaoGntBean.getName());
        walletIndex=position;
    }

    private void setGnt() {
        assetTv.setText(hongbaoGntBean.getName());
        tvUnit.setText(hongbaoGntBean.getName());
        EthCall call=new EthCall();
        try {
            String maxJson=call.redPacketMaxCount(App.isMain?Constant.MAIN_HONGBAO_ADDRESS:Constant.TEST_HONGBAO_ADDRESS);
            JSONObject maxRpc=new JSONObject(maxJson);
            ZixunApi.ethRpc(this, maxRpc, new JsonCallback<LzyResponse<String>>() {
                @Override
                public void onSuccess(Response<LzyResponse<String>> response) {
                    max=new BigDecimal(AppUtil.toD(response.body().data.replace("0x", "0"))).setScale(0, RoundingMode.DOWN).toPlainString();
                    if (App.get().isZh()){
                        maxRed.setText(Html.fromHtml("(最大可发送：<font color='#F9480E'>"+max+"个</font>)"));
                    }else {
                        maxRed.setText(Html.fromHtml("(Max: <font color='#F9480E'>"+max+"</font> Packet)"));
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        isGetNum=false;
        index=0;
        showFixLoading();
        //获取 token 余额
        getTokenNum(index);

    }

    private void setProgress() {
        min = Constant.GWEI_LOW.multiply(new BigDecimal(hongbaoGntBean.getGas()).divide(new BigDecimal(6),0, RoundingMode.DOWN));
        high = Constant.GWEI_HIGH.multiply(new BigDecimal(hongbaoGntBean.getGas()).divide(new BigDecimal(6),0, RoundingMode.DOWN));
        distance = high.subtract(min);

        ZixunApi.getMinGas(this, new JsonCallback<LzyResponse<HongbaoMinGas>>() {
            @Override
            public void onSuccess(Response<LzyResponse<HongbaoMinGas>> response) {
                minGas=response.body().data;
                if (new BigDecimal(minGas.getValue()).multiply(Constant.pEther).floatValue()>min.floatValue()){
                    min=new BigDecimal(minGas.getValue()).multiply(Constant.pEther);
                    distance= Constant.high.subtract(min);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (isUpGas){
                    float p=new BigDecimal(preGas).subtract(min.divide(Constant.pEther)).divide(distance.divide(Constant.pEther),2,RoundingMode.DOWN).multiply(new BigDecimal(100)).floatValue();
                    gasBar.setProgress(p);
                }else {
                    //设置 gas
                    gasBar.setProgress(10);
                }
            }
        });
    }

    private void getTokenNum(final int i) {
        final WalletBean walletBean=wallets.get(i);
        //请求用户资产
        WalletApi.balanceof(mActivity, hongbaoGntBean.getAddress(), walletBean.getAddress(), new JsonCallback<LzyResponse<ValueBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ValueBean>> response) {
                walletBean.setToken_num(new BigDecimal(AppUtil.toD(response.body().data.getValue().replace("0x", "0"))).divide(AppUtil.decimal(hongbaoGntBean.getDecimals())).setScale(4, BigDecimal.ROUND_DOWN).toString());
                walletBean.setToken_name(hongbaoGntBean.getName());
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<ValueBean>> response) {
                super.onCacheSuccess(response);
                try{
                    onSuccess(response);
                }catch (Exception e){
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                index++;
                if (index==wallets.size()){
                    //
                    Collections.sort(wallets, new Comparator<WalletBean>() {
                        @Override
                        public int compare(WalletBean a, WalletBean b) {
                            if (Float.parseFloat(null==a.getToken_num()?"0":a.getToken_num())>Float.parseFloat(null==b.getToken_num()?"0":b.getToken_num())){
                                return -1;
                            }else if (Float.parseFloat(null==a.getToken_num()?"0":a.getToken_num())<Float.parseFloat(null==b.getToken_num()?"0":b.getToken_num())){
                                return 1;
                            }else {
                                return 0;
                            }
                        }
                    });
                    for (int i=0; i<wallets.size();i++){
                        wallets.get(i).setSelect(false);
                        if (isUpGas){
                            if (wallets.get(i).getAddress().toLowerCase().equals(selectAddress.toLowerCase())){
                                walletIndex=i;
                            }
                        }
                    }
                    if (isUpGas){
                        address.setText(wallets.get(walletIndex).getAddress());
                        num.setText(wallets.get(walletIndex).getToken_num()+ hongbaoGntBean.getName());
                        wallets.get(walletIndex).setSelect(true);
                    }else {
                        address.setText(wallets.get(0).getAddress());
                        num.setText(wallets.get(0).getToken_num());
                        wallets.get(0).setSelect(true);
                        walletIndex=0;
                        isGetNum=true;
                    }
                    hideFixLoading();
                    return;
                }else {
                    getTokenNum(index);
                }
            }
        });

    }

    private void transfer() {
        //输入密码
        FragmentManager fm = getSupportFragmentManager();
        InputPassFragment input = new InputPassFragment();
        input.show(fm, "input");
        input.setOnNextListener(new InputPassFragment.OnNextInterface() {
            @Override
            public void onNext(final String passWord, final Dialog dialog) {
                if (passWord.length() == 0) {
                    ToastUtil.show(getString(R.string.qingshurumima));
                    return;
                }
                showFixLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String b = "";
                        final AccountManager accountManager = AccountManager.get(mActivity);
                        Account[] accounts = accountManager.getAccountsByType("com.inwecrypto.wallet");
                        for (int i = 0; i < accounts.length; i++) {
                            if (accounts[i].name.toLowerCase().equals(wallets.get(walletIndex).getAddress().toLowerCase())) {
                                //accountManager.getUserData(accounts[i], pass.getText().toString());
                                b = accountManager.getUserData(accounts[i], "wallet");
                                break;
                            }
                        }
                        Wallet wallet = null;
                        try {
                            wallet = Ethmobile.fromKeyStore(b, passWord);
                        } catch (Exception e) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(getString(R.string.mimacuowuqingchongshi));
                                    hideFixLoading();
                                }
                            });
                            return;
                        }
                        String data = "";
                        try {
                            //AppUtil.conver16HexStr
                            data = "0x" + wallet.approve(hongbaoGntBean.getAddress().toLowerCase()
                                    ,nonce
                                    ,App.isMain?Constant.MAIN_HONGBAO_ADDRESS:Constant.TEST_HONGBAO_ADDRESS
                                    ,oxPrice
                                    ,oxGas
                                    ,"0x" + new BigInteger(new BigDecimal(hongbaoGntBean.getGas()).setScale(0,BigDecimal.ROUND_DOWN).toPlainString(),10).toString(16));
                        } catch (Exception e) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(R.string.zhuanzhangshibaiqingchongshi);
                                    hideFixLoading();
                                }
                            });
                            return;
                        }
                        final String finalData = data;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideFixLoading();
                                getOrderInfo(finalData);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private void getOrderInfo(String data) {
        showFixLoading();
        ZixunApi.authRedbag(this
                , wallets.get(walletIndex).getAddress().toLowerCase()
                , hongbaoGntBean.getName()
                , etNum.getText().toString()
                , etPrice.getText().toString()
                , AppUtil.toD(nonce.replace("0x","0"))
                , gas.getText().toString()
                , isUpGas?id:"0"
                , hongbaoGntBean.getAddress().toLowerCase()
                , wallets.get(walletIndex).getAddress().toLowerCase()
                , App.isMain?Constant.MAIN_HONGBAO_ADDRESS:Constant.TEST_HONGBAO_ADDRESS
                , oxPrice
                , oxGas
                ,""
                , data
                , new JsonCallback<LzyResponse<HongbaoAuthBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<HongbaoAuthBean>> response) {
                        HongbaoAuthBean authBean=response.body().data;
                        Intent intent=new Intent(mActivity,SendHongbaoPendingActivity.class);
                        intent.putExtra("id",authBean.getId());
                        intent.putExtra("selectAddress",authBean.getRedbag_addr());
                        intent.putExtra("gntGas",authBean.getGnt_category().getGas());
                        intent.putExtra("gntName",authBean.getGnt_category().getName());
                        intent.putExtra("gntAddress",authBean.getGnt_category().getAddress());
                        intent.putExtra("gntDecimal",authBean.getGnt_category().getDecimals()+"");
                        intent.putExtra("redbagAddress",authBean.getRedbag_addr());
                        intent.putExtra("hongbaofei",etPrice.getText().toString());
                        intent.putExtra("hongbaoCount",Integer.parseInt(etNum.getText().toString()));
                        intent.putExtra("gnt",hongbaoGntBean);
                        intent.putExtra("preGas",gas.getText().toString());
                        finshTogo(intent);
                    }

                    @Override
                    public void onError(Response<LzyResponse<HongbaoAuthBean>> response) {
                        super.onError(response);
                        if (response.getException().getMessage().contains("nonce too low")){
                            Intent intent=new Intent(mActivity,SendHongbaoPendingActivity.class);
                            intent.putExtra("id",id);
                            intent.putExtra("selectAddress",selectAddress);
                            intent.putExtra("gntGas",hongbaoGntBean.getGas());
                            intent.putExtra("gntName",hongbaoGntBean.getName());
                            intent.putExtra("gntAddress",hongbaoGntBean.getAddress());
                            intent.putExtra("gntDecimal",hongbaoGntBean.getDecimals());
                            intent.putExtra("redbagAddress",selectAddress);
                            intent.putExtra("hongbaofei",etPrice.getText().toString());
                            intent.putExtra("hongbaoCount",Integer.parseInt(etNum.getText().toString()));
                            intent.putExtra("gnt",hongbaoGntBean);
                            intent.putExtra("preGas",gas.getText().toString());
                            finshTogo(intent);
                        }else {
                            ToastUtil.show(getString(R.string.shouquanshibai));
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        hideFixLoading();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_HONGBAO_REFERS));
    }
}
