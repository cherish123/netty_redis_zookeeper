package com.crazymakercircle.netty.decoder.cus;

import com.crazymakercircle.util.Logger;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class StringProcessHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String content = String.valueOf(msg) ;
        Logger.info("打印出一个字符串为 : " +content);
    }

}
