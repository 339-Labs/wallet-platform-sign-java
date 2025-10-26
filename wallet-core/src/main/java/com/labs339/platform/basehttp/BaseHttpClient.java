package com.labs339.platform.basehttp;

import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 通用HTTP客户端基类（基于OkHttp）
 * 支持连接池、拦截器
 */
public class BaseHttpClient {

    protected OkHttpClient okHttpClient;
    protected Map<String, String> defaultHeaders;

    public BaseHttpClient() {
        this.defaultHeaders = new HashMap<>();
        this.defaultHeaders.put("Content-Type", "application/json");

        // 创建默认OkHttpClient配置
        this.okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES))
                .retryOnConnectionFailure(true)
                .build();
    }

    /**
     * 使用自定义OkHttpClient
     */
    public BaseHttpClient(OkHttpClient client) {
        this.okHttpClient = client;
        this.defaultHeaders = new HashMap<>();
        this.defaultHeaders.put("Content-Type", "application/json");
    }

    /**
     * 获取OkHttpClient实例
     */
    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    /**
     * 配置超时时间
     */
    public void configureTimeouts(int connectTimeout, int readTimeout, int writeTimeout) {
        this.okHttpClient = okHttpClient.newBuilder()
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * 添加拦截器
     */
    public void addInterceptor(Interceptor interceptor) {
        this.okHttpClient = okHttpClient.newBuilder()
                .addInterceptor(interceptor)
                .build();
    }

    /**
     * 添加网络拦截器
     */
    public void addNetworkInterceptor(Interceptor interceptor) {
        this.okHttpClient = okHttpClient.newBuilder()
                .addNetworkInterceptor(interceptor)
                .build();
    }

    /**
     * 添加默认请求头
     */
    public void addDefaultHeader(String key, String value) {
        this.defaultHeaders.put(key, value);
    }

    /**
     * 构建请求头
     */
    protected Headers buildHeaders(Map<String, String> customHeaders) {
        Headers.Builder builder = new Headers.Builder();

        // 添加默认请求头
        for (Map.Entry<String, String> entry : defaultHeaders.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }

        // 添加自定义请求头
        if (customHeaders != null) {
            for (Map.Entry<String, String> entry : customHeaders.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        return builder.build();
    }

    /**
     * 同步GET请求
     */
    public HttpResponse get(String url) throws IOException {
        return get(url, null);
    }

    /**
     * 同步GET请求（带自定义请求头）
     */
    public HttpResponse get(String url, Map<String, String> headers) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .headers(buildHeaders(headers))
                .get()
                .build();

        return executeRequest(request);
    }

    /**
     * 同步POST请求
     */
    public HttpResponse post(String url, String body) throws IOException {
        return post(url, body, null);
    }

    /**
     * 同步POST请求（带自定义请求头）
     */
    public HttpResponse post(String url, String body, Map<String, String> headers) throws IOException {
        RequestBody requestBody = RequestBody.create(body, MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .headers(buildHeaders(headers))
                .post(requestBody)
                .build();

        return executeRequest(request);
    }


    /**
     * 同步PUT请求
     */
    public HttpResponse put(String url, String body) throws IOException {
        return put(url, body, null);
    }

    /**
     * 同步PUT请求（带自定义请求头）
     */
    public HttpResponse put(String url, String body, Map<String, String> headers) throws IOException {
        RequestBody requestBody = RequestBody.create(body, MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .headers(buildHeaders(headers))
                .put(requestBody)
                .build();

        return executeRequest(request);
    }


    /**
     * 同步DELETE请求
     */
    public HttpResponse delete(String url) throws IOException {
        return delete(url, null);
    }

    /**
     * 同步DELETE请求（带自定义请求头）
     */
    public HttpResponse delete(String url, Map<String, String> headers) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .headers(buildHeaders(headers))
                .delete()
                .build();

        return executeRequest(request);
    }

    /**
     * 执行同步请求
     */
    protected HttpResponse executeRequest(Request request) throws IOException {
        try (Response response = okHttpClient.newCall(request).execute()) {
            String body = response.body() != null ? response.body().string() : "";
            return new HttpResponse(response.code(), body, response.headers());
        }
    }


}
