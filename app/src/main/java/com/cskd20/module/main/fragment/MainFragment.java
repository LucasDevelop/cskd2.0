package com.cskd20.module.main.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.cskd20.R;
import com.cskd20.base.BaseFragment;
import com.cskd20.bean.OrderBean;
import com.cskd20.module.main.activity.MapNavActivity;
import com.cskd20.module.main.activity.ModeSettingActivity;
import com.cskd20.module.main.event.MessageEvent;
import com.cskd20.module.main.server.RequestOrderService;
import com.cskd20.module.main.server.SpeechServer;
import com.cskd20.popup.OrderPopup;
import com.cskd20.utils.Constants;
import com.cskd20.utils.SPUtils;
import com.cskd20.view.RippleButton;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @创建者 lucas
 * @创建时间 2017/5/26 0026 10:05
 * @描述 主页
 */

public class MainFragment extends BaseFragment implements OrderPopup.OnCloseListener {
    @Bind(R.id.modelSetting)
    ImageView    mModelSetting;
    @Bind(R.id.order)
    ImageView    mOrder;
    @Bind(R.id.search)
    RippleButton mSearch;
    @Bind(R.id.currentModel)
    TextView     mCurrenModel;
    @Bind(R.id.startText)
    TextView     mStartText;
    private RequestOrderService.RequestOrderBind mOrderBind;
    private OrderPopup                           mPopup;
    private static final int TAKING        = 1;//请求订单
    private static final int NORMAL        = 0;//待定状态
    private static final int RECEIVE_ORDER = 2;//收到订单
    private static       int currentStatus = NORMAL;//当前状态
    private OrderBean    mOrderBean;
    private SpeechServer mSpeechServer;
    private String       mModel;//接单模式


    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //播放欢迎语音
//       SpeechServer.getInstance(getActivity()).startSpeek("");

        //设置接单模式--默认手动接单
        mModel = (String) SPUtils.get(getActivity(), Constants.AUTO_ORDER, "0");
        //启动自动请求订单服务
        getActivity().bindService(new Intent(getActivity(), RequestOrderService.class), mConn, Context
                .BIND_AUTO_CREATE);
        if ("1".equals(mModel)) {
            mCurrenModel.setText("当前为自动接单模式");
            mSearch.setClickable(false);
        } else {
            mCurrenModel.setText("当前为手动接单模式");
            mSearch.setClickable(true);
        }
        //初始化语音播报
        mSpeechServer = new SpeechServer(getActivity());
        mSearch.setOnclickListener1(new View.OnClickListener() {
            int count = 0;

            @Override
            public void onClick(View v) {
                if (currentStatus == RECEIVE_ORDER) {//收到订单
                    //进入地图界面
                    if (mOrderBind != null) {
                        if (mModel.equals("0")) {
                            //手动接单
                            mOrderBind.startGrabOrder(mOrderBean.data.order_id);
                        } else {
                            //自动接单
                            openMap();
                        }
                    }
                } else {
                    if ("出车".equals(mStartText.getText().toString().trim())) {//开始请求订单
                        currentStatus = TAKING;
                        mStartText.setText("接单中");
                        mSearch.start();
                        startSearch();
                    } else {//停止请求订单
                        mStartText.setText("出车");
                        mSearch.stop();
                        currentStatus = NORMAL;
                        mOrderBind.stopRequest();
                    }
                    count++;
                }
            }
        });

    }

    private void openMap() {
        Intent intent = new Intent(getActivity(), MapNavActivity.class);
        intent.putExtra("order", mOrderBean);
        startActivity(intent);
        mPopup.dismiss();
        mSearch.stop();
        mStartText.setText("出车");
        currentStatus = NORMAL;
    }

    private ServiceConnection                        mConn          = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mOrderBind = (RequestOrderService.RequestOrderBind) service;
            mOrderBind.setOrderListener(mOrderListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    //订单请求回调
    private RequestOrderService.RequestOrderListener mOrderListener = new RequestOrderService.RequestOrderListener() {
        @Override
        public void onResponse1(Call<JsonObject> call, Response<JsonObject> response) {
            mOrderBean = mGson.fromJson(response.body().toString(), OrderBean.class);
            currentStatus = RECEIVE_ORDER;
            if (mModel.equals("0"))
                mStartText.setText("抢单");
            else
                mStartText.setText("接单");
            //停止请求订单
            mOrderBind.stopRequest();
            showWindow(mOrderBean);
            //语音播报
            float distance = AMapUtils.calculateLineDistance(
                    new LatLng(mOrderBean.data.start_latitude, mOrderBean.data.start_longitude),
                    new LatLng(mOrderBean.data.end_latitude, mOrderBean.data.end_longitude));
            mSpeechServer.startSpeek("收到新订单，从" + mOrderBean.data.start_place + "到" +
                    mOrderBean.data.end_place + ",距离您约" + ((int) distance) / 10 * 0.01 + "公里");
        }

        @Override
        public void onFailure1(Call<JsonObject> call, Throwable t) {

        }

        //抢订单回调
        @Override
        public void onGrabResponse(Call<JsonObject> call, Response<JsonObject> response) {
            Toast.makeText(getActivity(), "成功接单", Toast.LENGTH_SHORT).show();
            mSpeechServer.startSpeek("成功接单");
            openMap();
        }
    };

    //弹出订单信息
    private void showWindow(OrderBean orderBean) {
        if (mPopup == null) {
            mPopup = new OrderPopup(getActivity());
            mPopup.setOnCloseListener(this);
        }
        mPopup.setDate(orderBean);
        mPopup.show(getActivity());
    }

    //开始请求订单
    private void startSearch() {
        mOrderBind.startRequest(true);
    }

    @Override
    public View getLayoutView() {
        return View.inflate(getActivity(), R.layout.fragment_main, null);
    }

    @OnClick({R.id.modelSetting, R.id.order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.modelSetting:
                startActivity(new Intent(getActivity(), ModeSettingActivity.class));
                break;
            case R.id.order:
                break;
        }
    }

    //当用户关闭订单弹窗
    @Override
    public void onClose(OrderBean bean) {
        mOrderBind.startRequest(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent msg) {
        mModel = (String) SPUtils.get(getActivity(), Constants.AUTO_ORDER, "0");
        mCurrenModel.setText("当前模式为" + (mModel.equals("1") ? "自动" : "手动") + "接单模式");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        mOrderBind.stopRequest();//停止接单
    }
}
