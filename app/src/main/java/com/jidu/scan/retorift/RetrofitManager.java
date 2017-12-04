package com.jidu.scan.retorift;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.jidu.scan.AppConfig;
import com.jidu.scan.utils.AppLog;


import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jzs on 2017/7/28.
 */

public class RetrofitManager {
    private static RetrofitManager mRetrofitManager;
    private Retrofit mRetrofit;
    private ApiService apiService;
    public static String baseUrl = AppConfig.BASE_URL;


    private RetrofitManager() {
        initRetrofit();
    }

    private RetrofitManager(String url) {
        initRetrofit(url);
    }

    public static synchronized RetrofitManager getInstance() {
        return new RetrofitManager();
    }

    public static synchronized RetrofitManager getInstance(String url) {
        return new RetrofitManager(url);
    }

    private void initRetrofit() {
        initRetrofit("");
    }

    private void initRetrofit(String url) {
        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }
        HttpLoggingInterceptor LoginInterceptor = new HttpLoggingInterceptor(
                message -> {
                    //打印retrofit日志
                    AppLog.e("Okhttp", message);
                }
        );
        LoginInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        //创建okhttpclient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(LoginInterceptor); //添加retrofit日志打印
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        OkHttpClient client = builder.build();//添加下载进度监听

        try {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T createReq(Class<T> reqServer) {
        return mRetrofit.create(reqServer);
    }

    public RetrofitManager createBaseApi() {
        apiService = createReq(ApiService.class);
        return this;
    }

    /**
     * 一般的post的请求，直接将对象返回
     *
     * @param url
     * @param parame
     * @param mCallback
     * @param aClass
     */
    public void post(String url, Map<String, String> parame,
                     final RequestCallBack mCallback, final Class<?> aClass) {
        apiService.executePost(url, parame)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody entity) {
                        Gson gson = new Gson();
                        String response = null;
                        try {
                            response = entity.string();
                            BaseEntity object = (BaseEntity) gson.fromJson(response, aClass);
                            mCallback.onSuccess(object.code, object.mess, object);
                        } catch (Exception e) {
                            e.printStackTrace();
                            AppLog.e("解析失败，entity：" + response);
                            mCallback.onFailure(500, "请求失败", entity);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mCallback.onFailure(500, "请求失败", null);
                    }
                });
    }

    /**
     * 一般的get请求
     *
     * @param url
     * @param parame
     * @param mCallback
     * @param aClass
     */
    public void get(String url, Map<String, String> parame,
                    final RequestCallBack mCallback, final Class<?> aClass) {
        apiService.executeGet(url, parame)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Object>() {
                    @Override
                    public void onNext(Object entity) {
                        Gson gson = new Gson();
                        String response = null;
                        try {
                            response = entity.toString();
                            Object object = gson.fromJson(response, aClass);
                            mCallback.onSuccess(200, "请求成功", object);
                        } catch (Exception e) {
                            e.printStackTrace();
                            AppLog.e("解析失败，entity：" + response);
                            mCallback.onFailure(500, "请求失败", entity);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    /**
     * post json请求
     *
     * @param url
     * @param parame
     * @param jsonParame
     * @param mCallback
     * @param aClass
     */
    public void postJson(String url, Map<String, String> parame, Map<String, String> jsonParame,
                         final RequestCallBack mCallback, final Class<?> aClass) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(jsonParame));

        apiService.json(url, parame, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Object>() {
                    @Override
                    public void onNext(Object entity) {
                        ResponseBody entity1 = (ResponseBody) entity;
                        Gson gson = new Gson();
                        String response = null;
                        try {
                            response = entity1.string();
                            Object object = gson.fromJson(response, aClass);
                            mCallback.onSuccess(200, "请求成功", object);
                        } catch (Exception e) {
                            e.printStackTrace();
                            AppLog.e("解析失败，entity：" + response);
                            mCallback.onFailure(500, "请求失败", entity);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }


}