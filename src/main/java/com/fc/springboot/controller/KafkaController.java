package com.fc.springboot.controller;

import com.fc.springboot.bean.vo.RequestLoginParam;
import com.fc.springboot.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName KafkaController
 * @Description
 * @Author fc
 * @Date 2022/2/17 4:21 下午
 * @Version 1.0
 **/
@RestController
@RequestMapping("kafka")
public class KafkaController {


    @Autowired
    private KafkaProducer kafkaProducer;

    @RequestMapping(value = "/test")
    public String signOut(HttpServletRequest request, @RequestBody RequestLoginParam requestLoginParam) {
        kafkaProducer.send("kafka test message");
        return "success";
    }

}
