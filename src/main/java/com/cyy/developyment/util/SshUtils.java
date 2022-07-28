package com.cyy.developyment.util;

import cn.hutool.core.net.NetUtil;
import cn.hutool.extra.ssh.JschUtil;
import cn.hutool.log.StaticLog;
import com.cyy.developyment.entity.SshInfo;
import com.cyy.developyment.service.SshInfoService;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title
 * @Author fengq1
 * @Version 1.0
 * @Time 2022年07月28日 14:03
 */
public class SshUtils {
    private static Map<String, Session> sessionMap = new HashMap<>();

    public static boolean testConnection(String id) {
        Session session = getSession(id);
        boolean isConnected = session != null;
        disconnect(id);
        return isConnected;
    }

    public static Session getSession(String id) {
        SshInfo ssh = SshInfoService.getById(id);
        Session hostSession = null;
        Session session = null;
        try {
            if (ssh.isJump()) {
                hostSession = JschUtil.getSession(ssh.getJumpIp(), ssh.getJumpPort(), ssh.getJumpUsername(), ssh.getJumpPassword());
                int localPort = NetUtil.getUsableLocalPort();
                JschUtil.bindPort(hostSession, ssh.getIp(), ssh.getPort(), localPort);
                session = JschUtil.getSession("127.0.0.1", localPort, ssh.getUsername(), ssh.getPassword());
            } else
                session = JschUtil.getSession(ssh.getIp(), ssh.getPort(), ssh.getUsername(), ssh.getPassword());
            sessionMap.put(id, session);
            sessionMap.put("j" + id, hostSession);
            return session;
        } catch (Exception e) {
            e.printStackTrace();
            if (null != session)
                session.disconnect();
            if (null != hostSession)
                hostSession.disconnect();
            return null;
        }
    }

    public static void disconnect(String id) {
        Session session = sessionMap.get(id);
        if (null != session)
            session.disconnect();
        session = sessionMap.get("j" + id);
        if (null != session)
            session.disconnect();
    }

    public static void main(String[] args) {
        System.out.println(NetUtil.getUsableLocalPort());
    }

    public static String remoteExecute(Session session, String command) throws JSchException {
        StaticLog.info(">> {}", command);
        boolean result = NetUtil.isUsableLocalPort(6379);
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
                    StaticLog.info("   {}", inputLine);
                    resultLines.add(inputLine);
                }
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (Exception e) {
                        StaticLog.error("JSch inputStream close error:", e);
                    }
                }
            }
        } catch (IOException e) {
            StaticLog.error("IOcxecption:", e);
        } finally {
            if (channel != null) {
                try {
                    channel.disconnect();
                } catch (Exception e) {
                    StaticLog.error("JSch channel disconnect error:", e);
                }
            }
        }
        return resultLines.toString();
    }
}
