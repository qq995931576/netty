package com.achang.netty.InboundandoutboundHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/******
 @author 阿昌
 @create 2021-12-11 17:30
  *******
  *  解码器：字节--> long
 */
public class MyByteToLongDecoder2 extends ReplayingDecoder<Void> {
    /**
     * 解码方法
     *
     * @param channelHandlerContext 上下文
     * @param in                    入站的ByteBuf（客户端传来的数据）
     * @param out                   list集合，会将数据传递给下一个Handler处理
     * @throws Exception decode()方法，会根据接收的数据，被调用多次，直到确认没有新的元素被添加到list中，或者ByteBuf没有更多的可读字节为止
     *                   如果list out 不为空，就会将list的内容传递给下一个channelinboundhandler处理，该处理器的方法会被调用多次
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        //ReplayingDecoder不需要判断是否足够，他会内部进行判断
        out.add(in.readLong());
    }
}
