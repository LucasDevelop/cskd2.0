package com.cskd20.module.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cskd20.App;
import com.cskd20.R;
import com.cskd20.base.BaseActivity;
import com.cskd20.bean.CheckPhoneBean;
import com.cskd20.bean.LoginBean;
import com.cskd20.factory.CallBack;
import com.cskd20.factory.CommonFactory;
import com.cskd20.module.main.activity.MainActivity;
import com.cskd20.module.main.server.SpeechServer;
import com.cskd20.utils.CommonUtil;
import com.cskd20.utils.ResponseUtil;
import com.cskd20.utils.SPUtils;
import com.google.gson.JsonObject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.login_username)
    EditText mUsername;
    @Bind(R.id.login_pwd)
    EditText mPwd;
    @Bind(R.id.alert)
    TextView mAlert;
    private String IS_REMEMBER = "isRemember";


    @Override
    protected int setContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        String token = (String) SPUtils.get(mContext, "token", "");
        if (!TextUtils.isEmpty(token)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @OnTextChanged(R.id.login_username)
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String phone = s.toString();
        if (CommonUtil.isMobileNO(phone)) {
            //检查手机号是否注册
            mApi.checkPhone(phone, 1 + "").enqueue(new Callback<CheckPhoneBean>() {
                @Override
                public void onResponse(Call<CheckPhoneBean> call, Response<CheckPhoneBean> response) {
                    if (response.body().status == 0) {
                        Log.d("lucas", "改手机号未注册");
                        mAlert.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<CheckPhoneBean> call, Throwable t) {
                }
            });
        }
        mAlert.setVisibility(View.GONE);
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
        show();
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
        autoLogin(username, pwd);

    }

    private void autoLogin(final String username, final String pwd) {
        String deviceToken = App.getDeviceToken();
        CommonFactory.getApiInstance().login(username, pwd, deviceToken, 1)
                .enqueue(new CallBack<JsonObject>() {
                    @Override
                    public boolean onResponse1(Call<JsonObject> call, Response<JsonObject> response) {
                        hide();
                        String json = response.body().toString();
                        if (ResponseUtil.getStatus(response.body()) == 0)
                            return true;
                        LoginBean loginBean = CommonFactory.getGsonInstance().fromJson(json,
                                LoginBean.class);
                        if (loginBean.status == 1) {
                            SPUtils.put(mContext, "token", loginBean.data.token);
                            //保存手机号和密码
                            SPUtils.put(mContext, "phone", username);
                            SPUtils.put(mContext, "pwd", pwd);
                            //保存个人信息
                            App.setUser(loginBean);
                            //检查是否认证
                            checkAuth(Integer.parseInt(loginBean.data.status));
                            //进入主页
                            OpenMain();
                            new SpeechServer(mContext).startSpeek(getResources().getString(R.string.welcome));
                        } else {
                            //登录失败，显示错误
                            Toast.makeText(LoginActivity.this, loginBean.msg, Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }

                    @Override
                    public void onFailure1(Call<JsonObject> call, Throwable t) {
                        hide();
                        Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //检查是否认证
    private void checkAuth(int status) {
        switch (status) {
            case 0://审核中
                startActivity(new Intent(mContext, RegisterSetup3Activity.class));
                finish();
                break;
            case 1://通过
                break;
            case 2://未通过
                Intent intent = new Intent(mContext, RegisterSetup1Activity.class);
                String token = (String) SPUtils.get(mContext, "token", "");
                intent.putExtra("token", token);
                startActivity(intent);
                finish();
                break;
        }
    }


    //打开主页
    private void OpenMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("isAutoLogin", false);
        startActivity(intent);
        finish();
    }
}
