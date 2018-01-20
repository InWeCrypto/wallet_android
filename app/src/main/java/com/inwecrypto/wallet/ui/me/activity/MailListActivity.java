package com.inwecrypto.wallet.ui.me.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.me.adapter.CommonPagerAdapter;
import com.inwecrypto.wallet.ui.me.fragment.MailListFragment;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class MailListActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.vp_list)
    ViewPager vpList;

    private ArrayList<BaseFragment> fragments;
    private MailListFragment ethMailListFragment;
    private MailListFragment btcMailListFragment;
    private MailListFragment neoMailListFragment;
    private CommonPagerAdapter adapter;
    private boolean address;
    private int select;

    @Override
    protected void getBundleExtras(Bundle extras) {
        address=extras.getBoolean("address",false);
        select=extras.getInt("select");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.me_activity_mail_list;
    }

    @Override
    protected void initView() {

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtMainTitle.setText(R.string.tongxunlu);

        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDialog();
            }
        });

        Drawable drawableInfo= getResources().getDrawable(R.mipmap.btn_add_friends);
        /// 这一步必须要做,否则不会显示.
        drawableInfo.setBounds(0, 0, drawableInfo.getMinimumWidth(), drawableInfo.getMinimumHeight());
        txtRightTitle.setCompoundDrawables(drawableInfo,null,null,null);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_eth:
                        vpList.setCurrentItem(0);
                        break;
                    case R.id.rb_btc:
                        vpList.setCurrentItem(1);
                        break;
                    case R.id.rb_neo:
                        vpList.setCurrentItem(2);
                        break;
                }
            }
        });

        vpList.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        rg.check(R.id.rb_eth);
                        break;
                    case 1:
                        rg.check(R.id.rb_btc);
                        break;
                    case 2:
                        rg.check(R.id.rb_neo);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ethMailListFragment=new MailListFragment();
        Bundle ethBundle=new Bundle();
        ethBundle.putInt("type", App.isMain?6:1);
        ethBundle.putBoolean("address",address);
        ethMailListFragment.setArguments(ethBundle);

        btcMailListFragment=new MailListFragment();
        Bundle btcBundle=new Bundle();
        btcBundle.putInt("type",3);
        btcBundle.putBoolean("address",address);
        btcMailListFragment.setArguments(btcBundle);

        neoMailListFragment=new MailListFragment();
        Bundle neoBundle=new Bundle();
        neoBundle.putInt("type",App.isMain?7:2);
        neoBundle.putBoolean("address",address);
        neoMailListFragment.setArguments(neoBundle);

        fragments=new ArrayList<>();
        fragments.add(ethMailListFragment);
        fragments.add(btcMailListFragment);
        fragments.add(neoMailListFragment);

        adapter=new CommonPagerAdapter(getSupportFragmentManager(),fragments);
        vpList.setAdapter(adapter);
        if (select!=0){
            vpList.setCurrentItem(select);
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
    }

    private void showSelectDialog() {

        View selectPopupWin = LayoutInflater.from(this).inflate(R.layout.view_popup_wallet_detaile, null, false);
        TextView zhujici = (TextView) selectPopupWin.findViewById(R.id.zhujici);
        TextView keystore = (TextView) selectPopupWin.findViewById(R.id.keystore);
        TextView delete = (TextView) selectPopupWin.findViewById(R.id.delete);

        selectPopupWin.findViewById(R.id.word).setVisibility(View.GONE);
        selectPopupWin.findViewById(R.id.transfer).setVisibility(View.GONE);
        selectPopupWin.findViewById(R.id.delete_wallet).setVisibility(View.GONE);
        zhujici.setText("ETH");
        keystore.setText("BTC");
        delete.setText("NEO");

        final PopupWindow window = new PopupWindow(selectPopupWin, DensityUtil.dip2px(this, 100), WindowManager.LayoutParams.WRAP_CONTENT);
        // 产生背景变暗效果
        WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.alpha = 0.4f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.update();
        window.showAsDropDown(txtRightTitle, 0,-DensityUtil.dip2px(this,10));
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                mActivity.getWindow().setAttributes(lp);
            }
        });

        zhujici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                Intent intent=new Intent(mActivity,MailDetaileActivity.class);
                intent.putExtra("isAdd",true);
                intent.putExtra("type",App.isMain?6:1);
                keepTogo(intent);
            }
        });

        keystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                ToastUtil.show(getString(R.string.jingqingqidai));
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                Intent intent=new Intent(mActivity,MailDetaileActivity.class);
                intent.putExtra("isAdd",true);
                intent.putExtra("type",App.isMain?7:2);
                keepTogo(intent);
            }
        });
    }
}
