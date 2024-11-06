package com.cattail.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/11/6
 * @Copyright: https://github.com/CatTailzz
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(ConsumerConfig.class)
public @interface EnableProviderRpc {
}
