package com.achang.netty.groupchat;

import com.achang.netty.tcp.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/******
 @author 阿昌
 @create 2021-12-09 21:20
 *******
 *  群聊系统客户端
 */
public class GroupChatClient {
    private final String ipaddr;
    private final int port;

    private GroupChatClient(String ipaddr,int port) throws InterruptedException {
        this.ipaddr = ipaddr;
        this.port = port;
    }

    private void run() throws InterruptedException {
        NioEventLoopGroup clientGroup = new NioEventLoopGroup();

        try {
            Bootstrap clientBootstrap = new Bootstrap();
            clientBootstrap.group(clientGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //向pipeline加入处理器
                            pipeline.addLast("decoder",new StringDecoder());//解码器
                            //如果不加这个编码解码器的  无法直接传输字符串
                            pipeline.addLast("encoder",new StringEncoder());//编码器
                            pipeline.addLast("MyHandler",new GroupChatClientHandler());//加入自己的处理器
                        }
                    });

            //监听连接是否成功
            ChannelFuture cf = clientBootstrap.connect(ipaddr, port);
            cf.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()){
                        System.out.println("连接成功....");
                    }else {
                        System.out.println("连接失败");
                    }
                }
            });

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()){
                String msg = scanner.nextLine();
                cf.channel().writeAndFlush(msg+"\n");//发送消息
            }
            //对关闭通道进行监听
            cf.channel().close().sync();
        }finally {
            clientGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        GroupChatClient client = new GroupChatClient("127.0.0.1", 7000);
        client.run();
    }

}
