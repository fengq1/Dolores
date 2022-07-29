package com.cyy.developyment.service;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.cyy.developyment.connector.JSConnector;
import com.cyy.developyment.entity.MissionInfo;
import com.cyy.developyment.entity.ProjectInfo;
import com.cyy.developyment.util.CommandUtils;
import com.cyy.developyment.util.SshUtils;
import com.jcraft.jsch.Session;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title
 * @Author fengq1
 * @Version 1.0
 * @Time 2022年07月29日 10:46
 */
public class MissionService {

    private static MissionInfo currentMission;

    public static boolean currentPackageSuccess;

    public static MissionInfo createMission(String formData) {
        convert(formData);
        ThreadUtil.execute(MissionService::startMission);
        return currentMission;
    }

    private static void convert(String formData) {
        JSONObject jsonObject = JSONUtil.parseObj(formData);
        currentMission = new MissionInfo();
        currentMission.setName(jsonObject.getStr("name"));
        List<ProjectInfo> projectInfos = new ArrayList<>();
        jsonObject.getJSONArray("projects").forEach(projectObj -> {
            projectInfos.add(ProjectInfoService.getById(projectObj.toString()));
        });
        currentMission.setProjects(projectInfos);
    }

    private static void startMission() {
        LogService.pInfo("任务启动：" + currentMission.getName());
        LogService.pInfo("共：" + currentMission.getProjects().size() + "个项目");
        for (int i = 0; i < currentMission.getProjects().size(); i++) {
            ProjectInfo project = currentMission.getProjects().get(i);
            LogService.pInfo("执行【" + project.getName() + "】项目");
            LogService.pInfo("开始打包，POM路径：" + project.getPomPath());
            currentPackageSuccess = false;
            String packageCmd = "mvn clean install -f " + project.getPomPath() + "/pom.xml";
            LogService.pInfo("执行命令：" + packageCmd);
            CommandUtils.pExeCmd(packageCmd);
            File war = new File(project.getLocalWarPath());
            if (!currentPackageSuccess || !war.exists()) {
                LogService.pError("打包失败");
                JSConnector.pushMissionStatus(0, false);
                LogService.pError("当前项目执行结束");
                return;
            }
            LogService.pInfo("打包成功");
            JSConnector.pushMissionStatus(1, true);

            Session session = SshUtils.getSession(project.getSshId());
            if (null == session) {
                JSConnector.pushMissionStatus(1, false);
                LogService.pError("当前项目执行结束");
                return;
            }
            //上传war包
            boolean uploadSuccess = SshUtils.upload(session, project);
            if (!uploadSuccess) {
                JSConnector.pushMissionStatus(1, false);
                LogService.pError("当前项目执行结束");
                return;
            }
            JSConnector.pushMissionStatus(2, true);
            //项目启动
            getCommands(project).forEach(command -> {
                CommandUtils.remoteExec(session, command);
            });
            LogService.pError("当前项目执行完成");
            if (i != currentMission.getProjects().size() - 1)
                JSConnector.pushMissionStatus(0, true);
            else
                JSConnector.pushMissionStatus(3, true);
        }
        currentMission = null;
        currentPackageSuccess = false;
    }

    private static List<String> getCommands(ProjectInfo project) {
        List<String> commands = new ArrayList<>();
        commands.add("cd " + project.getRemotePath());
        commands.add("kill -9 `ps -ef | grep  " + project.getWarName() + "| grep -v grep | awk '{print $2}'`");
        commands.add("rm -rf " + project.getRemotePath() + "webapps/" + project.getWarName() + "*");
        commands.add("rm -rf " + project.getRemotePath() + "webapps/" + project.getWarName() + "*");
        commands.add("cp " + project.getRemoteBackupsWarPath() + " " + project.getRemoteWebappsWarPath());
        commands.add("sh " + project.getRemotePath() + "/bin/startup.sh");
        return commands;
    }


}
