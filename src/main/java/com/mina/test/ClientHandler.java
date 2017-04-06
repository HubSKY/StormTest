package com.mina.test;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * Created by User on 2017/4/6.
 */
public class ClientHandler extends IoHandlerAdapter{
    private static ClientHandler minaClientHangler =null;
    private static String contetn = null;
    NioSocketConnector connector;

    public ClientHandler(NioSocketConnector con,String contentstr){
        connector =con;
        contetn = contentstr;

    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        System.out.println("客户端:与服务端连接创建");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("客户端:打开了与服务端的会话");
        sendMsg(session);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("客户端:与服务端会话结束");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
        System.out.println("客户端:连接空闲");
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        System.out.println("客户端:接收到服务端返回信息");
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        System.out.println("客户端:发送对象完毕");
        session.close();//这里实际不能彻底关闭mina2.0需要connector.dispose()才能彻底关闭
       // connector.dispose();//用于关闭连接
       // System.out.println("连接关闭");
        }
    public void sendMsg(IoSession session) throws Exception{
        TransferObject transferObj=new TransferObject();
        transferObj.setDate(contetn);
        session.write(transferObj);
    }
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        super.exceptionCaught(session, cause);
    }
}
