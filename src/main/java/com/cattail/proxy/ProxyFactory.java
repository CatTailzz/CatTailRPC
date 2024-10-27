package com.cattail.proxy;

import com.cattail.common.constants.RpcProxy;
import com.cattail.proxy.cglib.CgLibProxyFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public class ProxyFactory {

    private static Map<RpcProxy, IProxy> proxyIProxyMap = new HashMap<>();

    static {
        proxyIProxyMap.put(RpcProxy.CG_LIB, new CgLibProxyFactory<>());
    }

    public static IProxy get(RpcProxy rpcProxy) {
        return proxyIProxyMap.get(rpcProxy);
    }
}
