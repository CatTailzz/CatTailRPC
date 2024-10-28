package com.cattail.register;

import com.cattail.common.constants.Register;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public class RegistryFactory {

    private static Map<Register, RegistryService> registryServiceMap = new HashMap<>();

    static {
        registryServiceMap.put(Register.ZOOKEEPER, new CuratorZookeeperRegistry("127.0.0.1:2181"));
    }

    public static RegistryService get(Register register) {
        return registryServiceMap.get(register);
    }
}
