package com.inwecrypto.wallet.base;

/**
 * Created by Administrator on 2017/7/15.
 * 功能描述：
 * 版本：@version
 */

public abstract class BaseMvpActivity <T extends BasePresenter> extends BaseActivity{

    public T mPresenter;

    @Override
    protected void setPresenter() {
        super.setPresenter();
        mPresenter=initPresenter();
    }

    /**
     * 简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
     */
    public abstract T initPresenter();

    @Override
    protected void destroyPresenter() {
        super.destroyPresenter();
        mPresenter.onDestroy();
        mPresenter = null;
    }
}
