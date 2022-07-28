package com.cyy.developyment.connector;


import com.cyy.developyment.entity.Result;
import com.cyy.developyment.service.SshInfoService;
import com.cyy.developyment.service.LogService;
import com.cyy.developyment.util.SshUtils;

public class JavaConnector {

    private static JavaConnector instance;

    public static JavaConnector getInstance() {
        if (instance == null) instance = new JavaConnector();
        return instance;
    }

    /**
     * 新增SSH配置
     *
     * @param formData
     * @return
     */
    public String js_addSsh(String formData) {
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
     * @param id
     * @return
     */
    public String js_delSsh(String id) {
        try {
            LogService.info("删除SSH配置，" + id);
            SshInfoService.delSsh(id);
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
     * @param id
     * @return
     */
    public String js_testConnect(String id) {
        try {
            LogService.info("测试ssh连接，" + id);
            boolean testSuccess = SshUtils.testConnection(id);
            return testSuccess ? Result.ok("连接成功") : Result.error("连接失败");
        } catch (Exception e) {
            e.printStackTrace();
            LogService.error("测试ssh连接失败，" + e.getMessage());
            return Result.error("连接失败，" + e.getMessage());
        }
    }
}
