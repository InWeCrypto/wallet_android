package com.inwecrypto.wallet.ui.news.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.NewNeoTokenListBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.newneo.NewNeoWalletActivity;
import com.inwecrypto.wallet.ui.newneo.NewNeoWalletListPopupAdapter;
import com.inwecrypto.wallet.ui.news.adapter.HongbaoWalletListPopupAdapter;
import com.inwecrypto.wallet.ui.wallet.activity.HotWalletActivity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 作者：xiaoji06 on 2018/1/8 15:01
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class HongbaoWalletListFragment extends DialogFragment {

    private RecyclerView list;
    private HongbaoWalletListPopupAdapter adapter;
    private ArrayList<WalletBean> walletBeans;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        final Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.hongbao_wallet_list_popup);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() * 3 / 5;
        window.setAttributes(lp);

        walletBeans= (ArrayList<WalletBean>) getArguments().getSerializable("wallet");

        list= (RecyclerView) dialog.findViewById(R.id.list);

        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        adapter=new HongbaoWalletListPopupAdapter(getActivity(),R.layout.hongbao_wallet_list_popup_item,walletBeans);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (walletBeans.get(position).getType().equals(Constant.GUANCHA)){
                    ToastUtil.show(R.string.bunengshiyongguanchaqianbao);
                    return;
                }
                for (WalletBean wallet:walletBeans){
                    wallet.setSelect(false);
                }
                walletBeans.get(position).setSelect(true);
                EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_HONGBAO_ADDRESS_GNT,position));
                dialog.dismiss();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return dialog;
    }
}