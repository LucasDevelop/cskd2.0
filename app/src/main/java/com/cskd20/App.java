package com.cskd20;

import android.app.Application;
import android.content.Context;

/**
 * @创建者 lucas
 * @创建时间 2017/5/26 0026 16:17
 * @描述 程序入口
 */

public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }
}
