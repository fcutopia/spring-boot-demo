package com.fc.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fc.springboot.bean.bo.Users;
import com.fc.springboot.bean.request.UserRequest;
import com.fc.springboot.bean.vo.SysLogQuery;
import com.fc.springboot.config.PropertiesConfig;
import com.fc.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName UserController
 * @Description
 * @Author fc
 * @Date 2021/12/20 5:50 下午
 * @Version 1.0
 **/
@Validated
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PropertiesConfig propertiesConfig;


    @RequestMapping("/config")
    public String configTest() {
        return propertiesConfig.getUserName();
    }

    @RequestMapping("/findAll")
    public List<Users> findAll() {
        System.out.println("test");
        //return userService.list();
        return userService.findAll();
    }

    @RequestMapping("/selectPage")
    public IPage<Users> selectPage() {
        return userService.selectPage();
    }


    @RequestMapping("/update")
    public Users update() {
        UpdateWrapper<Users> updateWrapper = new UpdateWrapper();
        updateWrapper.set("user_name", "王老七");
        updateWrapper.set("update_time", new Date());
        updateWrapper.eq("id", 15);
        userService.update(updateWrapper);
        return userService.getById(15);
    }

    @RequestMapping("/batchUpdate")
    public String batchUpdate() {
        Users user_7 = new Users();
        user_7.setId(7);
        user_7.setUserName("里斯_7");
        //user_7.setUpdateTime(new Date());

        Users user_11 = new Users();
        user_11.setId(11);
        user_11.setUserName("里斯_11");
        List<Users> list = new ArrayList<>();
        list.add(user_7);
        list.add(user_11);
        // user_11.setUpdateTime(new Date());

        userService.updateBatchById(list);
        return "success";
    }

    @RequestMapping("/delete")
    public String delete() {
        userService.removeById(7);
        return "success";
    }


    @Transactional(rollbackFor = {Exception.class, Error.class})
    @RequestMapping("/create")
    public String create(@RequestBody @Valid UserRequest userRequest) {
        userService.create(userRequest);
        return "success";
    }

    @RequestMapping("/log")
    public String log() {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/json");
        SysLogQuery sysLogQuery = new SysLogQuery();
        //将交集ID传给服务端
        ResponseEntity<String> result = restTemplate.postForEntity("http://127.0.0.1:9061/syslog/query", new HttpEntity<String>(JSON.toJSONString(sysLogQuery), headers), String.class);
        return "success";
    }

    @RequestMapping("/sendFate")
    public String sendFate() {
        JSONObject request = new JSONObject();
        JSONObject head = new JSONObject();

        head.put("serviceId", "yzx_004");

        JSONObject body = new JSONObject();
        JSONObject featureData = new JSONObject();
        featureData.put("x0", "1.88669");
        featureData.put("x1", "-1.359293");
        featureData.put("x2", "2.303601");
        featureData.put("x3", "2.00137");
        featureData.put("x4", "1.307686");

        JSONObject sendToRemoteFeatureData = new JSONObject();
        sendToRemoteFeatureData.put("phone_num", "0002e63f85cd40674e6c3b7640cfc7d9");

        body.put("featureData", featureData);
        body.put("sendToRemoteFeatureData", sendToRemoteFeatureData);


        request.put("head", head);
        request.put("body", body);
        String result = restTemplate.postForObject("http://192.168.198.210:8059/federation/v1/inference", new HttpEntity<String>(request.toJSONString()), String.class);
        System.out.println(result);
        return result;
    }

}
