package com.crazymakercircle.netty.decoder.cus.headercontent;

import com.crazymakercircle.netty.NettyDemoConfig;
import com.crazymakercircle.netty.echoServer.NettyEchoClientHandler;
import com.crazymakercircle.util.Logger;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;

public class NettyHeaderClient {

    private int serverPort;
    private String serverIp;
    Bootstrap bootstrap = new Bootstrap();

    public NettyHeaderClient(int serverPort,String serverIp) {
        this.serverPort = serverPort;
        this.serverIp = serverIp;
    }

    public void runClient() {
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup();

        try {
            bootstrap.group(workerLoopGroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(serverIp,serverPort)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new HeaderContentDecoder())
                                    .addLast(new NettyHeaderClientHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect();
            channelFuture.addListener((ChannelFuture futureListener) -> {
                if (futureListener.isSuccess()) {
                    Logger.info("NettyHeaderClient客户端连接成功!");
                } else {
                    Logger.info("NettyHeaderClient客户端连接失败!");
                }
            });

            // 阻塞,直到连接完成
            channelFuture.sync();
            Channel channel = channelFuture.channel();

            //发送大量的文字,看是否会拆包
            byte[] bytes = "疯狂创客圈：高性能学习社群!".getBytes(Charset.forName("utf-8"));
            for (int i = 0; i < 1000; i++) {
                //发送ByteBuf
                ByteBuf buffer = channel.alloc().buffer();
                //为了使用Header-Content协议
                buffer.writeInt(bytes.length);

                buffer.writeBytes(bytes);
                channel.writeAndFlush(buffer);
            }

            ChannelFuture closeFuture =channel.closeFuture();
            closeFuture.sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String args[]) {
        int port = NettyDemoConfig.SOCKET_SERVER_PORT;
        String ip = NettyDemoConfig.SOCKET_SERVER_IP;
        new NettyHeaderClient(port,ip).runClient();
    }
}
