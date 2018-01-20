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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonRecordBean;
import com.inwecrypto.wallet.bean.MailIconBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.wallet.activity.neowallet.AddNeoWalletClodNfcActivity;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by donghaijun on 2017/10/31.
 */

public class AddWalletClodNfcActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.card)
    ImageView card;

    private String name;
    private int type_id;
    private String address;
    private byte[] json;
    private byte[] json1=new byte[245];
    private byte[] json2=new byte[244];

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

    @Override
    protected void getBundleExtras(Bundle extras) {
        name=extras.getString("name");
        type_id=extras.getInt("type_id");
        address=extras.getString("address");
        json=extras.getByteArray("json");
        System.arraycopy(json,0,json1,0,245);
        System.arraycopy(json,245,json2,0,244);
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
        txtMainTitle.setText(R.string.zhengzaichaungjianlengqianbao);
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
//                        Log.i(TAG, "SELECT: " + bin2hex(result));

                        if (!(result[0] == (byte) 0x90 && result[1] == (byte) 0x00)){
                            ToastUtil.show(getString(R.string.nfc_card_error));
                            throw new IOException("could not select application");
                        }

                        System.out.println("result1:"+result[0]+","+result[1]);

                        ByteBuffer SET_STRING=ByteBuffer.allocate(250);
                        SET_STRING.put((byte) 0x80)
                                .put((byte) 0xD6)
                                .put((byte) 0x01)
                                .put((byte) 0x05)
                                .put((byte) 0xf5)
                                .put(json1);

                        result = isodep.transceive(SET_STRING.array());

                        if (!(result[result.length-2] == (byte) 0x90 && result[result.length-1] == (byte) 0x00)){
                            ToastUtil.show(getString(R.string.nfc_write_error));
                            throw new IOException("could not select application");
                        }

                        //创建文件
                        byte[] CREAT = {
                                (byte) 0x80, // CLA = 00 (first interindustry command set)
                                (byte) 0xE0, // INS = A4 (SELECT)
                                (byte) 0x03, // P1  = 04 (select file by DF name)
                                (byte) 0x02, // P2  = 0C (first or only file; no FCI)
                                (byte) 0x06, // Lc  = 6  (data/AID has 6 bytes)
                                (byte) 0x01,
                                (byte) 0x00,
                                (byte) 0x00,
                                (byte) 0x00,
                                (byte) 0x10,
                                (byte) 0x0F,
                        };
                        result = isodep.transceive(CREAT);

                        System.out.println("result3:"+result[0]+","+result[1]);

                        ByteBuffer SET_STRING2=ByteBuffer.allocate(249);
                        SET_STRING2.put((byte) 0x80)
                                .put((byte) 0xD6)
                                .put((byte) 0x02)
                                .put((byte) 0x05)
                                .put((byte) 0xf4)
                                .put(json2);

                        result = isodep.transceive(SET_STRING2.array());

                        if (!(result[result.length-2] == (byte) 0x90 && result[result.length-1] == (byte) 0x00)){
                            ToastUtil.show(R.string.nfc_write_error);
                            throw new IOException("could not select application");
                        }

                        isodep.close(); // 关闭连接

                        creatWallet();
                    } catch (Exception e) {
                        e.printStackTrace();
                        hideLoading();
                        readCardError();
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

    private void readCardError(){
        card.setImageResource(R.mipmap.card_error);
        card.postDelayed(new Runnable() {
            @Override
            public void run() {
                card.setImageResource(R.mipmap.card);
            }
        },3000);
    }

    private void creatWallet() {
        WalletApi.wallet(mActivity,type_id, name, address,"", new JsonCallback<LzyResponse<CommonRecordBean<WalletBean>>>() {
            @Override
            public void onSuccess(final Response<LzyResponse<CommonRecordBean<WalletBean>>> response) {

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        AppManager.getAppManager().finishActivity(AddNeoWalletClodNfcActivity.class);
                        AppManager.getAppManager().finishActivity(AddWalletClodSettingActivity.class);
                        AppManager.getAppManager().finishActivity(AddWalletTypeActivity.class);
                        AppManager.getAppManager().finishActivity(AddWalletListActivity.class);
                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET));

                        Intent intent = new Intent(mActivity, HotWalletActivity.class);
                        WalletBean walletBean=response.body().data.getRecord();
                        walletBean.setType(Constant.GUANCHA);

                        String mailIco= App.get().getSp().getString(Constant.WALLET_ICO,"[]");
                        ArrayList<MailIconBean> mailId = GsonUtils.jsonToArrayList(mailIco, MailIconBean.class);
                        int icon= AppUtil.getRoundmIcon();
                        mailId.add(new MailIconBean(walletBean.getId(),icon));
                        App.get().getSp().putString(Constant.WALLET_ICO,GsonUtils.objToJson(mailId));
                        walletBean.setIcon(AppUtil.getIcon(icon));
                        WalletBean.CategoryBean category=new WalletBean.CategoryBean();
                        category.setName("ETH");
                        walletBean.setCategory(category);
                        intent.putExtra("wallet",walletBean);
                        finshTogo(intent);
                    }
                });
            }

            @Override
            public void onError(Response<LzyResponse<CommonRecordBean<WalletBean>>> response) {
                super.onError(response);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        ToastUtil.show(getString(R.string.wallet_creat_error));
                    }
                });
            }
        });
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
