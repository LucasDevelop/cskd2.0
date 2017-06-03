package com.cskd20.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cskd20.R;

/**
 * @创建者 lucas
 * @创建时间 2017/6/2 0002 14:19
 * @描述 波纹按钮
 */

public class RippleButton extends LinearLayout implements View.OnClickListener {

    private static final int ANIMATIONEACHOFFSET = 600; // 每个动画的播放时间间隔
    private AnimationSet aniSet, aniSet2, aniSet3;
    private ImageView btn, wave1, wave2, wave3;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x222) {
                wave2.startAnimation(aniSet2);
            } else if (msg.what == 0x333) {
                wave3.startAnimation(aniSet3);
            }
            super.handleMessage(msg);
        }

    };
    private boolean mIsStart;

    public RippleButton(Context context) {
        super(context);
        initView(context);
    }


    public RippleButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        aniSet = getNewAnimationSet();
        aniSet2 = getNewAnimationSet();
        aniSet3 = getNewAnimationSet();
        View root = View.inflate(context, R.layout.view_ripple, this);
        btn = (ImageView) root.findViewById(R.id.btn);
        wave1 = (ImageView) root.findViewById(R.id.wave1);
        wave2 = (ImageView) root.findViewById(R.id.wave2);
        wave3 = (ImageView) root.findViewById(R.id.wave3);
        btn.setOnClickListener(this);
        //        btn.setOnTouchListener(new View.OnTouchListener() {
        //            @Override
        //            public boolean onTouch(View v, MotionEvent event) {
        //                switch (event.getAction()) {
        //                    case MotionEvent.ACTION_DOWN:
        //                        showWaveAnimation();
        //                        break;
        //                    case MotionEvent.ACTION_UP:
        //                        cancelWaveAnimation();
        //                        break;
        //                    case MotionEvent.ACTION_CANCEL:
        //                        cancelWaveAnimation();
        //                        break;
        //                }
        //                return true;
        //            }
        //        });
    }

    private AnimationSet getNewAnimationSet() {
        AnimationSet as = new AnimationSet(true);
        ScaleAnimation sa = new ScaleAnimation(1f, 1.5f, 1f, 1.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(ANIMATIONEACHOFFSET * 3);
        sa.setRepeatCount(-1);// 设置循环
        AlphaAnimation aniAlp = new AlphaAnimation(1, 0.1f);
        aniAlp.setRepeatCount(-1);// 设置循环
        as.setDuration(ANIMATIONEACHOFFSET * 3);
        as.addAnimation(sa);
        as.addAnimation(aniAlp);
        return as;
    }

    private void showWaveAnimation() {
        mIsStart = true;
        wave1.startAnimation(aniSet);
        handler.sendEmptyMessageDelayed(0x222, ANIMATIONEACHOFFSET);
        handler.sendEmptyMessageDelayed(0x333, ANIMATIONEACHOFFSET * 2);

    }

    private void cancelWaveAnimation() {
        mIsStart = false;
        wave1.clearAnimation();
        wave2.clearAnimation();
        wave3.clearAnimation();

    }

    public void start(){
        if (!mIsStart)
        showWaveAnimation();
    }

    public void stop(){
        if (mIsStart)
            cancelWaveAnimation();
    }

    @Override
    public void onClick(View v) {
        if (!mIsStart)
            showWaveAnimation();
    }
}
