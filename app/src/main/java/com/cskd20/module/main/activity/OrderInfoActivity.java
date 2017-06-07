package com.cskd20.module.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.cskd20.R;
import com.cskd20.base.BaseActivity;
import com.cskd20.bean.OrderBean;

import butterknife.Bind;

public class OrderInfoActivity extends BaseActivity {
    @Bind(R.id.start_point)
    TextView mStartTV;
    @Bind(R.id.end_point)
    TextView mEndTV;
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
        int km = intent.getIntExtra("km",-1);
        mStartTV.setText(mOrder.data.start_place);
        mEndTV.setText(mOrder.data.end_place);
    }
}
