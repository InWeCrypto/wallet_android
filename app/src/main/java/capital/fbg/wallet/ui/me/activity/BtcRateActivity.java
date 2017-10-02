package capital.fbg.wallet.ui.me.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import capital.fbg.wallet.R;
import capital.fbg.wallet.base.BaseActivity;
import capital.fbg.wallet.event.BaseEventBusBean;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class BtcRateActivity extends BaseActivity {
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.iv_10)
    ImageView iv10;
    @BindView(R.id.rl_10)
    RelativeLayout rl10;
    @BindView(R.id.iv_genggao)
    ImageView ivGenggao;
    @BindView(R.id.rl_genggao)
    RelativeLayout rlGenggao;
    @BindView(R.id.iv_gao)
    ImageView ivGao;
    @BindView(R.id.rl_gao)
    RelativeLayout rlGao;
    @BindView(R.id.iv_biaozhun)
    ImageView ivBiaozhun;
    @BindView(R.id.rl_biaozhun)
    RelativeLayout rlBiaozhun;
    @BindView(R.id.tv_save)
    TextView tvSave;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.me_activity_btc_rate;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        rlGenggao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        rlGao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        rlBiaozhun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    protected void initData() {
        txtMainTitle.setText(R.string.btc_rate_title);
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
