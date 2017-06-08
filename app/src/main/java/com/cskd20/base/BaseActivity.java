package com.cskd20.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.cskd20.R;
import com.cskd20.api.ApiService;
import com.cskd20.factory.CommonFactory;
import com.cskd20.imp.LoadingImp;
import com.cskd20.popup.LoadingPop;
import com.google.gson.Gson;
import com.umeng.message.PushAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;

import static anet.channel.util.Utils.context;

/**
 * @创建者 lucas
 * @创建时间 2017/5/22 0022 16:39
 * @描述 基类
 */

public abstract class BaseActivity extends AppCompatActivity implements LoadingImp {

    protected Context mContext;
    private static ArrayList<BaseActivity> mActivities = new ArrayList<>();
    public         ApiService              mApi        = CommonFactory.getApiInstance();
    public         Gson                    mGson       = CommonFactory.getGsonInstance();
    private LoadingPop mLoadingPop;
    private View       mRootView;
    public Handler mHandler = new Handler();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(context).onAppStart();
        mContext = getApplicationContext();
        setContentView(setContentView());
        ButterKnife.bind(this);
        initCommon();
        initView(savedInstanceState);
        initEvent();
    }

    protected abstract int setContentView();

    protected abstract void initView(@Nullable Bundle savedInstanceState);

    protected void initEvent() {
    }

    /**
     * 通用初始化
     */
    private void initCommon() {
        //设置横屏模式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //获取全局的根布局
        //        ViewGroup view = (ViewGroup) findViewById(android.R.id.content);
        mRootView = findViewById(R.id.root);
        //        //给所有的按钮添加点击事件
        //        UIUtils.setViewClickListener(view, this);
        //将活动页面添加到容器中

        if (!mActivities.contains(this)) {
            mActivities.add(this);
        }

        mLoadingPop = new LoadingPop(this);

    }

    protected void exit() {
        for (int i = 0; i < mActivities.size(); i++) {
            mActivities.get(i).finish();
        }
    }

    @Override
    public void show() {
        if (mLoadingPop != null && !mLoadingPop.isShowing()) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLoadingPop.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
                }
            }, 100);
        }
    }

    @Override
    public void hide() {
        if (mLoadingPop != null && mLoadingPop.isShowing())
            mLoadingPop.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivities.remove(this);
        ButterKnife.unbind(this);
    }

    private long mExitTime;

    /**
     * 按两次退出应用
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mActivities.size() == 1) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {
                    finish();
                    System.exit(0);
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
