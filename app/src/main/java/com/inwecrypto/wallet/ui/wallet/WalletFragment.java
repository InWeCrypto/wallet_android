package com.inwecrypto.wallet.ui.wallet;

import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.db.CacheManager;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import com.inwecrypto.wallet.AppApplication;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.LoginBean;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.bean.UserInfoBean;
import com.inwecrypto.wallet.bean.WalletCountBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.UserApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.imageloader.GlideCircleTransform;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.event.RefreshEvent;
import com.inwecrypto.wallet.ui.MainTabActivity;
import com.inwecrypto.wallet.ui.wallet.activity.MessageActivity;
import com.inwecrypto.wallet.ui.wallet.adapter.MainGntAdapter;

/**
 * Created by Administrator on 2017/7/15.
 * 功能描述：
 * 版本：@version
 */

public class WalletFragment extends BaseFragment {

    @BindView(R.id.wallet_list)
    RecyclerView walletList;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayoutCompat swipeRefresh;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.tv_eth_price)
    TextView tvEthPrice;
    @BindView(R.id.tv_eth_ch_price)
    TextView tvEthChPrice;
    @BindView(R.id.ll_eth)
    LinearLayout llEth;
    @BindView(R.id.tv_btc_price)
    TextView tvBtcPrice;
    @BindView(R.id.tv_btc_ch_price)
    TextView tvBtcChPrice;
    @BindView(R.id.ll_btc)
    LinearLayout llBtc;
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.titlell)
    View titlell;
    @BindView(R.id.titlePrice)
    TextView titlePrice;
    @BindView(R.id.amin_title)
    TextView mainTitle;
    @BindView(R.id.line)
    View line;

    private BigDecimal ETHEther = new BigDecimal("0.00");
    private BigDecimal ETHPrice = new BigDecimal("0.00");
    private BigDecimal pEther = new BigDecimal("1000000000000000000");

    private ArrayList<TokenBean.ListBean> data = new ArrayList<>();
    private MainGntAdapter adapter;
    private LoginBean loginBean;
    private boolean isFinish;
    private BigDecimal tokenPrice=new BigDecimal(0);

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_fragment;
    }

    @Override
    protected void initView() {
        isOpenEventBus = true;
        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    swipeRefresh.setEnabled(true);
                } else {
                    swipeRefresh.setEnabled(false);
                }
                if (verticalOffset==0){
                    titlell.setVisibility(View.INVISIBLE);
                    mainTitle.setVisibility(View.VISIBLE);
                    line.setVisibility(View.INVISIBLE);
                }
                if (verticalOffset+appBarLayout.getTotalScrollRange()==0){
                    titlell.setVisibility(View.VISIBLE);
                    mainTitle.setVisibility(View.INVISIBLE);
                    line.setVisibility(View.VISIBLE);
                    if (!isFinish){
                        startShowAnimation();
                        isFinish=true;
                    }
                }else {
                    if (isFinish){
                        startHideAnimation();
                        isFinish=false;
                    }
                }
            }
        });

        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BaseEventBusBean(Constant.OPEN_CLOSE_MENU));
            }
        });
        Drawable drawable = getResources().getDrawable(R.mipmap.nav_menu);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        txtLeftTitle.setCompoundDrawables(drawable, null, null, null);

        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keepTogo(MessageActivity.class);
            }
        });
        Drawable drawableInfo = getResources().getDrawable(R.mipmap.nav_info);
        /// 这一步必须要做,否则不会显示.
        drawableInfo.setBounds(0, 0, drawableInfo.getMinimumWidth(), drawableInfo.getMinimumHeight());
        txtRightTitle.setCompoundDrawables(drawableInfo, null, null, null);

        if (AppApplication.get().getSp().getBoolean(Constant.IS_CLOD, false)) {
            txtRightTitle.setVisibility(View.GONE);
            titlell.setVisibility(View.GONE);
            Glide.with(this)
                    .load(R.mipmap.clod_icon)
                    .crossFade()
                    .transform(new GlideCircleTransform(mContext))
                    .into(ivHeader);
            swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_WALLET));
                }
            });
        } else {
            setUserInfo();
            adapter = new MainGntAdapter(mContext, R.layout.wallet_main_item, data);
            walletList.setLayoutManager(new LinearLayoutManager(mContext));
            walletList.setAdapter(adapter);
            walletList.getItemAnimator().setChangeDuration(0);
            ((SimpleItemAnimator)walletList.getItemAnimator()).setSupportsChangeAnimations(false);
            swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_WALLET));
                }
            });

            walletList.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    int topRowVerticalPosition =
                            (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                    swipeRefresh.setEnabled(topRowVerticalPosition >= 0);
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });
        }
        if (1== AppApplication.get().getUnit()){
            tvEthChPrice.setText("≈￥0.00");
            tvBtcChPrice.setText("≈￥0.00");
        }else {
            tvEthChPrice.setText("≈$0.00");
            tvBtcChPrice.setText("≈$0.00");
        }
    }

    private void setUserInfo() {
        loginBean = AppApplication.get().getLoginBean();
        if (null==loginBean){
            return;
        }
        if (null != loginBean.getUser().getImg() && loginBean.getUser().getImg().length() > 0) {
            Glide.with(this)
                    .load(loginBean.getUser().getImg())
                    .crossFade()
                    .placeholder(R.mipmap.clod_icon)
                    .transform(new GlideCircleTransform(mContext))
                    .into(ivHeader);
        }
        tvName.setText(loginBean.getUser().getNickname());
    }

    @Override
    protected void loadData() {
        if (!AppApplication.get().getSp().getBoolean(Constant.IS_CLOD, false)) {
            swipeRefresh.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefresh.setRefreshing(true);
                }
            });
            //获取用户信息
            getUserInfo();
        }
    }

    private void getUserInfo() {
        UserApi.userInfo(mFragment, new JsonCallback<LzyResponse<UserInfoBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<UserInfoBean>> response) {
                UserInfoBean.UserBean user=response.body().data.getUser();
                if (null==user.getNickname()){
                    user.setNickname(AppApplication.get().getLoginBean().getUser().getOpen_id().substring(0,12));
                }
                if (null==user.getImg()){
                    user.setImg("1");
                }
                loginBean.getUser().setSex(user.getSex());
                AppApplication.get().getSp().putString(Constant.USER_INFO, GsonUtils.objToJson(loginBean));
                if (null != loginBean.getUser().getImg() && loginBean.getUser().getImg().length() > 0) {
                    Glide.with(mActivity)
                            .load(loginBean.getUser().getImg())
                            .crossFade()
                            .placeholder(R.mipmap.clod_icon)
                            .transform(new GlideCircleTransform(mContext))
                            .into(ivHeader);
                }
                tvName.setText(loginBean.getUser().getNickname());
                EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_USERINFO));
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden&&!AppApplication.get().getSp().getBoolean(Constant.IS_CLOD, false)) {
            EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_WALLET));
        }else {
            EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_DRAWLAYOUT));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        swipeRefresh.setRefreshing(false);
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_MAIN_REFERSH_COMP){
            swipeRefresh.setRefreshing(false);
        }
        if (event.getEventCode() == Constant.EVENT_USERINFO) {
            setUserInfo();
        }
        if (event.getEventCode() == Constant.EVENT_MAIN_PRICE) {
            swipeRefresh.setRefreshing(false);
            RefreshEvent main = (RefreshEvent) event.getData();
            HashMap<String, String> params = main.getParams();
            ETHEther=new BigDecimal(params.get("price"));
            ETHPrice=new BigDecimal(params.get("price"));
            if (1== AppApplication.get().getUnit()){
                tvEthPrice.setText(params.get("ether"));
                tvEthChPrice.setText("≈￥" + ETHEther.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
                tvBtcChPrice.setText("≈￥0.00");
            }else {
                tvEthPrice.setText(params.get("ether"));
                tvEthChPrice.setText("≈$" + ETHEther.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
                tvBtcChPrice.setText("≈$0.00");
            }
        }
        if (event.getEventCode() == Constant.EVENT_TOTLE_GNT) {
            swipeRefresh.setRefreshing(false);
            ArrayList<TokenBean.ListBean> totle = new ArrayList<>();
            MainTabActivity mActivity = (MainTabActivity) this.mActivity;
            HashMap gants = mActivity.getTotleGnt();
            Iterator iter = gants.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                TokenBean.ListBean val = (TokenBean.ListBean) entry.getValue();
                totle.add(val);
            }
            data.clear();
            data.addAll(totle);
            adapter.notifyDataSetChanged();

            BigDecimal tokenPrice=new BigDecimal(0);
            //计算总资产
            for (TokenBean.ListBean token:data){
                BigDecimal currentPrice = new BigDecimal(token.getBalance());
                if (1== AppApplication.get().getUnit()){
                    tokenPrice=tokenPrice.add(currentPrice.multiply(new BigDecimal(token.getGnt_category().getCap().getPrice_cny())));
                }else {
                    tokenPrice=tokenPrice.add(currentPrice.multiply(new BigDecimal(token.getGnt_category().getCap().getPrice_usd())));
                }
            }
            if (1== AppApplication.get().getUnit()){
                price.setText("￥" + ETHPrice.add(tokenPrice).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                titlePrice.setText("(￥"+ETHPrice.add(tokenPrice).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()+")");
            }else {
                price.setText("$" + ETHPrice.add(tokenPrice).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                titlePrice.setText("($"+ETHPrice.add(tokenPrice).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()+")");
            }
        }

        if (event.getEventCode() == Constant.EVENT_TOTLE_PRICE||event.getEventCode() == Constant.EVENT_TOTLE_PRICE_SERVICE) {
            setPrice();
        }
    }

    private void setPrice() {
        CacheEntity<LzyResponse<CommonListBean<WalletCountBean>>> wallets = (CacheEntity<LzyResponse<CommonListBean<WalletCountBean>>>) CacheManager.getInstance().get(Constant.CONVERSION+AppApplication.isMain);
        if (null != wallets && null != wallets.getData() && null != wallets.getData().data.getList()) {
            ArrayList<WalletCountBean> walletCount = wallets.getData().data.getList();
            ETHEther = ETHEther.multiply(new BigDecimal(0));
            ETHPrice = ETHPrice.multiply(new BigDecimal(0));
            for (WalletCountBean count : walletCount) {
                if (null != count.getBalance()) {
                    BigDecimal currentPrice = new BigDecimal(AppUtil.toD(count.getBalance().replace("0x", "")));
                    ETHEther = ETHEther.add(currentPrice);
                    if (1== AppApplication.get().getUnit()){
                        ETHPrice = ETHPrice.add(currentPrice.divide(pEther).multiply(new BigDecimal(count.getCategory().getCap().getPrice_cny())));
                    }else {
                        ETHPrice = ETHPrice.add(currentPrice.divide(pEther).multiply(new BigDecimal(count.getCategory().getCap().getPrice_usd())));
                    }
                }
            }

            if (1== AppApplication.get().getUnit()){
                tvEthPrice.setText(ETHEther.divide(pEther, 4, BigDecimal.ROUND_HALF_UP).toString());
                tvEthChPrice.setText("≈￥" + ETHPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }else {
                tvEthPrice.setText(ETHEther.divide(pEther, 4, BigDecimal.ROUND_HALF_UP).toString());
                tvEthChPrice.setText("≈$" + ETHPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
        }
        String gnt ="";
        if (AppApplication.isMain){
            gnt = AppApplication.get().getSp().getString(Constant.TOTOLE_GNT);
        }else {
            gnt = AppApplication.get().getSp().getString(Constant.TOTOLE_GNT_TEST);
        }
        if (null != gnt && gnt.length() > 0) {
            TokenBean tokenBean = GsonUtils.jsonToObj(gnt, TokenBean.class);
            data.clear();
            data.addAll(tokenBean.getList());
            adapter.notifyDataSetChanged();
        }
        tokenPrice=new BigDecimal(0);
        //计算总资产
        for (TokenBean.ListBean token:data){
            BigDecimal currentPrice = new BigDecimal(token.getBalance());
            if (1== AppApplication.get().getUnit()){
                tokenPrice=tokenPrice.add(currentPrice.multiply(new BigDecimal(token.getGnt_category().getCap().getPrice_cny())));
            }else {
                tokenPrice=tokenPrice.add(currentPrice.multiply(new BigDecimal(token.getGnt_category().getCap().getPrice_usd())));
            }
        }
        if (1== AppApplication.get().getUnit()){
            price.setText("￥" + ETHPrice.add(tokenPrice).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
            titlePrice.setText("(￥"+ETHPrice.add(tokenPrice).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()+")");
        }else {
            price.setText("$" + ETHPrice.add(tokenPrice).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
            titlePrice.setText("($"+ETHPrice.add(tokenPrice).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()+")");
        }
    }

    public void startHideAnimation(){
        //清除动画
        titlell.clearAnimation();
        /**
         * @param fromAlpha 开始的透明度，取值是0.0f~1.0f，0.0f表示完全透明， 1.0f表示和原来一样
         * @param toAlpha 结束的透明度，同上
         */
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        //设置动画持续时长
        alphaAnimation.setDuration(300);
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
        alphaAnimation.setFillAfter(true);
        //开始动画
        titlell.startAnimation(alphaAnimation);
    }

    public void startShowAnimation(){
        //清除动画
        titlell.clearAnimation();
        /**
         * @param fromAlpha 开始的透明度，取值是0.0f~1.0f，0.0f表示完全透明， 1.0f表示和原来一样
         * @param toAlpha 结束的透明度，同上
         */
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        //设置动画持续时长
        alphaAnimation.setDuration(300);
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
        alphaAnimation.setFillAfter(true);
        //开始动画
        titlell.startAnimation(alphaAnimation);
    }

}
