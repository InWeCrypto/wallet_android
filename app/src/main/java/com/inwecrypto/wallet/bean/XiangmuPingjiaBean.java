package com.inwecrypto.wallet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xiaoji06 on 2018/4/9 11:11
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class XiangmuPingjiaBean implements Serializable {


    /**
     * current_page : 1
     * data : [{"id":134,"category_id":2,"user_id":41,"is_favorite":false,"is_market_follow":false,"market_hige":"0","market_lost":"0","score":"1","is_favorite_dot":false,"is_top":true,"is_category_comment":1,"category_comment_tag_id":2,"category_comment_tag_name":"最新","category_comment":"test","category_comment_at":"2018-03-28 02:16:08","user_click_comment_up_count":0,"user_click_comment_down_count":1,"user_click_comment_equal_count":0,"comment_count":9,"user":{"id":41,"name":"啦啦啦","img":"","email":"18081789190@163.com"},"user_click_comment":[{"category_user_id":134,"up":0,"down":1,"equal":0}]}]
     * first_page_url : /category/home_follow?page=1
     * from : 1
     * last_page : 1
     * last_page_url : /category/home_follow?page=1
     * next_page_url :
     * path : /category/home_follow
     * per_page : 3
     * prev_page_url :
     * to : 3
     * total : 3
     */

    private int current_page;
    private String first_page_url;
    private int from;
    private int last_page;
    private String last_page_url;
    private String next_page_url;
    private String path;
    private int per_page;
    private String prev_page_url;
    private int to;
    private int total;
    private List<PingjiaBean> data;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public String getFirst_page_url() {
        return first_page_url;
    }

    public void setFirst_page_url(String first_page_url) {
        this.first_page_url = first_page_url;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public String getLast_page_url() {
        return last_page_url;
    }

    public void setLast_page_url(String last_page_url) {
        this.last_page_url = last_page_url;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public String getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(String prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<PingjiaBean> getData() {
        return data;
    }

    public void setData(List<PingjiaBean> data) {
        this.data = data;
    }
}
