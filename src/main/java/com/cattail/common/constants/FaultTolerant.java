package com.cattail.common.constants;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/30
 * @Copyright: https://github.com/CatTailzz
 */
public enum FaultTolerant {

    Failover("failover"),

    FailFast("failFast");

    final String name;

    FaultTolerant(String name) {
        this.name = name;
    }
}
