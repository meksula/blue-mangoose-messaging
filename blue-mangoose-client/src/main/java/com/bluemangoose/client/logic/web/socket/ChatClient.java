package com.bluemangoose.client.logic.web.socket;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

/**
 * @Author
 * Karol Meksuła
 * 25-07-2018
 * */

public class ChatClient {
    private static String URL = "ws://51.38.129.50:8060/chat";
    private WebSocketClient client;
    private WebSocketStompClient stompClient;
    private MyStompSessionHandler sessionHandler;

    public ChatClient(String roomTarget) {
        this.client = new StandardWebSocketClient();
        this.stompClient = new WebSocketStompClient(client);
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        this.sessionHandler = new MyStompSessionHandler(roomTarget);
    }

    public MyStompSessionHandler connect() {
        stompClient.connect(URL, sessionHandler);
        stompClient.start();
        return sessionHandler;
    }

    public void disconnect() {
        stompClient.stop();
    }

}
