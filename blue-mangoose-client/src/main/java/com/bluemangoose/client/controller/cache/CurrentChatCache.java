package com.bluemangoose.client.controller.cache;

import com.bluemangoose.client.model.dto.ChatAccess;

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

    public void setChatAccess(ChatAccess chatAccess) {
        this.chatAccess = chatAccess;
    }

    public ChatAccess getChatAccess() {
        return chatAccess;
    }

}
