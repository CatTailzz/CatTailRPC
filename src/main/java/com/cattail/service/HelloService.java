package com.cattail.service;

import com.cattail.annotation.RpcService;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/27
 * @Copyright: https://github.com/CatTailzz
 */
@RpcService(version = "1.0")
public class HelloService implements IHelloService{

    @Override
    public Object hello(String text) {
        int a = 0;
        a /= 0;
        return "hello" + text;
    }
}
