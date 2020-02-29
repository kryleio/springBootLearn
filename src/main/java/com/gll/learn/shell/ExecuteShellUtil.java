package com.gll.learn.shell;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * 执行Shell工具类
 *
 * @author JustryDeng
 * @date 2019/4/29 16:29
 */
public class ExecuteShellUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteShellUtil.class);

    /**
     * 未调用初始化方法 错误提示信息
     */
    private static final String DONOT_INIT_ERROR_MSG = "please invoke init(...) first!";

    private Session session;

    private Channel channel;

    private ChannelExec channelExec;

    private ExecuteShellUtil() {
    }

    /**
     * 获取ExecuteShellUtil类实例对象
     *
     * @return 实例
     * @date 2019/4/29 16:58
     */
    public static ExecuteShellUtil getInstance() {
        return new ExecuteShellUtil();
    }

    /**
     * 初始化
     *
     * @param ip       远程Linux地址
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @throws JSchException JSch异常
     * @date 2019/3/15 12:41
     */
    public void init(String ip, Integer port, String username, String password) throws JSchException {
        JSch jsch = new JSch();
        jsch.getSession(username, ip, port);
        session = jsch.getSession(username, ip, port);
        session.setPassword(password);
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        session.setConfig(sshConfig);
        session.connect(60 * 1000);
        LOGGER.info("Session connected!");
        // 打开执行shell指令的通道
        channel = session.openChannel("exec");
        channelExec = (ChannelExec) channel;
    }

    /**
     * 执行一条命令
     */
    public String execCmd(String command) throws Exception {
        if (session == null || channel == null || channelExec == null) {
            throw new Exception(DONOT_INIT_ERROR_MSG);
        }
        LOGGER.info("execCmd command - > {}", command);
        channelExec.setCommand(command);
        channel.setInputStream(null);
        channelExec.setErrStream(System.err);
        if(!channel.isConnected()) {
            channel.connect();
        }
        StringBuilder sb = new StringBuilder(16);
        try (InputStream in = channelExec.getInputStream();
             InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {
            String buffer;
            while ((buffer = reader.readLine()) != null) {
                sb.append("\n").append(buffer);
            }

            in.close();
            isr.close();
            reader.close();

            // 释放资源(执行多条在后面释放)
            //close();
            LOGGER.info("execCmd result - > {}", sb);
            return sb.toString();
        }
    }

    /**
     * 释放资源
     *
     * @date 2019/3/15 12:47
     */
    public void close() {
        if (channelExec != null && channelExec.isConnected()) {
            channelExec.disconnect();
        }
        if (channel != null && channel.isConnected()) {
            channel.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }
}