package com.crazymakercircle.netty.decoder.cus.headercontent;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

//@ChannelHandler.Sharable
public class HeaderContentDecoder extends ByteToMessageDecoder {

//    public static final HeaderContentDecoder INSTANCE = new HeaderContentDecoder();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        //可读大小小于int，头还没读满，return
        if (in.readableBytes() < 4) {
            return;
        }

        //头已经完整，读取长度并设置回滚点为header的readIndex位置
        in.markReaderIndex();
        int length = in.readInt();

        //剩余长度不够body体，说明本次入站的数据帧不完整，reset读指针，
        // 每次数据入站都是从头部开始读
        if (in.readableBytes()<length) {
            in.resetReaderIndex();
            return;
        }

        byte[] inBytes = new byte[length];
        in.readBytes(inBytes,0,length);
        out.add(new String(inBytes,"UTF-8"));
    }
}
