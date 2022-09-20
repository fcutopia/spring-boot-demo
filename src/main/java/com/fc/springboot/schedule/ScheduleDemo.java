package com.fc.springboot.schedule;

import com.fc.springboot.utils.DateUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName ScheduleDemo
 * @Description
 * @Author fc
 * @Date 2022/5/23 10:39 上午
 * @Version 1.0
 **/
@Component
public class ScheduleDemo {

    private Integer time = 0;


    /**
     * 定时器定义，每5秒执行一次
     */
    @Scheduled(cron = "0/5 * * * * ?")
    private void process() {
        System.out.println(Thread.currentThread().getName());
        System.out.println("定时器执行 " + DateUtils.getDate2Str(new Date()));
    }

}
