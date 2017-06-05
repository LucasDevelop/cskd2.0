package com.cskd20.module.main.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.cskd20.R;
import com.cskd20.base.BaseActivity;
import com.cskd20.module.main.utils.LocationUtil;

import butterknife.Bind;

/**
 * @创建者 lucas
 * @创建时间 2017/6/3 0003 16:36
 * @描述 地图导航
 */

public class MapNavActivity extends BaseActivity implements AMapLocationListener, AMap.OnMapLoadedListener {
    @Bind(R.id.map)
    MapView mMapView;
    private AMap          mMap;
    private LocationUtil  mLocationUtil;
    private MarkerOptions mDriverMK;//司机地图上的位置
    private LatLng mDriverPoint = null;//司机的地理位置


    @Override
    protected int setContentView() {
        return R.layout.activity_map_nav;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        initMap();
    }

    private void initMap() {
        if (mMap == null)
            mMap = mMapView.getMap();
        //启动定位
        mLocationUtil = new LocationUtil(this);
        mLocationUtil.setLocationChangeListener(this);
        mMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        mLocationUtil.startLocation();
        //标记乘客的起点
        //标记乘客的终点

    }

    //标记司机的位子
    private void addDriverMarker(LatLng latLng) {
        mDriverMK = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_car))
                .position(latLng)
                .draggable(true);
        mMap.clear();
        mMap.addMarker(mDriverMK);
        mMap.invalidate();//刷新地图
        moveMap(latLng);//移动中心点
    }

    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location != null) {
            mDriverPoint = new LatLng(location.getLatitude(), location.getLongitude());
            Log.d("lucas", "定位成功");
            //标记司机的位子
            addDriverMarker(mDriverPoint);
            Log.d("MapNavActivity", "mDriverPoint:" + mDriverPoint);
        }
    }

    /**
     * 监听amap地图加载成功事件回调
     */
    @Override
    public void onMapLoaded() {
        // 设置所有maker显示在当前可视区域地图中
    }

    private void moveMap(LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                latLng, 18, 30, 30)));
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

}
