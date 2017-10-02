package capital.fbg.wallet.ui.wallet.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.model.Response;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import capital.fbg.wallet.AppApplication;
import capital.fbg.wallet.R;
import capital.fbg.wallet.base.BaseActivity;
import capital.fbg.wallet.common.Constant;
import capital.fbg.wallet.common.http.LzyResponse;
import capital.fbg.wallet.common.http.api.WalletApi;
import capital.fbg.wallet.common.http.callback.JsonCallback;
import capital.fbg.wallet.common.util.DensityUtil;
import capital.fbg.wallet.common.util.ToastUtil;
import capital.fbg.wallet.common.widget.SimpleToolbar;
import capital.fbg.wallet.event.BaseEventBusBean;
import me.drakeet.materialdialog.MaterialDialog;
import unichain.ETHWallet;
import unichain.Unichain;

/**
 * Created by Administrator on 2017/7/16.
 * 功能描述：
 * 版本：@version
 */

public class ColdWalletActivity extends BaseActivity {


    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_watch)
    TextView tvWatch;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.code)
    ImageView code;
    @BindView(R.id.address_rl)
    LinearLayout addressRl;
    @BindView(R.id.denghao)
    TextView denghao;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_ch_price)
    TextView tvChPrice;
    @BindView(R.id.add_gnt)
    ImageView addGnt;
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.titlePrice)
    TextView titlePrice;
    @BindView(R.id.titlell)
    LinearLayout titlell;
    @BindView(R.id.eth_img)
    ImageView ethImg;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.eth_price)
    TextView ethPrice;
    @BindView(R.id.tv_eth_ch_price)
    TextView tvEthChPrice;
    @BindView(R.id.eth_rl)
    RelativeLayout ethRl;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.wallet_list)
    SwipeMenuRecyclerView walletList;

    private String address;
    private boolean isFinish;

    private int icon;
    private MaterialDialog mMaterialDialog;

    @Override
    protected void getBundleExtras(Bundle extras) {
        address = extras.getString("address");
        icon = extras.getInt("icon");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_activity_etc_cold;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title.setText("我的资产");
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDialog();
            }
        });

        Glide.with(this).load(icon).crossFade().into(ivImg);
        SlidrConfig config = new SlidrConfig.Builder()
                .primaryColor(Color.parseColor("#000000"))
                .secondaryColor(Color.parseColor("#000000"))
                .position(SlidrPosition.LEFT)
                .sensitivity(1f)
                .scrimColor(Color.BLACK)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(2400)
                .distanceThreshold(0.25f)
                .edge(true)
                .edgeSize(0.18f)
                .build();// The % of the screen that counts as the edge, default 18%
//                                .listener(new SlidrListener(){...})

        Slidr.attach(this, config);
        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    title.setVisibility(View.VISIBLE);
                    titlell.setVisibility(View.INVISIBLE);
                }
                if (verticalOffset + appBarLayout.getTotalScrollRange() == 0) {
                    if (!isFinish) {
                        title.setVisibility(View.INVISIBLE);
                        titlell.setVisibility(View.VISIBLE);
                        startShowAnimation();
                        isFinish = true;
                    }
                } else {
                    if (isFinish) {
                        startHideAnimation();
                        isFinish = false;
                    }
                }
            }
        });

        tvAddress.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                android.content.ClipboardManager cm = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(address);
                ToastUtil.show(getString(R.string.copyaddress));
                return false;
            }
        });

        ethRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ClodTokenWalletActivity.class);
                intent.putExtra("address", address);
                keepTogo(intent);
            }
        });

        addressRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ClodReceiveActivity.class);
                intent.putExtra("address", address);
                keepTogo(intent);
            }
        });

        addressRl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(address);
                ToastUtil.show(getString(R.string.copyaddress));
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        tvAddress.setText(address);
        tvWatch.setVisibility(View.VISIBLE);
        String wallets = AppApplication.get().getSp().getString(Constant.WALLETS_CLOD_BEIFEN, "");
        if (wallets.contains(address)) {
            tvWatch.setText("冷钱包");
            tvWatch.setBackgroundResource(R.drawable.round_999dp_bule_bg);
        } else {
            tvWatch.setText("未备份");
            tvWatch.setBackgroundResource(R.drawable.round_solid_pink_bg);
        }
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
    }

    private void showSelectDialog() {

        View selectPopupWin = LayoutInflater.from(this).inflate(R.layout.view_popup_wallet_detaile, null, false);
        View hit = selectPopupWin.findViewById(R.id.hit);
        final TextView zhujici = (TextView) selectPopupWin.findViewById(R.id.zhujici);
        TextView keystore = (TextView) selectPopupWin.findViewById(R.id.keystore);
        TextView delete = (TextView) selectPopupWin.findViewById(R.id.delete);

        String wallets = AppApplication.get().getSp().getString(Constant.WALLETS_ZJC_BEIFEN, "");
        if (wallets.contains(address)) {
            hit.setVisibility(View.GONE);
            zhujici.setVisibility(View.GONE);
        }

        final PopupWindow window = new PopupWindow(selectPopupWin, DensityUtil.dip2px(this, 140), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.update();
        window.showAsDropDown(txtRightTitle);

        zhujici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                Account account = null;
                                for (int i = 0; i < accounts.length; i++) {
                                    if (accounts[i].name.equals(address)) {
                                        //accountManager.getUserData(accounts[i], pass.getText().toString());
                                        b = accountManager.getUserData(accounts[i], "wallet").getBytes();
                                        account = accounts[i];
                                        break;
                                    }
                                }
                                ETHWallet ethWallet = null;
                                try {
                                    ethWallet = Unichain.openETHWallet(b, pass.getText().toString());
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
                                String zjc = "";
                                try {
                                    zjc = ethWallet.mnemonic();
                                } catch (Exception e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideLoading();
                                            ToastUtil.show("备份助记词失败！请稍后重试");
                                        }
                                    });
                                    return;
                                }
                                final String finalZjc = zjc;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideLoading();
                                        mMaterialDialog.dismiss();
                                        Intent intent = new Intent(mActivity, ClodWalletTipOneActivity.class);
                                        intent.putExtra("zjc", finalZjc);
                                        intent.putExtra("address", address);
                                        keepTogo(intent);
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
                window.dismiss();
            }
        });

        keystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(mActivity).inflate(R.layout.view_dialog_keystore, null, false);
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
                                Account account = null;
                                for (int i = 0; i < accounts.length; i++) {
                                    if (accounts[i].name.equals(address)) {
                                        //accountManager.getUserData(accounts[i], pass.getText().toString());
                                        b = accountManager.getUserData(accounts[i], "wallet").getBytes();
                                        account = accounts[i];
                                        break;
                                    }
                                }
                                ETHWallet wal = null;
                                try {
                                    wal = Unichain.openETHWallet(b, pass.getText().toString());
                                } catch (Exception e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideLoading();
                                            ToastUtil.show(getString(R.string.wallet_hit27));
                                        }
                                    });
                                    return;
                                }
                                byte[] keys = new byte[0];
                                try {
                                    keys = wal.encrypt(pass.getText().toString());
                                } catch (Exception e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideLoading();
                                            ToastUtil.show("备份keystory失败！请稍后重试");
                                        }
                                    });
                                    return;
                                }
                                accountManager.setUserData(account, "type", Constant.BEIFEN);

                                String wallets = AppApplication.get().getSp().getString(Constant.WALLETS_CLOD_BEIFEN, "");
                                if (!wallets.contains(address)) {
                                    wallets = wallets + address + ",";
                                    AppApplication.get().getSp().putString(Constant.WALLETS_CLOD_BEIFEN, wallets);
                                }
                                final byte[] finalKeys = keys;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideLoading();
                                        mMaterialDialog.dismiss();
                                        tvWatch.setVisibility(View.GONE);
                                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET));
                                        Intent intent1 = new Intent(Intent.ACTION_SEND);
                                        try {
                                            intent1.putExtra(Intent.EXTRA_TEXT, new String(finalKeys, "utf-8"));
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        intent1.setType("text/plain");
                                        startActivity(Intent.createChooser(intent1, "share"));
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
                window.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                Account account = null;
                                for (int i = 0; i < accounts.length; i++) {
                                    if (accountManager.getUserData(accounts[i], "wallet_type").equals("clod")) {
                                        if (accounts[i].name.equals(address)) {
                                            account = accounts[i];
                                            //accountManager.getUserData(accounts[i], pass.getText().toString());
                                            b = accountManager.getUserData(accounts[i], "wallet").getBytes();
                                            break;
                                        }
                                    }
                                }
                                try {
                                    Unichain.openETHWallet(b, pass.getText().toString());
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
                                final Account finalAccount = account;
                                if (null != finalAccount) {
                                    accountManager.removeAccount(finalAccount, null, null);
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideLoading();
                                        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_WALLET));
                                        ToastUtil.show(getString(R.string.shanchuchenggong));
                                        finish();
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
                window.dismiss();
            }
        });
    }

    public void startHideAnimation() {
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

    public void startShowAnimation() {
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
