package com.fc.springboot.process;

import com.alibaba.fastjson.JSONObject;
import com.fc.springboot.bean.EventType;
import com.fc.springboot.bean.bo.EventModel;
import com.fc.springboot.constant.Constants;
import com.fc.springboot.service.EventHandler;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName EventConsumer
 * @Description 处理队列中的事件并与各个handler沟通
 * InitializingBean接口的作用在spring 初始化后，执行完所有属性设置方法(即setXxx)将自动调用 afterPropertiesSet(), 在配置文件中无须特别的配置.
 * @Author fc
 * @Date 2022/2/18 3:22 下午
 * @Version 1.0
 **/
@Component
public class EventConsumer implements InitializingBean, ApplicationContextAware {

    public Map<EventType, EventHandler> config = new HashMap<>();

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * sping的上下文
     */
    private ApplicationContext applicationContext;


    @Resource
    private Executor executor;


    @Override
    public void afterPropertiesSet() throws Exception {
        //获取EventHandler初始化对象集合
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (MapUtils.isEmpty(beans)) {
            return;
        }
        for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
            EventType eventType = entry.getValue().getEventType();
            if (!config.containsKey(eventType)) {
                //注册事件对象
                config.put(eventType, entry.getValue());
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //此线程一直监听
                while (true) {
                    //移除集合中右边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出。
                    String object = (String) redisTemplate.opsForList().rightPop(Constants.REDIS_ASYNC_KEY, 10, TimeUnit.MILLISECONDS);
                    if (StringUtils.isNotBlank(object)) {
                        //点赞操作放到线程池中执行
                        executor.execute(() -> {
                            EventModel eventModel = JSONObject.parseObject(object, EventModel.class);
                            if (config.containsKey(eventModel.type)) {
                                config.get(eventModel.type).doHander(eventModel);
                            }
                        });
                    }
                }
            }
        });
        thread.start();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
