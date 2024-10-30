package com.cattail.socket.server;

import com.cattail.common.constants.MsgType;
import com.cattail.common.constants.RpcInvoker;
import com.cattail.filter.*;
import com.cattail.invoke.Invocation;
import com.cattail.invoke.Invoker;
import com.cattail.invoke.InvokerFactory;
import com.cattail.socket.codec.MsgHeader;
import com.cattail.socket.codec.RpcProtocol;
import com.cattail.socket.codec.RpcRequest;
import com.cattail.socket.codec.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/27
 * @Copyright: https://github.com/CatTailzz
 */
public class ServiceHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcRequest> rpcProtocol) throws Exception {
        final RpcRequest rpcRequest = rpcProtocol.getBody();
        final MsgHeader header = rpcProtocol.getHeader();

        final RpcResponse rpcResponse = new RpcResponse();
        final RpcProtocol<RpcResponse> resRpcProtocol = new RpcProtocol<>();

        header.setMsgType((byte) MsgType.RESPONSE.ordinal());
        resRpcProtocol.setHeader(header);
        final Invoker invoker = InvokerFactory.get(RpcInvoker.JDK);
        try {
            // 发送前
            FilterLoader.addAndHandelFilter(FilterFactory.getServiceBeforeFilters(), new FilterData<>(rpcRequest));
//            final List<Filter> serverBeforeFilters = FilterFactory.getServiceBeforeFilters();
//            if (!serverBeforeFilters.isEmpty()) {
//                final FilterData<RpcRequest> rpcRequestFilterData = new FilterData<>(rpcRequest);
//                final FilterLoader filterLoader = new FilterLoader();
//                filterLoader.addFilter(serverBeforeFilters);
//                final FilterResponse filterResponse = filterLoader.doFilter(rpcRequestFilterData);
//                if (!filterResponse.getResult()) {
//                    throw filterResponse.getException();
//                }
//            }
            // 执行
            final Object data = invoker.invoke(new Invocation(rpcRequest));
            rpcResponse.setData(data);
        } catch (Exception e) {
            rpcResponse.setException(e);
        } finally {
            // 发送后
            FilterLoader.addAndHandelFilter(FilterFactory.getServiceAfterFilters(), new FilterData<>(rpcResponse));
//            final List<Filter> serverAfterFilters = FilterFactory.getServiceAfterFilters();
//            if (!serverAfterFilters.isEmpty()) {
//                final FilterData<RpcResponse> rpcResponseFilterData = new FilterData<>(rpcResponse);
//                final FilterLoader filterLoader = new FilterLoader();
//                filterLoader.addFilter(serverAfterFilters);
//                final FilterResponse filterResponse = filterLoader.doFilter(rpcResponseFilterData);
//                if (!filterResponse.getResult()) {
//                    throw filterResponse.getException();
//                }
//            }
        }
        resRpcProtocol.setBody(rpcResponse);
        channelHandlerContext.writeAndFlush(resRpcProtocol);
    }
}
