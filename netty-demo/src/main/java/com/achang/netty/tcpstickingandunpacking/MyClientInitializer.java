package com.achang.netty.tcpstickingandunpacking;

import com.achang.netty.InboundandoutboundHandler.MyByteToLongDecoder;
import com.achang.netty.InboundandoutboundHandler.MyByteToLongDecoder2;
import com.achang.netty.InboundandoutboundHandler.MyByteToLongEncoder;
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
        //编码器
        pipeline.addLast(new MyMessageEncoder());
        //解码器
        pipeline.addLast(new MyMessageDecoder());
        pipeline.addLast(new MyClientHandler());
    }
}
