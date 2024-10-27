package com.cattail.proxy;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public interface IProxy {

    <T> T getProxy(Class<T> clz) throws InstantiationException, IllegalAccessException;
}
