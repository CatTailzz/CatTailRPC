package com.cattail.filter;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/30
 * @Copyright: https://github.com/CatTailzz
 */
public interface Filter<T> {

    FilterResponse doFilter(FilterData<T> filterData);

}
