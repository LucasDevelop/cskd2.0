package com.cskd20.module.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cskd20.base.BaseFragment;

/**
 * @创建者 lucas
 * @创建时间 2017/6/2 0002 9:34
 * @描述 服务站
 */

public class ServerFragment extends BaseFragment {
    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }

    @Override
    public View getLayoutView() {
        TextView textView = new TextView(getActivity());
        textView.setText("服务扎");
        return textView;
    }
}
