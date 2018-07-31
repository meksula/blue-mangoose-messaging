package com.meksula.chat.domain.chat;

import com.meksula.chat.domain.chat.dto.Message;
import com.meksula.chat.domain.room.ChatForm;
import com.meksula.chat.domain.room.ChatRoom;

import java.util.List;
import java.util.Map;

/**
 * @Author
 * Karol Meksuła
 * 26-07-2018
 * */

public interface ChatRoomManager {
    void receiveMessage(Message message);

    ChatRoom registerChatWrapper(ChatForm chatForm);

    void removeChatWrapper(String name);

    List<ChatRoom> getRoomSet();

    Map<String, ChatWrapper> getChatMap();
}
