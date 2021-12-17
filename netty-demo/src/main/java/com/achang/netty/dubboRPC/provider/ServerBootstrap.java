package com.achang.netty.dubboRPC.provider;

import com.achang.netty.dubboRPC.netty.NettyServer;

/******
 @author 阿昌
 @create 2021-12-17 21:05
 ******* 服务端启动器
 */
public class ServerBootstrap {
    public static void main(String[] args) throws InterruptedException {
        NettyServer.startServer("127.0.0.1",7000);
    }
}
