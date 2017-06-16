package com.cskd20.utils;

/**
 * @创建者 lucas
 * @创建时间 2017/5/22 0022 16:20
 * @描述 常量类
 */

public interface Constants {
    long   CONN_TIME_OUT      = 1000 * 20;
    long   READ_TIME_OUT      = 1000 * 20;
    //日志等级
    int    DEBUGLEVEL         = LogUtils.LEVEL_VERBOSE;
    //订单请求频率
    long   REQUEST_ORDER_RATE = 5 * 1000;
    //接单类型  true  自动  face 手动
    String ORDER_TYPE         = "ORDER_TYPE";
    //自动接单
    String AUTO_ORDER         = "AUTO_ORDER";
    //司机位子
    String LNG                = "lng", LAT = "lat";

    //域名
    String URI      = "http://www.chengshikuaidao.com:8080/index.php/Api/";
    //本地域名
    String TEST_URI = "http://www.chengshikuaidao.com:6060/index.php/Api/";
    //    http://www.chengshikuaidao.com:6060/index.php/Api/
    //    http://192.168.2.104/index.php/Api/

    //登录接口
    String LOGIN = "ApiDriver/login";

    //注册
    String REGISTER = "ApiDriver/register";

    //发送验证码
    String SEND_CODE = "ApiDriver/send_Code";

    //检查手机号
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

    //获取订单
    String GET_ORDER = "ApiDriver/get_order";

    //确认接到乘客
    String MEET_PASSENGER = "ApiDriver/meet_passenger";

    //返回价格详情
    String PUSH_PRICE_INFO = "ApiDriver/arrive_destination";

    //提交高速费
    String PUSH_TOLL_FEE   = "ApiDriver/send_order";

    //版本更新
    String VERSION_UPDATE = "Api/version";
}
