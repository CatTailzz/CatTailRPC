package com.cattail.tolerant;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/30
 * @Copyright: https://github.com/CatTailzz
 */
public class FailFastIFaultTolerantStrategy implements IFaultTolerantStrategy {
    @Override
    public Object handler(FaultContext faultContext) throws Exception {
        return faultContext.getException();
    }
}
