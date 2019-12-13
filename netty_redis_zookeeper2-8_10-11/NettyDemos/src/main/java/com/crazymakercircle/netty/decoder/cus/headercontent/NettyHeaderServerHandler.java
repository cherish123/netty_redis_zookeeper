package com.crazymakercircle.netty.decoder.cus.headercontent;

import com.crazymakercircle.util.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class NettyHeaderServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String content = (String) msg;
        Logger.info("server received: " +content);

        //写回数据，异步任务
        byte[] bytes = content.getBytes(Charset.forName("utf-8"));
        //为了使用Header-Content协议
        ByteBuf buffer = ctx.channel().alloc().buffer();
        buffer.writeInt(bytes.length);

        buffer.writeBytes(bytes);
        ctx.writeAndFlush(buffer);
    }
}
