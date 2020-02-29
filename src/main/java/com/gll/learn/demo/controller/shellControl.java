package com.gll.learn.demo.controller;

import com.gll.learn.shell.ExecuteShellUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

public class shellControl {

    private static final Logger LOGGER = LoggerFactory.getLogger(shellControl.class);

    private static final Timer timer = new Timer("Service Status Shell Timer");
    private static final int SCAN_FREQUENCY = 30 * 1000; //扫描频率(5秒一次)

    public static void main(String[] args) {

        //String res1 = result("shell");
        //String res2 = result("name2");

        TimerTask task = new TimerTask() {
            public void run() {
                getResult("shell");
            }
        };
        timer.scheduleAtFixedRate(task, 0, SCAN_FREQUENCY);//启动后立即执行一次，随后每5秒执行一次
    }

    public static void getResult(String fileName) {
        //String returnStr = "";
        try {
            ExecuteShellUtil executeShellUtil = ExecuteShellUtil.getInstance();
            executeShellUtil.init("47.99.64.142", 22, "root", "zhi1xingnan@ALY");

            //可以运行单个或者多个命令
            /*String java = "java";
            String result = executeShellUtil.execCmd("ps -ef | grep "+java+" | grep -v grep");
            System.out.println(result);*/

            //可以运行shell脚本
            String shellName = fileName;
            String result = executeShellUtil.execCmd("./" + shellName + ".sh");

            //去除特殊字符
            String regEx = "[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";
            String newString = result.replaceAll(regEx, "");

            if (newString.equals("process不存在")) {

                LOGGER.error(newString);//此时需要插入异常日志


            }else {
                LOGGER.info("服务运行状态正常");//此时可以插入正常日志
            }

            executeShellUtil.close();//释放资源
        } catch (Exception e) {
            LOGGER.error("执行Linux命令异常:" + e.getMessage()); //此时需要插入异常日志
        }
        //return returnStr;
    }
}
