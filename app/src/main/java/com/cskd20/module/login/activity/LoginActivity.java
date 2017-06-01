package com.cskd20.module.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cskd20.R;
import com.cskd20.base.BaseActivity;
import com.cskd20.factory.CommonFactory;
import com.cskd20.module.main.activity.MainActivity;
import com.google.gson.JsonObject;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
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
               CommonFactory.getApiInstance().login(username, pwd)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d("lucas","json:"+ response.body().toString());
//                        LoginBean loginBean = CommonFactory.getGsonInstance().fromJson(response.body().toString(),
//                                LoginBean.class);
//                        if (loginBean.getStatus() == 0) {
//                            //登录成功，判断是否记住密码
//                            if ((boolean) SPUtils.get(mContext, IS_REMEMBER, false)) {
//                                SPUtils.put(mContext, "phone", username);
//                                SPUtils.put(mContext, "pwd", pwd);
//                                //进入主页
//                                OpenMain();
//                            }
//                        } else {
//                            //登录失败，显示错误
//                            Toast.makeText(LoginActivity.this, loginBean.getResult(), Toast.LENGTH_SHORT).show();
//                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        //服务器错误或者网络错误
                        Toast.makeText(LoginActivity.this, "服务器没有响应", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.register:
                startActivity(new Intent(this, RegisterSetup0Activity.class));
                break;
            case R.id.forget_pwd:
                break;
            default:
                break;
        }
    }


    //打开主页
    private void OpenMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
