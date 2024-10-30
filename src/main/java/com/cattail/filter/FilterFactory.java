package com.cattail.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/30
 * @Copyright: https://github.com/CatTailzz
 */
public class FilterFactory {

    private static List<Filter> clientBeforeFilters = new ArrayList<>();
    private static List<Filter> clientAfterFilters = new ArrayList<>();
    private static List<Filter> serviceBeforeFilters = new ArrayList<>();
    private static List<Filter> serviceAfterFilters = new ArrayList<>();

    public static void registerClientBeforeFilter(Filter filter) {
        clientBeforeFilters.add(filter);
    }

    public static void registerClientAfterFilter(Filter filter) {
        clientAfterFilters.add(filter);
    }

    public static void registerServiceBeforeFilter(Filter filter) {
        serviceBeforeFilters.add(filter);
    }

    public static void registerServiceAfterFilter(Filter filter) {
        serviceAfterFilters.add(filter);
    }

    public static List<Filter> getClientBeforeFilters() {
        return clientBeforeFilters;
    }


    public static List<Filter> getClientAfterFilters() {
        return clientAfterFilters;
    }


    public static List<Filter> getServiceBeforeFilters() {
        return serviceBeforeFilters;
    }


    public static List<Filter> getServiceAfterFilters() {
        return serviceAfterFilters;
    }

}
