package com.cskd20.module.main.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.cskd20.R;
import com.cskd20.base.BaseActivity;
import com.cskd20.module.main.event.MessageEvent;
import com.cskd20.utils.Constants;
import com.cskd20.utils.SPUtils;
import com.kyleduo.switchbutton.SwitchButton;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class ModeSettingActivity extends BaseActivity {
    @Bind(R.id.cb_conv_car)
    CheckBox     mConvCar;
    @Bind(R.id.cb_commerce_car)
    CheckBox     mCommerceCar;
    @Bind(R.id.cb_accept_car)
    CheckBox     mAcceptCar;
    @Bind(R.id.cb_carpool)
    CheckBox     mCarpool;
    @Bind(R.id.cb_shuttle)
    CheckBox     mShuttle;
    @Bind(R.id.cb_all)
    CheckBox     mAll;
    @Bind(R.id.order_taking)
    SwitchButton mOrderTaking;
    @Bind(R.id.title_name)
    TextView mTitle;
    //    @Bind(R.id.auto_taking)
    //    SwitchButton mAutoTaking;

    @Override
    protected int setContentView() {
        return R.layout.activity_mode_setting;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mTitle.setText("模式设置");
        String auto = (String) SPUtils.get(mContext, Constants.AUTO_ORDER, "0");
        mOrderTaking.setChecked(auto.equals("1") ? true : false);
        String type = (String) SPUtils.get(mContext, Constants.ORDER_TYPE, "1");
        if (type.contains("1"))
            mConvCar.setChecked(true);
        if (type.contains("2"))
            mCommerceCar.setChecked(true);
        if (type.contains("3"))
            mAcceptCar.setChecked(true);
        if (type.contains("4"))
            mCarpool.setChecked(true);
        if (type.contains("5"))
            mShuttle.setChecked(true);
        if (type.contains("1,2,3,4,5")) {
            mConvCar.setChecked(true);
            mCommerceCar.setChecked(true);
            mAcceptCar.setChecked(true);
            mCarpool.setChecked(true);
            mShuttle.setChecked(true);
            mAll.setChecked(true);
        }
    }

    @OnClick({R.id.submit,R.id.back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                save();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @OnCheckedChanged(R.id.cb_all)
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mConvCar.setChecked(true);
            mCommerceCar.setChecked(true);
            mAcceptCar.setChecked(true);
            mCarpool.setChecked(true);
            mShuttle.setChecked(true);
        }else {
            mConvCar.setChecked(false);
            mCommerceCar.setChecked(false);
            mAcceptCar.setChecked(false);
            mCarpool.setChecked(false);
            mShuttle.setChecked(false);
        }
    }

    private void save() {
        StringBuilder sb = new StringBuilder("");
        String s1 = mConvCar.isChecked() ? "1" : "";
        sb.append(TextUtils.isEmpty(s1) ? "" : s1);
        String s2 = mCommerceCar.isChecked() ? "2" : "";
        sb.append(TextUtils.isEmpty(s2) ? "" : "," + s2);
        String s3 = mAcceptCar.isChecked() ? "3" : "";
        sb.append(TextUtils.isEmpty(s3) ? "" : "," + s3);
        String s4 = mCarpool.isChecked() ? "4" : "";
        sb.append(TextUtils.isEmpty(s4) ? "" : "," + s4);
        String s5 = mShuttle.isChecked() ? "5" : "";
        sb.append(TextUtils.isEmpty(s5) ? "" : "," + s5 + "");
        Log.d("lucas", sb.toString());
        SPUtils.put(mContext, Constants.ORDER_TYPE, mAll.isChecked()?"1,2,3,4,5":sb.toString());
        SPUtils.put(mContext, Constants.AUTO_ORDER, mOrderTaking.isChecked() ? "1" : "0");
        EventBus.getDefault().post(new MessageEvent(mOrderTaking.isChecked(), sb.toString()));
        finish();
    }
}
