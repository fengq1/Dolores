package com.cyy.developyment.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.cyy.developyment.connector.JSConnector;
import com.cyy.developyment.constant.Constants;
import com.cyy.developyment.entity.ProjectInfo;
import com.cyy.developyment.entity.Result;
import com.cyy.developyment.entity.SshInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title
 * @Author fengq1
 * @Version 1.0
 * @Time 2022年07月27日 15:02
 */
public class DataService {

    public static Map<String, SshInfo> sshMap;

    public static List<ProjectInfo> projects;

    private final static String sshConfigName = "/resources/ssh.json";

    private final static String appConfigName = "/resources/project.json";

    private static File sshConfigFile;

    private static File projectConfigFile;

    /**
     * 读取配置数据
     */
    public static void load() {
        LogService.info("读取SSH配置数据");
        sshConfigFile = new File(Constants.baseDirPath + sshConfigName);
        if (!sshConfigFile.exists())
            FileUtil.touch(sshConfigFile);
        try {
            sshMap = new HashMap<>();
            List<SshInfo> sshs = new ArrayList<>();
            FileReader fileReader = new FileReader(sshConfigFile);
            String data = fileReader.readString();
            if (StrUtil.isNotBlank(data)) {
                sshs = JSONUtil.parseArray(fileReader.readString()).toList(SshInfo.class);
                sshs.forEach(ssh -> {
                    sshMap.put(ssh.getId(), ssh);
                });
            }
            LogService.info("读取" + sshs.size() + "个配置");
            LogService.info(sshs.toString());
        } catch (Exception e) {
            e.getMessage();
            LogService.error("读取SSH配置数据失败！" + e.getMessage());
        }
        LogService.info("读取应用数据");
        projectConfigFile = new File(Constants.baseDirPath + appConfigName);
        if (!projectConfigFile.exists())
            FileUtil.touch(projectConfigFile);
        try {
            projects = new ArrayList<>();
            FileReader fileReader = new FileReader(projectConfigFile);
            String data = fileReader.readString();
            if (StrUtil.isNotBlank(data))
                projects = JSONUtil.parseArray(fileReader.readString()).toList(ProjectInfo.class);
            LogService.info("读取" + projects.size() + "个应用信息");
        } catch (Exception e) {
            e.getMessage();
            LogService.error("读取应用数据失败！" + e.getMessage());
        }
        JSConnector.pushData();
    }

    /**
     * 新增SSH配置
     *
     * @param formData 表单数据
     * @return
     */
    public static void addSsh(String formData) throws IOException {
        SshInfo sshInfo = JSONUtil.toBean(formData, SshInfo.class);
        sshMap.put(sshInfo.getId(), sshInfo);
        writeSsh();
    }

    private static void writeSsh() throws IOException {
        FileWriter fileWriter = new FileWriter(sshConfigFile);
        fileWriter.write(JSONUtil.toJsonStr(sshMap.values()));
    }

    /**
     * 删除SSH 配置
     *
     * @param formData id
     * @return
     */
    public static void delSsh(String id) throws IOException {
        sshMap.remove(id);
        writeSsh();
        LogService.info("SSH配置数据：" + sshMap.values());
    }
}
