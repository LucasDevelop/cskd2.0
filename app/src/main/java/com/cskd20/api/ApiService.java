package com.cskd20.api;

import com.cskd20.bean.CheckPhoneBean;
import com.cskd20.bean.SendCodeBean;
import com.cskd20.utils.Constants;
import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * @创建者 lucas
 * @创建时间 2017/5/22 0022 16:32
 * @描述 网络接口
 */

public interface ApiService {

    //登录
    @FormUrlEncoded
    @POST(Constants.LOGIN)
    Call<JsonObject> login(@Field("phone") String username, @Field("password") String pwd);

    //发送验证码
    @FormUrlEncoded
    @POST(Constants.SEND_CODE)
    Call<SendCodeBean> sendCode(@Field("phone") String phone);

    //注册
    @FormUrlEncoded
    @POST(Constants.REGISTER)
    Call<JsonObject> register(@Field("phone") String phone, @Field("password") String pwd, @Field("verify") String
            verify, @Field("pid") String pid);

    //检查手机号是否存在
    @FormUrlEncoded
    @POST(Constants.CHECK_PHONE)
    Call<CheckPhoneBean> checkPhone(@Field("phone") String phone, @Field("interface") String i);

    //上传图片
    @Multipart
    @Headers("Connection: close")//iDValidationByDPI
    @POST(Constants.UPLOAD_IMG)
    Call<JsonObject> uploadImg(@Part MultipartBody.Part imgs);

    //修改密码
    @FormUrlEncoded
    @POST(Constants.MIDF_PWD)
    Call<JsonObject> modifPwd(@Field("phone") String phone, @Field("password") String pwd);

    //校验验证码
    @GET(Constants.CHECK_CODE)
    Call<JsonObject> checkCode(@Query("phone") String phone, @Query("code") String code, @Query("interface") int inter);

    //获取车型
    @FormUrlEncoded
    @POST(Constants.CAR_TYPE)
    Call<JsonObject> getCarType(@Field("name") String name);

    //司机注册
    @FormUrlEncoded
    @POST(Constants.DRIVE_REGISTER)
    Call<JsonObject> driveRegister(@FieldMap Map<String, String> map);

    //获取订单
    @FormUrlEncoded
    @POST(Constants.GET_ORDER)
    Call<JsonObject> getOrder(@Field("uid") String uid, @Field("lng") String lng, @Field("lat") String lat
            , @Field("auto") String auto, @Field("car_type") String carType, @Field("order_id") String orderId);

    //确认接单乘客
    @FormUrlEncoded
    @POST(Constants.MEET_PASSENGER)
    Call<JsonObject> meetPass(@Field("order_id") String orderId);

    //返回价格详情
    @FormUrlEncoded
    @POST(Constants.push_price_info)
    Call<JsonObject> pushPriceInfo(@Field("order_id") String orderId, @Field("token") String token,
                                   @Field("mem_type_id") String memTypeId, @Field("total_km") String totalKm,
                                   @Field("areas") String areas, @Field("gotime") String goTime);


}
