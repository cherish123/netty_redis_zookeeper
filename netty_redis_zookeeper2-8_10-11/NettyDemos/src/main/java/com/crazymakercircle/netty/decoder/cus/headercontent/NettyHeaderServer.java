package com.crazymakercircle.netty.decoder.cus.headercontent;

import com.crazymakercircle.netty.NettyDemoConfig;
import com.crazymakercircle.netty.echoServer.NettyEchoServerHandler;
import com.crazymakercircle.util.Logger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyHeaderServer {

    private final int serverPort;
    ServerBootstrap serverBootstrap = new ServerBootstrap();

    public NettyHeaderServer(int serverPort) {
        this.serverPort = serverPort;
    }

    public void runServer(){
        EventLoopGroup bossLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup();

        serverBootstrap.group(bossLoopGroup,workerLoopGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(serverPort)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.ALLOCATOR,PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new HeaderContentDecoder())
                        .addLast(new NettyHeaderServerHandler());
                    }
                });

        try {
            ChannelFuture channelFuture = serverBootstrap.bind().sync();

            Logger.info(" 服务器启动成功，监听端口: " +
                    channelFuture.channel().localAddress());

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerLoopGroup.shutdownGracefully();
            bossLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String args[]) {
        int port = NettyDemoConfig.SOCKET_SERVER_PORT;
        new NettyHeaderServer(port).runServer();
    }
}
