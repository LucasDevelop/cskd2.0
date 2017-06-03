package com.cskd20.bean;

import java.util.List;

/**
 * @创建者 lucas
 * @创建时间 2017/6/2 0002 17:29
 * @描述 TODO
 */

public class CarType2Bean {

    /**
     * msg : 获取成功！
     * data : [{"make_name":"奥迪"},{"make_name":"劳伦士"},{"make_name":"迈凯轮"}]
     * status : 1
     */
    public String msg;
    public List<DataEntity> data;
    public int              status;


    public class DataEntity {
        /**
         * make_name : 奥迪
         */
        public String model_name;

    }
}
