package com.cskd20.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @创建者 lucas
 * @创建时间 2017/5/23 0023 10:44
 * @描述 TODO
 */

public class CommonUtil {

    /**
     * uri -> filename
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getUri2Filename(Context context, Uri uri) {
        String filePath = CommonUtil.getRealPathFromURI(context, uri);
        return filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
    }


    /**
     * uri _>path
     *
     * @param context
     * @param contentUri
     * @return
     */
//    public static String getRealPathFromURI(Context context, Uri data) {
//        String filename = "";
//        if (data.getScheme().toString().compareTo("content") == 0) {
//            Cursor cursor = context.getContentResolver().query(data,
//                    new String[]{MediaStore.Audio.Media.DATA}, null, null, null);
//            if (cursor.moveToFirst()) {
//                filename = cursor.getString(0);
//                Log.d("CommonUtil", "|");
//            }
//        } else if (data.getScheme().toString().compareTo("file") == 0)         //file:///开头的uri
//        {
//            filename = data.toString();
//            filename = data.toString().replace("file://", "");
//            //替换file://
//            if (!filename.startsWith("/mnt")) {
//                //加上"/mnt"头
//                filename += "/mnt";
//            }
//        }
//            Log.d("lucas", "未获取到文件路径");
//        return filename;
//    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        String photoPath = "";
        if(context == null || uri == null) {
            return photoPath;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if(isExternalStorageDocument(uri)) {
                String [] split = docId.split(":");
                if(split.length >= 2) {
                    String type = split[0];
                    if("primary".equalsIgnoreCase(type)) {
                        photoPath = Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
            }
            else if(isDownloadsDocument(uri)) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                photoPath = getDataColumn(context, contentUri, null, null);
            }
            else if(isMediaDocument(uri)) {
                String[] split = docId.split(":");
                if(split.length >= 2) {
                    String type = split[0];
                    Uri contentUris = null;
                    if("image".equals(type)) {
                        contentUris = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    }
                    else if("video".equals(type)) {
                        contentUris = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    }
                    else if("audio".equals(type)) {
                        contentUris = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    String selection = MediaStore.Images.Media._ID + "=?";
                    String[] selectionArgs = new String[] { split[1] };
                    photoPath = getDataColumn(context, contentUris, selection, selectionArgs);
                }
            }
        }
        else if("file".equalsIgnoreCase(uri.getScheme())) {
            photoPath = uri.getPath();
        }
        else {
            photoPath = getDataColumn(context, uri, null, null);
        }

        return photoPath;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return null;
    }


    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    //截取手机号
    public static String subMobile(String mobile) {
        if (TextUtils.isEmpty(mobile))
            return null;
        return mobile.substring(0, 3) + "****" + mobile.substring(7);
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
        联通：130、131、132、152、155、156、185、186
        电信：133、153、180、189、（1349卫通）
        总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
        */
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * 返回一个高亮spannable
     *
     * @param content 文本内容
     * @param color   高亮颜色
     * @param start   起始位置
     * @param end     结束位置
     * @return 高亮spannable
     */
    public static CharSequence getHighLightText(String content, int color, int start, int end) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        start = start >= 0 ? start : 0;
        end = end <= content.length() ? end : content.length();
        SpannableString spannable = new SpannableString(content);
        CharacterStyle span = new ForegroundColorSpan(color);
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static void getBitmapFormUri(Activity ac, Uri uri, ImageView imageView) throws FileNotFoundException,
            IOException {

        Bitmap bitmap = getBitmapFormUri(ac, uri);
        if (bitmap == null || imageView == null) {
            Log.e("CommonUtil", "bitmap = null||imageView ==null");
            return;
        }
        imageView.setImageBitmap(bitmap);
    }

    public static void getBitmapFormUri(Activity ac, Uri uri, ViewGroup view) throws IOException {
        Bitmap bitmap = getBitmapFormUri(ac, uri);
        if (bitmap == null || view == null) {
            Log.e("CommonUtil", "bitmap = null||imageView ==null");
            return;
        }
        view.setBackgroundDrawable(new BitmapDrawable(bitmap));
    }


    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * dp-->px
     */
    public static int dp2px(Context context,int dp) {
        // int densityDpi = getResources().getDisplayMetrics().densityDpi;
        float density = context.getResources().getDisplayMetrics().density;
        // System.out.println("densityDpi:" + densityDpi);
        // System.out.println("density:" + density);
        int px = (int) (dp * density + .5f);
        return px;
    }

    /**
     * px->dp
     */
    public static int px2dp(Context context,int px) {
        // px/dp = density
        float density = context.getResources().getDisplayMetrics().density;
        int dp = (int) (px / density + .5f);
        return dp;
    }

    /**
     * sp转px
     */
    public static int sp2px(Context context,float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转sp
     */
    public static float px2sp(Context context,float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }
    /**
     * 获取屏幕宽
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getHeight();
    }

    /**
     * 获得状态栏的高度
     *
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
