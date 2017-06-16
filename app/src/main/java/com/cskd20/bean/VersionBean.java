package com.cskd20.bean;

/**
 * @创建者 lucas
 * @创建时间 2017/6/10 0010 16:31
 * @描述 TODO
 */

public class VersionBean {

    /**
     * msg : 查询成功！
     * data : {"version_desc":"看看行不行","release_date":"2017-06-02 10:02:57","version_code":"3.0","client":null,
     * "title":"测试 ","url":"www.chengshikuaidao.com:5050/cskd.apk","force_update":"0"}
     * status : 1
     */
    public String msg;
    public DataEntity data;
    public int        status;



    public class DataEntity {
        /**
         * version_desc : 看看行不行
         * release_date : 2017-06-02 10:02:57
         * version_code : 3.0
         * client : null
         * title : 测试
         * url : www.chengshikuaidao.com:5050/cskd.apk
         * force_update : 0
         */
        public String version_desc;
        public String release_date;
        public String version_code;
        public String client;
        public String title;
        public String url;
        public String force_update;


    }
}
