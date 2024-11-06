package com.cattail.proxy;

import com.cattail.annotation.RpcReference;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public interface IProxy {

    <T> T getProxy(Class<T> clz, RpcReference rpcReference) throws InstantiationException, IllegalAccessException;
}
