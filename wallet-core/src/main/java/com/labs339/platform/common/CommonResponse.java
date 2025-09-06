package com.labs339.platform.common;
import java.io.Serializable;

public class CommonResponse<T> implements Serializable {

    private int code;
    private String success;// 状态码
    private String message;   // 返回信息
    private T data;           // 返回数据

    public CommonResponse() {}

    public CommonResponse(int code, String success,String message, T data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // 成功响应封装
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(0, "success","success", data);
    }

    public static <T> CommonResponse<T> success() {
        return new CommonResponse<>(0, "success","success", null);
    }

    // 失败响应封装
    public static <T> CommonResponse<T> fail(int code, String message) {
        return new CommonResponse<>(code,"fail", message, null);
    }

    public static <T> CommonResponse<T> fail(String message) {
        return new CommonResponse<>(-1, "fail",message, null);
    }

    // Getters & Setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

