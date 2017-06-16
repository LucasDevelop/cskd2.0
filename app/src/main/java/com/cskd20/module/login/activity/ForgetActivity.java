package com.cskd20.module.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cskd20.R;
import com.cskd20.base.BaseActivity;
import com.cskd20.bean.SendCodeBean;
import com.cskd20.utils.CommonUtil;
import com.cskd20.utils.ResponseUtil;
import com.google.gson.JsonObject;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 忘记密码
 */
public class ForgetActivity extends BaseActivity {

    int[]    icons  = {R.mipmap.forget_phone, R.mipmap.forget_code, R.mipmap.forget_password};
    String[] text1s = {"输入手机号", "输入验证码", "输入登录密码"};
    String[] text2s = {"", "请输入13464的短信4位验证码", "请输入密码6-16位"};

    int currentPage = 0;
    @Bind(R.id.next)
    Button   mNext;
    @Bind(R.id.edit)
    EditText mEditText;

    @Bind(R.id.top_img)
    ImageView mTopImg;
    @Bind(R.id.text1)
    TextView  mText1;
    @Bind(R.id.text2)
    TextView  mText2;
    private int    mPage;
    private Intent mIntent;


    @Override
    protected int setContentView() {
        mIntent = getIntent();
        mPage = mIntent.getIntExtra("page", 0);
        return R.layout.activity_forget_setup1;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mTopImg.setImageResource(icons[mPage]);
        mText1.setText(text1s[mPage]);
        if (mPage==2)
            mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        if (mPage == 1) {
            String phone = mIntent.getStringExtra("phone");
            String s = phone.substring(0,3) + "***" + phone.substring(phone.length() - 4);
            mText2.setText(String.format(getResources().getString(R.string._188_1154_4), s));
            Log.d("lucas", s);
        } else
            mText2.setText(text2s[mPage]);
        if (mPage == 2)
            mNext.setText("完成");
    }

    @OnClick({R.id.next, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.next:
                submit();
                break;
        }
    }

    private void submit() {
        switch (mPage) {
            case 0:
                sendCode();
                break;
            case 1:
                checkCode();
                break;
            case 2:
                midfPwd();
                break;
        }
    }

    //修改密码
    private void midfPwd() {
        String check = check();
        if (check == null)
            return;
        if (!mEditText.getText().toString().trim().matches("[A-Za-z0-9]{6,20}")) {
            Toast.makeText(mContext, "请输入6-20位包含数字和字母的密码", Toast.LENGTH_SHORT).show();
            return;
        }
        mApi.modifPwd(mIntent.getStringExtra("phone"), check).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (ResponseUtil.getStatus(response.body()) == 1) {
                    Intent intent = new Intent(ForgetActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                Toast.makeText(mContext, ResponseUtil.getMsg(response.body()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    //校验验证码
    private void checkCode() {
        String phone = mIntent.getStringExtra("phone");
        String check = check();
        if (check == null)
            return;
        mApi.checkCode(phone, check, 1).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (ResponseUtil.getStatus(response.body()) == 1) {
                    Intent intent = new Intent(ForgetActivity.this, ForgetActivity.class);
                    intent.putExtra("page", 2);
                    intent.putExtra("phone", getIntent().getStringExtra("phone"));
                    startActivity(intent);
                }
                Toast.makeText(mContext, ResponseUtil.getMsg(response.body()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    //发送验证码
    private void sendCode() {
        final String check = check();
        if (check == null)
            return;
        mApi.sendCode(check).enqueue(new Callback<SendCodeBean>() {
            @Override
            public void onResponse(Call<SendCodeBean> call, Response<SendCodeBean> response) {
                if (response.body().status == 1) {
                    Intent intent = new Intent(ForgetActivity.this, ForgetActivity.class);
                    intent.putExtra("page", 1);
                    intent.putExtra("phone", check);
                    startActivity(intent);
                }
                Toast.makeText(mContext, response.body().msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SendCodeBean> call, Throwable t) {
                Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String check() {
        String msg = null;
        String mobiles = mEditText.getText().toString().trim();
        switch (mPage) {
            case 0:
                if (!CommonUtil.isMobileNO(mobiles))
                    msg = "手机号格式不正确";
                break;
            case 1:
                if (TextUtils.isEmpty(mobiles) || mobiles.length() != 4)
                    msg = "验证码格式不正确";
                break;
            case 2:
                if (TextUtils.isEmpty(mobiles) || mobiles.length() < 6)
                    msg = "密码格式不正确";
                break;
        }
        if (msg == null)
            return mobiles;
        else {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
