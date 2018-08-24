package com.bluemangoose.client.controller.cache;

import com.bluemangoose.client.logic.web.socket.ChatMessage;
import com.bluemangoose.client.model.dto.ChatAccess;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author
 * Karol Meksuła
 * 03-08-2018
 * */

@Getter
@Setter
public class CurrentChatCache {
    private static CurrentChatCache currentChatCache = new CurrentChatCache();

    private ChatAccess chatAccess;

    private CurrentChatCache() {}

    public static CurrentChatCache getInstance() {
        return currentChatCache;
    }

    private List<ChatMessage> chatMessageList = new ArrayList<>();
    private List<Label> messagesCache = new ArrayList<>();
}
