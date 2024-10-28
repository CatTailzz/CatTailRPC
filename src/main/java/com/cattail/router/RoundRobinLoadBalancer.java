package com.cattail.router;

import com.cattail.common.URL;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public class RoundRobinLoadBalancer implements LoadBalancer{

    private static AtomicInteger roundRobinId = new AtomicInteger(0);

    @Override
    public URL select(List<URL> urls) {
        roundRobinId.addAndGet(1);
        if (roundRobinId.get() == Integer.MAX_VALUE) {
            roundRobinId.set(0);
        }
        return urls.get(roundRobinId.get() % urls.size());
    }
}
