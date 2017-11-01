package com.inwecrypto.wallet.ui.wallet.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.WriterException;


import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.TransferBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.common.util.QuanCodeUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import me.drakeet.materialdialog.MaterialDialog;
import unichain.ETHWallet;
import unichain.Unichain;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class ClodTransferAccountsConfirmActivity extends BaseActivity {
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_service_charge)
    TextView tvServiceCharge;
    @BindView(R.id.et_address)
    TextView etAddress;
    @BindView(R.id.et_hit)
    TextView etHit;
    @BindView(R.id.tv_transfer)
    TextView tvTransfer;

    private MaterialDialog mMaterialDialog;

    private TransferBean transfer;
    private Bitmap bitmap;

    private BigDecimal pEther = new BigDecimal("1000000000000000000");

    @Override
    protected void getBundleExtras(Bundle extras) {
        transfer = (TransferBean) extras.getSerializable("transfer");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_activity_transfer_accounts_confirm;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.transfer_confirm);
        txtRightTitle.setVisibility(View.GONE);
        tvTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        if (NetworkUtils.isConnected(mActivity)) {
                            ToastUtil.show("请确保手机处于飞行模式！");
                            return;
                        }
                        showLoading();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                byte[] b = new byte[0];
                                final AccountManager accountManager = AccountManager.get(mActivity);
                                Account[] accounts = accountManager.getAccountsByType("com.inwecrypto.wallet");
                                for (int i = 0; i < accounts.length; i++) {
                                    if (accounts[i].name.equals(transfer.getWallet_address().toLowerCase())) {
                                        //accountManager.getUserData(accounts[i], pass.getText().toString());
                                        b = accountManager.getUserData(accounts[i], "wallet").getBytes();
                                        break;
                                    }
                                }
                                ETHWallet wallet = null;
                                try {
                                    wallet = Unichain.openETHWallet(b, pass.getText().toString());
                                } catch (Exception e) {
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtil.show(getString(R.string.wallet_hit27));
                                            hideLoading();
                                        }
                                    });
                                    return;
                                }
                                String data = "";
                                try {
                                    if (transfer.getType() == 1) {
                                        data = "0x" + conver16HexStr(wallet.transferCurrency(transfer.getNonce(), transfer.getOx_gas(),"0x" + new BigInteger(new BigDecimal(Constant.GAS_LIMIT).setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString(),10).toString(16), transfer.getTransfer_address().toLowerCase(), transfer.getOx_price()));
                                    } else {
                                        data = "0x" + conver16HexStr(wallet.transferToken(transfer.getNonce()
                                                , transfer.getOx_gas()
                                                , transfer.getGas_limit()
                                                , transfer.getTransfer_address().toLowerCase()
                                                , transfer.getOx_price().getBytes("utf-8")));
                                    }
                                } catch (Exception e) {
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtil.show("转账失败！请稍后重试");
                                            hideLoading();
                                        }
                                    });
                                    return;
                                }
                                final String finalData = data;
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideLoading();
                                        mMaterialDialog.dismiss();
                                        showDataCode(finalData);
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
        });
    }

    @Override
    protected void initData() {
        tvPrice.setText(new BigDecimal(transfer.getShow_price()).setScale(4,BigDecimal.ROUND_HALF_UP).toPlainString());
        tvServiceCharge.setText(getString(R.string.transfer_hit1) + transfer.getShow_gas());
        etAddress.setText(transfer.getTransfer_address().toLowerCase());
        etHit.setText(transfer.getHit());
    }

    private void showDataCode(final String data) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_dialog_clod_code, null, false);
        View ok = view.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final ImageView code = (ImageView) view.findViewById(R.id.code);
        code.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) code.getLayoutParams();
                params.height = code.getMeasuredWidth();
                code.setLayoutParams(params);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                //文本类型
                try {
                    bitmap = QuanCodeUtils.createQuanCode(mActivity, data);
                    code.post(new Runnable() {
                        @Override
                        public void run() {
                            code.setImageBitmap(bitmap);
                        }
                    });
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        mMaterialDialog = new MaterialDialog(mActivity).setView(view);
        mMaterialDialog.setBackgroundResource(R.drawable.trans_bg);
        mMaterialDialog.setCanceledOnTouchOutside(true);
        mMaterialDialog.show();
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

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
}
