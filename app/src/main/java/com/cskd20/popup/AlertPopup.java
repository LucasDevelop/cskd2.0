package com.cskd20.popup;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cskd20.R;

/**
 * @创建者 lucas
 * @创建时间 2017/5/25 0025 16:11
 * @描述 注册界面的提示框
 */

public class AlertPopup extends PopupWindow{

    private Builder mBuilder;
    private View mView;

    public AlertPopup(Context context, Builder builder){
        super(context);
        mBuilder = builder;
        initView(context);
    }


    private void initView(Context context) {
        mView = View.inflate(context, R.layout.popup_alert, null);
        ImageView mIcon = (ImageView) mView.findViewById(R.id.icon);
        TextView mTitle = (TextView) mView.findViewById(R.id.title);
        mIcon.setBackgroundResource(mBuilder.iconId);
        mTitle.setText(mBuilder.text);

        //设置PopupWindow的View
        this.setContentView(mView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
//        this.setFocusable(true);
        //设置PopupWindow弹出窗体动画效果
//        mView.findViewById(R.id.ll_sexselect_pop).startAnimation(AnimationUtils.loadAnimation(context, R.anim
//                .popup_photo_enter));
        //实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        //设置PopupWindow弹出窗体的背景
//        this.setBackgroundDrawable(dw);
    }

    public void showPopup(){
        showAtLocation(mView, Gravity.CENTER,0,0);
    }

    public static class Builder{
        private Context mContext;
        private int iconId;
        private String text;

        public Builder(Context context) {
            mContext = context;
        }

        //设置图片
        public Builder setIcon(@IdRes int id){
            iconId = id;
            return this;
        }

        public Builder setTitle(String text){
            this.text = text;
            return this;
        }

        public AlertPopup build(){
            return new AlertPopup(mContext,this);
        }
    }
}
