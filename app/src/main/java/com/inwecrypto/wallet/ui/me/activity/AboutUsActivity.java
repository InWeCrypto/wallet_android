package com.inwecrypto.wallet.ui.me.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.event.BaseEventBusBean;

/**
 * Created by Administrator on 2017/8/8.
 * 功能描述：
 * 版本：@version
 */

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.rl_user)
    RelativeLayout rlUser;
    @BindView(R.id.rl_tiaokuan)
    RelativeLayout rlTiaokuan;
    @BindView(R.id.rl_xieyi)
    RelativeLayout rlXieyi;
    @BindView(R.id.rl_team)
    RelativeLayout rlTeam;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.me_activity_aboutus;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.guanyuwomen);
        txtRightTitle.setVisibility(View.GONE);

        rlUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,CommonWebActivity.class);
                intent.putExtra("title",getString(R.string.fuwuxieyi));
                intent.putExtra("url", Url.EULA);
                keepTogo(intent);
            }
        });

        rlTiaokuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,CommonWebActivity.class);
                intent.putExtra("title",getString(R.string.yinsitiaokuan));
                intent.putExtra("url",Url.EULA);
                keepTogo(intent);
            }
        });

        rlXieyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,CommonWebActivity.class);
                intent.putExtra("title",getString(R.string.kaiyuanxieyi));
                intent.putExtra("url",Url.OPENSOURCE);
                keepTogo(intent);
            }
        });

        rlTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,CommonWebActivity.class);
                intent.putExtra("title",getString(R.string.kaifatuandui));
                intent.putExtra("url",Url.TEAM);
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
