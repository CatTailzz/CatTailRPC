package com.cattail.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/30
 * @Copyright: https://github.com/CatTailzz
 */
public class FilterLoader {

    private List<Filter> filters = new ArrayList<>();

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public void addFilter(List<Filter> filters) {
        for (Object filter : filters) {
            addFilter((Filter) filter);
        }
    }

    public FilterResponse doFilter(FilterData data) {
        for (Filter filter : filters) {
            final FilterResponse filterResponse = filter.doFilter(data);
            if (!filterResponse.getResult()) {
                return filterResponse;
            }
        }
        return new FilterResponse(true, null);
    }

    public static void addAndHandelFilter(List<Filter> filters, FilterData<?> filterData) throws Exception {
        if (!filters.isEmpty()) {
            final FilterLoader filterLoader = new FilterLoader();
            filterLoader.addFilter(filters);
            final FilterResponse filterResponse = filterLoader.doFilter(filterData);
            if (!filterResponse.getResult()) {
                throw filterResponse.getException();
            }
        }
    }
}
