package com.cskd20.module.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cskd20.App;
import com.cskd20.R;
import com.cskd20.base.BaseActivity;
import com.cskd20.bean.LoginBean;
import com.cskd20.factory.CallBack;
import com.cskd20.factory.CommonFactory;
import com.cskd20.module.main.activity.MainActivity;
import com.cskd20.utils.SPUtils;
import com.google.gson.JsonObject;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.login_username)
    EditText mUsername;
    @Bind(R.id.login_pwd)
    EditText mPwd;
    private String IS_REMEMBER = "isRemember";


    @Override
    protected int setContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
    }

    @OnClick({R.id.login_submit, R.id.register, R.id.forget_pwd})
    public void onClick(View v) {
        switch (v.getId()) {
            //提交登录
            case R.id.login_submit:
//                OpenMain();
                login();
                break;
            case R.id.register:
                startActivity(new Intent(this, RegisterSetup0Activity.class));
                break;
            case R.id.forget_pwd:
                startActivity(new Intent(this, ForgetActivity.class));
                break;
            default:
                break;
        }
    }

    private void login() {
        final String username = mUsername.getText().toString().trim();
        final String pwd = mPwd.getText().toString().trim();
        //普通验证
        if (username.isEmpty() || pwd.isEmpty()) {
            Toast.makeText(this, "账号或者密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwd.length() < 6 && pwd.length() > 16) {
            Toast.makeText(this, "请输入6-16位的密码", Toast.LENGTH_SHORT).show();
            return;
        }
        try {

            String deviceToken = App.getDeviceToken();
            CommonFactory.getApiInstance().login(username, pwd, deviceToken)
                .enqueue(new CallBack<JsonObject>() {
                    @Override
                    public boolean onResponse1(Call<JsonObject> call, Response<JsonObject> response) {
                        LoginBean loginBean = CommonFactory.getGsonInstance().fromJson(response.body().toString(),
                                LoginBean.class);
                        if (loginBean.status == 1) {
                            SPUtils.put(mContext, "token", loginBean.data.token);
                            //保存个人信息
                            App.setUser(loginBean);
                            //进入主页
                            OpenMain();
                        } else {
                            //登录失败，显示错误
                            Toast.makeText(LoginActivity.this, loginBean.msg, Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }

                    @Override
                    public void onFailure1(Call<JsonObject> call, Throwable t) {

                    }
                });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //打开主页
    private void OpenMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
