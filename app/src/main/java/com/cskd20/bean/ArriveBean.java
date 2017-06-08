package com.cskd20.bean;

/**
 * @创建者 lucas
 * @创建时间 2017/6/8 0008 9:29
 * @描述 TODO
 */

public class ArriveBean {

    /**
     * msg : 获取价格成功！
     * data : {"service_charge":0,"km_money":41.16,"total_money":41.161,"red_packet":"0"}
     * status : 1
     */
    public String msg;
    public DataEntity data;
    public int        status;


    public class DataEntity {
        /**
         * service_charge : 0
         * km_money : 41.16
         * total_money : 41.161
         * red_packet : 0
         */
        public int service_charge;
        public double km_money;
        public double total_money;
        public String red_packet;

    }
}
