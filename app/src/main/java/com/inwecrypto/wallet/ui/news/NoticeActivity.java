package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.adapter.message.EMATextMessageBody;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.CommonProjectBean;
import com.inwecrypto.wallet.bean.NoticeBean;
import com.inwecrypto.wallet.bean.TradingNoticeBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.adapter.NoticeAdapter;
import com.inwecrypto.wallet.ui.news.adapter.TradingNoticeAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/2/8 20:01
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class NoticeActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    ImageView txtRightTitle;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayoutCompat swipeRefresh;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.views)
    TextView views;

    private int page=1;
    private boolean isEnd;

    private CommonProjectBean marks;

    private NoticeAdapter adapter;
    private ArrayList<NoticeBean> data=new ArrayList<>();

    private boolean isFirst;

    private String lastId;

    private SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void getBundleExtras(Bundle extras) {
        marks= (CommonProjectBean) extras.getSerializable("marks");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.inweproject_activity;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.tongzhi);
        txtRightTitle.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,ProjectDetaileActivity.class);
                intent.putExtra("marks",marks);
                keepTogo(intent);
            }
        });

        adapter=new NoticeAdapter(this,R.layout.jiaoyigonggao_item,data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        list.setLayoutManager(linearLayoutManager);
        list.setAdapter(adapter);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isFirst){
                    page++;
                }else {
                    page=1;
                }
                isEnd=false;
                //加载更多
                loadMore();

            }
        });

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mActivity, NoticeDetaileActivity.class);
                intent.putExtra("notice",data.get(position));
                keepTogo(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void loadMore() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation("SYS_MSG");
        if (null==conversation){
            isEnd=true;
            swipeRefresh.setEnabled(false);
            return;
        }
        //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
        //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
        List<EMMessage> messages = conversation.loadMoreMsgFromDB(lastId, 20);
        if (null==messages){
            isEnd=true;
            swipeRefresh.setEnabled(false);
            return;
        }
        lastId=messages.get(messages.size()-1).getMsgId();
        if (messages.size()<20){
            isEnd=true;
            swipeRefresh.setEnabled(false);
        }
        ArrayList<NoticeBean> totle=new ArrayList<>();
        for (EMMessage message:messages){
            NoticeBean tradingNotice=new NoticeBean();
            tradingNotice.setTitle((String) message.ext().get("title"));
            tradingNotice.setTime(""+sdr.format(new Date(message.getMsgTime())));
            EMTextMessageBody body= (EMTextMessageBody) message.getBody();
            String meg=body.getMessage().replace(":date",tradingNotice.getTime());
            tradingNotice.setContent(meg);
            totle.add(tradingNotice);
        }
        data.addAll(0,totle);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation("SYS_MSG");
        if (null==conversation){
            isEnd=true;
            swipeRefresh.setEnabled(false);
            return;
        }
        //获取此会话的所有消息
        List<EMMessage> messages = conversation.getAllMessages();
        //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
        if (null==messages){
            isEnd=true;
            swipeRefresh.setEnabled(false);
            return;
        }
        data.clear();
        if (messages.size()<20){
            isEnd=true;
            swipeRefresh.setEnabled(false);
        }
        lastId=messages.get(messages.size()-1).getMsgId();
        ArrayList<NoticeBean> totle=new ArrayList<>();
        for (EMMessage message:messages){
            NoticeBean tradingNotice=new NoticeBean();
            tradingNotice.setTitle((String) message.ext().get("title"));
            tradingNotice.setTime(""+sdr.format(new Date(message.getMsgTime())));
            EMTextMessageBody body= (EMTextMessageBody) message.getBody();
            String meg=body.getMessage().replace(":date",tradingNotice.getTime());
            tradingNotice.setContent(meg);
            totle.add(tradingNotice);
        }
        data.addAll(totle);
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()== Constant.EVENT_NOTIFY){
            marks.setOpenTip((Boolean) event.getData());
        }
    }

}
