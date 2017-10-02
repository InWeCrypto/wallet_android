package capital.fbg.wallet.common.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import capital.fbg.wallet.R;

/**
 * @author MaTianyu
 * @date 2014-12-10
 */
public class AppUtil {

    /**
     * 调用系统分享
     */
    public static void shareToOtherApp(Context context,String title,String content, String dialogTitle ) {
        Intent intentItem = new Intent(Intent.ACTION_SEND);
        intentItem.setType("text/plain");
        intentItem.putExtra(Intent.EXTRA_SUBJECT, title);
        intentItem.putExtra(Intent.EXTRA_TEXT, content);
        intentItem.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intentItem, dialogTitle));
    }

    /**
     * need < uses-permission android:name =“android.permission.GET_TASKS” />
     * 判断是否前台运行
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName componentName = taskList.get(0).topActivity;
            if (componentName != null && componentName.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取App包 信息版本号
     * @param context
     * @return
     */
    public PackageInfo getPackageInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }

    /**
     * 判断应用是否已经启动
     * @param context 一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName){
        ActivityManager activityManager =
                (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for(int i = 0; i < processInfos.size(); i++){
            if(processInfos.get(i).processName.equals(packageName)){
                Log.i("NotificationLaunch",
                        String.format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        Log.i("NotificationLaunch",
                String.format("the %s is not running, isAppAlive return false", packageName));
        return false;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188、178
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1]\\d{10}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isAddress(String address) {
        String num = "0x[0-9a-zA-Z]{40}";
        if (TextUtils.isEmpty(address)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return address.matches(num);
        }
    }

    //View 转 bitmap
    public static Bitmap getBitmapByView(View view,Activity context) {
        Bitmap bitmap= Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 300) {
            // 重置baos即清空baos
            baos.reset();
            // 每次都减少10
            options -= 10;
            // 这里压缩options%，把压缩后的数据存放到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        String path = Environment.getExternalStorageDirectory() +"/";
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + "small.jpg");
        try {
            FileOutputStream fos = new FileOutputStream(myCaptureFile);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 释放Bitmap
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return BitmapFactory.decodeFile(myCaptureFile.getAbsolutePath());
    }

    // 任意进制数转为十进制数
    public static String toD(String a) {
        BigDecimal nResult = new BigDecimal(0);
        String str = a.toUpperCase();
        if ( str.length() > 2 ){
            if ( str.charAt(0) == '0' && str.charAt(1) == 'X' ){
                str = str.substring(2);
            }
        }
        int nLen = str.length();
        for ( int i=0; i<nLen; ++i ){
            char ch = str.charAt(nLen-i-1);
            try {
                nResult = nResult.add(new BigDecimal(GetHex(ch)).multiply(new BigDecimal(16).pow(i)));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return nResult.toPlainString();
    }

    //计算16进制对应的数值
    public static int GetHex(char ch) throws Exception{
        if ( ch>='0' && ch<='9' )
            return (int)(ch-'0');
        if ( ch>='a' && ch<='f' )
            return (int)(ch-'a'+10);
        if ( ch>='A' && ch<='F' )
            return (int)(ch-'A'+10);
        throw new Exception("error param");
    }

    public static int getIcon(int i) {
        switch (i) {
            case 1:
                return R.mipmap.heade_01;
            case 2:
                return R.mipmap.heade_02;
            case 3:
                return R.mipmap.heade_03;
            case 4:
                return R.mipmap.heade_04;
            case 5:
                return R.mipmap.heade_05;
            case 6:
                return R.mipmap.heade_06;
            case 7:
                return R.mipmap.heade_07;
            case 8:
                return R.mipmap.heade_08;
            case 9:
                return R.mipmap.heade_09;
            case 10:
                return R.mipmap.heade_10;
        }
        return R.mipmap.heade_01;
    }


    public static int getRoundmIcon() {
        Random random = new Random();
        int i = random.nextInt(10) + 1;
        return i;
    }

    public static int getRoundmHeadIcon(int i) {
        switch (i){
            case 1:
                return R.drawable.img_1;
            case 2:
                return R.drawable.img_2;
            case 3:
                return R.drawable.img_3;
            case 4:
                return R.drawable.img_4;
            case 5:
                return R.drawable.img_5;
            case 6:
                return R.drawable.img_6;
            case 7:
                return R.drawable.img_7;
            case 8:
                return R.drawable.img_8;
            case 9:
                return R.drawable.img_9;
            case 10:
                return R.drawable.img_10;
        }
        return R.drawable.img_1;
    }
}
