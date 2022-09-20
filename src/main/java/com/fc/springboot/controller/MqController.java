package com.fc.springboot.controller;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName MqController
 * @Description
 * @Author fc
 * @Date 2022/2/10 11:24 上午
 * @Version 1.0
 **/
@RestController
@RequestMapping("/mq")
public class MqController {

  /*  @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @RequestMapping("/send")
    public String log() {
        rocketMQTemplate.convertAndSend("updateLoginInfo", " rocketmq 测试消息");
        return "success";
    }*/
}
