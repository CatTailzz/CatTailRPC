package com.cattail.service;

import com.cattail.annotation.RpcService;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/29
 * @Copyright: https://github.com/CatTailzz
 */
@RpcService(version = "1.0", serviceInterface = IHelloService.class)
public class HelloService2 implements Comparable, IHelloService{

    @Override
    public Object hello(String text) {
        return "bye!" + text;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}