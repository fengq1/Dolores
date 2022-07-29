package com.cyy.developyment.connector;

import cn.hutool.json.JSONObject;
import com.cyy.developyment.service.ProjectInfoService;
import com.cyy.developyment.service.SshInfoService;
import javafx.application.Platform;
import netscape.javascript.JSObject;


public class JSConnector {

    // 用于与Javascript引擎通信。
    public static JSObject jsObject;

    public static void load(JSObject jsObject) {
        JSConnector.jsObject = jsObject;
    }

    public static void pushData() {
        Platform.runLater(() -> {
            JSONObject data = new JSONObject();
            data.set("sshs", SshInfoService.list());
            data.set("projects", ProjectInfoService.list());
            jsObject.call("java_call", "reloadData", data);
        });
    }

    public static void pushConsole(String logContent) {
        Platform.runLater(() -> {
            JSONObject data = new JSONObject();
            data.set("log", logContent);
            jsObject.call("java_call", "pushConsole", data);
        });
    }

    public static void pushProjectLog(String logContent) {
        Platform.runLater(() -> {
            JSONObject data = new JSONObject();
            data.set("log", logContent);
            jsObject.call("java_call", "pushProjectLog", data);
        });
    }

    public static void pushMissionStatus(int step, boolean success) {
        Platform.runLater(() -> {
            JSONObject data = new JSONObject();
            data.set("processStatus", success ? "" : "error");
            data.set("missionStep", step);
            jsObject.call("java_call", "missionStatus", data);
        });
    }
}
