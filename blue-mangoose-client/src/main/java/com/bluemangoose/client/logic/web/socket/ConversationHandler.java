package com.bluemangoose.client.logic.web.socket;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author
 * Karol Meksuła
 * 26-07-2018
 * */

@Getter
@Setter
public class ConversationHandler {
    private static ConversationHandler conversationHandler = new ConversationHandler();
    private static String roomTarget;
    private static long capacity;
    private List<ChatMessage> messages;
    private WebsocketReceiver websocketReceiver;

    public static ConversationHandler getInstance(String roomTarget, long capacity) {
        ConversationHandler.roomTarget = roomTarget;
        ConversationHandler.capacity = capacity;
        return conversationHandler;
    }

    private ConversationHandler() {
        this.messages = new ArrayList<>();
    }

    public void insertMessage(ChatMessage chatMessage) {
        this.messages.add(chatMessage);
        websocketReceiver.actionOnMessage(chatMessage);
    }

    public ChatMessage peekLast() {
        return messages.get(messages.size() - 1);
    }

}
