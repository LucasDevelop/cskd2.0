package com.cskd20.module.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cskd20.R;
import com.cskd20.base.BaseActivity;
import com.cskd20.bean.CheckPhoneBean;
import com.cskd20.bean.RegisterBean;
import com.cskd20.bean.SendCodeBean;
import com.cskd20.factory.CommonFactory;
import com.cskd20.utils.CommonUtil;
import com.cskd20.utils.ResponseUtil;
import com.google.gson.JsonObject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @创建者 lucas
 * @创建时间 2017/5/26 0026 17:16
 * @描述 注册界面
 */

public class RegisterSetup0Activity extends BaseActivity {
    @Bind(R.id.login_name)
    EditText mPhone;
    @Bind(R.id.login_pwd)
    EditText mPwd;
    @Bind(R.id.code)
    EditText mCode;
    @Bind(R.id.send_code)
    TextView mSendCode;
    @Bind(R.id.alert)
    TextView mAlert;

    Handler mHandler = new Handler();
    private TimeTask mTimeTask;


    @Override
    protected int setContentView() {
        return R.layout.activity_register_setup0;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
    }

    @OnClick({R.id.send_code, R.id.register})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_code:
                sendCode();
                break;
            case R.id.register:
//                                startActivity(new Intent(mContext,RegisterSetup1Activity.class));
                String phone = checkPhone();
                if (phone == null)
                    return;
                checkPhoneIsRegister(phone);
                break;
            default:
                break;
        }
    }

    @OnTextChanged(R.id.login_name)
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String phone = s.toString();
        if (CommonUtil.isMobileNO(phone)) {
            //检查手机号是否注册
            mApi.checkPhone(phone, 1 + "").enqueue(new Callback<CheckPhoneBean>() {
                @Override
                public void onResponse(Call<CheckPhoneBean> call, Response<CheckPhoneBean> response) {
                    if (response.body().status == 1) {
                        Log.d("lucas", "改手机号已注册");
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

    //检查手机号是否注册
    private void checkPhoneIsRegister(final String phone) {
        mApi.checkPhone(phone, 1 + "").enqueue(new Callback<CheckPhoneBean>() {
            @Override
            public void onResponse(Call<CheckPhoneBean> call, Response<CheckPhoneBean> response) {
                Log.d("lucas", response.body().msg);
                if (response.body().status != 0)
                    Toast.makeText(mContext, response.body().msg, Toast.LENGTH_SHORT).show();
                else
                    register(phone);//注册
            }

            @Override
            public void onFailure(Call<CheckPhoneBean> call, Throwable t) {
                Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //提交注册
    private void register(String phone) {
        //检查验证码
        String code = mCode.getText().toString();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(mContext, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (code.length() != 4) {
            Toast.makeText(mContext, "请输入4位验证码", Toast.LENGTH_SHORT).show();
        }
        //检查密码
        String pwd = mPwd.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pwd.matches("[A-Za-z0-9]{6,20}")) {
            Toast.makeText(mContext, "请输入6-20位包含数字和字母的密码", Toast.LENGTH_SHORT).show();
            return;
        }
        show();
        mApi.register(phone, pwd, code, 0 + "")
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d("lucas", "json:" + response.body().toString());
                        if (ResponseUtil.getStatus(response.body()) == 1) {
                            RegisterBean registerBean = CommonFactory.getGsonInstance().fromJson(response.body()
                                    .toString(), RegisterBean.class);
                            //注册成功跳转到信息完善界面
                            Intent intent = new Intent(mContext, RegisterSetup1Activity.class);
                            intent.putExtra("token",registerBean.data.token);
                            startActivity(intent);
                            finish();
                        } else
                            Toast.makeText(mContext, ResponseUtil.getMsg(response.body()), Toast.LENGTH_SHORT).show();
                        hide();
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        hide();
                        Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public class TimeTask implements Runnable {

        private int mTime;

        @Override
        public void run() {
            if (mTime <= 0) {
                mSendCode.setText("发送验证码");
                //设置可以点击
                mSendCode.setClickable(true);
                stop();
                return;
            }
            mSendCode.setText(mTime-- + " s");
            //设置不可点击
            mSendCode.setClickable(false);
            mHandler.postDelayed(this, 1000);
        }

        public void start() {
            mHandler.post(this);
            mTime = 60;
        }

        public void stop() {
            mHandler.removeCallbacks(this);
        }
    }

    //发送验证码
    private void sendCode() {
        String phone = checkPhone();
        if (phone == null)
            return;
        //开启倒计时
        if (mTimeTask == null)
            mTimeTask = new TimeTask();
        mTimeTask.start();
        //发送验证码
        mApi.sendCode(phone).enqueue(new Callback<SendCodeBean>() {
            @Override
            public void onResponse(Call<SendCodeBean> call, Response<SendCodeBean> response) {
                Toast.makeText(mContext, response.body().msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SendCodeBean> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Nullable
    private String checkPhone() {
        String phone = mPhone.getText().toString();
        //手机号码普通验证
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (!CommonUtil.isMobileNO(phone)) {
            Toast.makeText(mContext, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
            return null;
        }
        return phone;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimeTask != null)
            mTimeTask.stop();
    }
}
