package com.cskd20.module.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cskd20.R;
import com.cskd20.base.BaseActivity;
import com.cskd20.module.login.activity.LoginActivity;
import com.cskd20.utils.SPUtils;
import com.kyleduo.switchbutton.SwitchButton;

import butterknife.Bind;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {
    @Bind(R.id.speck)
    SwitchButton mSpeck;

    @Override
    protected int setContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
    }

    @OnClick({R.id.back,R.id.complete,R.id.exit})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.complete:
                save();
                break;
            case R.id.exit:
                SPUtils.put(mContext,"token","");
                startActivity(new Intent(mContext, LoginActivity.class));
                finish();
                exit();
                break;
        }
    }

    private void save() {
        SPUtils.put(mContext,"isSpeck",mSpeck.isChecked());
        finish();
    }
}
