package com.cattail.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/11/6
 * @Copyright: https://github.com/CatTailzz
 */
@Component
public class ProviderConfig {

    @Bean
    public RpcProperties rpcProperties(){
        return new RpcProperties();
    }

    @Bean
    public ProviderPostProcessor providerPostProcessor(RpcProperties rpcProperties){
        Properties.setPort(rpcProperties.getPort());
        Properties.setRegister(rpcProperties.getRegistry());
        Properties.setInvoke(rpcProperties.getInvoke());
        Properties.setSerialization(rpcProperties.getSerialization());
        Properties.setCorePollSize(rpcProperties.getCorePollSize());
        Properties.setMaximumPoolSize(rpcProperties.getMaximumPoolSize());
        return new ProviderPostProcessor(rpcProperties);
    }
}
