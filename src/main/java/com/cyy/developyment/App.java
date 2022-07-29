package com.cyy.developyment;

import cn.hutool.core.thread.ThreadUtil;
import com.cyy.developyment.constant.Constants;
import com.cyy.developyment.entity.Config;
import com.cyy.developyment.listener.HeightChangeListener;
import com.cyy.developyment.listener.WebEngineChangeListener;
import com.cyy.developyment.listener.WidthChangeListener;
import com.cyy.developyment.service.LogService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            // 设置Java的监听器
            webEngine.setOnAlert((WebEvent<String> wEvent) -> {
                System.out.println("JS alert() message: " + wEvent.getData());
            });
            webEngine.setOnError((WebErrorEvent error) -> {
                System.out.println("JS alert() error: " + error.getMessage());
            });
            webEngine.getLoadWorker().stateProperty().addListener(new WebEngineChangeListener(webEngine));
            stage.setTitle(Config.title);
            stage.setScene(new Scene(webView, Config.width, Config.height));
            stage.setMinWidth(650);
            stage.setMinHeight(750);
            stage.setWidth(Config.width);
            stage.setHeight(Config.height);
            stage.widthProperty().addListener(new WidthChangeListener());
            stage.heightProperty().addListener(new HeightChangeListener());
            stage.getIcons().add(new Image(App.class.getResourceAsStream("/icon.png")));
            stage.show();
            // 这里加载页面
            webEngine.load(App.class.getResource("/static/homepage.html").toString());
            LogService.info(Config.title + " 启动！");
        } catch (Exception e) {
            e.printStackTrace();
            LogService.info(Config.title + " 启动失败！");
        }
    }

    @Override
    public void stop() {
        try {
            LogService.info("应用关闭");
            super.stop();
        } catch (Exception e) {

        }
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
