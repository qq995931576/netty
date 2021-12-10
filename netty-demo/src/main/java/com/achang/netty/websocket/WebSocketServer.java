package com.achang.netty.websocket;

import com.achang.netty.heartbeat.HeartbeatHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/******
 @author 阿昌
 @create 2021-12-10 19:52
 *******
 *  Websocket双工TCP长连接---服务端
 */
public class WebSocketServer {

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
                            //基于http协议，使用http的编码和解码器
                            pipeline.addLast(new HttpServerCodec());
                            //是以 块方式 写，添加ChunkedWriteHandler处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            /*
                            说明：
                                1、因为http的数据在传输过程中是【分段】的，HttpObjectAggregator可以将多个分段聚合起来
                                2、这就是当浏览器发送大量数据时，就会发送多次http请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /*
                            说明：
                                1、对于websocket是通过【帧frame】的形式传递的（数据链路层的传输单元是帧）
                                2、可以看到WebSocketFrame 下面有6个子类
                                3、浏览器发送文件、请求时，ws://localhost:7000/achang 表示请求的uri
                                4、WebSocketServerProtocolHandler核心功能：将http协议升级为ws协议（websocket长连接协议）
                                5、为什么http能够升级为ws协议呢？是通过状态码101
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/achang"));
                            //自定义处理业务逻辑的处理器
                            pipeline.addLast(new WebSocketServerFrameHandler());
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
