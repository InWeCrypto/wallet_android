package capital.fbg.wallet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import capital.fbg.wallet.common.Constant;
import capital.fbg.wallet.common.util.NetworkUtils;
import capital.fbg.wallet.event.BaseEventBusBean;

/**
 * Created by Administrator on 2017/8/30.
 * 功能描述：
 * 版本：@version
 */

public class NetStateReceiver extends BroadcastReceiver {

    private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)) {
            if (NetworkUtils.isConnected(context)) {
                EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_CLOD_NET));
            } else {
                EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_CLOD_UNNET));
            }
        }
    }
}

