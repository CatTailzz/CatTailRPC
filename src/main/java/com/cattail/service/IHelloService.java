package com.cattail.service;

import com.cattail.annotation.RpcReference;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/27
 * @Copyright: https://github.com/CatTailzz
 */
@RpcReference
public interface IHelloService {

    Object hello(String text);
}
