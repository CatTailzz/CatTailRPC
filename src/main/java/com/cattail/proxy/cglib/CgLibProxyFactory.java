package com.cattail.proxy.cglib;

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
    public <T> T getProxy(Class<T> clz) throws InstantiationException, IllegalAccessException {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clz);
        enhancer.setCallback(new CgLibProxy(clz.newInstance()));
        return (T) enhancer.create();
    }
}
