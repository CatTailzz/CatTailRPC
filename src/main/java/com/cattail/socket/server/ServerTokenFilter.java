package com.cattail.socket.server;

import com.cattail.filter.FilterData;
import com.cattail.filter.FilterResponse;
import com.cattail.filter.server.ServerBeforeFilter;
import com.cattail.socket.codec.RpcRequest;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/31
 * @Copyright: https://github.com/CatTailzz
 */
public class ServerTokenFilter implements ServerBeforeFilter {
    @Override
    public FilterResponse doFilter(FilterData<RpcRequest> filterData) {
        final RpcRequest rpcRequest = filterData.getObject();
        Object value = rpcRequest.getClientAttachments().get("token");
        System.out.println(value);
        if (!value.equals("TOKEN")) {
            return new FilterResponse(false, new Exception("token wrong" + value));
        }
        return new FilterResponse(true, null);
    }
}
