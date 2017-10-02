package capital.fbg.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/10.
 * 功能描述：
 * 版本：@version
 */

public class OrderBean implements Serializable {

    private int id;
    private int user_id;
    private int wallet_id;
    private String trade_no;
    private String pay_address;
    private String receive_address;
    private String remark;
    private String fee;
    private String handle_fee;
    private String hash;
    private int status;
    private String finished_at;
    private String created_at;
    private String updated_at;
    private String flag;
    private String block_number;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getPay_address() {
        return pay_address;
    }

    public void setPay_address(String pay_address) {
        this.pay_address = pay_address;
    }

    public String getReceive_address() {
        return receive_address;
    }

    public void setReceive_address(String receive_address) {
        this.receive_address = receive_address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getHandle_fee() {
        return handle_fee;
    }

    public void setHandle_fee(String handle_fee) {
        this.handle_fee = handle_fee;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFinished_at() {
        return finished_at;
    }

    public void setFinished_at(String finished_at) {
        this.finished_at = finished_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getBlock_number() {
        return block_number;
    }

    public void setBlock_number(String block_number) {
        this.block_number = block_number;
    }
}
