package com.cskd20.utils;

/**
 * @创建者 lucas
 * @创建时间 2017/5/22 0022 16:20
 * @描述 常量类
 */

public interface Constants {
    long CONN_TIME_OUT = 1000 * 20;
    long READ_TIME_OUT = 1000 * 20;
    //日志等级
    int DEBUGLEVEL = LogUtils.LEVEL_VERBOSE;

    //域名
    String URI = "http://www.chengshikuaidao.com:8080/index.php/Api/";
    //本地域名
    String TEST_URI = "http://www.chengshikuaidao.com:6060/index.php/Api/";

    //登录接口
    String LOGIN = "ApiDriver/login";

    //注册
    String REGISTER = "ApiDriver/register";

    //发送验证码
    String SEND_CODE   = "ApiDriver/send_Code";

    //检查验证码
    String CHECK_PHONE = "ApiDriver/checkPhone";

    //校验验证码
    String CHECK_CODE = "ApiDriver/checkCode";

    //图片上传
    String UPLOAD_IMG = "ApiDriver/upload_pic";

    //修改密码
    String MIDF_PWD = "ApiDriver/change_password";

    //车型
    String CAR_TYPE = "ApiDriver/get_cars";

    //司机注册
    String DRIVE_REGISTER = "ApiDriver/drive_register";
}
