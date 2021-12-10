package com.achang.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/******
 @author 阿昌
 @create 2021-12-10 23:20
 *******
  * 自定义处理业务逻辑的处理器
  * 对于websocket是通过【帧frame】的形式传递的（数据链路层的传输单元是帧）
 *  TextWebSocketFrame：表示一个文本帧，就是传输过程中的数据
 */
public class WebSocketServerFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务端收到消息："+msg.text());
        //回复浏览器
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间："+ LocalDateTime.now()+""+msg.text()));
    }

    //客户端断开连接时会触发该事件
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //id.asLongText表示获取这channel唯一的值
        System.out.println("handlerRemoved被调用："+ctx.channel().id().asLongText());
    }

    //客户端连接时会触发该事件
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //id.asLongText表示获取这channel唯一的值
        System.out.println("handlerAdded被调用："+ctx.channel().id().asLongText());
        //id.asShortText表示获取这channel的值，这个不是唯一的。有可能重复
        System.out.println("handlerAdded被调用："+ctx.channel().id().asShortText());
    }

    //当客户端发生异常时会触发该事件
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生："+cause.getMessage());
        ctx.channel().close();
    }
}
