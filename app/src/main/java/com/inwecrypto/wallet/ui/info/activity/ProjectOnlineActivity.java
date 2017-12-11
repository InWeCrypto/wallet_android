package com.inwecrypto.wallet.ui.info.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.InfoMarketBean;
import com.inwecrypto.wallet.bean.InfoPriceBean;
import com.inwecrypto.wallet.bean.NewsBean;
import com.inwecrypto.wallet.bean.ProjectBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.InfoApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.MultiItemTypeSupport;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.info.adapter.InweInfoAdapter;
import com.inwecrypto.wallet.ui.info.adapter.MarketAdapter;
import com.inwecrypto.wallet.ui.info.adapter.SocalAdapter;
import com.inwecrypto.wallet.ui.me.activity.CommonWebActivity;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2017/11/13 17:14
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ProjectOnlineActivity extends BaseActivity {


    @BindView(R.id.back)
    FrameLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.serch)
    FrameLayout serch;
    @BindView(R.id.menu)
    FrameLayout menu;
    @BindView(R.id.state)
    TextView state;
    @BindView(R.id.quickInfo)
    TextView quickInfo;
    @BindView(R.id.usd)
    TextView usd;
    @BindView(R.id.line)
    TextView line;
    @BindView(R.id.btc)
    TextView btc;
    @BindView(R.id.liulanqi)
    TextView liulanqi;
    @BindView(R.id.zhichiqianbao)
    TextView zhichiqianbao;
    @BindView(R.id.charge)
    TextView charge;
    @BindView(R.id.usdVolume)
    TextView usdVolume;
    @BindView(R.id.btcVolume)
    TextView btcVolume;
    @BindView(R.id.volumeLL)
    LinearLayout volumeLL;
    @BindView(R.id.usdMax)
    TextView usdMax;
    @BindView(R.id.btcMax)
    TextView btcMax;
    @BindView(R.id.maxLL)
    LinearLayout maxLL;
    @BindView(R.id.usdMin)
    TextView usdMin;
    @BindView(R.id.btcMin)
    TextView btcMin;
    @BindView(R.id.minLL)
    LinearLayout minLL;
    @BindView(R.id.kLine)
    TextView kLine;
    @BindView(R.id.market_list)
    RecyclerView marketList;
    @BindView(R.id.moreMarket)
    LinearLayout moreMarket;
    @BindView(R.id.shipin)
    RadioButton shipin;
    @BindView(R.id.tuwen)
    RadioButton tuwen;
    @BindView(R.id.inwe_rg)
    RadioGroup inweRg;
    @BindView(R.id.inweList)
    RecyclerView inweList;
    @BindView(R.id.inweiLayout)
    LinearLayout inweiLayout;
    @BindView(R.id.inweiMore)
    LinearLayout inweiMore;
    @BindView(R.id.teamLL)
    LinearLayout teamLL;
    @BindView(R.id.jianjieLL)
    LinearLayout jianjieLL;
    @BindView(R.id.twitter)
    FrameLayout twitter;
    @BindView(R.id.twitterLayout)
    LinearLayout twitterLayout;
    @BindView(R.id.left)
    ImageView left;
    @BindView(R.id.social)
    ViewPager social;
    @BindView(R.id.right)
    ImageView right;
    @BindView(R.id.moreinfoLaout)
    LinearLayout moreinfoLaout;
    @BindView(R.id.inwe_line)
    View inwe_line;

    private ProjectBean project;
    private ArrayList<ProjectBean.ProjectDescBean> descList;
    private ArrayList<InfoMarketBean> totleInfoMarketBeans = new ArrayList<>();
    private ArrayList<InfoMarketBean> infoMarketBeans = new ArrayList<>();
    private MarketAdapter marketAdapter;
    private EmptyWrapper emptyWrapper;
    private boolean hasBtc;
    private WebView[] teamWebs;
    private final ReferenceQueue<WebView> WEB_VIEW_QUEUE = new ReferenceQueue<>();
    private WebView mTwitterWebView = null;
    private final ReferenceQueue<WebView> TWITTER_WEB_VIEW_QUEUE = new ReferenceQueue<>();
    private EmptyWrapper inweEmptyWrapper;
    private InweInfoAdapter inweAdapter;
    private ArrayList<NewsBean> inweBeans = new ArrayList<>();
    private ArrayList<NewsBean> videoBeans = new ArrayList<>();
    private ArrayList<NewsBean> textBeans = new ArrayList<>();
    private boolean isVideoFinish;
    private boolean isTextFinish;
    private int currentTeam;
    private ArrayList<TextView> socal=new ArrayList<>();
    private int currentSocai;
    private boolean isVideo=true;

    @Override
    protected void getBundleExtras(Bundle extras) {
        project = (ProjectBean) extras.getSerializable("project");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.info_activity_project_online_layout;
    }

    @Override
    protected void initView() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.showMainMenu(menu, ProjectOnlineActivity.this, true);
            }
        });

        serch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SearchActivity.class);
                intent.putExtra("type", 0);
                keepTogo(intent);
            }
        });

        state.setText(R.string.shangxianjiaoyizhong);

        title.setText(project.getName());
        title.setFocusableInTouchMode(true);
        title.requestFocus();

        //添加行情
        addPrice();
        //添加市场
        addMarket();
        //添加介绍
        addIntro();
        //添加 INWE 报导
        addInwe();
        //添加 Twitter
        addTwitter();
        //添加 更多资讯
        addInfo();
    }


    private void addPrice() {
        kLine.setText(Html.fromHtml("<u>" + getString(R.string.chakankxian) + "</u>"));
        kLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        liulanqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == project.getProject_explorers() || project.getProject_explorers().size() == 0) {
                    ToastUtil.show("暂无浏览器");
                    return;
                }
                AppUtil.showLiulanqi(mActivity, liulanqi, project.getProject_explorers());
            }
        });
        zhichiqianbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == project.getProject_wallets() || project.getProject_wallets().size() == 0) {
                    ToastUtil.show("暂无钱包");
                    return;
                }
                AppUtil.showWallet(mActivity, zhichiqianbao, project.getProject_wallets());
            }
        });
    }

    private void addMarket() {
        if (null != project.getProject_markets() && project.getProject_markets().size() > 0) {
            for (int i = 0; i < project.getProject_markets().size(); i++) {
                if (project.getProject_markets().get(i).getEn_name().toUpperCase().equals("BTC")) {
                    hasBtc = true;
                    break;
                }
            }
        }
        marketAdapter = new MarketAdapter(this, R.layout.info_item_market, infoMarketBeans);
        emptyWrapper = new EmptyWrapper(marketAdapter);
        emptyWrapper.setEmptyView(R.layout.info_view_empty);
        marketList.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        marketList.setAdapter(emptyWrapper);
        marketList.setNestedScrollingEnabled(false);
    }

    private void addIntro() {
        if (null != project.getProject_desc() && project.getProject_desc().size() > 0) {
            descList = project.getProject_desc();
            int size = descList.size();
            teamWebs = new WebView[size];
            for (int i = 0; i < size; i++) {
                View view = LayoutInflater.from(mActivity).inflate(R.layout.info_view_team_detaile, null);
                TextView title = (TextView) view.findViewById(R.id.title);
                final ImageView show = (ImageView) view.findViewById(R.id.show);
                final FrameLayout content = (FrameLayout) view.findViewById(R.id.content);

                title.setText(descList.get(i).getTitle());
                final int finalI = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (descList.get(finalI).isShow()) {
                            content.setVisibility(View.GONE);
                            descList.get(finalI).setShow(false);
                            show.setImageResource(R.mipmap.up);
                        } else {
                            content.setVisibility(View.INVISIBLE);
                            descList.get(finalI).setShow(true);
                            show.setImageResource(R.mipmap.down);
                        }
                    }
                });
                final WeakReference<WebView> webViewWeakReference =
                        new WeakReference<>(new WebView(mActivity.getApplicationContext()), WEB_VIEW_QUEUE);
                teamWebs[i] = webViewWeakReference.get();
                teamWebs[i] = AppUtil.createWebView(teamWebs[i], mActivity);
                teamWebs[i].setBackgroundColor(Color.parseColor("#00000000"));
                teamWebs[i].setWebViewClient(initWebViewClient(false));

                FrameLayout.LayoutParams webParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                teamWebs[i].setLayoutParams(webParams);
                content.addView(teamWebs[i]);
                content.setVisibility(View.GONE);
                teamLL.addView(view);
            }

        } else {
            teamLL.setVisibility(View.GONE);
        }
    }

    private void addInwe() {

        inweRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.shipin:
                        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) inwe_line.getLayoutParams();
                        params.leftMargin=0;
                        inwe_line.setLayoutParams(params);
                        if (isVideoFinish) {
                            if(isVideo){
                                return;
                            }
                            if (videoBeans.size() <= 3) {
                                inweBeans.clear();
                                inweBeans.addAll(videoBeans);
                            } else {
                                inweBeans.clear();
                                inweBeans.add(videoBeans.get(0));
                                inweBeans.add(videoBeans.get(1));
                                inweBeans.add(videoBeans.get(2));
                            }
                            inweEmptyWrapper.notifyDataSetChanged();
                        } else {
                            //加载Inwe报导
                            InfoApi.getInweInfo(this, project.getId(), "video", new JsonCallback<LzyResponse<ArrayList<NewsBean>>>() {
                                @Override
                                public void onSuccess(Response<LzyResponse<ArrayList<NewsBean>>> response) {
                                    if (null != response) {
                                        if (null != response.body().data) {
                                            videoBeans.clear();
                                            videoBeans.addAll(response.body().data);
                                            if (response.body().data.size() <= 3) {
                                                inweBeans.clear();
                                                inweBeans.addAll(videoBeans);
                                            } else {
                                                inweBeans.clear();
                                                inweBeans.add(videoBeans.get(0));
                                                inweBeans.add(videoBeans.get(1));
                                                inweBeans.add(videoBeans.get(2));
                                            }
                                            inweEmptyWrapper.notifyDataSetChanged();
                                        }
                                    }
                                    isVideoFinish = true;
                                }
                            });
                        }
                        break;
                    case R.id.tuwen:
                        LinearLayout.LayoutParams tuwenParams= (LinearLayout.LayoutParams) inwe_line.getLayoutParams();
                        tuwenParams.leftMargin= DensityUtil.dip2px(mActivity,20);
                        inwe_line.setLayoutParams(tuwenParams);
                        if (isTextFinish) {
                            if(!isVideo){
                                return;
                            }
                            if (textBeans.size() <= 3) {
                                inweBeans.clear();
                                inweBeans.addAll(textBeans);
                            } else {
                                inweBeans.clear();
                                inweBeans.add(textBeans.get(0));
                                inweBeans.add(textBeans.get(1));
                                inweBeans.add(textBeans.get(2));
                            }
                            inweEmptyWrapper.notifyDataSetChanged();
                        } else {
                            //加载Inwe报导
                            InfoApi.getInweInfo(this, project.getId(), "img-txt", new JsonCallback<LzyResponse<ArrayList<NewsBean>>>() {
                                @Override
                                public void onSuccess(Response<LzyResponse<ArrayList<NewsBean>>> response) {
                                    if (null != response) {
                                        if (null != response.body().data) {
                                            textBeans.clear();
                                            textBeans.addAll(response.body().data);
                                            if (response.body().data.size() <= 3) {
                                                inweBeans.clear();
                                                inweBeans.addAll(textBeans);
                                            } else {
                                                inweBeans.clear();
                                                inweBeans.add(textBeans.get(0));
                                                inweBeans.add(textBeans.get(1));
                                                inweBeans.add(textBeans.get(2));
                                            }
                                            inweEmptyWrapper.notifyDataSetChanged();
                                        }
                                    }
                                    isTextFinish = true;
                                }
                            });
                        }
                        break;
                }
            }
        });

        inweiMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shipin.isChecked()) {
                    if (videoBeans.size() == 0) {
                        ToastUtil.show("暂无信息");
                        return;
                    } else {

                    }
                } else {
                    if (textBeans.size() == 0) {
                        ToastUtil.show("暂无信息");
                        return;
                    } else {

                    }
                }
            }
        });

        MultiItemTypeSupport multiItemSupport = new MultiItemTypeSupport<NewsBean>() {
            @Override
            public int getLayoutId(int itemType) {
                //根据itemType返回item布局文件id
                switch (itemType) {
                    case 1:
                        return R.layout.info_item_inwe_info_2;
                    case 2:
                    case 3:
                        return R.layout.info_item_inwe_info_1;
                }
                return 0;
            }

            @Override
            public int getItemViewType(int postion, NewsBean infoBean) {
                return infoBean.getType();
            }
        };
        inweAdapter = new InweInfoAdapter(this, inweBeans, multiItemSupport);
        inweEmptyWrapper = new EmptyWrapper(inweAdapter);
        inweEmptyWrapper.setEmptyView(R.layout.info_view_empty);
        inweList.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        inweList.setAdapter(inweEmptyWrapper);

        inweAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


    }

    private void addTwitter() {
        if (null != project.getDesc() && project.getDesc().length() != 0) {
            if (mTwitterWebView != null) {
                mTwitterWebView.removeAllViews();
                mTwitterWebView.destroy();
            } else {
                final WeakReference<WebView> webViewWeakReference =
                        new WeakReference<>(new WebView(mActivity.getApplicationContext()), TWITTER_WEB_VIEW_QUEUE);
                mTwitterWebView = webViewWeakReference.get();
                mTwitterWebView = AppUtil.createWebView(mTwitterWebView, mActivity);
                mTwitterWebView.setBackgroundColor(Color.parseColor("#00000000"));
                mTwitterWebView.setWebViewClient(initWebViewClient(true));
            }

            RelativeLayout.LayoutParams webParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mTwitterWebView.setLayoutParams(webParams);
            twitter.addView(mTwitterWebView);
            mTwitterWebView.loadData(project.getDesc(), "text/html", "utf-8");
        } else {
            twitterLayout.setVisibility(View.GONE);
        }
    }

    private void addInfo() {
        if (null != project.getProject_medias() && project.getProject_medias().size() > 0) {

            for (int i=0;i<project.getProject_medias().size();i++){
                TextView view= (TextView) LayoutInflater.from(mActivity).inflate(R.layout.info_view_socal,null,false);
                view.setText(project.getProject_medias().get(i).getName());
                final int finalI = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, CommonWebActivity.class);
                        intent.putExtra("title", project.getProject_medias().get(finalI).getName());
                        intent.putExtra("url", project.getProject_medias().get(finalI).getUrl());
                        keepTogo(intent);
                    }
                });
                socal.add(view);
            }
            social.setAdapter(new SocalAdapter(socal));
            social.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    currentSocai=position;
                }
                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentSocai!=0){
                        currentSocai--;
                        social.setCurrentItem(currentSocai);
                    }
                }
            });

            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentSocai<project.getProject_medias().size()){
                        currentSocai++;
                        social.setCurrentItem(currentSocai);
                    }
                }
            });
        } else {
            moreinfoLaout.setVisibility(View.GONE);
        }
    }


    @Override
    protected void initData() {
        //请求项目快讯
        getQuickInfo();
        //请求行情数据
        if (null != project.getProject_time_prices() && project.getProject_time_prices().size() > 0) {
            for (int i = 0; i < project.getProject_time_prices().size(); i++) {
                if (project.getProject_time_prices().get(i).getName().equals("美元")) {
                    getUsdPrice(i);
                } else if (project.getProject_time_prices().get(i).getName().equals("比特币")) {
                    getBtcPrice(i);
                }
            }
        }
        if (hasBtc && null != project.getProject_markets() && project.getProject_markets().size() > 0) {
            for (int i = 0; i < project.getProject_markets().size(); i++) {
                if (project.getProject_markets().get(i).getEn_name().toUpperCase().equals("BTC")) {
                    getMarket(i);
                    break;
                }
            }
        }
        if (null != project.getProject_desc() && project.getProject_desc().size() > 0) {
            //请求团队介绍
            getTeam();
        }
        //加载Inwe报导
        InfoApi.getInweInfo(this, project.getId(), "video", new JsonCallback<LzyResponse<ArrayList<NewsBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArrayList<NewsBean>>> response) {
                if (null != response) {
                    if (null != response.body().data) {
                        videoBeans.clear();
                        videoBeans.addAll(response.body().data);
                        if (response.body().data.size() <= 3) {
                            inweBeans.clear();
                            inweBeans.addAll(videoBeans);
                        } else {
                            inweBeans.clear();
                            inweBeans.add(videoBeans.get(0));
                            inweBeans.add(videoBeans.get(1));
                            inweBeans.add(videoBeans.get(2));
                        }
                        inweEmptyWrapper.notifyDataSetChanged();
                    }
                }
                isVideoFinish = true;
            }
        });
    }

    private void getQuickInfo() {
        InfoApi.getInweInfo(this, project.getId(), "txt", new JsonCallback<LzyResponse<ArrayList<NewsBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArrayList<NewsBean>>> response) {
                if (null != response.body().data && response.body().data.size() > 0) {
                    quickInfo.setText(response.body().data.get(0).getTitle());
                    if (response.body().data.size() == 1) {
                        quickInfo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    } else {
                        quickInfo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }
                } else {
                    quickInfo.setText("暂无快讯");
                }
            }

            @Override
            public void onError(Response<LzyResponse<ArrayList<NewsBean>>> response) {
                super.onError(response);
            }
        });
    }

    private void getTeam() {
        InfoApi.getTeam(this, descList.get(currentTeam).getId(), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                //网页加载数据
                if (null != response && null != response.body()) {
                    teamWebs[currentTeam].loadData(response.body(), "text/html", "utf-8");
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (currentTeam < descList.size()-1) {
                    currentTeam++;
                    getTeam();
                }
            }
        });
    }

    private void getMarket(int i) {
        InfoApi.getMarket(this, project.getProject_markets().get(i).getUrl(), new JsonCallback<LzyResponse<ArrayList<InfoMarketBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ArrayList<InfoMarketBean>>> response) {
                if (null != response.body()) {
                    if (null != response.body().data) {
                        if (response.body().data.size() <= 3) {
                            infoMarketBeans.clear();
                            infoMarketBeans.addAll(response.body().data);
                            moreMarket.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ToastUtil.show("暂无更多数据");
                                }
                            });
                        } else {
                            totleInfoMarketBeans.clear();
                            totleInfoMarketBeans.addAll(response.body().data);
                            infoMarketBeans.clear();
                            infoMarketBeans.add(response.body().data.get(0));
                            infoMarketBeans.add(response.body().data.get(1));
                            infoMarketBeans.add(response.body().data.get(2));
                            moreMarket.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (infoMarketBeans.size()<=3){
                                        infoMarketBeans.clear();
                                        infoMarketBeans.addAll(totleInfoMarketBeans);
                                    }else {
                                        infoMarketBeans.clear();
                                        infoMarketBeans.add(totleInfoMarketBeans.get(0));
                                        infoMarketBeans.add(totleInfoMarketBeans.get(1));
                                        infoMarketBeans.add(totleInfoMarketBeans.get(2));
                                    }
                                    emptyWrapper.notifyDataSetChanged();
                                }
                            });
                        }
                        emptyWrapper.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void getUsdPrice(int i) {
        InfoApi.getPrice(this, project.getProject_time_prices().get(i).getCurrent_url(), new JsonCallback<LzyResponse<InfoPriceBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<InfoPriceBean>> response) {
                if (null != response.body()) {
                    InfoPriceBean infoPriceBean = response.body().data;
                    if (null != infoPriceBean) {
                        usd.setText("$" + new BigDecimal(infoPriceBean.getPrice()).setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
                        usdVolume.setText(new BigDecimal(infoPriceBean.getVolume()).setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
                        charge.setText("(" + infoPriceBean.get_$24h_change() + ")");
                        if (infoPriceBean.get_$24h_change().contains("-")) {
                            charge.setTextColor(Color.parseColor("#22AC39"));
                        } else {
                            charge.setTextColor(Color.parseColor("#FF3232"));
                        }
                        usdMax.setText("$" + new BigDecimal(infoPriceBean.get_$24h_max_price()).setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
                        usdMin.setText("$" + new BigDecimal(infoPriceBean.get_$24h_min_price()).setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
                    }
                }
            }
        });
    }

    private void getBtcPrice(int i) {
        InfoApi.getPrice(this, project.getProject_time_prices().get(i).getCurrent_url(), new JsonCallback<LzyResponse<InfoPriceBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<InfoPriceBean>> response) {
                if (null != response.body()) {
                    InfoPriceBean infoPriceBean = response.body().data;
                    if (null != infoPriceBean) {
                        btc.setText(new BigDecimal(infoPriceBean.getPrice()).setScale(4,BigDecimal.ROUND_HALF_UP).toPlainString() + " BTC");
                        btcVolume.setText(new BigDecimal(infoPriceBean.getVolume()).setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString() + " BTC");
                        btcMax.setText(new BigDecimal(infoPriceBean.get_$24h_max_price()).setScale(4,BigDecimal.ROUND_HALF_UP).toPlainString() + " BTC");
                        btcMin.setText(new BigDecimal(infoPriceBean.get_$24h_min_price()).setScale(4,BigDecimal.ROUND_HALF_UP).toPlainString() + " BTC");
                        charge.setText("(" + infoPriceBean.get_$24h_change() + ")");
                        if (infoPriceBean.get_$24h_change().contains("-")) {
                            charge.setTextColor(Color.parseColor("#22AC39"));
                        } else {
                            charge.setTextColor(Color.parseColor("#FF3232"));
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
    }

    private WebViewClient initWebViewClient(final boolean isTwitter) {
        return new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (isTwitter) {
                    Intent intent = new Intent(mActivity, CommonWebActivity.class);
                    intent.putExtra("title", "Twitter");
                    intent.putExtra("content", url);
                    keepTogo(intent);
                    return true;
                }
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();    //表示等待证书响应
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != teamWebs) {
            for (int i = 0; i < teamWebs.length; i++) {
                if (teamWebs[i] != null) {
                    teamWebs[i].onPause();
                }
            }
        }
        if (mTwitterWebView != null) {
            mTwitterWebView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != teamWebs) {
            for (int i = 0; i < teamWebs.length; i++) {
                if (teamWebs[i] != null) {
                    teamWebs[i].onResume();
                }
            }
        }
        if (mTwitterWebView != null) {
            mTwitterWebView.onResume();
        }
    }

    @Override
    public void onDestroy() {
        if (null != teamWebs) {
            for (int i = 0; i < teamWebs.length; i++) {
                if (teamWebs[i] != null) {
                    teamWebs[i].loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
                    teamWebs[i].clearHistory();

                    ((ViewGroup) teamWebs[i].getParent()).removeView(teamWebs[i]);
                    teamWebs[i].removeAllViews();
                    teamWebs[i].destroy();
                    teamWebs[i] = null;
                }
            }
        }
        if (mTwitterWebView != null) {
            mTwitterWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mTwitterWebView.clearHistory();

            ((ViewGroup) mTwitterWebView.getParent()).removeView(mTwitterWebView);
            mTwitterWebView.removeAllViews();
            mTwitterWebView.destroy();
            mTwitterWebView = null;
        }
        super.onDestroy();
    }

}
