package com.inwecrypto.wallet.ui.wallet.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.TransferBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import ethmobile.Wallet;

import com.inwecrypto.wallet.common.widget.MaterialDialog;

/**
 * Created by donghaijun on 2017/10/31.
 */

public class WarchEthTransferAccountsNfcActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.card)
    ImageView card;

    private BigDecimal pEther = new BigDecimal("1000000000000000000");

    // NFC相关
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    public static String[][] TECHLISTS; //NFC技术列表
    public static IntentFilter[] FILTERS; //过滤器

    static {
        try {
            TECHLISTS = new String[][] { { IsoDep.class.getName() }, { NfcA.class.getName() } ,{NfcF.class.getName()}};

            FILTERS = new IntentFilter[] { new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED, "*/*") };
        } catch (Exception ignored) {
        }
    }

    private boolean isGnt;
    private WalletBean wallet;
    private String hint;
    private String address;
    private String price;
    private String gas;
    private String gnt;
    private String data;
    private String asset_id;
    private TransferBean transfer;
    private MaterialDialog mMaterialDialog;
    private byte[] json=new byte[489];
    private byte[] json1;
    private byte[] json2;

    @Override
    protected void getBundleExtras(Bundle extras) {
        isGnt = extras.getBoolean("isGnt", false);
        wallet = (WalletBean) extras.getSerializable("wallet");
        address = extras.getString("address");
        hint = extras.getString("hint");
        price = extras.getString("price");
        gas = extras.getString("gas");
        transfer= (TransferBean) extras.getSerializable("code");

        if (isGnt) {
            gnt = extras.getString("gnt");
            asset_id= extras.getString("asset_id");
        }
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_acivity_add_wallet_nfc;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.zhuanzhangqueren);
        txtRightTitle.setVisibility(View.GONE);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        onNewIntent(getIntent());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

    //处理NFC触发
    @Override
    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action))
        {
            //从intent中获取标签信息
            Parcelable p = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (p != null) {
                Tag tag = (Tag) p;
                IsoDep isodep = IsoDep.get(tag);
                if (isodep != null){
                    showLoading();
                    try {
                        isodep.connect(); // 建立连接

                        byte[] SELECT = {
                                (byte) 0x00, // CLA = 00 (first interindustry command set)
                                (byte) 0xA4, // INS = A4 (SELECT)
                                (byte) 0x04, // P1  = 04 (select file by DF name)
                                (byte) 0x00, // P2  = 0C (first or only file; no FCI)
                                (byte) 0x07, // Lc  = 6  (data/AID has 6 bytes)
                                (byte) 0xCC, (byte) 0xEC,(byte) 0xD2,(byte) 0xBB,(byte) 0x5F,(byte) 0x56,(byte) 0x31 // AID = 15845F
                        };

                        byte[] result = isodep.transceive(SELECT);

                        if (!(result[0] == (byte) 0x90 && result[1] == (byte) 0x00)){
                            ToastUtil.show(R.string.nfc_card_error);
                            throw new IOException("could not select application");
                        }


                        byte[] GET_STRING = {
                                (byte) 0x80, // CLA = 00 (first interindustry command set)
                                (byte) 0xB0, // INS = A4 (SELECT)
                                (byte) 0x01, // P1  = 04 (select file by DF name)
                                (byte) 0x05, // P2  = 0C (first or only file; no FCI)
                                (byte) 0xf5, // Lc  = 6  (data/AID has 6 bytes)
                        };

                        json1 = isodep.transceive(GET_STRING);

                        if (!(json1[json1.length-2] == (byte) 0x90 && json1[json1.length-1] == (byte) 0x00)){
                            ToastUtil.show(R.string.nfc_read_error);
                            throw new IOException("could not select application");
                        }

                        byte[] GET_STRING2 = {
                                (byte) 0x80, // CLA = 00 (first interindustry command set)
                                (byte) 0xB0, // INS = A4 (SELECT)
                                (byte) 0x02, // P1  = 04 (select file by DF name)
                                (byte) 0x05, // P2  = 0C (first or only file; no FCI)
                                (byte) 0xf4, // Lc  = 6  (data/AID has 6 bytes)
                        };
                        json2 = isodep.transceive(GET_STRING2);

                        if (!(json2[json2.length-2] == (byte) 0x90 && json2[json2.length-1] == (byte) 0x00)){
                            ToastUtil.show(R.string.nfc_read_error);
                            throw new IOException("could not select application");
                        }
                        isodep.close(); // 关闭连接

                        System.arraycopy(json1,0,json,0,245);
                        System.arraycopy(json2,0,json,245,244);

                        hideLoading();
                        getData();

                    } catch (IOException e) {
                        e.printStackTrace();
                        hideLoading();
                    }

                }else {
                    ToastUtil.show(R.string.nfc_read_error);
                    readCardError();
                }
            }else {
                ToastUtil.show(R.string.nfc_read_error);
                readCardError();
            }
        }
    }

    private void getData() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_dialog_pass, null, false);
        final EditText pass = (EditText) view.findViewById(R.id.et_pass);
        TextView cancle = (TextView) view.findViewById(R.id.cancle);
        TextView ok = (TextView) view.findViewById(R.id.ok);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass.getText().toString().length() == 0) {
                    ToastUtil.show(getString(R.string.qingshurumima));
                    return;
                }
                showLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Wallet wallet = null;
                        try {
                            //wallet = Unichain.openETHWallet(json, pass.getText().toString());
                        } catch (Exception e) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(getString(R.string.mimacuowuqingchongshi));
                                    hideLoading();
                                }
                            });
                            return;
                        }
                        try {
                            if (transfer.getType() == 1) {
                                //data = "0x" + conver16HexStr(wallet.transferCurrency(transfer.getNonce(), transfer.getOx_gas(),"0x" + new BigInteger(new BigDecimal(Constant.GAS_LIMIT).setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString(),10).toString(16), transfer.getTransfer_address().toLowerCase(), transfer.getOx_price()));
                            } else {
//                                data = "0x" + conver16HexStr(wallet.transferToken(transfer.getNonce()
//                                        , transfer.getOx_gas()
//                                        , transfer.getGas_limit()
//                                        , transfer.getTransfer_address().toLowerCase()
//                                        , transfer.getOx_price().getBytes("utf-8")));
                            }
                        } catch (Exception e) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(R.string.zhuanzhangshibaiqingchongshi);
                                    hideLoading();
                                }
                            });
                            return;
                        }
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideLoading();
                                mMaterialDialog.dismiss();
                                transfer();
                            }
                        });
                    }
                }).start();
            }
        });

        mMaterialDialog = new MaterialDialog(mActivity).setView(view);
        mMaterialDialog.setBackgroundResource(R.drawable.trans_bg);
        mMaterialDialog.setCanceledOnTouchOutside(true);
        mMaterialDialog.show();

    }


    private void readCardError(){
        card.setImageResource(R.mipmap.card_error);
        card.postDelayed(new Runnable() {
            @Override
            public void run() {
               card.setImageResource(R.mipmap.card);
            }
        },2000);
    }

    private void transfer() {
        showFixLoading();
        if (isGnt) {
            WalletApi.walletOrder(mActivity
                    , wallet.getId()
                    , data
                    , wallet.getAddress()
                    , address
                    , hint
                    , new BigDecimal(price.toString()).multiply(pEther).setScale(0, BigDecimal.ROUND_DOWN).toPlainString()
                    , new BigDecimal(gas).multiply(pEther).setScale(0, BigDecimal.ROUND_DOWN).toPlainString()
                    , gnt
                    , asset_id
                    , new JsonCallback<LzyResponse<Object>>() {
                @Override
                public void onSuccess(Response<LzyResponse<Object>> response) {
                    hideFixLoading();
                    ToastUtil.show(R.string.zhuanzhangchenggong);
                    AppManager.getAppManager().finishActivity(WatchTokenTransferAccountsActivity.class);
                    AppManager.getAppManager().finishActivity(TransferAccountsActivity.class);
                    EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
                    finish();
                }

                @Override
                public void onError(Response<LzyResponse<Object>> response) {
                    super.onError(response);
                    hideFixLoading();
                    if (response.getException().getMessage().contains("wallet_error")) {
                        ToastUtil.show(R.string.fuwuqineibucuowu);
                        AppManager.getAppManager().finishActivity(WatchTokenTransferAccountsActivity.class);
                        AppManager.getAppManager().finishActivity(TransferAccountsActivity.class);
                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
                        finish();
                    } else {
                        ToastUtil.show(getString(R.string.load_error));
                    }
                }
            });
        } else {
            WalletApi.walletOrder(mActivity
                    , wallet.getId()
                    , data
                    , wallet.getAddress()
                    , address
                    , hint
                    , new BigDecimal(price.toString()).multiply(pEther).toPlainString()
                    , new BigDecimal(gas).multiply(pEther).toPlainString(), wallet.getCategory().getName()
                    , Constant.ETH_ORDER_ASSET_ID
                    , new JsonCallback<LzyResponse<Object>>() {
                @Override
                public void onSuccess(Response<LzyResponse<Object>> response) {
                    hideFixLoading();
                    ToastUtil.show(R.string.zhuanzhangchenggong);
                    AppManager.getAppManager().finishActivity(WatchEthTransferAccountsConfirmActivity.class);
                    AppManager.getAppManager().finishActivity(TransferAccountsActivity.class);
                    EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
                    finish();
                }

                @Override
                public void onError(Response<LzyResponse<Object>> response) {
                    super.onError(response);
                    hideFixLoading();
                    if (response.getException().getMessage().contains("wallet_error")) {
                        ToastUtil.show(R.string.fuwuqineibucuowu);
                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
                        finish();
                    } else {
                        ToastUtil.show(getString(R.string.load_error));
                    }
                }
            });
        }
    }


    /**
     * byte数组转换为十六进制的字符串
     **/
    public static String conver16HexStr(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            if ((b[i] & 0xff) < 0x10)
                result.append("0");
            result.append(Long.toString(b[i] & 0xff, 16));
        }
        return result.toString().toUpperCase();
    }

    //程序恢复
    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            // 这行代码是添加调度，效果是读标签的时候不会弹出候选程序，直接用本程序处理
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, FILTERS, TECHLISTS);
        }
    }

    //程序暂停
    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null)
            nfcAdapter.disableForegroundDispatch(this); // 取消调度
    }


}
