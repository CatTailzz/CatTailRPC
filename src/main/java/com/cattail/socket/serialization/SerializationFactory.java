package com.cattail.socket.serialization;

import com.cattail.common.constants.RpcSerialization;
import com.cattail.spi.ExtensionLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/26
 * @Copyright: https://github.com/CatTailzz
 */
public class SerializationFactory {


    public static com.cattail.socket.serialization.RpcSerialization get(RpcSerialization serialization) {
        return ExtensionLoader.getInstance().get(serialization.name);
    }

    public static com.cattail.socket.serialization.RpcSerialization get(String name) {
        return ExtensionLoader.getInstance().get(name);
    }

    public static void init() throws IOException, ClassNotFoundException {
        ExtensionLoader.getInstance().loadExtension(com.cattail.socket.serialization.RpcSerialization.class);
    }
}
