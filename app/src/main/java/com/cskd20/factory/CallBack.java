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
    public void onResponse(Call<T> call, Response<T> response){
        Log.d("net", "call.request().url():" + call.request().url());
        Log.d("net", "json:" + response.body().toString());
        if (response.body() instanceof JsonObject)
            if (ResponseUtil.getStatus((JsonObject) response.body()) == 0) {
                Toast.makeText(App.getContext(), ResponseUtil.getMsg((JsonObject) response.body()), Toast
                        .LENGTH_SHORT).show();
                return;
            }
            onResponse1(call,response);
    }
    @Override
    public void onFailure(Call<T> call, Throwable t){
        Log.d("net", "url:" + call.request().url());
        t.printStackTrace();
        onFailure1(call,t);
    }


    public abstract void onResponse1(Call<T> call, Response<T> response);

    public abstract void onFailure1(Call<T> call, Throwable t) ;

}
