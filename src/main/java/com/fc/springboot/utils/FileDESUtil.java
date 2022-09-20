package com.fc.springboot.utils;


import com.fc.springboot.constant.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import java.io.*;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName FileDESUtil
 * @Description
 * @Author fc
 * @Date 2022/2/24 1:45 下午
 * @Version 1.0
 **/
@Component
public class FileDESUtil {


    public Key key;


    /**
     * 生成密匙
     */
    public FileDESUtil() {
        initKey();
    }

    /**
     * 根据参数生成KEY
     */
    @PostConstruct
    public void initKey() {
        try {
            KeyGenerator _generator = KeyGenerator.getInstance("DES");
            _generator.init(new SecureRandom(Constants.FILE_ENCODE_KEY.getBytes()));
            this.key = _generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException("Error initializing SqlMap class. Cause: " + e);
        }
    }

    /**
     * 文件流加密
     *
     * @param file
     * @return
     * @throws Exception
     */
    public InputStream encrypt(InputStream file) throws Exception {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, this.key);
        CipherInputStream cis = new CipherInputStream(file, cipher);
        return cis;
    }

    /**
     * 文件流解密
     *
     * @param file
     * @return
     * @throws Exception
     */
    public InputStream decrypt(InputStream file) throws Exception {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, this.key);
        CipherInputStream cis = new CipherInputStream(file, cipher);
        return cis;
    }

    /**
     * 文件加密
     *
     * @param file
     * @param destFile
     * @throws Exception
     */
    public void encrypt(String file, String destFile) throws Exception {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, this.key);
        InputStream is = new FileInputStream(file);
        OutputStream out = new FileOutputStream(destFile);
        CipherInputStream cis = new CipherInputStream(is, cipher);
        byte[] buffer = new byte[1024];
        int r;
        while ((r = cis.read(buffer)) > 0) {
            out.write(buffer, 0, r);
        }
        cis.close();
        is.close();
        out.close();
    }

    /**
     * 文件加密
     *
     * @param is
     * @param destFile
     * @throws Exception
     */
    public void encrypt(InputStream is, String destFile) throws Exception {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, this.key);
        OutputStream out = new FileOutputStream(destFile);
        CipherInputStream cis = new CipherInputStream(is, cipher);
        byte[] buffer = new byte[1024];
        int r;
        while ((r = cis.read(buffer)) > 0) {
            out.write(buffer, 0, r);
        }
        cis.close();
        is.close();
        out.close();
    }

    /**
     * 文件解密
     *
     * @param is
     * @param dest
     * @throws Exception
     */
    public void decrypt(InputStream is, String dest) throws Exception {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, this.key);
        OutputStream out = new FileOutputStream(dest);
        CipherOutputStream cos = new CipherOutputStream(out, cipher);
        byte[] buffer = new byte[1024];
        int r;
        while ((r = is.read(buffer)) >= 0) {
            System.out.println();
            cos.write(buffer, 0, r);
        }
        cos.close();
        out.close();
        is.close();
    }


    /**
     * 文件解密
     *
     * @param file
     * @param dest
     * @throws Exception
     */
    public void decrypt(String file, String dest) throws Exception {

        InputStream is = new FileInputStream(file);
        OutputStream out = new FileOutputStream(dest);

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, this.key);
        CipherOutputStream cos = new CipherOutputStream(out, cipher);
        byte[] buffer = new byte[1024];
        int r;
        while ((r = is.read(buffer)) >= 0) {
            System.out.println();
            cos.write(buffer, 0, r);
        }
        cos.close();
        out.close();
        is.close();
    }

    public static void main(String[] args) throws Exception {
        //密钥
        FileDESUtil fileDESUtil = new FileDESUtil();
        fileDESUtil.encrypt("/Users/fc/Documents/work/pn_md5_2.csv", "/Users/fc/Documents/work/encode/enc.txt");
        String encPath = "/Users/fc/Documents/work/encode/enc.txt";
        InputStream is = new FileInputStream(encPath);
        fileDESUtil.decrypt(is, "/Users/fc/Documents/work/encode/dec.csv");
        //fileDESUtil.decrypt("/Users/fc/Documents/work/def.txt", "/Users/fc/Documents/work/abc.csv");

    }


    /**
     * 文件名称转化
     */
    public static void filePath() {
        String path = "/Users/fc/Documents/work/encode/pn_md5_2.csv";
        List<String> list = Arrays.asList(path.split("/"));
        StringBuffer sbf = new StringBuffer();
        list.stream().limit(list.size() - 1).forEach(o -> {
            if (StringUtils.isNotBlank(o)) {
                sbf.append("/").append(o);
            }
        });
        String fileNameStr = list.get(list.size() - 1);
        String newPath = fileNameStr.substring(0, fileNameStr.length() - 4) + "_decrypt.csv";
        sbf.append("/").append(newPath);
        System.out.println(sbf.toString());
    }


}
