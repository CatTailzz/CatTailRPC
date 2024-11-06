package com.cattail.socket.client;

import com.cattail.common.*;
import com.cattail.common.constants.*;
import com.cattail.event.RpcListenerLoader;

import com.cattail.filter.FilterFactory;

import com.cattail.proxy.ProxyFactory;
import com.cattail.register.RegistryFactory;
import com.cattail.register.RegistryService;
import com.cattail.router.LoadBalancerFactory;

import com.cattail.socket.codec.*;
import com.cattail.tolerant.FaultTolerantFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;



import java.io.IOException;

import java.util.List;


/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/26
 * @Copyright: https://github.com/CatTailzz
 */
public class Client {

    private Bootstrap bootstrap;
    private EventLoopGroup eventLoopGroup;


    public void run(){
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

        Cache.BOOT_STRAP = bootstrap;
    }

    public void connectServer() throws Exception {
        for (URL url : Cache.SUBSCRIBE_SERVICE_LIST) {
            final RegistryService registryService = RegistryFactory.get(Register.ZOOKEEPER);
            final List<URL> urls = registryService.discoveries(url.getServiceName(), url.getVersion());
            if (!urls.isEmpty()) {
                for (URL u : urls) {
                    final ChannelFuture connect = bootstrap.connect(u.getIp(), u.getPort());
                    Cache.CHANNEL_FUTURE_MAP.put(new Host(u.getIp(), u.getPort()), connect);
                }
            }

        }
    }

    public void init() throws IOException, ClassNotFoundException {
        new RpcListenerLoader().init();
        FaultTolerantFactory.init();
        RegistryFactory.init();
        FilterFactory.initClient();
        ProxyFactory.init();
        LoadBalancerFactory.init();
//        SerializationFactory.init();
    }


}

