package com.mina.test;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

/**
 * Created by User on 2017/4/6.
 */
public class MinaClient {


    NioSocketConnector connector = new NioSocketConnector();
    //创建接收数据的过滤器
    DefaultIoFilterChainBuilder chain =  connector.getFilterChain();

    public void sendMessage(String mess){
        if(connector!=null){
            connector.setHandler(new ClientHandler(connector,mess));//by nafio用于彻底关闭客户端连接
            ConnectFuture cf = connector.connect(new InetSocketAddress("localhost",
                    8888));
        }
    }

    public MinaClient(String contentStr){
        //设定这个过滤器将按对象读取数据
        chain.addLast("myChin",new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        connector.setHandler(new ClientHandler(connector,contentStr));//by nafio用于彻底关闭客户端连接
        connector.setConnectTimeout(30);
        //连接服务器
        ConnectFuture cf = connector.connect(new InetSocketAddress("localhost",
                8888));
    }
    public static void main(String args[]) {
       MinaClient min= new MinaClient("hello");

        while(true) {
            try {
                BufferedReader strin = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("请输入一个字符串：");
                String str = strin.readLine();
                min.sendMessage(str);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
