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

public class AddBtcWalletActivity extends BaseActivity {
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.add)
    TextView add;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_acivity_add_btc;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void initData() {
        txtMainTitle.setText(getString(R.string.add_wallet_title));
        txtRightTitle.setVisibility(View.GONE);
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
