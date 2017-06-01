package com.cskd20.module.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cskd20.base.BaseFragment;

/**
 * @创建者 lucas
 * @创建时间 2017/5/26 0026 10:05
 * @描述 主页
 */

public class MainFragment extends BaseFragment {
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView view = new TextView(getActivity());
        view.setText("main");
        view.setTextColor(0xf00);
        return view;
    }
}
