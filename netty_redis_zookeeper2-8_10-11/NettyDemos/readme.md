#### basic
     运行NettyDiscardServer。
     如果想要看到最终的丢弃效果，不能仅仅启动服务器，还需要启动客户端com.crazymakercircle.ReactorModel.EchoClient.（端口一致）
     这里的客户端只要能发送消息到服务端即可。虽然EchoClient是用Java NIO，NettyDiscardServer是netty，但是底层也是使用NIO。