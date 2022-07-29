package com.cyy.developyment.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.cyy.developyment.constant.Constants;
import com.cyy.developyment.entity.SshInfo;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @Title
 * @Author fengq1
 * @Version 1.0
 * @Time 2022年07月27日 15:02
 */
public class SshInfoService {

    public static Map<String, SshInfo> sshMap;

    private final static String sshConfigName = "/resources/ssh.json";

    private static File sshConfigFile;


    /**
     * 读取配置数据
     */
    static {
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
    }

    /**
     * 新增SSH配置
     *
     * @param formData 表单数据
     * @return
     */
    public static void addSsh(String formData) {
        SshInfo sshInfo = JSONUtil.toBean(formData, SshInfo.class);
        sshMap.put(sshInfo.getId(), sshInfo);
        write();
    }

    private static void write() {
        FileWriter fileWriter = new FileWriter(sshConfigFile);
        fileWriter.write(JSONUtil.toJsonStr(sshMap.values()));
    }

    /**
     * 删除SSH 配置
     *
     * @param id id
     * @return
     */
    public static void delSsh(String id) {
        sshMap.remove(id);
        write();
        LogService.info("SSH配置数据：" + sshMap.values());
    }

    public static List<SshInfo> list() {
        List<SshInfo> sshs = new ArrayList<>(SshInfoService.sshMap.values());
        sshs.sort(Comparator.comparing(SshInfo::getCreateTime).reversed());
        return sshs;
    }

    public static SshInfo getById(String id) {
        return SshInfoService.sshMap.get(id);
    }
}
