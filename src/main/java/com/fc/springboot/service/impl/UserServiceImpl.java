package com.fc.springboot.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fc.springboot.bean.bo.Users;
import com.fc.springboot.bean.request.UserRequest;
import com.fc.springboot.dao.UserMapper;
import com.fc.springboot.service.UserService;
import com.fc.springboot.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @ClassName UserServiceImpl
 * @Description
 * @Author fc
 * @Date 2021/12/20 5:49 下午
 * @Version 1.0
 **/
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, Users> implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Users> findAll() {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<Users>();
        queryWrapper.eq("role", 2);
        return userMapper.selectList(queryWrapper);


    }


    @Override
    public int create(UserRequest userRequest) {
        Date currentTime = new Date();
        Users users = new Users();
        BeanUtils.copyProperties(userRequest,users);
        int result = userMapper.insert(users);
        return result;
    }

    @Override
    public IPage<Users> selectPage() {
        IPage<Users> page = new Page<>();
        page.setSize(3);
        page.setCurrent(1);
        Users user = new Users();
        user.setRole(2);
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_name", "山");
        queryWrapper.select("id", "user_name", "role");
        queryWrapper.orderBy(true, false, "create_time");
        return userMapper.selectPage(page, queryWrapper);
    }
}
