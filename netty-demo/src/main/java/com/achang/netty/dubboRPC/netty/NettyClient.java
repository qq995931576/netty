package com.achang.netty.dubboRPC.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.omg.SendingContext.RunTime;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/******
 @author 阿昌
 @create 2021-12-17 21:32
 ******* Netty客户端
 */
public class NettyClient {
    //创建线程池
    private static ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static NettyClientHandler nettyClientHandler;

    /**
     * 编写方式使用代理模式，获取一个代理对象
     * @param serviceClass service类
     * @param providerName 协议头
     * @return 代理对象
     */
    public Object getBean(final Class<?> serviceClass,final String providerName){
       return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
               new Class<?>[]{serviceClass},
               ((proxy, method, args) -> {
                   //客户端每调用一次就会进入该代码块
                    //第一次调用
                   if (nettyClientHandler==null){
                       startClient0("127.0.0.1",7000);
                   }

                   //设置要发送给服务器的信息
                   //providerName协议头，args传入的参数
                   nettyClientHandler.setParam(providerName+args[0]);
                   return executors.submit(nettyClientHandler).get();
                }
       ));
    }

    //初始化客户端
    private static void startClient0(String ipaddr,Integer port){
        nettyClientHandler = new NettyClientHandler();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            Bootstrap clientBootstrap = bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(nettyClientHandler);
                        }
                    });
            clientBootstrap.connect(ipaddr,port).sync();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
