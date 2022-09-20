package com.fc.springboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fc.springboot.bean.bo.Users;
import com.fc.springboot.bean.request.UserRequest;

import java.util.List;

/**
 * @ClassName UserService
 * @Description
 * @Author fc
 * @Date 2021/12/20 5:48 下午
 * @Version 1.0
 **/
public interface UserService extends IService<Users> {

    List<Users> findAll();

    int create(UserRequest userRequest);

    IPage<Users> selectPage();
}
