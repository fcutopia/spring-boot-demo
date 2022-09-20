/*
 * Copyright 2019 The FATE Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fc.springboot.exceptions;

import com.alibaba.fastjson.JSON;
import com.fc.springboot.bean.response.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        log.error("系统异常:", e);
        return new ResponseEntity(1001, "系统异常");
    }

    /**
     * 处理 Exception 异常
     *
     * @param httpServletRequest httpServletRequest
     * @param e                  异常
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity exceptionHandler(HttpServletRequest httpServletRequest, Exception e) {
        log.error("服务错误:", e);
        return new ResponseEntity(1002, "服务出错");
    }

    /**
     * 处理 BusinessException 异常
     *
     * @param httpServletRequest httpServletRequest
     * @param e                  异常
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = FateException.class)
    public ResponseEntity businessExceptionHandler(HttpServletRequest httpServletRequest, FateException e) {
        log.info("业务异常 code:" + e.getCode() + "msg:" + e.getMsg());
        return new ResponseEntity(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity(1003, "参数异常 errors:" + JSON.toJSONString(errors));

    }
}
