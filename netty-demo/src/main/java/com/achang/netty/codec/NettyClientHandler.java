package com.achang.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/******
 @author 阿昌
 @create 2021-12-05 21:11
 ******* 客户端处理器，自定义handler需要继承netty规定好的某个HandlerAdapter适配器才能生效
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    //通道就绪就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //发送一个Student 对象到服务端
        StudentPOJO.Student student = StudentPOJO.Student.newBuilder().setId(1).setName("阿昌").build();
        ctx.writeAndFlush(student);
        System.out.println("给服务端发送student数据");
    }

    //接受服务端返回的消息，当通道有读取事件时就触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        System.out.println("服务端回复的消息："+buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务端地址："+ctx.channel().remoteAddress());
    }

    //异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭通道
        cause.printStackTrace();
        ctx.channel().close();
    }
}
