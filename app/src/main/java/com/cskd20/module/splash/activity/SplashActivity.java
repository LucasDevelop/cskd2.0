package com.cskd20.module.splash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cskd20.R;
import com.cskd20.base.BaseActivity;
import com.cskd20.module.login.activity.LoginActivity;
import com.cskd20.utils.SPUtils;

public class SplashActivity extends BaseActivity {

    @Override
    protected int setContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        //延时2S进入主页
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isFirst = (boolean) SPUtils.get(mContext, "isFirst", true);
                if (isFirst) {
                    startActivity(new Intent(mContext, SetupActivity.class));
                    SPUtils.put(mContext, "isFirst", false);
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
                finish();
            }
        }, 2000);
    }
}
