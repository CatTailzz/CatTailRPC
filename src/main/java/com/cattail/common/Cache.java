package com.cattail.common;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public class Cache {

    //根据接口名找所有符合的服务url
    public static ConcurrentHashMap<ServiceName, List<URL>> SERVICE_URLS = new ConcurrentHashMap<>();
    //根据ip+port找channel
    public static Map<Host, ChannelFuture> CHANNEL_FUTURE_MAP = new HashMap<>();
    //所有可用的订阅服务
    public static List<URL> SUBSCRIBE_SERVICE_LIST = new ArrayList<>();

    public static Bootstrap BOOT_STRAP;
}
