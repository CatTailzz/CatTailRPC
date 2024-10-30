package com.cattail.tolerant;

import com.cattail.common.*;
import com.cattail.socket.codec.RpcResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/30
 * @Copyright: https://github.com/CatTailzz
 */
// 故障转移
public class FailoverIFaultTorelrantStrategy implements IFaultTolerantStrategy {

    @Override
    public Object handler(FaultContext faultContext) throws Exception {
        final URL currentURL = faultContext.getCurrentURL();
        final List<URL> urls = faultContext.getUrls();
        final Exception exception = faultContext.getException();
        final Iterator<URL> iterator = urls.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(currentURL)) {
                iterator.remove();
            }
        }
        if (urls.isEmpty()) {
            throw new Exception("无可用服务");
        }
        final URL url = urls.get(0);
        final ChannelFuture channelFuture = Cache.CHANNEL_FUTURE_MAP.get(new Host(url.getIp(), url.getPort()));

        channelFuture.channel().writeAndFlush(faultContext.getRpcProtocol());
        RpcFuture<RpcResponse> future = new RpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()), 3000);
        RpcRequestHolder.REQUEST_MAP.put(faultContext.getRequestId(), future);
        RpcResponse rpcResponse = future.getPromise().sync().get(future.getTimeout(), TimeUnit.MILLISECONDS);
        if (rpcResponse.getException() != null) {
            faultContext.setCurrentURL(url);
            return handler(faultContext);
        }
        return rpcResponse.getData();
    }
}
