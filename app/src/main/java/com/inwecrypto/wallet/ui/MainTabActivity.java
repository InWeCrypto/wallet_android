package com.inwecrypto.wallet.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.base.BaseFragment;
import com.inwecrypto.wallet.common.imageloader.GlideCircleTransform;
import com.inwecrypto.wallet.ui.info.InfoWebFragment;
import com.inwecrypto.wallet.ui.market.MarketFragment;
import com.inwecrypto.wallet.ui.wallet.activity.neowallet.NeoWalletActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.db.CacheManager;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import com.inwecrypto.wallet.AppApplication;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.MailIconBean;
import com.inwecrypto.wallet.bean.TokenBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.bean.WalletCountBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.GsonUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.event.RefreshEvent;
import com.inwecrypto.wallet.service.AutoUpdateService;
import com.inwecrypto.wallet.ui.info.InfoFragment;
import com.inwecrypto.wallet.ui.me.MeFragment;
import com.inwecrypto.wallet.ui.wallet.WalletFragment;
import com.inwecrypto.wallet.ui.wallet.activity.AddWalletListActivity;
import com.inwecrypto.wallet.ui.wallet.activity.HotWalletActivity;
import com.inwecrypto.wallet.ui.wallet.activity.MessageActivity;
import com.inwecrypto.wallet.ui.wallet.adapter.WalletMenuAdapter;

import static com.inwecrypto.wallet.common.Constant.EVENT_MAIN_PRICE;

/**
 * Created by xiaoji on 2017/7/14.
 * 功能描述：
 * 版本：@version
 */

public class MainTabActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.add_wallet)
    TextView addWallet;
    @BindView(R.id.import_wallet)
    TextView importWallet;
    @BindView(R.id.scan)
    TextView scan;
    @BindView(R.id.wallet_list)
    RecyclerView walletList;

    private LinearLayout[] tabs= new LinearLayout[4];
    private ImageView[] imgs=new ImageView[4];
    private TextView[] titles=new TextView[4];
    private int[] imgIds=new int[]{R.mipmap.tab_hangqing_nor,R.mipmap.tab_faxian_nor,R.mipmap.tab_qianbao_nor,R.mipmap.tab_wode_nor};
    private int[] imgClickIds=new int[]{R.mipmap.tab_hangqing_pre,R.mipmap.tab_faxian_pre,R.mipmap.tab_qianbao_pre,R.mipmap.tab_wode_pre};
    private String[] titleStrs;
    private int[] colors=new int[]{Color.parseColor("#afafb0"),Color.parseColor("#008c55")};
    private BaseFragment[] fragments=new BaseFragment[4];
    private String[] tags=new String[]{"f1","f2","f3","f4"};
    private FragmentManager manager;
    private int currentIndex=-1;


    private WalletMenuAdapter adapter;

    private ArrayList<WalletBean> wallet = new ArrayList<>();

    private HashMap<String, TokenBean.ListBean> totleGnt = new HashMap<>();

    private BigDecimal EthEther = new BigDecimal("0.00");
    private BigDecimal EthPrice = new BigDecimal("0.00");

    private boolean isMainScan;

    private int index;
    private ArrayList<WalletCountBean> walletCount;
    private WebView mWebView;
    private String address;
    private boolean hasNeo;
    private boolean isRefresh;

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    private boolean init;

    @Override
    protected void getBundleExtras(Bundle extras) {
        isOpenEventBus = true;
        String bundleExtra = getIntent().getStringExtra("pushInfo");
        if (bundleExtra != null) {
            Intent intent = new Intent(this, MessageActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected int setLayoutID() {
        return R.layout.activity_main_tab;
    }

    @Override
    protected void initView() {
        titleStrs=new String[]{getResources().getString(R.string.hangqing),getResources().getString(R.string.zixun), getResources().getString(R.string.zichan),getResources().getString(R.string.dinzhi)};
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tabs[0]= (LinearLayout) findViewById(R.id.tab1);
        tabs[1]= (LinearLayout) findViewById(R.id.tab2);
        tabs[2]= (LinearLayout) findViewById(R.id.tab3);
        tabs[3]= (LinearLayout) findViewById(R.id.tab4);
        for (int i=0;i<4;i++){
            imgs[i]= (ImageView) tabs[i].findViewById(R.id.img);
            imgs[i].setImageResource(imgIds[i]);
            titles[i]= (TextView) tabs[i].findViewById(R.id.title);
            titles[i].setText(titleStrs[i]);
        }
        initTab(savedInstanceState);
        initListener();
        startService(new Intent(this, AutoUpdateService.class));
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawerLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        drawerLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        drawerLayout.openDrawer(Gravity.LEFT);
                        return false;
                    }
                });
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        addWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                addWallet.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(mActivity, AddWalletListActivity.class);
                        intent.putExtra("type", 1);
                        keepTogo(intent);
                    }
                }, 600);
            }
        });

        importWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                importWallet.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            Intent intent = new Intent(mActivity, AddWalletListActivity.class);
                            intent.putExtra("type", 2);
                            intent.putExtra("wallets",wallet);
                            keepTogo(intent);
                    }
                }, 600);
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                scan.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isMainScan = true;
                        keepTogo(ScanActivity.class);
                    }
                }, 600);
            }
        });

        adapter = new WalletMenuAdapter(this, R.layout.wallet_item_menu, wallet);
        walletList.setLayoutManager(new LinearLayoutManager(this));
        walletList.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, final int position) {
                    drawerLayout.closeDrawers();
                    walletList.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent =null;
                            if (wallet.get(position).getCategory_id()==1){
                                intent = new Intent(mActivity, HotWalletActivity.class);
                            }else {
                                intent = new Intent(mActivity, NeoWalletActivity.class);
                            }
                            intent.putExtra("wallet", wallet.get(position));
                            keepTogo(intent);
                        }
                    }, 400);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        //预加载网页
        mWebView=new WebView(this);
        mWebView.loadUrl(Constant.MAIN_WEB);
        mWebView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mWebView.removeAllViews();
                mWebView.destroy();
                mWebView = null;
            }
        },2000);
    }

    private void initTab(Bundle savedInstanceState) {
        manager=getSupportFragmentManager();
        FragmentTransaction ft=manager.beginTransaction();

        int index=1;
        if (null!=savedInstanceState){
            for (int i=0;i<4;i++){
                if (null!=manager.findFragmentByTag(tags[i])){
                    fragments[i]= (BaseFragment) manager.findFragmentByTag(tags[i]);
                    fragments[i].setUserVisibleHint(false);
                    if (fragments[i].isShow){index=i+1;}
                    ft.hide(fragments[i]);
                }
            }
            ft.commitAllowingStateLoss();
        }

        changeTab(index);
    }

    private void initListener() {
        for (int i=0;i<4;i++){
            tabs[i].setOnClickListener(this);
        }
    }

    private void changeTab(int i) {

        if (currentIndex==i){
            return;
        }

        int befer=currentIndex;

        FragmentTransaction ft=manager.beginTransaction();

        if (currentIndex!=-1&&null!=fragments[currentIndex-1]){
            ft.hide(fragments[currentIndex-1]);
            fragments[currentIndex-1].setUserVisibleHint(false);
        }

        switch (i){
            case 1:
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                addTab(ft,1,null==fragments[0]?new MarketFragment():null);
                break;
            case 2:
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                addTab(ft,2,null==fragments[1]?new InfoWebFragment():null);
                break;
            case 3:
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                addTab(ft,3,null==fragments[2]?new WalletFragment():null);
                break;
            case 4:
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                addTab(ft,4,null==fragments[3]?new MeFragment():null);
                break;
        }

        ft.commitAllowingStateLoss();

        changeIcon(befer-1,i-1);
    }

    private void changeIcon(int current, int i) {
        if (current!=-2){
            imgs[current].setImageResource(imgIds[current]);
            titles[current].setTextColor(colors[0]);
        }
        imgs[i].setImageResource(imgClickIds[i]);
        titles[i].setTextColor(colors[1]);
    }

    private void addTab(FragmentTransaction ft,int index,BaseFragment fragment) {
        int position=index-1;
        if (fragments[position]==null){
            fragments[position]=fragment;
            ft.add(R.id.content,fragment,tags[position]);
        }
        ft.show(fragments[position]);
        fragments[position].setUserVisibleHint(true);
        currentIndex=index;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tab1:
                changeTab(1);
                break;
            case R.id.tab2:
                changeTab(2);
                break;
            case R.id.tab3:
                changeTab(3);
                break;
            case R.id.tab4:
                changeTab(4);
                break;
        }
    }


    @Override
    protected void initData() {
        //从网络获取钱包
        getInfoOnNet();
    }

    private void getInfoOnNet() {
        hasNeo=false;
        WalletApi.wallet(mActivity, new JsonCallback<LzyResponse<CommonListBean<WalletBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                boolean isSave = false;
                String mailIco = AppApplication.get().getSp().getString(Constant.WALLET_ICO, "[]");
                ArrayList<MailIconBean> mailId = GsonUtils.jsonToArrayList(mailIco, MailIconBean.class);
                HashMap<Integer, Integer> mailHash = new HashMap<>();
                for (int i = 0; i < mailId.size(); i++) {
                    mailHash.put(mailId.get(i).getId(), mailId.get(i).getIcon());
                }
                wallet.clear();
                if (null != response.body().data.getList()) {
                    String wallets = AppApplication.get().getSp().getString(Constant.WALLETS, "");
                    String wallets_beifen = AppApplication.get().getSp().getString(Constant.WALLETS_BEIFEN, "");
                    String walletsZjc = AppApplication.get().getSp().getString(Constant.WALLETS_ZJC_BEIFEN, "");
                    for (int i = 0; i < response.body().data.getList().size(); i++) {
                        if (null == mailHash.get(response.body().data.getList().get(i).getId())) {
                            int icon = AppUtil.getRoundmIcon();
                            mailHash.put(response.body().data.getList().get(i).getId(), icon);
                            response.body().data.getList().get(i).setIcon(AppUtil.getIcon(icon));
                            isSave = true;
                        } else {
                            response.body().data.getList().get(i).setIcon(AppUtil.getIcon(mailHash.get(response.body().data.getList().get(i).getId())));
                        }

                        if (wallets.contains(response.body().data.getList().get(i).getAddress())) {
                            if (wallets_beifen.contains(response.body().data.getList().get(i).getAddress())||walletsZjc.contains(response.body().data.getList().get(i).getAddress())) {
                                response.body().data.getList().get(i).setType(Constant.BEIFEN);
                            } else {
                                response.body().data.getList().get(i).setType(Constant.ZHENGCHANG);
                            }
                        } else {
                            response.body().data.getList().get(i).setType(Constant.GUANCHA);
                        }
                        if (!hasNeo && response.body().data.getList().get(i).getCategory().getName().equals("NEO")){
                            hasNeo=true;
                        }
                    }

                    if (isSave) {
                        mailId.clear();
                        Iterator iter = mailHash.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry entry = (Map.Entry) iter.next();
                            Integer key = (Integer) entry.getKey();
                            Integer val = (Integer) entry.getValue();
                            mailId.add(new MailIconBean(key, val));
                        }
                        AppApplication.get().getSp().putString(Constant.WALLET_ICO, GsonUtils.objToJson(mailId));
                    }
                    wallet.addAll(response.body().data.getList());
                }

                adapter.notifyDataSetChanged();

                if (!response.isFromCache()){
                    setInit(true);
                }

                InfoWebFragment fragment= (InfoWebFragment) manager.findFragmentByTag(tags[1]);
                if (!response.isFromCache()&&(isRefresh||(null!=fragment&&fragment.isShow))){
                    //获取代币列表
                    EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_WALLET_DAIBI));
                    isRefresh=false;
                }
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                super.onError(response);
                ToastUtil.show("加载失败");
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_MAIN_REFERSH_COMP));
            }
        });
    }

    public ArrayList<WalletBean> getWallet() {
        return wallet;
    }

    public void setWallet(ArrayList<WalletBean> wallet) {
        this.wallet = wallet;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }


    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.OPEN_CLOSE_MENU) {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
        if (event.getEventCode() == Constant.EVENT_KEY && isMainScan) {
            isMainScan = false;
            com.inwecrypto.wallet.event.KeyEvent keyEvent = (com.inwecrypto.wallet.event.KeyEvent) event.getData();
            if (AppUtil.isAddress(keyEvent.getKey().trim())) {
                address = keyEvent.getKey().trim().toLowerCase();
            } else {
                ToastUtil.show("请输入正确的钱包地址");
            }
        }

        if (event.getEventCode() == Constant.EVENT_WALLET){
            setRefresh(true);
            getInfoOnNet();
        }
    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //1.点击返回键条件成立
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                drawerLayout.closeDrawers();
                return true;
            }

            InfoWebFragment fragment= (InfoWebFragment) manager.findFragmentByTag(tags[1]);
            if (null!=fragment){
                if (fragment.isShow&&fragment.canBack()){
                    return true;
                }
            }

            //2.点击的时间差如果大于2000，则提示用户点击两次退出
            if (System.currentTimeMillis() - mExitTime > 2000) {
                //3.保存当前时间
                mExitTime = System.currentTimeMillis();
                //4.提示
                ToastUtil.show("再按一次退出InWeCrypto");
            } else {
                //5.点击的时间差小于2000，退出。
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (AppApplication.get().getSp().getBoolean(Constant.NEED_RESTART, false)) {
            AppApplication.get().getSp().putBoolean(Constant.NEED_RESTART, false);
            Intent restart = getIntent();
            finish();
            startActivity(restart);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, AutoUpdateService.class));
    }
}
