package com.fc.springboot.bean.hidden;

import lombok.Data;

@Data
public class MD5Hit {

    /**
     * 命中的md5
     */
    private String md5;

    /**
     * 公钥的module值
     */
    private String publicN;

    /**
     * 公钥的e值
     */
    private String publicE;
}
