package com.cskd20.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @创建者 lucas
 * @创建时间 2017/6/12 0012 10:47
 * @描述 TODO
 */

public class ImageUtils {


    //保存bitmap到本地
    public static void saveBitmap(Context context,Bitmap bm, String picName) {
        File mUserImagePath = new File(Environment.getExternalStorageDirectory() + "/Download/");
        File f = new File(mUserImagePath, picName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            LogUtils.d("图片保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d("保存失败！");
        }

    }


}
