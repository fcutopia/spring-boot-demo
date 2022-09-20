package com.fc.springboot.controller;

import com.fc.springboot.bean.vo.LogRequestVo;
import com.fc.springboot.config.Log;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName AopLogController
 * @Description
 * @Author fc
 * @Date 2021/12/29 5:45 下午
 * @Version 1.0
 **/


@RestController
@RequestMapping("/aop")
public class AopLogController {

    @Log(value="aop日志测试",type=8)
    @RequestMapping("/log")
    public String log(@RequestBody LogRequestVo logRequestVo, @RequestHeader("userId") int userId) {

        return "success";
    }

}
