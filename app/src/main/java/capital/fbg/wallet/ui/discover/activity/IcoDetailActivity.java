package capital.fbg.wallet.ui.discover.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.lzy.okgo.model.Response;
import com.xw.repo.BubbleSeekBar;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import butterknife.BindView;
import capital.fbg.wallet.AppApplication;
import capital.fbg.wallet.R;
import capital.fbg.wallet.base.BaseActivity;
import capital.fbg.wallet.bean.CommonListBean;
import capital.fbg.wallet.bean.CommonRecordBean;
import capital.fbg.wallet.bean.CountBean;
import capital.fbg.wallet.bean.GasBean;
import capital.fbg.wallet.bean.IcoGasBean;
import capital.fbg.wallet.bean.IcoListBean;
import capital.fbg.wallet.bean.TransferBean;
import capital.fbg.wallet.bean.TxHashBean;
import capital.fbg.wallet.bean.ValueBean;
import capital.fbg.wallet.bean.WalletBean;
import capital.fbg.wallet.common.Constant;
import capital.fbg.wallet.common.http.LzyResponse;
import capital.fbg.wallet.common.http.api.DiscoverApi;
import capital.fbg.wallet.common.http.api.WalletApi;
import capital.fbg.wallet.common.http.callback.JsonCallback;
import capital.fbg.wallet.common.util.AppManager;
import capital.fbg.wallet.common.util.AppUtil;
import capital.fbg.wallet.common.util.QuanCodeUtils;
import capital.fbg.wallet.common.util.ToastUtil;
import capital.fbg.wallet.common.widget.MaterialProgressBar;
import capital.fbg.wallet.ui.ScanActivity;
import capital.fbg.wallet.event.BaseEventBusBean;
import capital.fbg.wallet.ui.discover.adapter.IcoWalletAdapter;
import me.drakeet.materialdialog.MaterialDialog;
import me.grantland.widget.AutofitTextView;
import unichain.ETHWallet;
import unichain.Unichain;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class IcoDetailActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.web)
    FrameLayout web;
    @BindView(R.id.progress)
    MaterialProgressBar progress;

    private WebView webView;

    private IcoListBean ico;
    private MaterialDialog mMaterialDialog;

    private IcoWalletAdapter adapter;
    private ArrayList<WalletBean> wallet = new ArrayList<>();

    private WalletBean walletBean;
    private IcoGasBean gasBean;
    private float position;

    private BigDecimal low = new BigDecimal("25200000000000").multiply(new BigDecimal(Constant.GAS_LIMIT)).divide(new BigDecimal(21000),0,BigDecimal.ROUND_HALF_UP);
    private BigDecimal high = new BigDecimal("2520120000000000").multiply(new BigDecimal(Constant.GAS_LIMIT)).divide(new BigDecimal(21000),0,BigDecimal.ROUND_HALF_UP);
    private BigDecimal distance = high.subtract(low);
    private BigDecimal p100 = new BigDecimal(100);
    private BigDecimal pEther = new BigDecimal("1000000000000000000");
    private BigDecimal pGwei = new BigDecimal("1000000000");
    private BigDecimal gasPrice;

    private String price;
    private String oxPrice;
    private String nonce;
    private BigDecimal gas;
    private String showGas;
    private Bitmap bitmap;
    private String coldData;
    private String hash;

    @Override
    protected void getBundleExtras(Bundle extras) {
        ico = (IcoListBean) extras.getSerializable("ico");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.discover_article_detail;
    }

    @Override
    protected void initView() {
        txtMainTitle.setText(ico.getTitle());
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtRightTitle.setText("支付测试");
        txtRightTitle.setCompoundDrawables(null, null, null, null);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayInfo();
            }
        });

        webView = new WebView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(params);
        web.addView(webView);

        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //声明WebSettings子类
                WebSettings webSettings = webView.getSettings();

                //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
                webSettings.setJavaScriptEnabled(true);

                //设置自适应屏幕，两者合用
                webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
                webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

                //其他细节操作
                webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
                webSettings.setAllowFileAccess(true); //设置可以访问文件
                webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
                webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
                webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

                //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        progress.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        // TODO Auto-generated method stub
                        //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                        view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        progress.setVisibility(View.GONE);
                    }
                });

                webView.loadUrl(ico.getUrl());
            }
        }, 600);

    }

    @Override
    protected void initData() {
        showLoading();
    }

    private void showPayInfo() {
        WalletApi.wallet(mActivity, new JsonCallback<LzyResponse<CommonListBean<WalletBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                wallet.clear();
                if (null != response.body().data.getList() && response.body().data.getList().size() > 0) {
                    String wallets = AppApplication.get().getSp().getString(Constant.WALLETS, "");
                    String wallets_beifen = AppApplication.get().getSp().getString(Constant.WALLETS_BEIFEN, "");
                    for (int i = 0; i < response.body().data.getList().size(); i++) {
                        if (wallets.contains(response.body().data.getList().get(i).getAddress())) {
                            if (wallets_beifen.contains(response.body().data.getList().get(i).getAddress())) {
                                response.body().data.getList().get(i).setType(Constant.BEIFEN);
                            } else {
                                response.body().data.getList().get(i).setType(Constant.ZHENGCHANG);
                            }
                        } else {
                            response.body().data.getList().get(i).setType(Constant.GUANCHA);
                        }
                    }
                    wallet.addAll(response.body().data.getList());
                    showWalletList();
                } else {
                    ToastUtil.show("您还没有钱包");
                }

            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                super.onError(response);
                ToastUtil.show("获取钱包列表失败，请检查网络后重试");
            }
        });

    }

    private void showWalletList() {
        //选择钱包
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_dialog_ico_wallet, null, false);
        RecyclerView list = (RecyclerView) view.findViewById(R.id.wallet_list);
        adapter = new IcoWalletAdapter(this, R.layout.discover_item_ico_wallet, wallet);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                walletBean = wallet.get(position);
                //获取ico gas
                DiscoverApi.getGas(this, walletBean.getCategory_id(), 0, new JsonCallback<LzyResponse<CommonRecordBean<IcoGasBean>>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<CommonRecordBean<IcoGasBean>>> response) {
                        mMaterialDialog.dismiss();
                        gasBean = response.body().data.getRecord();
                        //请求gasPrice
                        getGasPrcie();
                    }

                    @Override
                    public void onError(Response<LzyResponse<CommonRecordBean<IcoGasBean>>> response) {
                        super.onError(response);
                        ToastUtil.show("Ico信息获取失败，请检查网络后重试");
                    }
                });
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
        tvOrder.setText(ico.getTitle());
        AutofitTextView tvAddress = (AutofitTextView) view.findViewById(R.id.address);
        tvAddress.setText(ico.getAddress());
        AutofitTextView tvPayAddress = (AutofitTextView) view.findViewById(R.id.pay_address);
        tvPayAddress.setText(walletBean.getAddress());
        final TextView tvKuanggong = (TextView) view.findViewById(R.id.kuanggong);
        final AutofitTextView tvKgDetil = (AutofitTextView) view.findViewById(R.id.kg_detail);
        BubbleSeekBar gasBar = (BubbleSeekBar) view.findViewById(R.id.gasBar);
        final EditText etPrice = (EditText) view.findViewById(R.id.et_price);
        View ok = view.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPrice.getText().toString().length() == 0) {
                    ToastUtil.show("请填写金额");
                    return;
                }
                price = etPrice.getText().toString();
                showLoading();
                //获取账户余额
                WalletApi.balance(this, walletBean.getAddress(), new JsonCallback<LzyResponse<ValueBean>>() {
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
        Long gasLong = Constant.GAS_LIMIT;
        if (null != gasBean.getGas()) {
            gasLong = Long.parseLong(gasBean.getGas().replace("0x", "0"), 16);
        }
        BigDecimal currentGas = gasPrice.multiply(new BigDecimal(gasLong));
        if (currentGas.subtract(low).longValue() <= 0) {
            position = 0;
            gasBar.setProgress(0);
            showGas = low.divide(pEther, 8, BigDecimal.ROUND_HALF_UP).toString();
            tvKuanggong.setText(showGas);
            gas = low.divide(gasPrice, 0, BigDecimal.ROUND_HALF_UP);
            tvKgDetil.setText("≈Gas(" + gas + ")*GasPrice(" + gasPrice.divide(pGwei, 2, BigDecimal.ROUND_HALF_UP) + "gwei)");
        } else if (currentGas.subtract(high).longValue() >= 0) {
            position = 100;
            gasBar.setProgress(position);
            showGas = high.divide(pEther, 8, BigDecimal.ROUND_HALF_UP).toString();
            tvKuanggong.setText(showGas);
            gas = high.divide(gasPrice, 0, BigDecimal.ROUND_HALF_UP);
            tvKgDetil.setText("≈Gas(" + gas + ")*GasPrice(" + gasPrice.divide(pGwei, 2, BigDecimal.ROUND_HALF_UP) + "gwei)");

        } else {
            position = currentGas.subtract(low).divide(distance, 0, BigDecimal.ROUND_HALF_UP).setScale(0, BigDecimal.ROUND_HALF_UP).multiply(p100).floatValue();
            gasBar.setProgress(position);
            showGas = currentGas.divide(pEther, 8, BigDecimal.ROUND_HALF_UP).toString();
            tvKuanggong.setText(showGas);
            gas = currentGas.divide(gasPrice, 0, BigDecimal.ROUND_HALF_UP);
            tvKgDetil.setText("≈Gas(" + gas + ")*GasPrice(" + gasPrice.divide(pGwei, 2, BigDecimal.ROUND_HALF_UP) + "gwei)");
        }
        gasBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                showGas = new BigDecimal(progressFloat).divide(p100).multiply(distance).add(low).divide(pEther).setScale(8, BigDecimal.ROUND_HALF_UP).toString();
                tvKuanggong.setText(showGas);
                gas = new BigDecimal(progressFloat).divide(p100).multiply(distance).add(low).divide(gasPrice, 0, BigDecimal.ROUND_HALF_UP);
                tvKgDetil.setText("≈Gas(" + gas + ")*GasPrice(" + gasPrice.divide(pGwei, 2, BigDecimal.ROUND_HALF_UP) + "gwei)");
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });

        final PopupWindow mPopupWindow = new PopupWindow(view,
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
        mPopupWindow.showAtLocation(txtRightTitle, Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void showLocalPay() {
        WalletApi.transactionCount(this, walletBean.getAddress(), new JsonCallback<LzyResponse<CountBean>>() {
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
                            data = "0x" + conver16HexStr(wallet.transferCurrency(nonce, "0x" + new BigInteger(gas.toPlainString(),10).toString(16),"0x" + new BigInteger(new BigDecimal(Constant.GAS_LIMIT).setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString(),10).toString(16), ico.getAddress(), oxPrice));
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
        WalletApi.transactionCount(this, walletBean.getAddress(), new JsonCallback<LzyResponse<CountBean>>() {
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
                , ico.getAddress()
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
        showLoading();
        WalletApi.sendRaw(this, data, new JsonCallback<LzyResponse<TxHashBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<TxHashBean>> response) {
                hash = response.body().data.getTxHash();
                DiscoverApi.icoOrder(mActivity
                        , walletBean.getId()
                        , ico.getId()
                        , hash
                        , walletBean.getAddress()
                        , ico.getAddress()
                        , price
                        , showGas
                        , hash
                        , new JsonCallback<LzyResponse<Object>>() {
                            @Override
                            public void onSuccess(Response<LzyResponse<Object>> response) {
                                hideLoading();
                                ToastUtil.show("众筹成功");
                                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PRICE));
                                AppManager.getAppManager().finishActivity(IcoDetailActivity.class);
                                finshTogo(IcoPaySuccesActivity.class);
                            }

                            @Override
                            public void onError(Response<LzyResponse<Object>> response) {
                                super.onError(response);
                                ToastUtil.show(getString(R.string.load_error));
                                hideLoading();
                            }
                        });
            }

            @Override
            public void onError(Response<LzyResponse<TxHashBean>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
                hideLoading();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != webView) {
            webView.destroy();
            webView = null;
        }
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

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_KEY) {
            capital.fbg.wallet.event.KeyEvent key = (capital.fbg.wallet.event.KeyEvent) event.getData();
            coldData = key.getKey();
        }
    }
}
