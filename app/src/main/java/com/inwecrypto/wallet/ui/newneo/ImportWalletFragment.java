package com.inwecrypto.wallet.ui.newneo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;

/**
 * 作者：xiaoji06 on 2018/1/8 15:01
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ImportWalletFragment extends DialogFragment {

    private OnNextInterface listener;
    private int type;
    private EditText etPs,etName;
    private View passll,line;
    private ImageView see;
    private boolean isSee;
    private String name;
    private boolean isEdit;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        final Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.newneo_importwallet_popup);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() * 3 / 5;
        window.setAttributes(lp);

        type=getArguments().getInt("type");
        name=getArguments().getString("name");
        isEdit=getArguments().getBoolean("isEdit",true);


        etName= (EditText) dialog.findViewById(R.id.et_name);

        if (!isEdit){
            etName.setText(name);
            etName.setEnabled(false);
        }

        passll=dialog.findViewById(R.id.passll);

        etPs= (EditText) dialog.findViewById(R.id.et_password);

        see=(ImageView)dialog.findViewById(R.id.pass_see);

        line=dialog.findViewById(R.id.passline);

        if (type==1){
            passll.setVisibility(View.INVISIBLE);
            line.setVisibility(View.INVISIBLE);
        }else {
            see.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if(isSee){
                        isSee=false;
                        see.setImageResource(R.mipmap.closeseexxhdpi);
                        //否则隐藏密码
                        etPs.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }else{
                        isSee=true;
                        see.setImageResource(R.mipmap.openpassxxhdpi);
                        //如果选中，显示密码
                        etPs.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    etPs.setSelection(etPs.getText().length());
                }
            });

        }

        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().trim().length() == 0) {
                    ToastUtil.show(getString(R.string.qingtianxiemingcheng));
                    return;
                }
                if (type!=1&&etPs.getText().toString().trim().length() == 0) {
                    ToastUtil.show(getString(R.string.qingtianxiemima));
                    return;
                }
                if (type!=1&&!AppUtil.isContainAll(etPs.getText().toString().trim())) {
                    ToastUtil.show(getString(R.string.mimayaoqiu));
                    return;
                }
                if (null!=listener){
                    listener.onNext(etName.getText().toString().trim(),etPs.getText().toString().trim(),dialog);
                }
            }
        });

        return dialog;
    }

    public void setOnNextListener(OnNextInterface listener){
        this.listener=listener;
    }

    public interface OnNextInterface{
        void onNext(String name,String pass,Dialog dialog);
    }
}