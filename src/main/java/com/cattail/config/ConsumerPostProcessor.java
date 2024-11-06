package com.cattail.config;

import com.cattail.annotation.RpcReference;
import com.cattail.common.URL;
import com.cattail.common.constants.Register;
import com.cattail.common.constants.RpcProxy;
import com.cattail.event.RpcListenerLoader;
import com.cattail.filter.FilterFactory;
import com.cattail.proxy.IProxy;
import com.cattail.proxy.ProxyFactory;
import com.cattail.register.RegistryFactory;
import com.cattail.register.RegistryService;
import com.cattail.router.LoadBalancerFactory;
import com.cattail.socket.client.Client;
import com.cattail.socket.serialization.SerializationFactory;
import com.cattail.tolerant.FaultTolerantFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/11/6
 * @Copyright: https://github.com/CatTailzz
 */
public class ConsumerPostProcessor implements BeanPostProcessor, InitializingBean {

    RpcProperties rpcProperties;

    public ConsumerPostProcessor(RpcProperties rpcProperties) {
        this.rpcProperties = rpcProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new RpcListenerLoader().init();
        FaultTolerantFactory.init();
        RegistryFactory.init();
        FilterFactory.initClient();
        ProxyFactory.init();
        LoadBalancerFactory.init();
        SerializationFactory.init();
        final Client client = new Client();
        client.run();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 获取所有字段
        final Field[] fields = bean.getClass().getDeclaredFields();
        // 遍历所有字段找到 RpcReference 注解的字段
        for (Field field : fields) {
            if(field.isAnnotationPresent(RpcReference.class)){
                final RegistryService registryService = RegistryFactory.get(Register.ZOOKEEPER);
                final Class<?> aClass = field.getType();
                final RpcReference rpcReference = field.getAnnotation(RpcReference.class);
                field.setAccessible(true);
                Object object = null;
                try {
                    final IProxy iproxy = ProxyFactory.get(RpcProxy.CG_LIB);
                    final Object proxy = iproxy.getProxy(aClass,rpcReference);
                    // 创建代理对象
                    object = proxy;
                    final URL url = new URL();
                    url.setServiceName(aClass.getName());
                    url.setVersion(rpcReference.version());
                    registryService.subscribe(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    // 将代理对象设置给字段
                    field.set(bean,object);
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
