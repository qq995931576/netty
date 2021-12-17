package com.achang.netty.dubboRPC.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/******
 @author 阿昌
 @create 2021-12-17 21:22
 *******
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {
    private ChannelHandlerContext channelHandlerContext;//上下文
    private String result;//调用的返回结果
    private String param;//客户端调用方法时的参数

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       cause.printStackTrace();
        ctx.close();
    }

    //收到服务器的数据后就会被调用
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead");
        result = msg.toString();
        notify();//唤醒等待的线程
    }

    //与服务器连接成功后就会被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
        channelHandlerContext = ctx;
    }

    //被代理对象调用，异步发送数据给服务器，然后阻塞，会等待被唤醒
    @Override
    public synchronized Object call() throws Exception {
        System.out.println("call1");
        channelHandlerContext.writeAndFlush(param);
        //进行wait阻塞
        wait();
        System.out.println("call2");

        return result;
    }


    //设置发送的数据
    void setParam(String msg){
        System.out.println("setParam");
        this.param = msg;
    }
}
