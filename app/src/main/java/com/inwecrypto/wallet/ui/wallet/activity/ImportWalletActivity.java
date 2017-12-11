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
import com.inwecrypto.wallet.common.widget.MaterialDialog;
import com.inwecrypto.wallet.ui.wallet.activity.neowallet.ImportNeoWalletSettingActivity;

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
        wallets= (ArrayList<WalletBean>) extras.getSerializable("wallets");
        isOpenEventBus=true;
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
                    ToastUtil.show(getString(R.string.tianxieneirongbuenngweikong));
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
                }
            }
        });
        switch (type){
            case 1:
                txtMainTitle.setText(R.string.tianjiakeystore);
                hit.setText(R.string.keystore_hit);
                break;
            case 2:
                txtMainTitle.setText(R.string.tianjiazhujici);
                hit.setText(R.string.zhujici_hit);
                break;
            case 3:
                txtMainTitle.setText(R.string.tianjiamingwensiyao);
                hit.setText(R.string.siyao_hit);
                break;
            case 4:
                txtMainTitle.setText(R.string.tianjiaguanchaqianbao);
                hit.setText(R.string.guancha_hit);
                break;
            case 5:
                txtMainTitle.setText(R.string.tianjiazhongzi);
                hit.setText(R.string.zhongzi_hit);
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
                            ToastUtil.show(getString(R.string.wallet_has_add_error));
                            return;
                        }
                    }
                }

                gotoImport(pass.getText().toString().trim(), key);
            }
        });

        mMaterialDialog = new MaterialDialog(mActivity).setView(view);
        mMaterialDialog.setBackgroundResource(R.drawable.trans_bg);
        mMaterialDialog.setCanceledOnTouchOutside(true);
        mMaterialDialog.show();
    }

    private void impotAnquanma(String key) {
        gotoImport("", key);
    }

    private void impotKey(String key) {
        gotoImport("", key);
    }

    private void impotWatch(String key) {
        if (AppUtil.isAddress(key.toLowerCase())){
            if (null!=wallets){
                for (WalletBean wallet:wallets){
                    if (key.toLowerCase().contains(wallet.getAddress().replace("0x","").toLowerCase())){
                        ToastUtil.show(R.string.wallet_has_add_error);
                        return;
                    }
                }
            }
            gotoImport("", key.toLowerCase().trim());
        }else {
            ToastUtil.show(getString(R.string.qingshuruzhengquedizhi));
        }
    }

    private void gotoImport(String pass, String key) {
        Intent intent=null;
        if (type_id==2){
            intent=new Intent(mActivity,ImportNeoWalletSettingActivity.class);
        }else {
            intent=new Intent(mActivity,ImportWalletSettingActivity.class);
        }
        intent.putExtra("wallets",wallets);
        intent.putExtra("pass",pass);
        intent.putExtra("key",key);
        intent.putExtra("type",type);
        intent.putExtra("type_id",type_id);
        keepTogo(intent);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()== Constant.EVENT_KEY){
            KeyEvent keyEvent= (KeyEvent) event.getData();
            etInfo.setText(keyEvent.getKey().trim());
        }
    }
}
