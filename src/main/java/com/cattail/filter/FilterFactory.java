package com.cattail.filter;

import com.cattail.filter.client.ClientAfterFilter;
import com.cattail.filter.client.ClientBeforeFilter;
import com.cattail.filter.server.ServerAfterFilter;
import com.cattail.filter.server.ServerBeforeFilter;
import com.cattail.spi.ExtensionLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/30
 * @Copyright: https://github.com/CatTailzz
 */
public class FilterFactory {



    public static List<Filter> getClientBeforeFilters() {
        return ExtensionLoader.getInstance().gets(ClientBeforeFilter.class);
    }


    public static List<Filter> getClientAfterFilters() {
        return ExtensionLoader.getInstance().gets(ClientAfterFilter.class);
    }


    public static List<Filter> getServiceBeforeFilters() {
        return ExtensionLoader.getInstance().gets(ServerBeforeFilter.class);
    }


    public static List<Filter> getServiceAfterFilters() {
        return ExtensionLoader.getInstance().gets(ServerAfterFilter.class);
    }

    public static void initClient() throws IOException, ClassNotFoundException {
        final ExtensionLoader extensionLoader = ExtensionLoader.getInstance();
        extensionLoader.loadExtension(ClientAfterFilter.class);
        extensionLoader.loadExtension(ClientBeforeFilter.class);
    }

    public static void initServer() throws IOException, ClassNotFoundException {
        final ExtensionLoader extensionLoader = ExtensionLoader.getInstance();
        extensionLoader.loadExtension(ServerBeforeFilter.class);
        extensionLoader.loadExtension(ServerAfterFilter.class);
    }

}
