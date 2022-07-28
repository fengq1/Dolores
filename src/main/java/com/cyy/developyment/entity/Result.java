package com.cyy.developyment.entity;

import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * @Title
 * @Author fengq1
 * @Version 1.0
 * @Time 2022年07月28日 10:34
 */
@Data
public class Result implements Serializable {

    private boolean success;

    private String msg;

    public static String ok() {
        Result result = new Result();
        result.success = true;
        result.msg = "操作成功";
        return JSONUtil.toJsonStr(result);
    }

    public static String ok(String msg) {
        Result result = new Result();
        result.success = true;
        result.msg = msg;
        return JSONUtil.toJsonStr(result);
    }

    public static String error(String message) {
        Result result = new Result();
        result.success = false;
        result.msg =  message;
        return JSONUtil.toJsonStr(result);

    }
}
