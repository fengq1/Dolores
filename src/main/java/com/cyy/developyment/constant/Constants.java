package com.cyy.developyment.constant;

import cn.hutool.core.io.FileUtil;
import com.cyy.developyment.util.DirPathUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 常量类
 */
public class Constants {

    //WebView页面表格回传的操作日志数据

    //WebView页面表格最大显示条数
    public static Integer webViewLogTablesMaxSize;

    //项目根路径
    public final static String baseDirPath;

    //今日完成总数
    public static int todayMissionFinishNumber;

    //任务中数量
    public static int missioningNumber;

    //任务失败数量
    public static int missionFailNumber;

    public final static List<String> billTypes = Arrays.asList("png", "jpg");

    static {
        webViewLogTablesMaxSize = 15;
        baseDirPath = DirPathUtils.getBaseDirPath();
    }

    public static void main(String[] args) {
        File src = new File("D:\\opt\\测试图片\\1.jpg");
        for (int i = 2; i <= 50; i++)
            FileUtil.copy(src, new File("D:\\opt\\测试图片\\" + i + ".jpg"), true);

    }
}
