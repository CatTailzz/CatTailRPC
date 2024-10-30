package com.cattail.invoke;

import com.cattail.common.constants.RpcInvoker;
import com.cattail.spi.ExtensionLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/27
 * @Copyright: https://github.com/CatTailzz
 */
public class InvokerFactory {

    // 传入的是枚举类的name，作为key去元文件里查找
    public static Invoker get(RpcInvoker rpcInvoker) {
        return ExtensionLoader.getInstance().get(rpcInvoker.name);
    }

    public static Invoker get(String name) {
        return ExtensionLoader.getInstance().get(name);
    }

    public static void init() throws IOException, ClassNotFoundException {
        ExtensionLoader.getInstance().loadExtension(Invoker.class);
    }
}
