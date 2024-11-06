package com.cattail.socket.client;

import com.cattail.filter.FilterData;
import com.cattail.filter.FilterResponse;
import com.cattail.filter.client.ClientBeforeFilter;
import com.cattail.socket.codec.RpcRequest;


/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/31
 * @Copyright: https://github.com/CatTailzz
 */
public class ClientTokenFilter implements ClientBeforeFilter {
    @Override
    public FilterResponse doFilter(FilterData<RpcRequest> filterData) {
        final RpcRequest rpcRequest = filterData.getObject();
        rpcRequest.getClientAttachments().put("token", "TOKEN");
        return new FilterResponse(true, null);
    }
}
