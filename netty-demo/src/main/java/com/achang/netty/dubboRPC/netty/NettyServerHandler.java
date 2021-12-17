package com.achang.netty.dubboRPC.netty;

import com.achang.netty.dubboRPC.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/******
 @author 阿昌
 @create 2021-12-17 21:13
 *******
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送来的消息，并调用服务
        System.out.println("msg="+msg);

        //客户端想要调用服务器的api时，想要满足一定协议的要求才能调用
        //比如，我们这里要求，每次发送消息时，都必须要求以"HelloService#hello开头"
        if (msg.toString().startsWith("HelloService#hello")){
            String result = new HelloServiceImpl().hello(msg.toString().split("HelloService#hello")[1]);
            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
