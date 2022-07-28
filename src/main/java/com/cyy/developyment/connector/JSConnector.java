package com.cyy.developyment.connector;

import cn.hutool.json.JSONObject;
import com.cyy.developyment.service.DataService;
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
            data.set("projects", DataService.projects);
            jsObject.call("java_call", "reloadData", data);
        });
    }
}
