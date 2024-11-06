package com.cattail.socket.server;

import com.cattail.annotation.RpcService;
import com.cattail.common.Cache;
import com.cattail.common.URL;
import com.cattail.common.constants.Register;
import com.cattail.config.Properties;
import com.cattail.filter.Filter;
import com.cattail.filter.FilterData;
import com.cattail.filter.FilterFactory;
import com.cattail.filter.FilterResponse;
import com.cattail.invoke.InvokerFactory;
import com.cattail.register.RegistryFactory;
import com.cattail.register.RegistryService;
import com.cattail.service.HelloService;
import com.cattail.socket.codec.RpcDecoder;
import com.cattail.socket.codec.RpcEncoder;
import com.cattail.socket.serialization.SerializationFactory;
import com.cattail.utils.ServiceNameBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.Service;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/26
 * @Copyright: https://github.com/CatTailzz
 */
public class Server {

    private Integer port;

    private ServerBootstrap bootstrap;

    public Server(Integer port) {

        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            bootstrap= new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new RpcEncoder());
                            ch.pipeline().addLast(new RpcDecoder());
                            ch.pipeline().addLast(new ServiceHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            if (port == null){
                bootstrap.bind(0).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (channelFuture.isSuccess()) {
                            Channel channel = channelFuture.channel();
                            InetSocketAddress localAddress = (InetSocketAddress) channel.localAddress();
                            Properties.setPort(localAddress.getPort());
                        }
                    }
                }).sync().channel().closeFuture().sync();
            }else{
                bootstrap.bind(port).sync().channel().closeFuture().sync();
            }
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }


    public void init() throws IOException, ClassNotFoundException {
        RegistryFactory.init();
        FilterFactory.initServer();
        InvokerFactory.init();
        SerializationFactory.init();
    }


}
