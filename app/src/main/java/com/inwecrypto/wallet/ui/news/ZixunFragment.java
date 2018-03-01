package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.CommonProjectBean;
import com.inwecrypto.wallet.bean.PositionBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.MaterialDialog;
import com.inwecrypto.wallet.common.widget.NoScrollViewPager;
import com.inwecrypto.wallet.common.widget.pullextend.ExtendListHeader;
import com.inwecrypto.wallet.common.widget.pullextend.PullExtendLayout;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.me.adapter.CommonPagerAdapter;
import com.inwecrypto.wallet.ui.newneo.CreateWalletFragment;
import com.inwecrypto.wallet.ui.newneo.NewTransferWalletActivity;
import com.inwecrypto.wallet.ui.news.adapter.ExtendMarkAdapter;
import com.inwecrypto.wallet.ui.news.fragment.CProjectFragment;
import com.inwecrypto.wallet.ui.news.fragment.ProjectFragment;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：xiaoji06 on 2018/2/6 16:12
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ZixunFragment extends BaseFragment {


    @BindView(R.id.extend_header)
    ExtendListHeader extendHeader;
    @BindView(R.id.shoucang)
    ImageView shoucang;
    @BindView(R.id.l1)
    View l1;
    @BindView(R.id.shoucangll)
    LinearLayout shoucangll;
    @BindView(R.id.trading)
    TextView trading;
    @BindView(R.id.l2)
    View l2;
    @BindView(R.id.tradingll)
    LinearLayout tradingll;
    @BindView(R.id.active)
    TextView active;
    @BindView(R.id.l3)
    View l3;
    @BindView(R.id.activell)
    LinearLayout activell;
    @BindView(R.id.upcoming)
    TextView upcoming;
    @BindView(R.id.l4)
    View l4;
    @BindView(R.id.upcomingll)
    LinearLayout upcomingll;
    @BindView(R.id.ended)
    TextView ended;
    @BindView(R.id.l5)
    View l5;
    @BindView(R.id.endedll)
    LinearLayout endedll;
    @BindView(R.id.refresh)
    ImageView refresh;
    @BindView(R.id.vp_list)
    NoScrollViewPager vpList;
    @BindView(R.id.pull_extend)
    PullExtendLayout pullExtend;
    @BindView(R.id.searchfl)
    FrameLayout searchfl;
    @BindView(R.id.more)
    ImageView more;

    private RecyclerView listHeader;
    private ExtendMarkAdapter mExtendMarkAdapter;
    private ArrayList<CommonProjectBean> marks = new ArrayList<>();
    private ArrayList<CommonProjectBean> topMarks = new ArrayList<>();

    private int[] projectNames = new int[]{R.string.inweerdian, R.string.jiaoyishitu, R.string.jiaoyisuogonggao, R.string.tangguohe, R.string.jiaoyitixing, R.string.tongzhi};
    private int[] projectImgs = new int[]{R.mipmap.zhuye_inwe_ico, R.mipmap.zhuye_tradingview_ico, R.mipmap.zhuye_jiaoyisuo_ico, R.mipmap.zhuye_candybowl_ico, R.mipmap.zhuye_jiaoyitixing_ico, R.mipmap.tongzhichakan_tongzhi};

    private int type = -1;

    private ArrayList<BaseFragment> fragments;
    private CProjectFragment shoucangFragment;
    private ProjectFragment tradingFragment;
    private ProjectFragment activeFragment;
    private ProjectFragment upcomingFragment;
    private ProjectFragment endedFragment;

    private CommonPagerAdapter adapter;

    private int currentType = 1;

    private ArrayList<WalletBean> wallet = new ArrayList<>();

    @Override
    protected int setLayoutID() {
        return R.layout.zixun_fragment;
    }

    @Override
    protected void initView() {

        setOpenEventBus(true);

        //设置头部公共小程序
        setCommonProject();

        //设置布局
        setLayout();

        searchfl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ProjectSearchActivity.class);
                keepTogo(intent);
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showaMoreDialog();
            }
        });

        shoucangll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType(1);
            }
        });

        tradingll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType(2);
            }
        });

        activell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType(3);
            }
        });

        upcomingll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType(4);
            }
        });

        endedll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType(5);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimat();
                switch (vpList.getCurrentItem()){
                    case 0:
                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFERSH,0));
                        break;
                    case 1:
                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFERSH,1));
                        break;
                    case 2:
                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFERSH,2));
                        break;
                    case 3:
                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFERSH,3));
                        break;
                    case 4:
                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFERSH,4));
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
                selectType(position + 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }


    private void setCommonProject() {
        //获取缓存文件
        marks.clear();
        String projectJson = App.get().getSp().getString(App.isMain?Constant.PROJECT_JSON_MAIN:Constant.PROJECT_JSON_TEST, Constant.BASE_PROJECT_JSON);
        marks.addAll(GsonUtils.jsonToArrayList(projectJson, CommonProjectBean.class));
        for (int i = 0; i < 6; i++) {
            marks.get(i).setName(projectNames[i]);
            marks.get(i).setImg(projectImgs[i]);
            if (i!=3){
                topMarks.add(marks.get(i));
            }
        }

        listHeader = extendHeader.getRecyclerView();
        listHeader.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mExtendMarkAdapter = new ExtendMarkAdapter(mContext, R.layout.zixun_project_item, topMarks);
        listHeader.setAdapter(mExtendMarkAdapter);
        mExtendMarkAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = null;
                switch (topMarks.get(position).getId()) {
                    case 0:
                        intent = new Intent(mActivity, InweHotActivity.class);
                        break;
                    case 1:
                        intent = new Intent(mActivity, TradingViewActivity.class);
                        break;
                    case 2:
                        intent = new Intent(mActivity, ExchangeNoticeActivity.class);
                        break;
                    case 3:
                        intent = new Intent(mActivity, CandyBowActivity.class);
                        break;
                    case 4:
                        intent = new Intent(mActivity, TradingNoticeActivity.class);
                        break;
                    case 5:
                        intent = new Intent(mActivity, NoticeActivity.class);
                        break;
                }
                pullExtend.closeExtendHeadAndFooter();
                intent.putExtra("marks", topMarks.get(position));
                keepTogo(intent);
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_FIX,position));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        trading.setText(R.string.trading);
        active.setText(R.string.active);
        upcoming.setText(R.string.upcoming);
        ended.setText(R.string.ended);
    }

    private void setLayout() {
        shoucangFragment = new CProjectFragment();
        Bundle soucnagBundle = new Bundle();
        soucnagBundle.putSerializable("marks", marks);
        shoucangFragment.setArguments(soucnagBundle);

        tradingFragment = new ProjectFragment();
        Bundle tradingBundle = new Bundle();
        tradingBundle.putInt("type", 1);
        tradingFragment.setArguments(tradingBundle);

        activeFragment = new ProjectFragment();
        Bundle activeBundle = new Bundle();
        activeBundle.putInt("type", 2);
        activeFragment.setArguments(activeBundle);

        upcomingFragment = new ProjectFragment();
        Bundle upcomingBundle = new Bundle();
        upcomingBundle.putInt("type", 3);
        upcomingFragment.setArguments(upcomingBundle);

        endedFragment = new ProjectFragment();
        Bundle endedBundle = new Bundle();
        endedBundle.putInt("type", 4);
        endedFragment.setArguments(endedBundle);

        fragments = new ArrayList<>();
        fragments.add(shoucangFragment);
        fragments.add(tradingFragment);
        fragments.add(activeFragment);
        fragments.add(upcomingFragment);
        fragments.add(endedFragment);

        adapter = new CommonPagerAdapter(getChildFragmentManager(), fragments);
        vpList.setAdapter(adapter);
    }

    @Override
    protected void loadData() {
        WalletApi.wallet(mActivity, new JsonCallback<LzyResponse<CommonListBean<WalletBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                wallet.clear();
                if (null != response.body().data.getList()) {
                    String wallets = App.get().getSp().getString(Constant.WALLETS, "");
                    String wallets_beifen = App.get().getSp().getString(Constant.WALLETS_BEIFEN, "");
                    String walletsZjc = App.get().getSp().getString(Constant.WALLETS_ZJC_BEIFEN, "");
                    for (int i = 0; i < response.body().data.getList().size(); i++) {
                        if (wallets.contains(response.body().data.getList().get(i).getAddress())) {
                            if (wallets_beifen.contains(response.body().data.getList().get(i).getAddress()) || walletsZjc.contains(response.body().data.getList().get(i).getAddress())) {
                                response.body().data.getList().get(i).setType(Constant.BEIFEN);
                            } else {
                                response.body().data.getList().get(i).setType(Constant.ZHENGCHANG);
                            }
                        } else {
                            response.body().data.getList().get(i).setType(Constant.GUANCHA);
                        }
                        wallet.add(response.body().data.getList().get(i));
                    }
                }
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()==Constant.EVENT_REFERSH_SUC){
            stopAnimat();
        }
    }

    private void selectType(int i) {
        if (type == i) {
            return;
        }
        vpList.setCurrentItem(i - 1);
        pullExtend.setRecyclerView(fragments.get(i - 1).getList());
        type = i;
        l1.setVisibility(View.INVISIBLE);
        l2.setVisibility(View.INVISIBLE);
        l3.setVisibility(View.INVISIBLE);
        l4.setVisibility(View.INVISIBLE);
        l5.setVisibility(View.INVISIBLE);

        shoucang.setImageResource(R.mipmap.zhuye_jiaoyizhong_xing_ico);
        trading.setTextColor(getResources().getColor(R.color.c_D8D8D8));
        active.setTextColor(getResources().getColor(R.color.c_D8D8D8));
        upcoming.setTextColor(getResources().getColor(R.color.c_D8D8D8));
        ended.setTextColor(getResources().getColor(R.color.c_D8D8D8));

        switch (type) {
            case 1:
                shoucang.setImageResource(R.mipmap.xiangmuzhuye_xing_cio);
                l1.setVisibility(View.VISIBLE);
                break;
            case 2:
                trading.setTextColor(getResources().getColor(R.color.c_FF6806));
                l2.setVisibility(View.VISIBLE);
                break;
            case 3:
                active.setTextColor(getResources().getColor(R.color.c_FF6806));
                l3.setVisibility(View.VISIBLE);
                break;
            case 4:
                upcoming.setTextColor(getResources().getColor(R.color.c_FF6806));
                l4.setVisibility(View.VISIBLE);
                break;
            case 5:
                ended.setTextColor(getResources().getColor(R.color.c_FF6806));
                l5.setVisibility(View.VISIBLE);
                break;
        }
    }

    public ExtendListHeader getExtendHeader() {
        return extendHeader;
    }

    public void setExtendHeader(ExtendListHeader extendHeader) {
        this.extendHeader = extendHeader;
    }

    public PullExtendLayout getPullExtend() {
        return pullExtend;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPullExtend(PullExtendLayout pullExtend) {
        this.pullExtend = pullExtend;
    }

    private void showaMoreDialog() {

        View selectPopupWin = LayoutInflater.from(mContext).inflate(R.layout.view_popup_zixun, null, false);
        View saoyisao = selectPopupWin.findViewById(R.id.saoyisao);
        View addWallet = selectPopupWin.findViewById(R.id.addWallet);
        View shoufukuan = selectPopupWin.findViewById(R.id.shoufukuan);


        final PopupWindow window = new PopupWindow(selectPopupWin, DensityUtil.dip2px(mContext, 130), WindowManager.LayoutParams.WRAP_CONTENT);
        // 产生背景变暗效果
        WindowManager.LayoutParams lp = mActivity.getWindow()
                .getAttributes();
        lp.alpha = 0.4f;
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mActivity.getWindow().setAttributes(lp);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.update();
        window.showAsDropDown(more, 0, 0);
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


        saoyisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(R.string.jinqingqidai);
            }
        });

        addWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getChildFragmentManager();
                CreateWalletFragment create = new CreateWalletFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("wallets",wallet);
                create.setArguments(bundle);
                create.show(fm,"create");
                window.dismiss();
            }
        });

        shoufukuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,ReciveActivity.class);
                keepTogo(intent);
                window.dismiss();
            }
        });
    }
    private RotateAnimation rotate;
    private boolean isFinish=true;

    public  void startAnimat(){
        if (refresh==null){
            return;
        }
        refresh.clearAnimation();

        rotate  = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setDuration(1600);//设置动画持续周期
        rotate.setRepeatCount(-1);//设置重复次数
        rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        rotate.setStartOffset(10);//执行前的等待时间
        refresh.setAnimation(rotate);
    }

    public void stopAnimat(){
        if (refresh==null){
            return;
        }
        refresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (rotate!=null){
                        rotate.cancel();
                    }
                    if (refresh!=null){
                        refresh.clearAnimation();
                    }
                    isFinish=true;
                }
        },1600);
    }
}
