package com.achang.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/******
 @author 阿昌
 @create 2021-12-09 20:52
 *******
 *  自定义服务器处理器
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    //定义管理每个客户端的channel组
    //GlobalEventExecutor.INSTANCE 全局的事件执行器，他是单例的
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //当连接建立会第一个执行该方法，【客户端连接事件】
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //当客户端连接，第一时间将每个客户端的channel加入到channelGroup统一管理
        Channel channel = ctx.channel();
        //给当前channelGroup管理的所有channel的客户端都发送消息
        channelGroup.writeAndFlush(sdf.format(new Date()) + "[客户端]" + channel.remoteAddress() + "加入聊天室...");
        channelGroup.add(channel);
    }

    //当某个channel处于活动状态，就会触发，用于发送某某上线【客户端上线活动状态事件】
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(sdf.format(new Date()) + "[客户端上线]:" + channel.remoteAddress() + "");//打印给服务端看
    }

    //当某个channel离开状态，就会触发，用于发送某某下线【客户端离开非活动状态事件】
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channelGroup.writeAndFlush(sdf.format(new Date()) + "[客户端]" + ctx.channel().remoteAddress() + "离线了...");
    }


    //当某个channel断开连接状态，就会触发【客户端离线断开连接事件】
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //告诉当前所有在线的用户，某某某断开连接
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush(sdf.format(new Date()) + "[客户端]" + channel.remoteAddress() + "离开聊天室...");
        System.out.println("当前channel组的个数为：" + channelGroup.size());
    }

    //客户端发送消息会触发【读取客户端数据事件】
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //获取当前发送消息的用户
        Channel nowSendChannel = ctx.channel();

        //排除发送者自己，不给他转发消息
        for (Channel channel : channelGroup) {
            if (!channel.equals(nowSendChannel)) {
                channel.writeAndFlush(sdf.format(new Date()) + "[客户_" + channel.remoteAddress() + "]发言：" + msg + "");//别的客户端
            } else {
                channel.writeAndFlush(sdf.format(new Date()) + "[您自己发送的消息为]：" + msg + "");//发送者自己，回显消息给自己看
            }
        }
    }

    //当发生异常会触发【异常触发事件】
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        channel.close();//关闭通道
    }
}
