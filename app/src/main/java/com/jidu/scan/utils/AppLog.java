package com.jidu.scan.utils;

import android.util.Log;

import com.jidu.scan.AppConfig;

/**
 * 打印日志调用
 *
 * @author Jzs created 2017/7/24
 */
public class AppLog {

    public static final String TAG = "AppLog";

    public static void e(String aLogInfo) {
        Log.e(TAG, aLogInfo);
    }

    public static void e(String aTag, String aLogInfo) {
        if (AppConfig.IS_LOG && aLogInfo != null) {
            Log.e(aTag, aLogInfo);
//			KLog.json(aTag, aLogInfo);
            aLogInfo = null;
            aTag = null;
        } else if (AppConfig.IS_LOG && aLogInfo == null) {
            Log.e(aTag, "null error");
        }
    }

    public static void redLog(String aTag, String aLogInfo) {
        if (AppConfig.IS_LOG && aLogInfo != null) {
            Log.e(aTag, aLogInfo);
//			KLog.json(aTag, aLogInfo);
            aLogInfo = null;
            aTag = null;
        } else if (AppConfig.IS_LOG && aLogInfo == null) {
            Log.e(aTag, "null error");
        }
    }

    public static void greenLog(String aTag, String aLogInfo) {
        if (AppConfig.IS_LOG && aLogInfo != null) {
            Log.i(aTag, aLogInfo);
            aLogInfo = null;
            aTag = null;
        } else if (AppConfig.IS_LOG && aLogInfo == null) {
            Log.e(aTag, "null error");
        }
    }

    public static void yellowLog(String aTag, String aLogInfo) {
        if (AppConfig.IS_LOG && aLogInfo != null) {
            Log.w(aTag, aLogInfo);
            aLogInfo = null;
            aTag = null;
        } else if (AppConfig.IS_LOG && aLogInfo == null) {
            Log.e(aTag, "null error");
        }
    }

    public static void blackLog(String aTag, String aLogInfo) {
        if (AppConfig.IS_LOG && aLogInfo != null) {
            Log.d(aTag, aLogInfo);
            aLogInfo = null;
            aTag = null;
        } else if (AppConfig.IS_LOG && aLogInfo == null) {
            Log.e(aTag, "null error");
        }
    }

    public static void debug(String aTag, String aLogInfo) {
        if (AppConfig.IS_LOG && aLogInfo != null) {
            Log.e(aTag, aLogInfo);
            aLogInfo = null;
            aTag = null;
        } else if (AppConfig.IS_LOG && aLogInfo == null) {
            Log.e(aTag, "null error");
        }

    }


}
