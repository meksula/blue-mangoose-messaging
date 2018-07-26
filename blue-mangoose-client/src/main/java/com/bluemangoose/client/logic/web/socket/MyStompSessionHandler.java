package com.bluemangoose.client.logic.web.socket;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

/**
* @Author
* Karol Meksuła
* 25-07-2018	
*/

public class MyStompSessionHandler extends StompSessionHandlerAdapter {
    private StompSession session;
    private String roomTarget;

    public MyStompSessionHandler(String roomTarget) {
        this.roomTarget = roomTarget;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        this.session = session;
        System.out.println("New session established : " + session.getSessionId());

        session.subscribe("/topic/" + roomTarget, this);
        System.out.println("Subscribed to /topic/" + roomTarget);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ChatMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        ChatMessage message = (ChatMessage) payload;
        System.out.println("Server: " + message.getContent());
    }

    public void postMessage(ChatMessage chatMessage) {
        this.session.send("/app/main", chatMessage);
    }

    public void changeRoomTarget(String roomTarget) {
        this.roomTarget = roomTarget;
    }

    public void disconect() {
        this.session.disconnect();
    }

}
