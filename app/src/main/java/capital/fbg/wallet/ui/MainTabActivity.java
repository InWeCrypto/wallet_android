package capital.fbg.wallet.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.db.CacheManager;
import com.lzy.okgo.model.Response;
import com.xw.repo.BubbleSeekBar;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import capital.fbg.wallet.AppApplication;
import capital.fbg.wallet.R;
import capital.fbg.wallet.base.BaseActivity;
import capital.fbg.wallet.bean.CommonListBean;
import capital.fbg.wallet.bean.CountBean;
import capital.fbg.wallet.bean.GasBean;
import capital.fbg.wallet.bean.MailIconBean;
import capital.fbg.wallet.bean.TokenBean;
import capital.fbg.wallet.bean.TransferBean;
import capital.fbg.wallet.bean.ValueBean;
import capital.fbg.wallet.bean.WalletBean;
import capital.fbg.wallet.bean.WalletCountBean;
import capital.fbg.wallet.common.Constant;
import capital.fbg.wallet.common.http.LzyResponse;
import capital.fbg.wallet.common.http.api.WalletApi;
import capital.fbg.wallet.common.http.callback.JsonCallback;
import capital.fbg.wallet.common.util.AppUtil;
import capital.fbg.wallet.common.util.GsonUtils;
import capital.fbg.wallet.common.util.NetworkUtils;
import capital.fbg.wallet.common.util.QuanCodeUtils;
import capital.fbg.wallet.common.util.ToastUtil;
import capital.fbg.wallet.common.widget.FragmentTabHost;
import capital.fbg.wallet.event.BaseEventBusBean;
import capital.fbg.wallet.event.RefreshEvent;
import capital.fbg.wallet.service.AutoUpdateService;
import capital.fbg.wallet.ui.discover.DiscoverFragment;
import capital.fbg.wallet.ui.discover.adapter.IcoWalletAdapter;
import capital.fbg.wallet.ui.market.MarketFragment;
import capital.fbg.wallet.ui.me.MeFragment;
import capital.fbg.wallet.ui.wallet.WalletFragment;
import capital.fbg.wallet.ui.wallet.activity.AddWalletListActivity;
import capital.fbg.wallet.ui.wallet.activity.ClodAddWalletListActivity;
import capital.fbg.wallet.ui.wallet.activity.ColdWalletActivity;
import capital.fbg.wallet.ui.wallet.activity.HotWalletActivity;
import capital.fbg.wallet.ui.wallet.activity.MessageActivity;
import capital.fbg.wallet.ui.wallet.adapter.WalletMenuAdapter;
import me.drakeet.materialdialog.MaterialDialog;
import me.grantland.widget.AutofitTextView;
import unichain.ETHWallet;
import unichain.Unichain;

import static capital.fbg.wallet.common.Constant.EVENT_MAIN_PRICE;

/**
 * Created by xiaoji on 2017/7/14.
 * 功能描述：
 * 版本：@version
 */

public class MainTabActivity extends BaseActivity implements TabHost.OnTabChangeListener {

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
    @BindView(android.R.id.tabs)
    TabWidget tabs;

    private List<TabItem> tabItemList;
    private WalletMenuAdapter adapter;
    private ArrayList<WalletBean> wallet = new ArrayList<>();

    private HashMap<String, TokenBean.ListBean> totleGnt = new HashMap<>();

    private BigDecimal EthEther = new BigDecimal("0.00");
    private BigDecimal EthPrice = new BigDecimal("0.00");
    private BigDecimal pEther = new java.math.BigDecimal("1000000000000000000");
    private BigDecimal low = new BigDecimal("25200000000000").multiply(new BigDecimal(Constant.GAS_LIMIT)).divide(new BigDecimal(21000),0,BigDecimal.ROUND_HALF_UP);
    private BigDecimal high = new BigDecimal("2520120000000000").multiply(new BigDecimal(Constant.GAS_LIMIT)).divide(new BigDecimal(21000),0,BigDecimal.ROUND_HALF_UP);
    private BigDecimal distance = high.subtract(low);
    private BigDecimal p100 = new BigDecimal(100);
    private BigDecimal pGwei = new BigDecimal("1000000000");
    private BigDecimal gasPrice;

    private String address;
    private MaterialDialog mMaterialDialog;
    private WalletBean walletBean;

    private String price;
    private String oxPrice;
    private String nonce;
    private BigDecimal gas;
    private String showGas;
    private Bitmap bitmap;
    private String coldData;
    private String hash;
    private AutofitTextView tvPayAddress;
    private float position;
    private PopupWindow mPopupWindow;

    private boolean isMainScan;

    private int index;
    private ArrayList<WalletCountBean> walletCount;

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
        initTabData();
        initTabHost();
        if (!AppApplication.get().getSp().getBoolean(Constant.IS_CLOD, false)) {
            startService(new Intent(this, AutoUpdateService.class));
        }
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
                        if (AppApplication.get().getSp().getBoolean(Constant.IS_CLOD, false)) {
                            Intent intent = new Intent(mActivity, ClodAddWalletListActivity.class);
                            intent.putExtra("type", 1);
                            keepTogo(intent);
                        } else {
                            Intent intent = new Intent(mActivity, AddWalletListActivity.class);
                            intent.putExtra("type", 1);
                            keepTogo(intent);
                        }
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
                        if (AppApplication.get().getSp().getBoolean(Constant.IS_CLOD, false)) {
                            Intent intent = new Intent(mActivity, ClodAddWalletListActivity.class);
                            intent.putExtra("type", 2);
                            intent.putExtra("wallets", wallet);
                            keepTogo(intent);
                        } else {
                            Intent intent = new Intent(mActivity, AddWalletListActivity.class);
                            intent.putExtra("type", 2);
                            intent.putExtra("wallets",wallet);
                            keepTogo(intent);
                        }
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
                if (AppApplication.get().getSp().getBoolean(Constant.IS_CLOD, false)) {
                    if (NetworkUtils.isConnected(mActivity)) {
                        ToastUtil.show("请确保手机处于飞行模式！");
                        return;
                    } else {
                        drawerLayout.closeDrawers();
                        walletList.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(mActivity, ColdWalletActivity.class);
                                intent.putExtra("address", wallet.get(position).getAddress());
                                intent.putExtra("icon", wallet.get(position).getIcon());
                                keepTogo(intent);
                            }
                        }, 600);
                    }
                } else {
                    drawerLayout.closeDrawers();
                    walletList.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(mActivity, HotWalletActivity.class);
                            intent.putExtra("wallet", wallet.get(position));
                            keepTogo(intent);
                        }
                    }, 600);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        if (AppApplication.get().getSp().getBoolean(Constant.IS_CLOD, false)) {
            scan.setVisibility(View.GONE);
            tabs.setEnabled(false);
            tabs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    //初始化Tab数据
    private void initTabData() {
        tabItemList = new ArrayList<>();
        //添加钱包Tab
        tabItemList.add(new TabItem(R.mipmap.tab_qianbao_nor
                , R.mipmap.tab_qianbao_pre, R.string.qianbao, WalletFragment.class));
        //添加行情Tab
        tabItemList.add(new TabItem(R.mipmap.tab_hangqing_nor
                , R.mipmap.tab_hangqing_pre, R.string.hangqing, MarketFragment.class));
        //添加发现Tab
        tabItemList.add(new TabItem(R.mipmap.tab_faxian_nor
                , R.mipmap.tab_faxian_pre, R.string.faxian, DiscoverFragment.class));
        //添加我的Tab
        tabItemList.add(new TabItem(R.mipmap.tab_wode_nor
                , R.mipmap.tab_wode_pre, R.string.wode, MeFragment.class));

    }

    //初始化主页选项卡视图
    private void initTabHost() {
        //获取FragmentTabHost
        FragmentTabHost fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        //绑定TabHost(绑定我们的body)
        fragmentTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        //去掉分割线
        fragmentTabHost.getTabWidget().setDividerDrawable(null);
        for (int i = 0; i < tabItemList.size(); i++) {
            TabItem tabItem = tabItemList.get(i);
            //绑定Fragment(将Fragment添加到FragmentTabHost组件上面)
            //newTabSpec:代表Tab名字
            //setIndicator:图片(今天我们采用布局文件--Tab到样式我们自己做)
            TabHost.TabSpec tabSpec = fragmentTabHost.
                    newTabSpec(tabItem.getTitleString())
                    .setIndicator(tabItem.getView());
            //添加Fragment
            //tabSpec:选项卡
            //tabItem.getFragmentClass():具体的Fragment
            //tabItem.getBundle():给我们的具体的Fragment传参数
            fragmentTabHost.addTab(tabSpec, tabItem.getFragmentClass(), tabItem.getBundle());
            //给我们的Tab按钮设置背景
            fragmentTabHost.getTabWidget()
                    .getChildAt(i)
                    .setBackgroundColor(getResources().getColor(R.color.c_ffffff));
            //监听点击Tab
            fragmentTabHost.setOnTabChangedListener(this);
            //默认选中第一个Tab
            if (i == 0) {
                tabItem.setChecked(true);
            }
        }
    }

    @Override
    protected void initData() {
        if (AppApplication.get().getSp().getBoolean(Constant.IS_CLOD, false)) {
            if (NetworkUtils.isConnected(mActivity)) {
                //启动dialog
                if (null == netDialog) {
                    showNetDialog();
                }
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    wallet.clear();
                    final AccountManager accountManager = AccountManager.get(mActivity);
                    Account[] accounts = accountManager.getAccountsByType("capital.fbg.wallet");
                    WalletBean bean = null;
                    String wallets = AppApplication.get().getSp().getString(Constant.WALLETS_CLOD_BEIFEN, "");
                    String walletsZjc = AppApplication.get().getSp().getString(Constant.WALLETS_ZJC_BEIFEN, "");
                    for (int i = 0; i < accounts.length; i++) {
                        if (accountManager.getUserData(accounts[i], "wallet_type").equals("clod")) {
                            bean = new WalletBean();
                            bean.setAddress(accounts[i].name);
                            bean.setName(accountManager.getUserData(accounts[i], "name"));
                            bean.setType(accountManager.getUserData(accounts[i], "type"));
                            bean.setIcon(AppUtil.getIcon(Integer.parseInt(accountManager.getUserData(accounts[i], "icon"))));
                            if (wallets.contains(accounts[i].name)||walletsZjc.contains(accounts[i].name)) {
                                bean.setType(Constant.BEIFEN);
                            } else {
                                bean.setType(Constant.CLOD);
                            }
                            wallet.add(bean);
                        }
                    }
                    walletList.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_MAIN_REFERSH_COMP));
                        }
                    });
                }
            }).start();
        } else {
            //获取缓存数据
            CacheEntity<LzyResponse<CommonListBean<WalletBean>>> walletsList = (CacheEntity<LzyResponse<CommonListBean<WalletBean>>>) CacheManager.getInstance().get(Constant.WALLETS+AppApplication.isMain);
            if (null != walletsList && null != walletsList.getData() && null != walletsList.getData().data.getList()) {
                boolean isSave = false;
                String mailIco = AppApplication.get().getSp().getString(Constant.WALLET_ICO, "[]");
                ArrayList<MailIconBean> mailId = GsonUtils.jsonToArrayList(mailIco, MailIconBean.class);
                HashMap<Integer, Integer> mailHash = new HashMap<>();
                for (int i = 0; i < mailId.size(); i++) {
                    mailHash.put(mailId.get(i).getId(), mailId.get(i).getIcon());
                }
                wallet.clear();
                if (null != walletsList.getData().data.getList()) {
                    String wallets = AppApplication.get().getSp().getString(Constant.WALLETS, "");
                    String wallets_beifen = AppApplication.get().getSp().getString(Constant.WALLETS_BEIFEN, "");
                    String walletsZjc = AppApplication.get().getSp().getString(Constant.WALLETS_ZJC_BEIFEN, "");
                    for (int i = 0; i < walletsList.getData().data.getList().size(); i++) {
                        if (null == mailHash.get(walletsList.getData().data.getList().get(i).getId())) {
                            int icon = AppUtil.getRoundmIcon();
                            mailHash.put(walletsList.getData().data.getList().get(i).getId(), icon);
                            walletsList.getData().data.getList().get(i).setIcon(AppUtil.getIcon(icon));
                            isSave = true;
                        } else {
                            walletsList.getData().data.getList().get(i).setIcon(AppUtil.getIcon(mailHash.get(walletsList.getData().data.getList().get(i).getId())));
                        }

                        if (wallets.contains(walletsList.getData().data.getList().get(i).getAddress())) {
                            if (wallets_beifen.contains(walletsList.getData().data.getList().get(i).getAddress())||walletsZjc.contains(walletsList.getData().data.getList().get(i).getAddress())) {
                                walletsList.getData().data.getList().get(i).setType(Constant.BEIFEN);
                            } else {
                                walletsList.getData().data.getList().get(i).setType(Constant.ZHENGCHANG);
                            }
                        } else {
                            walletsList.getData().data.getList().get(i).setType(Constant.GUANCHA);
                        }
                    }
                    wallet.addAll(walletsList.getData().data.getList());
                }
                adapter.notifyDataSetChanged();
            }
            //设置缓存数据
            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_TOTLE_PRICE));
            //请求网络
            getInfoOnNet();
        }
    }

    private void getInfoOnNet() {
        cancleTag();
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

                //进行钱包总额计算
                getTotle();
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                super.onError(response);
                ToastUtil.show("加载失败");
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_MAIN_REFERSH_COMP));
            }
        });
    }

    public void cancleTag() {
        OkGo.getInstance().cancelTag(this);
    }

    private void getTotle() {

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (WalletBean wa : wallet) {
            sb.append(wa.getId() + ",");
        }
        if(sb.length()!=1){
            sb.delete(sb.length() - 1, sb.length());
        }
        sb.append("]");
        WalletApi.conversion(this, sb.toString(), new JsonCallback<LzyResponse<CommonListBean<WalletCountBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<WalletCountBean>>> response) {
                EthEther = EthEther.multiply(new BigDecimal(0));
                EthPrice = EthPrice.multiply(new BigDecimal(0));
                totleGnt.clear();
                walletCount = response.body().data.getList();
                //计算ethPrice
                for (WalletCountBean count : walletCount) {
                    if (null != count.getBalance()) {
                        //进行计算
                        BigDecimal currentPrice = new BigDecimal(AppUtil.toD(count.getBalance().replace("0x", "0")));
                        EthEther = EthEther.add(currentPrice);
                        if (1 == AppApplication.get().getUnit()) {
                            EthPrice = EthPrice.add(currentPrice.divide(pEther).multiply(new BigDecimal(count.getCategory().getCap().getPrice_cny())));
                        } else {
                            EthPrice = EthPrice.add(currentPrice.divide(pEther).multiply(new BigDecimal(count.getCategory().getCap().getPrice_usd())));
                        }
                    }
                }
                //
                HashMap<String, String> params = new HashMap<>();
                params.put("ether", EthEther.divide(pEther, 4, BigDecimal.ROUND_HALF_UP).toString());
                params.put("price", EthPrice.toPlainString());
                EventBus.getDefault().postSticky(new BaseEventBusBean(EVENT_MAIN_PRICE, new RefreshEvent(0, params)));

                index=0;
                if (walletCount.size()>0){
                    getTokenPrice();
                }
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<WalletCountBean>>> response) {
                super.onError(response);
                ToastUtil.show("加载失败");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_MAIN_REFERSH_COMP));
            }
        });
    }

    private void getTokenPrice() {
        //请求代币列表
        WalletApi.conversionErrorCache(this, walletCount.get(index).getId(), new JsonCallback<LzyResponse<TokenBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<TokenBean>> response) {
                if (null != response.body().data.getList()) {
                    for (TokenBean.ListBean gnt : response.body().data.getList()) {
                        if (null != totleGnt.get(gnt.getName())) {
                            TokenBean.ListBean bfGnt = totleGnt.get(gnt.getName());

                            BigDecimal secPrice = new BigDecimal(AppUtil.toD(gnt.getBalance().replace("0x", "0")));

                            gnt.setBalance(secPrice.divide(pEther).add(new BigDecimal(bfGnt.getBalance())).toString());
                            totleGnt.put(gnt.getName(), gnt);
                        } else {
                            BigDecimal currentPrice = new BigDecimal(AppUtil.toD(gnt.getBalance().replace("0x", "0")));
                            gnt.setBalance(currentPrice.divide(pEther).toString());
                            totleGnt.put(gnt.getName(), gnt);
                        }
                    }
                }
            }

            @Override
            public void onCacheSuccess(Response<LzyResponse<TokenBean>> response) {
                super.onCacheSuccess(response);
                onSuccess(response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                index++;
                if (index>=walletCount.size()){
                    EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_TOTLE_GNT));
                    return;
                }else {
                    getTokenPrice();
                }
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.OPEN_CLOSE_MENU) {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
        if (event.getEventCode() == Constant.EVENT_WALLET
                || event.getEventCode() == Constant.EVENT_UNIT_CHANGE) {
            if (AppApplication.get().getSp().getBoolean(Constant.IS_CLOD, false)) {
                initData();
            } else {
                getInfoOnNet();
            }
        }

        if (event.getEventCode() == Constant.EVENT_KEY && isMainScan) {
            isMainScan = false;
            capital.fbg.wallet.event.KeyEvent keyEvent = (capital.fbg.wallet.event.KeyEvent) event.getData();
            if (AppUtil.isAddress(keyEvent.getKey().trim())) {
                address = keyEvent.getKey().trim().toLowerCase();
                getGasPrcie();
            } else {
                ToastUtil.show("请输入正确的钱包地址");
            }
        }
    }

    private void getGasPrcie() {
        //获取gas
        WalletApi.gas(this, new JsonCallback<LzyResponse<GasBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<GasBean>> response) {
                gasPrice=new BigDecimal(AppUtil.toD(response.body().data.getGasPrice().replace("0x", "0")));
                showOrderInfo();
            }

            @Override
            public void onError(Response<LzyResponse<GasBean>> response) {
                super.onError(response);
                ToastUtil.show("获取gasPrice失败，请检查网络后重试");
            }
        });
    }

    private void showOrderInfo() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_popup_ico_pay, null);
        TextView tvOrder = (TextView) view.findViewById(R.id.order);
        tvOrder.setText("钱包转账");
        AutofitTextView tvAddress = (AutofitTextView) view.findViewById(R.id.address);
        tvAddress.setText(address);
        tvPayAddress = (AutofitTextView) view.findViewById(R.id.pay_address);
        tvPayAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWalletList();
            }
        });
        final TextView tvKuanggong = (TextView) view.findViewById(R.id.kuanggong);
        final AutofitTextView tvKgDetil = (AutofitTextView) view.findViewById(R.id.kg_detail);
        BubbleSeekBar gasBar = (BubbleSeekBar) view.findViewById(R.id.gasBar);
        final EditText etPrice = (EditText) view.findViewById(R.id.et_price);
        View ok = view.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == walletBean) {
                    ToastUtil.show("请选择钱包");
                    return;
                }
                if (etPrice.getText().toString().length() == 0) {
                    ToastUtil.show("请填写金额");
                    return;
                }
                price = etPrice.getText().toString();
                showLoading();
                //获取账户余额
                WalletApi.balance(mActivity, walletBean.getAddress(), new JsonCallback<LzyResponse<ValueBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<ValueBean>> response) {
                        BigDecimal currentPrice = new BigDecimal(AppUtil.toD(response.body().data.getValue().replace("0x", "0")));
                        BigDecimal payPrice = new BigDecimal(price).add(new BigDecimal(showGas)).multiply(pEther);
                        if (currentPrice.subtract(payPrice).doubleValue() >= 0) {
                            oxPrice = "0x" + new BigInteger(new BigDecimal(price).multiply(pEther).setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString(),10).toString(16);

                            if (walletBean.getType().equals(Constant.GUANCHA)) {
                                showGuanchaPay();
                            } else {
                                showLocalPay();
                            }
                        } else {
                            hideLoading();
                            ToastUtil.show("余额不足！");
                            return;
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<ValueBean>> response) {
                        super.onError(response);
                        ToastUtil.show("余额获取失败");
                        hideLoading();
                    }
                });
            }
        });
        BigDecimal currentGas = gasPrice.multiply(new BigDecimal(Constant.GAS_LIMIT));
        if (currentGas.subtract(low).longValue() <= 0) {
            position = 0;
            gasBar.setProgress(0);
            showGas = low.divide(pEther, 8, BigDecimal.ROUND_HALF_UP).toString();
            tvKuanggong.setText(showGas);
            gas = low.divide(new BigDecimal(420993), 0, BigDecimal.ROUND_HALF_UP);
            tvKgDetil.setText("≈Gas(" + gas + ")*GasPrice(" + gasPrice.divide(pGwei, 2, BigDecimal.ROUND_HALF_UP) + "gwei)");
        } else if (currentGas.subtract(high).longValue() >= 0) {
            position = 100;
            gasBar.setProgress(position);
            showGas = high.divide(pEther, 8, BigDecimal.ROUND_HALF_UP).toString();
            tvKuanggong.setText(showGas);
            gas = high.divide(new BigDecimal(Constant.GAS_LIMIT), 0, BigDecimal.ROUND_HALF_UP);
            tvKgDetil.setText("≈Gas(" + gas + ")*GasPrice(" + gasPrice.divide(pGwei, 2, BigDecimal.ROUND_HALF_UP) + "gwei)");

        } else {
            position = currentGas.subtract(low).divide(distance, 0, BigDecimal.ROUND_HALF_UP).setScale(0, BigDecimal.ROUND_HALF_UP).multiply(p100).floatValue();
            gasBar.setProgress(position);
            showGas = currentGas.divide(pEther, 8, BigDecimal.ROUND_HALF_UP).toString();
            tvKuanggong.setText(showGas);
            gas = currentGas.divide(new BigDecimal(Constant.GAS_LIMIT), 0, BigDecimal.ROUND_HALF_UP);
            tvKgDetil.setText("≈Gas(" + gas + ")*GasPrice(" + gasPrice.divide(pGwei, 2, BigDecimal.ROUND_HALF_UP) + "gwei)");
        }
        gasBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                showGas = new BigDecimal(progressFloat).divide(p100).multiply(distance).add(low).divide(pEther).setScale(8, BigDecimal.ROUND_HALF_UP).toString();
                tvKuanggong.setText(showGas);
                gas = new BigDecimal(progressFloat).divide(p100).multiply(distance).add(low).divide(new BigDecimal(Constant.GAS_LIMIT), 0, BigDecimal.ROUND_HALF_UP);
                tvKgDetil.setText("≈Gas(" + gas + ")*GasPrice(" + gasPrice.divide(pGwei, 2, BigDecimal.ROUND_HALF_UP) + "gwei)");
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });

        mPopupWindow = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        mPopupWindow.setContentView(view);
        mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        //因为某些机型是虚拟按键的,所以要加上以下设置防止挡住按键.
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
        mPopupWindow.setBackgroundDrawable(colorDrawable);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                lp.alpha = 1f; //0.0-1.0
                mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                mActivity.getWindow().setAttributes(lp);
            }
        });

        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.7f; //0.0-1.0
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mActivity.getWindow().setAttributes(lp);
        mPopupWindow.showAtLocation(walletList, Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void showWalletList() {
        //选择钱包
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_dialog_ico_wallet, null, false);
        RecyclerView list = (RecyclerView) view.findViewById(R.id.wallet_list);
        IcoWalletAdapter adapter = new IcoWalletAdapter(this, R.layout.discover_item_ico_wallet, wallet);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                walletBean = wallet.get(position);
                tvPayAddress.setText(walletBean.getAddress());
                mMaterialDialog.dismiss();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mMaterialDialog = new MaterialDialog(mActivity).setView(view);
        mMaterialDialog.setBackgroundResource(R.drawable.trans_bg);
        mMaterialDialog.setCanceledOnTouchOutside(true);
        mMaterialDialog.show();
    }

    private void showLocalPay() {
        WalletApi.transactionCount(mActivity, walletBean.getAddress(), new JsonCallback<LzyResponse<CountBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CountBean>> response) {
                hideLoading();
                nonce = response.body().data.getCount();
                localTransfer();
            }

            @Override
            public void onError(Response<LzyResponse<CountBean>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
                hideLoading();
            }
        });


    }

    private void localTransfer() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_dialog_pass, null, false);
        final EditText pass = (EditText) view.findViewById(R.id.et_pass);
        TextView cancle = (TextView) view.findViewById(R.id.cancle);
        TextView ok = (TextView) view.findViewById(R.id.ok);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass.getText().toString().length() == 0) {
                    ToastUtil.show(getString(R.string.qingshurumima));
                    return;
                }
                showLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        byte[] b = new byte[0];
                        final AccountManager accountManager = AccountManager.get(mActivity);
                        Account[] accounts = accountManager.getAccountsByType("capital.fbg.wallet");
                        for (int i = 0; i < accounts.length; i++) {
                            if (accounts[i].name.equals(walletBean.getAddress())) {
                                //accountManager.getUserData(accounts[i], pass.getText().toString());
                                b = accountManager.getUserData(accounts[i], "wallet").getBytes();
                                break;
                            }
                        }
                        ETHWallet wallet = null;
                        try {
                            wallet = Unichain.openETHWallet(b, pass.getText().toString());
                        } catch (Exception e) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(getString(R.string.wallet_hit27));
                                    hideLoading();
                                }
                            });
                            return;
                        }
                        String data = "";
                        try {
                            data = "0x" + conver16HexStr(wallet.transferCurrency(nonce, "0x" + new BigInteger(gas.toPlainString(),16).toString(),"0x" + new BigInteger(new BigDecimal(Constant.GAS_LIMIT).setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString(),10).toString(16), address, oxPrice));
                        } catch (Exception e) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show("转账失败！请稍后重试");
                                    hideLoading();
                                }
                            });
                            return;
                        }
                        final String finalData = data;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mMaterialDialog.dismiss();
                                getOrderInfo(finalData);
                            }
                        });
                    }
                }).start();

            }
        });

        mMaterialDialog = new MaterialDialog(mActivity).setView(view);
        mMaterialDialog.setBackgroundResource(R.drawable.trans_bg);
        mMaterialDialog.setCanceledOnTouchOutside(true);
        mMaterialDialog.show();
    }

    private void showGuanchaPay() {
        WalletApi.transactionCount(mActivity, walletBean.getAddress(), new JsonCallback<LzyResponse<CountBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CountBean>> response) {
                hideLoading();
                nonce = response.body().data.getCount();
                transfer();
            }

            @Override
            public void onError(Response<LzyResponse<CountBean>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
                hideLoading();
            }
        });
    }

    private void transfer() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_dialog_watch_code, null, false);
        final ImageView code = (ImageView) view.findViewById(R.id.code);
        TextView ok = (TextView) view.findViewById(R.id.ok);
        code.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) code.getLayoutParams();
                params.height = code.getMeasuredWidth();
                code.setLayoutParams(params);
            }
        });
        final TransferBean codeJson = new TransferBean(walletBean.getAddress()
                , nonce
                , "0x" + new BigInteger(gas.toPlainString(),10).toString(16)
                , walletBean.getAddress()
                , oxPrice
                , price
                , showGas
                , ""
                , ""
                , 1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //文本类型
                try {
                    bitmap = QuanCodeUtils.createQuanCode(mActivity, new Gson().toJson(codeJson));
                    code.post(new Runnable() {
                        @Override
                        public void run() {
                            code.setImageBitmap(bitmap);
                        }
                    });
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanDialog();
            }
        });

        mMaterialDialog = new MaterialDialog(mActivity).setView(view);
        mMaterialDialog.setBackgroundResource(R.drawable.trans_bg);
        mMaterialDialog.setCanceledOnTouchOutside(true);
        mMaterialDialog.show();
    }

    private void scanDialog() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_dialog_scan_code, null, false);
        final ImageView code = (ImageView) view.findViewById(R.id.code);
        TextView ok = (TextView) view.findViewById(R.id.ok);
        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMainScan = true;
                keepTogo(ScanActivity.class);
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == coldData || coldData.length() == 0) {
                    ToastUtil.show("请扫描二维码");
                    return;
                }
                mMaterialDialog.dismiss();
                getOrderInfo(coldData);
            }
        });

        mMaterialDialog = new MaterialDialog(mActivity).setView(view);
        mMaterialDialog.setBackgroundResource(R.drawable.trans_bg);
        mMaterialDialog.setCanceledOnTouchOutside(true);
        mMaterialDialog.show();
    }

    private void getOrderInfo(String data) {
        showFixLoading();
        WalletApi.walletOrder(mActivity, walletBean.getId(), data, walletBean.getAddress(), address, "", new BigDecimal(price).multiply(pEther).setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString(), new BigDecimal(showGas).multiply(pEther).setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString(), walletBean.getCategory().getName(), new JsonCallback<LzyResponse<Object>>() {
            @Override
            public void onSuccess(Response<LzyResponse<Object>> response) {
                hideFixLoading();
                if (null != mPopupWindow) {
                    mPopupWindow.dismiss();
                }
                ToastUtil.show("转账成功");
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
            }

            @Override
            public void onError(Response<LzyResponse<Object>> response) {
                super.onError(response);
                hideFixLoading();
                if (response.getException().getMessage().contains("wallet_error")){
                    ToastUtil.show("服务器内部错误");
                    EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
                }else {
                    ToastUtil.show(getString(R.string.load_error));
                }
            }
        });
    }

    /**
     * byte数组转换为十六进制的字符串
     **/
    public static String conver16HexStr(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            if ((b[i] & 0xff) < 0x10)
                result.append("0");
            result.append(Long.toString(b[i] & 0xff, 16));
        }
        return result.toString().toUpperCase();
    }

    //代表每一个Tab
    class TabItem {
        //正常情况下显示的图片
        private int imageNormal;
        //选中情况下显示的图片
        private int imagePress;
        //tab的名字
        private int title;
        private String titleString;
        private Class<? extends Fragment> fragmentClass;

        private View view;
        private ImageView imageView;
        private TextView textView;
        private Bundle bundle;

        public TabItem(int imageNormal, int imagePress, int title, Class<? extends Fragment> fragmentClass) {
            this.imageNormal = imageNormal;
            this.imagePress = imagePress;
            this.title = title;
            this.fragmentClass = fragmentClass;
        }

        public Class<? extends Fragment> getFragmentClass() {
            return fragmentClass;
        }

        public int getImageNormal() {
            return imageNormal;
        }

        public int getImagePress() {
            return imagePress;
        }

        public int getTitle() {
            return title;
        }

        public String getTitleString() {
            if (title == 0) {
                return "";
            }
            if (!"".equals(titleString)) {
                titleString = getString(title);
            }
            return titleString;
        }

        public Bundle getBundle() {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putString("title", getTitleString());
            return bundle;
        }

        //还需要提供一个切换Tab方法---改变Tab样式
        public void setChecked(boolean isChecked) {
            if (imageView != null) {
                if (isChecked) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(mActivity, imagePress));
                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(mActivity, imageNormal));
                }
            }
            if (textView != null && title != 0) {
                if (isChecked) {
                    textView.setTextColor(getResources().getColor(R.color.c_232772));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.c_afafb0));
                }
            }
        }

        public View getView() {
            if (this.view == null) {
                this.view = getLayoutInflater().inflate(R.layout.view_tab_indicator, null);
                this.imageView = (ImageView) this.view.findViewById(R.id.iv_tab);
                this.textView = (TextView) this.view.findViewById(R.id.tv_tab);
                //判断资源是否存在,不再我就因此
                if (this.title == 0) {
                    this.textView.setVisibility(View.GONE);
                } else {
                    this.textView.setVisibility(View.VISIBLE);
                    this.textView.setText(getTitleString());
                }
                //绑定图片默认资源
                this.imageView.setImageDrawable(ContextCompat.getDrawable(mActivity, imageNormal));
            }
            return this.view;
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        //重置Tab样式
        for (int i = 0; i < tabItemList.size(); i++) {
            TabItem tabItem = tabItemList.get(i);
            if (tabId.equals(tabItem.getTitleString())) {
                //选中设置为选中壮体啊
                tabItem.setChecked(true);
            } else {
                //没有选择Tab样式设置为正常
                tabItem.setChecked(false);
            }
        }
    }

    public HashMap<String, TokenBean.ListBean> getTotleGnt() {
        return totleGnt;
    }

    public void setTotleGnt(HashMap<String, TokenBean.ListBean> totleGnt) {
        this.totleGnt = totleGnt;
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
            //2.点击的时间差如果大于2000，则提示用户点击两次退出
            if (System.currentTimeMillis() - mExitTime > 2000) {
                //3.保存当前时间
                mExitTime = System.currentTimeMillis();
                //4.提示
                ToastUtil.show("再按一次退出RYPTO BOX");
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
        if (!AppApplication.get().getSp().getBoolean(Constant.IS_CLOD, false)) {
            stopService(new Intent(this, AutoUpdateService.class));
        }
    }
}
