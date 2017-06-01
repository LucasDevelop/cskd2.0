package com.cskd20.bean;

/**
 * @创建者 lucas
 * @创建时间 2017/6/1 0001 15:47
 * @描述 TODO
 */

public class UploadImgBean {

    /**
     * msg : 图片上传成功！
     * data : http://localhost./Public/Uploads/driver/2017-05-23/5923f532b76b0.jpg
     * status : 1
     */
    public String msg;
    public String data;
    public int    status;

    @Override
    public String toString() {
        return "UploadImgBean{" +
                "msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                ", status=" + status +
                '}';
    }
}
