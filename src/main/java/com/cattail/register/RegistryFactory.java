package com.cattail.register;

import com.cattail.common.constants.Register;
import com.cattail.spi.ExtensionLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public class RegistryFactory {


    public static RegistryService get(Register register) {
        return ExtensionLoader.getInstance().get(register.name);
    }

    public static RegistryService get(String name) {
        return ExtensionLoader.getInstance().get(name);
    }

    public static void init() throws IOException, ClassNotFoundException {
        ExtensionLoader.getInstance().loadExtension(RegistryService.class);
    }
}
