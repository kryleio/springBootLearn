package com.gll.learn.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class shellTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(shellTest.class);
    private static final boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

    //第二种方法
    /*ProcessBuilder builder = new ProcessBuilder();
    if (isWindows) {
        builder.command("cmd.exe", "/c", "dir");
    } else {
        builder.command("sh", "-c", "ls");
    }
    builder.directory(new File(System.getProperty("user.home")));
    Process process = builder.start();
    StreamGobbler streamGobbler =
            new StreamGobbler(process.getInputStream(), System.out::println);
    Executors.newSingleThreadExecutor().submit(streamGobbler);
    int exitCode = process.waitFor();*/

    public static void main(String[] args) {
        BufferedReader stdInput = null;
        BufferedReader stdError = null;
        try {
            String bashCommand = "";
            if (isWindows) {
                bashCommand =  "cmd /c dir";  //①
            }else{
                bashCommand =  "sh -c ls";  //①
            }

            Runtime runtime = Runtime.getRuntime();
            Process pro = runtime.exec(bashCommand);  //②
            int status = 0;  //③
            status = pro.waitFor();
            if (status != 0) {  //④
                LOGGER.error("shell exec error.");
                return;
            }
            LOGGER.info("shell exec success.");

            stdInput = new BufferedReader(new InputStreamReader(pro.getInputStream(),"UTF-8"));
            stdError = new BufferedReader(new InputStreamReader(pro.getErrorStream(),"UTF-8"));

            String s1 = "";
            while ((s1 = stdInput.readLine()) != null) {
                System.out.println("normal output: " + s1);
            }

            String s2 = "";
            while ((s2 = stdError.readLine()) != null) {
                System.out.println("error output: " + s1);
            }

            //System.exit(0);
        } catch (Exception e) {
            LOGGER.info("shell failed:" +e.getMessage());
        }finally {
            try {
                stdInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                stdError.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
