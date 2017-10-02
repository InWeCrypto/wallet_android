package capital.fbg.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/15.
 * 功能描述：
 * 版本：@version
 */

public class TransferBean implements Serializable {

    private String wallet_address;
    private String nonce;
    private String ox_gas;
    private String transfer_address;  //如果是代币转账 这里放合约地址
    private String ox_price;  //如果是代币转账 这里放data
    private String show_price;
    private String show_gas;
    private String gas_limit;
    private String hit;
    private int type;//1,钱包转账  2，代币转账

    public TransferBean(){

    }

    public TransferBean(String wallet_address, String nonce, String ox_gas, String transfer_address, String ox_price, String show_price, String show_gas,String gas_limit, String hit,int type) {
        this.wallet_address = wallet_address;
        this.nonce = nonce;
        this.ox_gas = ox_gas;
        this.transfer_address = transfer_address;
        this.ox_price = ox_price;
        this.show_price = show_price;
        this.show_gas = show_gas;
        this.gas_limit=gas_limit;
        this.hit = hit;
        this.type=type;
    }

    public String getWallet_address() {
        return wallet_address;
    }

    public void setWallet_address(String wallet_address) {
        this.wallet_address = wallet_address;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getOx_gas() {
        return ox_gas;
    }

    public void setOx_gas(String ox_gas) {
        this.ox_gas = ox_gas;
    }

    public String getTransfer_address() {
        return transfer_address;
    }

    public void setTransfer_address(String transfer_address) {
        this.transfer_address = transfer_address;
    }

    public String getOx_price() {
        return ox_price;
    }

    public void setOx_price(String ox_price) {
        this.ox_price = ox_price;
    }

    public String getShow_price() {
        return show_price;
    }

    public void setShow_price(String show_price) {
        this.show_price = show_price;
    }

    public String getShow_gas() {
        return show_gas;
    }

    public void setShow_gas(String show_gas) {
        this.show_gas = show_gas;
    }

    public String getHit() {
        return hit;
    }

    public void setHit(String hit) {
        this.hit = hit;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGas_limit() {
        return gas_limit;
    }

    public void setGas_limit(String gas_limit) {
        this.gas_limit = gas_limit;
    }
}
