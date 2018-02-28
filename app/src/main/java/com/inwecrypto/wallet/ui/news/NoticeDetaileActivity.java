package com.inwecrypto.wallet.ui.news;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.NoticeBean;
import com.inwecrypto.wallet.common.widget.SimpleToolbar;
import com.inwecrypto.wallet.event.BaseEventBusBean;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class NoticeDetaileActivity extends BaseActivity {


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
    @BindView(R.id.order_time)
    TextView orderTime;
    @BindView(R.id.content)
    TextView content;

    private NoticeBean notice;

    @Override
    protected void getBundleExtras(Bundle extras) {
        notice = (NoticeBean) extras.getSerializable("notice");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.notice_detaile_activity;
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
        txtRightTitle.setVisibility(View.GONE);

        title.setText(notice.getTitle());
        orderTime.setText(notice.getTime());
        content.setText(notice.getContent());
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {

    }

}
