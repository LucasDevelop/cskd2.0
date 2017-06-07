package com.cskd20.module.main.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cskd20.R;
import com.cskd20.base.BaseFragment;
import com.cskd20.bean.OrderBean;
import com.cskd20.module.main.activity.MapNavActivity;
import com.cskd20.module.main.activity.ModeSettingActivity;
import com.cskd20.module.main.server.RequestOrderService;
import com.cskd20.popup.OrderPopup;
import com.cskd20.utils.Constants;
import com.cskd20.utils.SPUtils;
import com.cskd20.view.RippleButton;
import com.google.gson.JsonObject;

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
    private OrderBean mOrderBean;


    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //设置接单模式--默认手动接单
        String orderType = (String) SPUtils.get(getActivity(), Constants.ORDER_TYPE, "");
        if ("1".equals(orderType)) {
            mCurrenModel.setText("当前为自动接单模式");
            mSearch.setClickable(false);
            startSearch();
        } else {
            mCurrenModel.setText("当前为手动接单模式");
            mSearch.setClickable(true);
        }
        mSearch.setOnclickListener1(new View.OnClickListener() {
            int count = 0;

            @Override
            public void onClick(View v) {
                if (currentStatus == RECEIVE_ORDER) {//收到订单
                    //进入地图界面
                    if (mOrderBind != null) {
                        Intent intent = new Intent(getActivity(), MapNavActivity.class);
                        intent.putExtra("order", mOrderBean);
                        //停止请求订单
                        mOrderBind.stopRequest();
                        startActivity(intent);
                    }
                } else {
                    if (count % 2 == 0) {//开始请求订单
                        currentStatus = TAKING;
                        mSearch.start();
                        startSearch();
                    } else {//停止请求订单
                        mSearch.stop();
                        currentStatus = NORMAL;
                        mOrderBind.stopRequest();
                    }
                    count++;
                }
            }
        });

        //启动自动请求订单服务
        getActivity().bindService(new Intent(getActivity(), RequestOrderService.class), mConn, Context
                .BIND_AUTO_CREATE);
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
            mStartText.setText("接单");
            //停止请求订单
            mOrderBind.stopRequest();
            showWindow(mOrderBean);
        }

        @Override
        public void onFailure1(Call<JsonObject> call, Throwable t) {

        }
    };

    //弹出订单信息
    private void showWindow(OrderBean orderBean) {
        if (mPopup == null){
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
}
