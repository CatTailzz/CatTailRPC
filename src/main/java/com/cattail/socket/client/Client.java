package com.cattail.socket.client;

import com.cattail.common.RpcFuture;
import com.cattail.common.RpcRequestHolder;
import com.cattail.common.constants.MsgType;
import com.cattail.common.constants.ProtocolConstants;
import com.cattail.common.constants.RpcSerialization;
import com.cattail.service.HelloService;
import com.cattail.socket.codec.*;
import com.sun.jndi.cosnaming.IiopUrl;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.Port;
import java.awt.print.Book;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/26
 * @Copyright: https://github.com/CatTailzz
 */
public class Client {
    private Logger logger = LoggerFactory.getLogger(Client.class);

    private final String host;

    private final Integer port;

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    private ChannelFuture channelFuture;

    public Client(String host, Integer port) throws InterruptedException {
        this.host = host;
        this.port = port;

        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup(4);
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new RpcEncoder())
                                .addLast(new RpcDecoder())
                                .addLast(new ClientHanlder());
                    }
                });
        channelFuture = bootstrap.connect(host, port).sync();
    }

    public void sendRequest(Object o) {
        channelFuture.channel().writeAndFlush(o);
    }

    public static void main(String[] args) throws InterruptedException, NoSuchMethodException, ExecutionException, TimeoutException {
        final Client nettyClient = new Client("127.0.0.1", 12345);
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
        final Class<HelloService> aClass = HelloService.class;
        rpcRequest.setClassName(aClass.getName());
        final Method method = aClass.getMethod("hello", String.class);
        rpcRequest.setMethodCode(method.hashCode());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setServiceVersion("1.0");
        rpcRequest.setParameterTypes(method.getParameterTypes()[0]);
        rpcRequest.setParameter("cattail!");

        rpcProtocol.setBody(rpcRequest);
        nettyClient.sendRequest(rpcProtocol);

        RpcFuture<RpcResponse> future = new RpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()), 3000);
        RpcRequestHolder.REQUEST_MAP.put(requestId, future);
        RpcResponse rpcResponse = future.getPromise().sync().get(future.getTimeout(), TimeUnit.MILLISECONDS);
        System.out.println(rpcResponse);
    }
}

