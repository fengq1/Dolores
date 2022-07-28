package com.cyy.developyment.entity;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.setting.Setting;
import com.cyy.developyment.constant.Constants;
import com.cyy.developyment.util.DirPathUtils;

import java.util.Date;

/**
 * 项目配置类
 */
public class Config {

    //标题
    public final static String title;

    //远程服务地址
    public final static String remoteUrl;

    //应用宽度
    public static double width;

    //应用高度
    public static double height;

//    public static int billType;

    //监控文件夹路径
    public static String monitorDirPath;

    //今日完成数量
    public static String todayMissionFinishNumber;

    public final static String managerWeb;
    //setting对象
    public static Setting setting;

    static {
        String settingPath = Constants.baseDirPath + "project.setting";
        if (!FileUtil.exist(settingPath))
            createSettingFile(settingPath);
        setting = new Setting(settingPath);
//        billType = setting.getInt("billType");
        title = setting.getStr("title");
        width = setting.getDouble("width");
        height = setting.getDouble("height");
        managerWeb = setting.getStr("managerWeb");
        monitorDirPath = DirPathUtils.removeSeparator(setting.getStr("monitorDirPath"));
        remoteUrl = setting.getStr("remoteUrl");
        todayMissionFinishNumber = setting.getStr("todayMissionFinishNumber");
        String[] infos = todayMissionFinishNumber.split(":");
        Constants.todayMissionFinishNumber = DateUtil.isSameDay(new Date(), DateUtil.parseDate(infos[0])) ? Integer.valueOf(infos[1]) : 0;
    }

    public static void createSettingFile(String settingPath) {
        FileUtil.touch(settingPath);
        setting = new Setting(settingPath);
        setting.set("title", "请我喝杯小蓝");
        setting.set("width", "650");
        setting.set("height", "750");
        setting.set("monitorDirPath", "C://测试文件夹");
        setting.set("todayMissionFinishNumber", DateUtil.formatDate(new Date()) + ":0");
        setting.set("remoteUrl", "http://123.207.152.13:10002/ocr-server/");
//        setting.set("billType", "0");
        setting.set("managerWeb", "http://123.207.152.13:10002/ocr-web");
        setting.store();
    }

    public static void changeMonitorDirPath(String monitorDirPath) {
        setting.set("monitorDirPath", monitorDirPath);
        setting.store();
        Config.monitorDirPath = monitorDirPath;
    }

    public static void changeWidth(double width) {
        Config.width = width;
        changeWindow();
    }

    public static void changeHeight(double height) {
        Config.height = height;
        changeWindow();
    }

    private static boolean isAllowChange = true;

    private static void changeWindow() {
        if (isAllowChange) {
            isAllowChange = false;
            new Thread(() -> {
                ThreadUtil.sleep(1500);
                Config.setting.set("height", String.valueOf(height));
                Config.setting.set("width", String.valueOf(width));
                Config.setting.store();
                isAllowChange = true;
            }).start();
        }
    }

    public static void changeTodayMissionFinishNumber() {
        Config.todayMissionFinishNumber = DateUtil.formatDate(new Date()) + ":" + Constants.todayMissionFinishNumber;
        Config.setting.set("todayMissionFinishNumber", todayMissionFinishNumber);
        Config.setting.store();
    }
}
