package com.cyy.developyment.util;

import cn.hutool.core.util.StrUtil;
import com.cyy.developyment.entity.Config;

import java.io.File;

public class DirPathUtils {

    /**
     * 处理文件路径格式，去掉末尾的 \ 和 \\
     *
     * @param monitorDirPath
     * @return
     */
    public static String removeSeparator(String monitorDirPath) {
        if (StrUtil.isBlank(monitorDirPath) || monitorDirPath.length() < 2)
            return monitorDirPath;
        if (monitorDirPath.substring(monitorDirPath.length() - 2, monitorDirPath.length()).equals("\\\\"))
            return monitorDirPath.substring(0, monitorDirPath.length() - 2);
        else if (monitorDirPath.substring(monitorDirPath.length() - 1, monitorDirPath.length()).equals("\\"))
            return monitorDirPath.substring(0, monitorDirPath.length() - 1);
        return monitorDirPath;
    }

    /**
     * 获取项目根路径
     *
     * @return
     */
    public static String getBaseDirPath() {
        String path = new File(Config.class.getResource("").getPath()).getAbsolutePath();
        if (path.contains("\\file:\\")) {
            path = path.substring(0, path.lastIndexOf("\\file:\\")).replace("app", "");
        } else if (path.contains("\\target\\classes"))
            path = path.substring(0, path.lastIndexOf("\\target\\classes"));
        return DirPathUtils.removeSeparator(path) + "\\";
    }
}
