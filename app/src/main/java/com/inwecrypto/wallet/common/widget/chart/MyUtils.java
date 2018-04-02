package com.inwecrypto.wallet.common.widget.chart;

import com.inwecrypto.wallet.App;
import com.inwecrypto.wallet.R;

import java.text.DecimalFormat;

/**
 * Created by loro on 2017/2/8.
 */
public class MyUtils {
    /**
     * Prevent class instantiation.
     */
    private MyUtils() {
    }

    public static String getVolUnit(float num) {

        int e = (int) Math.floor(Math.log10(num));

        if (e >= 8) {
            return App.get().getResources().getString(R.string.yi);
        } else if (e >= 4) {
            return App.get().getResources().getString(R.string.wan);
        } else {
            return "";
        }
    }

    public static int getVolUnitNum(float num) {

        int e = (int) Math.floor(Math.log10(num));

        if (e >= 8) {
            return 8;
        } else if (e >= 4) {
            return 4;
        } else {
            return 1;
        }
    }

    public static String getVolUnitText(int unit, float num) {
        DecimalFormat mFormat;
        if (unit == 1) {
            mFormat = new DecimalFormat("#0");
        } else {
            mFormat = new DecimalFormat("#0.00");
        }
        num = num / unit;
        if (num == 0) {
            return "0";
        }
        return mFormat.format(num);
    }

    public static String getDecimalFormatVol(float vol) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        return decimalFormat.format(vol);//format 返回的是字符串
    }
}
