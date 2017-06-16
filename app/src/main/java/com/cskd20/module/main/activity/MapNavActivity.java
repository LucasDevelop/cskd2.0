package com.cskd20.module.main.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.cskd20.R;
import com.cskd20.base.BaseNaviActivity;
import com.cskd20.bean.OrderBean;
import com.cskd20.factory.CallBack;
import com.cskd20.module.main.server.LocationService;
import com.cskd20.module.main.utils.DrivingRouteOverLay;
import com.cskd20.popup.LoadingPop;
import com.cskd20.utils.AMapUtil;
import com.cskd20.utils.LogUtils;
import com.cskd20.utils.ResponseUtil;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

import static com.taobao.accs.ACCSManager.mContext;

/**
 * @创建者 lucas
 * @创建时间 2017/6/3 0003 16:36
 * @描述 地图导航
 */

public class MapNavActivity extends BaseNaviActivity implements AMap.OnMapLoadedListener, LocationService
        .LocationChangeListener {
    @Bind(R.id.map)
    MapView  mMapView;
    @Bind(R.id.submit)
    Button   mSubmit;
    @Bind(R.id.start_point)
    TextView mStartTV;
    @Bind(R.id.end_point)
    TextView mEndTV;
    @Bind(R.id.name)
    TextView mName;

    private AMap          mMap;
    private MarkerOptions mDriverMK;//司机地图上的位置
    private LatLonPoint mDriverPoint = null;//司机的地理位置22.56904,113.866899
    private LatLonPoint mStartPoint  = new LatLonPoint(22.56910, 113.866888);//起点
    private LatLonPoint mEndPoint    = new LatLonPoint(22.56730, 113.866555);//终点
    private RouteSearch mRouteSearch;
    private       int driverIC         = R.mipmap.map_car;
    private       int startIC          = R.mipmap.map_qidian;
    private       int endIC            = R.mipmap.map_zhongdian;
    private final int ROUTE_TYPE_DRIVE = 2;
    private LoadingPop       mLoadingPop;
    private DriveRouteResult mDriveRouteResult;
    private String           mDrivingToPassTime;//驾驶到乘客位置所需时间
    private String           mDrivingLength;//距离乘客的距离
    private Marker           mDriverMarker;

    public static final int GOTO_PASS       = 1;//准备出发
    public static final int REACH_DES       = 2;//到达乘客位置
    public static final int RECEIVE         = 3;//接到乘客
    public static final int REACH_END_POINT = 4;//到达目的地
    public static final int NORMAL          = 0;//准备出发
    public              int DRIVER_STATUS   = NORMAL;

    public static final int DRIVER     = 100;//司机
    public static final int PASS_START = 101;//乘客

    //定位服务
    private ServiceConnection locationConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLocationBind = (LocationService.LocationBind) service;
            mLocationBind.setOnceLocation(true);
            mLocationBind.setOnLocationListener(MapNavActivity.this);
            mLocationBind.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.d("定位服务绑定失败");
        }
    };
    private LocationService.LocationBind mLocationBind;
    private OrderBean                    mOrderBean;
    private String                       mCity;
    private String                       mReceivePassTime;
    private int                          mStrategy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_nav);
        ButterKnife.bind(this);
        initNavi(savedInstanceState);
        initMap();
        initView(savedInstanceState);
    }

    protected void initView(@Nullable Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        mLoadingPop = new LoadingPop(this);
        showProgressDialog();
        mOrderBean = (OrderBean) getIntent().getSerializableExtra("order");
        if (mOrderBean == null) {
            Log.d("lucas", "未获取到订单详情");
            return;
        }
        mName.setText(mOrderBean.data.username);
        mStartTV.setText(mOrderBean.data.start_place);
        mEndTV.setText(mOrderBean.data.end_place);
        if (mOrderBean != null) {
            mStartPoint.setLatitude(mOrderBean.data.start_latitude);
            mStartPoint.setLongitude(mOrderBean.data.start_longitude);
            mEndPoint.setLatitude(mOrderBean.data.end_latitude);
            mEndPoint.setLongitude(mOrderBean.data.end_longitude);
        } else {
            Toast.makeText(mContext, "未获取到订单信息", Toast.LENGTH_SHORT).show();
        }
    }

    private void initMap() {
        if (mMap == null)
            mMap = mMapView.getMap();
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(new RouteSearchListener());
        mMap.setInfoWindowAdapter(new InfoWindow());
        //绑定定位
        bindService(new Intent(this, LocationService.class), locationConn, BIND_AUTO_CREATE);
        mMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        mMap.setOnMarkerClickListener(null);
    }

    private void initNavi(Bundle savedInstanceState) {
        mAMapNaviView = (AMapNaviView) findViewById(R.id.map_navi);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
        //自定义UI
        setMarkerIc(R.mipmap.map_car, R.mipmap.map_qidian, R.mipmap.map_zhongdian);
    }

    //设置marker  icon
    private void setMarkerIc(int map_car, int map_qidian, int map_zhongdian) {
        AMapNaviViewOptions options = mAMapNaviView.getViewOptions();
        options.setCarBitmap(BitmapFactory.decodeResource(this.getResources(), map_car));
        //        options.setFourCornersBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.lane00));
        options.setStartPointBitmap(BitmapFactory.decodeResource(this.getResources(), map_qidian));
        //        options.setWayPointBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.navi_way));
        options.setEndPointBitmap(BitmapFactory.decodeResource(this.getResources(), map_zhongdian));
        mAMapNaviView.setViewOptions(options);
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        /**
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
         */
        mStrategy = 0;
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            mStrategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.setCarNumber("京", "DFZ588");
        sList.clear();
        eList.clear();
    }

    //开始导航
    private void startNavi(LatLonPoint startPoint, LatLonPoint endPoint) {
        sList.clear();
        eList.clear();
        sList.add(new NaviLatLng(startPoint.getLatitude(), startPoint.getLongitude()));
        eList.add(new NaviLatLng(endPoint.getLatitude(), endPoint.getLongitude()));
        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, mStrategy);
    }

    @Override
    public void onCalculateRouteSuccess() {
        super.onCalculateRouteSuccess();
        mAMapNavi.startNavi(NaviType.GPS);
    }


    //添加一个marker
    private Marker addMK(LatLonPoint point, int id) {
        return mMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(point))
                .icon(BitmapDescriptorFactory.fromResource(id)));
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint, int routeType, int mode) {
        if (mDriverPoint == null) {
            Log.d("lucas", "定位中，稍后再试...");
            return;
        }
        if (mStartPoint == null) {
            Log.d("lucas", "乘客起点未设置");
            return;
        }
        if (mEndPoint == null) {
            Log.d("lucas", "终点未设置");
            return;
        }
        //        mLoadingPop.dismiss();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                startPoint, endPoint);
        if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        }
    }

    private void showProgressDialog() {
        //        mLoadingPop.setTitle("加载中...");
        //        mLoadingPop.show(this);
    }

    //定位成功
    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location != null) {
            mDriverPoint = new LatLonPoint(location.getLatitude(), location.getLongitude());
            //开始绘制司机到乘客起点的路线和marker
            searchRouteResult(mDriverPoint, mStartPoint, ROUTE_TYPE_DRIVE,
                    RouteSearch.DrivingDefault);
            mCity = location.getCity();
        }
    }

    public class InfoWindow implements AMap.InfoWindowAdapter {

        @Override
        public View getInfoWindow(Marker marker) {
            View view = null;
            switch ((int) marker.getObject()) {
                case PASS_START:
                    if (DRIVER_STATUS == NORMAL) {
                        view = View.inflate(getApplicationContext(), R.layout.map_info_window, null);
                        TextView length = (TextView) view.findViewById(R.id.length);
                        TextView time = (TextView) view.findViewById(R.id.time);
                        length.setText(mDrivingLength + "");
                        time.setText(mDrivingToPassTime + "");
                    } else
                        return null;
                    break;
                default:
                    marker.hideInfoWindow();
                    break;
            }
            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    public class RouteSearchListener implements RouteSearch.OnRouteSearchListener {
        @Override
        public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
        }

        @Override
        public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
            mLoadingPop.hide();
            mMap.clear();// 清理地图上的所有覆盖物
            //添加终点marker
            addMK(mEndPoint, R.mipmap.map_zhongdian);
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                if (result != null && result.getPaths() != null) {
                    if (result.getPaths().size() > 0) {
                        mDriveRouteResult = result;
                        final DrivePath drivePath = mDriveRouteResult.getPaths().get(0);
                        DrivingRouteOverLay drivingRouteOverlay = new DrivingRouteOverLay(
                                mContext, mMap, drivePath,
                                mDriveRouteResult.getStartPos(),
                                mDriveRouteResult.getTargetPos(), null);
                        drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                        drivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
                        drivingRouteOverlay.removeFromMap();
                        if (DRIVER_STATUS == NORMAL) {
                            int dis = (int) drivePath.getDistance();
                            int dur = (int) drivePath.getDuration();
                            //时间、距离
                            mDrivingToPassTime = AMapUtil.getFriendlyTime(dur);
                            mDrivingLength = AMapUtil.getFriendlyLength(dis);
                            String des = mDrivingToPassTime + "(" + mDrivingLength + ")";
                            Log.d("lucas", "des:" + des);
                        }
                        drivingRouteOverlay.addToMap(DRIVER_STATUS);
                        drivingRouteOverlay.zoomToSpan();
                        //                        mBottomLayout.setVisibility(View.VISIBLE);
                    } else if (result != null && result.getPaths() == null) {
                        Toast.makeText(mContext, "对不起，没有搜索到相关数据！", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(mContext, "对不起，没有搜索到相关数据！", Toast.LENGTH_SHORT).show();
                }
            } else {
                //                ToastUtil.showerror(this.getApplicationContext(), errorCode);
            }

        }

        @Override
        public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
        }

        @Override
        public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
        }
    }

    @OnClick({R.id.submit, R.id.sms, R.id.phone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                if (DRIVER_STATUS == NORMAL) {
                    gotoPass();//准备出发
                    return;
                }
                if (DRIVER_STATUS == GOTO_PASS) {
                    reachStart(); //到达上车地点
                    return;
                }
                if (DRIVER_STATUS == REACH_DES) {
                    receivePass();//接到乘客
                    return;
                }
                if (DRIVER_STATUS == RECEIVE) {
                    reachEnd();//到达目的地
                    return;
                }
                break;
            case R.id.phone:
                //用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mOrderBean.data.phone));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager
                        .PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
                break;
            case R.id.sms:
                break;
        }
    }

    //准备出发
    private void gotoPass() {
        DRIVER_STATUS = GOTO_PASS;
        mSubmit.setText("到达上车地点");
        //隐藏mapview
        mMapView.setVisibility(View.GONE);
        //显示导航地图
        mAMapNaviView.setVisibility(View.VISIBLE);
        if (mAMapNavi != null)
            startNavi(mDriverPoint, mStartPoint);
    }

    //接到乘客
    private void receivePass() {
        DRIVER_STATUS = RECEIVE;
        mSubmit.setText("到达目的地");
        mSubmit.setBackgroundResource(R.color.org);
        //重新规划路线
        if (mAMapNavi != null) {
            startNavi(mStartPoint, mEndPoint);
        }

        //回传数据到服务器
        mApi.meetPass(mOrderBean.data.order_id).enqueue(new CallBack<JsonObject>() {
            @Override
            public boolean onResponse1(Call<JsonObject> call, Response<JsonObject> response) {
                if (ResponseUtil.getStatus(response.body()) == 1) {
                    Log.d("lcuas", "服务器已收到接到乘客的确认");
                    //记录时间
                    mReceivePassTime = new SimpleDateFormat("yyyyMMddHHmm").format(new Date(System.currentTimeMillis
                            ()));
                    //开始绘制乘客起点到乘客终点的路线和marker
                    searchRouteResult(mStartPoint, mEndPoint, ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
                } else {
                    Log.e("lcuas", "没有收到确认或者司机未登录");
                    return true;
                }
                return false;
            }

            @Override
            public void onFailure1(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    //到达上车地点
    private void reachStart() {
        //        mSubmit.setText("到达上车地点");
        DRIVER_STATUS = REACH_DES;
        mSubmit.setText("接到乘客");
    }

    //到达目的地
    private void reachEnd() {
        DRIVER_STATUS = REACH_END_POINT;
        //进入订单详情
        Intent intent = new Intent(this, OrderInfoActivity.class);
        intent.putExtra("order", mOrderBean);
        intent.putExtra("city", mCity);
        intent.putExtra("time", mReceivePassTime);
        // TODO: 2017/6/8 0008 距离算法保留
        intent.putExtra("km", DrivingRouteOverLay.calculateDistance(
                new LatLng(mStartPoint.getLatitude(), mStartPoint.getLongitude())
                , new LatLng(mEndPoint.getLatitude(), mEndPoint.getLongitude())
        ));
        startActivity(intent);
        finish();
    }

    /**
     * 监听amap地图加载成功事件回调
     */
    @Override
    public void onMapLoaded() {
        // 设置所有maker显示在当前可视区域地图中
    }

    //    private void moveMap(LatLng latLng) {
    //        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
    //                latLng, 18, 30, 30)));
    //    }


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
        if (mLocationBind != null)
            mLocationBind.stop();
        if (mMapView != null)
            mMapView.onDestroy();

        unbindService(locationConn);
        ButterKnife.unbind(this);

    }


}
