/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.inwecrypto.wallet.bean;

import java.io.Serializable;

public class LocalWalletBean implements Serializable {

    private static final long serialVersionUID = 5213230387175987834L;

    public int code;
    public String msg;
    public String url;
    public LocalWalletListBean data;

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
