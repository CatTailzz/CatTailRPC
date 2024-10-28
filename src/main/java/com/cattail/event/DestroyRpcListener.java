package com.cattail.event;

import com.cattail.common.Cache;
import com.cattail.common.Host;
import com.cattail.common.ServiceName;
import com.cattail.common.URL;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public class DestroyRpcListener implements IRpcListener<DestroyRpcEventData>{

    @Override
    public void exec(DestroyRpcEventData destroyRpcEventData) {
        final URL url = (URL) destroyRpcEventData.getData();
        final ServiceName serviceName = new ServiceName(url.getServiceName(), url.getVersion());
        if (Cache.SERVICE_URLS.containsKey(serviceName)) {
            Cache.SERVICE_URLS.get(serviceName).remove(url);
        }
        final Host ip = new Host(url.getIp(), url.getPort());
        if (Cache.CHANNEL_FUTURE_MAP.containsKey(ip)) {
            Cache.CHANNEL_FUTURE_MAP.remove(ip);
        }
    }
}
