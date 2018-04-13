package com.inwecrypto.wallet.ui.me.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.PingjiaBean;
import com.inwecrypto.wallet.bean.PingjiaInfoBean;
import com.inwecrypto.wallet.bean.PingjiaReplyBean;
import com.inwecrypto.wallet.bean.ProjectListBean;
import com.inwecrypto.wallet.bean.ReplyBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.api.MeApi;
import com.inwecrypto.wallet.common.http.callback.JsonCallback;
import com.inwecrypto.wallet.common.imageloader.GlideCircleTransform;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.EndLessOnScrollListener;
import com.inwecrypto.wallet.common.widget.RatingBar;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.event.PingjiaEvent;
import com.inwecrypto.wallet.ui.login.LoginActivity;
import com.inwecrypto.wallet.ui.me.adapter.PingjiaXiangqingAdapter;
import com.lzy.okgo.model.Response;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：xiaoji06 on 2018/4/4 16:07
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class PingjiaXiangqingActivity extends BaseActivity {
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.pingjia_et)
    EditText pingjiaEt;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.swipeRefersh)
    SwipeRefreshLayout swipeRefersh;

    private HeaderAndFooterWrapper header;
    private View head;

    private PingjiaXiangqingAdapter adapter;
    private ArrayList<PingjiaReplyBean.DataBean> data=new ArrayList<>();

    private boolean isFromMe;
    private PingjiaBean pingjia;
    private ImageView img;
    private TextView name;
    private TextView cantouguo;
    private TextView time;
    private RatingBar ratingbar;
    private TextView fenshu;
    private TextView leve;
    private TextView content;
    private View zan;
    private ImageView zan_img;
    private TextView zan_num;
    private View down;
    private ImageView down_img;
    private TextView down_num;
    private View intr;
    private ImageView intr_img;
    private TextView intr_num;
    private TextView pinglun_num;
    private TextView all_pinglun_num;
    private PingjiaInfoBean pingjiaInfo;

    private int page=1;
    private boolean isEnd;
    private boolean isShow;
    private boolean isInit;
    private LinearLayoutManager layoutManager;
    private EndLessOnScrollListener scrollListener;

    private int position;

    @Override
    protected void getBundleExtras(Bundle extras) {
        isFromMe=extras.getBoolean("isFromMe",false);
        pingjia= (PingjiaBean) extras.getSerializable("pingjia");
        position=extras.getInt("position");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.pingjiaxiangqing_activity_layout;
    }

    @Override
    protected void initView() {

        txtMainTitle.setText(R.string.pinglunxiangqing);
        txtRightTitle.setVisibility(View.GONE);
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter=new PingjiaXiangqingAdapter(this,R.layout.pingjia_xiangqing_item,data);
        header=new HeaderAndFooterWrapper(adapter);
        head=LayoutInflater.from(this).inflate(R.layout.pingjia_xiangqing_header_view,null,false);

        head.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        img=(ImageView)head.findViewById(R.id.img);
        name=(TextView)head.findViewById(R.id.name);
        cantouguo=(TextView)head.findViewById(R.id.cantouguo);
        time=(TextView)head.findViewById(R.id.time);
        ratingbar=(RatingBar)head.findViewById(R.id.ratingbar);
        fenshu=(TextView)head.findViewById(R.id.fenshu);
        leve=(TextView)head.findViewById(R.id.leve);
        content=(TextView)head.findViewById(R.id.content);
        zan=head.findViewById(R.id.zan);
        zan_img=(ImageView)head.findViewById(R.id.zan_img);
        zan_num=(TextView)head.findViewById(R.id.zan_num);
        down=head.findViewById(R.id.down);
        down_img=(ImageView)head.findViewById(R.id.down_img);
        down_num=(TextView)head.findViewById(R.id.down_num);
        intr=head.findViewById(R.id.intr);
        intr_img=(ImageView)head.findViewById(R.id.intr_img);
        intr_num=(TextView)head.findViewById(R.id.intr_num);
        pinglun_num=(TextView)head.findViewById(R.id.pinglun_num);
        all_pinglun_num=(TextView)head.findViewById(R.id.all_pinglun_num);

        header.addHeaderView(head);
        layoutManager=new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        scrollListener=new EndLessOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                if (!isInit){
                    return;
                }
                if (isEnd){
                    if (!isShow&&page!=1){
                        ToastUtil.show(getString(R.string.zanwugengduoshuju));
                        isShow=true;
                    }
                }else {
                    page++;
                    initData();
                }
            }
        };
        list.addOnScrollListener(scrollListener);
        list.setAdapter(header);

        swipeRefersh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefersh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                isEnd=false;
                isShow=false;
                isInit=false;
                scrollListener.reset();
                //从网络获取钱包
                initData();
            }
        });

        Glide.with(this)
                .load(pingjia.getUser()
                .getImg())
                .crossFade()
                .error(R.mipmap.wode_touxiang)
                .transform(new GlideCircleTransform(this)).into(img);
        name.setText(pingjia.getUser().getName());
        time.setText(AppUtil.getGTime(pingjia.getCategory_comment_at()));
        ratingbar.setStar(Float.parseFloat(pingjia.getScore()));
        fenshu.setText(pingjia.getScore()+getString(R.string.fenshu));

        String leveStr="";
        if (Float.parseFloat(pingjia.getScore())<=1){
            leveStr=getString(R.string.henbumanyi);
        }else if (Float.parseFloat(pingjia.getScore())<=2){
            leveStr=getString(R.string.bumanyi);
        }else if (Float.parseFloat(pingjia.getScore())<=3){
            leveStr=getString(R.string.yiban);
        }else if (Float.parseFloat(pingjia.getScore())<=4){
            leveStr=getString(R.string.tuijian);
        }else if (Float.parseFloat(pingjia.getScore())<=5){
            leveStr=getString(R.string.feichangtuijian);
        }

        leve.setText(leveStr);
        if (null==pingjia.getCategory_comment()||"".equals(pingjia.getCategory_comment())){
            content.setText(R.string.morenpingjia);
        }else {
            content.setText(pingjia.getCategory_comment());
        }

        if (pingjia.getIs_category_comment()==1){
            if (pingjia.getCategory_comment_tag_name().length()==0){
                cantouguo.setVisibility(View.GONE);
            }else {
                cantouguo.setVisibility(View.VISIBLE);
                cantouguo.setText(pingjia.getCategory_comment_tag_name());
            }
        }else {
            cantouguo.setVisibility(View.GONE);
        }

        zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()) {
                    keepTogo(LoginActivity.class);
                    return;
                }
                if (null==pingjiaInfo){
                    return;
                }
                MeApi.postReply(this, pingjia.getCategory().getId() + "", pingjia.getId() + "", "up", new JsonCallback<LzyResponse<ReplyBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<ReplyBean>> response) {
                        ReplyBean replyBean = response.body().data;

                        down_img.setImageResource(R.mipmap.bad_icon);
                        intr_img.setImageResource(R.mipmap.smile_icon);
                        zan_img.setImageResource(R.mipmap.good_icon);
                        if (replyBean.getUp()==1){
                            zan_img.setImageResource(R.mipmap.good_select_icon);
                            zan_num.setText((Integer.parseInt(zan_num.getText().toString())+1)+"");
                        }else {
                            zan_num.setText((Integer.parseInt(zan_num.getText().toString())-1)+"");
                        }

                        //获取项目评价详情
                        getProjectDetaile();

                        if (!isFromMe){
                            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PINGJIA
                                    ,new PingjiaEvent(position
                                    ,replyBean.getUp()
                                    ,replyBean.getDown()
                                    ,replyBean.getEqual()
                                    ,Integer.parseInt(pinglun_num.getText().toString()))));
                        }

                    }

                    @Override
                    public void onError(Response<LzyResponse<ReplyBean>> response) {
                        super.onError(response);
                        ToastUtil.show(R.string.caozuoshibai);
                    }
                });
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()) {
                    keepTogo(LoginActivity.class);
                    return;
                }
                if (null==pingjiaInfo){
                    return;
                }
                MeApi.postReply(this, pingjia.getCategory().getId() + "", pingjia.getId() + "", "down", new JsonCallback<LzyResponse<ReplyBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<ReplyBean>> response) {
                        ReplyBean replyBean=response.body().data;

                        down_img.setImageResource(R.mipmap.bad_icon);
                        intr_img.setImageResource(R.mipmap.smile_icon);
                        zan_img.setImageResource(R.mipmap.good_icon);
                        if (replyBean.getDown()==1){
                            down_img.setImageResource(R.mipmap.bad_select_icon);
                            down_num.setText((Integer.parseInt(down_num.getText().toString())+1)+"");
                        }else {
                            down_num.setText((Integer.parseInt(down_num.getText().toString())-1)+"");
                        }

                        //获取项目评价详情
                        getProjectDetaile();

                        if (!isFromMe){
                            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PINGJIA
                                    ,new PingjiaEvent(position
                                    ,replyBean.getUp()
                                    ,replyBean.getDown()
                                    ,replyBean.getEqual()
                                    ,Integer.parseInt(pinglun_num.getText().toString()))));
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<ReplyBean>> response) {
                        super.onError(response);
                        ToastUtil.show(R.string.caozuoshibai);
                    }
                });
            }
        });

        intr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.get().isLogin()) {
                    keepTogo(LoginActivity.class);
                    return;
                }
                if (null==pingjiaInfo){
                    return;
                }
                MeApi.postReply(this, pingjia.getCategory().getId() + "", pingjia.getId() + "", "equal", new JsonCallback<LzyResponse<ReplyBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<ReplyBean>> response) {
                        ReplyBean replyBean=response.body().data;

                        down_img.setImageResource(R.mipmap.bad_icon);
                        intr_img.setImageResource(R.mipmap.smile_icon);
                        zan_img.setImageResource(R.mipmap.good_icon);
                        if (replyBean.getEqual()==1){
                            intr_img.setImageResource(R.mipmap.smile_select_icon);
                            intr_num.setText((Integer.parseInt(intr_num.getText().toString())+1)+"");
                        }else {
                            intr_num.setText((Integer.parseInt(intr_num.getText().toString())-1)+"");
                        }

                        //获取项目评价详情
                        getProjectDetaile();

                        if (!isFromMe){
                            EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PINGJIA
                                    ,new PingjiaEvent(position
                                    ,replyBean.getUp()
                                    ,replyBean.getDown()
                                    ,replyBean.getEqual()
                                    ,Integer.parseInt(pinglun_num.getText().toString()))));
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<ReplyBean>> response) {
                        super.onError(response);
                        ToastUtil.show(R.string.caozuoshibai);
                    }
                });
            }
        });

        pingjiaEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {

                    if (!App.get().isLogin()) {
                        keepTogo(LoginActivity.class);
                        return false;
                    }

                    if (pingjiaEt.getText().toString().trim().length()==0){
                        ToastUtil.show(getString(R.string.neirongbunengweikong));
                        return false;
                    }

                    showFixLoading();
                    //处理事件
                    MeApi.postCommentReply(this,pingjia.getCategory().getId() + "", pingjia.getId() + "" ,pingjiaEt.getText().toString(), new JsonCallback<LzyResponse<Object>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<Object>> response) {
                            initData();
                            pingjiaEt.setText("");
                            if (!isFromMe){
                                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_PINGJIA
                                        ,new PingjiaEvent(position
                                        ,null==pingjiaInfo.getUser_click_comment()?0:(pingjiaInfo.getUser_click_comment().size()==0?0:pingjiaInfo.getUser_click_comment().get(0).getUp())
                                        ,null==pingjiaInfo.getUser_click_comment()?0:(pingjiaInfo.getUser_click_comment().size()==0?0:pingjiaInfo.getUser_click_comment().get(0).getDown())
                                        ,null==pingjiaInfo.getUser_click_comment()?0:(pingjiaInfo.getUser_click_comment().size()==0?0:pingjiaInfo.getUser_click_comment().get(0).getEqual())
                                        ,Integer.parseInt(pinglun_num.getText().toString())+1)));
                            }
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            hideFixLoading();
                        }
                    });
                }
                return false;
            }
        });

    }

    @Override
    protected void initData() {
        //获取项目评价详情
        getProjectDetaile();
        //获取评价列表
        getList();
    }

    private void getList() {

        MeApi.getCommentReply(this, pingjia.getCategory().getId() + "", pingjia.getId() + "", new JsonCallback<LzyResponse<PingjiaReplyBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<PingjiaReplyBean>> response) {
                PingjiaReplyBean replyBean=response.body().data;
                pinglun_num.setText(replyBean.getTotal()+"");
                all_pinglun_num.setText(getString(R.string.quanbupinglun)+"("+replyBean.getTotal()+")");

                if (page==1){
                    data.clear();
                }

                if (null != response.body().data.getData()) {
                    data.addAll(response.body().data.getData());
                }

                if (page>=response.body().data.getCurrent_page()){
                    //最后一页
                    isEnd=true;
                }
                header.notifyDataSetChanged();

                if (page==1){
                    isInit=true;
                }
            }

            @Override
            public void onError(Response<LzyResponse<PingjiaReplyBean>> response) {
                super.onError(response);
                if (page!=1){
                    page--;
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if(null!=swipeRefersh){
                    swipeRefersh.setRefreshing(false);
                }
            }
        });

    }

    private void getProjectDetaile() {
        MeApi.getCommentInfo(this,pingjia.getCategory().getId()+"", pingjia.getId()+"" , new JsonCallback<LzyResponse<PingjiaInfoBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<PingjiaInfoBean>> response) {
                pingjiaInfo=response.body().data;
                zan_num.setText(pingjiaInfo.getUser_click_comment_up_count()+"");
                down_num.setText(pingjiaInfo.getUser_click_comment_down_count()+"");
                intr_num.setText(pingjiaInfo.getUser_click_comment_equal_count()+"");

                if (pingjiaInfo.getUser_click_comment().size()!=0){
                    PingjiaInfoBean.UserClickCommentBean userClick=pingjiaInfo.getUser_click_comment().get(0);
                    if (userClick.getUp()==1){
                        zan_img.setImageResource(R.mipmap.good_select_icon);
                    }else if (userClick.getDown()==1){
                        down_img.setImageResource(R.mipmap.bad_select_icon);
                    }else if (userClick.getEqual()==1){
                        intr_img.setImageResource(R.mipmap.smile_select_icon);
                    }
                }else {
                    zan_img.setImageResource(R.mipmap.good_icon);
                    down_img.setImageResource(R.mipmap.bad_icon);
                    intr_img.setImageResource(R.mipmap.smile_icon);
                }
            }
        });
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
