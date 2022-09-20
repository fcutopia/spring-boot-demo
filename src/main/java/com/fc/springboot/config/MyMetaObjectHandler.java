package com.fc.springboot.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ClassName MyMetaObjectHandler
 * @Description
 * @Author fc
 * @Date 2022/5/26 7:44 下午
 * @Version 1.0
 **/
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 实现插入时的自动填充
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insert开始属性填充");
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "deleted", Boolean.class, false);
    }

    /**
     * 实现更新时的自动填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("update开始属性填充");
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
    }
}
