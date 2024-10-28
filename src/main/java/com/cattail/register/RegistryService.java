package com.cattail.register;

import com.cattail.common.URL;

import java.util.List;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public interface RegistryService {

    void register(URL url) throws Exception;

    void unRegistry(URL url) throws Exception;

    List<URL> discoveries(String serviceName, String version) throws Exception;

    void subscribe(URL url) throws Exception;

    void unSubscribe(URL url);
}
