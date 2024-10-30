package com.cattail.socket.client;

import com.cattail.common.*;
import com.cattail.common.constants.*;
import com.cattail.event.RpcListenerLoader;
import com.cattail.filter.Filter;
import com.cattail.filter.FilterData;
import com.cattail.filter.FilterFactory;
import com.cattail.filter.FilterResponse;
import com.cattail.proxy.IProxy;
import com.cattail.proxy.ProxyFactory;
import com.cattail.register.RegistryFactory;
import com.cattail.register.RegistryService;
import com.cattail.service.HelloService;
import com.cattail.service.IHelloService;
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
import java.util.List;
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

    private Bootstrap bootstrap;
    private EventLoopGroup eventLoopGroup;

    private ChannelFuture channelFuture;

    public Client(String host, Integer port) throws InterruptedException {
        this.host = host;
        this.port = port;
    }

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

    public static void main(String[] args) throws Exception {
        new RpcListenerLoader().init();
        final Client client = new Client("127.0.0.1", 12345);
        client.run();
        final RegistryService registryService = RegistryFactory.get(Register.ZOOKEEPER);
        final URL url = new URL();
        url.setServiceName(IHelloService.class.getName());
        url.setVersion("1.0");
        registryService.subscribe(url);
        client.connectServer();
        final IProxy iProxy = ProxyFactory.get(RpcProxy.CG_LIB);
        final IHelloService proxy = iProxy.getProxy(IHelloService.class);
        FilterFactory.registerClientBeforeFilter(new ClientTokenFilter());
        FilterFactory.registerClientAfterFilter(new Filter() {
            @Override
            public FilterResponse doFilter(FilterData filterData) {
                System.out.println("client after");
                return new FilterResponse(true, null);
            }
        });
        System.out.println(proxy.hello("cattail"));
        System.out.println("===");
        System.out.println(proxy.hello("xxx"));
        System.out.println("===");
        System.out.println(proxy.hello("xxx"));
        System.out.println("===");
        System.out.println(proxy.hello("xxx"));

    }
}

