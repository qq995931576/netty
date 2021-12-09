package com.achang.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

/******
 @author 阿昌
 @create 2021-12-09 21:31
 *******
 *  聊天室客户端处理器
 */
public class GroupChatClientHandler extends SimpleChannelInboundHandler<String> {

   //读取服务端发来的消息【读写消息事件】
   protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
       Channel channel = ctx.channel();
       System.out.println("[用户_"+channel.remoteAddress()+"]发送的消息为："+msg);
    }

}
