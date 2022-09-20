package com.fc.springboot.utils;


import com.jcraft.jsch.*;
import com.oracle.tools.packager.Log;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.SystemException;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @ClassName SftpUtilV2
 * @Description
 * @Author fc
 * @Date 2022/4/28 3:29 下午
 * @Version 1.0
 **/
public class SftpUtilV2 {


    private final static org.slf4j.Logger log = LoggerFactory.getLogger(SftpUtilV2.class);


    private static String sftp_ip = "192.168.198.211";
    private static Integer sftp_port = 65412;
    private static String sftp_username = "yzx00101";
    private static String sftp_password = "yzx@fc";

    /*private static String sftp_ip = "106.15.179.151";
    private static Integer sftp_port = 22;
    private static String sftp_username = "root";
    private static String sftp_password = "Fc@12345";*/


    /**
     * sftp存储根目录
     */
    public static String windows_path = "/Users/fc/Documents/work/sftp";
    public static String linux_path = "/usr/local/codes/upload";
    private Session session;
    private ChannelSftp channel;
    /**
     * 规避多线程并发不断开问题
     */
    private volatile static ThreadLocal<SftpUtilV2> sftpLocal = new ThreadLocal<>();

    private SftpUtilV2() {
    }

    private SftpUtilV2(String host, Integer port, String username, String password) {
        super();
        init(host, port, username, password);
    }

    /**
     * 获取本地线程存储的sftp客户端，使用玩必须调用 release()释放连接
     *
     * @return
     * @throws Exception
     */
    public static SftpUtilV2 getSftpUtil() {
        SftpUtilV2 sftpUtil = sftpLocal.get();
        if (null == sftpUtil || !sftpUtil.isConnected()) {
            sftpLocal.set(new SftpUtilV2(sftp_ip, sftp_port, sftp_username, sftp_password));
        }
        return sftpLocal.get();
    }

    /**
     * 获取本地线程存储的sftp客户端，使用玩必须调用 release()释放连接
     *
     * @param host
     * @param port
     * @param username
     * @param password
     * @return
     */
    public static SftpUtilV2 getSftpUtil(String host, Integer port, String username, String password) {
        SftpUtilV2 sftpUtil = sftpLocal.get();
        if (null == sftpUtil || !sftpUtil.isConnected()) {
            log.info("建立连接");
            sftpLocal.set(new SftpUtilV2(host, port, username, password));
        } else {
            log.info("连接已经存在");
        }
        return sftpLocal.get();
    }

    /**
     * 初始化 创建一个新的 SFTP 通道
     *
     * @param host
     * @param port
     * @param username
     * @param password
     */
    private void init(String host, Integer port, String username, String password) {
        try {
            //场景JSch对象
            JSch jSch = new JSch();
            // jsch.addIdentity(); 私钥
            session = jSch.getSession(username, host, port);
            // 第一次登陆时候提示, (ask|yes|no)
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            config.put("compression.s2c", "zlib,none");
            config.put("compression.c2s", "zlib,none");
            session.setConfig(config);
            //设置超时
//            session.setTimeout(10*1000);
            //设置密码
            session.setPassword(password);
            session.connect();
            //打开SFTP通道
            channel = (ChannelSftp) session.openChannel("sftp");
            //建立SFTP通道的连接
            channel.connect();
            // 失败重试2次  失败不管了，只发送一次 失败回复  并行调用所有节点
        } catch (JSchException e) {
            log.error("initsftp异常，可能是获得连接错误，请检查用户名密码或者重启sftp服务" + e);
        }
    }

    /**
     * 是否已连接
     *
     * @return
     */
    private boolean isConnected() {
        return null != channel && channel.isConnected();
    }

    /**
     * 关闭通道
     */
    public void closeChannel() {
        if (null != channel) {
            try {
                channel.disconnect();
            } catch (Exception e) {
                log.error("关闭SFTP通道发生异常:", e);
            }
        }
        if (null != session) {
            try {
                session.disconnect();
            } catch (Exception e) {
                log.error("SFTP关闭 session异常:", e);
            }
        }
    }

    /**
     * 每次连接必须释放资源，类似OSS服务
     * 释放本地线程存储的sftp客户端
     */
    public static void release() {
        if (null != sftpLocal.get()) {
            sftpLocal.get().closeChannel();
            sftpLocal.set(null);
        }
    }

    /**
     * 列出目录下文件，只列出文件名字，没有类型
     *
     * @param dir 目录
     * @return
     */
    public List list(String dir) {
        if (channel == null) {
            log.error("获取sftp连接失败，请检查" + sftp_ip + ":" + sftp_port + "@" + sftp_username + "  " + sftp_password + "是否可以访问");
            return null;
        }
        Vector<ChannelSftp.LsEntry> files = null;
        try {
            files = channel.ls(dir);
        } catch (SftpException e) {
            log.error(e.getMessage());
        }
        if (null != files) {
            List fileNames = new ArrayList<String>();
            Iterator<ChannelSftp.LsEntry> iter = files.iterator();
            while (iter.hasNext()) {
                String fileName = iter.next().getFilename();
                if (StringUtils.equals(".", fileName) || StringUtils.equals("..", fileName)) {
                    continue;
                }
                fileNames.add(fileName);
            }
            return fileNames;
        }
        return null;
    }

    /**
     * 列出文件详情
     *
     * @param dir
     * @return
     */
    public List listDetail(String dir) {
        if (channel == null) {
            log.error("获取sftp连接失败，请检查" + sftp_ip + +sftp_port + "@" + sftp_username + "  " + sftp_password + "是否可以访问");
            return null;
        }
        Vector<ChannelSftp.LsEntry> files = null;
        try {
            files = channel.ls(dir);
        } catch (SftpException e) {
            log.error("listDetail 获取目录列表 channel.ls " + dir + "失败 " + e);
        }
        if (null != files) {
            List<Map<String, String>> fileList = new ArrayList<>();
            Iterator<ChannelSftp.LsEntry> iter = files.iterator();
            while (iter.hasNext()) {
                ChannelSftp.LsEntry next = iter.next();
                Map<String, String> map = new HashMap<>();
                String fileName = next.getFilename();
                if (StringUtils.equals(".", fileName) || StringUtils.equals("..", fileName)) {
                    continue;
                }
                String size = String.valueOf(next.getAttrs().getSize());
                long mtime = next.getAttrs().getMTime();
                String type = "";
                String longname = String.valueOf(next.getLongname());
                if (longname.startsWith("-")) {
                    type = "file";
                } else if (longname.startsWith("d")) {
                    type = "dir";
                }
                map.put("name", fileName);
                map.put("size", size);
                map.put("type", type);
                //mtime
                map.put("mtime", DateUtils.getDate2Str(new Date(mtime)));
                fileList.add(map);
            }
            return fileList;
        }
        return null;
    }

    /**
     * 递归获得文件path下所有文件列表
     *
     * @param path
     * @param list
     * @return
     * @throws SftpException
     */
    public List<String> listOfRecursion(String path, List<String> list) throws SftpException {
        if (channel == null) {
            log.error("获取sftp连接失败，请检查" + sftp_ip + +sftp_port + "@" + sftp_username + "" + sftp_password + "是否可以访问");
            return null;
        }
        Vector<ChannelSftp.LsEntry> files = null;
        files = channel.ls(path);
        for (ChannelSftp.LsEntry entry : files) {
            if (!entry.getAttrs().isDir()) {
                String str = path + "/" + entry.getFilename();
                str = str.replace("//", "/");
                list.add(str);
            } else {
                if (!entry.getFilename().equals(".") && !entry.getFilename().equals("..")) {
                    listOfRecursion(path + "/" + entry.getFilename(), list);
                }
            }
        }
        log.debug(list.toString());
        return list;
    }

    /**
     * @param file       上传文件
     * @param remotePath 服务器存放路径，支持多级目录
     * @throws SystemException
     */
    public void upload(File file, String remotePath) throws SystemException {
        if (channel == null) {
            log.error("获取sftp连接失败，请检查" + sftp_ip + +sftp_port + "@" + sftp_username + "" + sftp_password + "是否可以访问");
        }
        FileInputStream fileInputStream = null;
        try {
            if (file.isFile()) {
                String rpath = remotePath;//服务器要创建的目录
                try {
                    createDir(rpath);
                } catch (Exception e) {
                    Log.info("创建路径失败：" + rpath);
                }
                channel.cd(remotePath);
                System.out.println(remotePath);
                fileInputStream = new FileInputStream(file);
                channel.put(fileInputStream, file.getName());
            }
        } catch (FileNotFoundException e) {
            Log.info("上传文件没有找到");
        } catch (SftpException e) {
            Log.info("上传ftp服务器错误");
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param file       上传文件
     * @param remoteName 上传文件名字
     * @param remotePath 服务器存放路径，支持多级目录
     * @throws SystemException
     */
    public boolean upload(File file, String remoteName, String remotePath) {
        if (channel == null) {
            System.out.println("get sftp connect fail，please reboot sftp client");
            log.error("获取sftp连接失败，请检查" + sftp_ip + +sftp_port + "@" + sftp_username + "" + sftp_password + "是否可以访问");
        } else {
            FileInputStream fileInputStream = null;
            try {
                if (file.isFile()) {
                    //服务器要创建的目录
                    String rpath = remotePath;
                    createDir(rpath);
                    channel.cd(remotePath);
                    log.error(remotePath + "  " + remoteName);
                    fileInputStream = new FileInputStream(file);
                    channel.put(fileInputStream, remoteName);
                    return true;
                }
            } catch (FileNotFoundException e) {
                log.error("上传文件没有找到", e.getMessage());
                return false;
            } catch (SftpException e) {
                log.error("upload" + remotePath + e);
                return false;
            } finally {
                try {
                    if (fileInputStream != null) {
                        fileInputStream.close();//这里要关闭文件流
                    } else {
                        log.error("流不存在" + remotePath + "  " + remoteName);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // try to delete the file immediately
//                boolean deleted = false;
//                try {
//                    deleted = file.delete();
//                } catch (SecurityException e) {
//                    log.error(e.getMessage());
//                }
//                // else delete the file when the program ends
//                if (deleted) {
//                    System.out.println("Temp file deleted.");
//                    log.info("Temp file deleted.");
//                } else {
//                    file.deleteOnExit();
//                    System.out.println("Temp file scheduled for deletion.");
//                    log.info("Temp file scheduled for deletion.");
//                }
            }
        }
        return false;
    }

    /**
     * 上传文件并执行
     *
     * @param inputStream
     * @param remoteName
     * @param remotePath
     * @return
     */
    public boolean upload(InputStream inputStream, String remoteName, String remotePath) {
        if (channel == null) {
            log.error("获取sftp连接失败，请检查" + sftp_ip + +sftp_port + "@" + sftp_username + "" + sftp_password + "是否可以访问");
        } else {
            try {
                List<String> msgQueue = new ArrayList<>();
                //服务器要创建的目录
                String rpath = remotePath;
                createDir(rpath);
                channel.cd(remotePath);
                log.debug(remotePath + "  " + remoteName);
                AtomicBoolean isDone = new AtomicBoolean(false);
                PySftpProgressMonitor monitor = new PySftpProgressMonitor(session, msgQueue, isDone);
                channel.put(inputStream, remoteName, monitor, ChannelSftp.OVERWRITE);
                while (!isDone.get()) {
                    // System.out.println();
                }
                log.info("python 算法执行完毕...");
                return true;
            } catch (SftpException e) {
                log.error("upload路径不存在" + remotePath + e);
                return false;
            } finally {
                try {
                    if (inputStream != null) {
                        //这里要关闭文件流
                        inputStream.close();
                    } else {
                        log.error("流不存在" + remotePath + "  " + remoteName);
                    }
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
        return false;
    }

    /**
     * 下载文件
     *
     * @param rootDir
     * @param filePath
     * @return
     */
    public File downFile(String rootDir, String filePath) {
        if (channel == null) {
            log.error("获取sftp连接失败，请检查" + sftp_ip + +sftp_port + "@" + sftp_username + "  " + sftp_password + "是否可以访问");
            return null;
        }
        OutputStream outputStream = null;
        File file = null;
        try {
            channel.cd(rootDir);
            String folder = System.getProperty("java.io.tmpdir");
            file = new File(folder + File.separator + filePath.substring(filePath.lastIndexOf("/") + 1));
//            file = new File(filePath.substring(filePath.lastIndexOf("/") + 1));
            outputStream = new FileOutputStream(file);
            channel.get(filePath, outputStream);
        } catch (SftpException e) {
            log.error("downFile" + filePath + e);
            file = null;
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException", e);
            file = null;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 创建一个文件目录
     */
    public void createDir(String createpath) {
        try {
            if (isDirExist(createpath)) {
                this.channel.cd(createpath);
                return;
            }
            String pathArry[] = createpath.split("/");
            StringBuffer filePath = new StringBuffer("/");
            for (String path : pathArry) {
                if (path.equals("")) {
                    continue;
                }
                filePath.append(path + "/");
                if (isDirExist(filePath.toString())) {
                    channel.cd(filePath.toString());
                } else {
                    // 建立目录
                    channel.mkdir(filePath.toString());
                    // 进入并设置为当前目录
                    channel.cd(filePath.toString());
                }
            }
            this.channel.cd(createpath);
        } catch (SftpException e) {
            log.error("createDir" + createpath + e);
        }
    }

    /**
     * 判断目录是否存在
     */
    public boolean isDirExist(String directory) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = channel.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }

    static String PS = "/";

    /**
     * This method is called recursively to download the folder content from SFTP server
     *
     * @param sourcePath
     * @param destinationPath
     * @throws SftpException
     */
    public void recursiveFolderDownload(String sourcePath, String destinationPath) throws SftpException {
        Vector<ChannelSftp.LsEntry> fileAndFolderList = channel.ls(sourcePath); // Let list of folder content
        //Iterate through list of folder content
        for (ChannelSftp.LsEntry item : fileAndFolderList) {
            if (!item.getAttrs().isDir()) { // Check if it is a file (not a directory).
                if (!(new File(destinationPath + PS + item.getFilename())).exists()
                        || (item.getAttrs().getMTime() > Long
                        .valueOf(new File(destinationPath + PS + item.getFilename()).lastModified()
                                / (long) 1000)
                        .intValue())) { // Download only if changed later.
                    new File(destinationPath + PS + item.getFilename());
                    channel.get(sourcePath + PS + item.getFilename(),
                            destinationPath + PS + item.getFilename()); // Download file from source (source filename, destination filename).
                }
            } else if (!(".".equals(item.getFilename()) || "..".equals(item.getFilename()))) {
                new File(destinationPath + PS + item.getFilename()).mkdirs(); // Empty folder copy.
                recursiveFolderDownload(sourcePath + PS + item.getFilename(),
                        destinationPath + PS + item.getFilename()); // Enter found folder on server to read its contents and create locally.
            }
        }
    }

    /**
     * 文件夹不存在，创建
     *
     * @param folder 待创建的文件节夹
     */
    public void createFolder(String folder) {
        SftpATTRS stat = null;
        try {
            stat = channel.stat(folder);
        } catch (SftpException e) {
            log.error("复制目的地文件夹" + folder + "不存在，创建");
        }
        if (stat == null) {
            try {
                channel.mkdir(folder);
            } catch (SftpException e) {
                log.error("创建失败", e.getCause());
            }
        }
    }

    public InputStream get(String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = channel.get(filePath);
        } catch (SftpException e) {
            log.error("get" + e);
        }
        return inputStream;
    }

    public void put(InputStream inputStream, String filePath) {
        try {
            channel.put(inputStream, filePath);
        } catch (SftpException e) {
            log.error("put" + e);
        }
    }

    public Vector<ChannelSftp.LsEntry> ls(String filePath) {
        Vector ls = null;
        try {
            ls = channel.ls(filePath);
        } catch (SftpException e) {
            log.error("ls" + e);
        }
        return ls;
    }

    /**
     * 复制文件夹
     *
     * @param src  源文件夹
     * @param desc 目的文件夹
     */
    public void copy(String src, String desc) {
//        检查目的文件存在与否，不存在则创建
        this.createDir(desc);
//        查看源文件列表
        Vector<ChannelSftp.LsEntry> fileAndFolderList = this.ls(src);
        for (ChannelSftp.LsEntry item : fileAndFolderList) {
            if (!item.getAttrs().isDir()) {//是一个文件
                try (InputStream tInputStream = this.get(src + PS + item.getFilename());
                     ByteArrayOutputStream baos = new ByteArrayOutputStream()
                ) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = tInputStream.read(buffer)) > -1) {
                        baos.write(buffer, 0, len);
                    }
                    baos.flush();
                    InputStream nInputStream = new ByteArrayInputStream(baos.toByteArray());
                    this.put(nInputStream, desc + PS + item.getFilename());
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
                // 排除. 和 ..
            } else if (!(".".equals(item.getFilename()) || "..".equals(item.getFilename()))) {
//                创建文件，可能不需要
                this.createFolder(desc + PS + item.getFilename());
                //递归复制文件
                copy(src + PS + item.getFilename(), desc + PS + item.getFilename());
            }
        }
    }

    /**
     * 删除指定目录文件
     *
     * @param filePath 删除文件路径
     * @return
     */
    public Boolean del(String filePath) {
        boolean flag = false;
        try {
            channel.rm(filePath);
            flag = true;
        } catch (SftpException e) {
            flag = false;
            log.error("删除文件错误报告： " + e);
        }
        return flag;
    }

    public static void main(String[] args) throws FileNotFoundException {
        SftpUtilV2 sftpUtil = getSftpUtil();
        String fatePath = "/data/projects/fate/serving-10000/data/upload";
        String localPath = "/usr/local/codes/sftp";
        //上传文件并执行脚本
        FileInputStream inputStream = new FileInputStream("/Users/fc/Documents/work/code/springboot/src/test/java/com/fc/springboot/python/pythonTest.py");
        sftpUtil.upload(inputStream, "pythonTest.py", fatePath);

        //上传文件
        /*File file = new File("/Users/fc/Documents/work/code/springboot/src/test/java/com/fc/springboot/python/pythonTest.py");
        sftpUtil.upload(file, "pythonTest.py", "/usr/local/codes/sftp");*/
    }
}
