package com.cattail.proxy;

import com.cattail.common.constants.RpcProxy;
import com.cattail.proxy.cglib.CgLibProxyFactory;
import com.cattail.spi.ExtensionLoader;
import sun.security.ec.point.ImmutablePoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public class ProxyFactory {


    public static IProxy get(RpcProxy rpcProxy) {
        return ExtensionLoader.getInstance().get(rpcProxy.name);
    }

    public static IProxy get(String name) {
        return ExtensionLoader.getInstance().get(name);
    }

    public static void init() throws IOException, ClassNotFoundException {
        ExtensionLoader.getInstance().loadExtension(IProxy.class);
    }
}
