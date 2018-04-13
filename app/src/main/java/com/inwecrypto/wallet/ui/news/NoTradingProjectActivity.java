package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.PingjiaBean;
import com.inwecrypto.wallet.bean.TradingProjectDetaileBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.RatingBar;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.login.LoginActivity;
import com.inwecrypto.wallet.ui.me.activity.MyPingjiaActivity;
import com.inwecrypto.wallet.ui.me.adapter.CommonPagerAdapter;
import com.inwecrypto.wallet.ui.news.fragment.NoTradingProjectFragment;
import com.inwecrypto.wallet.ui.news.fragment.ProjectXiangmuFragment;
import com.inwecrypto.wallet.ui.news.fragment.YonghuPingjiaFragment;
import com.lzy.okgo.model.Response;

import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/2/9 15:23
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class NoTradingProjectActivity extends BaseActivity {


    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.symble)
    TextView symble;
    @BindView(R.id.block_chain)
    TextView blockChain;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.charge)
    TextView charge;
    @BindView(R.id.no)
    TextView no;
    @BindView(R.id.ratingbar)
    RatingBar ratingbar;
    @BindView(R.id.fenshu)
    TextView fenshu;
    @BindView(R.id.zonghe)
    TextView zonghe;
    @BindView(R.id.l1)
    View l1;
    @BindView(R.id.zonghell)
    LinearLayout zonghell;
    @BindView(R.id.xiangmu)
    TextView xiangmu;
    @BindView(R.id.l2)
    View l2;
    @BindView(R.id.xiangmull)
    LinearLayout xiangmull;
    @BindView(R.id.vp_list)
    ViewPager vpList;
    @BindView(R.id.yonghu)
    TextView yonghu;
    @BindView(R.id.yonghull)
    LinearLayout yonghull;
    @BindView(R.id.l3)
    View l3;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    private TradingProjectDetaileBean project;

    private ArrayList<BaseFragment> fragments;
    private NoTradingProjectFragment tradingProjectFragment;
    private YonghuPingjiaFragment yonghuPingjiaFragment;
    private ProjectXiangmuFragment xiangmuFragment;
    private CommonPagerAdapter adapter;
    private int type = 1;
    private boolean isShow;

    @Override
    protected void getBundleExtras(Bundle extras) {
        project = (TradingProjectDetaileBean) extras.getSerializable("project");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.no_trading_project_activity;
    }

    @Override
    protected void initView() {

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtMainTitle.setText(project.getUnit());

        txtRightTitle.setText(R.string.xiepingjia);
        txtRightTitle.setCompoundDrawables(null, null, null, null);

        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()) {
                    keepTogo(LoginActivity.class);
                    return;
                }
                ZixunApi.getCurComment(mActivity, project.getId() + "", new JsonCallback<LzyResponse<PingjiaBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<PingjiaBean>> response) {
                        if (null == response.body().data) {
                            Intent intent = new Intent(mActivity, PingjiaXiangmuActivity.class);
                            intent.putExtra("project", project);
                            keepTogo(intent);
                        } else {
                            if (response.body().data.getIs_category_comment()==0){
                                Intent intent = new Intent(mActivity, PingjiaXiangmuActivity.class);
                                intent.putExtra("project", project);
                                keepTogo(intent);
                            }else {
                                Intent intent = new Intent(mActivity, MyPingjiaActivity.class);
                                intent.putExtra("isFromMe", false);
                                intent.putExtra("pingjia", response.body().data);
                                keepTogo(intent);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<PingjiaBean>> response) {
                        super.onError(response);
                        ToastUtil.show(R.string.jiazaishibai);
                    }
                });
//                //输入密码
//                FragmentManager fm = getSupportFragmentManager();
//                ProjectStartFragment input = new ProjectStartFragment();
//                Bundle bundle = new Bundle();
//                if (null != project.getCategory_user() && null != project.getCategory_user().getScore()&& !project.getCategory_user().getScore().equals("0")) {
//                    bundle.putBoolean("isSb", true);
//                    bundle.putString("num", project.getCategory_user().getScore());
//                } else {
//                    bundle.putBoolean("isSb", false);
//                    bundle.putString("num", "0.0");
//                }
//                input.setArguments(bundle);
//                input.show(fm, "start");
//                input.setOnNextListener(new ProjectStartFragment.OnNextInterface() {
//                    @Override
//                    public void onNext(final float fen, final Dialog dialog) {
//                        ZixunApi.projectScore(this, project.getId(), fen, new JsonCallback<LzyResponse<Object>>() {
//                                    @Override
//                                    public void onSuccess(Response<LzyResponse<Object>> response) {
//                                        ToastUtil.show(getString(R.string.pingfenchenggong));
//                                        if (null == project.getCategory_user()) {
//                                            TradingProjectDetaileBean.CategoryUserBean categoryUserBean = new TradingProjectDetaileBean.CategoryUserBean();
//                                            categoryUserBean.setScore(fen + "");
//                                            project.setCategory_user(categoryUserBean);
//                                        } else {
//                                            project.getCategory_user().setScore(fen + "");
//                                        }
//                                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PINGLUN, fen + ""));
//                                        dialog.cancel();
//                                    }
//
//                                    @Override
//                                    public void onError(Response<LzyResponse<Object>> response) {
//                                        super.onError(response);
//                                        ToastUtil.show(R.string.caozuoshibai);
//                                    }
//                                }
//                        );
//                    }
//                });

            }
        });

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if ((verticalOffset + appBarLayout.getTotalScrollRange()) == 0) {
                    //已经折叠
                    isShow = false;
                } else {
                    //未折叠
                    isShow = true;
                }
            }
        });

        if (null != project.getImg()) {
            Glide.with(this)
                    .load(project.getImg())
                    .crossFade()
                    .into(img);
        }

        name.setText(project.getName());
        symble.setText("(" + project.getLong_name() + ")");
        blockChain.setText(project.getIndustry());
        if (null != project.getCategory_score()) {
            if (App.get().isZh()) {
                no.setText("第" + project.getCategory_score().getSort() + "名");
            } else {
                no.setText("No." + project.getCategory_score().getSort());
            }
            ratingbar.setStar((float) project.getCategory_score().getValue());
            fenshu.setText(new BigDecimal(project.getCategory_score().getValue()).setScale(1, BigDecimal.ROUND_DOWN).toPlainString() + getString(R.string.fenshu));
        } else {
            ratingbar.setStar(0);
            fenshu.setText("0" + getString(R.string.fenshu));
        }

        zonghell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpList.setCurrentItem(0);
            }
        });

        yonghull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpList.setCurrentItem(1);
            }
        });

        xiangmull.setOnClickListener(new View.OnClickListener() {
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

        tradingProjectFragment = new NoTradingProjectFragment();
        Bundle tradingBundle = new Bundle();
        tradingBundle.putSerializable("project", project);
        tradingProjectFragment.setArguments(tradingBundle);

        yonghuPingjiaFragment = new YonghuPingjiaFragment();
        Bundle yonghuBundle = new Bundle();
        yonghuBundle.putSerializable("project", project);
        yonghuPingjiaFragment.setArguments(yonghuBundle);

        xiangmuFragment = new ProjectXiangmuFragment();
        Bundle xiangmuBundle = new Bundle();
        xiangmuBundle.putSerializable("project", project);
        xiangmuFragment.setArguments(xiangmuBundle);

        fragments = new ArrayList<>();
        fragments.add(tradingProjectFragment);
        fragments.add(yonghuPingjiaFragment);
        fragments.add(xiangmuFragment);


        adapter = new CommonPagerAdapter(getSupportFragmentManager(), fragments);
        vpList.setAdapter(adapter);

        vpList.setOffscreenPageLimit(4);
    }

    private void selectType(int i) {
        if (type == i) {
            return;
        }
        type = i;

        l1.setVisibility(View.INVISIBLE);
        l2.setVisibility(View.INVISIBLE);
        l3.setVisibility(View.INVISIBLE);

        zonghe.setTextColor(Color.parseColor("#ABABAB"));
        xiangmu.setTextColor(Color.parseColor("#ABABAB"));
        yonghu.setTextColor(Color.parseColor("#ABABAB"));

        switch (type) {
            case 1:
                zonghe.setTextColor(getResources().getColor(R.color.c_FF6806));
                l1.setVisibility(View.VISIBLE);
                break;
            case 2:
                yonghu.setTextColor(getResources().getColor(R.color.c_FF6806));
                l2.setVisibility(View.VISIBLE);
                break;
            case 3:
                xiangmu.setTextColor(getResources().getColor(R.color.c_FF6806));
                l3.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }


    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

}
