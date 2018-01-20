package com.inwecrypto.wallet.common.util;

import android.widget.Toast;

import com.inwecrypto.wallet.App;

/**
 * Created by Stevin on 2017/1/17.
 */

public class ToastUtil {

    private static Toast toast;

    public static void show(String msg) {
        if (toast == null) {
            toast = Toast.makeText(App.get(), msg, Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }

    public static void show(int msgId) {
        if (toast == null) {
            toast = Toast.makeText(App.get(),msgId, Toast.LENGTH_SHORT);
        }
        toast.setText(msgId);
        toast.show();
    }
}
