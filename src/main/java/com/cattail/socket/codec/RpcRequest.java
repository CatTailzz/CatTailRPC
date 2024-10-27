package com.cattail.socket.codec;

import java.io.Serializable;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/27
 * @Copyright: https://github.com/CatTailzz
 */
public class RpcRequest implements Serializable {

    private String serviceVersion;
    private String className;
    private String methodName;
    private Integer methodCode;
    private Object parameter;
    private Class<?> parameterTypes;

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Integer getMethodCode() {
        return methodCode;
    }

    public void setMethodCode(Integer methodCode) {
        this.methodCode = methodCode;
    }

    public Object getParameter() {
        return parameter;
    }

    public void setParameter(Object parameter) {
        this.parameter = parameter;
    }

    public Class<?> getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
}
