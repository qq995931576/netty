package com.achang.netty.codec2;

import com.achang.netty.codec.StudentPOJO;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/******
 @author 阿昌
 @create 2021-12-05 20:47
 ******* 服务端处理器，自定义handler需要继承netty规定好的某个HandlerAdapter适配器才能生效
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    /*
    读取数据事件（这里可以读取客户端发送来的消息）
    1、ChannelHandlerContext ctx：上下文对象。含有管道pipeline、通道channel、地址等
    2、Object msg：客户端发送来的数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyDataInfo.MyMessage myMessage) throws Exception {
        //根据不同的myMessage的dataType 来显示不同的信息
        MyDataInfo.MyMessage.DataType dataType = myMessage.getDataType();
        if (dataType==MyDataInfo.MyMessage.DataType.StudentType){
            MyDataInfo.Student student = myMessage.getStudent();
            System.out.println("收到客户端发来的消息student："+student);
        }else if (dataType== MyDataInfo.MyMessage.DataType.WorkerType){
            MyDataInfo.Worker worker = myMessage.getWorker();
            System.out.println("收到客户端发来的消息worker："+worker);
        }else {
            System.out.println("客户端发来的消息类型不正确");
        }

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
