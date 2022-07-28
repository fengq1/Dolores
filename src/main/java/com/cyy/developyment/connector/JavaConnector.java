package com.cyy.developyment.connector;


public class JavaConnector {

    private static JavaConnector instance;

    public static JavaConnector getInstance() {
        if (instance == null) instance = new JavaConnector();
        return instance;
    }


}
