package com.cskd20.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cskd20.R;

/**
 * @创建者 lucas
 * @创建时间 2017/5/26 0026 14:37
 * @描述 注册界面的提示弹窗
 */

public class NoPapersPopup extends PopupWindow implements View.OnClickListener {

    private final TextView mMsg;

    public NoPapersPopup(Context context, Builder builder) {
        super(context);
        View view = View.inflate(context, R.layout.popup_papers, null);
        view.findViewById(R.id.cancel).setOnClickListener(this);
        mMsg = (TextView) view.findViewById(R.id.msg);
        mMsg.setText(builder.mMsg);

        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置PopupWindow弹出窗体的背景
        setBackgroundDrawable(dw);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                break;
        }
    }

    public static class Builder {
        private Context mContext;
        private String  mMsg;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setMsg(String msg) {
            mMsg = msg;
            return this;
        }

        public NoPapersPopup build() {
            return new NoPapersPopup(mContext, this);
        }
    }
}
