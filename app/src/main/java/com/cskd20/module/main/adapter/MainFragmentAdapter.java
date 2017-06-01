package com.cskd20.module.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cskd20.base.BaseFragment;

import java.util.List;

/**
 * @创建者 lucas
 * @创建时间 2017/5/26 0026 9:53
 * @描述 主页面界面切换
 */

public class MainFragmentAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> mFragments;
    private List<String>       mTitles;

    public MainFragmentAdapter(FragmentManager fm, List<BaseFragment> fragments, List<String> titles) {
        super(fm);
        mFragments = fragments;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments==null?0:mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
