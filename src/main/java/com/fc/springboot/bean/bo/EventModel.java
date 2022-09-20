package com.fc.springboot.bean.bo;

import com.fc.springboot.bean.EventType;
import lombok.Data;

/**
 * @ClassName EventModel
 * @Description 事件实例
 * @Author fc
 * @Date 2022/2/18 3:14 下午
 * @Version 1.0
 **/
@Data
public class EventModel {


    // entityId和entityType就是评论的是那个问题，entityOwnerId就是那个问题关联的对象
    /**
     * 事件的类型
     */
    public EventType type;

    /**
     * 事件的触发者
     */
    public int actorId;

    /**
     * 触发事件的载体
     */
    public int entityType;

    /**
     * 和entityType组合成触发事件的载体  可以使任何一个实体的id，问题，评论，用户，站内信等等
     */
    public int entityId;

    /**
     * 载体关联的对象,当我们给一个人点赞时，系统要给那个人（也就是entityOwnerId）发送一个站内信，通知那个人他被点赞了。
     */
    public int entityOwnerId;

    public EventModel() {

    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public void setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
    }
}
