package com.cattail.spi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/31
 * @Copyright: https://github.com/CatTailzz
 */
public class ExtensionLoader {

    // 系统SPI
    private static String SYS_EXTENSION_LOADER_DIR_PREFIX = "META-INF/xrpc/";

    // 用户SPI
    private static String DIY_EXTENSION_LOADER_DIR_PREFIX = "META-INF/rpc/";

    private static String[] prefixs = {SYS_EXTENSION_LOADER_DIR_PREFIX, DIY_EXTENSION_LOADER_DIR_PREFIX};

    // bean定义信息
    // key:定义的key， value：具体类
    private static Map<String, Class> extensionClassCache = new ConcurrentHashMap<>();

    // bean定义信息
    // key：接口， value：接口子类
    private static Map<String, Map<String, Class>> extensionClassCaches = new ConcurrentHashMap<>();

    // 实例化的bean
    private static Map<String, Object> singletonObject = new ConcurrentHashMap<>();

    private static ExtensionLoader extensionLoader;

    static {
        extensionLoader = new ExtensionLoader();
    }

    public static ExtensionLoader getInstance() {
        return extensionLoader;
    }

    private ExtensionLoader() {
        // 私有构造函数，防止外部初始化
    }


    /**
     * 根据name获取bean
     * @param name
     * @return
     * @param <V>
     */
    public <V> V get(String name) {
        if (!singletonObject.containsKey(name)) {
            try {
                singletonObject.put(name, extensionClassCache.get(name).newInstance());
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return (V) singletonObject.get(name);
    }


    /**
     * 获取接口下所有的类
     * @param clazz
     * @return
     */
    public List gets(Class clazz) {
        final String name = clazz.getName();
        if (!extensionClassCaches.containsKey(name)) {
            try {
                throw new ClassNotFoundException(clazz + "not found");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        final Map<String, Class> stringClassMap = extensionClassCaches.get(name);
        List<Object> objects = new ArrayList<>();
        if (stringClassMap.size() > 0) {
            stringClassMap.forEach((k, v) -> {
                try {
                    objects.add(singletonObject.getOrDefault(k, v.newInstance()));
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return objects;
    }

    /**
     * 根据spi机制初加载bean的信息放入map，传入一个接口
     * @param clazz
     */
    public void loadExtension(Class clazz) throws IOException, ClassNotFoundException {
        if (clazz == null) {
            throw new IllegalArgumentException("class not found");
        }
        ClassLoader classLoader = this.getClass().getClassLoader();
        Map<String, Class> classMap = new HashMap<>();
        // 从系统以及用户SPI中加载
        for (String prefix : prefixs) {
            String spiFIlePath = prefix + clazz.getName();
            Enumeration<URL> resources = classLoader.getResources(spiFIlePath);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                InputStreamReader inputStreamReader = null;
                inputStreamReader = new InputStreamReader(url.openStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] lineArr = line.split("=");
                    String key = lineArr[0];
                    String name = lineArr[1];
                    final Class<?> aClass = Class.forName(name);
                    extensionClassCache.put(key, aClass);
                    classMap.put(key, aClass);
                }
            }
        }
        extensionClassCaches.put(clazz.getName(), classMap);
    }
}
