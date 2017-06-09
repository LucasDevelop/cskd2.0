package com.cskd20;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.cskd20.bean.LoginBean;
import com.cskd20.module.login.activity.LoginActivity;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

/**
 * @创建者 lucas
 * @创建时间 2017/5/26 0026 16:17
 * @描述 程序入口
 */

public class App extends Application {

    private static Context mContext;


    private static String mDeviceToken;


    private static LoginBean user;

    public static String getDeviceToken() {
        return mDeviceToken;
    }
    public static LoginBean getUser() {
        return user;
    }
    //未登录
    public static LoginBean getUserExit() {
        if (user==null){
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
            Toast.makeText(mContext, "用户未登录", Toast.LENGTH_SHORT).show();
        }
        return user;
    }

    public static void setUser(LoginBean user1) {
        user = user1;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();

        //初始化讯飞语音
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=593685fe");

        //初始化友盟推送
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //是否显示通知
        mPushAgent.setNotificaitonOnForeground(false);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                mDeviceToken = deviceToken;
                Log.d("lucas","deviceToken:"+ mDeviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });

    }

    public static Context getContext(){
        return mContext;
    }


}
