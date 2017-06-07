package com.cskd20.module.personal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cskd20.R;
import com.cskd20.base.BaseActivity;
import com.cskd20.base.BaseFragment;
import com.cskd20.module.personal.activity.PersonalCenterActivity;

import butterknife.OnClick;

/**
 * @创建者 lucas
 * @创建时间 2017/5/26 0026 10:09
 * @描述 我的  界面
 */

public class PersonalFragment extends BaseFragment {



    @Override
    public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }

    @Override
    public View getLayoutView() {
        return View.inflate(getContext(), R.layout.fragment_personal,null);
    }

    @OnClick({R.id.appointment,R.id.my,R.id.wallet,R.id.msg,
            R.id.recommend,R.id.my_car,R.id.activity,R.id.setting,R.id.head_img})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.appointment://预约行程
                break;
            case R.id.my://我的行程
//                openPage();
                break;
            case R.id.wallet://钱包
                break;
            case R.id.msg://消息中心
                break;
            case R.id.recommend://推荐有奖
                break;
            case R.id.my_car://我的车辆
                break;
            case R.id.activity://奖励活动
                break;
            case R.id.setting://设置
                break;
            case R.id.head_img:
                startActivity(new Intent(getActivity(), PersonalCenterActivity.class));
                break;
        }
    }

    //打开界面
    private void openPage(Class<BaseActivity> clazz) {

    }
}
