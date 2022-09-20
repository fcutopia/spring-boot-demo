package com.fc.springboot.process.register;

import com.fc.springboot.bean.EventType;
import com.fc.springboot.bean.bo.EventModel;
import com.fc.springboot.service.EventHandler;
import org.springframework.stereotype.Component;

/**
 * @ClassName LikeHandler
 * @Description 点赞 操作
 * @Author fc
 * @Date 2022/2/18 3:54 下午
 * @Version 1.0
 **/
@Component
public class LikeHandler implements EventHandler {


    @Override
    public void doHander(EventModel model) {
        System.out.println("do like");
    }

    @Override
    public EventType getEventType() {
        return EventType.LIKE;
    }
}
