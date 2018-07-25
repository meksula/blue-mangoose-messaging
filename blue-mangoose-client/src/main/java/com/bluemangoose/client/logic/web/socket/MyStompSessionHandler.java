package com.bluemangoose.client.logic.web.socket;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("New session established : " + session.getSessionId());
        session.subscribe("/topic/chat", this);
        System.out.println("Subscribed to /topic/greetings");
        session.send("/app/main", getSampleMessage());
        System.out.println("Message sent to websocket server");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Hello.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println("Reveived" + payload.toString());
    }

    /**
     * A sample message instance.
     * @return instance of <code>Message</code>
     */
    private Message getSampleMessage() {
        return new Message("karoladmin", "Hello");
    }
}

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class Hello {
    String welcome = "Działa.";
    String name = "server";

    @Override
    public String toString() {
        return welcome + " " + name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public String getName() {
        return name;
    }

    public String getWelcome() {
        return welcome;
    }
}