package com.example.tangdan.cloudmusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MyViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;
    private FragmentManager mFragmentManager;

    public MyViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.mFragmentManager = fm;
        this.mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
