package com.fc.springboot.utils;

import com.alibaba.fastjson.JSON;
import com.fc.springboot.bean.bo.Users;

import java.util.Optional;

/**
 * @ClassName NpeUtils
 * @Description
 * @Author fc
 * @Date 2022/1/21 4:59 下午
 * @Version 1.0
 **/
public class NpeUtils {
    public static void main(String[] args) {
        try {
            test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void test() throws Exception {
        //当user值不为null时，orElse函数依然会执行createUser()方法（但是返回的user 对象不会覆盖原来的user对象），而orElseGet函数并不会执行createUser()方法
        Users users = new Users();
        users = Optional.ofNullable(users).orElse(createLisi());
        users = Optional.ofNullable(users).orElseGet(() -> createZhangsan());
        System.out.println(JSON.toJSONString(users));

        Optional.ofNullable(users).orElseThrow(() -> new Exception("用户不存在"));

        String userName = Optional.ofNullable(users).map(u -> u.getUserName()).get();


    }

    public static Users createZhangsan() {
        Users users = new Users();
        users.setUserName("zhangsan");
        return users;
    }

    public static Users createLisi() {
        Users users = new Users();
        users.setUserName("lisi");
        return users;
    }
}
