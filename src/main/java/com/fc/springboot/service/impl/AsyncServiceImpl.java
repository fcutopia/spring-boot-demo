package com.fc.springboot.service.impl;

import com.fc.springboot.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * @ClassName AsyncServiceImpl
 * @Description 线程池异步
 * @Author fc
 * @Date 2022/2/17 10:17 上午
 * @Version 1.0
 **/
@Service
public class AsyncServiceImpl implements AsyncService {


    @Resource
    private Executor executor;

    /**
     * 打开AsyncServiceImpl.java，在executeAsync方法上增加注解  @Async(“asyncServiceExecutor”)，
     * asyncServiceExecutor是前面ExecutorConfig.java中的方法名，
     * 表明executeAsync方法进入的线程池是asyncServiceExecutor方法创建的，如下：
     */
    @Override
    @Async("asyncServiceExecutor")
    public void executeAysncTask1(Integer i) {
        System.out.println("AsyncServiceImpl 当前线程=" + Thread.currentThread().getName());
        System.out.println("CustomMultiThreadingService ==> executeAysncTask1 method: 执行异步任务 " + i);

    }

    @Override
    @Async("asyncServiceExecutor")
    public void executeAsyncTask2(Integer i) {
        System.out.println("CustomMultiThreadingService ==> executeAysncTask1 method: 执行异步任务 " + i);

    }

    @Override
    public void executeAsyncTask3(Integer i) {
        executor.execute(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("CustomMultiThreadingService ==> executeAysncTask1 method: 执行异步任务 " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }
}
