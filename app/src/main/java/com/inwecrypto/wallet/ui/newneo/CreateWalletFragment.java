package com.inwecrypto.wallet.ui.newneo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.bean.WalletBean;
import com.inwecrypto.wallet.common.util.ToastUtil;

import java.util.ArrayList;


/**
 * @author wudz
 * @Description: 底部弹框
 * @date 2016/07/03 下午 3:19:34
 */
public class CreateWalletFragment extends DialogFragment {

    private LinearLayout addLayout, typeLayout, innerTypeLayout;
    private View close,addNew,importWallet,back,neo,eth,back2,hot,clod;
    private ArrayList<WalletBean> wallets;
    private boolean isNeo=true;
    private boolean isImport;
    private boolean hasType;
    private boolean isEth;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        final Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout. newneo_add_wallet_popup);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() * 3 / 5;
        window.setAttributes(lp);

        wallets= (ArrayList<WalletBean>) getArguments().getSerializable("wallets");

        hasType=getArguments().getBoolean("hasType",false);

        if (hasType){
            isEth=getArguments().getBoolean("isEth",false);
        }

        addLayout = (LinearLayout) dialog.findViewById(R.id.add_newwallet_layout);
        typeLayout = (LinearLayout) dialog.findViewById(R.id.wallet_type_layout);//付款详情
        innerTypeLayout = (LinearLayout) dialog.findViewById(R.id.wallet_inner_type_layout);//付款方式

        close=dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        addNew=dialog.findViewById(R.id.add_new);
        importWallet=dialog.findViewById(R.id.import_wallet);
        back=dialog.findViewById(R.id.back);
        neo=dialog.findViewById(R.id.neo);
        eth=dialog.findViewById(R.id.eth);

        back2=dialog.findViewById(R.id.back2);
        hot=dialog.findViewById(R.id.hot);
        hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),NewNeoAddWalletActivity.class);
                intent.putExtra("wallets",wallets);
                intent.putExtra("isNeo",isNeo);
                startActivity(intent);
                dialog.cancel();
            }
        });
        clod=dialog.findViewById(R.id.clod);
        clod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(getString(R.string.zanbuzhichilengqianbao));
            }
        });

        addNew.setOnClickListener(listener);
        back.setOnClickListener(listener);
        neo.setOnClickListener(listener);
        back2.setOnClickListener(listener);
        importWallet.setOnClickListener(listener);
        eth.setOnClickListener(listener);
        return dialog;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Animation slide_left_to_left = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left_to_left);
            Animation slide_right_to_left = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_right_to_left);
            Animation slide_left_to_right = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left_to_right);
            Animation slide_left_to_left_in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left_to_left_in);
            switch (view.getId()) {
                case R.id.import_wallet:
                    isImport=true;
                case R.id.add_new:
                    if (hasType){
                        if (isEth){
                            isNeo=false;
                        }else {
                            isNeo=true;
                        }
                        if (isImport){
                            Intent intent=new Intent(getActivity(),NewImprotWalletActivity.class);
                            intent.putExtra("wallets",wallets);
                            intent.putExtra("isNeo",isNeo);
                            startActivity(intent);
                            CreateWalletFragment.this.getDialog().cancel();
                        }else {
                            Intent intent=new Intent(getActivity(),NewNeoAddWalletActivity.class);
                            intent.putExtra("wallets",wallets);
                            intent.putExtra("isNeo",isNeo);
                            startActivity(intent);
                            CreateWalletFragment.this.getDialog().cancel();
                        }
                    }else {
                        addLayout.startAnimation(slide_left_to_left);
                        addLayout.setVisibility(View.GONE);
                        typeLayout.startAnimation(slide_right_to_left);
                        typeLayout.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.eth:
                    isNeo=false;
                case R.id.neo:
                    if (isImport){
                        Intent intent=new Intent(getActivity(),NewImprotWalletActivity.class);
                        intent.putExtra("wallets",wallets);
                        intent.putExtra("isNeo",isNeo);
                        startActivity(intent);
                        CreateWalletFragment.this.getDialog().cancel();
                    }else {
                        Intent intent=new Intent(getActivity(),NewNeoAddWalletActivity.class);
                        intent.putExtra("wallets",wallets);
                        intent.putExtra("isNeo",isNeo);
                        startActivity(intent);
                        CreateWalletFragment.this.getDialog().cancel();
                    }
                    break;
                case R.id.back:
                    isNeo=true;
                    isImport=false;
                    addLayout.startAnimation(slide_left_to_left_in);
                    addLayout.setVisibility(View.VISIBLE);
                    typeLayout.startAnimation(slide_left_to_right);
                    typeLayout.setVisibility(View.GONE);
                    break;
                case R.id.back2:
                    if (hasType){
                        addLayout.startAnimation(slide_left_to_left_in);
                        addLayout.setVisibility(View.VISIBLE);
                        innerTypeLayout.startAnimation(slide_left_to_right);
                        innerTypeLayout.setVisibility(View.GONE);
                    }else {
                        typeLayout.startAnimation(slide_left_to_left_in);
                        typeLayout.setVisibility(View.VISIBLE);
                        innerTypeLayout.startAnimation(slide_left_to_right);
                        innerTypeLayout.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
