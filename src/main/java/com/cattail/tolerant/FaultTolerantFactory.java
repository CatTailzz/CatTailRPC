package com.cattail.tolerant;

import com.cattail.common.constants.FaultTolerant;
import com.cattail.spi.ExtensionLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/30
 * @Copyright: https://github.com/CatTailzz
 */
public class FaultTolerantFactory {

    public static IFaultTolerantStrategy get(FaultTolerant faultTolerant) {
        return ExtensionLoader.getInstance().get(faultTolerant.name);
    }

    public static IFaultTolerantStrategy get(String name) {
        return ExtensionLoader.getInstance().get(name);
    }

    public static void init() throws IOException, ClassNotFoundException {
        ExtensionLoader.getInstance().loadExtension(IFaultTolerantStrategy.class);
    }
}
