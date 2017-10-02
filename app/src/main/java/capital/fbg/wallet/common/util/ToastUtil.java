package capital.fbg.wallet.common.util;

import android.widget.Toast;
import capital.fbg.wallet.AppApplication;

/**
 * Created by Stevin on 2017/1/17.
 */

public class ToastUtil {

    private static Toast toast;

    public static void show(String msg) {
        if (toast == null) {
            toast = Toast.makeText(AppApplication.get(), msg, Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }
}
