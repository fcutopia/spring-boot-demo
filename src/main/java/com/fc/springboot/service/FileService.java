package com.fc.springboot.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName FileService
 * @Description
 * @Author fc
 * @Date 2022/5/6 2:51 下午
 * @Version 1.0
 **/
@Service
public class FileService {

    @Autowired
    private RestTemplate restTemplate;

    public void handEvent(String fileName) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            InputStream inputStream = null;
            //下载文件
            inputStream = new FileInputStream(fileName);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int temp;
            while ((temp = inputStream.read(bytes)) != -1) {
                byteArrayOutputStream.write(bytes, 0, temp);
            }
            //文件流转byte[]
            byte[] by = byteArrayOutputStream.toByteArray();
            String url = "http://127.0.0.1:8006/upload/receiveFile";
            HttpEntity<byte[]> httpEntity = new HttpEntity<>(by, headers);
            HttpEntity<String> result = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class);
            System.out.println("--------上传结果：{}" + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        JSONObject cancelParam = new JSONObject();
        cancelParam.put("job_id", "202205180246594425731");
        ResponseEntity<JSONObject> cancelResponse = restTemplate.postForEntity("https://fateboard-client01-test.yunzhenxin.com/job/v1/pipeline/job/stop", cancelParam, JSONObject.class);
        System.out.println(cancelResponse.getBody());
    }
}
