package com.cskd20.utils;

import android.support.annotation.NonNull;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @创建者 lucas
 * @创建时间 2017/6/1 0001 10:18
 * @描述 TODO
 */

public abstract class ResponseUtil {
    public static JSONObject Jsb2JSb(@NonNull JsonObject object){
        try {
            return new JSONObject(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getStatus(JsonObject body){
        JSONObject sb = Jsb2JSb(body);
        try {
          return   sb.getInt("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getMsg(JsonObject body){
        JSONObject sb = Jsb2JSb(body);
        try {
            return sb.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
