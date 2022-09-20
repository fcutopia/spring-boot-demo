package com.fc.springboot.bean.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @ClassName UserRequest
 * @Description
 * @Author fc
 * @Date 2022/5/26 8:54 下午
 * @Version 1.0
 **/
@Data
@Builder
@AllArgsConstructor  //全属性构造方法
@NoArgsConstructor  //无参构造函数
public class UserRequest {

    @NotNull(message = "userName 不能为空")
    private String userName;

    @Length(max = 5, message = "密码长度不能超过5")
    @NotNull(message = "passWord 不能为空")
    private String passWord;

    //@Pattern(regexp = "(1$|2$|3$)", message = "role 值不在可选范围")
    private int role;

    /**
     * ^string : 匹配以 string 开头的字符串
     * string$ ：匹配以 string 结尾的字符串
     * ^string$ ：精确匹配 string 字符串
     * (^Man$|^Woman$|^UGM$) : 值只能在 Man,Woman,UGM 这三个值中选择
     */
    @Pattern(regexp = "(^Man$|^Woman$|^UGM$)", message = "sex 值不在可选范围")
    @NotNull(message = "sex 不能为空")
    private String sex;
}
