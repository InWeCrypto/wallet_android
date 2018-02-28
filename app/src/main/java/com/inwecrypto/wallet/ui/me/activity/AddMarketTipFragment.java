package com.inwecrypto.wallet.ui.me.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;

/**
 * 作者：xiaoji06 on 2018/1/8 15:01
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class AddMarketTipFragment extends DialogFragment {

    private OnNextInterface listener;
    private EditText hight,low;
    private String price;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        final Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.add_market_tip_popup);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //lp.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() * 3 / 5;
        window.setAttributes(lp);

        price=getArguments().getString("price");
        String high = getArguments().getString("high");
        String lowP = getArguments().getString("low");

        hight= (EditText) dialog.findViewById(R.id.hight);
        low= (EditText) dialog.findViewById(R.id.low);
        hight.setText(high);
        low.setText(lowP);

        ((TextView)dialog.findViewById(R.id.price)).setText(price);

        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hight.getText().toString().trim().length()==0){
                    ToastUtil.show(getString(R.string.jiagebunengweikong));
                    return;
                }
                if (low.getText().toString().trim().length()==0){
                    ToastUtil.show(getString(R.string.jiagebunengweikong));
                    return;
                }
                try {
                    Float.parseFloat(hight.getText().toString().trim());
                    Float.parseFloat(low.getText().toString().trim());
                }catch (Exception e){
                    ToastUtil.show(getString(R.string.qingshuruzhengquedejiage));
                    return;
                }

                if (Float.parseFloat(hight.getText().toString().trim())<=Float.parseFloat(low.getText().toString().trim())){
                    ToastUtil.show(getString(R.string.zuigaozhibunengxiaoyudengyuzuidizhi));
                    return;
                }

                if (null!=listener){
                    listener.onNext(hight.getText().toString().trim(),low.getText().toString().trim(),dialog);
                }
            }
        });

        return dialog;
    }

    public void setOnNextListener(OnNextInterface listener){
        this.listener=listener;
    }

    public interface OnNextInterface{
        void onNext(String hight, String low, Dialog dialog);
    }
}