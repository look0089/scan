package com.jidu.scan;

import android.app.Application;

/**
 * Created by Jzs on 2017/7/19.
 * 全局Applaciton
 */
public class MyApplaciton extends Application {

    private static MyApplaciton instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // 初始化参数
        AppConfig.init();
    }

    public static MyApplaciton getInstance() {
        if (instance == null) {
            instance = new MyApplaciton();
            return instance;
        } else {
            return instance;
        }
    }

}
