package com.cskd20.module.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cskd20.R;
import com.cskd20.base.BaseActivity;
import com.cskd20.bean.ArriveBean;
import com.cskd20.bean.OrderBean;
import com.cskd20.factory.CallBack;
import com.cskd20.utils.SPUtils;
import com.google.gson.JsonObject;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class OrderInfoActivity extends BaseActivity {
    @Bind(R.id.start_point)
    TextView mStartTV;
    @Bind(R.id.end_point)
    TextView mEndTV;
    @Bind(R.id.charging)
    TextView mChanrging;
    @Bind(R.id.car_type)
    TextView mCarTYpe;
    @Bind(R.id.length)
    TextView mLength;
    @Bind(R.id.length_charging)
    TextView mLengthCharging;
    @Bind(R.id.server_charging)
    TextView mServerCharging;
    @Bind(R.id.red_packet)
    TextView mRedPacket;
    @Bind(R.id.expressway)
    EditText mExp;


    private OrderBean mOrder;

    @Override
    protected int setContentView() {
        return R.layout.activity_taking_order;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        mOrder = (OrderBean) intent.getSerializableExtra("order");
        String city = intent.getStringExtra("city");
        String time = intent.getStringExtra("time");
        int km = intent.getIntExtra("km", -1);
        mStartTV.setText(mOrder.data.start_place);
        mEndTV.setText(mOrder.data.end_place);
        mLength.setText("里程" + km + "公里");
        initData(city, km, time);
    }

    private void initData(String city, int km, String time) {
        show();
        mApi.pushPriceInfo(mOrder.data.order_id, (String) SPUtils.get(mContext, "token", "")
                , 2 + "", km + "", city, time).enqueue(new CallBack<JsonObject>() {
            @Override
            public boolean onResponse1(Call<JsonObject> call, Response<JsonObject> response) {
                hide();
                ArriveBean arriveBean = mGson.fromJson(response.body().toString(), ArriveBean.class);
                mChanrging.setText(arriveBean.data.total_money + "元");
                mLengthCharging.setText(arriveBean.data.km_money + "元");
                mServerCharging.setText(arriveBean.data.service_charge + "元");
                mRedPacket.setText(arriveBean.data.red_packet + "元");
                return false;
            }

            @Override
            public void onFailure1(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    @OnClick(R.id.send)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                sendDate();
                break;
        }
    }

    private void sendDate() {
        String exp = mExp.getText().toString().trim();
        if (Double.parseDouble(exp)<0){
            Toast.makeText(mContext, "高速费不能为负数", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
