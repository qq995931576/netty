package com.achang.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/******
 @author 阿昌
 @create 2021-12-09 22:17
 *******
 *  心跳检测服务端处理器
 *  因为不需要接受数据，所以就直接继承 ChannelInboundHandlerAdapter
 */
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {


    /**
     * 当触发心跳事件后，会触发该方法
     * @param ctx 上下文
     * @param evt 事件
     * @throws Exception 异常
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            //evt向下转型
            IdleStateEvent event = (IdleStateEvent)evt;

            //判断是什么时间
            switch (event.state()){
                case ALL_IDLE:
                    System.out.println("【读写空闲】事件");
                    break;
                case READER_IDLE:
                    System.out.println("【读空闲】事件");
                    break;
                case WRITER_IDLE:
                    System.out.println("【写空闲】事件");
            }
            System.out.println(ctx.channel().remoteAddress()+"---客户端空闲时间发生---事件为【"+event.state()+"】");

            //如果发生空闲事件后，就关闭channel，就会停止连接
//            ctx.channel().close();
        }
    }


}
