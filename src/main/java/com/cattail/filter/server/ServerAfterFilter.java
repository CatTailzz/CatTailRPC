package com.cattail.filter.server;

import com.cattail.filter.Filter;
import com.cattail.socket.codec.RpcResponse;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/30
 * @Copyright: https://github.com/CatTailzz
 */
public interface ServerAfterFilter extends Filter<RpcResponse> {
}
