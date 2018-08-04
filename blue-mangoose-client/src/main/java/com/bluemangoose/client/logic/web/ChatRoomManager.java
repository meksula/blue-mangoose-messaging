package com.bluemangoose.client.logic.web;

import com.bluemangoose.client.model.dto.ChatRoom;

import java.util.List;

/**
 * @Author
 * Karol Meksuła
 * 26-07-2018
 * */

public interface ChatRoomManager {
    List<ChatRoom> fetchChatRoomList();

    boolean joinRoom(String roomId);

    void postMessage(String text) throws IllegalAccessException;

    boolean isConnected();

    void disconnect();

    String getRoomTarget();

}
