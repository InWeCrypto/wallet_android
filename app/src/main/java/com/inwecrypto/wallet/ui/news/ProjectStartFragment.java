package com.inwecrypto.wallet.ui.news;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.common.widget.RatingBar;

/**
 * 作者：xiaoji06 on 2018/1/8 15:01
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ProjectStartFragment extends DialogFragment {

    private OnNextInterface listener;
    private RatingBar ratingbar;
    private TextView fen;
    private float start=0.0f;
    private boolean isSb;
    private String num;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        final Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.project_start_popup);
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

        isSb=getArguments().getBoolean("isSb");
        num=getArguments().getString("num");
        ratingbar= (RatingBar) dialog.findViewById(R.id.ratingbar);
        fen= (TextView) dialog.findViewById(R.id.fen);

        fen.setText(num+getString(R.string.fenshu));
        ratingbar.setStar(Float.parseFloat(num));

        if (isSb){
            dialog.findViewById(R.id.ratingbarfl).setClickable(true);
            dialog.findViewById(R.id.ratingbarfl).setFocusable(true);
            ratingbar.setClickable(false);
            ratingbar.setFocusable(false);
            ratingbar.setEnabled(false);
        }

        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ratingbar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float RatingCount) {
                start=RatingCount;
                fen.setText(start+getString(R.string.fenshu));
            }
        });

        dialog.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSb){
                    ToastUtil.show(getString(R.string.gaixiangmuyijngpingfen));
                    return;
                }
                if (null!=listener){
                    listener.onNext(start,dialog);
                }
            }
        });

        return dialog;
    }

    public void setOnNextListener(OnNextInterface listener){
        this.listener=listener;
    }

    public interface OnNextInterface{
        void onNext(float fen, Dialog dialog);
    }
}