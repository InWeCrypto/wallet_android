package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonListBean;
import com.inwecrypto.wallet.bean.CommonPageBean;
import com.inwecrypto.wallet.bean.HongbaoNumBean;
import com.inwecrypto.wallet.bean.SendHongbaoBean;
import com.inwecrypto.wallet.bean.ValueBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.Url;
import com.inwecrypto.wallet.common.http.api.WalletApi;
import com.inwecrypto.wallet.common.http.api.ZixunApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ScreenUtils;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.MaterialDialog;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.me.activity.CommonWebActivity;
import com.inwecrypto.wallet.ui.news.adapter.HongbaoAdapter;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/4/23 10:54
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class HongbaoActivity extends BaseActivity {

    @BindView(R.id.header_img)
    ImageView headerImg;
    @BindView(R.id.send_red)
    View sendRed;
    @BindView(R.id.totle)
    TextView totle;
    @BindView(R.id.create)
    TextView create;
    @BindView(R.id.send)
    TextView send;
    @BindView(R.id.chenggong)
    TextView chenggong;
    @BindView(R.id.faile)
    TextView faile;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.more)
    TextView more;
    @BindView(R.id.swipeRefersh)
    SwipeRefreshLayout swipeRefersh;
    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.get_record)
    TextView getRecord;
    @BindView(R.id.titlefl)
    FrameLayout titlefl;
    @BindView(R.id.sv)
    NestedScrollView sv;

    private EmptyWrapper emptyWrapper;
    private HongbaoAdapter adapter;
    private ArrayList<SendHongbaoBean> data = new ArrayList<>();

    private ArrayList<WalletBean> wallet = new ArrayList<>();

    public static int speed = 10000;
    private Timer timer;
    private TimerTask task;
    public int currentBlock;
    private MaterialDialog mMaterialDialog;

    private int titleHight;
    private int bgHight;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int setLayoutID() {
        return R.layout.hongbao_activity_layout;
    }

    @Override
    protected void initView() {

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (!App.get().isZh()){
            headerImg.setImageResource(R.mipmap.red_bg_en);
        }

        sendRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wallet.size() == 0) {
                    ToastUtil.show(getString(R.string.ninhaimeiyouethqianbao));
                } else {
                    if (App.get().getSp().getBoolean(Constant.IS_SEND_HONGBAO, true)) {
                        showDialog();
                        return;
                    }
                    //请求钱包接口
                    Intent intent = new Intent(mActivity, SendHongbao1Activity.class);
                    intent.putExtra("wallet", wallet);
                    keepTogo(intent);
                }
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, HongbaoRecordActivity.class);
                intent.putExtra("wallet", wallet);
                keepTogo(intent);
            }
        });

        getRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, HongbaoGetRecordActivity.class);
                intent.putExtra("wallet", wallet);
                keepTogo(intent);
            }
        });


        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) headerImg.getLayoutParams();
        params.height = (int) (ScreenUtils.getScreenWidth(this) / 2250.0f * 1338.0f);
        bgHight=params.height;
        headerImg.setLayoutParams(params);

        titlefl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                titlefl.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                titleHight=titlefl.getMeasuredHeight();
            }
        });

        sv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int alpha=0;
                if (scrollY<100){
                    alpha=0;
                }else {
                    alpha= (int) ((scrollY-100)*1.0f/(bgHight-titleHight)*1.0f*255);
                    if (alpha>=255){
                        alpha=255;
                    }
                }
                if (alpha<0){alpha=0;}
                String al=new BigInteger(alpha+"",10).toString(16);
                String color="#"+(al.length()==1?("0"+al):al)+"B23E2E";
                Log.e("bbb",color);
                // 只是layout背景透明(仿知乎滑动效果)
                titlefl.setBackgroundColor(Color.parseColor(color));
            }
        });

        adapter = new HongbaoAdapter(this, R.layout.hongbao_record_adapter, data);
        emptyWrapper = new EmptyWrapper(adapter);
        emptyWrapper.setEmptyView(R.layout.empty_list_layout);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(emptyWrapper);
        list.setNestedScrollingEnabled(false);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mActivity, HongbaoRecordDetailActivity.class);
                intent.putExtra("redbag", data.get(position));
                keepTogo(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        swipeRefersh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefersh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        swipeRefersh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefersh.setRefreshing(true);
            }
        });

        setBlock();

    }

    private void showDialog() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_dialog_hongbaotip, null, false);
        View ok = view.findViewById(R.id.ok);
        View cancle = view.findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.get().getSp().putBoolean(Constant.IS_SEND_HONGBAO, false);
                Intent intent = new Intent(mActivity, CommonWebActivity.class);
                intent.putExtra("title", getString(R.string.inwehongbaoshiyongxieyi));
                intent.putExtra("url", App.get().isZh() ? Url.MAIN_HONGBAOXIEYI : Url.MAIN_HONGBAOXIEYI_EN);
                startActivityForResult(intent,0);
                mMaterialDialog.dismiss();
            }
        });
        mMaterialDialog = new MaterialDialog(mActivity).setView(view);
        mMaterialDialog.setBackgroundResource(R.drawable.trans_bg);
        mMaterialDialog.setCanceledOnTouchOutside(true);
        mMaterialDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0){
            //请求钱包接口
            Intent intent = new Intent(mActivity, SendHongbao1Activity.class);
            intent.putExtra("wallet", wallet);
            keepTogo(intent);
        }
    }

    private void setBlock() {
        //保存 minBlock
        currentBlock = new BigDecimal(App.get().getSp().getString(Constant.CURRENT_BLOCK, "0")).intValue();

        adapter.setCurrentBlock(currentBlock);

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
                        //刷新数据
                        adapter.setCurrentBlock(currentBlock);
                        emptyWrapper.notifyDataSetChanged();
                    }
                });
            }
        });
    }


    @Override
    protected void initData() {
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
                        if (response.body().data.getList().get(i).getCategory_id() == 1) {
                            wallet.add(response.body().data.getList().get(i));
                        }
                    }
                }

                //获取记录
                getRecord();
                //获取个数
                getRecordNum();
            }

            @Override
            public void onError(Response<LzyResponse<CommonListBean<WalletBean>>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.jiazaishibai));
            }
        });

    }

    private void getRecordNum() {

        ZixunApi.getSendRecordNum(this, wallet, new JsonCallback<LzyResponse<HongbaoNumBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<HongbaoNumBean>> response) {
                if (App.get().isZh()){
                    totle.setText(Html.fromHtml("一共发送了" + "<font  color=\"#C63437\">" + response.body().data.getAll() + "</font>"+ "个红包"));
                }else {
                    totle.setText(Html.fromHtml("Total Sent "+ "<font  color=\"#C63437\">" +response.body().data.getAll() + "</font>" + " Red Packet"));
                }
                create.setText(response.body().data.getCreate() +" "+ getString(R.string._ge));
                send.setText(response.body().data.getSend()+" "+ getString(R.string._ge));
                chenggong.setText(response.body().data.getSuccess() +" "+ getString(R.string._ge));
                faile.setText(response.body().data.getFail() +" "+ getString(R.string._ge));
            }
        });

    }

    private void getRecord() {
        ZixunApi.getSendRecord(this, wallet, 1, new JsonCallback<LzyResponse<CommonPageBean<SendHongbaoBean>>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CommonPageBean<SendHongbaoBean>>> response) {
                data.clear();

                if (null != response.body().data.getData()) {
                    if (response.body().data.getData().size() > 5) {
                        more.setVisibility(View.VISIBLE);
                        data.add(response.body().data.getData().get(0));
                        data.add(response.body().data.getData().get(1));
                        data.add(response.body().data.getData().get(2));
                        data.add(response.body().data.getData().get(3));
                        data.add(response.body().data.getData().get(4));
                    } else {
                        data.addAll(response.body().data.getData());
                    }
                } else {
                    more.setVisibility(View.GONE);
                }
                adapter.setCurrentBlock(currentBlock);
                emptyWrapper.notifyDataSetChanged();

            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (null == swipeRefersh) return;
                swipeRefersh.setRefreshing(false);
            }
        });
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
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode() == Constant.EVENT_HONGBAO_REFERS) {
            initData();
        }
    }
}
