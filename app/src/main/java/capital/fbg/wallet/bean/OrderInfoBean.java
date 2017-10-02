package capital.fbg.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/14.
 * 功能描述：
 * 版本：@version
 */

public class OrderInfoBean implements Serializable {

    private String from;
    private String to;
    private String value;
    private String gas;
    private String gasPrice;
    private String nonce;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
}
