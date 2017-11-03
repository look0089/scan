package com.jidu.scan.retorift;

/**
 * Created by Jzs on 2017/7/31 0031.
 */

public interface RequestCallBack {

    void onSuccess(int code, String msg, Object object);

    void onFailure(int code, String msg, Object object);

}
