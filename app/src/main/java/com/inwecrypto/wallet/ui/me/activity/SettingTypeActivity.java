package com.inwecrypto.wallet.ui.me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.util.AppManager;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.LocaleUtils;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.MainTabActivity;
import com.inwecrypto.wallet.ui.login.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class SettingTypeActivity extends BaseActivity {


    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.ll1)
    RelativeLayout ll1;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.ll2)
    RelativeLayout ll2;

    private int selectPosition = -1;
    private int firstPosition=-1;
    private int type;

    @Override
    protected void getBundleExtras(Bundle extras) {
        type=extras.getInt("type");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.me_activity_unit;
    }

    @Override
    protected void initView() {
        txtRightTitle.setText(R.string.baocun);
        txtRightTitle.setCompoundDrawables(null, null, null, null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type){
                    case 1:
                        if (selectPosition == -1||selectPosition==firstPosition) {
                            finish();
                        } else {
                            App.get().getSp().putInt(Constant.UNIT_TYPE,selectPosition);
                            App.get().getSp().putBoolean(Constant.UNIT_CHANGE,true);
                            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_UNIT_CHANGE));
                            finish();
                        }
                        break;
                    case 2:
                        if (selectPosition == -1||selectPosition==firstPosition) {
                            finish();
                            return;
                        }
                        showFixLoading();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                App.isMain = (selectPosition==1?true:false);
                                App.get().getSp().putBoolean(Constant.NET, selectPosition==1?true:false);
                                Url.changeNet(selectPosition==1?true:false);
                                App.get().getSp().putBoolean(Constant.NEED_RESTART, true);
                                EMClient.getInstance().logout(true);
                                //修改 appKey
                                try {
                                    EMClient.getInstance().changeAppkey(
                                            selectPosition==1?"1109180116115999#online":"1109180116115999#test");
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                }
                                App.get().getSp().putString(Constant.TOKEN,"");
                                App.get().getSp().putString(Constant.TEST_TOKEN,"");
                                AppManager.getAppManager().finishAllActivity();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent=new Intent(mActivity, LoginActivity.class);
                                        finshTogo(intent);
                                        hideFixLoading();
                                    }
                                });
                            }}).start();
                        break;
                    case 3:
                        if (selectPosition == -1||selectPosition==firstPosition) {
                            finish();
                            return;
                        }
                        if (selectPosition==1) {//中文
                           App.get().getSp().putBoolean(Constant.IS_CHINESE,true);
                            LocaleUtils.updateLocale(mActivity, LocaleUtils.LOCALE_CHINESE);
                        } else{//英文
                            LocaleUtils.updateLocale(mActivity, LocaleUtils.LOCALE_ENGLISH);
                            App.get().getSp().putBoolean(Constant.IS_CHINESE,false);
                        }
                        App.get().getSp().putBoolean(Constant.LANGUE_CHANGE,true);
                        //
                        //设置语言
                        AppUtil.changeAppLanguage(mActivity);
                        App.get().getSp().putBoolean(Constant.NEED_RESTART,true);
                        Intent intent=new Intent(mActivity, MainTabActivity.class);
                        finshTogo(intent);
                        break;
                }
            }
        });
        switch (type){
            case 1:
                txtMainTitle.setText(R.string.huobidanwei);
                text1.setText(R.string.renmingbi);
                text2.setText(R.string.meiyuan);
                if (App.get().getUnit()==1){
                    img1.setVisibility(View.VISIBLE);
                    selectPosition=1;
                }else {
                    img2.setVisibility(View.VISIBLE);
                    selectPosition=2;
                }
                break;
            case 2:
                txtMainTitle.setText(R.string.wangluoqiehuan);
                text1.setText(R.string.zhengshiwangluo);
                text2.setText(R.string.ceshiwangluo);
                if (App.isMain){
                    img1.setVisibility(View.VISIBLE);
                    selectPosition=1;
                }else {
                    img2.setVisibility(View.VISIBLE);
                    selectPosition=2;
                }
                break;
            case 3:
                txtMainTitle.setText(R.string.yuyanshezhi);
                text1.setText(R.string.jiantizhongwen);
                text2.setText(R.string.yingwen);
                if (App.get().getSp().getBoolean(Constant.IS_CHINESE)){
                    img1.setVisibility(View.VISIBLE);
                    selectPosition=1;
                }else {
                    img2.setVisibility(View.VISIBLE);
                    selectPosition=2;
                }
                break;
        }
        firstPosition=selectPosition;
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectPosition==1){
                    return;
                }
                selectPosition=1;
                img1.setVisibility(View.VISIBLE);
                img2.setVisibility(View.INVISIBLE);
            }
        });

        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectPosition==2){
                    return;
                }
                selectPosition=2;
                img1.setVisibility(View.INVISIBLE);
                img2.setVisibility(View.VISIBLE);
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
