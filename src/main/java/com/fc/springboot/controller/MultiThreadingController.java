package com.fc.springboot.controller;

import com.fc.springboot.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName MultiThreadingController
 * @Description
 * @Author fc
 * @Date 2022/2/17 10:19 上午
 * @Version 1.0
 **/
@RestController
@RequestMapping("thread")
public class MultiThreadingController {


    @Autowired
    private AsyncService asyncService;

    @RequestMapping("/async")
    public String findAll() {

        // asyncService.executeAsyncTask3(3);
        System.out.println("ThreadTestController 当前线程=" + Thread.currentThread().getName());
        asyncService.executeAysncTask1(1);
        return "success";
    }
}
