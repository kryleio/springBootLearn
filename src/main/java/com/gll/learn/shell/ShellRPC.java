package com.gll.learn.shell;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 *
 *  storm 远程调用脚本..
 * @author chenwen
 *
 */
public class ShellRPC {

    private static Connection conn;
    private static Session session;
    /** 远程机器IP */
    private static String ip;
    /** 用户名 */
    private static String userName;
    /** 密码 */
    private static String password;
    private static String charset = Charset.defaultCharset().toString();

    private static final int TIME_OUT = 1000 * 5 * 60;


    /**
     * 登录
     * @return
     * @throws IOException
     */
    private static boolean getConnection(String username, String password) throws IOException {
        ip = "47.93.118.58";
        userName = username;
        password = password;

        conn = new Connection(ip);
        conn.connect();
        return conn.authenticateWithPassword(userName, password);
    }

    /**
     * 执行脚本
     *
     * @param cmds
     * @return
     * @throws Exception
     */
    public static String exec(String cmds) throws Exception {
        InputStream stdOut = null;
        InputStream stdErr = null;
        String outStr = "";
        String outErr = "";
        //int ret = -1;
        try {
            if (getConnection("transx","Test_1234")) {
                // Open a new {@link Session} on this connection
                session = conn.openSession();
                // Execute a command on the remote machine.
                session.execCommand(cmds);

                stdOut = new StreamGobbler(session.getStdout());
                outStr = processStream(stdOut, charset);
                System.out.println("outStr: " + outStr);

                stdErr = new StreamGobbler(session.getStderr());
                outErr = processStream(stdErr, charset);
                if(!outErr.isEmpty()){
                    System.out.println("outErr: " + outErr);
                    return outErr;
                }

                session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);

                //ret = session.getExitStatus();
            } else {
                throw new Exception("登录远程机器失败" + ip); // 自定义异常类 实现略
            }
        } catch (Exception e){
            System.out.println("登录远程机器失败:" +e.getMessage());
        }finally {
            if (session != null) {
                session.close();
            }
            if (conn != null) {
                conn.close();
            }
            stdOut.close();
            stdErr.close();
        }
        return outStr;
    }

    /**
     * @param in
     * @param charset
     * @return
     * @throws Exception
     */
    private static String processStream(InputStream in, String charset) throws Exception {
        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while (in.read(buf) != -1) {
            sb.append(new String(buf, charset));
        }
        return sb.toString();
    }

}