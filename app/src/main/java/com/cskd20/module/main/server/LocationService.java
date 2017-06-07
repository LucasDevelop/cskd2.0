package com.cskd20.module.main.server;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.model.LatLng;
import com.cskd20.module.main.utils.LocationUtil;
import com.cskd20.utils.LogUtils;

/**
 * 定位服务
 */
public class LocationService extends Service implements AMapLocationListener {
    private LocationUtil mLocationUtil;

    public LocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        mLocationUtil = new LocationUtil(this);
        mLocationUtil.setLocationChangeListener(this);
        LogUtils.d("定位服务已开启");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new LocationBind();
    }

    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location != null) {
            LatLng mDriverPoint = new LatLng(location.getLatitude(), location.getLongitude());
            Log.d("lucas", "定位成功");
            Log.d("lucas", "mPoint:" + mDriverPoint);
            if (mListener != null)
                mListener.onLocationChanged(location);
        }
    }

    private LocationChangeListener mListener;

    public class LocationBind extends Binder {

        public void start() {
            mLocationUtil.startLocation();
        }

        public void stop() {
            mLocationUtil.stopLocation();
        }

        public void setOnceLocation(boolean once) {
            AMapLocationClientOption option = mLocationUtil.getDefaultOption();
            option.setOnceLocation(once);
            mLocationUtil.resetOption(option);
        }

        public void setOnLocationListener(LocationChangeListener listener) {
            mListener = listener;
        }

        public LocationUtil getLocationUtil() {
            return mLocationUtil;
        }
    }

    public interface LocationChangeListener {
        void onLocationChanged(Location location);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        LogUtils.d("定位服务已停止");
    }
}
