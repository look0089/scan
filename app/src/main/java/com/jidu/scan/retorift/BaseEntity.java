package com.jidu.scan.retorift;

import java.io.Serializable;

/**
 * 实体基础类
 */
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 返回码
     */
    public int code;
    /**
     * 返回信息描述
     */
    public String mess;

}
