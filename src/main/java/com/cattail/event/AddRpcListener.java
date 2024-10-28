package com.cattail.event;

import com.cattail.common.Cache;
import com.cattail.common.Host;
import com.cattail.common.ServiceName;
import com.cattail.common.URL;
import io.netty.channel.ChannelFuture;

import java.util.ArrayList;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public class AddRpcListener implements IRpcListener<AddRpcEventData> {

    @Override
    public void exec(AddRpcEventData addRpcEventData) {
        final URL url = (URL) addRpcEventData.getData();
        final ServiceName serviceName = new ServiceName(url.getServiceName(), url.getVersion());
        if (!Cache.SERVICE_URLS.containsKey(serviceName)) {
            Cache.SERVICE_URLS.put(serviceName, new ArrayList<>());
        }
        Cache.SERVICE_URLS.get(serviceName).add(url);
        final Host ip = new Host(url.getIp(), url.getPort());
        if (!Cache.CHANNEL_FUTURE_MAP.containsKey(ip)) {
            ChannelFuture channelFuture = Cache.BOOT_STRAP.connect(url.getIp(), url.getPort());
            Cache.CHANNEL_FUTURE_MAP.put(ip, channelFuture);
        }
    }
}
