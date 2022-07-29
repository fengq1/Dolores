package com.cyy.developyment.connector;


import cn.hutool.json.JSONUtil;
import com.cyy.developyment.entity.Result;
import com.cyy.developyment.service.MissionService;
import com.cyy.developyment.service.ProjectInfoService;
import com.cyy.developyment.service.SshInfoService;
import com.cyy.developyment.service.LogService;
import com.cyy.developyment.util.SshUtils;

/**
 * 前端调用Java程序入口
 */
public class JavaConnector {

    private static JavaConnector instance;

    public static JavaConnector getInstance() {
        if (instance == null) instance = new JavaConnector();
        return instance;
    }

    /**
     * 创建任务
     *
     * @param formData
     * @return
     */
    public String createMission(String formData) {
        try {
            LogService.info("创建任务，" + formData);
            return Result.ok(MissionService.createMission(formData));
        } catch (Exception e) {
            e.printStackTrace();
            LogService.error("创建任务失败，" + e.getMessage());
            return Result.error("操作失败，" + e.getMessage());
        }
    }

    /**
     * 新增SSH配置
     *
     * @param formData
     * @return
     */
    public String addSsh(String formData) {
        try {
            LogService.info("新增SSH配置，" + formData);
            SshInfoService.addSsh(formData);
            JSConnector.pushData();
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            LogService.error("新增SSH配置失败，" + e.getMessage());
            return Result.error("操作失败，" + e.getMessage());
        }
    }

    /**
     * 删除SSH配置
     *
     * @param sshId
     * @return
     */
    public String delSsh(String sshId) {
        try {
            LogService.info("删除SSH配置，" + sshId);
            SshInfoService.delSsh(sshId);
            JSConnector.pushData();
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            LogService.error("删除SSH配置失败，" + e.getMessage());
            return Result.error("操作失败，" + e.getMessage());
        }
    }

    /**
     * 测试ssh连接
     *
     * @param sshId
     * @return
     */
    public String testConnect(String sshId) {
        try {
            LogService.info("测试ssh连接，" + sshId);
            boolean testSuccess = SshUtils.testConnection(sshId);
            return testSuccess ? Result.ok("连接成功") : Result.error("连接失败");
        } catch (Exception e) {
            e.printStackTrace();
            LogService.error("测试ssh连接失败，" + e.getMessage());
            return Result.error("连接失败，" + e.getMessage());
        }
    }

    /**
     * 新增项目信息
     *
     * @param formData
     * @return
     */
    public String submitProject(String formData) {
        try {
            LogService.info("提交项目信息，" + formData);
            ProjectInfoService.submitProject(formData);
            JSConnector.pushData();
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            LogService.error("提交项目信息失败，" + e.getMessage());
            return Result.error("操作失败，" + e.getMessage());
        }
    }

    /**
     * 删除项目信息
     *
     * @param projectId
     * @return
     */
    public String delProject(String projectId) {
        try {
            LogService.info("删除项目信息，" + projectId);
            ProjectInfoService.delProject(projectId);
            JSConnector.pushData();
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            LogService.error("删除项目信息失败，" + e.getMessage());
            return Result.error("操作失败，" + e.getMessage());
        }
    }

    /**
     * 查询控制台信息
     *
     * @param projectId
     * @return
     */
    public void tailLog(String projectId) {
        try {
            LogService.info("查询控制台信息，" + projectId);
            SshUtils.tailLog(projectId);
        } catch (Exception e) {
            e.printStackTrace();
            LogService.error("查询控制台信息失败，" + e.getMessage());
        }
    }

}
