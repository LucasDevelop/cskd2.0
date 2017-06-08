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
    int  DEBUGLEVEL    = LogUtils.LEVEL_VERBOSE;
    //订单请求频率
    long REQUEST_ORDER_RATE = 10*1000;
    //接单类型  true  自动  face 手动
    String ORDER_TYPE = "ORDER_TYPE";
    //自动接单
    String AUTO_ORDER = "AUTO_ORDER";
    //司机位子
    String LNG        = "lng", LAT = "lat";

    //域名
    String URI      = "http://www.chengshikuaidao.com:8080/index.php/Api/ApiDriver/";
    //本地域名
    String TEST_URI = "http://www.chengshikuaidao.com:6060/index.php/Api/ApiDriver/";
//    http://www.chengshikuaidao.com:6060/index.php/Api/
//    http://192.168.2.104/index.php/Api/

    //登录接口
    String LOGIN = "login";

    //注册
    String REGISTER = "register";

    //发送验证码
    String SEND_CODE = "send_Code";

    //检查验证码
    String CHECK_PHONE = "checkPhone";

    //校验验证码
    String CHECK_CODE = "checkCode";

    //图片上传
    String UPLOAD_IMG = "upload_pic";

    //修改密码
    String MIDF_PWD = "change_password";

    //车型
    String CAR_TYPE = "get_cars";

    //司机注册
    String DRIVE_REGISTER = "drive_register";

    //获取订单
    String GET_ORDER = "get_order";

    //确认接到乘客
    String MEET_PASSENGER = "meet_passenger";

    //返回价格详情
    String push_price_info = "arrive_destination";
}
