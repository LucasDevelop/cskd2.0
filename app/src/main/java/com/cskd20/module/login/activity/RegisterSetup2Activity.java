package com.cskd20.module.login.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cskd20.R;
import com.cskd20.base.BaseActivity;
import com.cskd20.popup.PictureSelectPopup;
import com.cskd20.utils.CommonUtil;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cskd20.utils.CommonUtil.hasSdcard;

/**
 * @创建者 lucas
 * @创建时间 2017/5/25 0025 11:39
 * @描述 注册第二步
 */

public class RegisterSetup2Activity extends BaseActivity implements View.OnClickListener {

    private PictureSelectPopup mPictureSelectPopup;
    /* 请求识别码 */
    private static final int    CODE_GALLERY_REQUEST = 0xa0;
    private static final int    CODE_CAMERA_REQUEST  = 0xa1;
    private static final int    CODE_RESULT_REQUEST  = 0xa2;
    /* 头像文件 */
    private static       String IMAGE_FILE_NAME      = "temp_head_image.jpg";

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static       int output_X      = 200;
    private static       int output_Y      = output_X;
    private static final int PHOTO1        = 1;
    private static final int PHOTO2        = 2;
    private static final int PHOTO3        = 3;
    private              int mCurrentPhoto = 0;//当前准备添加的图片

    @Bind(R.id.photo1)
    RelativeLayout mPhoto1;
    @Bind(R.id.photo2)
    RelativeLayout mPhoto2;
    @Bind(R.id.photo3)
    RelativeLayout mPhoto3;
    @Bind(R.id.temp1)
    LinearLayout   mTemp1;
    @Bind(R.id.temp2)
    LinearLayout   mTemp2;
    @Bind(R.id.temp3)
    LinearLayout   mTemp3;

    @Override
    protected int setContentView() {
        return R.layout.activity_register_setup2;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
    }

    @OnClick({R.id.photo1, R.id.photo2, R.id.photo3})
    public void onClickUpload(View view) {
        switch (view.getId()) {
            case R.id.photo1:
                mCurrentPhoto = PHOTO1;
                showSelectPhotoPopup();
                break;
            case R.id.photo2:
                mCurrentPhoto = PHOTO2;
                showSelectPhotoPopup();
                break;
            case R.id.photo3:
                mCurrentPhoto = PHOTO3;
                showSelectPhotoPopup();
                break;
            default:
                break;
        }
    }

    //显示popup
    private void showSelectPhotoPopup() {
        if (mPictureSelectPopup == null)
            mPictureSelectPopup = new PictureSelectPopup(this, this);
        if (!mPictureSelectPopup.isShowing())
            mPictureSelectPopup.showAtLocation(this.findViewById(R.id.photo1), Gravity
                    .BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_select_photo:
                choseHeadImageFromGallery();
                mPictureSelectPopup.dismiss();
                break;
            case R.id.bt_select_takephoto:
                choseHeadImageFromCameraCapture();
                mPictureSelectPopup.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                //                cropRawPhoto(intent.getData());
                showPhoto(intent.getData());
                break;
            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempFile = new File(Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    showPhoto(Uri.fromFile(tempFile));
                    //                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_LONG).show();
                }
                break;
            //裁剪后的图片
            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    //                    setImageToHeadView(intent);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    //显示预览图片
    private void showPhoto(Uri uri) {
        RelativeLayout tempView = null;
        //图片设置成功，隐藏上层控件
        switch (mCurrentPhoto) {
            case PHOTO1:
                tempView = mPhoto1;
                mTemp1.setVisibility(View.GONE);
                break;
            case PHOTO2:
                tempView = mPhoto2;
                mTemp2.setVisibility(View.GONE);
                break;
            case PHOTO3:
                tempView = mPhoto3;
                mTemp3.setVisibility(View.GONE);
                break;
        }
        try {
            CommonUtil.getBitmapFormUri(this, uri, tempView);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //上传图片
        uploadPic(uri);
    }

    //上传图片
    private void uploadPic(Uri uri) {
        File file = new File(CommonUtil
                .getRealPathFromURI(mContext, uri));
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        String filePath = CommonUtil.getRealPathFromURI(mContext, uri);
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
        // MultipartBody.Part  和后端约定好Key，这里的partName是用image
        MultipartBody.Part body = MultipartBody.Part.createFormData("pic", fileName, requestFile);
        mApi.uploadImg(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("lucas", "json:" + response.body().toString());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            IMAGE_FILE_NAME = new Random().nextLong() + ".jpg";
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }
        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }


}
