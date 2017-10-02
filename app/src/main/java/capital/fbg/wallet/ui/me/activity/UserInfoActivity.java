package capital.fbg.wallet.ui.me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import capital.fbg.wallet.AppApplication;
import capital.fbg.wallet.R;
import capital.fbg.wallet.base.BaseActivity;
import capital.fbg.wallet.bean.LoginBean;
import capital.fbg.wallet.common.Constant;
import capital.fbg.wallet.common.imageloader.GlideCircleTransform;
import capital.fbg.wallet.event.BaseEventBusBean;

/**
 * Created by Administrator on 2017/8/8.
 * 功能描述：
 * 版本：@version
 */

public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.right_hint1)
    TextView rightHint1;
    @BindView(R.id.rl_img)
    RelativeLayout rlImg;
    @BindView(R.id.hit)
    TextView hit;
    @BindView(R.id.nicheng)
    TextView nicheng;
    @BindView(R.id.right_hint2)
    TextView rightHint2;
    @BindView(R.id.rl_nicheng)
    RelativeLayout rlNicheng;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.right_hint3)
    TextView rightHint3;
    @BindView(R.id.rl_sex)
    RelativeLayout rlSex;

    private LoginBean loginBean;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.me_activity_userinfo;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.settting);
        txtRightTitle.setVisibility(View.GONE);
        rlImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != loginBean) {
                    Intent intent = new Intent(mActivity, HeadImgActivity.class);
                    intent.putExtra("user", loginBean);
                    keepTogo(intent);
                }
            }
        });
        rlNicheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, FixNameActivity.class);
                intent.putExtra("user", loginBean);
                keepTogo(intent);
            }
        });
        rlSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, FixSexActivity.class);
                intent.putExtra("user", loginBean);
                keepTogo(intent);
            }
        });
    }

    @Override
    protected void initData() {
        loginBean = AppApplication.get().getLoginBean();
        if (null != loginBean) {
            if (null != loginBean.getUser().getImg() && loginBean.getUser().getImg().length() > 0) {
                Glide.with(this)
                        .load(loginBean.getUser().getImg())
                        .crossFade()
                        .placeholder(R.mipmap.clod_icon)
                        .transform(new GlideCircleTransform(this))
                        .into(img);
            }
            nicheng.setText(loginBean.getUser().getNickname());
            switch (loginBean.getUser().getSex()) {
                case 1:
                    sex.setText(R.string.nan);
                    break;
                case 2:
                    sex.setText(R.string.nv);
                    break;
            }
        }
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_USERINFO) {
            initData();
        }
    }
}
