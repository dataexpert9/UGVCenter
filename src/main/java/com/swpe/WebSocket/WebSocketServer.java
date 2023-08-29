package com.swpe.WebSocket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WebSocket Server,include sendMessage,receive message
 */
@Component
@ServerEndpoint(value = "/socket/{name}")
public class WebSocketServer {

    //store online connection count
    private static AtomicInteger online = new AtomicInteger();

    //store username and connection
    private static Map<String, Session> sessionPools = new HashMap<>();


    /**
     * send Message
     *
     * @param session
     * @param message
     * @throws IOException
     */
    public static void sendMessage(Session session, String message) throws IOException {
        if (session != null) {
            session.getBasicRemote().sendText(message);
        }
    }


    /**
     * when connection successed
     *
     * @param session
     * @param userName
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "name") String userName) {
        sessionPools.put(userName, session);
        addOnlineCount();
        System.out.println(userName + "connect webSocket！online count is" + online);
    }

    /**
     * when connection need closed
     *
     * @param userName
     */
    @OnClose
    public void onClose(@PathParam(value = "name") String userName) {
        sessionPools.remove(userName);
        subOnlineCount();
        System.out.println(userName + "disconnect webSocket！online count is" + online);
    }

    /**
     * sendMessage to all Online user
     *
     * @param message
     * @throws IOException
     */
    @OnMessage
    public static void onMessage(String message) throws IOException {
        for (Session session : sessionPools.values()) {
            try {
                sendMessage(session, message);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * when error happened
     *
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("error");
        throwable.printStackTrace();
    }

    /**
     * send message to username
     *
     * @param userName
     * @param message
     * @throws IOException
     */
    public void sendInfo(String userName, String message) {
        Session session = sessionPools.get(userName);
        try {
            sendMessage(session, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void addOnlineCount() {
        online.incrementAndGet();
    }

    public static void subOnlineCount() {
        online.decrementAndGet();
    }

}