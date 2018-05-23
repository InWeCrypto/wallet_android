package com.inwecrypto.wallet.ui.news;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.CountBean;
import com.inwecrypto.wallet.bean.HongbaoFeeBean;
import com.inwecrypto.wallet.bean.HongbaoMinGas;
import com.inwecrypto.wallet.bean.HongbaoOrderBean;
import com.inwecrypto.wallet.bean.ValueBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.bean.WalletCountBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.VerticalSwipeRefreshLayout;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.newneo.InputPassFragment;
import com.inwecrypto.wallet.ui.news.fragment.HongbaoWalletListFragment;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import ethmobile.EthCall;
import ethmobile.Ethmobile;
import ethmobile.Wallet;

/**
 * 作者：xiaoji06 on 2018/4/23 12:29
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class SendHongbao2Activity extends BaseActivity {

    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.fei)
    TextView fei;
    @BindView(R.id.gasBar)
    BubbleSeekBar gasBar;
    @BindView(R.id.low)
    TextView low;
    @BindView(R.id.high)
    TextView high;
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

    private ArrayList<WalletCountBean> walletCountBeans = new ArrayList<>();

    private boolean isGetNum;
    private String nonce;

    private float position;

    private ArrayList<WalletBean> wallet = new ArrayList<>();

    private BigDecimal distance;

    private int walletIndex;

    private String selectAddress;
    private String gntGas;
    private String gntName;
    private String gntAddress;
    private String gntDecimal;
    private String redbagAddress;
    private String hongbaofei;
    private int hongbaoCount;
    private boolean isUpGas;
    private String preGas;
    private String preWallet;

    private String oxHongbaofei;
    private String oxCount;
    private String oxPrice;
    private String oxGas;
    private BigDecimal positionMin;
    private BigDecimal positionMax;
    private BigDecimal tLow;
    private BigDecimal tHigh;
    private String redbgId;
    private String id;

    private HongbaoMinGas minGas;
    private BigDecimal min;
    private BigDecimal max;


    @Override
    protected void getBundleExtras(Bundle extras) {
        id=extras.getString("id");
        selectAddress=extras.getString("selectAddress");
        gntGas=extras.getString("gntGas");
        gntName=extras.getString("gntName");
        gntAddress=extras.getString("gntAddress");
        gntDecimal=extras.getString("gntDecimal");
        redbagAddress=extras.getString("redbagAddress");
        hongbaofei=extras.getString("hongbaofei");
        hongbaoCount=extras.getInt("hongbaoCount");

        oxHongbaofei="0x" + new BigInteger(new BigDecimal(hongbaofei).multiply(AppUtil.decimal(gntDecimal)).setScale(0,RoundingMode.DOWN).toPlainString(), 10).toString(16);
        oxCount="0x" + new BigInteger(new BigDecimal(hongbaoCount).toPlainString(), 10).toString(16);

        isUpGas=extras.getBoolean("isUpGas",false);
        if (isUpGas){
            redbgId="0x"+new BigInteger(extras.getString("redbgId"),10).toString(16);
            nonce="0x"+new BigInteger(extras.getString("nonce"),10).toString(16);
            preGas=extras.getString("preGas");
            preWallet=extras.getString("preWallet");
            selectAddress=preWallet;
        }
    }

    @Override
    protected int setLayoutID() {
        return R.layout.send_hongbao_2_activity;
    }

    @Override
    protected void initView() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_HONGBAO_REFERS));
                finish();
            }
        });

        walletList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUpGas)return;
                if (isGetNum) {
                    //弹出钱包选择框
                    FragmentManager fm = mActivity.getSupportFragmentManager();
                    HongbaoWalletListFragment walletlist = new HongbaoWalletListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("wallet", wallet);
                    walletlist.setArguments(bundle);
                    walletlist.show(fm, "list");
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //计算 红包手续费  和  转账手续费
                oxPrice="0x" + new BigInteger(positionMax.subtract(positionMin).multiply(new BigDecimal(gasBar.getProgress()).divide(Constant.p100)).add(positionMin).setScale(0,RoundingMode.DOWN).toPlainString(), 10).toString(16);
                oxGas="0x" + new BigInteger(max.subtract(min).multiply(new BigDecimal(gasBar.getProgress()).divide(Constant.p100)).add(min).divide(new BigDecimal(360000),0,RoundingMode.DOWN).toPlainString(), 10).toString(16);

                showFixLoading();

                //获取账户余额
                WalletApi.balance(mActivity, wallet.get(walletIndex).getAddress(), new JsonCallback<LzyResponse<ValueBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<ValueBean>> response) {
                        BigDecimal currentPrice = new BigDecimal(AppUtil.toD(response.body().data.getValue().replace("0x", "0")));
                        BigDecimal price = new BigDecimal(fei.getText().toString().replace("ETH","")).multiply(Constant.pEther);
                        if (currentPrice.subtract(price).doubleValue() >= 0) {
                            if (isUpGas){
                                transfer();
                            }else {
                                WalletApi.transactionCount(mActivity, wallet.get(walletIndex).getAddress(), new JsonCallback<LzyResponse<CountBean>>() {
                                    @Override
                                    public void onSuccess(Response<LzyResponse<CountBean>> response) {
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
                            }
                        } else {
                            ToastUtil.show(R.string.qianbaoyuebuzu);
                            hideFixLoading();
                            return;
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<ValueBean>> response) {
                        super.onError(response);
                        ToastUtil.show(R.string.yuehuoqushibai);
                        hideFixLoading();
                    }

                });
            }
        });

        swipeRefersh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefersh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        min=Constant.GWEI_LOW.multiply(new BigDecimal(360000));
        max=Constant.GWEI_HIGH.divide(new BigDecimal(2)).multiply(new BigDecimal(360000));
    }


    @Override
    protected void initData() {

        showFixLoading();

        ZixunApi.getMinGas(this, new JsonCallback<LzyResponse<HongbaoMinGas>>() {
            @Override
            public void onSuccess(Response<LzyResponse<HongbaoMinGas>> response) {
                minGas=response.body().data;
                if (new BigDecimal(minGas.getValue()).floatValue()>min.divide(Constant.pEther).floatValue()){
                    min=new BigDecimal(minGas.getValue()).multiply(Constant.pEther);
                    if (null!=positionMin){
                        setMainProgess();
                    }
                }
            }
        });


        WalletApi.wallet(mActivity, new JsonCallback<LzyResponse<CommonListBean<WalletBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                wallet.clear();
                if (null != response.body().data.getList()) {
                    String wallets = App.get().getSp().getString(Constant.WALLETS, "").toLowerCase();
                    String wallets_beifen = App.get().getSp().getString(Constant.WALLETS_BEIFEN, "").toLowerCase();
                    String walletsZjc = App.get().getSp().getString(Constant.WALLETS_ZJC_BEIFEN, "").toLowerCase();
                    for (int i = 0; i < response.body().data.getList().size(); i++) {
                        if (wallets.contains(response.body().data.getList().get(i).getAddress().toLowerCase())) {
                            if (wallets_beifen.contains(response.body().data.getList().get(i).getAddress().toLowerCase()) || walletsZjc.contains(response.body().data.getList().get(i).getAddress().toLowerCase())) {
                                response.body().data.getList().get(i).setType(Constant.BEIFEN);
                            } else {
                                response.body().data.getList().get(i).setType(Constant.ZHENGCHANG);
                            }
                        } else {
                            response.body().data.getList().get(i).setType(Constant.GUANCHA);
                        }
                        if (response.body().data.getList().get(i).getCategory_id()==1){
                            wallet.add(response.body().data.getList().get(i));
                            if (response.body().data.getList().get(i).getAddress().toLowerCase().equals(selectAddress.toLowerCase())){
                                response.body().data.getList().get(i).setSelect(true);
                                walletIndex=wallet.size()-1;
                            }
                        }
                    }
                }

                getWalletBanlence();

                //获取所有钱包的 Eth
                getTokenList();
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                super.onError(response);
                hideFixLoading();
                ToastUtil.show(getString(R.string.jiazaishibai));
            }

        });

        EthCall call=new EthCall();
        try {
            String cast=call.redPacketTaxCost(App.isMain?Constant.MAIN_HONGBAO_ADDRESS:Constant.TEST_HONGBAO_ADDRESS);
            JSONObject castRpc=new JSONObject(cast);
            ZixunApi.ethRpc(this, castRpc, new JsonCallback<LzyResponse<String>>() {
                @Override
                public void onSuccess(Response<LzyResponse<String>> response) {
                    positionMin=new BigDecimal(AppUtil.toD(response.body().data.substring(2,66))).multiply(new BigDecimal(hongbaoCount)).setScale(0, RoundingMode.DOWN);
                    positionMax=new BigDecimal(AppUtil.toD(response.body().data.substring(66))).multiply(new BigDecimal(hongbaoCount)).setScale(0, RoundingMode.DOWN);
                    setMainProgess();
                }

                @Override
                public void onError(Response<LzyResponse<String>> response) {
                    super.onError(response);
                    ToastUtil.show(R.string.shujujiazaishibaiqingdianjichongshi);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMainProgess() {
        tLow=min.add(positionMin);
        tHigh=max.add(positionMax);

        low.setText(tLow.divide(Constant.pEther,8, RoundingMode.DOWN).toPlainString());
        high.setText(tHigh.divide(Constant.pEther,8,RoundingMode.DOWN).toPlainString());

        gasBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                BigDecimal gas=max.subtract(min).multiply(new BigDecimal(progressFloat).divide(Constant.p100)).add(min);
                BigDecimal hbFee=positionMax.subtract(positionMin).multiply(new BigDecimal(progressFloat).divide(Constant.p100)).add(positionMin);

                fei.setText(gas.add(hbFee).divide(Constant.pEther).setScale(8,BigDecimal.ROUND_HALF_DOWN).toPlainString()+"ETH");
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });

        if (isUpGas){
            float p=new BigDecimal(preGas).subtract(tLow.divide(Constant.pEther,8, RoundingMode.DOWN)).divide(tHigh.subtract(tLow).divide(Constant.pEther,8, RoundingMode.DOWN),2,RoundingMode.DOWN).multiply(new BigDecimal(100)).floatValue();
            gasBar.setProgress(p);
        }else {
            gasBar.setProgress(10);
        }
    }

    private void getWalletBanlence() {
        //获取选择的钱包的 Eth
        WalletApi.balance(mActivity, selectAddress, new JsonCallback<LzyResponse<ValueBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ValueBean>> response) {
                BigDecimal currentPrice = new BigDecimal(AppUtil.toD(response.body().data.getValue().replace("0x", "0"))).divide(Constant.pEther,8, RoundingMode.DOWN);
                address.setText(selectAddress);
                num.setText(currentPrice.toPlainString());
                for (int i=0;wallet.size()>i;i++){
                    if (wallet.get(i).getAddress().toLowerCase().equals(selectAddress.toLowerCase())){
                        walletIndex=i;
                        break;
                    }
                }
            }

            @Override
            public void onError(Response<LzyResponse<ValueBean>> response) {
                super.onError(response);
                ToastUtil.show(R.string.yuehuoqushibai);
                hideLoading();
            }
        });
    }

    private void getTokenList() {

        //获取每个钱包的eth 和 neo 余额
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (WalletBean wa : wallet) {
            sb.append(wa.getId() + ",");
        }
        if (sb.length() != 1) {
            sb.delete(sb.length() - 1, sb.length());
        }
        sb.append("]");
        WalletApi.conversion(this, sb.toString(), new JsonCallback<LzyResponse<CommonListBean<WalletCountBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<WalletCountBean>>> response) {
                isGetNum = true;
                walletCountBeans.clear();
                walletCountBeans.addAll(response.body().data.getList());

                //计算ethPrice
                for (int i = 0; i < wallet.size(); i++) {
                    for (int j=0;j<walletCountBeans.size();j++){
                        if (wallet.get(i).getAddress().toLowerCase().equals(
                                walletCountBeans.get(j).getAddress().toLowerCase()
                        )){
                            //进行计算
                            BigDecimal currentPrice = new BigDecimal(AppUtil.toD(walletCountBeans.get(j).getBalance().replace("0x", "0"))).divide(Constant.pEther).setScale(4, RoundingMode.DOWN);
                            wallet.get(i).setToken_name("ETH");
                            wallet.get(i).setToken_num(currentPrice.toPlainString());
                            break;
                        }
                    }
                }
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<WalletCountBean>>> response) {
                super.onError(response);
                if (NetworkUtils.isConnected(mActivity)) {
                    ToastUtil.show(R.string.jiazaishibai);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (null==swipeRefersh)return;
                hideFixLoading();
                swipeRefersh.setRefreshing(false);
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_HONGBAO_ADDRESS_GNT) {
            address.setText(wallet.get(event.getKey1()).getAddress());
            num.setText(wallet.get(event.getKey1()).getToken_num()+"ETH");
            walletIndex=event.getKey1();
            selectAddress=wallet.get(event.getKey1()).getAddress();
        }
    }

    private void transfer() {

        if (isUpGas){
            hideFixLoading();
            showPassDialog();
        }else {
            ZixunApi.getRedbagId(this, new JsonCallback<LzyResponse<String>>() {
                @Override
                public void onSuccess(Response<LzyResponse<String>> response) {
                    redbgId="0x"+new BigInteger(new BigDecimal(response.body().data).toPlainString(),10).toString(16);
                    showPassDialog();
                }

                @Override
                public void onError(Response<LzyResponse<String>> response) {
                    super.onError(response);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    hideFixLoading();
                }
            });
        }
    }

    private void showPassDialog() {
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
                            if (accounts[i].name.toLowerCase().equals(wallet.get(walletIndex).getAddress().toLowerCase())) {
                                //accountManager.getUserData(accounts[i], pass.getText().toString());
                                b = accountManager.getUserData(accounts[i], "wallet");
                                break;
                            }
                        }
                        Wallet walletH = null;
                        try {
                            walletH = Ethmobile.fromKeyStore(b, passWord);
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
                            data = "0x" + walletH.newRedPacket(
                                    App.isMain?Constant.MAIN_HONGBAO_ADDRESS:Constant.TEST_HONGBAO_ADDRESS
                                    , nonce
                                    , gntAddress.toLowerCase()
                                    , redbgId
                                    , redbagAddress.toLowerCase()
                                    , oxPrice
                                    , oxHongbaofei
                                    , oxCount
                                    ,"0x0"
                                    , oxGas
                                    , "0x" + new BigInteger(new BigDecimal(Constant.HONGBAO_GAS_LIMIT).setScale(0, BigDecimal.ROUND_DOWN).toPlainString(), 10).toString(16));
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
        ZixunApi.redbagFee(this
                , id
                , redbagAddress.toLowerCase()
                , fei.getText().toString().replace("ETH","")
                , wallet.get(walletIndex).getAddress().toLowerCase()
                , AppUtil.toD(redbgId.replace("0x","0"))
                , AppUtil.toD(nonce.replace("0x","0"))
                , isUpGas?id:"0"
                , Constant.ETH_ORDER_ASSET_ID
                , wallet.get(walletIndex).getAddress().toLowerCase()
                , App.isMain?Constant.MAIN_HONGBAO_ADDRESS:Constant.TEST_HONGBAO_ADDRESS
                , oxPrice
                , oxGas
                ,""
                , data
                , new JsonCallback<LzyResponse<HongbaoFeeBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<HongbaoFeeBean>> response) {
                        Intent intent=new Intent(mActivity,SendHongbaoCreateActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("selectAddress", selectAddress);
                        intent.putExtra("gntGas", gntGas);
                        intent.putExtra("gntName", gntName);
                        intent.putExtra("gntAddress", gntAddress);
                        intent.putExtra("gntDecimal", gntDecimal);
                        intent.putExtra("redbagAddress", redbagAddress);
                        intent.putExtra("hongbaofei", hongbaofei);
                        intent.putExtra("hongbaoCount", hongbaoCount);
                        intent.putExtra("redbgId", AppUtil.toD(redbgId.replace("0x","0")));
                        intent.putExtra("nonce", AppUtil.toD(nonce.replace("0x","0")));
                        intent.putExtra("preGas", fei.getText().toString().replace("ETH",""));
                        intent.putExtra("preWallet", selectAddress);
                        finshTogo(intent);
                    }

                    @Override
                    public void onError(Response<LzyResponse<HongbaoFeeBean>> response) {
                        super.onError(response);
                        if (response.getException().getMessage().contains("nonce too low")){
                            Intent intent=new Intent(mActivity,SendHongbaoCreateActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("selectAddress", selectAddress);
                            intent.putExtra("gntGas", gntGas);
                            intent.putExtra("gntName", gntName);
                            intent.putExtra("gntAddress", gntAddress);
                            intent.putExtra("gntDecimal", gntDecimal);
                            intent.putExtra("redbagAddress", redbagAddress);
                            intent.putExtra("hongbaofei", hongbaofei);
                            intent.putExtra("hongbaoCount", hongbaoCount);
                            intent.putExtra("redbgId", AppUtil.toD(redbgId.replace("0x","0")));
                            intent.putExtra("nonce", AppUtil.toD(nonce.replace("0x","0")));
                            intent.putExtra("preGas", fei.getText().toString().replace("ETH",""));
                            intent.putExtra("preWallet", selectAddress);
                            finshTogo(intent);
                        }else {
                            ToastUtil.show(getString(R.string.hongbaochuangjianshibai));
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
