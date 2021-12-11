package com.achang.netty.InboundandoutboundHandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/******
 @author 阿昌
 @create 2021-12-11 18:18
 *******
 */
public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Long aLong) throws Exception {
        System.out.println("客户端收到服务端发来的消息:"+aLong);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler.channelActive发送数据");
        ctx.writeAndFlush(123456);//发送long数据
    }
}
