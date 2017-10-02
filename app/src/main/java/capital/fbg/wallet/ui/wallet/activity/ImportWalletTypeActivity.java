package capital.fbg.wallet.ui.wallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import capital.fbg.wallet.R;
import capital.fbg.wallet.base.BaseActivity;
import capital.fbg.wallet.bean.WalletBean;
import capital.fbg.wallet.event.BaseEventBusBean;

/**
 * Created by Administrator on 2017/8/9.
 * 功能描述：
 * 版本：@version
 */

public class ImportWalletTypeActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.keystore)
    LinearLayout keystore;
    @BindView(R.id.anquanma)
    LinearLayout anquanma;
    @BindView(R.id.key)
    LinearLayout key;
    @BindView(R.id.watch)
    LinearLayout watch;
    @BindView(R.id.seed)
    LinearLayout seed;

    private int type_id;
    private ArrayList<WalletBean> wallets;

    @Override
    protected void getBundleExtras(Bundle extras) {
        type_id=extras.getInt("type_id");
        wallets= (ArrayList<WalletBean>) extras.getSerializable("wallets");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_acivity_import_wallet_type;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.import_wallet);
        txtRightTitle.setVisibility(View.GONE);
        keystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,ImportWalletActivity.class);
                intent.putExtra("type",1);
                intent.putExtra("type_id",type_id);
                intent.putExtra("wallets",wallets);
                keepTogo(intent);
            }
        });
        anquanma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,ImportWalletActivity.class);
                intent.putExtra("type",2);
                intent.putExtra("type_id",type_id);
                keepTogo(intent);
            }
        });
        key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,ImportWalletActivity.class);
                intent.putExtra("type",3);
                intent.putExtra("type_id",type_id);
                keepTogo(intent);
            }
        });
        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,ImportWalletActivity.class);
                intent.putExtra("type",4);
                intent.putExtra("type_id",type_id);
                intent.putExtra("wallets",wallets);
                keepTogo(intent);
            }
        });
        seed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,ImportWalletActivity.class);
                intent.putExtra("type",5);
                intent.putExtra("type_id",type_id);
                keepTogo(intent);
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
