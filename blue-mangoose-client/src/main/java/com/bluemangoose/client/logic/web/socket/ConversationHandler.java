package com.bluemangoose.client.logic.web.socket;

import com.bluemangoose.client.controller.cache.CurrentChatCache;
import com.bluemangoose.client.logic.web.ApiPath;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnectorImpl;
import com.bluemangoose.client.model.dto.ChatAccess;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

    public void fetchLastMessages() throws IOException {
        ChatAccess chatAccess = CurrentChatCache.getInstance().getChatAccess();

        String json = new HttpServerConnectorImpl<>(String.class).post(chatAccess, ApiPath.MESSAGES_LAST);
        try {
            this.messages = new ObjectMapper().readValue(json, new TypeReference<List<ChatMessage>>(){});
        } catch (MismatchedInputException mie) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setUsernmame("ADMIN");
            chatMessage.setContent("NIE MASZ PRAW DO BYCIA W TYM POKOJU. WSZYSTKO POZOSTANIE UKRYTE.");
            this.messages = new ArrayList<>(Collections.singleton(chatMessage));
        }
    }

}
