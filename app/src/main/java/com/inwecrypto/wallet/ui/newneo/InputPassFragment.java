package com.inwecrypto.wallet.ui.newneo;

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

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.common.util.ToastUtil;

/**
 * 作者：xiaoji06 on 2018/1/8 15:01
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class InputPassFragment extends DialogFragment {

    private OnNextInterface listener;
    private EditText pass;
    private boolean isSee;
    private ImageView passsee;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        final Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.newneo_createwallet_pass_popup);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() * 3 / 5;
        window.setAttributes(lp);

        pass= (EditText) dialog.findViewById(R.id.et_password);
        passsee = (ImageView)dialog.findViewById(R.id.pass_see);

        passsee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(isSee){
                    isSee=false;
                    passsee.setImageResource(R.mipmap.closeseexxhdpi);
                    //否则隐藏密码
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    isSee=true;
                    passsee.setImageResource(R.mipmap.openpassxxhdpi);
                    //如果选中，显示密码
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                pass.setSelection(pass.getText().length());
            }
        });

        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass.getText().toString().length()==0){
                    ToastUtil.show("密码不能为空");
                    return;
                }
                if (null!=listener){
                    listener.onNext(pass.getText().toString().trim(),dialog);
                }
            }
        });

        return dialog;
    }

    public void setOnNextListener(OnNextInterface listener){
        this.listener=listener;
    }

    public interface OnNextInterface{
        void onNext(String pass,Dialog dialog);
    }
}