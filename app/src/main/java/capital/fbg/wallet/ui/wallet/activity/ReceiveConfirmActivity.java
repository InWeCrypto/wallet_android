package capital.fbg.wallet.ui.wallet.activity;

import android.os.Bundle;
import android.view.View;
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

public class ReceiveConfirmActivity extends BaseActivity {
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.tv_wallet_address)
    TextView tvWalletAddress;
    @BindView(R.id.tv_hit)
    TextView tvHit;
    @BindView(R.id.tv_order)
    TextView tvOrder;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_activity_receive_confirm;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        txtMainTitle.setText(R.string.receive_confirm_title);
        txtRightTitle.setVisibility(View.GONE);
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
