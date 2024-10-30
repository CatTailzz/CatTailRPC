package com.cattail.filter;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/30
 * @Copyright: https://github.com/CatTailzz
 */
public class FilterResponse {

    private final Boolean result;

    private final Exception exception;

    public FilterResponse(Boolean result, Exception exception) {
        this.result = result;
        this.exception = exception;
    }

    public Boolean getResult() {
        return result;
    }

    public Exception getException() {
        return exception;
    }
}
