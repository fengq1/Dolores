package com.cyy.developyment.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.log.StaticLog;
import com.cyy.developyment.constant.Constants;

import java.util.Date;

public class LogService {

    private static String clientLogPath;

    private static FileWriter clientLogWriter;

    static {
        Date now = new Date();
        clientLogPath = Constants.baseDirPath + "logs\\" + DateUtil.year(now) + "年\\" + (DateUtil.month(now) + 1) + "月\\client_" + DateUtil.format(now, "MM月dd日") + ".log";
        if (!FileUtil.exist(clientLogPath)) {
            FileUtil.touch(clientLogPath);
        }
        clientLogWriter = new FileWriter(clientLogPath);
    }

    public synchronized static void append(boolean isInfo, String logInfo) {
        try {
            StaticLog.info(logInfo);
            Date now = new Date();
            clientLogPath = Constants.baseDirPath + "logs\\" + DateUtil.year(now) + "年\\" + (DateUtil.month(now) + 1) + "月\\client_" + DateUtil.format(now, "MM月dd日") + ".log";
            if (!FileUtil.exist(clientLogPath)) {
                FileUtil.touch(clientLogPath);
                clientLogWriter = new FileWriter(clientLogPath);
            }
            StringBuilder builder = new StringBuilder();
            builder.append(DateUtil.formatDateTime(new Date()));
            builder.append(isInfo ? " 【信息】 -" : " 【错误】 -");
            builder.append(logInfo);
            builder.append("\n");
            clientLogWriter.append(builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized static void info(String logInfo) {
        append(true, logInfo);
    }

    public synchronized static void error(String logInfo) {
        append(false, logInfo);
    }

}
