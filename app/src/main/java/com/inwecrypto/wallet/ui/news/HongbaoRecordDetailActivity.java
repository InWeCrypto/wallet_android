package com.inwecrypto.wallet.ui.news;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.HongbaoGntBean;
import com.inwecrypto.wallet.bean.HongbaoRecordDetaileBean;
import com.inwecrypto.wallet.bean.SendHongbaoBean;
import com.inwecrypto.wallet.bean.ValueBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.me.activity.CommonWebActivity;
import com.inwecrypto.wallet.ui.news.adapter.HongbaoReciveDetaileAdapter;
import com.lzy.okgo.model.Response;

import net.qiujuer.genius.ui.widget.Button;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.inwecrypto.wallet.common.http.Url.ORDER_TEST_ULR;
import static com.inwecrypto.wallet.common.http.Url.ORDER_ULR;

/**
 * 作者：xiaoji06 on 2018/4/23 16:03
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class HongbaoRecordDetailActivity extends BaseActivity {


    @BindView(R.id.bg)
    ImageView bg;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.state)
    TextView state;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.fee)
    TextView fee;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.lbtxid)
    TextView lbtxid;
    @BindView(R.id.hbtxid)
    TextView hbtxid;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.see)
    Button see;
    @BindView(R.id.hit)
    TextView hit;
    @BindView(R.id.address1)
    TextView address1;
    @BindView(R.id.num1)
    TextView num1;
    @BindView(R.id.time1)
    TextView time1;
    @BindView(R.id.state1)
    TextView state1;
    @BindView(R.id.address2)
    TextView address2;
    @BindView(R.id.num2)
    TextView num2;
    @BindView(R.id.time2)
    TextView time2;
    @BindView(R.id.state2)
    TextView state2;
    @BindView(R.id.createll)
    LinearLayout createll;
    @BindView(R.id.get_num)
    TextView getNum;
    @BindView(R.id.totle1)
    TextView totle1;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.listll)
    LinearLayout listll;
    @BindView(R.id.back_num)
    TextView backNum;
    @BindView(R.id.totle2)
    TextView totle2;
    @BindView(R.id.address3)
    TextView address3;
    @BindView(R.id.num3)
    TextView num3;
    @BindView(R.id.time3)
    TextView time3;
    @BindView(R.id.state3)
    TextView state3;
    @BindView(R.id.backll)
    LinearLayout backll;
    @BindView(R.id.scroll)
    NestedScrollView scroll;
    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.titlefl)
    FrameLayout titlefl;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private ArrayList<HongbaoRecordDetaileBean.DrawsBean> data = new ArrayList<>();
    private HongbaoReciveDetaileAdapter adapter;

    private SendHongbaoBean hongbaoBean;
    private HongbaoRecordDetaileBean hongbaoDetaileBean;

    public static int speed = 10000;
    private Timer timer;
    private TimerTask task;
    public int currentBlock;
    private boolean isStart;

    private boolean isAuth;
    private boolean isCreate;

    private int titleHight;
    private int bgHight;


    @Override
    protected void getBundleExtras(Bundle extras) {
        hongbaoBean = (SendHongbaoBean) extras.getSerializable("redbag");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.hongbao_record_detail_activity;
    }

    @Override
    protected void initView() {
        int hight = (int) (ScreenUtils.getScreenWidth(this) / 2250.0f * 687.0f);
        bgHight = hight;
        bg.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, hight));
        titlefl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                titlefl.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                titleHight = titlefl.getMeasuredHeight();
            }
        });

        scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e("aaaa", "scrollY:" + scrollY + " bgHight:" + bgHight + " titleHight:" + titleHight);
                int alpha = (int) ((scrollY) * 1.0f / (bgHight - titleHight) * 1.0f * 255);
                if (alpha >= 255) {
                    alpha = 255;
                }
                if (alpha < 0) {
                    alpha = 0;
                }
                String al = new BigInteger(alpha + "", 10).toString(16);
                String color = "#" + (al.length() == 1 ? ("0" + al) : al) + "B23E2E";
                Log.e("bbb", color);
                // 只是layout背景透明(仿知乎滑动效果)
                titlefl.setBackgroundColor(Color.parseColor(color));
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        num.setText(new BigDecimal(hongbaoBean.getRedbag()).setScale(4, RoundingMode.DOWN).toPlainString() + hongbaoBean.getRedbag_symbol());
        fee.setText(getString(R.string.shouxufei) + ":" + (hongbaoBean.getFee().equals("") ? "" : (new BigDecimal(hongbaoBean.getFee()).setScale(8, RoundingMode.DOWN).toPlainString()+ "ETH")));
        address.setText(Html.fromHtml("<u>" + hongbaoBean.getRedbag_addr() + "</u>"));
        lbtxid.setText(Html.fromHtml("<u>" + hongbaoBean.getAuth_tx_id() + "</u>"));
        hbtxid.setText(Html.fromHtml("<u>" + hongbaoBean.getRedbag_tx_id() + "</u>"));
        time.setText(getString(R.string.hongbaochaungjianshijian) + ":" + AppUtil.getGTime(hongbaoBean.getCreated_at()));
        if (App.get().isZh()) {
            hit.setText(Html.fromHtml("未领取的红包将于<font color='#E24C0A'>24H</font>后退回"));
        } else {
            hit.setText(Html.fromHtml("Red Packets not open within <font color='#E24C0A'>24H</font> will be refunded"));
        }

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setPrimaryClip(ClipData.newPlainText(null, hongbaoBean.getRedbag_addr()));
                ToastUtil.show(getString(R.string.fuzhichenggong));
            }
        });

        lbtxid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity, CommonWebActivity.class);
                intent.putExtra("title",getString(R.string.chaxunjiaoyi));
                intent.putExtra("url", (App.isMain?ORDER_ULR:ORDER_TEST_ULR)+hongbaoBean.getAuth_tx_id());
                keepTogo(intent);
            }
        });

        hbtxid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity, CommonWebActivity.class);
                intent.putExtra("title",getString(R.string.chaxunjiaoyi));
                intent.putExtra("url", (App.isMain?ORDER_ULR:ORDER_TEST_ULR)+hongbaoBean.getRedbag_tx_id());
                keepTogo(intent);
            }
        });

        lbtxid.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 将文本内容放到系统剪贴板里。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setPrimaryClip(ClipData.newPlainText(null, hongbaoBean.getAuth_tx_id()));
                ToastUtil.show(getString(R.string.fuzhichenggong));
                return true;
            }
        });

        hbtxid.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 将文本内容放到系统剪贴板里。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setPrimaryClip(ClipData.newPlainText(null, hongbaoBean.getRedbag_tx_id()));
                ToastUtil.show(getString(R.string.fuzhichenggong));
                return true;
            }
        });

        if (hongbaoBean.getStatus() == 2 || hongbaoBean.getStatus() == -2 || hongbaoBean.getStatus() == -202) {//授权
            if (hongbaoBean.getStatus() == 2) {
                state.setText(R.string.lijindabaozhong);
                state1.setText(R.string.dabaozhong);
                setBlock();
                isStart = true;
            } else if (hongbaoBean.getStatus() == -2) {
                state.setText(R.string.lijindabaoshibai);
                state1.setText(R.string.dabaoshibai);
                see.setEnabled(false);
                state1.setTextColor(Color.parseColor("#E24C0A"));
                if (App.get().isZh()) {
                    hit.setText(Html.fromHtml("礼金打包<font color='#E24C0A'>失败</font>,需重新创建红包.如有疑问请联系我们."));
                } else {
                    hit.setText(Html.fromHtml("Packing <font color='#E24C0A'>Failed</font>,You need to create Red Packet again. If you have any questions, please contact with us."));
                }
            } else if (hongbaoBean.getStatus() == -202) {
                state.setText(R.string.zhunbeilijindabao);
                state1.setText(R.string.zhunbeidabao);
            }
            fee.setText(R.string.shouxufei + "：");
            address1.setText(hongbaoBean.getRedbag_addr());
            num1.setText("-" + new BigDecimal(hongbaoBean.getRedbag()).setScale(4, RoundingMode.DOWN).toPlainString() + hongbaoBean.getRedbag_symbol());
            time1.setText(AppUtil.getGTime(hongbaoBean.getAuth_at()));
        } else if (hongbaoBean.getStatus() == 3 || hongbaoBean.getStatus() == -3 || hongbaoBean.getStatus() == -303) {//创建红包
            if (hongbaoBean.getStatus() == 3) {
                state.setText(R.string.hongbaochuangjianzhong);
                state2.setText(R.string.chuangjianzhong);
                setBlock();
                isStart = true;
            } else if (hongbaoBean.getStatus() == -3) {
                state.setText(R.string.hongbaochuangjianshibai);
                state2.setText(R.string.chuangjianshibai);
                state2.setTextColor(Color.parseColor("#E24C0A"));
                if (hongbaoBean.getGlobal_status() != 8) {
                    see.setEnabled(false);
                    if (App.get().isZh()) {
                        hit.setText(Html.fromHtml("红包创建<font color='#E24C0A'>失败</font>,需重新创建红包.如有疑问请联系我们."));
                    } else {
                        hit.setText(Html.fromHtml("Creation <font color='#E24C0A'>Failed</font>,You need to create Red Packet again. If you have any questions, please contact with us."));
                    }
                }
            } else if (hongbaoBean.getStatus() == -303) {
                state.setText(R.string.zhunbeichuangjianhongbao);
                state2.setText(R.string.zhunbeichuangjian);
            }
            address1.setText(hongbaoBean.getRedbag_addr());
            num1.setText("-" + new BigDecimal(hongbaoBean.getRedbag()).setScale(4, RoundingMode.DOWN).toPlainString() + hongbaoBean.getRedbag_symbol());
            time1.setText(AppUtil.getGTime(hongbaoBean.getAuth_at()));
            state1.setText(R.string.chenggong);
            createll.setVisibility(View.VISIBLE);

            address2.setText(hongbaoBean.getFee_addr());
            num2.setText("-" + new BigDecimal(hongbaoBean.getFee()).setScale(8, RoundingMode.DOWN).toPlainString() + "ETH");
            time2.setText(AppUtil.getGTime(hongbaoBean.getRedbag_at()));

        } else if (hongbaoBean.getStatus() == 4) {//领取中
            state.setText(R.string.lingquzhong);
            address1.setText(hongbaoBean.getRedbag_addr());
            num1.setText("-" + new BigDecimal(hongbaoBean.getRedbag()).setScale(4, RoundingMode.DOWN).toPlainString() + hongbaoBean.getRedbag_symbol());
            time1.setText(AppUtil.getGTime(hongbaoBean.getAuth_at()));
            state1.setText(R.string.chenggong);
            createll.setVisibility(View.VISIBLE);

            address2.setText(hongbaoBean.getFee_addr());
            num2.setText("-" + new BigDecimal(hongbaoBean.getFee()).setScale(8, RoundingMode.DOWN).toPlainString() + "ETH");
            time2.setText(AppUtil.getGTime(hongbaoBean.getRedbag_at()));
            state2.setText(R.string.chenggong);

            listll.setVisibility(View.VISIBLE);

            getNum.setText(R.string.lingqushuliang + " " + hongbaoBean.getDraw_redbag_number() + "/" + hongbaoBean.getRedbag_number());

        } else if (hongbaoBean.getStatus() == 1) {//完成
            state.setText(R.string.wancheng);
            address1.setText(hongbaoBean.getRedbag_addr());
            num1.setText("-" + new BigDecimal(hongbaoBean.getRedbag()).setScale(4, RoundingMode.DOWN).toPlainString() + hongbaoBean.getRedbag_symbol());
            time1.setText(AppUtil.getGTime(hongbaoBean.getAuth_at()));
            state1.setText(R.string.chenggong);
            createll.setVisibility(View.VISIBLE);

            address2.setText(hongbaoBean.getFee_addr());
            num2.setText("-" + new BigDecimal(hongbaoBean.getFee()).setScale(8, RoundingMode.DOWN).toPlainString() + "ETH");
            time2.setText(AppUtil.getGTime(hongbaoBean.getRedbag_at()));
            state2.setText(R.string.chenggong);

            listll.setVisibility(View.VISIBLE);
            getNum.setText(R.string.lingqushuliang + " " + hongbaoBean.getDraw_redbag_number() + "/" + hongbaoBean.getRedbag_number());

            if (!hongbaoBean.getRedbag_back().equals("")) {
                backll.setVisibility(View.VISIBLE);
                backNum.setText(getString(R.string.chehuishuliang) + " " + (hongbaoBean.getRedbag_number() - hongbaoBean.getDraw_redbag_number()));
                totle2.setText("");
                address3.setText(hongbaoBean.getRedbag_addr());
                num3.setText("");
                time3.setText(null == hongbaoBean.getRedbag_back_at() ? "" : AppUtil.getGTime(hongbaoBean.getRedbag_back_at()));
                if (hongbaoBean.getRedbag_back_tx_status() == 0) {
                    state3.setText(R.string.chehuizhong);
                } else if (hongbaoBean.getRedbag_back_tx_status() == 1) {
                    state3.setText(R.string.chenggong);
                } else {
                    state3.setText(R.string.shibai);
                }
            }
        }

        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == hongbaoDetaileBean) return;
                if (hongbaoDetaileBean.getStatus() == 2 || hongbaoDetaileBean.getStatus() == -2 || hongbaoDetaileBean.getStatus() == -202) {//授权
                    if (hongbaoDetaileBean.getStatus() == 2) {
                        if (isAuth) {
                            Intent intent = new Intent(mActivity, SendHongbao2Activity.class);
                            intent.putExtra("id", hongbaoDetaileBean.getId());
                            intent.putExtra("selectAddress", hongbaoDetaileBean.getRedbag_addr());
                            intent.putExtra("gntGas", hongbaoDetaileBean.getGnt_category().getGas());
                            intent.putExtra("gntName", hongbaoDetaileBean.getGnt_category().getName());
                            intent.putExtra("gntAddress", hongbaoDetaileBean.getGnt_category().getAddress());
                            intent.putExtra("gntDecimal", hongbaoDetaileBean.getGnt_category().getDecimals());
                            intent.putExtra("redbagAddress", hongbaoDetaileBean.getRedbag_addr());
                            intent.putExtra("hongbaofei", hongbaoDetaileBean.getRedbag());
                            intent.putExtra("hongbaoCount", hongbaoDetaileBean.getRedbag_number());
                            keepTogo(intent);
                        } else {
                            Intent intent = new Intent(mActivity, SendHongbaoPendingActivity.class);
                            intent.putExtra("id", hongbaoDetaileBean.getId());
                            intent.putExtra("selectAddress", hongbaoDetaileBean.getRedbag_addr());
                            intent.putExtra("gntGas", hongbaoDetaileBean.getGnt_category().getGas());
                            intent.putExtra("gntName", hongbaoDetaileBean.getGnt_category().getName());
                            intent.putExtra("gntAddress", hongbaoDetaileBean.getGnt_category().getAddress());
                            intent.putExtra("gntDecimal", hongbaoDetaileBean.getGnt_category().getDecimals());
                            intent.putExtra("redbagAddress", hongbaoDetaileBean.getRedbag_addr());
                            intent.putExtra("hongbaofei", hongbaoDetaileBean.getRedbag());
                            intent.putExtra("hongbaoCount", hongbaoDetaileBean.getRedbag_number());
                            keepTogo(intent);
                        }
                    } else if (hongbaoDetaileBean.getStatus() == -2) {
//                        Intent intent = new Intent(mActivity, SendHongbao1Activity.class);
//                        keepTogo(intent);
                    } else if (hongbaoDetaileBean.getStatus() == -202) {
                        Intent intent = new Intent(mActivity, SendHongbaoPendingActivity.class);
                        intent.putExtra("id", hongbaoDetaileBean.getId());
                        intent.putExtra("selectAddress", hongbaoDetaileBean.getRedbag_addr());
                        intent.putExtra("gntGas", hongbaoDetaileBean.getGnt_category().getGas());
                        intent.putExtra("gntName", hongbaoDetaileBean.getGnt_category().getName());
                        intent.putExtra("gntAddress", hongbaoDetaileBean.getGnt_category().getAddress());
                        intent.putExtra("gntDecimal", hongbaoDetaileBean.getGnt_category().getDecimals());
                        intent.putExtra("redbagAddress", hongbaoDetaileBean.getRedbag_addr());
                        intent.putExtra("hongbaofei", hongbaoDetaileBean.getRedbag());
                        intent.putExtra("hongbaoCount", hongbaoDetaileBean.getRedbag_number());
                        intent.putExtra("gnt",hongbaoDetaileBean.getGnt_category());
                        intent.putExtra("preGas",hongbaoDetaileBean.getAuth_gas());
                        keepTogo(intent);
                    }
                } else if (hongbaoDetaileBean.getStatus() == 3 || hongbaoDetaileBean.getStatus() == -3 || hongbaoDetaileBean.getStatus() == -303) {//创建红包
                    if (hongbaoDetaileBean.getStatus() == 3) {
                        if (isCreate) {
                            Intent intent = new Intent(mActivity, SendHongbao3Activity.class);
                            intent.putExtra("id", hongbaoDetaileBean.getId());
                            intent.putExtra("redbagAddress", hongbaoDetaileBean.getRedbag_addr());
                            intent.putExtra("gntName",hongbaoDetaileBean.getRedbag_symbol());
                            keepTogo(intent);
                        } else {
                            Intent intent = new Intent(mActivity, SendHongbaoCreateActivity.class);
                            intent.putExtra("id", hongbaoDetaileBean.getId());
                            intent.putExtra("selectAddress", hongbaoDetaileBean.getRedbag_addr());
                            intent.putExtra("gntGas", hongbaoDetaileBean.getGnt_category().getGas());
                            intent.putExtra("gntName", hongbaoDetaileBean.getGnt_category().getName());
                            intent.putExtra("gntAddress", hongbaoDetaileBean.getGnt_category().getAddress());
                            intent.putExtra("gntDecimal", hongbaoDetaileBean.getGnt_category().getDecimals());
                            intent.putExtra("redbagAddress", hongbaoDetaileBean.getRedbag_addr());
                            intent.putExtra("hongbaofei", hongbaoDetaileBean.getRedbag());
                            intent.putExtra("hongbaoCount", hongbaoDetaileBean.getRedbag_number());
                            intent.putExtra("redbgId", hongbaoDetaileBean.getRedbag_id());
                            intent.putExtra("nonce", hongbaoDetaileBean.getRedbag_tx_nonce());
                            intent.putExtra("preGas", hongbaoDetaileBean.getFee());
                            intent.putExtra("preWallet", hongbaoDetaileBean.getFee_addr());
                            keepTogo(intent);
                        }
                    } else if (hongbaoDetaileBean.getStatus() == -3) {
                        Intent intent = new Intent(mActivity, SendHongbao2Activity.class);
                        intent.putExtra("id", hongbaoDetaileBean.getId());
                        intent.putExtra("selectAddress", hongbaoDetaileBean.getRedbag_addr());
                        intent.putExtra("gntGas", hongbaoDetaileBean.getGnt_category().getGas());
                        intent.putExtra("gntName", hongbaoDetaileBean.getGnt_category().getName());
                        intent.putExtra("gntAddress", hongbaoDetaileBean.getGnt_category().getAddress());
                        intent.putExtra("gntDecimal", hongbaoDetaileBean.getGnt_category().getDecimals());
                        intent.putExtra("redbagAddress", hongbaoDetaileBean.getRedbag_addr());
                        intent.putExtra("hongbaofei", hongbaoDetaileBean.getRedbag());
                        intent.putExtra("hongbaoCount", hongbaoDetaileBean.getRedbag_number());
                        keepTogo(intent);
                    } else if (hongbaoDetaileBean.getStatus() == -303) {
                        Intent intent = new Intent(mActivity, SendHongbaoCreateActivity.class);
                        intent.putExtra("id", hongbaoDetaileBean.getId());
                        intent.putExtra("selectAddress", hongbaoDetaileBean.getRedbag_addr());
                        intent.putExtra("gntGas", hongbaoDetaileBean.getGnt_category().getGas());
                        intent.putExtra("gntName", hongbaoDetaileBean.getGnt_category().getName());
                        intent.putExtra("gntAddress", hongbaoDetaileBean.getGnt_category().getAddress());
                        intent.putExtra("gntDecimal", hongbaoDetaileBean.getGnt_category().getDecimals());
                        intent.putExtra("redbagAddress", hongbaoDetaileBean.getRedbag_addr());
                        intent.putExtra("hongbaofei", hongbaoDetaileBean.getRedbag());
                        intent.putExtra("hongbaoCount", hongbaoDetaileBean.getRedbag_number());
                        intent.putExtra("redbgId", hongbaoDetaileBean.getRedbag_id());
                        intent.putExtra("nonce", hongbaoDetaileBean.getRedbag_tx_nonce());
                        intent.putExtra("preGas", hongbaoDetaileBean.getFee());
                        intent.putExtra("preWallet", hongbaoDetaileBean.getFee_addr());
                        keepTogo(intent);
                    }
                } else if (hongbaoDetaileBean.getStatus() == 4 || hongbaoDetaileBean.getStatus() == 1) {//领取中

                    Intent intent = new Intent(mActivity, HongbaoShareWebActivity.class);
                    String username = "";
                    try {
                        username = URLEncoder.encode(hongbaoDetaileBean.getShare_user(), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    String urlch = (App.isMain ? Url.MAIN_HONGBAO : Url.TEST_HONGBAO) + hongbaoDetaileBean.getId() + "/" + hongbaoDetaileBean.getRedbag_addr() + "?share_user=" + username + "&lang=" + (App.get().isZh() ? "zh" : "en") + "&target=draw2&inwe";
                    String urlen = (App.isMain ? Url.MAIN_HONGBAO : Url.TEST_HONGBAO) + hongbaoDetaileBean.getId() + "/" + hongbaoDetaileBean.getRedbag_addr() + "?share_user=" + username + "&lang=" + (App.get().isZh() ? "zh" : "en") + "&target=draw&inwe";

                    intent.putExtra("url", urlch);
                    intent.putExtra("urlen", urlen);
                    intent.putExtra("name", hongbaoDetaileBean.getShare_user());
                    intent.putExtra("content", hongbaoDetaileBean.getShare_msg());
                    intent.putExtra("gntName", hongbaoDetaileBean.getRedbag_symbol());
                    intent.putExtra("showShare", hongbaoDetaileBean.getStatus() == 4 ? true : false);
                    keepTogo(intent);

                }
            }
        });

        adapter = new HongbaoReciveDetaileAdapter(this, R.layout.hongbao_recive_detaile_item, data, hongbaoBean.getRedbag_symbol(), hongbaoBean.getDone() == 2 ? true : false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        list.setNestedScrollingEnabled(false);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });

    }

    private void setBlock() {
        //保存 minBlock
        currentBlock = new BigDecimal(App.get().getSp().getString(Constant.CURRENT_BLOCK, "0")).intValue();

        if (timer == null) {
            timer = new Timer(true);
            task = new TimerTask() {

                @Override
                public void run() {
                    getBlock();
                }
            };
            timer.schedule(task, speed, speed);
        }
    }

    private void getBlock() {
        //获取当前块高
        WalletApi.blockNumber(this, new JsonCallback<LzyResponse<ValueBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<ValueBean>> response) {
                //0x181d02
                //进行计算
                BigDecimal currentPrice = new BigDecimal(AppUtil.toD(response.body().data.getValue().replace("0x", "0")));
                currentBlock = currentPrice.intValue();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (null == state) return;
                        if (null == hongbaoDetaileBean) return;
                        ;
                        //刷新数据
                        if (hongbaoDetaileBean.getStatus() == 2) {
                            if (0 != hongbaoDetaileBean.getAuth_block() && (currentBlock - hongbaoDetaileBean.getAuth_block()) >= 12) {
                                state.setText(R.string.lijindabaochenggong);
                                state1.setText(R.string.chenggong);
                                isAuth = true;
                            } else {
                                state.setText(R.string.lijindabaozhong);
                                state1.setText(R.string.dabaozhong);
                            }
                        }

                        if (hongbaoDetaileBean.getStatus() == 3) {
                            if (0 != hongbaoDetaileBean.getRedbag_block() && (currentBlock - hongbaoDetaileBean.getRedbag_block()) >= 12) {
                                state.setText(R.string.hongbaochuangjianchenggong);
                                state2.setText(R.string.chenggong);
                                isCreate = true;
                            } else {
                                state.setText(R.string.hongbaochuangjianzhong);
                                state2.setText(R.string.dabaozhong);
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void initData() {

        getBlock();

        ZixunApi.getRedbagDetaile(this, hongbaoBean.getId() + "", new JsonCallback<LzyResponse<HongbaoRecordDetaileBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<HongbaoRecordDetaileBean>> response) {

                setHongbaoDetaile(response.body().data);

            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (null == swipeRefresh) return;
                swipeRefresh.setRefreshing(false);
            }
        });

    }

    private void setHongbaoDetaile(HongbaoRecordDetaileBean hongbao) {
        if (null == num) {
            return;
        }
        hongbaoDetaileBean = hongbao;
        Glide.with(this).load(hongbao.getGnt_category().getIcon()).crossFade().into(icon);
        num.setText(new BigDecimal(hongbao.getRedbag()).setScale(4, RoundingMode.DOWN).toPlainString() + hongbao.getRedbag_symbol());
        fee.setText(getString(R.string.shouxufei) + "：" + (hongbao.getFee().equals("") ? "" : (new BigDecimal(hongbao.getFee()).setScale(8, RoundingMode.DOWN).toPlainString()+ "ETH")) );
        address.setText(Html.fromHtml("<u>" + hongbao.getRedbag_addr() + "</u>"));
        lbtxid.setText(Html.fromHtml("<u>" + hongbao.getAuth_tx_id() + "</u>"));
        hbtxid.setText(Html.fromHtml("<u>" + hongbao.getRedbag_tx_id() + "</u>"));
        time.setText(getString(R.string.hongbaochaungjianshijian) + ":" + AppUtil.getGTime(hongbao.getCreated_at()));

        if (hongbao.getStatus() == 2 || hongbao.getStatus() == -2 || hongbao.getStatus() == -202) {//授权
            if (hongbao.getStatus() == 2) {
                if (0 != hongbao.getAuth_block() && (currentBlock - hongbao.getAuth_block()) >= 12) {
                    state.setText(R.string.lijindabaochenggong);
                    state1.setText(R.string.chenggong);
                } else {
                    state.setText(R.string.lijindabaozhong);
                    state1.setText(R.string.dabaozhong);
                }
                if (!isStart) {
                    setBlock();
                }
            } else if (hongbao.getStatus() == -2) {
                state.setText(R.string.lijindabaoshibai);
                state1.setText(R.string.dabaoshibai);
                state1.setTextColor(Color.parseColor("#E24C0A"));
                see.setEnabled(false);
                if (App.get().isZh()) {
                    hit.setText(Html.fromHtml("礼金打包<font color='#E24C0A'>失败</font>,需重新创建钱包.如有疑问请联系我们."));
                } else {
                    hit.setText(Html.fromHtml("Packing <font color='#E24C0A'>Failed</font>,You need to create Red Packet again. If you have any questions, please contact with us."));
                }
            } else if (hongbao.getStatus() == -202) {
                state.setText(R.string.zhunbeilijindabao);
                state1.setText(R.string.zhunbeidabao);
            }
            fee.setText(getString(R.string.shouxufei) + "：");
            address1.setText(hongbao.getRedbag_addr());
            num1.setText("-" + new BigDecimal(hongbao.getRedbag()).setScale(4, RoundingMode.DOWN).toPlainString() + hongbao.getRedbag_symbol());
            time1.setText(AppUtil.getGTime(hongbao.getAuth_at()));
        } else if (hongbao.getStatus() == 3 || hongbao.getStatus() == -3 || hongbao.getStatus() == -303) {//创建红包
            if (hongbao.getStatus() == 3) {
                if (0 != hongbao.getRedbag_block() && (currentBlock - hongbao.getRedbag_block()) >= 12) {
                    state.setText(R.string.hongbaochuangjianchenggong);
                    state2.setText(R.string.chenggong);
                } else {
                    state.setText(R.string.hongbaochuangjianzhong);
                    state2.setText(R.string.dabaozhong);
                }
                if (!isStart) {
                    setBlock();
                }
            } else if (hongbao.getStatus() == -3) {
                state.setText(R.string.hongbaochuangjianshibai);
                state2.setText(R.string.chuangjianshibai);
                state2.setTextColor(Color.parseColor("#E24C0A"));
                if (hongbaoBean.getGlobal_status() != 8) {
                    see.setEnabled(false);
                    if (App.get().isZh()) {
                        hit.setText(Html.fromHtml("红包创建<font color='#E24C0A'>失败</font>,需重新创建钱包.如有疑问请联系我们."));
                    } else {
                        hit.setText(Html.fromHtml("Creation <font color='#E24C0A'>Failed</font>,You need to create Red Packet again. If you have any questions, please contact with us."));
                    }
                }
            } else if (hongbao.getStatus() == -303) {
                state.setText(R.string.zhunbeichuangjianhongbao);
                state2.setText(R.string.zhunbeichuangjian);
            }
            address1.setText(hongbao.getRedbag_addr());
            num1.setText("-" + new BigDecimal(hongbao.getRedbag()).setScale(4, RoundingMode.DOWN).toPlainString() + hongbao.getRedbag_symbol());
            time1.setText(AppUtil.getGTime(hongbao.getAuth_at()));
            state1.setText(R.string.chenggong);
            createll.setVisibility(View.VISIBLE);

            address2.setText(hongbao.getFee_addr());
            num2.setText("-" + new BigDecimal(hongbao.getFee()).setScale(8, RoundingMode.DOWN).toPlainString() + "ETH");
            time2.setText(AppUtil.getGTime(hongbao.getRedbag_at()));

        } else if (hongbao.getStatus() == 4) {//领取中
            state.setText(R.string.lingquzhong);
            address1.setText(hongbao.getRedbag_addr());
            num1.setText("-" + new BigDecimal(hongbao.getRedbag()).setScale(4, RoundingMode.DOWN).toPlainString() + hongbao.getRedbag_symbol());
            time1.setText(AppUtil.getGTime(hongbao.getAuth_at()));
            state1.setText(R.string.chenggong);
            createll.setVisibility(View.VISIBLE);

            address2.setText(hongbao.getFee_addr());
            num2.setText("-" + new BigDecimal(hongbao.getFee()).setScale(8, RoundingMode.DOWN).toPlainString() + "ETH");
            time2.setText(AppUtil.getGTime(hongbao.getRedbag_at()));
            state2.setText(R.string.chenggong);

            listll.setVisibility(View.VISIBLE);

            getNum.setText(getString(R.string.lingqushuliang) + " " + hongbao.getDraw_redbag_number() + "/" + hongbao.getRedbag_number());

            if (null != hongbao.getDraws()) {
                totle1.setText("***" + hongbao.getRedbag_symbol());
                adapter.setIsOpen(false);
                data.clear();
                data.addAll(hongbao.getDraws());
                adapter.notifyDataSetChanged();
            } else {
                totle1.setText("0" + hongbao.getRedbag_symbol());
            }

        } else if (hongbao.getStatus() == 1) {//完成
            state.setText(R.string.wancheng);
            address1.setText(hongbao.getRedbag_addr());
            num1.setText("-" + new BigDecimal(hongbao.getRedbag()).setScale(4, RoundingMode.DOWN).toPlainString() + hongbao.getRedbag_symbol());
            time1.setText(AppUtil.getGTime(hongbao.getAuth_at()));
            state1.setText(R.string.chenggong);
            createll.setVisibility(View.VISIBLE);

            address2.setText(hongbao.getFee_addr());
            num2.setText("-" + new BigDecimal(hongbao.getFee()).setScale(8, RoundingMode.DOWN).toPlainString() + "ETH");
            time2.setText(AppUtil.getGTime(hongbao.getRedbag_at()));
            state2.setText(R.string.chenggong);

            listll.setVisibility(View.VISIBLE);
            getNum.setText(getString(R.string.lingqushuliang) + " " + hongbao.getDraw_redbag_number() + "/" + hongbao.getRedbag_number());

            if (null != hongbao.getDraws()) {
                adapter.setIsOpen(true);
                data.clear();
                data.addAll(hongbao.getDraws());
                adapter.setDecimal(hongbao.getGnt_category().getDecimals() + "");
                adapter.notifyDataSetChanged();

                BigDecimal totlePrice = new BigDecimal(0);
                for (HongbaoRecordDetaileBean.DrawsBean drawsBean : data) {
                    if (null != drawsBean.getValue()) {
                        totlePrice = totlePrice.add(new BigDecimal(AppUtil.toD(drawsBean.getValue().replace("0x", "0"))));
                    }
                }
                totle1.setText(totlePrice.divide(AppUtil.decimal(hongbao.getGnt_category().getDecimals() + ""), 4, RoundingMode.DOWN).toPlainString() + hongbao.getRedbag_symbol());
            } else {
                totle1.setText("0" + hongbao.getRedbag_symbol());
            }

            if (!hongbao.getRedbag_back().equals("")) {
                hit.setVisibility(View.VISIBLE);
                backll.setVisibility(View.VISIBLE);
                backNum.setText(getString(R.string.chehuishuliang) + " " + (hongbao.getRedbag_number() - hongbao.getDraw_redbag_number()));
                totle2.setText(new BigDecimal(AppUtil.toD(hongbao.getRedbag_back().replace("0x", "0"))).divide(AppUtil.decimal(hongbao.getGnt_category().getDecimals() + ""), 4, RoundingMode.DOWN).toPlainString() + hongbao.getRedbag_symbol());
                address3.setText(hongbao.getRedbag_addr());
                num3.setText("+" + new BigDecimal(AppUtil.toD(hongbao.getRedbag_back().replace("0x", "0"))).divide(AppUtil.decimal(hongbao.getGnt_category().getDecimals() + ""), 4, RoundingMode.DOWN).toPlainString() + hongbao.getRedbag_symbol());
                time3.setText(null == hongbao.getRedbag_back_at() ? "" : AppUtil.getGTime(hongbao.getRedbag_back_at()));
                if (hongbao.getRedbag_back_tx_status() == 0) {
                    state3.setText(R.string.chehuizhong);
                } else if (hongbao.getRedbag_back_tx_status() == 1) {
                    state3.setText(R.string.chenggong);
                } else {
                    state3.setText(R.string.shibai);
                }
            }
        }
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_HONGBAO_REFERS) {
            initData();
        }
    }

}
