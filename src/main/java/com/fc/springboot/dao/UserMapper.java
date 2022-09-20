package com.fc.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fc.springboot.bean.bo.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName UserMapper
 * @Description
 * @Author fc
 * @Date 2021/12/20 5:45 下午
 * @Version 1.0
 **/
@Mapper
public interface UserMapper extends BaseMapper<Users> {

    List<Users> findAll();

    int create(Users users);

    IPage<Users> selectPageVo(IPage<Users> page, @Param("user") Users users);




}
