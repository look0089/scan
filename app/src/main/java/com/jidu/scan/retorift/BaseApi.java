package com.jidu.scan.retorift;

import java.util.Map;

public class BaseApi {

    public RequestCallBack mCallBack;
    public String Url;
    public String BaseUrl;
    public Map<String, String> Parame;
    public Map<String, String> JsonMaps;
    public Class<?> aClass;


    public BaseApi callBack(final RequestCallBack aCallBack) {
        this.mCallBack = aCallBack;
        return this;
    }


    /**
     * post请求
     */
    public void post() {
        RetrofitManager.getInstance().createBaseApi().post(Url, Parame, mCallBack, aClass);
    }

    /**
     * post请求、改变baseurl
     */
    public void postUrl() {
        RetrofitManager.getInstance(BaseUrl).createBaseApi().post(Url, Parame, mCallBack, aClass);
    }

    /**
     * post请求、json提交参数
     */
    public void postJson() {
        RetrofitManager.getInstance().createBaseApi().postJson(Url, Parame, JsonMaps, mCallBack, aClass);
    }

    /**
     * post请求、json提交参数、改变baseurl
     */
    public void postJsonUrl() {
        RetrofitManager.getInstance(BaseUrl).createBaseApi().postJson(Url, Parame, JsonMaps, mCallBack, aClass);
    }

    /**
     * get请求
     */
    public void get() {
        RetrofitManager.getInstance().createBaseApi().get(Url, Parame, mCallBack, aClass);
    }

    /**
     * get请求、改变baseurl
     */
    public void getUrl() {
        RetrofitManager.getInstance(BaseUrl).createBaseApi().get(Url, Parame, mCallBack, aClass);
    }

    /**
     * 设置参数的方法
     * 成员变量都是null，必须传值过来
     * <p>
     * 一定的有参数
     *
     * @param url      接口地址
     * @param parame   maps类型的参数
     * @param aClass   返回的实体类型
     *                 <p>
     *                 <p>
     *                 不一定有的参数：
     * @param baseUrl  变化的基地址
     * @param jsonMaps 以json传递的参数
     */
    public void setConfig(String baseUrl, String url, Map<String, String> parame, Map<String, String> jsonMaps, final Class<?> aClass) {
        this.BaseUrl = baseUrl;
        this.Url = url;
        this.Parame = parame;
        this.JsonMaps = jsonMaps;
        this.aClass = aClass;
    }

    public void setConfig(String baseUrl, String url, Map<String, String> parame, final Class<?> aClass) {
        this.BaseUrl = baseUrl;
        this.Url = url;
        this.Parame = parame;
        this.aClass = aClass;
    }

    public void setConfig(String url, Map<String, String> parame, Map<String, String> jsonMaps, final Class<?> aClass) {
        this.Url = url;
        this.Parame = parame;
        this.JsonMaps = jsonMaps;
        this.aClass = aClass;
    }

    public void setConfig(String url, Map<String, String> parame, final Class<?> aClass) {
        this.Url = url;
        this.Parame = parame;
        this.aClass = aClass;
    }

}

