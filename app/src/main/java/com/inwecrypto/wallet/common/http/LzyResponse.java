
package com.inwecrypto.wallet.common.http;

import java.io.Serializable;

public class LzyResponse<T> implements Serializable {

    private static final long serialVersionUID = 5213230387175987834L;

    public int code;
    public String msg;
    public String url;
    public T data;

    @Override
    public String toString() {
        return "LzyResponse{\n" +//
               "\tcode=" + code + "\n" +//
               "\tmsg='" + msg + "\'\n" +//
                "\turl='" + msg + "\'\n" +//
               "\tdata=" + data + "\n" +//
               '}';
    }
}
