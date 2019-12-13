package com.crazymakercircle.netty.decoder.cus;

import com.crazymakercircle.netty.decoder.IntegerAddDecoder;
import com.crazymakercircle.util.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class Byte2StringDecoder extends ReplayingDecoder<Byte2StringDecoder.Status> {

    enum Status {
        PARSE_1, PARSE_2
    }

    private char first;
    private char second;

    public Byte2StringDecoder() {
        //构造函数中，需要初始化父类的state 属性，表示当前阶段
        super(Byte2StringDecoder.Status.PARSE_1);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()) {
            case PARSE_1:
                //从装饰器ByteBuf 中读取数据
                first = in.readChar();
                //第一步解析成功，
                // 进入第二步，并且设置“读指针断点”为当前的读取位置
                checkpoint(Byte2StringDecoder.Status.PARSE_2);
                break;
            case PARSE_2:
                second = in.readChar();
                String ct = String.valueOf(new char[]{first,second});
                out.add(ct);
                checkpoint(Byte2StringDecoder.Status.PARSE_1);
                break;
            default:
                break;
        }
    }
}
