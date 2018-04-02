package com.inwecrypto.wallet.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.NoticeBean;
import com.inwecrypto.wallet.bean.TradingNoticeBean;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.EndLessOnScrollListener;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.common.widget.SwipeRefreshLayoutCompat;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.news.adapter.NoticeAdapter;
import com.inwecrypto.wallet.ui.news.adapter.NoticeHistoryAdapter;
import com.inwecrypto.wallet.ui.news.adapter.TradingNoticeHistoryAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：xiaoji06 on 2018/2/10 11:29
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class NoticeHistoryActivity extends BaseActivity {

    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.toolbar)
    SimpleToolbar toolbar;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayoutCompat swipeRefresh;

    private boolean isEnd;
    private boolean isShow;
    private String lastId;
    private boolean isFirst;

    private LinearLayoutManager layoutManager;

    private NoticeHistoryAdapter adapter;
    private ArrayList<NoticeBean> data=new ArrayList<>();

    private SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private EndLessOnScrollListener scrollListener;

    @Override
    protected void getBundleExtras(Bundle extras) {
    }

    @Override
    protected int setLayoutID() {
        return R.layout.inwehot_history_activity;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtRightTitle.setVisibility(View.GONE);

        txtMainTitle.setText(R.string.lishizixun);

        adapter = new NoticeHistoryAdapter(this, R.layout.notice_item, data);
        layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
        scrollListener=new EndLessOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                if (isEnd) {
                    if (!isShow&&!isFirst) {
                        ToastUtil.show(getString(R.string.zanwugengduoshuju));
                        isShow = true;
                    }
                } else {
                    loadMore();
                }
            }
        };
        list.addOnScrollListener(scrollListener);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isEnd = false;
                scrollListener.reset();
                initData();
            }
        });
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(mActivity,NoticeDetaileActivity.class);
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
            swipeRefresh.setRefreshing(false);
            return;
        }
        //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
        //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
        List<EMMessage> messages = conversation.loadMoreMsgFromDB(lastId, 20);
        if (null==messages){
            isEnd=true;
            swipeRefresh.setRefreshing(false);
            return;
        }
        lastId=messages.get(messages.size()-1).getMsgId();
        if (messages.size()<20){
            isEnd=true;
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
        data.addAll(totle);
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }


    @Override
    protected void initData() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation("SYS_MSG");
        if (null==conversation){
            isEnd=true;
            isFirst=true;
            swipeRefresh.setRefreshing(false);
            return;
        }
        //获取此会话的所有消息
        List<EMMessage> messages = conversation.getAllMessages();
        //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
        if (null==messages){
            isEnd=true;
            isFirst=true;
            swipeRefresh.setRefreshing(false);
            return;
        }
        data.clear();
        if (messages.size()<20){
            isEnd=true;
            isFirst=true;
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
        swipeRefresh.setRefreshing(false);
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }
}
