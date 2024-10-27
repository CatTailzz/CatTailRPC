package com.cattail.common.constants;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/26
 * @Copyright: https://github.com/CatTailzz
 */
public enum RpcSerialization {
    JSON("json"),
    JDK("jdk");

    public String name;

    RpcSerialization(String type) {
        this.name = type;
    }

    public static RpcSerialization get(String type) {
        for (RpcSerialization value : values()) {
            if (value.name.equals(type)) {
                return value;
            }
        }
        return null;
    }
}
