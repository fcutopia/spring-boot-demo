package com.fc.springboot.utils;

import com.alibaba.fastjson.JSONObject;
import com.fc.springboot.config.SSHConfig;
import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.IOUtils;


import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.Vector;

/**
 * @Author loukw
 * @ClassName SFTPUtil
 * @Date 2022/5/9 16:15
 * @Deacription sftp文件处理类
 **/
public class SSHUtil {

    private final static Logger logger = LoggerFactory.getLogger(SSHUtil.class);

    private final String charset = "UTF-8";

    private ChannelSftp sftp;

    private ChannelExec channelExec;

    private Session session;

    /**
     * FTP 登录用户名
     */
    private String username;
    /**
     * FTP 登录密码
     */
    private String password;
    /**
     * 私钥
     */
    private String privateKey;
    /**
     * FTP 服务器地址IP地址
     */
    private String host;
    /**
     * FTP 端口
     */
    private int port;


    private SSHUtil() {
    }

    /**
     * 建议使用配置构参方式，可拓展性比较强
     *
     * @param SSHConfig
     */
    public SSHUtil(SSHConfig SSHConfig) {
        this.username = SSHConfig.getUserName();
        this.password = SSHConfig.getPassWord();
        this.privateKey = SSHConfig.getPrivateKey();
        this.host = SSHConfig.getHost();
        this.port = SSHConfig.getPort();
    }


    /**
     * 连接sftp服务器
     *
     * @throws Exception
     */
    public void login() {
        try {
            JSch jsch = new JSch();
            if (privateKey != null) {
                jsch.addIdentity(privateKey);// 设置私钥
                logger.info("sftp connect,path of private key file：{}", privateKey);
            }
            logger.info("sftp connect by host:{} username:{}", host, username);

            session = jsch.getSession(username, host, port);
            logger.info("Session is build");
            if (password != null) {
                session.setPassword(password);
            }
            Properties config = new Properties();
            config.put("PreferredAuthentications", "password");
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            logger.info("Session is connected");

            Channel channel = session.openChannel("sftp");
            channel.connect();
            logger.info("channel is connected");

            sftp = (ChannelSftp) channel;
            logger.info(String.format("sftp server host:[%s] port:[%s] is connect successfull", host, port));
        } catch (JSchException e) {
            logger.error("Cannot connect to specified sftp server : {}:{} \n Exception message is: {}", new Object[]{host, port, e.getMessage()});
        }
    }

    public Session getSession() {
        if (session == null) {
            logger.error("Cannot get SHH Session");
        }
        return session;
    }

    /**
     * 关闭连接 server
     */
    public void logout() {
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
                logger.info("sftp channel is closed already");
            }
        }
        if (channelExec != null) {
            if (channelExec.isConnected()) {
                channelExec.disconnect();
                logger.info("shell channel is closed already");
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
                logger.info("sshSession is closed already");
            }
        }
    }

    /**
     * 将输入流的数据上传到sftp作为文件
     *
     * @param directory    上传到该目录
     * @param sftpFileName sftp端文件名
     * @param input        输入流
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, InputStream input) throws SftpException {
        try {
            sftp.cd(directory);
        } catch (SftpException e) {
            logger.warn("directory is not exist");
            sftp.mkdir(directory);
            sftp.cd(directory);
        }
        sftp.put(input, sftpFileName);
        logger.info("file:{} is upload successful", sftpFileName);
    }

    /**
     * 上传单个文件
     *
     * @param directory  上传到sftp目录
     * @param uploadFile 要上传的文件,包括路径
     * @throws FileNotFoundException
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String uploadFile) throws FileNotFoundException, SftpException {
        File file = new File(uploadFile);
        upload(directory, file.getName(), new FileInputStream(file));
    }

    /**
     * 将byte[]上传到sftp，作为文件。注意:从String生成byte[]是，要指定字符集。
     *
     * @param directory    上传到sftp目录
     * @param sftpFileName 文件在sftp端的命名
     * @param byteArr      要上传的字节数组
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, byte[] byteArr) throws SftpException {
        upload(directory, sftpFileName, new ByteArrayInputStream(byteArr));
    }

    /**
     * 将字符串按照指定的字符编码上传到sftp
     *
     * @param directory    上传到sftp目录
     * @param sftpFileName 文件在sftp端的命名
     * @param dataStr      待上传的数据
     * @param charsetName  sftp上的文件，按该字符编码保存
     * @throws UnsupportedEncodingException
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, String dataStr, String charsetName) throws UnsupportedEncodingException, SftpException {
        upload(directory, sftpFileName, new ByteArrayInputStream(dataStr.getBytes(charsetName)));
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     * @throws SftpException
     * @throws FileNotFoundException
     * @throws Exception
     */
    public void download(String directory, String downloadFile, String saveFile) throws SftpException, FileNotFoundException {
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        File file = new File(saveFile);
        sftp.get(downloadFile, new FileOutputStream(file));
        logger.info("file:{} is download successful", downloadFile);
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件名
     * @return 字节数组
     * @throws SftpException
     * @throws IOException
     * @throws Exception
     */
    public byte[] download(String directory, String downloadFile) throws SftpException, IOException {
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        InputStream is = sftp.get(downloadFile);

        byte[] fileData = IOUtils.readFully(is, -1, true);

        logger.info("file:{} is download successful", downloadFile);
        return fileData;
    }

    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     * @throws SftpException
     * @throws Exception
     */
    public void delete(String directory, String deleteFile) throws SftpException {
        if (isExist(directory, deleteFile)) {
            sftp.cd(directory);
            sftp.rm(deleteFile);
            logger.info("file:{} is delete successful", deleteFile);
        } else {
            logger.info("file:{} is delete failure,because of file is not exist", deleteFile);
        }
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @return
     * @throws SftpException
     */
    public Vector<?> listFiles(String directory) throws SftpException {
        return sftp.ls(directory);
    }


    /**
     * 判断是否存在该文件
     *
     * @param directory 路径
     * @param fileName  文件名称
     * @return
     * @throws SftpException
     */
    public boolean isExist(String directory, String fileName) throws SftpException {
        return isExist(directory + "/" + fileName);
    }


    /**
     * 判断是否存在该文件
     * 文件不存在的话会抛 SftpException，e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE
     *
     * @param path 文件(绝对路径)
     * @return
     * @throws SftpException
     */
    public boolean isExist(String path) throws SftpException {
        boolean flag = false;
        try {
            sftp.stat(path);
            flag = true;
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                logger.info("path:{} is not exist", path);
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 修改文件名
     *
     * @param path    路径
     * @param oldName 原始名称
     * @param newName 修改名称
     * @throws SftpException
     */
    public boolean rename(String path, String oldName, String newName) throws SftpException {
        boolean flag = false;
        try {
            sftp.rename(path + "/" + oldName, path + "/" + newName);
            flag = true;
            logger.info("old file name:[{}] rename to new file name:[{}] successful", oldName, newName);
        } catch (SftpException e) {
            logger.warn("old file name:[{}] is not exist or new file name:[{}] is exist", oldName, newName);
            flag = false;
        }
        return flag;
    }

    public String executeCmd(String command) throws JSchException, IOException {
        logger.info("exec command: {}", command);
        channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);
        channelExec.setInputStream(null);
        channelExec.setErrStream(System.err);
        channelExec.connect();
        BufferedReader err_in = new BufferedReader(new InputStreamReader(channelExec.getErrStream(), Charset.forName(charset)));
        BufferedReader std_in = new BufferedReader(new InputStreamReader(channelExec.getInputStream(), Charset.forName(charset)));
        String errStr = "";
        String stdStr = "";
        while ((errStr = err_in.readLine()) != null) {
            System.out.println("error msg :" + errStr);
        }
        StringBuilder sb = new StringBuilder();
        while ((stdStr = std_in.readLine()) != null) {
            System.out.println("std msg :" + stdStr);
            sb.append(stdStr);
        }
        return sb.toString();
    }

    public static void main(String args[]) {
        if (1 == 2) {
            hostShell();
            return;
        }
        SSHConfig sshConfig = new SSHConfig();
        sshConfig.setHost("192.168.198.131");
        sshConfig.setPort(22);
        sshConfig.setUserName("yzx00101");
        sshConfig.setPassWord("yzx@fc");
        SSHUtil sshUtil = new SSHUtil(sshConfig);
        try {
            sshUtil.login();
            String command = "kubectl exec -n fate-9999 $(kubectl get pod -n fate-9999  | awk '{print $1}' | sort | head -n 8 | tail -n 1) -c client  bash ./upload.sh ";
            String submit = "docker exec -i  confs10000_client_1 bash /qy.sh config1.json dsl2.json";
            String upload = "docker exec -i  confs10000_client_1 bash /Examples/Pipeline/notebooks/upload.sh Examples/Pipeline/notebooks/upload_pn_1000_13_838.json";
            String submit_2 = "docker exec -i  confs10000_client_1 bash /Examples/Pipeline/notebooks/submit.sh Examples/Pipeline/notebooks/config_1658304306359582.json Examples/Pipeline/notebooks/dsl_1658304306359550.json";
            String bind = "kubectl exec -it `kubectl get po -n fate-9999 | grep python  | awk '{print $1}'` -c client -n fate-9999 -- python /inference/fea_bind.py arbiter-9999#guest-10000#host-9999#model 20220803093853099610441";
            String result = sshUtil.executeCmd(bind);
            System.out.println(result);
            /*String modelId = JSONObject.parseObject("data").getJSONObject("model_info").getString("model_id");
            String modelVersion = JSONObject.parseObject("data").getJSONObject("model_info").getString("model_version");
            String jobId = JSONObject.parseObject("data").getString("jobId");*/

           // String deploy = "docker exec -i  confs10000_client_1 bash /qy.sh " + modelId + " " + modelVersion;
           // String deployResult = sshUtil.executeCmd(deploy);
          //  System.out.println(deployResult);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sshUtil.logout();
        }
    }


    public static void hostShell(){
        SSHConfig sshConfig = new SSHConfig();
        sshConfig.setHost("192.168.198.131");
        sshConfig.setPort(22);
        sshConfig.setUserName("yzx00101");
        sshConfig.setPassWord("yzx@fc");
        SSHUtil sshUtil = new SSHUtil(sshConfig);
        try {
            sshUtil.login();
            String upload = "kubectl exec -it `kubectl get po -n fate-9999 | grep python  | awk '{print $1}'` -c client -n fate-9999 -- sh /Examples/Pipeline/notebooks/upload.sh  /Examples/Pipeline/notebooks/upload_pn_1000_13_957.json";
            String result = sshUtil.executeCmd(upload);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sshUtil.logout();
        }
    }

    public static void testJschPool() {
        SSHConfig sshConfig = new SSHConfig();
        sshConfig.setHost("192.168.198.211");
        sshConfig.setPort(65412);
        sshConfig.setUserName("yzx00101");
        sshConfig.setPassWord("yzx@fc");
        SSHUtil sshUtil = new SSHUtil(sshConfig);
        try {
            sshUtil.login();
            Session session = sshUtil.getSession();
            try {
                ChannelExec exec = (ChannelExec) session.openChannel("exec");
                exec.setCommand("sudo docker exec -i confs10000_client_1 bash");
                InputStream in = exec.getInputStream();
                OutputStream out = exec.getOutputStream();
                exec.connect();
                out.write(("yzx@fc" + "\n").getBytes()); //这里是密码后跟了一个换行符
                out.flush();
                sshUtil.executeCmd("ls -la");
                byte[] tmp = new byte[1024];
                exec.disconnect();
            } catch (JSchException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                sshUtil.logout();
                ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

