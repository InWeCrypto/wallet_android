package com.inwecrypto.wallet.common.util;

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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.base.BaseActivity;
import com.inwecrypto.wallet.bean.ProjectBean;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.widget.WebView4Scroll;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.info.activity.AllInfoActivity;
import com.inwecrypto.wallet.ui.info.activity.InfoWebActivity;
import com.inwecrypto.wallet.ui.info.adapter.ExplorerAdapter;
import com.inwecrypto.wallet.ui.info.adapter.MarketAdapter;
import com.inwecrypto.wallet.ui.info.adapter.WalletAdapter;
import com.inwecrypto.wallet.ui.info.fragment.AllInfoFragment;
import com.inwecrypto.wallet.ui.info.activity.IcoActivity;
import com.inwecrypto.wallet.ui.info.activity.ProjectOnlineActivity;
import com.inwecrypto.wallet.ui.info.activity.ProjectUnderlineActivity;
import com.inwecrypto.wallet.ui.info.adapter.PopupMarketAdapter;
import com.inwecrypto.wallet.ui.me.activity.CommonWebActivity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import org.greenrobot.eventbus.EventBus;


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

    public static boolean isAddress(String address) {
        String num = "0x[0-9a-zA-Z]{40}";
        String neo = "[0-9a-zA-Z]{34}";
        if (TextUtils.isEmpty(address)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return address.matches(num)||address.matches(neo);
        }
    }

    public static boolean isEthAddress(String address) {
        String num = "0x[0-9a-zA-Z]{40}";
        if (TextUtils.isEmpty(address)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return address.matches(num);
        }
    }

    public static boolean isNeoAddress(String address) {
        String neo = "[0-9a-zA-Z]{34}";
        if (TextUtils.isEmpty(address)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return address.matches(neo);
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

    /**
     * 规则3：必须同时包含大小写字母及数字
     * 是否包含
     *
     * @param str
     * @return
     */
    public static boolean isContainAll(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLowerCase = false;//定义一个boolean值，用来表示是否包含字母
        boolean isUpperCase = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLowerCase(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLowerCase = true;
            } else if (Character.isUpperCase(str.charAt(i))) {
                isUpperCase = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLowerCase && isUpperCase && str.matches(regex);
        return isRight;
    }

    /**
     * byte数组转换为十六进制的字符串
     **/
    public static String conver16HexStr(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            if ((b[i] & 0xff) < 0x10)
                result.append("0");
            result.append(Long.toString(b[i] & 0xff, 16));
        }
        return result.toString().toUpperCase();
    }

    public static void showMainMenu(View view, final BaseActivity activity,boolean isShowMain) {

        View selectPopupWin = LayoutInflater.from(activity).inflate(R.layout.view_popup_main_menu, null, false);
        TextView main=(TextView)selectPopupWin.findViewById(R.id.main);
        TextView allNews = (TextView) selectPopupWin.findViewById(R.id.all_news);
        TextView ico = (TextView) selectPopupWin.findViewById(R.id.ico);

        if (!isShowMain){
            main.setVisibility(View.GONE);
        }
        final PopupWindow window = new PopupWindow(selectPopupWin, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00FFFFFF")));
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();
        window.showAsDropDown(view,-DensityUtil.dip2px(activity,20),-DensityUtil.dip2px(activity,20));

        if (isShowMain){
            main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppManager.getAppManager().finishActivity(ProjectOnlineActivity.class);
                    AppManager.getAppManager().finishActivity(ProjectUnderlineActivity.class);
                    AppManager.getAppManager().finishActivity(AllInfoFragment.class);
                    AppManager.getAppManager().finishActivity(IcoActivity.class);
                }
            });
        }

        allNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.keepTogo(AllInfoActivity.class);
                window.dismiss();
            }
        });

        ico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.keepTogo(IcoActivity.class);
                window.dismiss();
            }
        });
    }

    public static void showMarketMenu(View view, final BaseActivity activity,ArrayList<ProjectBean.ProjectMarketsBean> marketList) {

        View selectPopupWin = LayoutInflater.from(activity).inflate(R.layout.info_popup_view_market, null, false);
        RecyclerView list= (RecyclerView) selectPopupWin.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(activity));
        PopupMarketAdapter adapter=new PopupMarketAdapter(activity,R.layout.info_item_popup_market,marketList);
        list.setAdapter(adapter);

        final PopupWindow window = new PopupWindow(selectPopupWin, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00FFFFFF")));
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();
        window.showAsDropDown(view,0,0);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_POPUP_MARKET,position));
                window.dismiss();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    public static void showLiulanqi(final BaseActivity activity, View view, final ArrayList<ProjectBean.ProjectExplorersBean> data){
        // 产生背景变暗效果
        WindowManager.LayoutParams lp = activity.getWindow()
                .getAttributes();
        lp.alpha = 0.4f;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);

        View selectPopupWin = LayoutInflater.from(activity).inflate(R.layout.view_popup_liulanqi, null, false);
        TextView title=(TextView)selectPopupWin.findViewById(R.id.title);
        RecyclerView list = (RecyclerView) selectPopupWin.findViewById(R.id.list);
        title.setText(R.string.liulanqi);

        final PopupWindow window = new PopupWindow(selectPopupWin, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#eeeeee")));
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();
        window.showAtLocation(view,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                activity.getWindow().setAttributes(lp);
            }
        });

        ExplorerAdapter explorerAdapter = new ExplorerAdapter(activity, R.layout.info_item_explorer, data);
        list.setLayoutManager(new LinearLayoutManager(activity));
        list.setAdapter(explorerAdapter);
        explorerAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (null == data.get(position).getUrl()
                        || null == data.get(position).getName()) {
                    return;
                }
                Intent intent = new Intent(activity, InfoWebActivity.class);
                intent.putExtra("title", data.get(position).getName());
                intent.putExtra("url", data.get(position).getUrl());
                activity.keepTogo(intent);
                window.dismiss();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    public static void showWallet(final BaseActivity activity, View view, final ArrayList<ProjectBean.ProjectWalletsBean> data){
        // 产生背景变暗效果
        WindowManager.LayoutParams lp = activity.getWindow()
                .getAttributes();
        lp.alpha = 0.4f;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);

        View selectPopupWin = LayoutInflater.from(activity).inflate(R.layout.view_popup_liulanqi, null, false);
        TextView title=(TextView)selectPopupWin.findViewById(R.id.title);
        RecyclerView list = (RecyclerView) selectPopupWin.findViewById(R.id.list);
        title.setText(R.string.zhichiqianbao);

        final PopupWindow window = new PopupWindow(selectPopupWin, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#eeeeee")));
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();
        window.showAtLocation(view,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                activity.getWindow().setAttributes(lp);
            }
        });

        WalletAdapter walletAdapter = new WalletAdapter(activity,R.layout.info_item_explorer, data);
        list.setLayoutManager(new LinearLayoutManager(activity));
        list.setAdapter(walletAdapter);
        walletAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (null == data.get(position).getUrl()
                        || null == data.get(position).getName()) {
                    return;
                }
                Intent intent = new Intent(activity, InfoWebActivity.class);
                intent.putExtra("title", data.get(position).getName());
                intent.putExtra("url", data.get(position).getUrl());
                activity.keepTogo(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    public static WebView createWebView(WebView webView,BaseActivity activity) {

        WebView.setWebContentsDebuggingEnabled(true);
        //不能横向滚动
        webView.setHorizontalScrollBarEnabled(false);
        //不能纵向滚动
        webView.setVerticalScrollBarEnabled(false);
        //允许截图
        webView.setDrawingCacheEnabled(true);
        //屏蔽长按事件
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        //初始化WebSettings
        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //隐藏缩放控件
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        //禁止缩放
        settings.setSupportZoom(false);
        //文件权限
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowContentAccess(true);

        if (NetworkUtils.isConnected(activity.getApplicationContext())) {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
        }

        //缓存相关
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        return webView;
    }

    public static WebView4Scroll createWebView(WebView4Scroll webView, BaseActivity activity) {

        WebView.setWebContentsDebuggingEnabled(true);
        //不能横向滚动
        webView.setHorizontalScrollBarEnabled(false);
        //不能纵向滚动
        webView.setVerticalScrollBarEnabled(false);
        //允许截图
        webView.setDrawingCacheEnabled(true);
        //屏蔽长按事件
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        //初始化WebSettings
        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //隐藏缩放控件
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        //禁止缩放
        settings.setSupportZoom(false);
        //文件权限
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowContentAccess(true);

        if (NetworkUtils.isConnected(activity.getApplicationContext())) {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
        }

        //缓存相关
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        return webView;
    }

    public static String getTime(String time){
        SimpleDateFormat dff =null;
        if (time.contains(".")){
            dff = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");//输入的被转化的时间格式
        }else {
            dff= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");//输入的被转化的时间格式
        }
        dff.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//需要转化成的时间格式
        Date date1 = null;
        try {
            date1 = dff.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null==date1?"":df1.format(date1);
    }

    /*
     * 函数：reverseArray1和reverseArray2
     * 功能：实现 数组翻转
     * 例如：{'a','b','c','d'}变成{'d','c','b','a'}
     */
    public static byte[] reverseArray(String string) {
        if ("0".equals(string)){
            byte[] zero=new byte[1];
            return zero;
        }
        byte[] array=hexStringToBytes(string);
        byte[] array_list = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            array_list[i]=(array[array.length - i - 1]);
        }
        return array_list;
    }

    /**
     * Convert hex string to byte[]
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     * @param c char
     * @return byte
     */
    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
