package com.cskd20.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.cskd20.R;
import com.cskd20.bean.OrderBean;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @创建者 lucas
 * @创建时间 2017/6/7 0007 10:12
 * @描述 订单
 */

public class OrderPopup extends PopupWindow {

    private View mView;
    @Bind(R.id.title)
    TextView mTile;
    @Bind(R.id.start_point)
    TextView mStart;
    @Bind(R.id.end_point)
    TextView mEnd;
    @Bind(R.id.length)
    TextView mLength;

    String[] types = {"", "方便快车", "商务专车", "顺路车", "拼车", "接送机"};
    private Handler mHandler = new Handler();
    private OrderBean mBean;
    private OnCloseListener mListener;

    public OrderPopup(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mView = View.inflate(context, R.layout.popup_order, null);
        ButterKnife.bind(this, mView);
        //设置PopupWindow的View
        this.setContentView(mView);
        setFocusable(false);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(0x0000);
        //        设置PopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    //添加数据
    public void setDate(OrderBean bean) {
        mBean = bean;
        if (bean == null) {
            Log.d("lucas", "订单获取失败");
            dismiss();
            return;
        }
        mTile.setText(types[Integer.parseInt(bean.data.car_type)] + "订单");
        mStart.setText(bean.data.start_place);
        mEnd.setText(bean.data.end_place);
        float distance = AMapUtils.calculateLineDistance(
                new LatLng(bean.data.start_latitude, bean.data.start_longitude),
                new LatLng(bean.data.end_latitude, bean.data.end_longitude));
        Log.d("lucas", "distance:" + distance);
        mLength.setText("距离您约"+((int)distance)/10*0.01+"公里");
    }

    public void show(final Activity activity) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showAtLocation(activity.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
            }
        }, 100);
    }

    public interface OnCloseListener{
        void onClose(OrderBean bean);
    }

    public void setOnCloseListener(OnCloseListener listener){
        mListener = listener;
    }

    @OnClick(R.id.close)
    public void onClick(View view){
        dismiss();
        if (mListener!=null)
            mListener.onClose(mBean);
    }
}
