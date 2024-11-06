package com.cattail.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/11/6
 * @Copyright: https://github.com/CatTailzz
 */
public class IpUtil {

    public static String getIP(){
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return inetAddress.getHostAddress();
    }
}
