package com.cskd20;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.cskd20.bean.LoginBean;
import com.cskd20.module.login.activity.LoginActivity;
import com.cskd20.utils.Constants;
import com.cskd20.utils.SPUtils;

/**
 * @创建者 lucas
 * @创建时间 2017/5/26 0026 16:17
 * @描述 程序入口
 */

public class App extends Application {

    private static Context mContext;

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

    private static LoginBean user;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();

        //初始化
        //初始化接单模式
        SPUtils.put(mContext, Constants.AUTO_ORDER,"0");
        SPUtils.put(mContext,Constants.ORDER_TYPE,"[1]");
    }

    public static Context getContext(){
        return mContext;
    }
}
