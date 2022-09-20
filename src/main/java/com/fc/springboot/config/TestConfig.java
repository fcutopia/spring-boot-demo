package com.fc.springboot.config;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TestConfig {

    @PostConstruct
    public void test(){
        System.out.println("项目启动时加载此方法");
    }


}
