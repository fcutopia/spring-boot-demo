package com.fc.springboot.bean.bo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fc.springboot.bean.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.Set;

/**
 * @ClassName User
 * @Description
 * @Author fc
 * @Date 2021/12/20 5:44 下午
 * @Version 1.0
 **/
@TableName("tb_user")
public class Users extends BaseEntity {


    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String passWord;


    private int role;

    public Users(String userName, String passWord, int role) {
        this.userName = userName;
        this.passWord = passWord;
        this.role = role;
    }

    public Users() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }


    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }


}
