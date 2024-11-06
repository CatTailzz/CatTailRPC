package com.cattail.config;

import com.cattail.annotation.RpcService;
import com.cattail.common.Cache;
import com.cattail.common.URL;
import com.cattail.filter.FilterFactory;
import com.cattail.invoke.InvokerFactory;
import com.cattail.register.RegistryFactory;
import com.cattail.register.RegistryService;
import com.cattail.socket.serialization.SerializationFactory;
import com.cattail.socket.server.Server;
import com.cattail.utils.IpUtil;
import com.cattail.utils.ServiceNameBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/11/6
 * @Copyright: https://github.com/CatTailzz
 */
public class ProviderPostProcessor implements InitializingBean, BeanPostProcessor {
    private RpcProperties rpcProperties;

    private final String ip = IpUtil.getIP();

    public ProviderPostProcessor(RpcProperties rpcProperties) {
        this.rpcProperties = rpcProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RegistryFactory.init();
        FilterFactory.initServer();
        InvokerFactory.init();
        SerializationFactory.init();
        Thread t = new Thread(() -> {
            final Server server = new Server(rpcProperties.getPort());
            try {
                server.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        t.setDaemon(true);
        t.start();
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        // 找到bean上带有 RpcService 注解的类
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        if (rpcService != null) {
            // 可能会有多个接口,默认选择第一个接口
            String serviceName = beanClass.getInterfaces()[0].getName();
            if (!rpcService.serviceInterface().equals(void.class)){
                serviceName = rpcService.serviceInterface().getName();
            }
            try {
                RegistryService registryService = RegistryFactory.get(rpcProperties.getRegistry().getName());
                final URL url = new URL();
                url.setPort(Properties.getPort());
                url.setIp(ip);
                url.setServiceName(serviceName);
                url.setVersion(rpcService.version());
                registryService.register(url);
                // 缓存
                final String key = ServiceNameBuilder.buildServiceKey(serviceName, rpcService.version());
                Cache.SERVICE_MAP.put(key,bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bean;
    }
}
