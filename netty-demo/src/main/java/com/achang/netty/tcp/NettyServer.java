package com.achang.netty.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/******
 @author 阿昌
 @create 2021-12-05 19:49
 ******* netty服务器
 *  模拟tcp长连接
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        /*
        创建BoosGroup 和 WorkerGroup
        说明：
            1、创建两个线程组 BoosGroup 和 WorkerGroup
            2、 BoosGroup：只处理连接请求
                WorkerGroup： 处理和客户端业务处理
            3、两个现场组都是自旋
         */
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建服务器端启动的对象，配置启动参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            //使用链式编程进行配置设置
            bootstrap.group(boosGroup,workerGroup)//设置两个线程组
                     .channel(NioServerSocketChannel.class)//设置服务器使用哪个通道
                     .option(ChannelOption.SO_BACKLOG,128)//设置线程队列等待连接个数
                     .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持活动连接状态
                     .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道初始化对象
                         //给pipeline设置处理器
                         protected void initChannel(SocketChannel socketChannel) throws Exception {
                             socketChannel.pipeline().addLast(new NettyServerHandler());
                         }
                     });//设置WorkerGroup的EventLoop对应的管道设置处理器
            System.out.println("【服务端】准备完毕.......");
            //绑定端口并同步处理，生成ChannelFuture对象
            //启动服务器
            ChannelFuture cf = bootstrap.bind(6668).sync();
            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            //优雅关闭
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
