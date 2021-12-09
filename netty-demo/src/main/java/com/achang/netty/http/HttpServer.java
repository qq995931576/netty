package com.achang.netty.http;

import com.sun.corba.se.internal.CosNaming.BootstrapServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/******
 @author 阿昌
 @create 2021-12-06 22:05
 *******
 *  模拟http请求连接
 */
public class HttpServer {
    public static void main(String[] args) throws Exception {

        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(workGroup,boosGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerInitializer());
            System.out.println("服务器启动成功....");

            ChannelFuture channelFuture = bootstrap.bind(7001).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            workGroup.shutdownGracefully();
            boosGroup.shutdownGracefully();
        }

    }
}
