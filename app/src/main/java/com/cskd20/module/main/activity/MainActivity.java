package com.cskd20.module.main.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.cskd20.App;
import com.cskd20.R;
import com.cskd20.base.BaseActivity;
import com.cskd20.base.BaseFragment;
import com.cskd20.bean.LoginBean;
import com.cskd20.bean.VersionBean;
import com.cskd20.factory.CallBack;
import com.cskd20.factory.CommonFactory;
import com.cskd20.module.login.activity.LoginActivity;
import com.cskd20.module.login.activity.RegisterSetup1Activity;
import com.cskd20.module.login.activity.RegisterSetup3Activity;
import com.cskd20.module.main.adapter.MainFragmentAdapter;
import com.cskd20.module.main.fragment.MainFragment;
import com.cskd20.module.main.server.DownloadServer;
import com.cskd20.module.main.server.LocationService;
import com.cskd20.module.personal.fragment.PersonalFragment;
import com.cskd20.utils.CommonUtil;
import com.cskd20.utils.ResponseUtil;
import com.cskd20.utils.SPUtils;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 主页框架
 */
public class MainActivity extends BaseActivity {
    @Bind(R.id.tab)
    TabLayout mTab;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    private SweetAlertDialog            mSweetAlertDialog;
    private DownloadServer.DownloadBind mBind;
    private OnActivityResultListener    mListener;

    @Override
    protected int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        show();
        autoLogin();
        EventBus.getDefault().register(this);
        ArrayList<BaseFragment> fragments = new ArrayList<>();
        fragments.add(new PersonalFragment());
        fragments.add(new MainFragment());
        //        fragments.add(new ServerFragment());
        ArrayList<String> titles = new ArrayList<>();
        titles.add("我的");
        titles.add("首页");
        //        titles.add("服务站");
        MainFragmentAdapter fragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager(), fragments,
                titles);
        mTab.addTab(mTab.newTab().setText(titles.get(0)));
        mTab.addTab(mTab.newTab().setText(titles.get(1)));
        mTab.setTabMode(TabLayout.MODE_FIXED);
        mViewPager.setAdapter(fragmentAdapter);
        mTab.setupWithViewPager(mViewPager);

        //启动定位服务
        startService(new Intent(this, LocationService.class));
        //默认选中首页
        mTab.getTabAt(1).select();

        //开启更新服务
        bindService(new Intent(mContext, DownloadServer.class), mConn, BIND_AUTO_CREATE);
    }

    private void checkVersion() {
        //检查版本
        String versionName = CommonUtil.getVersionName(getApplicationContext());
        Log.d("lucas", versionName);
        mApi.checkVersion(versionName, 3).enqueue(new CallBack<JsonObject>() {
            @Override
            public boolean onResponse1(Call<JsonObject> call, Response<JsonObject> response) {
                if (ResponseUtil.getStatus(response.body()) == 1) {
                    VersionBean versionBean = mGson.fromJson(response.body().toString(), VersionBean.class);
                    showUpdateVersionDialog(versionBean);
                }
                return false;
            }

            @Override
            public void onFailure1(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBind = (DownloadServer.DownloadBind) service;
            mBind.setOnDownloadListener(mDownloadListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    DownloadServer.OnDownloadListener mDownloadListener = new DownloadServer.OnDownloadListener() {
        @Override
        public void onProgressUpdate(int progress) {
        }

        @Override
        public void onStart() {
        }

        @Override
        public void onComplete(File apkFile) {
            Log.d("lucas", apkFile.getAbsolutePath());
            Log.d("lucas", "apkFile.length():" + apkFile.length());
            //安装APk
            CommonUtil.installApp(mContext, apkFile);
        }

        @Override
        public void onError(Exception e) {
        }
    };

    private void showUpdateVersionDialog(final VersionBean bean) {
        if (mSweetAlertDialog == null)
            mSweetAlertDialog = new SweetAlertDialog(this)
                    .setTitleText("提示")
                    .setContentText("发现新版本，是否立即更新？")
                    .setCancelText("取消")
                    .setConfirmText("后台更新")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.cancel();
                            if (mBind != null)
                                mBind.start(bean.data.url);
                        }
                    });
        if (!isFinishing())
            mSweetAlertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mListener.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setOnActivityResultListener(OnActivityResultListener listener) {
        mListener = listener;
    }


    public interface OnActivityResultListener {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //自动登录
    private void autoLogin() {
        boolean isAutoLogin = getIntent().getBooleanExtra("isAutoLogin", true);
        if (!isAutoLogin){
            hide();
            //检查版本
            checkVersion();
            return;
        }
        String deviceToken = App.getDeviceToken();
        final String token = (String) SPUtils.get(mContext, "token", "");
        if (TextUtils.isEmpty(token)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        final String username = (String) SPUtils.get(mContext, "phone", "");
        final String pwd = (String) SPUtils.get(mContext, "pwd", "");
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
                            checkAuth(Integer.parseInt(loginBean.data.status));
                            //检查版本
                            checkVersion();
                        } else {
                            //登录失败，显示错误
                            Toast.makeText(MainActivity.this, loginBean.msg, Toast.LENGTH_SHORT).show();
                            SPUtils.put(mContext,"token","");
                            startActivity(new Intent(mContext,LoginActivity.class));
                            finish();
                        }
                        return true;
                    }

                    @Override
                    public void onFailure1(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
                        hide();
                    }
                });
    }

    //检查是否认证
    private void checkAuth(int status) {
        switch (status) {
            case 0://审核中
                Intent intent = new Intent(mContext, RegisterSetup3Activity.class);
                intent.putExtra("status",true);
                startActivity(intent);
                finish();
                break;
            case 1://通过
                break;
            case 2://未通过
                Intent intent1 = new Intent(mContext, RegisterSetup1Activity.class);
                String token = (String) SPUtils.get(mContext, "token", "");
                intent1.putExtra("token",token);
                startActivity(intent1);
                finish();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String action) {
        if ("outLogin".equals(action)) {
            hide();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            exit();
        }
    }
}
