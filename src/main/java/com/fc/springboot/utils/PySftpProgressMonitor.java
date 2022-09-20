package com.fc.springboot.utils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpProgressMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class PySftpProgressMonitor implements SftpProgressMonitor {

    private final static Logger logger = LoggerFactory.getLogger(PySftpProgressMonitor.class);

    private Session session;

    private List<String> msgQueue;

    private AtomicBoolean isDone;

    private String dest;

    public PySftpProgressMonitor(Session session, List<String> msgQueue, AtomicBoolean isDone) {
        this.session = session;
        this.msgQueue = msgQueue;
        this.isDone = isDone;
    }

    @Override
    public void init(int i, String source, String dest, long l) {
        this.dest = dest;
    }

    @Override
    public boolean count(long l) {
        return true;
    }

    @Override
    public void end() {
        ChannelExec channelExec = null;
        ChannelExec channelRm = null;
        BufferedReader in = null;
        //上传成功后开始执行
        try {
            channelExec = (ChannelExec) session.openChannel("exec");
            //保持编码一致性
            /*String command = "export LANG=zh_CN.UTF-8;" +
                    "export LC_CTYPE=zh_CN.UTF-8;" +
                    "export LC_ALL=zh_CN.UTF-8; " +
                    "python3 {}";
            command = MessageFormatter.format(command, dest + " a 1 test").getMessage();*/

            String command = "export LANG=zh_CN.UTF-8;" +
                    "export LC_CTYPE=zh_CN.UTF-8;" +
                    "export LC_ALL=zh_CN.UTF-8; " + "docker exec -it confs10000_client_1 bash && flow job submit -c /Examples/Pipeline/notebooks/yzx_lr_config1.json -d /Examples/Pipeline/notebooks/yzx_lr_dsl1.json";
            logger.info("exec command: {}", command);
            channelExec.setCommand(command);
            in = new BufferedReader(new InputStreamReader(channelExec.getErrStream()));
            BufferedReader input = new BufferedReader(new InputStreamReader(channelExec.getInputStream()));
            channelExec.connect();
            String msg = null;
            while ((msg = input.readLine()) != null) {
                System.out.println(msg);
                msgQueue.add(msg);
            }
            //删除
            channelRm = (ChannelExec) session.openChannel("exec");
            // channelRm.setCommand("rm -fr " + dest);
            channelRm.connect();
        } catch (JSchException e) {
            msgQueue.add(e.getMessage());
        } catch (IOException e) {
            msgQueue.add(e.getMessage());
        } finally {

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }

            if (channelExec != null) {
                channelExec.disconnect();
            }
            if (channelRm != null) {
                channelRm.disconnect();
            }
            isDone.getAndSet(true);
        }
    }
}
