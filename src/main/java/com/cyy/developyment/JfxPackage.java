package com.cyy.developyment;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.log.StaticLog;
import com.cyy.developyment.entity.Config;
import com.cyy.developyment.util.CmdUtil;

import java.io.File;

public class JfxPackage {

    public static void main(String[] args) {
        String path = new File(Config.class.getResource("").getPath()).getAbsolutePath();
        path = path.substring(0, path.lastIndexOf("\\target\\classes"));
        CmdUtil.exeCmd("D:");
        StaticLog.info("1.进入项目目录-" + path);
        CmdUtil.exeCmd("cd " + path);

        StaticLog.info("2.clean");
        CmdUtil.exeCmd("mvn clean");
        StaticLog.info("3.打包");
        CmdUtil.exeCmd("mvn jfx:native -f pom.xml");


        String zipPath = path + "\\target\\jfx\\native";
//        String settingPath = zipPath + "\\Client\\project.setting";
//        Config.createSettingFile(settingPath);
        File zipFile = new File(zipPath);
        //压缩发票客户端
        StaticLog.info("4.打包压缩客户端" + zipPath);
        ZipUtil.zip(zipFile);
        FileUtil.rename(new File(path + "\\target\\jfx\\native.zip"), "Client.zip", true);
        FileUtil.move(new File(path + "\\target\\jfx\\Client.zip"), new File("C:\\Users\\86917\\Desktop\\华润医药识别客户端.zip"), true);
//        StaticLog.info("5.打包压缩出库单识别客户端" + zipPath);
//        //压缩出库单客户端
//        Setting setting = new Setting(settingPath);
//        setting.set("billType", "1");
//        setting.set("monitorDirPath", "C://测试出库单");
//        setting.store();
//
//        ZipUtil.zip(zipFile);
//        FileUtil.rename(new File(path + "\\target\\jfx\\native.zip"), "Client.zip", true);
//        FileUtil.move(new File(path + "\\target\\jfx\\Client.zip"), new File("C:\\Users\\86917\\Desktop\\出库单识别客户端.zip"), true);
    }

}
