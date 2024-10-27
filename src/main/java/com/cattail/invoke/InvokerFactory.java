package com.cattail.invoke;

import com.cattail.common.constants.RpcInvoker;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/27
 * @Copyright: https://github.com/CatTailzz
 */
public class InvokerFactory {

    public static Map<RpcInvoker, Invoker> invokerInvokerMap = new HashMap<>();

    static {
        invokerInvokerMap.put(RpcInvoker.JDK, new JdkReflectionInvoker());
    }

    public static Invoker get(RpcInvoker rpcInvoker) {
        return invokerInvokerMap.get(rpcInvoker);
    }
}
