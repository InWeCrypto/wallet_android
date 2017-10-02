package capital.fbg.wallet;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Administrator on 2017/8/10.
 * 功能描述：
 * 版本：@version
 */

public class SleepyAccountsService extends Service {

    private Authenticator authenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        authenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }

}
