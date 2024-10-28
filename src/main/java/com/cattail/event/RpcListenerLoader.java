package com.cattail.event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public class RpcListenerLoader {

    private static ExecutorService eventThreadPool = Executors.newFixedThreadPool(2);

    private static List<IRpcListener> rpcListenerList = new ArrayList<>();

    public void init() {
        registerListener(new AddRpcListener());
        registerListener(new DestroyRpcListener());
        registerListener(new UpdateRpcListener());

    }

    public static void registerListener(IRpcListener rpcListener) {
        rpcListenerList.add(rpcListener);
    }

    public static void sendEvent(RpcEventData eventData) {
        if (eventData == null) {
            return;
        }
        if (!rpcListenerList.isEmpty()) {
            for (IRpcListener irpcListener : rpcListenerList) {
                // 获取接口上的范性，即根据EventData来匹配
                final Class<?> generics = getInterfaceGenerics(irpcListener);
                if (eventData.getClass().equals(generics)) {
                    eventThreadPool.execute(() -> {
                        irpcListener.exec(eventData);
                    });
                }
            }
        }
    }

    public static Class<?> getInterfaceGenerics(Object o) {
        Type[] types = o.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Type type = parameterizedType.getActualTypeArguments()[0];
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        return null;
    }
}
