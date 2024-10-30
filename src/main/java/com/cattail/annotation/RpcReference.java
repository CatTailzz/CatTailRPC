package com.cattail.annotation;

import com.cattail.common.constants.FaultTolerant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface RpcReference {

    String version() default "1.0";

    FaultTolerant faultTolerant() default FaultTolerant.Failover;

    long time() default 3000;

    TimeUnit timeUnit() default TimeUnit.MICROSECONDS;
}
