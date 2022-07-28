package com.cyy.developyment.connector;

import cn.hutool.json.JSONObject;
import com.cyy.developyment.entity.AppInfo;
import com.cyy.developyment.entity.SshInfo;
import javafx.application.Platform;
import netscape.javascript.JSObject;

import java.util.List;

public class JSConnector {

    // 用于与Javascript引擎通信。
    public static JSObject jsObject;

    public static void load(JSObject jsObject) {
        JSConnector.jsObject = jsObject;
    }

    public static void pushData(List<SshInfo> sshs, List<AppInfo> apps) {
        Platform.runLater(() -> {
            JSONObject data = new JSONObject();
            data.set("sshs", sshs);
            data.set("apps", apps);
            jsObject.call("java_call", "reloadData", data);
        });
    }
}
