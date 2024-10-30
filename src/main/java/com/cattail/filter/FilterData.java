package com.cattail.filter;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/30
 * @Copyright: https://github.com/CatTailzz
 */
public class FilterData<T> {

    private T object;

    public FilterData(T object) {
        this.object = object;
    }

    public T getObject() {
        return object;
    }
}
