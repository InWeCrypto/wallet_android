package capital.fbg.wallet.ui.me.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import capital.fbg.wallet.R;
import capital.fbg.wallet.base.BaseActivity;
import capital.fbg.wallet.event.BaseEventBusBean;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class SignatureActivity extends BaseActivity {
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.iv_address)
    ImageView ivAddress;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.iv_info)
    ImageView ivInfo;
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.iv_sign)
    ImageView ivSign;
    @BindView(R.id.sign)
    TextView sign;
    @BindView(R.id.tv_yanzheng)
    TextView tvYanzheng;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.me_activity_signature;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvYanzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        txtMainTitle.setText(R.string.signature_title);
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
