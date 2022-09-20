package com.fc.springboot.controller;


import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("api")
@Api("swagger Controller相关的api")
public class SwagerController {


    //1.商品添加
    @PutMapping("add")
    @ApiOperation(value = "商品新增")
    //正常业务时， 需要在category类里或者server层进行事务控制，控制层一般不进行业务控制的。
    //@Transactional(rollbackFor = Exception.class)
    //@RequestParam 接收页面中的请求的参数
    public String addCategory(@RequestParam String name) {
        return String.format("hello: %s ",name);
    }
}
