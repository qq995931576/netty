package com.achang.netty.tcp;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/******
 @author 阿昌
 @create 2021-12-05 20:47
 ******* 服务端处理器，自定义handler需要继承netty规定好的某个HandlerAdapter适配器才能生效
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /*
    读取数据事件（这里可以读取客户端发送来的消息）
    1、ChannelHandlerContext ctx：上下文对象。含有管道pipeline、通道channel、地址等
    2、Object msg：客户端发送来的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //用户程序自定义的普通任务，taskQueue自定义异步任务
        ctx.channel().eventLoop().execute(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("阿昌来也1!!!");
            }
        });

        //用户自定义定时任务--->该任务提交到scheduledTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("阿昌来也2!!!");
            }
        },2, TimeUnit.SECONDS);//两秒后执行



//        System.out.println("server ctx:"+ctx);
//
//        //将msg转为ByteBuffer（这个ByteBuf和nio的ByteBuffer是有区别的）
//        ByteBuf buf=(ByteBuf)msg;
//
//        System.out.println("客户端发送消息是："+buf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址为："+ctx.channel().remoteAddress());
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
