package com.imooc;

import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

    private static final String SERVER_HOST = "127.0.0.1";

    public static void main(String args[]) {
        new Client().start(Constant.BEGIN_PORT,Constant.N_PORT);
    }

    public void start(final int beginPort,int nPort) {
        System.out.println("client starting....");

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        final Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                    }
                });

        int index = 0;
        int port;
        while (!Thread.interrupted()) {
            port = beginPort + index;
            try {
                ChannelFuture channelFuture = bootstrap.connect(SERVER_HOST, port);
                channelFuture.addListener((ChannelFutureListener) future -> {
                    if(!future.isSuccess()) {
                        System.out.println("connect failed,exit!");
                        System.exit(0);
                    }
                });
                channelFuture.get();
            } catch (Exception e) {

            }
            if(++index == nPort) {
                index = 0;
            }
        }
    }
}
