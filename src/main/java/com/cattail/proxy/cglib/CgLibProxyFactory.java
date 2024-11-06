package com.cattail.proxy.cglib;

import com.cattail.annotation.RpcReference;
import com.cattail.proxy.IProxy;
import net.sf.cglib.proxy.Enhancer;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public class CgLibProxyFactory<T> implements IProxy {

    @Override
    public <T> T getProxy(Class<T> clz, RpcReference rpcReference) throws InstantiationException, IllegalAccessException {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clz);
        enhancer.setCallback(new CgLibProxy(clz, rpcReference));
        return (T) enhancer.create();
    }
}
