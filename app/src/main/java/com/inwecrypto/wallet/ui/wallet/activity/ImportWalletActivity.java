package com.inwecrypto.wallet.ui.wallet.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.ui.ScanActivity;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.event.KeyEvent;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class ImportWalletActivity extends BaseActivity {
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.hit)
    TextView hit;
    @BindView(R.id.et_info)
    EditText etInfo;
    @BindView(R.id.tv_import)
    TextView tvImport;

    private int type;

    private int type_id;

    private MaterialDialog mMaterialDialog;
    private ArrayList<WalletBean> wallets;

    @Override
    protected void getBundleExtras(Bundle extras) {
        type=extras.getInt("type");
        type_id=extras.getInt("type_id");
        isOpenEventBus=true;
        if (type==1||type==4){
            wallets= (ArrayList<WalletBean>) extras.getSerializable("wallets");
        }
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_acivity_import_wallet;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Drawable drawableInfo= getResources().getDrawable(R.mipmap.import_scan);
        /// 这一步必须要做,否则不会显示.
        drawableInfo.setBounds(0, 0, drawableInfo.getMinimumWidth(), drawableInfo.getMinimumHeight());
        txtRightTitle.setCompoundDrawables(drawableInfo,null,null,null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity, ScanActivity.class);
                intent.putExtra("type",1);
                keepTogo(intent);
            }
        });

        tvImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etInfo.getText().toString().length() == 0) {
                    ToastUtil.show("填写内容不能为空");
                    return;
                }
                switch (type){
                    case 1:
                        impotKeystore(etInfo.getText().toString().trim());
                        break;
                    case 2:
                        impotAnquanma(etInfo.getText().toString().trim());
                        break;
                    case 3:
                        impotKey(etInfo.getText().toString().trim());
                        break;
                    case 4:
                        impotWatch(etInfo.getText().toString().trim());
                        break;
                    case 5:
                        impotSeed(etInfo.getText().toString().trim());
                        break;
                }
            }
        });
        switch (type){
            case 1:
                txtMainTitle.setText("添加结果");
                hit.setText(R.string.wallet_hit21);
                break;
            case 2:
                txtMainTitle.setText("添加助记词");
                hit.setText(R.string.wallet_hit22);
                break;
            case 3:
                txtMainTitle.setText("添加明文私钥");
                hit.setText(R.string.wallet_hit23);
                break;
            case 4:
                txtMainTitle.setText("添加观察钱包");
                hit.setText(R.string.wallet_hit24);
                break;
            case 5:
                txtMainTitle.setText("添加种子");
                hit.setText(R.string.wallet_hit25);
                break;
        }
    }

    private void impotKeystore(final String key) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_dialog_pass, null, false);
        final EditText pass= (EditText) view.findViewById(R.id.et_pass);
        TextView cancle= (TextView) view.findViewById(R.id.cancle);
        TextView ok= (TextView) view.findViewById(R.id.ok);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass.getText().toString().length()==0){
                    ToastUtil.show(getString(R.string.qingshurumima));
                    return;
                }
                if (null!=wallets){
                    for (WalletBean wallet:wallets){
                        if (key.contains(wallet.getAddress().replace("0x","").toLowerCase())){
                            ToastUtil.show("该钱包已添加，不能重复添加");
                            return;
                        }
                    }
                }

                Intent intent=new Intent(mActivity,ImportWalletSettingActivity.class);
                intent.putExtra("wallets",wallets);
                intent.putExtra("pass",pass.getText().toString());
                intent.putExtra("key",key);
                intent.putExtra("type",type);
                intent.putExtra("type_id",type_id);
                keepTogo(intent);
            }
        });

        mMaterialDialog = new MaterialDialog(mActivity).setView(view);
        mMaterialDialog.setBackgroundResource(R.drawable.trans_bg);
        mMaterialDialog.setCanceledOnTouchOutside(true);
        mMaterialDialog.show();
    }

    private void impotAnquanma(String key) {
        Intent intent=new Intent(mActivity,ImportWalletSettingActivity.class);
        intent.putExtra("wallets",wallets);
        intent.putExtra("pass","");
        intent.putExtra("key",key);
        intent.putExtra("type",type);
        intent.putExtra("type_id",type_id);
        keepTogo(intent);
    }

    private void impotKey(String key) {
        Intent intent=new Intent(mActivity,ImportWalletSettingActivity.class);
        intent.putExtra("wallets",wallets);
        intent.putExtra("pass","");
        intent.putExtra("key",key);
        intent.putExtra("type",type);
        intent.putExtra("type_id",type_id);
        keepTogo(intent);
    }

    private void impotWatch(String key) {
        if (AppUtil.isAddress(key.toLowerCase())){
            if (null!=wallets){
                for (WalletBean wallet:wallets){
                    if (key.toLowerCase().contains(wallet.getAddress().replace("0x","").toLowerCase())){
                        ToastUtil.show("该钱包已添加，不能重复添加");
                        return;
                    }
                }
            }
            Intent intent=new Intent(mActivity,ImportWalletSettingActivity.class);
            intent.putExtra("wallets",wallets);
            intent.putExtra("pass","");
            intent.putExtra("key",key.toLowerCase());
            intent.putExtra("type",type);
            intent.putExtra("type_id",type_id);
            keepTogo(intent);
        }else {
            ToastUtil.show("请输入正确地址");
        }
    }

    private void impotSeed(String key) {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()== Constant.EVENT_KEY){
            KeyEvent keyEvent= (KeyEvent) event.getData();
            etInfo.setText(keyEvent.getKey());
        }
    }
}
