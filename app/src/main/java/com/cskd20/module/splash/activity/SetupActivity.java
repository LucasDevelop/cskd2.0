package com.cskd20.module.splash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.cskd20.R;
import com.cskd20.base.BaseActivity;
import com.cskd20.module.login.activity.LoginActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class SetupActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    @Bind(R.id.goin)
    Button    mGoin;

    int[] imgs = {R.mipmap.setup_yindao1, R.mipmap.setup_yindao2, R.mipmap.setup_yindao3};

    @Override
    protected int setContentView() {
        return R.layout.activity_setup;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mViewPager.setAdapter(new SetupAdapter(imgs));
        mViewPager.addOnPageChangeListener(this);
    }

    @OnClick(R.id.goin)
    public void onClick(View view){
        startActivity(new Intent(mContext, LoginActivity.class));
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mGoin.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class SetupAdapter extends PagerAdapter {
        private int[] mImgs;

        public SetupAdapter(int[] imgs) {
            mImgs = imgs;
        }

        @Override
        public int getCount() {
            return mImgs.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(params);
            imageView.setImageResource(mImgs[position]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
