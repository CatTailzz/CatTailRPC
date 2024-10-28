package com.cattail.utils;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public class ServiceNameBuilder {

    public static String buildServiceKey(String serviceName, String serviceVersion) {
        return String.join("$", serviceName, serviceVersion);
    }

    public static String buildServiceNodeInfo(String key,String ip,Integer port){
        return String.join("#", key, ip,String.valueOf(port));
    }
}