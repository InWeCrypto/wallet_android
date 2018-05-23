package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.BpsBean;
import com.inwecrypto.wallet.bean.HongbaoGntBean;
import com.inwecrypto.wallet.bean.HongbaoRecordDetaileBean;
import com.inwecrypto.wallet.bean.MinBlockBean;
import com.inwecrypto.wallet.bean.ValueBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.lzy.okgo.model.Response;

import net.qiujuer.genius.ui.widget.Button;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/4/23 12:29
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class SendHongbaoPendingActivity extends BaseActivity {

    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.state)
    TextView state;
    @BindView(R.id.progess)
    ProgressBar progess;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.addGas)
    TextView addGas;

    private String id;
    private String selectAddress;
    private String gntGas;
    private String gntName;
    private String gntAddress;
    private String gntDecimal;
    private String redbagAddress;
    private String hongbaofei;
    private int hongbaoCount;


    private int minBlock;
    public int currentBlock;
    public int speed = 10000;
    private Timer timer;
    private TimerTask task;

    private boolean isOpen;
    private HongbaoRecordDetaileBean hongbaoDetaileBean;
    private HongbaoGntBean gnt;
    private String preGas;

    @Override
    protected void getBundleExtras(Bundle extras) {
        id = extras.getString("id");
        selectAddress = extras.getString("selectAddress");
        gntGas = extras.getString("gntGas");
        gntName = extras.getString("gntName");
        gntAddress = extras.getString("gntAddress");
        gntDecimal = extras.getString("gntDecimal");
        redbagAddress = extras.getString("redbagAddress");
        hongbaofei = extras.getString("hongbaofei");
        hongbaoCount = extras.getInt("hongbaoCount");
        gnt = (HongbaoGntBean) extras.getSerializable("gnt");
        preGas = extras.getString("preGas");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.send_hongbao_pending_activity;
    }

    @Override
    protected void initView() {

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_HONGBAO_REFERS));
                finish();
            }
        });

        back.setText(Html.fromHtml(getString(R.string.fanhuihongbaoshouye)));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_HONGBAO_REFERS));
                finish();
            }
        });

        addGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SendHongbao1Activity.class);
                intent.putExtra("isUpGas", true);
                intent.putExtra("id",id);
                intent.putExtra("selectAddress", selectAddress);
                intent.putExtra("hongbaofei", hongbaofei);
                intent.putExtra("hongbaoCount", hongbaoCount);
                intent.putExtra("gnt",gnt);
                intent.putExtra("preGas",preGas);
                finshTogo(intent);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SendHongbao2Activity.class);
                intent.putExtra("id", id);
                intent.putExtra("selectAddress", selectAddress);
                intent.putExtra("gntGas", gntGas);
                intent.putExtra("gntName", gntName);
                intent.putExtra("gntAddress", gntAddress);
                intent.putExtra("gntDecimal", gntDecimal);
                intent.putExtra("redbagAddress", redbagAddress);
                intent.putExtra("hongbaofei", hongbaofei);
                intent.putExtra("hongbaoCount", hongbaoCount);
                finshTogo(intent);
            }
        });

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getState();
                getBlock();
            }
        });

        //保存 minBlock
        minBlock = 6;
        //保存 minBlock
        currentBlock = new BigDecimal(App.get().getSp().getString(Constant.CURRENT_BLOCK, "0")).intValue();

        if (App.get().isLogin()) {
            startRound();
        }

    }

    private void startRound() {
        //获取最小确认块数
        WalletApi.mimBlock(mActivity, new JsonCallback<LzyResponse<MinBlockBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<MinBlockBean>> response) {
                //minBlock = Integer.parseInt(response.body().data.getMin_block_num());
                //保存 minBlock
                App.get().getSp().putInt(Constant.MIN_BLOCK, minBlock);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                //获取当前块高
                WalletApi.blockNumber(mActivity, new JsonCallback<LzyResponse<ValueBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<ValueBean>> response) {
                        //0x181d02
                        //进行计算
                        BigDecimal currentPrice = new BigDecimal(AppUtil.toD(response.body().data.getValue().replace("0x", "0")));
                        currentBlock = currentPrice.intValue();
                        //保存 minBlock
                        App.get().getSp().putString(Constant.CURRENT_BLOCK, currentPrice.toPlainString());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        //获取轮询速度
                        WalletApi.blockPerSecond(mActivity, new JsonCallback<LzyResponse<BpsBean>>() {
                            @Override
                            public void onSuccess(Response<LzyResponse<BpsBean>> response) {
                                //开启轮询
                                speed = (int) (1.0 / response.body().data.getBps());
                                if (speed < 5000) {
                                    speed = 10000;
                                }
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                if (timer == null) {
                                    timer = new Timer(true);
                                    task = new TimerTask() {

                                        @Override
                                        public void run() {
                                            if (isOpen) {
                                                getBlock();
                                            } else {
                                                getState();
                                            }
                                        }
                                    };
                                    timer.schedule(task, speed, speed);
                                }
                            }
                        });
                    }
                });
            }

        });
    }

    private void getState() {
        ZixunApi.getRedbagDetaile(this, id, new JsonCallback<LzyResponse<HongbaoRecordDetaileBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<HongbaoRecordDetaileBean>> response) {
                hongbaoDetaileBean = response.body().data;

                if (hongbaoDetaileBean.getStatus() == -2) {
                    state.setText("(" + getString(R.string.lijindabaoshibai) + ")");
                } else if (hongbaoDetaileBean.getStatus() == -202) {
                    state.setText("(" + getString(R.string.zhunbeilijindabao) + ")");
                } else if (hongbaoDetaileBean.getStatus() == 2) {
                    //开启轮询
                    if (!isOpen) {
                        isOpen = true;
                    }
                    changeState();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (null == swipeRefresh) return;
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void changeState() {
        if (null == hongbaoDetaileBean) return;
        if (hongbaoDetaileBean.getStatus() == -2) {
            addGas.setVisibility(View.INVISIBLE);
            return;
        }
        if (0 != hongbaoDetaileBean.getAuth_block() && (currentBlock - hongbaoDetaileBean.getAuth_block()) >= minBlock) {
            addGas.setVisibility(View.INVISIBLE);
            state.setText("(" + getString(R.string.lijindabaochenggong) + ")");
            progess.setProgress(100);
            next.setEnabled(true);
        } else if (0 != hongbaoDetaileBean.getAuth_block()) {
            addGas.setVisibility(View.INVISIBLE);
            int precent = currentBlock - hongbaoDetaileBean.getAuth_block();
            if (precent < 0) {
                precent = 0;
            }
            progess.setProgress((int) (precent * 1.0f / minBlock * 100.f));
            state.setText("(" + getString(R.string.yijingqueren) + " " + precent + "/" + minBlock + ")");
            if (precent >= minBlock) {
                progess.setProgress(100);
                state.setText("(" + getString(R.string.lijindabaochenggong) + ")");
                next.setEnabled(true);
            }
        } else {
            addGas.setVisibility(View.VISIBLE);
            state.setText("(" + getString(R.string.zhunbeilijindabao) + ")");
        }
    }


    @Override
    protected void initData() {
        getBlock();
        getState();
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
                        changeState();
                    }
                });
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (null != swipeRefresh) {
                    swipeRefresh.setRefreshing(false);
                }
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

    @Override
    protected void onDestroy() {
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
        if (null != task) {
            task.cancel();
            task = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_HONGBAO_REFERS));
    }
}
