package com.cattail.event;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
// 监听zookeeper事件，根据事件类型来操作本地缓存
public interface IRpcListener<T> {

    void exec(T t);
}
