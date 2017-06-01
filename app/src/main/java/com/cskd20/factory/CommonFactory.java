package com.cskd20.factory;

import com.cskd20.api.ApiService;
import com.cskd20.utils.Constants;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @创建者 lucas
 * @创建时间 2017/5/23 0023 9:20
 * @描述 普通工厂  创建一些常用的工具实体
 */

public abstract class CommonFactory {

    private static ApiService apiService;
    private static Gson       gson;

    //创建一个api实体
    public static ApiService getApiInstance() {
        if (apiService == null)
            synchronized (CommonFactory.class) {
                if (apiService == null)
                    apiService = new Retrofit.Builder()
                            .baseUrl(Constants.TEST_URI)
                            .client(providerOkHttpClient())
                            .addConverterFactory(GsonConverterFactory.create(getGsonInstance()))
                            .build()
                            .create(ApiService.class);
            }
        return apiService;
    }

    //创建一个gson实体
    public static Gson getGsonInstance() {
        if (gson == null)
            synchronized (CommonFactory.class) {
                if (gson == null)
                    gson = new Gson();
            }
        return gson;
    }

    static OkHttpClient providerOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(Constants.CONN_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(Constants.READ_TIME_OUT,TimeUnit.MILLISECONDS)
                .writeTimeout(Constants.READ_TIME_OUT,TimeUnit.MILLISECONDS)
                .build();
    }

}
