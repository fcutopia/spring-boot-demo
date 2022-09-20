package com.fc.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fc.springboot.bean.EventType;
import com.fc.springboot.bean.bo.EventModel;
import com.fc.springboot.bean.bo.Users;
import com.fc.springboot.bean.vo.RequestLoginParam;
import com.fc.springboot.process.EventProducer;
import com.fc.springboot.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
//import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;

  /*  @Autowired
    private JedisCluster jedisCluster;*/

    @Autowired
    private RestTemplate restTemplate;

    @Resource
    private RedisUtil redisUtil;


    @Autowired
    private EventProducer eventProducer;


    @PostMapping("setAndGet")
    public String setAndGet(@RequestBody Users users) {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        redisTemplate.opsForList().leftPush("test", list);
        System.out.println(redisTemplate.opsForValue().get("FateTemplate"));
        return String.valueOf(redisTemplate.opsForList().rightPop("test"));

    }

    @RequestMapping(value = "/signOut")
    public String signOut(HttpServletRequest request, @RequestBody RequestLoginParam requestLoginParam) {

        String sessionToken = request.getHeader("token");
        String userName = requestLoginParam.getUserName();
        String passWord = requestLoginParam.getPassWord();
        return "success";
    }


    @RequestMapping(value = "/restTemplate")
    public String restTemplate(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", "qwe");
        headers.add("Content-Type", "application/json");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", "张山");
        jsonObject.put("passWord", "12ab");
        String result = restTemplate.postForObject("http://127.0.0.1:8001/redis/callback", new HttpEntity<String>(jsonObject.toJSONString(), headers), String.class);
        System.out.println(result);
        return "success";
    }

    @RequestMapping(value = "/callback")
    public String callback(HttpServletRequest request, @RequestBody RequestLoginParam param) {
        String sessionToken = request.getHeader("token");
        System.out.println(JSON.toJSONString(param));
        return "userservice";
    }

    /**
     * 点赞操作
     *
     * @return
     */
    @RequestMapping(value = "/like")
    public String like() {
        EventModel model = new EventModel();
        model.setActorId(1);
        model.setType(EventType.LIKE);
        eventProducer.fireEvent(model);
        return "userservice";
    }




}



