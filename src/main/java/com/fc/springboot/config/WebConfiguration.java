package com.fc.springboot.config;

import com.fc.springboot.interceptors.TestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName WebConfiguration
 * @Description
 * @Author fc
 * @Date 2021/12/15 5:45 下午
 * @Version 1.0
 **/
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public TestInterceptor testInterceptor() {
        return new TestInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(testInterceptor())
                .addPathPatterns("/mail/**")
        .excludePathPatterns("/user/sendFate")
                .excludePathPatterns("/redis/setAndGet")
        ;
    }

}
