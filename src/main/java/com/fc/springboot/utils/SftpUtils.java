package com.fc.springboot.utils;

import com.jcraft.jsch.*;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName SftpUtils
 * @Description
 * @Author fc
 * @Date 2022/4/28 2:45 下午
 * @Version 1.0
 **/
public class SftpUtils {


    private final static org.slf4j.Logger log = LoggerFactory.getLogger(SftpUtils.class);

    /**
     * 下载重试次数
     */
    private static final int DOWNLOAD_RETRY = 3;

    /**
     * 下载重试间隔时间 单位毫秒
     */
    private static final long DOWNLOAD_SLEEP = 3 * 1000;
    private static final SftpUtils SFTP = new SftpUtils();
    private static ChannelSftp client;
    private static Session session;

    /**
     * @return
     */
    public static SftpUtils getInstance() {
        return SFTP;
    }

    /**
     * 获取SFTP连接
     *
     * @param username
     * @param password
     * @param ip
     * @param port
     * @return
     */
    synchronized public ChannelSftp makeConnection(String username, String password, String ip, int port) {
        if (client == null || session == null || !client.isConnected() || !session.isConnected()) {
            try {
                JSch jsch = new JSch();
                session = jsch.getSession(username, ip, port);
                if (password != null) {
                    session.setPassword(password);
                }
                Properties config = new Properties();
                // 设置第一次登陆的时候主机公钥确认提示，可选值：(ask | yes | no)
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.connect();
                //sftp协议
                Channel channel = session.openChannel("sftp");
                channel.connect();
                client = (ChannelSftp) channel;
                log.info("sftp connected success，connect to [{}:{}], username [{}]", ip, port, username);
            } catch (JSchException e) {
                log.error("sftp connected fail，connect to [{}:{}], username [{}], password [{}], error message : [{}]", ip, port, username, password, e.getMessage());
            }
        }
        return client;
    }

    /**
     * 关闭连接 server
     */
    public static void close() {
        if (client != null && client.isConnected()) {
            client.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }


    /**
     * 单次下载文件
     *
     * @param downloadFile 下载文件地址
     * @param saveFile     保存文件地址
     * @param ip           主机地址
     * @param port         主机端口
     * @param username     用户名
     * @param password     密码
     * @param rootPath     根目录
     * @return
     */
    public synchronized static File download(String downloadFile, String saveFile, String ip, Integer port, String username, String password, String rootPath) {
        boolean result = false;
        File file = null;
        Integer i = 0;
        while (!result) {
            //获取连接
            ChannelSftp sftp = getInstance().makeConnection(username, password, ip, port);
            FileOutputStream fileOutputStream = null;
            log.info("sftp file download start, target filepath is {}, save filepath is {}", downloadFile, saveFile);
            try {
                sftp.cd(rootPath);
                file = new File(saveFile);
                if (file.exists()) {
                    file.delete();
                } else {
                    file.createNewFile();
                }
                fileOutputStream = new FileOutputStream(file);
                sftp.get(downloadFile, fileOutputStream);
                result = true;
            } catch (FileNotFoundException e) {
                log.error("sftp file download fail, FileNotFound: [{}]", e.getMessage());
            } catch (IOException e) {
                log.error("sftp file download fail, IOException: [{}]", e.getMessage());
            } catch (SftpException e) {
                i++;
                log.error("sftp file download fail, sftpException: [{}]", e.getMessage());
                if (i > DOWNLOAD_RETRY) {
                    log.error("sftp file download fail, retry three times, SftpException: [{}]", e.getMessage());
                    return file;
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(DOWNLOAD_SLEEP);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            SftpUtils.close();

        }
        return file;
    }

    /**
     * 下载文件
     *
     * @param downloadFile 下载文件的路径
     * @param saveFile     保存的路径
     * @param rootPath     根目录
     * @return
     */
    public synchronized static File download(String downloadFile, String saveFile, String rootPath) {
        boolean result = false;
        File file = null;
        Integer i = 0;
        while (!result) {
            FileOutputStream fileOutputStream = null;
            log.info("sftp file download start, target filepath is {}, save filepath is {}", downloadFile, saveFile);
            try {
                //获取连接、读取文件(ChannelSftp) session.openChannel("sftp")
                client.cd(rootPath);
                file = new File(saveFile);
                if (file.exists()) {
                    file.delete();
                } else {
                    file.createNewFile();
                }
                fileOutputStream = new FileOutputStream(file);
                client.get(downloadFile, fileOutputStream);
                result = true;
            } catch (FileNotFoundException e) {
                log.error("sftp file download fail, FileNotFound: [{}]", e.getMessage());
            } catch (IOException e) {
                log.error("sftp file download fail, IOException: [{}]", e.getMessage());
            } catch (SftpException e) {
                i++;
                log.error("sftp file download fail, sftpException: [{}]", e.getMessage());
                if (i > DOWNLOAD_RETRY) {
                    log.error("sftp file download fail, retry three times, SftpException: [{}]", e.getMessage());
                    return file;
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(DOWNLOAD_SLEEP);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    public static void main(String[] args) {
        String downloadFile = "text.txt";
        String saveFile = "/Users/fc/Documents/work/2.txt";
        String ip = "106.15.179.151";
        Integer port = 22;
        String username = "root";
        String password = "Fc@12345";
        String rootPath = "/usr/local/codes/sftp";
        download(downloadFile, saveFile, ip, port, username, password, rootPath);
    }
}
