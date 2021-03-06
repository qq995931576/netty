package com.achang.netty.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/******
 @author 阿昌
 @create 2021-12-06 22:07
 *******
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("myHttpServerCodec",new HttpServerCodec());
        pipeline.addLast(new HttpServerHandler());
        System.out.println("1");
    }
}
