package com.cattail.invoke;

import java.lang.reflect.InvocationTargetException;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/27
 * @Copyright: https://github.com/CatTailzz
 */
public interface Invoker {

    Object invoke(Invocation invocation) throws InvocationTargetException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}
