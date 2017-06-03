package com.cskd20.module.main.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.cskd20.R;
import com.cskd20.base.BaseActivity;
import com.cskd20.base.BaseFragment;
import com.cskd20.module.main.adapter.MainFragmentAdapter;
import com.cskd20.module.main.fragment.MainFragment;
import com.cskd20.module.main.fragment.PersonalFragment;
import com.cskd20.module.main.fragment.ServerFragment;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * 主页框架
 */
public class MainActivity extends BaseActivity {
    @Bind(R.id.tab)
    TabLayout mTab;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    @Override
    protected int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ArrayList<BaseFragment> fragments = new ArrayList<>();
        fragments.add(new PersonalFragment());
        fragments.add(new MainFragment());
        fragments.add(new ServerFragment());
        ArrayList<String> titles = new ArrayList<>();
        titles.add("我的");
        titles.add("首页");
        titles.add("服务站");
        MainFragmentAdapter fragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager(), fragments,
                titles);
        mTab.addTab(mTab.newTab().setText(titles.get(0)));
        mTab.addTab(mTab.newTab().setText(titles.get(1)));
        mTab.setTabMode(TabLayout.MODE_FIXED);
        mViewPager.setAdapter(fragmentAdapter);
        mTab.setupWithViewPager(mViewPager);
    }
}
