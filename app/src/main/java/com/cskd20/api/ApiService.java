package com.cskd20.api;

import com.cskd20.bean.CheckPhoneBean;
import com.cskd20.bean.SendCodeBean;
import com.cskd20.utils.Constants;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @创建者 lucas
 * @创建时间 2017/5/22 0022 16:32
 * @描述 网络接口
 */

public interface ApiService {

    //登录
    @FormUrlEncoded
    @POST(Constants.LOGIN)
    Call<JsonObject> login(@Field("Phone") String username, @Field("password") String pwd);

    //发送验证码
    @FormUrlEncoded
    @POST(Constants.SEND_CODE)
    Call<SendCodeBean> sendCode(@Field("phone") String phone);

    //注册
    @FormUrlEncoded
    @POST(Constants.REGISTER)
    Call<JsonObject> register(@Field("phone")  String phone,@Field("password") String pwd,@Field("verify") String verify, @Field("pid") String pid);

    //检查手机号是否存在
    @FormUrlEncoded
    @POST(Constants.CHECK_PHONE)
    Call<CheckPhoneBean> checkPhone(@Field("phone") String phone, @Field("interface") String i);

    //上传图片
    @Multipart
    @Headers("Connection: close")//iDValidationByDPI
    @POST(Constants.UPLOAD_IMG)
    Call<JsonObject> uploadImg(@Part MultipartBody.Part imgs);
}
