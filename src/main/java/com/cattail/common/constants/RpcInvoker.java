package com.cattail.common.constants;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/27
 * @Copyright: https://github.com/CatTailzz
 */
public enum RpcInvoker {
    JDK("jdk");

    public String name;

    RpcInvoker(String type) {
        this.name = type;
    }
}
