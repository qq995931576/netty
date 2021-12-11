package com.achang.netty.InboundandoutboundHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/******
 @author 阿昌
 @create 2021-12-11 17:35
 *******
 *  客户端发来long类型的数据处理器
 */
public class MyServerHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Long msg) throws Exception {
        System.out.println("客户端发来的long数据："+msg);
        //给客户端回送消息
        channelHandlerContext.writeAndFlush(98765L);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
