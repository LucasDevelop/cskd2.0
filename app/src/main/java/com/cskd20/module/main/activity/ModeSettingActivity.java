package com.cskd20.module.main.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.cskd20.R;
import com.cskd20.base.BaseActivity;
import com.cskd20.utils.Constants;
import com.cskd20.utils.SPUtils;
import com.kyleduo.switchbutton.SwitchButton;

import butterknife.Bind;
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
    //    @Bind(R.id.auto_taking)
    //    SwitchButton mAutoTaking;

    @Override
    protected int setContentView() {
        return R.layout.activity_mode_setting;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

    }

    @OnClick(R.id.submit)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                save();
                break;
        }
    }

    private void save() {
        StringBuilder sb = new StringBuilder("{");
        String s1 = mConvCar.isChecked() ? "1" : "";
        sb.append(TextUtils.isEmpty(s1) ? "" : s1);
        String s2 = mConvCar.isChecked() ? "2" : "";
        sb.append(TextUtils.isEmpty(s2) ? "" : "," + s2);
        String s3 = mConvCar.isChecked() ? "3" : "";
        sb.append(TextUtils.isEmpty(s3) ? "" : "," + s3);
        String s4 = mConvCar.isChecked() ? "4" : "";
        sb.append(TextUtils.isEmpty(s4) ? "" : "," + s4);
        String s5 = mConvCar.isChecked() ? "5" : "";
        sb.append(TextUtils.isEmpty(s5) ? "" : "," + s5 + "}");
        Log.d("lucas", sb.toString());
        SPUtils.put(mContext, Constants.ORDER_TYPE, sb.toString());
        SPUtils.put(mContext, Constants.AUTO_ORDER, mOrderTaking.isChecked()?"1":"0");
    }
}
