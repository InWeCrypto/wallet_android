package com.inwecrypto.wallet.ui.me.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.MailBean;
import com.inwecrypto.wallet.bean.CommonRecordBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.MeApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.StringUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.ui.ScanActivity;
import com.inwecrypto.wallet.event.BaseEventBusBean;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class MailDetaileActivity extends BaseActivity {
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.scan)
    ImageView scan;
    @BindView(R.id.et_hint)
    EditText etHint;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.ll_detaile)
    LinearLayout llDetaile;

    private boolean isAdd;
    private boolean isEdit;
    private MailBean mailListBean;

    @Override
    protected void getBundleExtras(Bundle extras) {
        isOpenEventBus=true;
        isAdd = extras.getBoolean("isAdd",false);
        if (!isAdd){
            mailListBean= (MailBean) extras.getSerializable("mail");
        }
    }

    @Override
    protected int setLayoutID() {
        return R.layout.me_activity_mail_detaile;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit){
                    resetUI();
                }else {
                    finish();
                }
            }
        });
        txtRightTitle.setVisibility(View.GONE);

        if (isAdd){
            txtMainTitle.setText(R.string.mail_title_add);
            llDetaile.setVisibility(View.GONE);
            tvSave.setVisibility(View.VISIBLE);
        }else {
            resetUI();
            tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtMainTitle.setText(R.string.mail_title_edit);
                    isEdit=true;
                    etName.setEnabled(true);
                    etAddress.setEnabled(true);
                    etHint.setEnabled(true);
                    scan.setEnabled(true);
                    llDetaile.setVisibility(View.GONE);
                    tvSave.setVisibility(View.VISIBLE);

                }
            });
            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteMaile();
                }
            });
        }
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMail();
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keepTogo(ScanActivity.class);
            }
        });
    }

    private void saveMail() {
        String name=etName.getText().toString();
        String address=etAddress.getText().toString().trim().toLowerCase();
        String hint=etHint.getText().toString();

        if (StringUtils.isEmpty(name)){
            ToastUtil.show(getString(R.string.mail_hit1));
            return;
        }

        if (StringUtils.isEmpty(address)){
            ToastUtil.show(getString(R.string.mail_hit2));
            return;
        }

        if (!AppUtil.isAddress(address.trim())){
            ToastUtil.show("请输入正确的钱包地址");
            return;
        }

        if (isAdd){
            MeApi.contact(mActivity,2,name, address, hint, new JsonCallback<LzyResponse<Object>>() {
                @Override
                public void onSuccess(Response<LzyResponse<Object>> response) {
                    ToastUtil.show(getString(R.string.mail_hit3));
                    EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFRESH));
                    finish();
                }

                @Override
                public void onError(Response<LzyResponse<Object>> response) {
                    super.onError(response);
                    ToastUtil.show(getString(R.string.mail_hit7));
                }
            });
        }else {
            MeApi.editContact(mActivity,mailListBean.getId(),mailListBean.getCategory_id(), name, address, hint, new JsonCallback<LzyResponse<Object>>() {
                @Override
                public void onSuccess(Response<LzyResponse<Object>> response) {
                    resetUI();
                    ToastUtil.show(getString(R.string.mail_hit4));
                    EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFRESH));
                }

                @Override
                public void onError(Response<LzyResponse<Object>> response) {
                    super.onError(response);
                    ToastUtil.show(getString(R.string.mail_hit8));
                }
            });
        }

    }

    private void deleteMaile() {
        MeApi.deleteContact(mActivity,mailListBean.getId(), new JsonCallback<LzyResponse<Object>>() {
            @Override
            public void onSuccess(Response<LzyResponse<Object>> response) {
                ToastUtil.show(getString(R.string.mail_hit5));
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFRESH));
                finish();
            }

            @Override
            public void onError(Response<LzyResponse<Object>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.mail_hit9));
            }
        });
    }

    @Override
    protected void initData() {
        if (!isAdd){
            MeApi.getContact(mActivity,mailListBean.getId(), new JsonCallback<LzyResponse<CommonRecordBean<MailBean>>>() {
                @Override
                public void onSuccess(Response<LzyResponse<CommonRecordBean<MailBean>>> response) {
                    setData(response.body().data);
                }

                @Override
                public void onCacheSuccess(Response<LzyResponse<CommonRecordBean<MailBean>>> response) {
                    super.onCacheSuccess(response);
                    onSuccess(response);
                }
            });
        }
    }

    private void setData(CommonRecordBean<MailBean> data) {
        etName.setText(data.getRecord().getName());
        etAddress.setText(data.getRecord().getAddress());
        etHint.setText(data.getRecord().getRemark());
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()==Constant.EVENT_KEY){
            if (null!=event.getData()){
                com.inwecrypto.wallet.event.KeyEvent key= (com.inwecrypto.wallet.event.KeyEvent) event.getData();
                if (key.getKey().startsWith("0x")&&key.getKey().length()==42){
                    etAddress.setText(key.getKey().trim());
                }else {
                    ToastUtil.show("请输入正确的钱包地址");
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //1.点击返回键条件成立
        if(keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            if (isEdit){
                resetUI();
                return true;
            }
        }
        return super.onKeyDown(keyCode,event);
    }

    private void resetUI() {
        txtMainTitle.setText(R.string.mail_title_see);
        isEdit=false;
        etName.setEnabled(false);
        etAddress.setEnabled(false);
        etHint.setEnabled(false);
        scan.setEnabled(false);
        llDetaile.setVisibility(View.VISIBLE);
        tvSave.setVisibility(View.GONE);
    }
}
