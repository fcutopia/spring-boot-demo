package com.fc.springboot.bean.vo;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName SysLogQuery
 * @Description
 * @Author fc
 * @Date 2021/12/31 9:43 上午
 * @Version 1.0
 **/
@Data
public class SysLogQuery  {


    /**
     * 用户名
     */
    public String userName;

    /**
     * 角色Id
     */
    public int roleId;

    /**
     * 操作类型
     */
    public int type;

    /**
     * 开始时间
     */
    public Date startTime;

    /**
     * 结束时间
     */
    public Date endTime;

    public Integer limitStart;

    public Integer limitNum;


}
