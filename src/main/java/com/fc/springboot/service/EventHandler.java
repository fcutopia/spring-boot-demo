package com.fc.springboot.service;

import com.fc.springboot.bean.EventType;
import com.fc.springboot.bean.bo.EventModel;

import java.util.List;

/**
 * @ClassName EventHandler
 * @Description
 * @Author fc
 * @Date 2022/2/18 3:20 下午
 * @Version 1.0
 **/
public interface EventHandler {

    /**
     * 处理事件
     *
     * @param model
     */
    void doHander(EventModel model);

    /**
     * 事件所属类型
     *
     * @return
     */
    EventType getEventType();

}
