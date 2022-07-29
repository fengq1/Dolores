package com.cyy.developyment.util;

import com.cyy.developyment.constant.Constants;
import com.cyy.developyment.enums.PushTypeEnum;
import com.cyy.developyment.service.LogService;
import com.cyy.developyment.service.MissionService;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class CommandUtils {

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
        exeCmd(PushTypeEnum.NONE, commandStr);
    }

    public static void pExeCmd(String commandStr) {
        exeCmd(PushTypeEnum.CONSOLE, commandStr);
    }

    public static void exeCmd(PushTypeEnum pushType, String commandStr) {
        commandStr = "cmd /c " + commandStr;
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(commandStr);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.contains("BUILD SUCCESS"))
                    MissionService.currentPackageSuccess = true;
                LogService.append(true, pushType, line);
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

    public static String remoteExec(Session session, String command) {
        return remoteExec(PushTypeEnum.NONE, session, command);
    }

    public static String pRemoteExec(Session session, String command) {
        return remoteExec(PushTypeEnum.CONSOLE, session, command);
    }

    public static String remoteExec(PushTypeEnum pushType, Session session, String command) {
        LogService.append(true, pushType, "执行命令：" + command);
        List<String> resultLines = new ArrayList<>();
        ChannelExec channel = null;
        try {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            InputStream input = channel.getInputStream();
            channel.connect(10000);
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(input));
                String inputLine = null;
                while ((inputLine = inputReader.readLine()) != null) {
                    resultLines.add(inputLine);
                    LogService.append(true, pushType, inputLine);
                }
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (Exception e) {
                        LogService.append(false, pushType, "JSch inputStream close error:" + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            LogService.append(false, pushType, "IOcxecption:" + e.getMessage());
        } finally {
            if (channel != null) {
                try {
                    channel.disconnect();
                } catch (Exception e) {
                    LogService.append(false, pushType, "JSch channel disconnect error:" + e.getMessage());
                }
            }
        }
        return resultLines.toString();
    }

}
