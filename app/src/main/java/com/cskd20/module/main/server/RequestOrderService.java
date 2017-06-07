package com.cskd20.module.main.server;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.amap.api.maps2d.model.LatLng;
import com.cskd20.App;
import com.cskd20.factory.CallBack;
import com.cskd20.factory.CommonFactory;
import com.cskd20.utils.Constants;
import com.cskd20.utils.LogUtils;
import com.cskd20.utils.SPUtils;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 循环请求订单
 */
public class RequestOrderService extends Service implements LocationService.LocationChangeListener {
    private Context mContext = this;

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLocationBind = (LocationService.LocationBind) service;
            mLocationBind.setOnceLocation(false);
            mLocationBind.setOnLocationListener(RequestOrderService.this);
            boolean isStart = mLocationBind.getLocationUtil().getLocationClient().isStarted();
            if (!isStart) {
                mLocationBind.start();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private LocationService.LocationBind mLocationBind;
    private LatLng                       mDriverLocation;
    private Call<JsonObject>             mCall;
    private TimeTask                     mTimeTask;
    private RequestOrderBind             mBind;

    public RequestOrderService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //绑定定位服务
        bindService(new Intent(this, LocationService.class), mConn, BIND_AUTO_CREATE);
        //开启定时任务
        mTimeTask = new TimeTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        LogUtils.d("循环请求订单任务取消");
    }

    @Override
    public IBinder onBind(Intent intent) {
        mBind = new RequestOrderBind();
        return mBind;
    }

    public class RequestOrderBind extends Binder {
        public RequestOrderListener mListener;

        /**
         * 开始任务---请求订单
         *
         * @param isReset 是否重置条件
         */
        public void startRequest(boolean isReset) {
            startAutoRequest(isReset);
            Log.d("lucas", "开启接单任务");
        }

        public void stopRequest() {
            mTimeTask.stopTask();
            Log.d("lucas", "停止接单任务");
        }

        public void setOrderListener(RequestOrderListener listener) {
            mListener = listener;
        }
    }


    //循环请求
    private void startAutoRequest(boolean isReset) {
        if (mCall == null || isReset) {
            mHandler.sendEmptyMessage(REQUEST_ORDER);
        }
    }

    static final int REQUEST_ORDER = 0x01;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int type = msg.what;
            switch (type) {
                case REQUEST_ORDER:
                    if (mDriverLocation == null) {
                        LogUtils.d("没有获取到司机的地理位置");
                        //延时请求，直到获取到司机的位置
                        Message message = mHandler.obtainMessage();
                        message.what = REQUEST_ORDER;
                        mHandler.sendMessageDelayed(message, 200);
                    }
                    //开始任务
                    if (mTimeTask != null && !mTimeTask.isStart())
                        mTimeTask.startTask();
                    break;
            }
        }
    };

    //心跳
    public class TimeTask implements Runnable {
        int count;

        public boolean isStart() {
            return mIsStart;
        }

        private boolean mIsStart;

        @Override
        public void run() {
            mIsStart = true;
            String lng = mDriverLocation.longitude + "";
            String lat = mDriverLocation.latitude + "";
            String orderType = (String) SPUtils.get(mContext, Constants.ORDER_TYPE, "");
            String autoOrder = (String) SPUtils.get(mContext, Constants.AUTO_ORDER, "");
            String id = App.getUser().data.id;
            //            mCall = CommonFactory.getApiInstance().getOrder(id, lng, lat, autoOrder, orderType, "");
            mCall = CommonFactory.getApiInstance().getOrder("1", "113.9865709127455", "22.46598997406529", "0", "[1," +
                    "3,5]", "");
            mCall.enqueue(new CallBack<JsonObject>() {
                @Override
                public boolean onResponse1(Call<JsonObject> call, Response<JsonObject> response) {
                    if (mBind != null && mBind.mListener != null)
                        mBind.mListener.onResponse1(call, response);
                    return false;
                }

                @Override
                public void onFailure1(Call<JsonObject> call, Throwable t) {
                    if (mBind != null && mBind.mListener != null)
                        mBind.mListener.onFailure1(call, t);
                }
            });
            mHandler.postDelayed(this, Constants.REQUEST_ORDER_RATE);
            Log.d("lucas", "订单请求次数:" + count++);
        }

        public void startTask() {
            mHandler.post(this);
        }

        public void stopTask() {
            mHandler.removeCallbacks(this);
            mCall.cancel();
            mIsStart = false;
        }
    }

    public interface RequestOrderListener {
        void onResponse1(Call<JsonObject> call, Response<JsonObject> response);

        void onFailure1(Call<JsonObject> call, Throwable t);
    }

    @Override
    public void onLocationChanged(Location location) {
        mDriverLocation = new LatLng(location.getLatitude(), location.getLongitude());
        LogUtils.d("司机定位成功");
    }
}
