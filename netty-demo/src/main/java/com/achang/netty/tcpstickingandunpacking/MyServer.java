package com.achang.netty.tcpstickingandunpacking;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/******
 @author 阿昌
 @create 2021-12-11 17:05
 *******
 *  自定义编码解码器&协议包解决tcp拆包粘包问题---服务端
 */
public class MyServer {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup eventGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup,eventGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MyServerInitializer());//自定义初始化类
            ChannelFuture cf = serverBootstrap.bind(7000).sync();
            System.out.println("服务端启动成功，绑定端口7000");
            cf.channel().closeFuture().sync();
        }finally {
            boosGroup.shutdownGracefully();
            eventGroup.shutdownGracefully();
        }

    }
}
