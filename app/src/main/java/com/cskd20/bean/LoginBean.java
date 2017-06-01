package com.cskd20.bean;

/**
 * @创建者 lucas
 * @创建时间 2017/5/23 0023 10:36
 * @描述 TODO
 */

public class LoginBean extends IBean {


    /**
     * result : {"number":"464664","bankID":"","uname":"测试账号","mobile":"18825204205","car_img":"http://8.soowww
     * .com/attachment/images/14954438571.jpeg","id":"207","avatar":"http://cskd.eltcn.cn/driver.png","bankname":"",
     * "type":"1","card":"421182199112263355","car_spec":"哦送"}
     * status : 0
     * token : 66332c487f1f4db7cce167fb76133727ce4a42f0
     */
    public ResultEntity result;
    public int    status;
    public String token;


    public class ResultEntity {
        /**
         * number : 464664
         * bankID :
         * uname : 测试账号
         * mobile : 18825204205
         * car_img : http://8.soowww.com/attachment/images/14954438571.jpeg
         * id : 207
         * avatar : http://cskd.eltcn.cn/driver.png
         * bankname :
         * type : 1
         * card : 421182199112263355
         * car_spec : 哦送
         */
        public String number;
        public String bankID;
        public String uname;
        public String mobile;
        public String car_img;
        public String id;
        public String avatar;
        public String bankname;
        public String type;
        public String card;
        public String car_spec;

    }
}
