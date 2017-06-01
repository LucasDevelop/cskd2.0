package com.cskd20.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @创建者 lucas
 * @创建时间 2017/5/26 0026 9:58
 * @描述 fragment基类
 */

public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return initView(inflater,container,savedInstanceState);
    }

    public abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
}
