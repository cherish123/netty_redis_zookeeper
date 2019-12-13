package com.crazymakercircle.netty.decoder;

import com.crazymakercircle.util.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class StringHeaderDecoder extends ByteToMessageDecoder {    //头是一个整数
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext,
                          ByteBuf buf,
                          List<Object> out) throws Exception {

        //可读大小小于int，头还没读满，return

        if (buf.readableBytes() < 4) {
            return;
        }

        //头已经完整
        //在真正开始从buffer读取数据之前，调用markReaderIndex()设置回滚点
        // 回滚点为 header的readIndex位置
//        Logger.info("读取消息头之前的指针位置: "+buf.readerIndex());
        buf.markReaderIndex();
        int length = buf.readInt();
        Logger.info("消息头的大小: "+length);
        Logger.info("读取消息头之后的指针位置: "+buf.readerIndex());
        //从buffer中读出头的大小，这会使得readIndex前移
        //剩余长度不够body体，reset 读指针
        if (buf.readableBytes() < length) {
            //读指针回滚到header的readIndex位置处，没进行状态的保存
            buf.resetReaderIndex();
            Logger.info("重置之后的指针位置: "+buf.readerIndex());
            return;
        }
        // 读取数据，编码成字符串
        byte[] inBytes = new byte[length];
        buf.readBytes(inBytes, 0, length);
        out.add(new String(inBytes, "UTF-8"));
    }
}