package capital.fbg.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/13.
 * 功能描述：
 * 版本：@version
 */

public class GasBean implements Serializable {
    private String gasPrice;

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }
}
