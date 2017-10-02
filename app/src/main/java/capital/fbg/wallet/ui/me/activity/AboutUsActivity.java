package capital.fbg.wallet.ui.me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import capital.fbg.wallet.AppApplication;
import capital.fbg.wallet.R;
import capital.fbg.wallet.base.BaseActivity;
import capital.fbg.wallet.bean.LoginBean;
import capital.fbg.wallet.common.Constant;
import capital.fbg.wallet.common.http.Url;
import capital.fbg.wallet.common.imageloader.GlideCircleTransform;
import capital.fbg.wallet.event.BaseEventBusBean;

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
        txtMainTitle.setText("关于我们");
        txtRightTitle.setVisibility(View.GONE);

        rlUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,CommonWebActivity.class);
                intent.putExtra("title","服务协议");
                intent.putExtra("url", Url.EULA);
                keepTogo(intent);
            }
        });

        rlTiaokuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,CommonWebActivity.class);
                intent.putExtra("title","隐私条款");
                intent.putExtra("url",Url.EULA);
                keepTogo(intent);
            }
        });

        rlXieyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,CommonWebActivity.class);
                intent.putExtra("title","开源协议");
                intent.putExtra("url",Url.OPENSOURCE);
                keepTogo(intent);
            }
        });

        rlTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,CommonWebActivity.class);
                intent.putExtra("title","开发团队");
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
