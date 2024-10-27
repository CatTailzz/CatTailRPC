package com.cattail.invoke;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/27
 * @Copyright: https://github.com/CatTailzz
 */
public class MethodInvocation {

    private Object o;

    private Method method;

    public MethodInvocation(Object o, Method method) {
        this.o = o;
        this.method = method;
    }

    public Object invoke(Object parameter) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(o, parameter);
    }
}
