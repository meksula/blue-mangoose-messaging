package com.bluemangoose.client.logic.web.socket;

/**
 * @Author
 * Karol Meksuła
 * 27-07-2018
 * */

public interface WebsocketReceiver {
    void actionOnMessage(ChatMessage chatMessage);
}
