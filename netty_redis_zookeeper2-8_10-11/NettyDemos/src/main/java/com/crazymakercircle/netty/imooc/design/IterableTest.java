package com.crazymakercircle.netty.imooc.design;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 迭代器模式
 * 1.迭代器接口
 * 2.对容器里面各个对象进行访问
 */
public class IterableTest {

    public static void main(String args[]) {
        ByteBuf header = Unpooled.wrappedBuffer(new byte[]{1, 2, 3});
        ByteBuf body = Unpooled.wrappedBuffer(new byte[]{4, 5, 6});

        ByteBuf merge = merge(header, body);
        merge.forEachByte(value -> {
            System.out.println(value);
            return true;
        });
    }

    public static ByteBuf merge(ByteBuf header,ByteBuf body) {
//        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
//        byteBuf.writeBytes(header);
//        byteBuf.writeBytes(body);

        /**
         * 这种方式实现零拷贝
         */
        CompositeByteBuf byteBuf = ByteBufAllocator.DEFAULT.compositeBuffer();
        byteBuf.addComponent(true,header);
        byteBuf.addComponent(true,body);
        return byteBuf;
    }

}
