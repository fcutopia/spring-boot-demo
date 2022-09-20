package com.fc.springboot.bean.vo;


/**
 * @ClassName IntersecRecordEntity
 * @Description 用户求交集记录
 * @Author fc
 * @Date 2021/12/9 2:26 下午
 * @Version 1.0
 **/
public class TaskRequestParam {

    /**
     * 请求记录Id
     */
    public String recordId;

    /**
     * appId
     */
    public String appId;


    /**
     * 文件名称
     */
    public String fileName;


    /**
     * 样本数量
     */
    public long recordSize;

    /**
     * 回调通知地址
     */
    public String callbackUrl;


    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getRecordSize() {
        return recordSize;
    }

    public void setRecordSize(long recordSize) {
        this.recordSize = recordSize;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
