package com.cattail.router;

import com.cattail.common.URL;

import java.util.List;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public interface LoadBalancer {

    URL select(List<URL> urls);
}
