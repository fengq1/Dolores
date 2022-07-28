package com.cyy.developyment.entity;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.setting.Setting;
import com.cyy.developyment.constant.Constants;

/**
 * 项目配置类
 */
public class Config {

    //标题
    public final static String title;

    //应用宽度
    public static double width;

    //应用高度
    public static double height;
    //setting对象
    public static Setting setting;

    static {
        String settingPath = Constants.baseDirPath + "/resources/project.setting";
        if (!FileUtil.exist(settingPath))
            createSettingFile(settingPath);
        setting = new Setting(settingPath);
//        billType = setting.getInt("billType");
        title = setting.getStr("title");
        width = setting.getDouble("width");
        height = setting.getDouble("height");
    }

    public static void createSettingFile(String settingPath) {
        FileUtil.touch(settingPath);
        setting = new Setting(settingPath);
        setting.set("title", "Dolores");
        setting.set("width", "705");
        setting.set("height", "750");
        setting.store();
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

}
