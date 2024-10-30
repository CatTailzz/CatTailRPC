package com.cattail.proxy.cglib;

import com.cattail.annotation.RpcReference;
import com.cattail.common.*;
import com.cattail.common.constants.*;
import com.cattail.filter.*;
import com.cattail.register.RegistryFactory;
import com.cattail.router.LoadBalancer;
import com.cattail.router.LoadBalancerFactory;
import com.cattail.service.HelloService;
import com.cattail.socket.codec.MsgHeader;
import com.cattail.socket.codec.RpcProtocol;
import com.cattail.socket.codec.RpcRequest;
import com.cattail.socket.codec.RpcResponse;
import com.cattail.tolerant.FaultContext;
import com.cattail.tolerant.FaultTolerantFactory;
import com.cattail.tolerant.IFaultTolerantStrategy;
import io.netty.channel.ChannelFuture;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public class CgLibProxy implements MethodInterceptor {

    private final String serviceName;

    private final String version;

    private final FaultTolerant faultTolerant;

    private final long time;

    private final TimeUnit timeUnit;

    public CgLibProxy(Class clazz) {
        this.serviceName = clazz.getName();
        RpcReference annotation = (RpcReference) clazz.getAnnotation(RpcReference.class);
        this.version = annotation.version();
        this.faultTolerant = annotation.faultTolerant();
        this.time = annotation.time();
        this.timeUnit = annotation.timeUnit();
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
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodCode(method.hashCode());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setServiceVersion(version);
        if (null != objects && objects.length > 0) {
            rpcRequest.setParameterTypes(objects[0].getClass());
            rpcRequest.setParameter(objects[0]);
        }
        rpcProtocol.setBody(rpcRequest);

        // 可用的服务从zookeeper里获取
        final List<URL> urls = RegistryFactory.get(Register.ZOOKEEPER).discoveries(serviceName, version);
        if (urls.isEmpty()) {
            throw new Exception("无服务可用：" + serviceName);
        }
        // 这里先取第一个，后面换负载均衡
        final LoadBalancer loadBalancer = LoadBalancerFactory.get(LoadBalance.Round);
        final URL url = loadBalancer.select(urls);

        final ChannelFuture channelFuture = Cache.CHANNEL_FUTURE_MAP.get(new Host(url.getIp(), url.getPort()));
        // 发送前
        FilterLoader.addAndHandelFilter(FilterFactory.getClientBeforeFilters(), new FilterData<>(rpcRequest));
//        final List<Filter> clientBeforeFilters = FilterFactory.getClientBeforeFilters();
//        if (!clientBeforeFilters.isEmpty()) {
//            final FilterData<RpcRequest> rpcRequestFilterData = new FilterData<>(rpcRequest);
//            final FilterLoader filterLoader = new FilterLoader();
//            filterLoader.addFilter(clientBeforeFilters);
//            final FilterResponse filterResponse = filterLoader.doFilter(rpcRequestFilterData);
//            if (!filterResponse.getResult()) {
//                throw filterResponse.getException();
//            }
//        }
        //发送
        channelFuture.channel().writeAndFlush(rpcProtocol);

        RpcFuture<RpcResponse> future = new RpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()), time);
        RpcRequestHolder.REQUEST_MAP.put(requestId, future);
        RpcResponse rpcResponse = future.getPromise().sync().get(future.getTimeout(), timeUnit);

        // 发送后
        FilterLoader.addAndHandelFilter(FilterFactory.getClientAfterFilters(), new FilterData<>(rpcResponse));
//        final List<Filter> clientAfterFilters = FilterFactory.getClientAfterFilters();
//        if (!clientAfterFilters.isEmpty()) {
//            final FilterData<RpcResponse> rpcResponseFilterData = new FilterData<>(rpcResponse);
//            final FilterLoader filterLoader = new FilterLoader();
//            filterLoader.addFilter(clientAfterFilters);
//            final FilterResponse filterResponse = filterLoader.doFilter(rpcResponseFilterData);
//            if (!filterResponse.getResult()) {
//                throw filterResponse.getException();
//            }
//        }

        // 发生异常
        if (rpcResponse.getException() != null) {
            rpcResponse.getException().printStackTrace();
            final FaultContext faultContext = new FaultContext(url, urls, rpcProtocol, requestId, rpcResponse.getException());
            final IFaultTolerantStrategy iFaultTolerantStrategy = FaultTolerantFactory.get(faultTolerant);
            return iFaultTolerantStrategy.handler(faultContext);
        }
        return rpcResponse.getData();

    }
}
