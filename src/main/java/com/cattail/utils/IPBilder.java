package com.cattail.utils;

import com.cattail.common.Host;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public class IPBilder {

    private static final String symbol = ":";

    public static String buildIp(String host, Integer port) {
        return host + symbol + port;
    }
}
