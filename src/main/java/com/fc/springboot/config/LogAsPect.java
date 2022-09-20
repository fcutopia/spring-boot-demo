package com.fc.springboot.config;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

/**
 * @ClassName LogAsPect
 * @Description
 * @Author fc
 * @Date 2021/12/29 5:31 下午
 * @Version 1.0
 **/
@Aspect
@Component
public class LogAsPect {


    //表示匹配带有自定义注解的方法
    @Pointcut("@annotation(com.fc.springboot.config.Log)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        try {
            result = point.proceed();
            long endTime = System.currentTimeMillis();
            insertLog(point, endTime - beginTime);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

    private void insertLog(ProceedingJoinPoint point, long time) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Log userAction = method.getAnnotation(Log.class);
        String value = userAction.value();

        int type= userAction.type();

        // 请求的类名
        String className = point.getTarget().getClass().getName();
        // 请求的方法名
        String methodName = signature.getName();
        // 请求的方法参数值
        String args = Arrays.toString(point.getArgs());

        //从session中获取当前登陆人id
//      Long useride = (Long)SecurityUtils.getSubject().getSession().getAttribute("userid");

        Long userid = 1L;//应该从session中获取当前登录人的id，这里简单模拟下

    }
}
