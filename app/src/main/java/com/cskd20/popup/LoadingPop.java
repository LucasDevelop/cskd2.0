package com.cskd20.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cskd20.R;

/**
 * @创建者 lucas
 * @创建时间 2017/6/5 0005 14:26
 * @描述 加载中
 */

public class LoadingPop extends PopupWindow {

    private View     mView;
    private Context  mContext;
    private Builder mBuilder;
    private TextView mTitle;
    private Handler mHandler = new Handler();
    private ImageView mIc;

    public LoadingPop(Context context) {
        super(context);
        mContext = context;
        mView = View.inflate(context, R.layout.popup_loading, null);
        commInit();
        initView();
    }

    public LoadingPop(Context context, Builder builder) {
        super(context);
        mView = View.inflate(context, R.layout.popup_loading, null);
        mContext = context;
        mBuilder = builder;
        initView();
        commInit();
    }

    private void commInit() {
        //设置PopupWindow的View
        this.setContentView(mView);
        setFocusable(false);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(0x0000);
        //        设置PopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private void initView() {
        mTitle = (TextView) mView.findViewById(R.id.title);
        mIc = (ImageView) mView.findViewById(R.id.ic);
        View progress = mView.findViewById(R.id.progress);
        if (mBuilder==null){
            Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate);
            LinearInterpolator lin = new LinearInterpolator();
            operatingAnim.setInterpolator(lin);
            progress.startAnimation(operatingAnim);
            return;
        }else {
            progress.clearAnimation();
        }
        if (!TextUtils.isEmpty(mBuilder.text)) {
            mTitle.setText(mBuilder.text);
            progress.setVisibility(View.GONE);
        } else {
            progress.setVisibility(View.INVISIBLE);

        }
        if (!TextUtils.isEmpty(mBuilder.iconId + ""))
            mIc.setImageResource(mBuilder.iconId);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }
    public void setIcon(@DrawableRes int id){
        mIc.setImageResource(id);
    }

    public void show(final Activity activity) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showAtLocation(activity.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
            }
        }, 100);
    }

    public void hide(){
        dismiss();
    }

    public static class Builder {
        private Context mContext;
        private int     iconId;
        private String  text;

        public Builder(Context context) {
            mContext = context;
        }

        //设置图片
        public Builder setIcon(@IdRes int id) {
            iconId = id;
            return this;
        }

        public Builder setTitle(String text) {
            this.text = text;
            return this;
        }

        public LoadingPop build() {
            return new LoadingPop(mContext, this);
        }
    }
}
