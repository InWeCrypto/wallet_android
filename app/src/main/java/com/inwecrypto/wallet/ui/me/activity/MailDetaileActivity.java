package com.inwecrypto.wallet.ui.me.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonRecordBean;
import com.inwecrypto.wallet.bean.MailBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.MeApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.StringUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.ScanActivity;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.scan)
    ImageView scan;
    @BindView(R.id.tv_delete)
    TextView tvDelete;

    private boolean isAdd;
    private int type;
    private MailBean mailListBean;

    @Override
    protected void getBundleExtras(Bundle extras) {
        isOpenEventBus = true;
        isAdd = extras.getBoolean("isAdd", false);
        type = extras.getInt("type");
        if (!isAdd) {
            mailListBean = (MailBean) extras.getSerializable("mail");
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
            finish();
            }
        });
        txtRightTitle.setText(R.string.baocun);
        txtRightTitle.setCompoundDrawables(null,null,null,null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().trim().length()==0){
                    ToastUtil.show(getString(R.string.nichengbunengweikong));
                    return;
                }
                if (etAddress.getText().toString().trim().length()==0){
                    ToastUtil.show(getString(R.string.dizhibunengweikong));
                    return;
                }
                saveMail();
            }
        });


        if (isAdd) {
            txtMainTitle.setText(R.string.tianjialianxiren);
            tvDelete.setVisibility(View.INVISIBLE);
        } else {
            txtMainTitle.setText(R.string.bianjilianxiren);
            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteMaile();
                }
            });
            etName.setText(mailListBean.getName());
            etAddress.setText(mailListBean.getAddress());
        }
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keepTogo(ScanActivity.class);
            }
        });
    }

    private void saveMail() {
        String name = etName.getText().toString();
        String address = etAddress.getText().toString().trim();
        if (type == 1) {
            address = address.toLowerCase();
        }
        String hint = "";

        if (StringUtils.isEmpty(name)) {
            ToastUtil.show(R.string.qingshurunicheng);
            return;
        }

        if (StringUtils.isEmpty(address)) {
            ToastUtil.show(R.string.qingshuruqianbaodizhi);
            return;
        }

        if (!AppUtil.isAddress(address.trim())) {
            ToastUtil.show(R.string.qingshuruzhengquedeqianbaodizhi);
            return;
        }

        showFixLoading();
        if (isAdd) {
            MeApi.contact(mActivity, type, name, address, hint, new JsonCallback<LzyResponse<Object>>() {
                @Override
                public void onSuccess(Response<LzyResponse<Object>> response) {
                    hideFixLoading();
                    ToastUtil.show(R.string.tianjiachenggong);
                    EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFRESH,type));
                    finish();
                }

                @Override
                public void onError(Response<LzyResponse<Object>> response) {
                    super.onError(response);
                    hideFixLoading();
                    ToastUtil.show(R.string.tianjiashibai);
                }
            });
        } else {
            MeApi.editContact(mActivity, mailListBean.getId(), mailListBean.getIco_id(), name, address, hint, new JsonCallback<LzyResponse<Object>>() {
                @Override
                public void onSuccess(Response<LzyResponse<Object>> response) {
                    hideFixLoading();
                    ToastUtil.show(R.string.xiugaichenggong);
                    EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFRESH,type));
                    finish();
                }

                @Override
                public void onError(Response<LzyResponse<Object>> response) {
                    super.onError(response);
                    hideFixLoading();
                    ToastUtil.show(R.string.xiugaishibai);
                }
            });
        }

    }

    private void deleteMaile() {
        showFixLoading();
        MeApi.deleteContact(mActivity, mailListBean.getId(), new JsonCallback<LzyResponse<Object>>() {
            @Override
            public void onSuccess(Response<LzyResponse<Object>> response) {
                hideFixLoading();
                ToastUtil.show(R.string.shanchuchenggong);
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFRESH,type));
                finish();
            }

            @Override
            public void onError(Response<LzyResponse<Object>> response) {
                super.onError(response);
                hideFixLoading();
                ToastUtil.show(R.string.shanchushibai);
            }
        });
    }

    @Override
    protected void initData() {
        if (!isAdd) {
            MeApi.getContact(mActivity, mailListBean.getId(), new JsonCallback<LzyResponse<CommonRecordBean<MailBean>>>() {
                @Override
                public void onSuccess(Response<LzyResponse<CommonRecordBean<MailBean>>> response) {
                    setData(response.body().data.getRecord());
                }

                @Override
                public void onCacheSuccess(Response<LzyResponse<CommonRecordBean<MailBean>>> response) {
                    super.onCacheSuccess(response);
                    onSuccess(response);
                }
            });
        }
    }

    private void setData(MailBean data) {
        etName.setText(data.getName());
        etAddress.setText(data.getAddress());
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_KEY) {
            if (null != event.getData()) {
                com.inwecrypto.wallet.event.KeyEvent key = (com.inwecrypto.wallet.event.KeyEvent) event.getData();
                if (type == 1) {
                    if (AppUtil.isEthAddress(key.getKey().trim())) {
                        etAddress.setText(key.getKey().trim());
                    } else {
                        ToastUtil.show(R.string.qingshuruzhengquedeethdizhi);
                    }
                } else if (type == 2) {
                    if (AppUtil.isNeoAddress(key.getKey().trim())) {
                        etAddress.setText(key.getKey().trim());
                    } else {
                        ToastUtil.show(R.string.qingshuruzhengquedeneodizhi);
                    }
                } else {
                    ToastUtil.show(R.string.qingshuruzhengquedeqianbaodizhi);
                }
            }
        }
    }

}
