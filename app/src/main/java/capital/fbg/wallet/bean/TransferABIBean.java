package capital.fbg.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/15.
 * 功能描述：
 * 版本：@version
 */

public class TransferABIBean implements Serializable {

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
