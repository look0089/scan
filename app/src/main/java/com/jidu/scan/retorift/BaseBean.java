package com.jidu.scan.retorift;

/**
 * Created by Jzs on 2017/8/2.
 */

public class BaseBean<T> {


    private static final long serialVersionUID = 1L;
    /**
     * 返回码
     */
    public int code;
    /**
     * 返回信息描述
     */
    public String msg;

    public T content;


    @Override
    public String toString() {
        return "BaseBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", content=" + content +
                '}';
    }
}
