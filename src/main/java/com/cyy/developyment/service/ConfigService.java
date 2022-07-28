package com.cyy.developyment.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.cyy.developyment.connector.JSConnector;
import com.cyy.developyment.constant.Constants;
import com.cyy.developyment.entity.AppInfo;
import com.cyy.developyment.entity.SshInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title
 * @Author fengq1
 * @Version 1.0
 * @Time 2022年07月27日 15:02
 */
public class ConfigService {

    public static List<SshInfo> sshs;

    public static List<AppInfo> apps;

    private final static String sshConfigName = "/ssh.json";

    private final static String appConfigName = "/app.json";

    /**
     * 读取配置数据
     */
    public static void load() {
        LogService.info("读取SSH配置数据");
        File sshConfigFile = new File(Constants.baseDirPath + sshConfigName);
        if (!sshConfigFile.exists())
            FileUtil.touch(sshConfigFile);
        try {
            sshs = new ArrayList<>();
            FileReader fileReader = new FileReader(sshConfigFile);
            String data = fileReader.readString();
            if (StrUtil.isNotBlank(data))
                sshs = JSONUtil.parseArray(fileReader.readString()).toList(SshInfo.class);
            LogService.info("读取" + sshs.size() + "个配置");
            LogService.info(sshs.toString());
        } catch (Exception e) {
            e.getMessage();
            LogService.error("读取SSH配置数据失败！" + e.getMessage());
        }
        LogService.info("读取应用数据");
        File appConfigFile = new File(Constants.baseDirPath + appConfigName);
        if (!appConfigFile.exists())
            FileUtil.touch(appConfigFile);
        try {
            apps = new ArrayList<>();
            FileReader fileReader = new FileReader(appConfigFile);
            String data = fileReader.readString();
            if (StrUtil.isNotBlank(data))
                apps = JSONUtil.parseArray(fileReader.readString()).toList(AppInfo.class);
            LogService.info("读取" + apps.size() + "个应用信息");
        } catch (Exception e) {
            e.getMessage();
            LogService.error("读取应用数据失败！" + e.getMessage());
        }
        JSConnector.pushData(sshs, apps);
    }
}
