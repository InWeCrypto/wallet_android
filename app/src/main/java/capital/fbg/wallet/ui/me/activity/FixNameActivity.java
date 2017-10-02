package capital.fbg.wallet.ui.me.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import capital.fbg.wallet.AppApplication;
import capital.fbg.wallet.R;
import capital.fbg.wallet.base.BaseActivity;
import capital.fbg.wallet.bean.LoginBean;
import capital.fbg.wallet.common.Constant;
import capital.fbg.wallet.common.http.LzyResponse;
import capital.fbg.wallet.common.http.api.MeApi;
import capital.fbg.wallet.common.http.callback.JsonCallback;
import capital.fbg.wallet.common.util.GsonUtils;
import capital.fbg.wallet.common.util.ToastUtil;
import capital.fbg.wallet.event.BaseEventBusBean;

/**
 * Created by Administrator on 2017/8/12.
 * 功能描述：
 * 版本：@version
 */

public class FixNameActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.et_name)
    EditText etName;

    private LoginBean user;

    @Override
    protected void getBundleExtras(Bundle extras) {
        user= (LoginBean) extras.getSerializable("user");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.me_activity_fixname;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.fixname_title);
        txtRightTitle.setText(R.string.baocun2);
        txtRightTitle.setCompoundDrawables(null,null,null,null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().length()==0){
                    ToastUtil.show(getString(R.string.fixname_hit1));
                    return;
                }
                if (etName.getText().toString().length()>12){
                    ToastUtil.show("昵称不能超过12个字符");
                    return;
                }
                showLoading();
                MeApi.setUserInfo(mActivity,etName.getText().toString(),user.getUser().getImg(),user.getUser().getSex(), new JsonCallback<LzyResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<Object>> response) {
                        hideLoading();
                        AppApplication.get().getLoginBean().getUser().setNickname(etName.getText().toString());
                        AppApplication.get().getSp().putString(Constant.USER_INFO, GsonUtils.objToJson(user));
                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_USERINFO));
                        ToastUtil.show(getString(R.string.xiugaichenggong));
                        finish();
                    }

                    @Override
                    public void onError(Response<LzyResponse<Object>> response) {
                        super.onError(response);
                        hideLoading();
                        ToastUtil.show(getString(R.string.xiugaishibai));
                    }
                });
            }
        });
    }

    @Override
    protected void initData() {
        etName.setText(user.getUser().getNickname());
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
