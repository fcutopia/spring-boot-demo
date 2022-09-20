package com.fc.springboot.interceptors;


import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

public class TestInterceptor implements HandlerInterceptor {


    private static final Logger logger = LoggerFactory.getLogger(TestInterceptor.class);


    /**
     * host端请求
     */
    private static List<String> SELF_REQUEST = Arrays.asList("/efedration/host/createAppIdAndSecert", "/federation/host/getAccessToken");

    /**
     * guest 端请求
     */
    private static List<String> GUEST_REQUEST = Arrays.asList("/federation/host/getAccessToken");


    /**
     * 区分不同请求路径
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        logger.info("进入到拦截器");
        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            logger.info("拦截器, request_method={},RequestMethod.OPTIONS.name={}", request.getMethod(), RequestMethod.OPTIONS.name());
            response.setStatus(HttpStatus.OK.value());
            return true;
        }
        logger.info("拦截器, request_method={},RequestMethod.OPTIONS.name={}", request.getMethod(), RequestMethod.OPTIONS.name());


        String uri = request.getRequestURI();
        //来自guest端的请求，需验证
        if (GUEST_REQUEST.contains(uri)) {
            //从参数中获取token
            String accessionToken = request.getParameter("token");
            // request.getInputStream();
            if (StringUtils.isBlank(accessionToken)) {
                return false;
            }
            return true;
        }
        //本地请求，从head 中获取token
        String token = request.getHeader("token");
        //抛异常
        ResponseEntityVo responseEntityVo = new ResponseEntityVo();
        // Preconditions.checkArgument(StringUtils.isNotBlank(token), "parameter sessionToken is required");
        if (StringUtils.isBlank(token)) {
            responseEntityVo.setCode(127);
            responseEntityVo.setMessage("session token unavailable");
            response.getWriter().write(JSON.toJSONString(responseEntityVo));
            response.flushBuffer();
            return false;
        }
        return true;
    }


}
