package com.fc.springboot.service;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.web.multipart.MultipartFile;


/**
 * 生成线上全量数据布隆过滤器
 */
public class HdfsService {

    public static String hdfsPath = "hdfs://192.168.198.105";

    public static String hdfsName = "hdfs";

    public static void main(String[] args) {
        String path = "/tmp/pn/pn_1000.txt";
        try {
            readFile(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 读取HDFS文件内容
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static String readFile(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!existFile(path)) {
            return null;
        }
        FileSystem fs = getFileSystem();
        // 目标路径
        Path srcPath = new Path(path);
        FSDataInputStream inputStream = null;
        int limit = 1670693076;
        //初始化布隆过滤器
        FileOutputStream outputStream = new FileOutputStream("/Users/fc/Documents/bloomFilter_test.txt");
        BloomFilter<CharSequence> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), limit, 0.03);
        try {
            inputStream = fs.open(srcPath);
            // 防止中文乱码
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String lineTxt = "";
            while ((lineTxt = reader.readLine()) != null) {
                bloomFilter.put(lineTxt);
            }
            bloomFilter.writeTo(outputStream);
            outputStream.close();
            //return sb.toString();
            return "success";
        } finally {
            inputStream.close();
            fs.close();
        }
    }


    /**
     * HDFS创建文件
     *
     * @param path
     * @param file
     * @throws Exception
     */
    public static void createFile(String path, MultipartFile file) throws Exception {
        if (StringUtils.isEmpty(path) || null == file.getBytes()) {
            return;
        }
        String fileName = file.getOriginalFilename();
        FileSystem fs = getFileSystem();
        // 上传时默认当前目录，后面自动拼接文件的目录
        Path newPath = new Path(path + "/" + fileName);
        // 打开一个输出流
        FSDataOutputStream outputStream = fs.create(newPath);
        outputStream.write(file.getBytes());
        outputStream.close();
        fs.close();
    }


    /**
     * 判断HDFS文件是否存在
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static boolean existFile(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        FileSystem fs = getFileSystem();
        Path srcPath = new Path(path);
        boolean isExists = fs.exists(srcPath);
        return isExists;
    }


    /**
     * 获取HDFS文件系统对象
     *
     * @return
     * @throws Exception
     */
    public static FileSystem getFileSystem() throws Exception {
        // 客户端去操作hdfs时是有一个用户身份的，默认情况下hdfs客户端api会从jvm中获取一个参数作为自己的用户身份
        // DHADOOP_USER_NAME=hadoop
        // 也可以在构造客户端fs对象时，通过参数传递进去
        FileSystem fileSystem = FileSystem.get(new URI(hdfsPath), getConfiguration(), hdfsName);
        return fileSystem;
    }

    /**
     * 获取HDFS配置信息
     *
     * @return
     */
    private static Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", hdfsPath);
        return configuration;
    }


}
