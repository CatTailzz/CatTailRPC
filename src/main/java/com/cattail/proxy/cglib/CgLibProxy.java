package com.cattail.proxy.cglib;

import com.cattail.common.*;
import com.cattail.common.constants.MsgType;
import com.cattail.common.constants.ProtocolConstants;
import com.cattail.common.constants.Register;
import com.cattail.common.constants.RpcSerialization;
import com.cattail.register.RegistryFactory;
import com.cattail.service.HelloService;
import com.cattail.socket.codec.MsgHeader;
import com.cattail.socket.codec.RpcProtocol;
import com.cattail.socket.codec.RpcRequest;
import com.cattail.socket.codec.RpcResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public class CgLibProxy implements MethodInterceptor {

    private final Object object;

    private final String serviceName;

    public CgLibProxy(Object object) {
        this.object = object;
        this.serviceName = object.getClass().getName();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        final RpcProtocol rpcProtocol = new RpcProtocol();
        // 构建消息头
        MsgHeader header = new MsgHeader();
        long requestId = RpcRequestHolder.getRequestId();
        header.setMagic(ProtocolConstants.MAGIC);
        header.setVersion(ProtocolConstants.VERSION);
        header.setRequestId(requestId);

        final byte[] serialzation = RpcSerialization.JSON.name.getBytes();
        header.setSerializationLen(serialzation.length);
        header.setSerialization(serialzation);
        // 获取枚举的索引值
        header.setMsgType((byte) MsgType.REQUEST.ordinal());
        header.setStatus((byte) 0x1);
        rpcProtocol.setHeader(header);

        //设置消息体
        final RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(object.getClass().getName());
        rpcRequest.setMethodCode(method.hashCode());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setServiceVersion("1.0");
        if (null != objects && objects.length > 0) {
            rpcRequest.setParameterTypes(objects[0].getClass());
            rpcRequest.setParameter(objects[0]);
        }
        rpcProtocol.setBody(rpcRequest);

        // 可用的服务从zookeeper里获取
        final List<URL> urls = RegistryFactory.get(Register.ZOOKEEPER).discoveries(serviceName, "1.0");
        if (urls.isEmpty()) {
            throw new Exception("无服务可用：" + serviceName);
        }
        // 这里先取第一个，后面换负载均衡
        final URL url = urls.get(0);

        final ChannelFuture channelFuture = Cache.CHANNEL_FUTURE_MAP.get(new Host(url.getIp(), url.getPort()));
        channelFuture.channel().writeAndFlush(rpcProtocol);

        RpcFuture<RpcResponse> future = new RpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()), 3000);
        RpcRequestHolder.REQUEST_MAP.put(requestId, future);
        RpcResponse rpcResponse = future.getPromise().sync().get(future.getTimeout(), TimeUnit.MILLISECONDS);

        if (rpcResponse.getException() != null) {
            throw rpcResponse.getException();
        }
        return rpcResponse.getData();

    }
}
