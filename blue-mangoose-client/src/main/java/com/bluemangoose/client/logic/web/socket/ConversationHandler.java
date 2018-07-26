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
    private String roomTarget;
    private long capacity;
    private List<ChatMessage> messages;

    public ConversationHandler(String roomTarget, long capacity) {
        this.roomTarget = roomTarget;
        this.capacity = capacity;
        this.messages = new ArrayList<>();
    }

    public void insertMessage(ChatMessage chatMessage) {
        this.messages.add(chatMessage);
    }

    public ChatMessage peekLast() {
        return messages.get(messages.size() - 1);
    }

}
