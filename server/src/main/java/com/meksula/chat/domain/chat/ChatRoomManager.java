package com.meksula.chat.domain.chat;

import com.meksula.chat.domain.room.ChatRoom;

import java.util.List;

/**
 * @Author
 * Karol Meksuła
 * 26-07-2018
 * */

public interface ChatRoomManager {
    List<ChatRoom> getRoomSet();
}
