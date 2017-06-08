package com.cskd20.module.main.event;

/**
 * @创建者 lucas
 * @创建时间 2017/6/8 0008 14:29
 * @描述 TODO
 */

public class MessageEvent {
    public Object obj1;
    public Object obj2;
    public Object obj3;
    public Object obj4;

    public MessageEvent() {
    }

    public MessageEvent(Object obj1) {
        this.obj1 = obj1;
    }

    public MessageEvent(Object obj1, Object obj2) {
        this.obj1 = obj1;
        this.obj2 = obj2;
    }

    public MessageEvent(Object obj1, Object obj2, Object obj3) {
        this.obj1 = obj1;
        this.obj2 = obj2;
        this.obj3 = obj3;
    }
}
