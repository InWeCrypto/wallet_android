package capital.fbg.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/14.
 * 功能描述：
 * 版本：@version
 */

public class LocaWalletBean implements Serializable {

    private String address;
    private String type;
    private String wallet_type;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWallet_type() {
        return wallet_type;
    }

    public void setWallet_type(String wallet_type) {
        this.wallet_type = wallet_type;
    }
}
