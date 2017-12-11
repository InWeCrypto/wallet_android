package com.inwecrypto.wallet.ui.info.adapter;

import android.content.Context;
import android.view.View;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.ProjectBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.widget.MultiItemCommonAdapter;
import com.inwecrypto.wallet.common.widget.MultiItemTypeSupport;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 作者：xiaoji06 on 2017/11/17 10:27
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class WalletAdapter extends CommonAdapter<ProjectBean.ProjectWalletsBean> {

    public WalletAdapter(Context context, int layoutId, List<ProjectBean.ProjectWalletsBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ProjectBean.ProjectWalletsBean projectWalletsBean, final int position) {
        holder.setText(R.id.title,null==projectWalletsBean.getName()?"":projectWalletsBean.getName());
        holder.getView(R.id.title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_WALLET_INFO,position));
            }
        });
    }
}
