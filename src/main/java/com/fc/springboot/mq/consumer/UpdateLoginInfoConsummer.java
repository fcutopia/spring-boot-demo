package com.fc.springboot.mq.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * @ClassName
 * @Description
 * @Author fc
 * @Date 2022/2/10 11:20 上午
 * @Version 1.0
 **/
@Component
//@RocketMQMessageListener(topic = "updateLoginInfo", consumerGroup = "update_login_info")
public class UpdateLoginInfoConsummer implements RocketMQListener<String> {

    private static final Logger logger = LoggerFactory.getLogger(UpdateLoginInfoConsummer.class);

    @Override
    public void onMessage(String message) {
        System.out.println("rocketMq Receive message：" + message);
    }
}
