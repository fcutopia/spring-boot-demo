package com.fc.springboot.bean.hidden;

import lombok.Data;

@Data
public class RedisMiddleData extends RSAEntity {

    /**
     * 模型分结果集
     */
    private Object result;

    /**
     * 从hbase中命中的pn
     */
    private String md5;

    /**
     * 请求中的pn
     */
    private String value;

}
