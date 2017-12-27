package com.inwecrypto.wallet.ui.info.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.NewsBean;
import com.inwecrypto.wallet.bean.ProjectBean;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.InfoApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.DensityUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.common.widget.MultiItemTypeSupport;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.info.adapter.ExplorerAdapter;
import com.inwecrypto.wallet.ui.info.adapter.IcoDetaileAdapter;
import com.inwecrypto.wallet.ui.info.adapter.InweInfoAdapter;
import com.inwecrypto.wallet.ui.info.adapter.MoreInfoAdapter;
import com.inwecrypto.wallet.ui.info.adapter.WalletAdapter;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import java.lang.ref.ReferenceQueue;
import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2017/11/13 17:14
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ProjectUnderlineActivity extends BaseActivity {

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
    @BindView(R.id.title1)
    TextView title1;
    @BindView(R.id.guanwang)
    TextView guanwang;
    @BindView(R.id.pingfen)
    TextView pingfen;
    @BindView(R.id.tuijianView)
    View tuijianView;
    @BindView(R.id.tuijainTxt)
    TextView tuijainTxt;
    @BindView(R.id.tuijianLL)
    LinearLayout tuijianLL;
    @BindView(R.id.fengxianView)
    View fengxianView;
    @BindView(R.id.fengxianTxt)
    TextView fengxianTxt;
    @BindView(R.id.fengxianLL)
    LinearLayout fengxianLL;
    @BindView(R.id.chakanpingce)
    TextView chakanpingce;
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
    @BindView(R.id.teamLL)
    LinearLayout teamLL;
    @BindView(R.id.jianjieLL)
    LinearLayout jianjieLL;
    @BindView(R.id.twitter)
    FrameLayout twitter;
    @BindView(R.id.twitterLayout)
    LinearLayout twitterLayout;
    @BindView(R.id.social)
    ViewPager social;
    @BindView(R.id.moreinfoLaout)
    LinearLayout moreinfoLaout;

    private ProjectBean project;
    private ArrayList<ProjectBean.ProjectDescBean> descList;
    private ExplorerAdapter explorerAdapter;
    private WalletAdapter walletAdapter;
    private MoreInfoAdapter moreInfoAdapter;
    private WebView mWebView = null;
    private final ReferenceQueue<WebView> WEB_VIEW_QUEUE = new ReferenceQueue<>();
    private IcoDetaileAdapter icoDetaileAdapter;
    private EmptyWrapper inweEmptyWrapper;
    private InweInfoAdapter inweAdapter;
    private ArrayList<NewsBean> inweBeans = new ArrayList<>();
    private ArrayList<NewsBean> videoBeans = new ArrayList<>();
    private ArrayList<NewsBean> textBeans = new ArrayList<>();
    private boolean isVideoFinish;
    private boolean isTextFinish;

    @Override
    protected void getBundleExtras(Bundle extras) {
        project = (ProjectBean) extras.getSerializable("project");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.info_activity_project_underline_layout;
    }

    @Override
    protected void initView() {
        setLightMode(false);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.showMainMenu(menu, ProjectUnderlineActivity.this, true);
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

        title.setText(project.getName());

        //添加众筹详情
        addZhongchouDetaile();
        //添加 ico细则
        addIcoDetaile();
        //添加介绍
        addIntro();
        //添加 INWE 报导
        addInwe();
        //添加 Explorer
        addExplorer();
        //添加 Wallet
        addWallet();
        //添加 Twitter
        addTwitter();
        //添加 更多资讯
        addInfo();
    }

    private void addZhongchouDetaile() {

        if (null == project.getProject_detail()) {
            return;
        }

        //6:待上线,7:众筹中,8:即将众筹
        switch (project.getType()) {
            case 6:
                state.setText(Html.fromHtml(getResources().getString(R.string.dengdaishangxianjiaoyi) + "<font color='" + project.getProject_detail().getRisk_level_color() + "'> " + project.getScore() + "</font>"));
                break;
            case 7:
                state.setText(Html.fromHtml(getResources().getString(R.string.zhongchouzhong) + "<font color='" + project.getProject_detail().getRisk_level_color() + "'> " + project.getScore() + "</font>"));
                break;
            case 8:
                state.setText(Html.fromHtml(getResources().getString(R.string.jijiangzhongchoukaishi) + "<font color='" + project.getProject_detail().getRisk_level_color() + "'> " + project.getScore() + "</font>"));
                break;
        }


        //104dp
        int totleWidth = ScreenUtils.getScreenWidth(this) - DensityUtil.dip2px(this, 124);
        float current = new BigDecimal(project.getProject_detail().getCurrent_quantity())
                .divide(new BigDecimal(project.getProject_detail().getTarget_quantity()), BigDecimal.ROUND_HALF_UP, 2)
                .floatValue();

        //开启倒计时
    }

    private void addIcoDetaile() {
        if (null != project.getIco_detail() && project.getIco_detail().size() > 0) {
            icoDetaileAdapter = new IcoDetaileAdapter(this, R.layout.info_item_ico_detaie, project.getIco_detail());
        }

        if (null != project.getWebsite()) {
            guanwang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    private void addIntro() {
//        if (null != project.getProject_desc() && project.getProject_desc().size() > 0) {
//            descList = project.getProject_desc();
//            int size = descList.size();
//            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
//            for (int i = 0; i < size; i++) {
//                RadioButton rb = (RadioButton) LayoutInflater.from(this).inflate(R.layout.info_view_team_rb, projectRg, false);
//
//                if (i != 0) {
//                    params.leftMargin = DensityUtil.dip2px(this, 0.5f);
//                }
//                rb.setLayoutParams(params);
//
//                rb.setText(descList.get(i).getTitle());
//                rb.setTag(i);
//                rb.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //请求团队介绍
//                        int i = (int) v.getTag();
//                        getTeam(i);
//
//                    }
//                });
//                projectRg.addView(rb);
//            }
//            projectRg.check(projectRg.getChildAt(0).getId());
//            if (mWebView != null) {
//                mWebView.removeAllViews();
//                mWebView.destroy();
//            } else {
//                final WeakReference<WebView> webViewWeakReference =
//                        new WeakReference<>(new WebView(mActivity.getApplicationContext()), WEB_VIEW_QUEUE);
//                mWebView = webViewWeakReference.get();
//                mWebView = AppUtil.createWebView(mWebView, mActivity);
//                ;
//                mWebView.setWebViewClient(initWebViewClient());
//            }
//
//            RelativeLayout.LayoutParams webParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            mWebView.setLayoutParams(webParams);
//            projectContent.addView(mWebView);
//
//        } else {
//            teamLayout.setVisibility(View.GONE);
//        }
    }

    private void addInwe() {

        inweRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.shipin:
                        if (isVideoFinish) {
                            inweBeans.clear();
                            inweBeans.addAll(videoBeans);
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
                                            inweBeans.clear();
                                            inweBeans.addAll(response.body().data);
                                            inweEmptyWrapper.notifyDataSetChanged();
                                        }
                                    }
                                    isVideoFinish = true;
                                }
                            });
                        }
                        break;
                    case R.id.tuwen:
                        if (isTextFinish) {
                            inweBeans.clear();
                            inweBeans.addAll(textBeans);
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
                                            inweBeans.clear();
                                            inweBeans.addAll(response.body().data);
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

    private void addExplorer() {
//        if (null == project.getProject_explorers()) {
//            return;
//        }
//        explorerAdapter = new ExplorerAdapter(this, R.layout.info_item_explorer, project.getProject_explorers());
//        explorerList.setLayoutManager(new LinearLayoutManager(this));
//        explorerList.setAdapter(explorerAdapter);
    }

    private void addWallet() {
//        if (null == project.getProject_wallets()) {
//            return;
//        }
//        MultiItemTypeSupport multiItemSupport = new MultiItemTypeSupport<ProjectBean.ProjectWalletsBean>() {
//            @Override
//            public int getLayoutId(int itemType) {
//                //根据itemType返回item布局文件id
//                switch (itemType) {
//                    case 0:
//                        return R.layout.info_item_wallet_1;
//                    case 1:
//                        return R.layout.info_item_wallet_2;
//                }
//                return 0;
//            }
//
//            @Override
//            public int getItemViewType(int postion, ProjectBean.ProjectWalletsBean infoBean) {
//                return postion % 2;
//            }
//        };
//        walletAdapter = new WalletAdapter(this, project.getProject_wallets(), multiItemSupport);
//        walletList.setLayoutManager(new GridLayoutManager(this, 2));
//        walletList.setAdapter(walletAdapter);
    }

    private void addTwitter() {
        if (null != project.getDesc()) {
            //twitter.setText(Html.fromHtml(project.getDesc()));
        }
    }

    private void addInfo() {
//        if (null == project.getProject_medias()) {
//            return;
//        }
//        moreInfoAdapter = new MoreInfoAdapter(this, R.layout.info_item_more, project.getProject_medias());
//        moreList.setLayoutManager(new GridLayoutManager(this, 7));
//        moreList.setAdapter(moreInfoAdapter);
    }


    @Override
    protected void initData() {
        if (null != project.getProject_desc() && project.getProject_desc().size() > 0) {
            //请求团队介绍
            getTeam(0);
        }
    }

    private void getTeam(int i) {
        InfoApi.getTeam(this, i, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                //网页加载数据
                if (null != response && null != response.body()) {
                    mWebView.loadData(response.body(), "text/html", "utf-8");
                }
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

    private WebViewClient initWebViewClient() {
        return new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();    //表示等待证书响应
            }

            // 旧版本，会在新版本中也可能被调用，所以加上一个判断，防止重复显示
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            // 新版本，只会在Android6及以上调用
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }


    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
