package com.jidu.scan;

import com.jidu.scan.utils.SharedPreferencesUtil;

/**
 * Created by Jzs on 2017/7/24.
 * 用于存放系统的一些常量参数
 * 如基地址、文件存放地址、是否开启log打印、SP储存名
 */

public class AppConfig {

    // 是否开启Log打印
    public static Boolean IS_LOG;

    public static String BASE_URL;
    public static String TEST_URL = "http://192.168.1.131:806/";
    public static String RELEASE_URL = "http://192.168.1.131:806/";

    /**
     * 是否测试环境
     */
    public static boolean isTest = true;

    public static void init() {
        String base_url = SharedPreferencesUtil.getString("base_url", "http://192.168.1.131:806/");
        BASE_URL = base_url;
//        IS_LOG = isTest ? true : false;
        IS_LOG = true;
    }


    public static final String APICHECK = "api/check";
    public static final String CHECK = "api/code/check_code";
    public static final String ORDER = "api/order/list";
}
