package com.fc.springboot.config;

/**
 * @ClassName SSHConfig
 * @Description
 * @Author fc
 * @Date 2022/5/20 2:42 下午
 * @Version 1.0
 **/
public class SSHConfig {

    /**
     * FTP 登录用户名
     */
    private String userName;
    /**
     * FTP 登录密码
     */
    private String passWord;
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
    /**
     * FTP 指定上传路径
     */
    private String uploadUrl;
    /**
     * FTP 指定下载路径
     */
    private String downloadUrl;

    public SSHConfig(String userName, String passWord, String privateKey, String host, int port, String uploadUrl, String downloadUrl) {
        this.userName = userName;
        this.passWord = passWord;
        this.privateKey = privateKey;
        this.host = host;
        this.port = port;
        this.uploadUrl = uploadUrl;
        this.downloadUrl = downloadUrl;
    }

    public SSHConfig(String userName, String passWord, String privateKey, String host, int port) {
        this.userName = userName;
        this.passWord = passWord;
        this.privateKey = privateKey;
        this.host = host;
        this.port = port;
    }

    public SSHConfig(String userName, String passWord, String host, int port) {
        this.userName = userName;
        this.passWord = passWord;
        this.host = host;
        this.port = port;
    }

    public SSHConfig() {
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }


    // -------------------------------------------------------------------------------
    public SSHConfig userName(String userName) {
        this.userName = userName;
        return this;
    }

    public SSHConfig passWord(String passWord) {
        this.passWord = passWord;
        return this;
    }

    public SSHConfig privateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public SSHConfig host(String host) {
        this.host = host;
        return this;
    }

    public SSHConfig port(int port) {
        this.port = port;
        return this;
    }

    /**
     * 默认配置
     *
     * @return
     */
    public SSHConfig getDefaultConfig() {
        this.userName = "username";
        this.passWord = "password";
        this.host = "0.0.0.0";
        this.port = 22;
        this.uploadUrl = "/home/";
        return this;
    }


    @Override
    public String toString() {
        return "com.yzx.sshutil.SSHConfig{" +
                "userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", uploadUrl='" + uploadUrl + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }
}
