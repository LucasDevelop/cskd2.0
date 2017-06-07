package com.cskd20.module.login.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cskd20.R;
import com.cskd20.base.BaseActivity;

import butterknife.OnClick;

public class RegisterSetup3Activity extends BaseActivity {

    @Override
    protected int setContentView() {
        return R.layout.activity_register_setup3;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

    }

    @OnClick(R.id.exit)
    public void onClick(View view){
        exit();
    }
}
