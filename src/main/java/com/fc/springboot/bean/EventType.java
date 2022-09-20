package com.fc.springboot.bean;

/**
 * @ClassName EventType
 * @Description
 * @Author fc
 * @Date 2022/2/17 11:52 上午
 * @Version 1.0
 **/
public enum EventType {

    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3);

    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
