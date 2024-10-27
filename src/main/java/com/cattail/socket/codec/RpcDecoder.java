package com.cattail.socket.codec;

import com.cattail.common.constants.MsgType;
import com.cattail.common.constants.ProtocolConstants;
import com.cattail.common.constants.RpcSerialization;
import com.cattail.socket.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/26
 * @Copyright: https://github.com/CatTailzz
 */
public class RpcDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {

        //如果读取到的小于请求头大小，直接返回
        if (in.readableBytes() < ProtocolConstants.HEADER_PRE_LEN) {
            return;
        }
        //记录当前读取位置，方便回退
        in.markReaderIndex();

        //读取魔数
        short magic = in.readShort();
        if (magic != ProtocolConstants.MAGIC) {
            throw new IllegalArgumentException("magic number is illegal, " + magic);
        }
        byte version = in.readByte();
        byte msgType = in.readByte();
        byte status = in.readByte();
        long requestId = in.readLong();
        // 对于serialization和data这种先用len指定长度的，都先读长度再判断是否够，够了再用数组接收，否则回退
        final int len = in.readInt();
        if (in.readableBytes() < len) {
            in.resetReaderIndex();
            return;
        }
        final byte[] bytes = new byte[len];
        in.readBytes(bytes);
        final String serialization = new String(bytes);
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        MsgType msgtypeEnum = MsgType.findByType(msgType);
        if (msgtypeEnum == null) {
            return;
        }

        // 构建消息头
        MsgHeader header = new MsgHeader();
        header.setMagic(magic);
        header.setVersion(version);
        header.setStatus(status);
        header.setRequestId(requestId);
        header.setMsgType(msgType);
        header.setSerialization(bytes);
        header.setSerializationLen(len);
        header.setMsgLen(dataLength);

        //构建body
        com.cattail.socket.serialization.RpcSerialization rpcSerialization = SerializationFactory.get(RpcSerialization.get(serialization));
        final String body = rpcSerialization.deserialize(data, String.class);

        //构建protocol
        RpcProtocol protocol = new RpcProtocol();
        protocol.setHeader(header);
        protocol.setBody(body);
        out.add(protocol);
    }
}
