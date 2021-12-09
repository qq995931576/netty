package com.achang.netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/******
 @author 阿昌
 @create 2021-12-09 20:40
 *******
 *  群聊系统服务端
 */
public class GroupChatServer {

    private final Integer port;//监听端口

    private GroupChatServer (Integer port){
        this.port=port;
    }

    public void run() throws InterruptedException {
        //创建两个线程组
        NioEventLoopGroup boosGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup(8);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //向pipeline加入处理器
                            pipeline.addLast("decoder",new StringDecoder());//解码器
                            //如果不加这个编码解码器的  无法直接传输字符串
                            pipeline.addLast("encoder",new StringEncoder());//编码器
                            pipeline.addLast("MyHandler",new GroupChatServerHandler());//自己的业务处理器
                        }
                    });
            System.out.println("netty服务器启动成功.....绑定端口："+port);
            ChannelFuture cf = serverBootstrap.bind(port).sync();
            cf.channel().closeFuture().sync();//监听关闭时间
        }finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }


    //主入口
    public static void main(String[] args) throws InterruptedException {
        GroupChatServer server = new GroupChatServer(7000);
        server.run();
    }

}
