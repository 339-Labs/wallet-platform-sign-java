package com.labs339.platform.basehttp;

import okhttp3.Headers;

public class HttpResponse {

    private final int statusCode;
    private final String body;
    private final Headers headers;

    public HttpResponse(int statusCode, String body, Headers headers) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public Headers getHeaders() {
        return headers;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }

}
