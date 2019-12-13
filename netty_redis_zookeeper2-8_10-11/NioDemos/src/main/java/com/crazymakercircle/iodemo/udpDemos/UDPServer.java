package com.crazymakercircle.iodemo.udpDemos;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.Print;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class UDPServer {

    public void receive() throws IOException {
        //操作一：获取DatagramChannel数据报通道
        DatagramChannel datagramChannel = DatagramChannel.open();
        //设置为非阻塞模式
        datagramChannel.configureBlocking(false);
        //绑定监听地址
        datagramChannel.bind(new InetSocketAddress(
                NioDemoConfig.SOCKET_SERVER_IP
                , NioDemoConfig.SOCKET_SERVER_PORT));
        Print.tcfo("UDP 服务器启动成功！");
        //开启一个通道选择器
        Selector selector = Selector.open();
        //查看DatagramChannel支持哪些IO事件
        Print.tcfo("DatagramChannel:"+datagramChannel.validOps());
        //将通道datagramChannel注册到选择器（selector）,并注册感兴趣的IO事件OP_READ
        datagramChannel.register(selector, SelectionKey.OP_READ);


        //通过选择器，查询IO事件
        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            ByteBuffer buffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);

            //迭代IO事件
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                //可读事件，有数据到来
                if (selectionKey.isReadable()) {
                    //操作二：读取DatagramChannel数据报通道数据
                    SocketAddress client = datagramChannel.receive(buffer);
                    buffer.flip();
                    Print.tcfo(new String(buffer.array(), 0, buffer.limit()));
                    buffer.clear();
                }
            }
            iterator.remove();
        }

        selector.close();
        datagramChannel.close();
    }

    public static void main(String[] args) throws IOException {
        new UDPServer().receive();
    }
}
