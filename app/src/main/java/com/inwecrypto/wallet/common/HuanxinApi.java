package com.inwecrypto.wallet.common;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

/**
 * 作者：xiaoji06 on 2018/2/11 19:59
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class HuanxinApi {
    public static List<EMGroup> getGroup(){
        //从服务器获取自己加入的和创建的群组列表，此api获取的群组sdk会自动保存到内存和db。
        try {
            List<EMGroup> grouplist = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();//需异步处理
            return grouplist;
        } catch (HyphenateException e) {
            e.printStackTrace();
            return null;
        }
    }
}
