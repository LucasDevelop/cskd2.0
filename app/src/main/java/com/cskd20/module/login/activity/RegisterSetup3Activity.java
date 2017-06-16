package com.cskd20.module.login.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.cskd20.R;
import com.cskd20.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class RegisterSetup3Activity extends BaseActivity {

    @Bind(R.id.msg)
    TextView mMsg;

    @Override
    protected int setContentView() {
        return R.layout.activity_register_setup3;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        boolean status = getIntent().getBooleanExtra("status", false);
        if (status)
            mMsg.setText("您的账号正在审核中...");
    }

    @OnClick(R.id.exit)
    public void onClick(View view){
        exit();
    }
}
