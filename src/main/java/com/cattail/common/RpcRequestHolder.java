package com.cattail.common;

import com.cattail.socket.codec.RpcResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/27
 * @Copyright: https://github.com/CatTailzz
 */
public class RpcRequestHolder {

    public final static AtomicLong REQUEST_ID_GEN = new AtomicLong(0);

    public static final Map<Long, RpcFuture<RpcResponse>> REQUEST_MAP = new ConcurrentHashMap<>();

    public static Long getRequestId() {
        if (REQUEST_ID_GEN.longValue() == Long.MAX_VALUE) {
            REQUEST_ID_GEN.set(0);
        }
        return REQUEST_ID_GEN.incrementAndGet();
    }
}
