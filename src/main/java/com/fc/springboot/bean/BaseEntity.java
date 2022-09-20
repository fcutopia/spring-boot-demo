package com.fc.springboot.bean;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName BaseEntity
 * @Description
 * @Author fc
 * @Date 2022/5/12 10:10 上午
 * @Version 1.0
 **/
@Data
public class BaseEntity extends Model<BaseEntity> {

    /**
     *
     */
    private static final long serialVersionUID = -3890934764385076499L;

    @TableId(value = "ID", type = IdType.AUTO)
    private long id;


    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)// 插入的时候自动填充
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)// 插入和更新的时候自动填充
    private Date updateTime;

    /**
     * 逻辑删除属性
     */
    @TableLogic
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private Boolean deleted;


}
