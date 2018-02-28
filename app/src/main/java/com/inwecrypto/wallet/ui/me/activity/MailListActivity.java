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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.me.adapter.CommonPagerAdapter;
import com.inwecrypto.wallet.ui.me.fragment.MailListFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    View txtRightTitle;
    @BindView(R.id.eth)
    TextView eth;
    @BindView(R.id.l2)
    View l2;
    @BindView(R.id.ethll)
    LinearLayout ethll;
    @BindView(R.id.neo)
    TextView neo;
    @BindView(R.id.l3)
    View l3;
    @BindView(R.id.neoll)
    LinearLayout neoll;
    @BindView(R.id.btc)
    TextView btc;
    @BindView(R.id.l4)
    View l4;
    @BindView(R.id.btcll)
    LinearLayout btcll;
    @BindView(R.id.vp_list)
    ViewPager vpList;

    private ArrayList<BaseFragment> fragments;
    private MailListFragment ethMailListFragment;
    private MailListFragment btcMailListFragment;
    private MailListFragment neoMailListFragment;
    private CommonPagerAdapter adapter;
    private boolean address;
    private int select;

    private int type = 1;

    @Override
    protected void getBundleExtras(Bundle extras) {
        address = extras.getBoolean("address", false);
        select = extras.getInt("select");
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

        ethll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpList.setCurrentItem(0);
            }
        });

        neoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpList.setCurrentItem(1);
            }
        });

        btcll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpList.setCurrentItem(2);
            }
        });

        vpList.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        selectType(1);
                        break;
                    case 1:
                        selectType(2);
                        break;
                    case 2:
                        selectType(3);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ethMailListFragment = new MailListFragment();
        Bundle ethBundle = new Bundle();
        ethBundle.putInt("type", 1);
        ethBundle.putBoolean("address", address);
        ethMailListFragment.setArguments(ethBundle);

        btcMailListFragment = new MailListFragment();
        Bundle btcBundle = new Bundle();
        btcBundle.putInt("type", 3);
        btcBundle.putBoolean("address", address);
        btcMailListFragment.setArguments(btcBundle);

        neoMailListFragment = new MailListFragment();
        Bundle neoBundle = new Bundle();
        neoBundle.putInt("type", 2);
        neoBundle.putBoolean("address", address);
        neoMailListFragment.setArguments(neoBundle);

        fragments = new ArrayList<>();
        fragments.add(ethMailListFragment);
        fragments.add(neoMailListFragment);
        fragments.add(btcMailListFragment);

        adapter = new CommonPagerAdapter(getSupportFragmentManager(), fragments);
        vpList.setAdapter(adapter);
        if (select != 0) {
            vpList.setCurrentItem(select);
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()== Constant.EVENT_REFERSH){
            if (1==event.getKey1()){
                selectType(1);
            }

            if (2==event.getKey1()){
                selectType(2);
            }
        }
    }

    private void selectType(int i) {
        if (type == i) {
            return;
        }
        type = i;
        l2.setVisibility(View.INVISIBLE);
        l3.setVisibility(View.INVISIBLE);
        l4.setVisibility(View.INVISIBLE);

        eth.setTextColor(getResources().getColor(R.color.c_D8D8D8));
        neo.setTextColor(getResources().getColor(R.color.c_D8D8D8));
        btc.setTextColor(getResources().getColor(R.color.c_D8D8D8));

        switch (type) {
            case 1:
                eth.setTextColor(getResources().getColor(R.color.c_FF6806));
                l2.setVisibility(View.VISIBLE);
                break;
            case 2:
                neo.setTextColor(getResources().getColor(R.color.c_FF6806));
                l3.setVisibility(View.VISIBLE);
                break;
            case 3:
                btc.setTextColor(getResources(). getColor(R.color.c_FF6806));
                l4.setVisibility(View.VISIBLE);
                break;
        }
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
        keystore.setText("NEO");
        delete.setText("BTC");

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
        window.showAsDropDown(txtRightTitle, 0, -DensityUtil.dip2px(this, 10));
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
                Intent intent = new Intent(mActivity, MailDetaileActivity.class);
                intent.putExtra("isAdd", true);
                intent.putExtra("type", 1);
                keepTogo(intent);
            }
        });

        keystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                Intent intent = new Intent(mActivity, MailDetaileActivity.class);
                intent.putExtra("isAdd", true);
                intent.putExtra("type", 2);
                keepTogo(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                ToastUtil.show(getString(R.string.jingqingqidai));
            }
        });
    }
}
