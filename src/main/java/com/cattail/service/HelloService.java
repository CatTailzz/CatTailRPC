package com.cattail.service;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/27
 * @Copyright: https://github.com/CatTailzz
 */
public class HelloService implements IHelloService{

    @Override
    public Object hello(String text) {
        return "hello" + text;
    }
}
