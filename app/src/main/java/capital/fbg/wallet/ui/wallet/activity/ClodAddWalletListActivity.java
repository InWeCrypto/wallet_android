package capital.fbg.wallet.ui.wallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import capital.fbg.wallet.R;
import capital.fbg.wallet.base.BaseActivity;
import capital.fbg.wallet.bean.WalletBean;
import capital.fbg.wallet.common.util.ToastUtil;
import capital.fbg.wallet.event.BaseEventBusBean;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class ClodAddWalletListActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.eth_select)
    ImageView ethSelect;
    @BindView(R.id.btc_select)
    ImageView btcSelect;
    @BindView(R.id.rl_eth)
    RelativeLayout rlEth;
    @BindView(R.id.rl_btc)
    RelativeLayout rlBtc;

    private int selectPosition = -1;

    private int type;

    private int type_id=1;

    private ArrayList<WalletBean> wallets;

    @Override
    protected void getBundleExtras(Bundle extras) {
        type = extras.getInt("type");
        wallets= (ArrayList<WalletBean>) extras.getSerializable("wallets");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_clod_activity_add_wallet;
    }

    @Override
    protected void initView() {
        txtMainTitle.setText(R.string.add_wallet_title);
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtRightTitle.setVisibility(View.GONE);

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type_id==3){
                    ToastUtil.show("暂时无法添加BTC");
                    return;
                }
                Intent intent;
                if (type == 1) {
                    intent = new Intent(mActivity, ClodAddWalletSettingActivity.class);
                } else {
                    intent = new Intent(mActivity, ClodImportWalletTypeActivity.class);
                    intent.putExtra("wallets",wallets);
                }
                intent.putExtra("type_id", type_id);
                keepTogo(intent);
            }
        });

        rlEth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(mActivity).load(R.mipmap.list_btn_selected).crossFade().into(ethSelect);
                Glide.with(mActivity).load(R.mipmap.list_btn_default).crossFade().into(btcSelect);
                type_id=1;
            }
        });

        rlBtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(mActivity).load(R.mipmap.list_btn_selected).crossFade().into(btcSelect);
                Glide.with(mActivity).load(R.mipmap.list_btn_default).crossFade().into(ethSelect);
                type_id=3;
            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
