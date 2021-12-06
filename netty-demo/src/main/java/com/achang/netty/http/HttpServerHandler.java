package com.achang.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/******
 @author 阿昌
 @create 2021-12-06 22:05
 *******
 *  SimpleChannelInboundHandler：他就是ChannelInboundHandlerAdapter的子类
 *  HttpObject：表示客户端和服务器端相互通讯的数据被封装成HttpObject类型
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    //当有读取事件就会触发该事件，读取客户端数据
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest){

            System.out.println("pipeline hashCode: "+channelHandlerContext.pipeline().hashCode());
            System.out.println("msg:"+msg.getClass());
            System.out.println("客户端浏览器地址："+channelHandlerContext.channel().remoteAddress());

            //自定义不做响应uri
            String uri = ((HttpRequest) msg).uri();
           if ("/favicon.ico".equals(uri)){
               System.out.println("对/favicon.ico特定资源进行不做响应");
               return;
           }

            //回复信息给客户端浏览器[http协议]
            ByteBuf content = Unpooled.copiedBuffer("hello,我是阿昌的服务器", CharsetUtil.UTF_8);
            //构造http响应，即HttpResponse响应
            DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=utf-8");
            httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

            //讲构建好的HttpResponse返回
            channelHandlerContext.writeAndFlush(httpResponse);
        }
    }
}
