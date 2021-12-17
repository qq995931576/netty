package com.achang.netty.dubboRPC.provider;

import com.achang.netty.dubboRPC.publicinterface.HelloService;

/******
 @author 阿昌
 @create 2021-12-17 21:03
 *******
 */
public class HelloServiceImpl implements HelloService {
    private static int count = 0;

    @Override
    public String hello(String message) {
        System.out.println("客户端发来的消息为：【"+message+"】");
        if (message!=null){
            return "你好客户端，服务端已经收到了消息"+"调用次数为：【"+(++count)+"】";
        }else {
            return "消息不能为空";
        }
    }
}
