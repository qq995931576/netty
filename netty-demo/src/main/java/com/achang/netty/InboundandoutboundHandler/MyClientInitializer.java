package com.achang.netty.InboundandoutboundHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/******
 @author 阿昌
 @create 2021-12-11 17:39
 *******
 *  自定义客户端初始化器
 */
public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new MyClientHandler());
        //编码器
        pipeline.addLast(new MyByteToLongEncoder());
        //解码器
//        pipeline.addLast(new MyByteToLongDecoder());
        pipeline.addLast(new MyByteToLongDecoder2());
    }
}
