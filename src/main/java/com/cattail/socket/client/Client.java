package com.cattail.socket.client;

import com.cattail.common.*;
import com.cattail.common.constants.MsgType;
import com.cattail.common.constants.ProtocolConstants;
import com.cattail.common.constants.RpcProxy;
import com.cattail.common.constants.RpcSerialization;
import com.cattail.proxy.IProxy;
import com.cattail.proxy.ProxyFactory;
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
    }

    public void registerBean(String serviceName) {
        final URL url = new URL(this.host, this.port);
        Cache.services.put(new ServiceName(serviceName), url);
        channelFuture = bootstrap.connect(host, port);
        Cache.channelFutureMap.put(url, channelFuture);
    }

    public static void main(String[] args) throws Exception {
        final Client client = new Client("127.0.0.1", 12345);
        client.registerBean(HelloService.class.getName());
        final IProxy iProxy = ProxyFactory.get(RpcProxy.CG_LIB);
        final HelloService proxy = iProxy.getProxy(HelloService.class);
        System.out.println(proxy.hello("cattail"));


    }
}

