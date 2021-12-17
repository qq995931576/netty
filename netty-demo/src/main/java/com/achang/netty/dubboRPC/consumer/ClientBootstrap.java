package com.achang.netty.dubboRPC.consumer;

import com.achang.netty.dubboRPC.netty.NettyClient;
import com.achang.netty.dubboRPC.publicinterface.HelloService;

/******
 @author 阿昌
 @create 2021-12-17 21:51
 *******
 */
public class ClientBootstrap {

    //定义协议头
    public static final String providerName = "HelloService#hello";


    public static void main(String[] args) throws InterruptedException {
        NettyClient client = new NettyClient();
        HelloService serviceProxy = (HelloService) client.getBean(HelloService.class, providerName);//拿到代理对象
//        for (; ; ) {
            //调用客户端的方法
//            Thread.sleep(2000);
            String result = serviceProxy.hello("阿昌来也");
            System.out.println("客户端调用服务端，结果为：" + result);
//        }
    }

}

