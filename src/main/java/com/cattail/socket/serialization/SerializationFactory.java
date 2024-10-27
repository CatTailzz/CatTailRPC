package com.cattail.socket.serialization;

import com.cattail.common.constants.RpcSerialization;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/26
 * @Copyright: https://github.com/CatTailzz
 */
public class SerializationFactory {

    private static Map<RpcSerialization, com.cattail.socket.serialization.RpcSerialization> serializationMap = new HashMap<>();

    static {
        serializationMap.put(RpcSerialization.JSON, new JsonSerialization());
    }

    public static com.cattail.socket.serialization.RpcSerialization get(RpcSerialization serialization) {
        return serializationMap.get(serialization);
    }
}
