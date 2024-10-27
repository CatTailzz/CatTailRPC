package com.cattail.socket.server;

import com.cattail.socket.codec.RpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/26
 * @Copyright: https://github.com/CatTailzz
 */
//自动处理RpcProtocol类型的数据，这里只是简单打印，正常会是注册、激活和事件触发等
public class SimpleChatServerHandler extends SimpleChannelInboundHandler<RpcProtocol> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol rpcProtocol) throws Exception {
        System.out.println(rpcProtocol.getBody());
    }
}
