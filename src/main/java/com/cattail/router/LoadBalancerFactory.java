package com.cattail.router;

import com.cattail.common.constants.LoadBalance;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public class LoadBalancerFactory {

    private static Map<LoadBalance, LoadBalancer> loadBalancerMap = new HashMap<>();

    static {
        loadBalancerMap.put(LoadBalance.Round, new RoundRobinLoadBalancer());
    }

    public static LoadBalancer get(LoadBalance loadBalance) {
        return loadBalancerMap.get(loadBalance);
    }
}
