package com.fc.springboot;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.*;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName HdfsProcess
 * @Description
 * @Author fc
 * @Date 2022/5/7 10:43 上午
 * @Version 1.0
 **/
public class HdfsProcess {

    public static String hdfsPath = "hdfs://hzxs-yzx-hadoop-ds";

    public static String hdfsName = "coop";

    /**
     * 核心线程数（默认线程数）
     */
    private static final int corePoolSize = 6;
    /**
     * 最大线程数
     */
    private static final int maxPoolSize = 30;
    /**
     * 允许线程空闲时间（单位：默认为秒）
     */
    private static final int keepAliveTime = 30;
    /**
     * 缓冲队列大小
     */
    private static final int queueCapacity = 10000;
    /**
     * 线程池名前缀
     */
    private static final String threadNamePrefix = "hdl-uhi-service-";

    private static ThreadPoolTaskExecutor taskExecutor;

    /**
     * 布隆过滤器文件路径
     */
    private static String filterPath = "";


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
        // 这个解决hdfs问题
        configuration.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        // 这个解决本地file问题
        configuration.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
        return configuration;
    }


    /**
     * 初始化线程池
     * @return
     */static {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setThreadNamePrefix(threadNamePrefix);
        // 线程池对拒绝任务的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        taskExecutor = executor;
    }


    /**
     * 遍历全量数据求交集
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static void inner(List<String> list, String path, CountDownLatch countDownLatch, BloomFilter<CharSequence> bloomFilter) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("开始全量扫描：file=" + sdf.format(new Date()) + ",时间：" + sdf.format(new Date()));
        FileSystem fs = null;
        FSDataInputStream inputStream = null;
        try {
            Path srcPath = new Path(path);
            fs = getFileSystem();
            inputStream = fs.open(srcPath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String lineTxt = "";
            while ((lineTxt = reader.readLine()) != null) {
                if (bloomFilter.mightContain(lineTxt)) {
                    list.add(lineTxt);
                }
            }
            System.out.println("全量扫描结束：file=" + sdf.format(new Date()) + ",时间：" + sdf.format(new Date()));
        } catch (Exception e) {
            System.out.println("全量扫描失败：");
            e.printStackTrace();
        } finally {
            inputStream.close();
            fs.close();
            countDownLatch.countDown();
        }
    }

    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        List<String> filePaths = new ArrayList<String>();
        filePaths.add("/tmp/csv/pn_6/part-00000-0c0e95f7-37d2-49e4-b227-501d95f75364-c000.csv");
        filePaths.add("/tmp/csv/pn_6/part-00001-0c0e95f7-37d2-49e4-b227-501d95f75364-c000.csv");
        filePaths.add("/tmp/csv/pn_6/part-00002-0c0e95f7-37d2-49e4-b227-501d95f75364-c000.csv");
        filePaths.add("/tmp/csv/pn_6/part-00003-0c0e95f7-37d2-49e4-b227-501d95f75364-c000.csv");
        filePaths.add("/tmp/csv/pn_6/part-00004-0c0e95f7-37d2-49e4-b227-501d95f75364-c000.csv");
        filePaths.add("/tmp/csv/pn_6/part-00005-0c0e95f7-37d2-49e4-b227-501d95f75364-c000.csv");
        Map<String, List<String>> map = new HashMap<>();
        filePaths.stream().forEach(o -> {
            map.put(o.split("-")[1], new ArrayList<>());
        });
        CountDownLatch countDownLatch = new CountDownLatch(6);
        try {
            FileInputStream inputStream = new FileInputStream(System.getProperty("user.dir") + "/bloomFilter_test.txt");
            BloomFilter<CharSequence> bloomFilter = BloomFilter.readFrom(inputStream, Funnels.stringFunnel(Charsets.UTF_8));
            filePaths.stream().forEach(path -> {
                taskExecutor.execute(() -> {
                    try {
                        inner(map.get(path.split("-")[1]), path, countDownLatch, bloomFilter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
            System.out.println("主线程暂停");
            countDownLatch.await();
            System.out.println("任务线程执行结束：" + JSON.toJSONString(map));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
