package com.cyy.developyment.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.cyy.developyment.connector.JSConnector;
import com.cyy.developyment.constant.Constants;
import com.cyy.developyment.entity.ProjectInfo;
import com.cyy.developyment.entity.ProjectInfo;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @Title
 * @Author fengq1
 * @Version 1.0
 * @Time 2022年07月27日 15:02
 */
public class ProjectInfoService {

    public static Map<String, ProjectInfo> projectMap;

    private final static String projectConfigName = "/resources/project.json";

    private static File projectConfigFile;


    /**
     * 读取项目数据
     */
    static {
        LogService.info("读取项目数据");
        projectConfigFile = new File(Constants.baseDirPath + projectConfigName);
        if (!projectConfigFile.exists())
            FileUtil.touch(projectConfigFile);
        try {
            projectMap = new HashMap<>();
            List<ProjectInfo> projects = new ArrayList<>();
            FileReader fileReader = new FileReader(projectConfigFile);
            String data = fileReader.readString();
            if (StrUtil.isNotBlank(data)) {
                projects = JSONUtil.parseArray(fileReader.readString()).toList(ProjectInfo.class);
                projects.forEach(project -> {
                    projectMap.put(project.getId(), project);
                });
            }
            LogService.info("读取" + projects.size() + "个项目数据");
            LogService.info(projects.toString());
        } catch (Exception e) {
            e.getMessage();
            LogService.error("读取项目数据失败！" + e.getMessage());
        }
    }

    /**
     * 新增项目信息
     *
     * @param formData 表单数据
     * @return
     */
    public static void submitProject(String formData) {
        ProjectInfo projectInfo = JSONUtil.toBean(formData, ProjectInfo.class);
        projectMap.put(projectInfo.getId(), projectInfo);
        write();
    }

    private static void write() {
        FileWriter fileWriter = new FileWriter(projectConfigFile);
        fileWriter.write(JSONUtil.toJsonStr(projectMap.values()));
    }

    /**
     * 删除项目信息
     *
     * @param id id
     * @return
     */
    public static void delProject(String id) {
        projectMap.remove(id);
        write();
        LogService.info("项目信息数据：" + projectMap.values());
    }

    public static List<ProjectInfo> list() {
        List<ProjectInfo> projects = new ArrayList<>(projectMap.values());
        projects.sort(Comparator.comparing(ProjectInfo::getCreateTime).reversed());
        return projects;
    }

    public static ProjectInfo getById(String id) {
        return ProjectInfoService.projectMap.get(id);
    }
}
