package com.cyy.developyment.constant;

public class BillServiceStatusConstants {

    //当前状态
    public static int NOW;

    //启动中
    public final static int STARTING;

    //运行中
    public final static int RUNNING;

    //
    public final static int STOP;

    //未找到文件夹
    public final static int NO_DIR;

    //建立远程连接失败
    public final static int TEST_SERVER_FAIL;

    static {
        STARTING = 0;
        RUNNING = 1;
        STOP = 2;
        NO_DIR = 3;
        TEST_SERVER_FAIL = 4;
        NOW = -1;
    }
}
