package com.achang.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/******
 @author 阿昌
 @create 2021-12-09 22:00
 *******
 *  netty心跳检查服务端案例
 */
public class MyHeartbeatServer {

    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();


        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))//netty自带的日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            /*
                            加入netty提供的IdleStateHandler
                            IdleStateHandler；空闲状态处理器
                            构造参数↓
                            long readerIdleTime : 表示多长时间没读操作了，服务端没有接受到客户端的读操作；(触发后，服务端就会发送心跳检测包给客户端，检测是否还是连接状态)
                            long writerIdleTime : 表示有多长时间没有写操作了，服务端没有接收到客户端的写操作
                            long allIdleTime : 表示多长时间既没有读操作也没有写操作。
                            TimeUnit unit : 时间单位

                             */
                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                            /*
                            加入一个对空闲状态检测进一步处理的handler
                            时间到了就会触发对应的事件(IdleStateEvent)，我们再写一个自定义的handler来监听这个时间就可以做出对应的操作
                            当事件触发之后，就会传递给对应的channel管道的下一个handler去处理
                            通过调用(触发)下一个handler的 userEventTiggerd()方法，该方法去处理
                             */
                            pipeline.addLast(new HeartbeatHandler());//紧挨着上面的IdleStateHandler，作为下一个handler，这样子就可以触发到userEventTiggerd方法
                        }
                    });
            System.out.println("服务端绑定端口7000.....启动成功");

            //启动服务端
            ChannelFuture cf = serverBootstrap.bind(7000).sync();
            cf.channel().closeFuture().sync();
        }finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
