package com.cskd20.module.main.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.cskd20.R;
import com.cskd20.base.BaseActivity;
import com.cskd20.module.main.adapter.OrderListAdapter;

import java.util.ArrayList;

import butterknife.Bind;

public class OrderListActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {
    @Bind(R.id.tabs)
    TabLayout    mTabs;
    @Bind(R.id.title_name)
    TextView     mTitle;
    @Bind(R.id.list)
    RecyclerView mList;
    @Bind(R.id.refresh)
    SwipeRefreshLayout mRefreshLayout;

    String[] tabStrs = {"默认排序","我的附近","出发时间","全程最长"};
    private OrderListAdapter mAdapter;

    @Override
    protected int setContentView() {
        return R.layout.activity_order_list;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mTitle.setText("接待订单");
        mTabs.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < tabStrs.length; i++) {
            TabLayout.Tab tab = mTabs.newTab();
            tab.setText(tabStrs[i]);
            mTabs.addTab(tab);
        }
        mTabs.addOnTabSelectedListener(this);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new OrderListAdapter();
        mList.setAdapter(mAdapter);
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        mAdapter.setDatas(list);

        mRefreshLayout.setOnRefreshListener(new OrderRefreshListener());
    }

    public class OrderRefreshListener implements SwipeRefreshLayout.OnRefreshListener{
        @Override
        public void onRefresh() {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
