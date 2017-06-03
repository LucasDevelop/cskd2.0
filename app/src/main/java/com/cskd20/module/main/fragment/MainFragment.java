package com.cskd20.module.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cskd20.R;
import com.cskd20.base.BaseFragment;
import com.cskd20.module.main.activity.MapNavActivity;
import com.cskd20.module.main.activity.ModeSettingActivity;
import com.cskd20.view.RippleButton;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @创建者 lucas
 * @创建时间 2017/5/26 0026 10:05
 * @描述 主页
 */

public class MainFragment extends BaseFragment {
    @Bind(R.id.modelSetting)
    ImageView mModelSetting;
    @Bind(R.id.order)
    ImageView mOrder;
    @Bind(R.id.search)
    RippleButton mSearch;


    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSearch.setOnclickListener1(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearch();
            }
        });
    }


    private void startSearch() {
        startActivity(new Intent(getActivity(), MapNavActivity.class));
    }

    @Override
    public View getLayoutView() {
        return View.inflate(getActivity(),R.layout.fragment_main,null);
    }

    @OnClick({R.id.modelSetting,R.id.order})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.modelSetting:
                startActivity(new Intent(getActivity(), ModeSettingActivity.class));
                break;
            case R.id.order:
                break;
        }
    }
}
