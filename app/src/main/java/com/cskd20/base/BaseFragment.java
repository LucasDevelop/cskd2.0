package com.cskd20.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * @创建者 lucas
 * @创建时间 2017/5/26 0026 9:58
 * @描述 fragment基类
 */

public abstract class BaseFragment extends Fragment {
    private View mLayoutID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = getLayoutView();
        ButterKnife.bind(this,view);
        initView(inflater, container, savedInstanceState);
        return view;
    }

    public abstract void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract View getLayoutView() ;
}
