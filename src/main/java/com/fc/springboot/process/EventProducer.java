package com.fc.springboot.process;

import com.alibaba.fastjson.JSONObject;
import com.fc.springboot.bean.bo.EventModel;
import com.fc.springboot.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @ClassName EventProducer
 * @Description 事件的入口，用来统一分发事件，就是在队列中插入
 * @Author fc
 * @Date 2022/2/18 3:17 下午
 * @Version 1.0
 **/
@Service
public class EventProducer {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 把事件分发出去  EventProducer
     *
     * @param eventModel
     * @return
     */
    public boolean fireEvent(EventModel eventModel) {
        try {
            //序列化，将EventModel 转换JSON的字符串
            String json = JSONObject.toJSONString(eventModel);
            redisTemplate.opsForList().leftPush(Constants.REDIS_ASYNC_KEY, json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
