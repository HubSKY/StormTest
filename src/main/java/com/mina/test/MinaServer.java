package com.mina.test;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by User on 2017/4/6.
 */
public class MinaServer {
    private static MinaServer minaServer = null;
    //创建一个非阻塞的Server端Socket
    private SocketAcceptor acceptor = new NioSocketAcceptor();
    //创建接收数据的过滤器
    private DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
    private int bindPort = 8888;
    //单例
    public static MinaServer getInstances() {
        if (null == minaServer) {
            minaServer = new MinaServer();
        }
        return minaServer;
    }
    private MinaServer() {
        //设定这个过滤器将按对象读取数据
        chain.addLast("myChin", new ProtocolCodecFilter(
                new ObjectSerializationCodecFactory()));
        //设定服务器端的消息处理器:一个MinaServerHandler对象,
        acceptor.setHandler(new ServerHandler());

        try {
            //绑定端口,启动服务器
            acceptor.bind(new InetSocketAddress(bindPort));

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("服务器监听端口为：   " + bindPort);
    }
    public static void main(String[] args) throws Exception {
        MinaServer.getInstances();
    }
}
