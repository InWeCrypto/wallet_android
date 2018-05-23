package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.bean.ArticleDetaileBean;
import com.inwecrypto.wallet.bean.ArticleListBean;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.CommonProjectBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.CacheUtils;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.AutoLoopViewPager;
import com.inwecrypto.wallet.common.widget.NoScrollViewPager;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.event.KeyEvent;
import com.inwecrypto.wallet.ui.QuickActivity;
import com.inwecrypto.wallet.ui.ScanActivity;
import com.inwecrypto.wallet.ui.login.LoginActivity;
import com.inwecrypto.wallet.ui.me.adapter.CommonPagerAdapter;
import com.inwecrypto.wallet.ui.newneo.CreateWalletFragment;
import com.inwecrypto.wallet.ui.news.fragment.CProjectFragment;
import com.inwecrypto.wallet.ui.news.fragment.ProjectFragment;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * 作者：xiaoji06 on 2018/2/6 16:12
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ZixunFragment extends BaseFragment implements AutoLoopViewPager.OnGetAdsViewPager {


    @BindView(R.id.searchfl)
    FrameLayout searchfl;
    @BindView(R.id.more)
    ImageView more;
    @BindView(R.id.scroll)
    AutoLoopViewPager scroll;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.vptext)
    TextView vptext;
    @BindView(R.id.hotll)
    LinearLayout hotll;
    @BindView(R.id.hot)
    View hot;
    @BindView(R.id.filpper)
    ViewFlipper filpper;
    @BindView(R.id.dongtaiimg)
    ImageView dongtaiimg;
    @BindView(R.id.dongtaihot)
    TextView dongtaihot;
    @BindView(R.id.dongtai)
    RelativeLayout dongtai;
    @BindView(R.id.guandianimg)
    ImageView guandianimg;
    @BindView(R.id.guandianhot)
    TextView guandianhot;
    @BindView(R.id.guandian)
    RelativeLayout guandian;
    @BindView(R.id.qiwangimg)
    ImageView qiwangimg;
    @BindView(R.id.qiwanghot)
    TextView qiwanghot;
    @BindView(R.id.qiwang)
    RelativeLayout qiwang;
    @BindView(R.id.paihangimg)
    ImageView paihangimg;
    @BindView(R.id.paihanghot)
    TextView paihanghot;
    @BindView(R.id.paihang)
    RelativeLayout paihang;
    @BindView(R.id.tongzhiimg)
    ImageView tongzhiimg;
    @BindView(R.id.tongzhihot)
    TextView tongzhihot;
    @BindView(R.id.tongzhi)
    RelativeLayout tongzhi;
    @BindView(R.id.shoucang)
    TextView shoucang;
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
    @BindView(R.id.refresh)
    ImageView refresh;
    @BindView(R.id.vp_list)
    NoScrollViewPager vpList;
    @BindView(R.id.dongtaitxt)
    TextView dongtaitxt;
    @BindView(R.id.guandiantxt)
    TextView guandiantxt;
    @BindView(R.id.qiwangtxt)
    TextView qiwangtxt;
    @BindView(R.id.paihangtxt)
    TextView paihangtxt;
    @BindView(R.id.tongzhitxt)
    TextView tongzhitxt;
    Unbinder unbinder;

    private ArrayList<CommonProjectBean> marks = new ArrayList<>();

    private int type = -1;

    private ArrayList<BaseFragment> fragments;
    private CProjectFragment shoucangFragment;
    private ProjectFragment tradingFragment;

    private CommonPagerAdapter adapter;

    private int[] projectNames = new int[]{R.string.dongtai, R.string.guandian, R.string.qiwang, R.string.paihang, R.string.tongzhi};
    private int[] projectImgs = new int[]{R.mipmap.zhuye_inwe_ico, R.mipmap.zhuye_guandianxxhdpi, R.mipmap.zhuye_tradingview_ico, R.mipmap.zhuye_paihangxxhdpi, R.mipmap.tongzhichakan_tongzhi};

    private ArrayList<WalletBean> wallet = new ArrayList<>();

    private SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private ArrayList<ArticleDetaileBean> scrollData = new ArrayList<>();

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

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) scroll.getLayoutParams();
        params.height = (int) (ScreenUtils.getScreenWidth(mContext) / 750.0 * 373.0);
        scroll.setLayoutParams(params);

        scroll.getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                vptext.setText(scrollData.get(position % scrollData.size()).getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (0 == verticalOffset) {
                    hotll.setVisibility(View.INVISIBLE);
                    if (null != filpper) {
                        filpper.stopFlipping();
                    }
                    vptext.setVisibility(View.VISIBLE);
                } else {
                    hotll.setVisibility(View.VISIBLE);
                    vptext.setVisibility(View.INVISIBLE);
                    if (null != filpper) {
                        filpper.startFlipping();
                    }
                }
            }
        });

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

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != refresh && null != refresh.getAnimation()) {
                    if (refresh.getAnimation().hasStarted()) {
                        return;
                    }
                }
                startAnimat();
                switch (vpList.getCurrentItem()) {
                    case 0:
                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFERSH, 0));
                        break;
                    case 1:
                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_REFERSH, 1));
                        break;
                }
                loadData();
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

        if (App.get().getSp().getBoolean(Constant.FIRST_3, true)) {
            vpList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(mActivity, QuickActivity.class);
                    intent.putExtra("type", 3);
                    keepTogo(intent);
                }
            }, 300);
        }
    }


    private void setCommonProject() {
        //获取MARKS缓存文件
        ArrayList<CommonProjectBean> cacheMarks = CacheUtils.getCache(((App.isMain ? Constant.PROJECT_JSON_MAIN : Constant.PROJECT_JSON_TEST)
                + (null == App.get().getLoginBean() ? "" : App.get().getLoginBean().getEmail())));
        marks.clear();
        marks.addAll(cacheMarks);

        for (int i = 0; i < marks.size(); i++) {
            marks.get(i).setName(projectNames[i]);
            marks.get(i).setImg(projectImgs[i]);
        }

        dongtai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(marks.get(0).getChatId());//指定会话消息未读数清零
                if (null != conversation) {
                    conversation.markAllMessagesAsRead();
                }
                marks.get(0).setHasMessage(false);
                marks.get(0).setMessageCount(0);
                CacheUtils.setCache((App.isMain ? Constant.PROJECT_JSON_MAIN : Constant.PROJECT_JSON_TEST)
                        + (null == App.get().getLoginBean() ? "" : App.get().getLoginBean().getEmail()), marks);
                resetUI();

                Intent intent = new Intent(mActivity, InweHotActivity.class);
                intent.putExtra("marks", marks.get(0));
                keepTogo(intent);

            }
        });

        guandian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(marks.get(1).getChatId());//指定会话消息未读数清零
                if (null != conversation) {
                    conversation.markAllMessagesAsRead();
                }
                marks.get(1).setHasMessage(false);
                marks.get(1).setMessageCount(0);
                CacheUtils.setCache((App.isMain ? Constant.PROJECT_JSON_MAIN : Constant.PROJECT_JSON_TEST)
                        + (null == App.get().getLoginBean() ? "" : App.get().getLoginBean().getEmail()), marks);
                resetUI();

                Intent intent = new Intent(mActivity, InweViewActivity.class);
                intent.putExtra("marks", marks.get(1));
                keepTogo(intent);

            }
        });

        qiwang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(marks.get(2).getChatId());//指定会话消息未读数清零
                if (null != conversation) {
                    conversation.markAllMessagesAsRead();
                }
                marks.get(2).setHasMessage(false);
                marks.get(2).setMessageCount(0);
                CacheUtils.setCache((App.isMain ? Constant.PROJECT_JSON_MAIN : Constant.PROJECT_JSON_TEST)
                        + (null == App.get().getLoginBean() ? "" : App.get().getLoginBean().getEmail()), marks);
                resetUI();

                Intent intent = new Intent(mActivity, TradingViewActivity.class);
                intent.putExtra("marks", marks.get(2));
                keepTogo(intent);
            }
        });

        paihang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               keepTogo(RankingActivity.class);
            }
        });

        tongzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()) {
                    keepTogo(LoginActivity.class);
                    return;
                }

                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(marks.get(4).getType());//指定会话消息未读数清零
                if (null != conversation) {
                    conversation.markAllMessagesAsRead();
                }
                marks.get(4).setHasMessage(false);
                marks.get(4).setMessageCount(0);
                CacheUtils.setCache((App.isMain ? Constant.PROJECT_JSON_MAIN : Constant.PROJECT_JSON_TEST)
                        + (null == App.get().getLoginBean() ? "" : App.get().getLoginBean().getEmail()), marks);
                resetUI();

                Intent intent = new Intent(mActivity, NoticeActivity.class);
                intent.putExtra("marks", marks.get(4));
                keepTogo(intent);

            }
        });
    }

    private void getIM() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();

                Iterator<Map.Entry<String, EMConversation>> it = conversations.entrySet().iterator();

                while (it.hasNext()) {
                    Map.Entry<String, EMConversation> entry = it.next();

                    for (CommonProjectBean mark : marks) {
                        if (!entry.getKey().equals(mark.getType())) {
                            //根据群组ID从服务器获取群组基本信息
                            try {
                                EMGroup group = EMClient.getInstance().groupManager().getGroupFromServer(entry.getKey());
                                if (group.getGroupName().contains(mark.getType().toUpperCase())) {
                                    mark.setChatId(entry.getKey());
                                } else {
                                    continue;
                                }
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                continue;
                            }
                        }
                        EMConversation message = conversations.get(entry.getKey());
                        EMTextMessageBody body = (EMTextMessageBody) message.getLastMessage().getBody();
                        mark.setTime("" + sdr.format(new Date(message.getLastMessage().getMsgTime())));
                        String meg = body.getMessage().replace(":date", mark.getTime());
                        if (mark.getType().equals("sys_msg_exchangenotice")) {
                            mark.setLastMessage(AppUtil.delHTMLTag(meg));
                        } else {
                            mark.setLastMessage(meg);
                        }
                        int count = message.getUnreadMsgCount();
                        if (count > 0) {
                            mark.setHasMessage(true);
                            mark.setMessageCount(count);
                        }
                        break;
                    }

                }

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resetUI();
                    }
                });

                CacheUtils.setCache((App.isMain ? Constant.PROJECT_JSON_MAIN : Constant.PROJECT_JSON_TEST)
                        + (null == App.get().getLoginBean() ? "" : App.get().getLoginBean().getEmail()), marks);
            }
        }).start();
    }

    private void resetUI() {
        for (int i = 0; i < marks.size(); i++) {
            if (marks.get(i).isHasMessage()) {
                switch (i) {
                    case 0:
                        dongtaihot.setVisibility(View.VISIBLE);
                        dongtaihot.setText("" + marks.get(0).getMessageCount());
                        break;
                    case 1:
                        guandianhot.setVisibility(View.VISIBLE);
                        guandianhot.setText("" + marks.get(1).getMessageCount());
                        break;
                    case 2:
                        qiwanghot.setVisibility(View.VISIBLE);
                        qiwanghot.setText("" + marks.get(2).getMessageCount());
                        break;
                    case 3:
                        break;
                    case 4:
                        tongzhihot.setVisibility(View.VISIBLE);
                        tongzhihot.setText("" + marks.get(4).getMessageCount());
                        break;
                }
            } else {
                switch (i) {
                    case 0:
                        dongtaihot.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        guandianhot.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        qiwanghot.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        break;
                    case 4:
                        tongzhihot.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        shoucang.setText(R.string.zixuan);
        trading.setText(R.string.trading);
        dongtaitxt.setText(R.string.dongtai);
        guandiantxt.setText(R.string.guandian);
        qiwangtxt.setText(R.string.qiwang);
        paihangtxt.setText(R.string.paihang);
        tongzhitxt.setText(R.string.tongzhi);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (null != filpper) {
                filpper.stopFlipping();
            }
        } else {
            if (null != filpper) {
                filpper.startFlipping();
            }
        }
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

        fragments = new ArrayList<>();
        fragments.add(shoucangFragment);
        fragments.add(tradingFragment);

        adapter = new CommonPagerAdapter(getChildFragmentManager(), fragments);
        vpList.setAdapter(adapter);

        vpList.setCurrentItem(2);
    }

    @Override
    protected void loadData() {
        OkGo.getInstance().cancelTag(this);
        //获取轮播
        ZixunApi.getScrollInweHot(this, new JsonCallback<LzyResponse<ArticleListBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArticleListBean>> response) {
                scrollData.clear();
                filpper.stopFlipping();
                filpper.removeAllViewsInLayout();
                //加载轮播数据
                if (null != response.body().data.getData()) {
                    scrollData.addAll(response.body().data.getData());
                }
                for (int i = 0; i < scrollData.size(); i++) {
                    View view = LayoutInflater.from(mContext).inflate(R.layout.home_filpper_item, null);
                    ((TextView) view.findViewById(R.id.title)).setText(scrollData.get(i).getTitle());
                    final int finalI = i;
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mActivity, ProjectNewsWebActivity.class);
                            intent.putExtra("title", scrollData.get(finalI).getTitle());
                            intent.putExtra("url", (App.isMain ? Url.MAIN_NEWS : Url.TEST_NEWS) + scrollData.get(finalI).getId());
                            intent.putExtra("id", scrollData.get(finalI).getId());
                            intent.putExtra("decs", scrollData.get(finalI).getDesc());
                            intent.putExtra("img", scrollData.get(finalI).getImg());
                            keepTogo(intent);
                        }
                    });
                    filpper.addView(view);
                }
                filpper.startFlipping();
                scroll.setPagerAdapter(ZixunFragment.this, response.body().data.getData());
                scroll.setOnItemClickListener(new AutoLoopViewPager.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        int rPosition = position % scrollData.size();
                        Intent intent = new Intent(mActivity, ProjectNewsWebActivity.class);
                        intent.putExtra("title", scrollData.get(rPosition).getTitle());
                        intent.putExtra("url", (App.isMain ? Url.MAIN_NEWS : Url.TEST_NEWS) + scrollData.get(rPosition).getId());
                        intent.putExtra("id", scrollData.get(rPosition).getId());
                        intent.putExtra("decs", scrollData.get(rPosition).getDesc());
                        intent.putExtra("img", scrollData.get(rPosition).getImg());
                        keepTogo(intent);
                    }
                });
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<ArticleListBean>> response) {
                super.onCacheSuccess(response);
            }
        });

        //获取消息
        getIM();
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_REFERSH_SUC) {
            stopAnimat();
        }

        if (event.getEventCode() == Constant.EVENT_ZIXUN_MESSAGE) {
            getIM();
        }

        if (event.getEventCode() == Constant.EVENT_HONGBAO_GET){
            Intent intent=new Intent(mActivity,ScanHongbaoActivity.class);
            intent.putExtra("url",((KeyEvent)event.getData()).getKey());
            keepTogo(intent);
            EventBus.getDefault().removeAllStickyEvents();
        }

        if (event.getEventCode() == Constant.EVENT_KEY){
            ToastUtil.show(getString(R.string.qingsaomiaoinwehongbao));
            EventBus.getDefault().removeAllStickyEvents();
        }

    }

    private void selectType(int i) {
        if (type == i) {
            return;
        }
        vpList.setCurrentItem(i - 1);
        type = i;
        l1.setVisibility(View.INVISIBLE);
        l2.setVisibility(View.INVISIBLE);

        shoucang.setTextColor(getResources().getColor(R.color.c_D8D8D8));
        trading.setTextColor(getResources().getColor(R.color.c_D8D8D8));

        switch (type) {
            case 1:
                shoucang.setTextColor(getResources().getColor(R.color.c_FF6806));
                l1.setVisibility(View.VISIBLE);
                break;
            case 2:
                trading.setTextColor(getResources().getColor(R.color.c_FF6806));
                l2.setVisibility(View.VISIBLE);
                break;
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private void showaMoreDialog() {

        View selectPopupWin = LayoutInflater.from(mContext).inflate(R.layout.view_popup_zixun, null, false);
        View saoyisao = selectPopupWin.findViewById(R.id.saoyisao);
        View addWallet = selectPopupWin.findViewById(R.id.addWallet);
        View shoufukuan = selectPopupWin.findViewById(R.id.shoufukuan);
        View hongbao = selectPopupWin.findViewById(R.id.hongbao);


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


        hongbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()) {
                    keepTogo(LoginActivity.class);
                    return;
                }
                Intent intent=new Intent(getActivity(),HongbaoActivity.class);
                keepTogo(intent);
                window.dismiss();
            }
        });

        saoyisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()) {
                    keepTogo(LoginActivity.class);
                    return;
                }
                Intent intent=new Intent(getActivity(),ScanActivity.class);
                keepTogo(intent);
                window.dismiss();
            }
        });

        addWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()) {
                    keepTogo(LoginActivity.class);
                    return;
                }
                WalletApi.wallet(mActivity, new JsonCallback<LzyResponse<CommonListBean<WalletBean>>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                        wallet.clear();
                        if (null != response.body().data.getList()) {
                            String wallets = App.get().getSp().getString(Constant.WALLETS, "").toLowerCase();
                            String wallets_beifen = App.get().getSp().getString(Constant.WALLETS_BEIFEN, "").toLowerCase();
                            String walletsZjc = App.get().getSp().getString(Constant.WALLETS_ZJC_BEIFEN, "").toLowerCase();
                            for (int i = 0; i < response.body().data.getList().size(); i++) {
                                if (wallets.contains(response.body().data.getList().get(i).getAddress().toLowerCase())) {
                                    if (wallets_beifen.contains(response.body().data.getList().get(i).getAddress().toLowerCase()) || walletsZjc.contains(response.body().data.getList().get(i).getAddress().toLowerCase())) {
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

                        FragmentManager fm = getChildFragmentManager();
                        CreateWalletFragment create = new CreateWalletFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("wallets", wallet);
                        create.setArguments(bundle);
                        create.show(fm, "create");
                        window.dismiss();
                    }

                    @Override
                    public void onCacheSuccess(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                        super.onCacheSuccess(response);

                    }
                });
            }
        });

        shoufukuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()) {
                    keepTogo(LoginActivity.class);
                    return;
                }
                Intent intent = new Intent(mActivity, ReciveActivity.class);
                keepTogo(intent);
                window.dismiss();
            }
        });
    }

    private RotateAnimation rotate;
    private boolean isFinish = true;

    public void startAnimat() {
        if (refresh == null) {
            return;
        }
        refresh.clearAnimation();

        rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setDuration(1600);//设置动画持续周期
        rotate.setRepeatCount(-1);//设置重复次数
        rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        rotate.setStartOffset(10);//执行前的等待时间
        refresh.setAnimation(rotate);
    }

    public void stopAnimat() {
        if (refresh == null) {
            return;
        }
        refresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (rotate != null) {
                    rotate.cancel();
                }
                if (refresh != null) {
                    refresh.clearAnimation();
                }
                isFinish = true;
            }
        }, 1600);
    }

    @Override
    public AutoLoopViewPager getAdsViewPager() {
        return scroll;
    }

}
