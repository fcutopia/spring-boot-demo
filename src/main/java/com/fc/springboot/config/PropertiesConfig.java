package com.fc.springboot.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @ClassName PropertiesConfig
 * @Description
 * @Author fc
 * @Date 2022/6/2 2:56 下午
 * @Version 1.0
 **/
@Data
@Configuration
@PropertySource(value = {"classpath:application.properties"})
public class PropertiesConfig {


    @Value("${user.name.ef}")
    public String userName;
}
