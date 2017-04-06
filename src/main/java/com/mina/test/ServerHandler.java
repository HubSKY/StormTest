package com.mina.test;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * Created by User on 2017/4/6.
 */
public class ServerHandler extends IoFilterAdapter implements IoHandler {
    private static ServerHandler samplMinaServerHandler = null;
    public ServerHandler() {
    }
    public void sessionOpened(IoSession session) throws Exception {

    }
    public void sessionClosed(IoSession session) {

    }
    public void messageReceived(IoSession session, Object message)throws Exception {//获取client 发过来的对象数据
        if (message instanceof TransferObject) {
            TransferObject obj = (TransferObject) message;
            System.out.println("接收到客户端的数据为："+obj.getDate());
        }
    }
    public void exceptionCaught(IoSession arg0, Throwable arg1)throws Exception {
    }
    public void messageSent(IoSession arg0, Object arg1) throws Exception {

    }
    public void sessionCreated(IoSession arg0) throws Exception {

    }
    public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {

    }
}
