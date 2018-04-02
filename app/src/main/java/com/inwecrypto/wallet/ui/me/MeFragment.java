package com.inwecrypto.wallet.ui.me;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.LoginBean;
import com.inwecrypto.wallet.bean.YaoqinBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.imageloader.GlideCircleTransform;
import com.inwecrypto.wallet.common.util.NetworkUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.login.LoginActivity;
import com.inwecrypto.wallet.ui.me.activity.AboutUsActivity;
import com.inwecrypto.wallet.ui.me.activity.CommonWebActivity;
import com.inwecrypto.wallet.ui.me.activity.HelpCenterActivity;
import com.inwecrypto.wallet.ui.me.activity.MailListActivity;
import com.inwecrypto.wallet.ui.me.activity.MarketTipActivity;
import com.inwecrypto.wallet.ui.me.activity.SettingActivity;
import com.inwecrypto.wallet.ui.me.activity.SettingTypeActivity;
import com.inwecrypto.wallet.ui.me.activity.ShoucangActivity;
import com.inwecrypto.wallet.ui.me.activity.UserActivity;
import com.inwecrypto.wallet.ui.me.activity.YaoqinActivity;
import com.lzy.okgo.model.Response;

import java.io.CharArrayWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

import butterknife.BindView;

import static com.inwecrypto.wallet.common.Constant.EVENT_USERINFO;
import static com.inwecrypto.wallet.common.http.Url.MAIN_YAOQIN;
import static com.inwecrypto.wallet.common.http.Url.TEST_YAOQIN;

/**
 * Created by Administrator on 2017/7/15.
 * 功能描述：
 * 版本：@version
 */

public class MeFragment extends BaseFragment {


    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.nickName)
    TextView nickName;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.user)
    RelativeLayout user;
    @BindView(R.id.address_list)
    RelativeLayout addressList;
    @BindView(R.id.shoucang)
    RelativeLayout shoucang;
    @BindView(R.id.tip)
    RelativeLayout tip;
    @BindView(R.id.setting)
    RelativeLayout setting;
    @BindView(R.id.about_us)
    RelativeLayout aboutUs;
    @BindView(R.id.yaoqing)
    RelativeLayout yaoqing;
    @BindView(R.id.zhangben)
    RelativeLayout zhangben;
    @BindView(R.id.help)
    RelativeLayout help;
    @BindView(R.id.qindenglu)
    TextView qindenglu;

    @Override
    protected int setLayoutID() {
        return R.layout.me_fragment;
    }

    @Override
    protected void initView() {
        isOpenEventBus=true;
        user.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()){
                    keepTogo(LoginActivity.class);
                    return;
                }
                Intent intent=new Intent(mActivity, UserActivity.class);
                mActivity.keepTogo(intent);
            }
        });

        yaoqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()){
                    keepTogo(LoginActivity.class);
                    return;
                }
                ZixunApi.getYaoqinKey(this, new JsonCallback<LzyResponse<YaoqinBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<YaoqinBean>> response) {
                        if (response.body().data.isCandy_bow_stat()){
                            if (!App.get().isZh()){
                                ToastUtil.show(getString(R.string.jinxianzhongguoyonghucanjia));
                                return;
                            }
                            //获取邀请码
                            Intent intent=new Intent(mActivity, YaoqinActivity.class);
                            String token=App.get().getSp().getString(App.isMain? Constant.TOKEN:Constant.TEST_TOKEN);
                            intent.putExtra("url",(App.isMain?MAIN_YAOQIN:TEST_YAOQIN)+response.body().data.getCode()+"&token="+token);
                            intent.putExtra("code",response.body().data.getCode());
                            keepTogo(intent);
                        }else {
                            ToastUtil.show(getString(R.string.huodongyijieshu));
                        }

                    }

                    @Override
                    public void onError(Response<LzyResponse<YaoqinBean>> response) {
                        super.onError(response);
                        ToastUtil.show(getString(R.string.yaoqingshibai));
                    }
                });
            }
        });

        zhangben.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(R.string.jinqingqidai);
            }
        });

        addressList.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()){
                    keepTogo(LoginActivity.class);
                    return;
                }
                Intent intent=new Intent(mActivity, MailListActivity.class);
                mActivity.keepTogo(intent);

            }
        });

        shoucang.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()){
                    keepTogo(LoginActivity.class);
                    return;
                }
                Intent intent=new Intent(mActivity, ShoucangActivity.class);
                mActivity.keepTogo(intent);

            }
        });

        tip.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()){
                    keepTogo(LoginActivity.class);
                    return;
                }
                Intent intent=new Intent(mActivity, MarketTipActivity.class);
                mActivity.keepTogo(intent);

            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity, SettingActivity.class);
                mActivity.keepTogo(intent);

            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity, HelpCenterActivity.class);
                mActivity.keepTogo(intent);
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity, AboutUsActivity.class);
                mActivity.keepTogo(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void loadData() {
        if (App.get().isLogin()){
            qindenglu.setVisibility(View.GONE);
            nickName.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            LoginBean loginBean = App.get().getLoginBean();
            if (null != loginBean) {
                if (null != loginBean.getImg() && loginBean.getImg().length() > 0) {
                    Glide.with(this)
                            .load(loginBean.getImg())
                            .crossFade()
                            .error(R.mipmap.wode_touxiang)
                            .transform(new GlideCircleTransform(mContext))
                            .into(img);
                }else{
                    Glide.with(this)
                            .load(R.mipmap.wode_touxiang)
                            .crossFade()
                            .transform(new GlideCircleTransform(mContext))
                            .into(img);
                }
                nickName.setText(loginBean.getName());
                email.setText(getString(R.string.dengluzhanghu)+loginBean.getEmail());
            }
        }else {
            nickName.setVisibility(View.INVISIBLE);
            email.setVisibility(View.INVISIBLE);
            qindenglu.setVisibility(View.VISIBLE);
        }
        isLoadSuccess=true;
        isFirst=false;
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()==EVENT_USERINFO){
            loadData();
        }
    }


}
