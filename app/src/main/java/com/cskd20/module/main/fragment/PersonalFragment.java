package com.cskd20.module.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cskd20.base.BaseFragment;

/**
 * @创建者 lucas
 * @创建时间 2017/5/26 0026 10:09
 * @描述 我的  界面
 */

public class PersonalFragment extends BaseFragment {
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView view = new TextView(getActivity());
        view.setText("personal");
        view.setTextColor(0xf00);
        return view;
    }
}
