package com.cattail.socket.client;

import com.cattail.common.RpcFuture;
import com.cattail.common.RpcRequestHolder;
import com.cattail.socket.codec.RpcProtocol;
import com.cattail.socket.codec.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/27
 * @Copyright: https://github.com/CatTailzz
 */
public class ClientHanlder extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> rpcResponseRpcProtocol) throws Exception {
        long requestId = rpcResponseRpcProtocol.getHeader().getRequestId();
        RpcFuture<RpcResponse> future = RpcRequestHolder.REQUEST_MAP.remove(requestId);
        future.getPromise().setSuccess(rpcResponseRpcProtocol.getBody());
    }
}
