package com.cskd20.module.personal.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cskd20.App;
import com.cskd20.R;
import com.cskd20.base.BaseActivity;
import com.cskd20.bean.LoginBean;
import com.cskd20.module.personal.view.CircleImageView;
import com.cskd20.popup.PictureSelectPopup;
import com.cskd20.utils.CommonUtil;
import com.cskd20.utils.ImageUtils;
import com.cskd20.utils.ResponseUtil;
import com.google.gson.JsonObject;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cskd20.utils.CommonUtil.hasSdcard;

public class PersonalCenterActivity extends BaseActivity {
    @Bind(R.id.name)
    TextView        mName;
    @Bind(R.id.sex)
    TextView        mSex;
    @Bind(R.id.phone)
    TextView        mPhone;
    @Bind(R.id.car_type)
    TextView        mCarType;
    @Bind(R.id.car_number)
    TextView        mCarNumber;
    @Bind(R.id.driver_name)
    TextView        mDriverName;
    @Bind(R.id.register_city)
    TextView        mRegisterCity;
    @Bind(R.id.ic)
    CircleImageView mIc;
    /* 头像文件 */
    private static final String IMAGE_FILE_NAME      = "temp_head_image.jpg";
    /* 请求识别码 */
    private static final int    CODE_GALLERY_REQUEST = 0xa0;
    private static final int    CODE_CAMERA_REQUEST  = 0xa1;
    private static final int    CODE_RESULT_REQUEST  = 0xa2;
    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static       int    output_X             = 200;
    private static       int    output_Y             = output_X;
    private PictureSelectPopup mPopUp;


    @Override
    protected int setContentView() {
        return R.layout.activity_personal_conter;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mPopUp = new PictureSelectPopup(this, itemsOnClick);
        LoginBean.DataEntity data = App.getUserExit().data;
        if (data==null)
            return;
        mName.setText(data.username);
        mSex.setText(data.sex);
        mPhone.setText(data.phone);
//        mCarType.setText();
        mCarNumber.setText(data.car_no);
        mDriverName.setText(data.realname);
        mRegisterCity.setText(data.city);
    }

    @OnClick({R.id.ic,R.id.back})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.ic:
                mPopUp.showAtLocation(mIc, Gravity.CENTER,0,0);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_select_photo:
                    choseHeadImageFromGallery();
                    mPopUp.dismiss();
                    break;
                case R.id.bt_select_takephoto:
                    choseHeadImageFromCameraCapture();
                    mPopUp.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            return;
        }
//        Uri data = intent.getData();

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());
                break;
            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempFile = new File(Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getApplicationContext(), "没有SDCard!", Toast.LENGTH_LONG).show();
                }
                break;
            case CODE_RESULT_REQUEST:
                if (intent != null) {
                                        setImageToHeadView(intent);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
    private String            mPicName;
    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            final Bitmap photo = extras.getParcelable("data");
            //获取到了裁剪后的图片，上传到接口中去
            mIc.setImageBitmap(photo);
            //将图片存在本地
            mPicName = "userHeadImage";
            ImageUtils.saveBitmap(getApplicationContext(),photo, mPicName);
            //上传头像
            uploadPic(intent.getData());
        }
    }

    //上传图片
    private void uploadPic(final Uri uri) {
        File file = new File(CommonUtil.getRealPathFromURI(mContext, uri));
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
                int status = ResponseUtil.getStatus(response.body());
                if (status == 1) {
                    //上传成功
                    Toast.makeText(mContext, "上传成功!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "上传失败!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(mContext, "上传失败!", Toast.LENGTH_SHORT).show();
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
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }
        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }
}
