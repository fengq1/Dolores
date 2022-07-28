package com.cyy.developyment.util;

import cn.hutool.log.StaticLog;
import com.cyy.developyment.constant.Constants;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class CmdUtil {

    public static void bootUpRun() {
        final String exeName = "Client.exe";
        String path = Constants.baseDirPath + exeName;
        String cmd = "reg add \"HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Run\" /v "
                + exeName +
                " /t reg_sz /d " + path + " /f";
        new Thread(() -> exeCmd(cmd)).start();
//        LogService.append(true, "开机启动设置完成！ " + path);
    }

    public static void exeCmd(String commandStr) {
        commandStr = "cmd /c " + commandStr;
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(commandStr);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                StaticLog.info(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
