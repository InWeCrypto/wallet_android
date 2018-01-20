package com.inwecrypto.wallet.ui.newneo;

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
import android.widget.EditText;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.NewNeoTokenListBean;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.ui.wallet.adapter.NeoWalletListAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 作者：xiaoji06 on 2018/1/8 15:01
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class WalletListFragment extends DialogFragment {

    private OnNextInterface listener;
    private RecyclerView list;
    private NewNeoWalletListPopupAdapter adapter;
    private ArrayList<NewNeoTokenListBean> walletBeans=new ArrayList<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        final Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.newneo_wallet_list_popup);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() * 3 / 5;
        window.setAttributes(lp);

        walletBeans= (ArrayList<NewNeoTokenListBean>) getArguments().getSerializable("wallets");

        list= (RecyclerView) dialog.findViewById(R.id.list);

        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        Collections.sort(walletBeans, new SortByPrice());
        adapter=new NewNeoWalletListPopupAdapter(getActivity(),R.layout.newneo_wallet_list_popup_item,walletBeans);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), NewNeoWalletActivity.class);
                intent.putExtra("wallet", walletBeans.get(position).getWallet());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return dialog;
    }

    public void setOnNextListener(OnNextInterface listener){
        this.listener=listener;
    }

    public interface OnNextInterface{
        void onNext(String pass, Dialog dialog);
    }

    class SortByPrice implements Comparator {
        public int compare(Object o1, Object o2) {
            NewNeoTokenListBean s1 = (NewNeoTokenListBean) o1;
            NewNeoTokenListBean s2 = (NewNeoTokenListBean) o2;
            if ((Float.parseFloat(s1.getPrice())-Float.parseFloat(s2.getPrice()))>0){
                return -1;
            }else{
                return 1;
            }
        }
    }
}