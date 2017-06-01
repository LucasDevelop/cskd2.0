package com.cskd20.module.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.cskd20.R;
import com.cskd20.base.BaseActivity;
import com.cskd20.bean.InfoBean;
import com.cskd20.utils.SPUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;


public class RegisterSetup1Activity extends BaseActivity {


    @Bind(R.id.brand)
    Spinner mBrand;//品牌
    @Bind(R.id.clazz)
    Spinner mClazz;//类型
    @Bind(R.id.color)
    Spinner mColor;//颜色
    private TimePickerView mPickerView;
    @Bind(R.id.insurance_end)
    TextView mInsuranceEnd;
    @Bind(R.id.insurance_start)
    TextView mInsuranceStart;
    //    @Bind(R.id.car_register_date)
    //    TextView MCarRegosterDate;
    @Bind(R.id.name)
    EditText mName;//姓名
    @Bind(R.id.code)
    EditText mCode;//身份证
    @Bind(R.id.age)
    EditText mAge;//驾龄
    @Bind(R.id.city)
    EditText mCity;//出车城市
    @Bind(R.id.car_code)
    EditText mCarCode;//车牌
    @Bind(R.id.owner_name)
    EditText mOwnerName;//车主姓名
    @Bind(R.id.car_register_date)
    TextView mCarRegisterDate;//车辆注册日期
    private InfoBean mInfoBean;

    @Override
    protected int setContentView() {
        return R.layout.activity_register_setup1;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        initSpinner();
    }

    private void initSpinner() {
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBrand.setAdapter(adapter);
        mClazz.setAdapter(adapter);
        mColor.setAdapter(adapter);
    }

    @OnClick({R.id.next, R.id.insurance_start, R.id.insurance_end, R.id.car_register_date})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                //                saveInfo();
                startActivity(new Intent(this, RegisterSetup2Activity.class));
                break;
            case R.id.insurance_start://车险起始时间
                showPickerDate(R.id.insurance_start);
                break;
            case R.id.insurance_end://车险终止时间
                showPickerDate(R.id.insurance_end);
                break;
            case R.id.car_register_date://车辆注册时间
                showPickerDate(R.id.car_register_date);
                break;
            default:
                break;
        }
    }

    private void saveInfo() {
        String name = mName.getText().toString().trim();
        String code = mCode.getText().toString().trim();
        String age = mAge.getText().toString().trim();
        String city = mCity.getText().toString().trim();
        String carCode = mCarCode.getText().toString().trim();
        String ownerName = mOwnerName.getText().toString().trim();
        String carRegisterDate = mCarRegisterDate.getText().toString().trim();
        String insuranceStart = mInsuranceStart.getText().toString().trim();
        String insuranceEnd = mInsuranceEnd.getText().toString().trim();
        //验证
        String msg = null;
        if (TextUtils.isEmpty(name))
            msg = "姓名不能为空";
        if (TextUtils.isEmpty(code))
            msg = "身份证号不能为空";
        if (code.length() != 18)
            msg = "身份证号格式不对";
        if (TextUtils.isEmpty(age))
            msg = "驾龄不能为空";
        if (TextUtils.isEmpty(city))
            msg = "出车城市不能为空";
        if (TextUtils.isEmpty(carCode))
            msg = "车牌号不能为空";
        if (TextUtils.isEmpty(ownerName))
            msg = "车主姓名不能为空";
        if (TextUtils.isEmpty(carRegisterDate))
            msg = "车辆注册日期不能为空";
        if (insuranceStart.contains("-"))
            msg = "请选择车险起始日期";
        if (insuranceEnd.contains("-"))
            msg = "请选择车险结束日期";

        if (msg != null) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            return;
        }

        mInfoBean = new InfoBean();
        mInfoBean.realname = name;
        mInfoBean.id_card = code;
//        mInfoBean.car_brand =
        //        mInfoBean.car_series =
        //        mInfoBean.car_color =
        mInfoBean.city = city;
        mInfoBean.driving = age;
        mInfoBean.token = (String) SPUtils.get(mContext,"token","");
        mInfoBean.car_owner = ownerName;
        mInfoBean.start_insurance = insuranceStart;
        mInfoBean.end_insurance = insuranceEnd;
        mInfoBean.car_regist_time = carRegisterDate;
        Intent intent = new Intent(this, RegisterSetup2Activity.class);
        intent.putExtra("info",mInfoBean);
        startActivity(intent);
    }

    //显示日期选择器
    private void showPickerDate(@IdRes int id) {
        final TextView text = (TextView) findViewById(id);
        mPickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy年 MM月 dd日");
                String dateStr = format.format(date);
                text.setText(dateStr);
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).build();
        mPickerView.setDate(Calendar.getInstance());
        if (!mPickerView.isShowing())
            mPickerView.show();
    }


}
