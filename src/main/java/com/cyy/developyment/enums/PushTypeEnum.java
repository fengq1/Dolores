package com.cyy.developyment.enums;

import lombok.Getter;

import java.io.Serializable;

/**
 * @Title
 * @Author fengq1
 * @Version 1.0
 * @Time 2022年07月29日 15:57
 */
@Getter
public enum PushTypeEnum implements Serializable {

    NONE(0, "无"),
    CONSOLE(1, "任务视图控制台"),

    PROJECT(2, "项目服务器日志");

    PushTypeEnum(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    private int code;
    private String txt;
}
