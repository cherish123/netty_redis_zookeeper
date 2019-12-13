### com.crazymakercircle.iodem
    四种通道：FileChannel :fileDemos;
            DatagramChannel:udpDemos;
            ServerSocketChannel和SocketChannel：NioDiscard和socketDemos
#### 文件复制
     FileNIOCopyDemo：着重演示FileChannel文件通道以及字符缓冲区的使用；
     FileNIOFastCopyDemo：调用文件通道的transferFrom方法实现文件的高效复制。
#### 粘包和拆包
     com.crazymakercircle.iodemo.NioDiscard：在Server端打一个断点，会出现粘包现象
     com.crazymakercircle.iodemo.socketDemos：在客户端socketChannel.write(buffer)打断点
     会出现，只接收一次，无断点，则粘包
     解决方法：1）自定义解码器分包器；2）使用Netty内置的解码器