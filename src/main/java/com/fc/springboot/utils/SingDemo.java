package com.fc.springboot.utils;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName
 * @Description
 * @Author fc
 * @Date 2022/7/01 15:21 上午
 * @Version 1.0
 **/
public class SingDemo {

    private static ThreadLocal<MessageDigest> messageDigestHolder = new ThreadLocal<MessageDigest>();

    /**
     * 用来将字节转换成 16 进制表示的字符
     */
    static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static void main(String[] args) {
        String userCode = "sfWnagSfNqSKwHAU";
        String authCode = "ef6e1a6846f8347ff3f4e3e9278e3b95";
        String token = getToken(userCode, authCode);
        String result = single(token);
        System.out.println(result);

    }

    /**
     * 标准标签查询
     *
     * @param token
     * @return
     */
    public static String single(String token) {
        String url = "https://test-api.yunzhenxin.com:60443/credit/standard/service/single";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("serviceId", "xOVABa6r44eycyc9c00zoK");
        jsonObject.put("idType", "phone");
        jsonObject.put("idValue", "61f7cb7695cbc879d31fd99315c9f4df");
        return doPostForJson(url, jsonObject.toJSONString(), headers).getString("data");

    }

    /**
     * 鉴权接口
     *
     * @param userCode
     * @param authCode
     * @return
     */
    public static String getToken(String userCode, String authCode) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String sign1 = getMD5Format((userCode + getMD5Format(timestamp)));
        String sign2 = getMD5Format((userCode + authCode + getMD5Format(timestamp)));
        String authUrl = "https://test-api.yunzhenxin.com:60443/credit/auth";
        JSONObject authParam = new JSONObject();
        authParam.put("userCode", userCode);
        authParam.put("timestamp", timestamp);
        authParam.put("sign1", sign1);
        authParam.put("sign2", sign2);
        return doPostForJson(authUrl, authParam.toJSONString(), null).getString("data");
    }

    public static JSONObject doPostForJson(String url, String jsonParams, Map<String, String> headers) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        JSONObject jsonObject = null;
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().
                setConnectTimeout(180 * 1000).setConnectionRequestTimeout(180 * 1000)
                .setSocketTimeout(180 * 1000).setRedirectsEnabled(true).build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type", "application/json");
        if (MapUtils.isNotEmpty(headers)) {
            headers.keySet().stream().forEach(key -> {
                httpPost.setHeader(key, headers.get(key));
            });
        }
        try {
            httpPost.setEntity(new StringEntity(jsonParams, ContentType.create("application/json", "utf-8")));

            HttpResponse response = httpClient.execute(httpPost);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                jsonObject = JSONObject.parseObject(result);
                return jsonObject;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return jsonObject;
        }
    }

    public static String getMD5Format(String data) {
        try {
            MessageDigest message = messageDigestHolder.get();
            if (message == null) {
                message = java.security.MessageDigest.getInstance("MD5");
                messageDigestHolder.set(message);
            }
            message.update(data.getBytes());
            byte[] b = message.digest();

            String digestHexStr = "";
            for (int i = 0; i < 16; i++) {
                digestHexStr += byteHEX(b[i]);
            }

            return digestHexStr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     *
     * @Title: byteHEX
     * @Description:
     * @author wujl
     * @param ib
     * @return String 返回类型
     */
    private static String byteHEX(byte ib) {
        char[] ob = new char[2];
        ob[0] = hexDigits[(ib >>> 4) & 0X0F];
        ob[1] = hexDigits[ib & 0X0F];
        String s = new String(ob);
        return s;
    }
}
