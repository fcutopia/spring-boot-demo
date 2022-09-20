package com.fc.springboot.service;

/**
 * @ClassName AsyncService
 * @Description
 * @Author fc
 * @Date 2022/2/17 10:17 上午
 * @Version 1.0
 **/
public interface AsyncService {

    /**
     * 执行异步任务
     */
    void executeAysncTask1(Integer i);

    void executeAsyncTask2(Integer i);

    void executeAsyncTask3(Integer i);
}
