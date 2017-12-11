package com.inwecrypto.wallet.common.widget;

/**
 * 作者：xiaoji06 on 2017/11/14 15:36
 * github：https://github.com/xiaoji06
 * 功能：
 */
public interface MultiItemTypeSupport<T>
{
    int getLayoutId(int itemType);

    int getItemViewType(int position, T t);
}
