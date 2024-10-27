package com.cattail.common;

import io.netty.channel.ChannelFuture;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public class Cache {

    public static Map<ServiceName, URL> services = new HashMap<>();

    public static Map<URL, ChannelFuture> channelFutureMap = new HashMap<>();
}
