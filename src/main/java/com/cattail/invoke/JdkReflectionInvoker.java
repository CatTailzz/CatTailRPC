package com.cattail.invoke;

import com.cattail.common.Cache;
import com.cattail.utils.ServiceNameBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/27
 * @Copyright: https://github.com/CatTailzz
 */
public class JdkReflectionInvoker implements Invoker{

    //jdk反射较慢，加缓存提升速度
    private Map<Integer, MethodInvocation> methodCache = new HashMap<>();

    @Override
    public Object invoke(Invocation invocation) throws InvocationTargetException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final Integer methodCode = invocation.getMethodCode();
        if (!methodCache.containsKey(methodCode)) {
            final String key = ServiceNameBuilder.buildServiceKey(invocation.getClassName(), invocation.getServiceVersion());
            Object bean = Cache.SERVICE_MAP.get(key);
            final Class<?> aClass = bean.getClass();
            final Method method = aClass.getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            methodCache.put(methodCode, new MethodInvocation(bean, method));
        }
        final MethodInvocation methodInvocation = methodCache.get(methodCode);
        return methodInvocation.invoke(invocation.getParameter());
    }
}
