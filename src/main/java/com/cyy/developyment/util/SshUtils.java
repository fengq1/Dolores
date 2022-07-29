package com.cyy.developyment.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.extra.ssh.JschUtil;
import com.cyy.developyment.entity.ProjectInfo;
import com.cyy.developyment.entity.SshInfo;
import com.cyy.developyment.enums.PushTypeEnum;
import com.cyy.developyment.service.LogService;
import com.cyy.developyment.service.ProjectInfoService;
import com.cyy.developyment.service.SshInfoService;
import com.jcraft.jsch.*;

import java.util.*;

/**
 * @Title
 * @Author fengq1
 * @Version 1.0
 * @Time 2022年07月28日 14:03
 */
public class SshUtils {
    private static Map<String, Session> sessionMap = new HashMap<>();

    public static boolean testConnection(String sshId) {
        Session session = getSession(sshId);
        boolean isConnected = session != null;
        disconnect(sshId);
        return isConnected;
    }

    public static Session getSession(String sshId) {
        return getSession(PushTypeEnum.CONSOLE, sshId);
    }

    public static Session getSession(PushTypeEnum pushType, String sshId) {
        SshInfo ssh = SshInfoService.getById(sshId);
        LogService.append(true, pushType, "开始与服务器【" + ssh.getInfo() + "】建立连接");
        String sshKey = pushType.getCode() + sshId;
        Session session = sessionMap.get(sshKey);
        if (null != session)
            return session;

        Session hostSession = null;
        try {
            if (ssh.isJump()) {
                hostSession = JschUtil.getSession(ssh.getJumpIp(), ssh.getJumpPort(), ssh.getJumpUsername(), ssh.getJumpPassword());
                int localPort = NetUtil.getUsableLocalPort();
                JschUtil.bindPort(hostSession, ssh.getIp(), ssh.getPort(), localPort);
                session = JschUtil.getSession("127.0.0.1", localPort, ssh.getUsername(), ssh.getPassword());
            } else
                session = JschUtil.getSession(ssh.getIp(), ssh.getPort(), ssh.getUsername(), ssh.getPassword());
            sessionMap.put(sshKey, session);
            sessionMap.put("j" + sshKey, hostSession);
            return session;
        } catch (Exception e) {
            e.printStackTrace();
            LogService.append(false, pushType, "与服务器建立连接失败," + e.getMessage());
            if (null != session)
                session.disconnect();
            if (null != hostSession)
                hostSession.disconnect();
            return null;
        }
    }

    public static void disconnect(PushTypeEnum pushType, String sshId) {
        String sshKey = sshId;
        Session session = sessionMap.get(sshKey);
        if (null != session)
            session.disconnect();
        session = sessionMap.get("j" + sshKey);
        if (null != session)
            session.disconnect();
        LogService.append(true, pushType, "关闭服务器连接");
    }

    public static void disconnect(String sshId) {
        disconnect(PushTypeEnum.CONSOLE, sshId);
    }

    public static boolean upload(Session session, ProjectInfo project) {
        try {
            ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
            sftp.connect();
            try {
                sftp.mkdir(project.getRemoteBackupsPath());
            } catch (Exception e) {
            }
            sftp.cd(project.getRemoteBackupsPath());
            try {
                sftp.rename(project.getRemoteBackupsWarPath(), project.getRemoteBackupsWarPath() + "_" + DateUtil.formatDateTime(new Date()));
            } catch (Exception e) {
            }
            LogService.pInfo("开始上传war包");
            sftp.put(project.getLocalWarPath(), project.getRemoteBackupsPath());
            LogService.pInfo("上传war包成功！");
            return Boolean.TRUE;
        } catch (Exception e) {
            if (e.getMessage().contains("session is down")) {
                session = getSession(project.getSshId());
                upload(session, project);
            }
            e.printStackTrace();
            disconnect(project.getSshId());
            LogService.pError("上传war包失败," + e.getMessage());
            return Boolean.FALSE;
        }
    }

    public static void tailLog(String projectId) {
        ThreadUtil.execute(() -> {
            ProjectInfo project = ProjectInfoService.getById(projectId);
            Session session = getSession(PushTypeEnum.PROJECT, project.getSshId());
            String cmd = "tail -f " + project.getRemotePath() + "/logs/catalina.out";
            CommandUtils.remoteExec(PushTypeEnum.PROJECT, session, cmd);
        });
    }
}
