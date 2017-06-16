package com.cskd20.factory;

import android.util.Log;
import android.widget.Toast;

import com.cskd20.App;
import com.cskd20.utils.ResponseUtil;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @创建者 lucas
 * @创建时间 2017/6/2 0002 15:19
 * @描述 对网络回调做装饰
 */

public abstract class CallBack<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Log.d("lucas_net", "call.request().url():" + call.request().url());
        if (response == null || response.body() == null) {
            Log.e("lucas_net", "respense == null?");
            onFailure1(call,new Exception());
            return;
        }
        Log.d("lucas_net", "json:" + response.body().toString());
        if (response.body() instanceof JsonObject) {
            int status = ResponseUtil.getStatus((JsonObject) response.body());
            boolean isToast = false;
            isToast = onResponse1(call, response);
            //如果获取数据失败
            if (status == 0) {
                if (isToast)
                    Toast.makeText(App.getContext(), ResponseUtil.getMsg((JsonObject) response.body()), Toast
                            .LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.d("lucas_net", "url:" + call.request().url());
        t.printStackTrace();
        onFailure1(call, t);
    }

    /**
     * @param call
     * @param response
     * @return 是否toast 提示
     */
    public abstract boolean onResponse1(Call<T> call, Response<T> response);

    public abstract void onFailure1(Call<T> call, Throwable t);

}
