package com.fc.springboot.exceptions;

/**
 * @ClassName FateException
 * @Description
 * @Author fc
 * @Date 2022/5/12 3:57 下午
 * @Version 1.0
 **/
public class FateException extends RuntimeException {

    private int code;

    private String msg;

    public FateException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
