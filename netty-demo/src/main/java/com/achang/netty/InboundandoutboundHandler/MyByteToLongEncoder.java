package com.achang.netty.InboundandoutboundHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/******
 @author 阿昌
 @create 2021-12-11 17:41
 *******
 *  编码器：long ---> 字节
 */
public class MyByteToLongEncoder extends MessageToByteEncoder<Long> {
    /**
     * 编码方法
     * @param channelHandlerContext 上下文
     * @param msg 数据
     * @param out ByteBuf集合
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Long msg, ByteBuf out) throws Exception {
        System.out.println("MyByteToLongEncoder.encode被调用");
        System.out.println("msg"+msg);
        out.writeLong(msg);//发送数据
    }
}
