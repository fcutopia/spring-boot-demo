package com.fc.springboot.bean.hidden;

import lombok.Data;

import java.util.List;

@Data
public class MD5HitRES {

    /**
     * 随机uuid
     */
    private String uuid;


    /**
     * md5信息
     */
    private List<MD5Hit> md5HitList;
}
