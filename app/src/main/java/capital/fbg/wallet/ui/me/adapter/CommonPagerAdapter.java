package capital.fbg.wallet.ui.me.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import capital.fbg.wallet.base.BaseFragment;

/**
 * Created by Administrator on 2017/8/4.
 * 功能描述：
 * 版本：@version
 */

public class CommonPagerAdapter extends FragmentPagerAdapter{

    ArrayList<BaseFragment> fragments;

    public CommonPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
        super(fm);
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
