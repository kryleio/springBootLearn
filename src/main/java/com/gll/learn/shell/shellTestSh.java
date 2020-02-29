package com.gll.learn.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class shellTestSh {

    private static final Logger LOGGER = LoggerFactory.getLogger(shellTestSh.class);
    private static final boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

    public static void main(String[] args) {
        String shellSh = "";//要运行的脚本文件的名称,首先要确保用户对文件有执行权
        String param1 = "";
        String param2 = "";
        String param3 = "";
        BufferedReader stdInput = null;
        try {
            String rootDir = "";//要运行的脚本所在的目录,也可以全路径，也可以在shell脚本第一行cd
            ProcessBuilder pb = new ProcessBuilder("./" + shellSh, param1, param2, param3);
            pb.directory(new File(rootDir));

            int runningStatus = 0;
            Process p = pb.start();
            runningStatus = p.waitFor();

            String s = null;
            if (runningStatus != 0) {
                LOGGER.error("ProcessBuilder start error.");
            }else{
                stdInput = new BufferedReader(new InputStreamReader(p.getInputStream(),"UTF-8"));
                while ((s = stdInput.readLine()) != null) {
                    System.out.println("normal output: " + s);
                }
            }
        } catch (Exception e) {
            LOGGER.error("ProcessBuilder start failed: "+e.getMessage());
        }finally {
            try {
                stdInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*这里有必要解释一下几个参数：
        RUNNING_SHELL_FILE：要运行的脚本
        SHELL_FILE_DIR：要运行的脚本所在的目录； 当然你也可以把要运行的脚本写成全路径。
        runningStatus：运行状态，0标识正常。 详细可以看java文档。
        param1, param2, param3：可以在RUNNING_SHELL_FILE脚本中直接通过1,2,$3分别拿到的参数。

        直接通过系统Runtime执行shell
        这个方法比较暴力，也比较常用， 代码如下：
        p = Runtime.getRuntime().exec(SHELL_FILE_DIR + RUNNING_SHELL_FILE + " "+param1+" "+param2+" "+param3);
        p.waitFor();
        我们发现，通过Runtime的方式并没有builder那么方便，特别是参数方面，必须自己加空格分开，因为exec会把整个字符串作为shell运行。

        赋予sh文件执行权
        ProcessBuilder builder = new ProcessBuilder("/bin/chmod", "755", tempFile.getPath());*/
    }
}
