package com.cattail.socket.server;

import com.cattail.annotation.RpcService;
import com.cattail.common.Cache;
import com.cattail.common.URL;
import com.cattail.common.constants.Register;
import com.cattail.filter.Filter;
import com.cattail.filter.FilterData;
import com.cattail.filter.FilterFactory;
import com.cattail.filter.FilterResponse;
import com.cattail.register.RegistryFactory;
import com.cattail.register.RegistryService;
import com.cattail.service.HelloService;
import com.cattail.socket.codec.RpcDecoder;
import com.cattail.socket.codec.RpcEncoder;
import com.cattail.utils.ServiceNameBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.Service;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/26
 * @Copyright: https://github.com/CatTailzz
 */
public class Server {
    private Logger logger = LoggerFactory.getLogger(Server.class);

    private String host;
    private final int port;

    private ServerBootstrap bootstrap;

    public Server(int port) {

        this.port = port;
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        host = inetAddress.getHostAddress();
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new RpcEncoder());
                            ch.pipeline().addLast(new RpcDecoder());
                            ch.pipeline().addLast(new ServiceHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.bind(port).sync().channel().closeFuture().sync();
            logger.info("rpc server start...", port);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

    public void registerBean(Class clazz) throws Exception {
        final URL url = new URL(host, port);
        if (!clazz.isAnnotationPresent(RpcService.class)) {
            throw new Exception(clazz.getName() + "没有注解 RpcService");
        }
        final RpcService annotation = (RpcService) clazz.getAnnotation(RpcService.class);
        String serverName = clazz.getInterfaces()[0].getName();
        if (!annotation.serviceInterface().equals(void.class)) {
            serverName = annotation.serviceInterface().getName();
        }
        url.setServiceName(serverName);
        url.setVersion(annotation.version());
        final RegistryService registryService = RegistryFactory.get(Register.ZOOKEEPER);
        registryService.register(url);
        final String key = ServiceNameBuilder.buildServiceKey(serverName, annotation.version());
        Cache.SERVICE_MAP.put(key, clazz.newInstance());

    }

    public static void main(String[] args) throws Exception {
        final Server server = new Server(12345);
        FilterFactory.registerServiceBeforeFilter(new ServerTokenFilter());
        FilterFactory.registerServiceAfterFilter(new Filter() {
            @Override
            public FilterResponse doFilter(FilterData filterData) {
                System.out.println("server after");
                return new FilterResponse(true, null);
            }
        });
        server.registerBean(HelloService.class);
        server.run();
    }
}
