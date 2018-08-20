package com.bluemangoose.client.controller.cache;

import com.bluemangoose.client.logic.web.socket.ChatMessage;
import com.bluemangoose.client.model.dto.ChatAccess;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author
 * Karol Meksuła
 * 03-08-2018
 * */

public class CurrentChatCache {
    private static CurrentChatCache currentChatCache = new CurrentChatCache();

    private ChatAccess chatAccess;

    private CurrentChatCache() {}

    public static CurrentChatCache getInstance() {
        return currentChatCache;
    }

    private List<ChatMessage> chatMessageList = new ArrayList<>();

    public void setChatAccess(ChatAccess chatAccess) {
        this.chatAccess = chatAccess;
    }

    public ChatAccess getChatAccess() {
        return chatAccess;
    }

    public List<ChatMessage> getChatMessageList() {
        return chatMessageList;
    }

    public void setChatMessageList(List<ChatMessage> chatMessageList) {
        this.chatMessageList = chatMessageList;
    }

}
