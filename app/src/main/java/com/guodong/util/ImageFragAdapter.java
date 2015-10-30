package com.guodong.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by yechy on 2015/10/30.
 */
public class ImageFragAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public ImageFragAdapter(FragmentManager fm, List<Fragment> fragmentList1) {
        super(fm);
        fragmentList = fragmentList1;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}