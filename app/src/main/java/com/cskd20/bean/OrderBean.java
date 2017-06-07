package com.cskd20.bean;

import java.io.Serializable;

/**
 * @创建者 lucas
 * @创建时间 2017/6/6 0006 16:59
 * @描述 订单
 */

public class OrderBean implements Serializable{

    /**
     * msg : 获取订单信息成功！
     * data : {"phone":"13715138443","start_place":"新安街道泰安花园(里仁府东北)泰安花园","p_latitude":"22.56598997406529",
     * "end_place":"坪洲(地铁站)","order_id":"1","p_longitude":"113.8865709127455","username":"yahaha还是","car_type":"1"}
     * status : 1
     */
    public String msg;
    public DataEntity data;
    public int        status;


    public class DataEntity implements Serializable{


        /**
         * start_longitude : 113.8865709127455
         * phone : 13715138443
         * start_latitude : 22.56598997406529
         * end_longitude : 113.8865709127455
         * start_place : 新安街道泰安花园(里仁府东北)泰安花园
         * end_place : 坪洲(地铁站)
         * end_latitude : 22.56598997406529
         * order_id : 1
         * username : yahaha还是
         * car_type : 1
         */
        public double start_longitude;
        public String phone;
        public double start_latitude;
        public double end_longitude;
        public String start_place;
        public String end_place;
        public double end_latitude;
        public String order_id;
        public String username;
        public String car_type;

    }
}
