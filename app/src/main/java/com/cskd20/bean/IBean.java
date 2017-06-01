package com.cskd20.bean;

/**
 * @创建者 lucas
 * @创建时间 2017/5/23 0023 10:34
 * @描述 实体的公共接口
 */

public class IBean {

    /**
     * result : token
     * status : 1
     */
    private String result;
    private int status;

    public void setResult(String result) {
        this.result = result;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public int getStatus() {
        return status;
    }
}
