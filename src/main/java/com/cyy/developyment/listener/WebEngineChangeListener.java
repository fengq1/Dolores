package com.cyy.developyment.listener;

import com.cyy.developyment.connector.JSConnector;
import com.cyy.developyment.connector.JavaConnector;
import com.cyy.developyment.service.ConfigService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

public class WebEngineChangeListener implements ChangeListener<Worker.State> {

    private WebEngine webEngine;

    public WebEngineChangeListener(WebEngine webEngine) {
        this.webEngine = webEngine;
    }

    @Override
    public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
        if (Worker.State.SUCCEEDED == newValue) {
            // 在web引擎页面中设置一个名为“connector”的接口对象
            JSObject window = (JSObject) webEngine.executeScript("window");
            window.setMember("javaConnector", JavaConnector.getInstance());
            // 获取Javascript连接器对象。
            JSConnector.load((JSObject) webEngine.executeScript("getJsConnector()"));
            //载入配置数据
            ConfigService.load();
        }
    }
}
