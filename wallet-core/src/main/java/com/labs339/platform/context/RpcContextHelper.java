package com.labs339.platform.context;

import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RpcContextHelper {

    private static final ThreadLocal<Map<String, String>> CONTEXT_HOLDER =
            new ThreadLocal<Map<String, String>>() {
                @Override
                protected Map<String, String> initialValue() {
                    return new HashMap<>();
                }
            };

    public static void setHeader(String key, String value) {
        CONTEXT_HOLDER.get().put(key, value);
        MDC.put(key, value);
    }

    public static String getHeader(String key) {
        return CONTEXT_HOLDER.get().get(key);
    }

    public static Map<String, String> getAllHeaders() {
        return new HashMap<>(CONTEXT_HOLDER.get());
    }

    public static void clear() {
        CONTEXT_HOLDER.remove();
        MDC.clear();
    }

    // 将 RpcContext 中的附加信息设置到当前线程
    public static void setFromRpcContext() {
        RpcContext context = RpcContext.getContext();
        Map<String, Object> attachments = context.getObjectAttachments();

        for (Map.Entry<String, Object> entry : attachments.entrySet()) {
            if (entry.getValue() instanceof String) {
                setHeader(entry.getKey(), (String) entry.getValue());
            }
        }
    }

    // 将当前线程的请求头设置到 RpcContext
    public static void setToRpcContext() {
        RpcContext context = RpcContext.getContext();
        Map<String, String> headers = getAllHeaders();

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            context.setAttachment(entry.getKey(), entry.getValue());
        }
    }

}
