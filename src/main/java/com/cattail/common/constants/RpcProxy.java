package com.cattail.common.constants;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public enum RpcProxy {
    CG_LIB("cglib");

    public String name;

    RpcProxy(String name) {
        this.name = name;
    }

    public static RpcProxy get(String type) {
        for (RpcProxy value : values()) {
            if (value.name.equals(type)) {
                return value;
            }
        }
        return null;
    }
}
