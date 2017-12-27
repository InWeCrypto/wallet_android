package com.inwecrypto.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/10.
 * 功能描述：
 * 版本：@version
 */

public class OrderBean implements Serializable {

    /**
     * remark : eth
     * handle_fee : 2246500000000000
     * trade_no : 0x43e7a6a92d35bd52bbf9368600815d2e30d9487983138493704db3bf40d2840b
     * hash : 0x43e7a6a92d35bd52bbf9368600815d2e30d9487983138493704db3bf40d2840b
     * pay_address : 0x8214b824927a28dc16581cd22e460fe0f7e31994
     * receive_address : 0x21d6c1bebb4e8141d4784f30a7877bd8c38d5bf8
     * block_number : 2293980
     * fee : 1000000000000000000
     * confirm_at : 2017-12-19T06:17:00Z
     * created_at : 2017-12-19T06:16:45Z
     */

    private String remark;
    private String handle_fee;
    private String trade_no;
    private String hash;
    private String pay_address;
    private String receive_address;
    private int block_number;
    private String fee;
    private String confirm_at;
    private String created_at;
    private int status;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getHandle_fee() {
        return handle_fee;
    }

    public void setHandle_fee(String handle_fee) {
        this.handle_fee = handle_fee;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
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

    public int getBlock_number() {
        return block_number;
    }

    public void setBlock_number(int block_number) {
        this.block_number = block_number;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getConfirm_at() {
        return confirm_at;
    }

    public void setConfirm_at(String confirm_at) {
        this.confirm_at = confirm_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
