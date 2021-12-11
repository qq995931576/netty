package com.achang.netty.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/******
 @author 阿昌
 @create 2021-12-05 20:47
 ******* 服务端处理器，自定义handler需要继承netty规定好的某个HandlerAdapter适配器才能生效
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<StudentPOJO.Student> {

    /*
    读取数据事件（这里可以读取客户端发送来的消息）
    1、ChannelHandlerContext ctx：上下文对象。含有管道pipeline、通道channel、地址等
    2、Object msg：客户端发送来的数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, StudentPOJO.Student student) throws Exception {
        //读取从客户端发送的StudentPOJO.Student
        System.out.println("客户端发送的数据，id："+student.getId());
        System.out.println("客户端发送的数据，name："+student.getName());
    }


    //数据读取完毕事件
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        /*
        1、writeAndFlush是 write+Flush方法的合并
        2、将数据写入缓存并刷新
        3、对发送的数据进行编码
        */
        ctx.writeAndFlush(Unpooled.copiedBuffer("我是服务端~",CharsetUtil.UTF_8));
    }

    //发送异常事件，一般是关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
