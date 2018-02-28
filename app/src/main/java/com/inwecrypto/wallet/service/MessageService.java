package com.inwecrypto.wallet.service;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.inwecrypto.wallet.R;
import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.util.AppUtil;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import com.inwecrypto.wallet.ui.MainTabActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/2/24 14:59
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class MessageService extends Service {

    private int i;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        return Service.START_STICKY;
    }

    EMMessageListener msgListener;

    {
        msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {

                //应用是否在前台
                if (1==AppUtil.getAppSatus(getApplicationContext(),"com.inwecrypto.wallet")) {
                    //收到消息
                    EventBus.getDefault().postSticky(new BaseEventBusBean(Constant.EVENT_ZIXUN_MESSAGE));
                } else {
                    Intent intent=null;
                    //判断app进程是否存活
                    if(2==AppUtil.getAppSatus(getApplicationContext(),"com.inwecrypto.wallet")){
                        //如果存活的话，就直接启动DetailActivity，但要考虑一种情况，就是app的进程虽然仍然在
                        //但Task栈已经空了，比如用户点击Back键退出应用，但进程还没有被系统回收，如果直接启动
                        //DetailActivity,再按Back键就不会返回MainActivity了。所以在启动
                        //DetailActivity前，要先启动MainActivity。
                        intent = new Intent(getApplicationContext(), MainTabActivity.class);
                        //将MainAtivity的launchMode设置成SingleTask, 或者在下面flag中加上Intent.FLAG_CLEAR_TOP,
                        //如果Task栈中有MainActivity的实例，就会把它移到栈顶，把在它之上的Activity都清理出栈，
                        //如果Task栈不存在MainActivity实例，则在栈顶创建
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("isRe",true);

                    }else {
                        //如果app进程已经被杀死，先重新启动app，将DetailActivity的启动参数传入Intent中，参数经过
                        //SplashActivity传入MainActivity，此时app的初始化已经完成，在MainActivity中就可以根据传入             //参数跳转到DetailActivity中去了
                        intent = getApplicationContext().getPackageManager().
                                getLaunchIntentForPackage("com.inwecrypto.wallet");
                        intent.setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    }
                    PendingIntent intentPend = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

                    String title= "InWe";
                    EMTextMessageBody body= (EMTextMessageBody) messages.get(0).getBody();
                    builder.setContentTitle(title)
                            .setTicker(body.getMessage())
                            .setContentIntent(intentPend)
                            .setSmallIcon(R.mipmap.ic_launcher);

                    NotificationManager manager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(i++, builder.build());

                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                //收到已送达回执
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }
}
